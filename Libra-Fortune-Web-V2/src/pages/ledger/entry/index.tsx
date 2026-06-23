import {
  CloseOutlined,
  DeleteOutlined,
  DragOutlined,
  EditOutlined,
  PlusOutlined,
  RollbackOutlined,
} from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormDatePicker,
  ProFormDigit,
  type ProFormInstance,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import { history, useParams } from '@umijs/max';
import {
  AutoComplete,
  Button,
  Form,
  InputNumber,
  message,
  Popconfirm,
  Select,
  Space,
  Switch,
  Table,
  Tag,
} from 'antd';
import dayjs, { type Dayjs } from 'dayjs';
import React, {
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
} from 'react';
import * as accountApi from '@/services/libra-fortune/account/account';
import * as entryApi from '@/services/libra-fortune/ledger/entry';
import { get as getLedger } from '@/services/libra-fortune/ledger/ledger';
import * as categoryApi from '@/services/libra-fortune/metadata/category';
import * as currencyApi from '@/services/libra-fortune/metadata/currency';
import * as tagSetApi from '@/services/libra-fortune/metadata/tag-set';

type LedgerEntryFormValues = Omit<
  Partial<LibraFortune.Ledger.LedgerEntryDTO>,
  'date' | 'details' | 'tags'
> & {
  date?: string | Dayjs;
  details?: LedgerEntryDetailFormValues[];
  tagIds?: number[];
};

type LedgerEntryDetailFormValues =
  Partial<LibraFortune.Ledger.LedgerEntryDetailDTO> & {
    allocationLock?: 'amount' | 'ratio';
  };

type Option<T extends string | number = string | number> = {
  label: string;
  value: T;
};

type GroupedTagOption = {
  allowMultiple: boolean;
  label: string;
  name: string;
  options: Option<number>[];
  required: boolean;
};

type CategoryTagSelectorProps = {
  categories: GalaxyWeb.EnumDTO<number>[];
  value?: number[];
  onChange?: (value?: number[]) => void;
};

type TagSetTagSelectorProps = {
  tagSets: GroupedTagOption[];
  value?: number[];
  onChange?: (value?: number[]) => void;
};

type PaymentChainEditorProps = {
  accountOptions: Option<number>[];
  onChange?: (value?: number[]) => void;
  value?: number[];
};

type LedgerEntrySearchParams = LibraFortune.Ledger.LedgerEntryQuery & {
  dateRange?: string[];
  name?: string;
  tagId?: number[];
};

type DecimalInputValue = string | null;

const toDateString = (value: string | Dayjs): string =>
  typeof value === 'string' ? value : value.format('YYYY-MM-DD');

const isEmptyFormValue = (value: unknown): boolean =>
  value === undefined || value === null || value === '';

const sortTagIds = (
  tagIds: number[],
  tagSets: GroupedTagOption[],
): number[] => {
  const knownTagIds = new Set<number>();
  const sortedTagIds = tagSets.flatMap((tagSet) =>
    tagSet.options.flatMap((option) => {
      knownTagIds.add(option.value);
      return tagIds.includes(option.value) ? [option.value] : [];
    }),
  );

  return [
    ...sortedTagIds,
    ...tagIds.filter((tagId) => !knownTagIds.has(tagId)),
  ];
};

const amountToCents = (value: string): bigint | undefined => {
  const match = /^(\d+)(?:\.(\d{1,2}))?$/.exec(value);
  if (!match) return undefined;
  return BigInt(match[1]) * 100n + BigInt((match[2] ?? '').padEnd(2, '0'));
};

const centsToAmount = (value: bigint): string =>
  `${value / 100n}.${(value % 100n).toString().padStart(2, '0')}`;

const ratioUnitsFromCents = (amount: bigint, total: bigint): bigint =>
  (amount * 10000n + total / 2n) / total;

const ratioUnitsToString = (value: bigint): string =>
  `${value / 100n}.${(value % 100n).toString().padStart(2, '0')}`;

const ratioToUnits = (value: string): bigint | undefined => {
  const match = /^(\d+)(?:\.(\d{1,2}))?$/.exec(value);
  if (!match) return undefined;
  return BigInt(match[1]) * 100n + BigInt((match[2] ?? '').padEnd(2, '0'));
};

const flattenCategoryOptions = (
  records: GalaxyWeb.EnumDTO<number>[],
  prefix = '',
): Option<number>[] =>
  records.flatMap((record) => {
    const label = `${prefix}${record.label}`;
    return [
      {
        label,
        value: record.value,
      },
      ...flattenCategoryOptions(record.children ?? [], `${label} / `),
    ];
  });

const flattenCategoryQueryOptions = (
  records: GalaxyWeb.EnumDTO<number>[],
  prefix = '',
): Option<string>[] =>
  records.flatMap((record) => {
    const label = `${prefix}${record.label}`;
    return [
      {
        label,
        value: String(record.value),
      },
      ...(record.children ?? []).map((child) => ({
        label: `${label} / ${child.label}`,
        value: `${record.value},${child.value}`,
      })),
    ];
  });

const findParentCategoryId = (
  categories: GalaxyWeb.EnumDTO<number>[],
  value?: number[],
): number | undefined => {
  const leafCategoryId = value?.at(-1);
  if (!leafCategoryId) return undefined;
  for (const category of categories) {
    if (category.value === leafCategoryId) {
      return category.value;
    }
    if (category.children?.some((child) => child.value === leafCategoryId)) {
      return category.value;
    }
  }
  return undefined;
};

const CategoryTagSelector: React.FC<CategoryTagSelectorProps> = ({
  categories,
  value,
  onChange,
}) => {
  const [selectedParentId, setSelectedParentId] = useState<number | undefined>(
    () => findParentCategoryId(categories, value),
  );

  useEffect(() => {
    const parentId = findParentCategoryId(categories, value);
    if (parentId) {
      setSelectedParentId(parentId);
      return;
    }
    setSelectedParentId((oldParentId) =>
      oldParentId &&
      categories.some((category) => category.value === oldParentId)
        ? oldParentId
        : undefined,
    );
  }, [categories, value]);

  const selectedParent = categories.find(
    (category) => category.value === selectedParentId,
  );

  const selectParent = (category: GalaxyWeb.EnumDTO<number>) => {
    setSelectedParentId(category.value);
    if (!category.children?.length) {
      onChange?.([category.value]);
    } else if (
      value?.[0] === category.value ||
      !category.children.some((child) => child.value === value?.at(-1))
    ) {
      onChange?.(undefined);
    }
  };

  return (
    <Space direction="vertical" size={8}>
      <Space size={[0, 8]} wrap>
        {categories.map((category) => (
          <Tag.CheckableTag
            key={category.value}
            checked={selectedParentId === category.value}
            onChange={() => selectParent(category)}
          >
            {category.label}
          </Tag.CheckableTag>
        ))}
      </Space>
      {selectedParent?.children?.length ? (
        <Space size={[0, 8]} wrap>
          {selectedParent.children.map((category) => (
            <Tag.CheckableTag
              key={category.value}
              checked={value?.at(-1) === category.value}
              onChange={() =>
                onChange?.([selectedParent.value, category.value])
              }
            >
              {category.label}
            </Tag.CheckableTag>
          ))}
        </Space>
      ) : null}
    </Space>
  );
};

const PaymentChainEditor: React.FC<PaymentChainEditorProps> = ({
  accountOptions,
  onChange,
  value,
}) => {
  const paymentChain = value ?? [];
  const [draggedIndex, setDraggedIndex] = useState<number>();
  const [selectedAccountId, setSelectedAccountId] = useState<number | null>(
    null,
  );
  const accountNameMap = new Map(
    accountOptions.map((option) => [option.value, option.label]),
  );

  const moveByDrag = (targetIndex: number) => {
    if (draggedIndex === undefined || draggedIndex === targetIndex) {
      return;
    }
    const nextValue = [...paymentChain];
    const [accountId] = nextValue.splice(draggedIndex, 1);
    nextValue.splice(targetIndex, 0, accountId);
    onChange?.(nextValue);
    setDraggedIndex(undefined);
  };

  return (
    <Space direction="vertical" size={8} style={{ width: '100%' }}>
      <Select
        allowClear
        options={accountOptions.map((option) => ({
          ...option,
          disabled: paymentChain.includes(option.value),
        }))}
        placeholder="选择账户并追加到付款链尾部"
        value={selectedAccountId}
        onChange={(accountId: number) => {
          if (!paymentChain.includes(accountId)) {
            onChange?.([...paymentChain, accountId]);
          }
          setSelectedAccountId(null);
        }}
      />
      {paymentChain.length > 0 && (
        <Space size={4} wrap>
          {paymentChain.map((accountId, index) => (
            <Tag
              key={accountId}
              draggable
              icon={<DragOutlined />}
              style={{
                cursor: 'grab',
                opacity: draggedIndex === index ? 0.5 : 1,
              }}
              onDragEnd={() => setDraggedIndex(undefined)}
              onDragOver={(event) => event.preventDefault()}
              onDragStart={(event) => {
                event.dataTransfer.effectAllowed = 'move';
                event.dataTransfer.setData('text/plain', String(accountId));
                setDraggedIndex(index);
              }}
              onDrop={() => moveByDrag(index)}
            >
              <Space size={2}>
                {accountNameMap.get(accountId) ?? `账户 #${accountId}`}
                <Button
                  aria-label="移除账户"
                  danger
                  icon={<CloseOutlined />}
                  size="small"
                  type="text"
                  onClick={() =>
                    onChange?.(
                      paymentChain.filter(
                        (_, chainIndex) => chainIndex !== index,
                      ),
                    )
                  }
                />
              </Space>
            </Tag>
          ))}
        </Space>
      )}
    </Space>
  );
};

const TagSetTagSelector: React.FC<TagSetTagSelectorProps> = ({
  tagSets,
  value,
  onChange,
}) => {
  const selectedTagIds = value ?? [];

  const toggleTag = (tagSet: GroupedTagOption, tagId: number) => {
    if (selectedTagIds.includes(tagId)) {
      onChange?.(
        selectedTagIds.filter((selectedTagId) => selectedTagId !== tagId),
      );
      return;
    }

    if (tagSet.allowMultiple) {
      onChange?.([...selectedTagIds, tagId]);
      return;
    }

    const tagSetItemIds = new Set(tagSet.options.map((option) => option.value));
    onChange?.([
      ...selectedTagIds.filter(
        (selectedTagId) => !tagSetItemIds.has(selectedTagId),
      ),
      tagId,
    ]);
  };

  return (
    <Space direction="vertical" size={10}>
      {tagSets.map((tagSet) => (
        <Space key={tagSet.label} size={[8, 8]} wrap>
          <span>{tagSet.label}</span>
          {tagSet.options.map((option) => (
            <Tag.CheckableTag
              key={option.value}
              checked={selectedTagIds.includes(option.value)}
              onChange={() => toggleTag(tagSet, option.value)}
            >
              {option.label}
            </Tag.CheckableTag>
          ))}
        </Space>
      ))}
    </Space>
  );
};

const LedgerEntry: React.FC = () => {
  const params = useParams();
  const ledgerId = useMemo(() => {
    const value = Number(params.ledgerId);
    return Number.isInteger(value) && value > 0 ? value : undefined;
  }, [params.ledgerId]);

  const [messageApi, contextHolder] = message.useMessage();
  const [modalVisible, setModalVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<
    LibraFortune.Ledger.LedgerEntryDTO | undefined
  >(undefined);
  const [ledger, setLedger] = useState<
    LibraFortune.Ledger.LedgerDTO | undefined
  >(undefined);
  const [categories, setCategories] = useState<GalaxyWeb.EnumDTO<number>[]>([]);
  const [categoryOptions, setCategoryOptions] = useState<Option<number>[]>([]);
  const [categoryQueryOptions, setCategoryQueryOptions] = useState<
    Option<string>[]
  >([]);
  const [currencyOptions, setCurrencyOptions] = useState<Option<string>[]>([]);
  const [tagSetOptions, setTagSetOptions] = useState<GroupedTagOption[]>([]);
  const [accountOptions, setAccountOptions] = useState<Option<number>[]>([]);
  const [datePickerDefaultValue, setDatePickerDefaultValue] = useState<
    Dayjs | undefined
  >(undefined);
  const [form] = Form.useForm<LedgerEntryFormValues>();
  const detailValues = Form.useWatch('details', form) ?? [];
  const actionRef = useRef<ActionType | null>(null);
  const searchFormRef =
    useRef<ProFormInstance<LedgerEntrySearchParams>>(undefined);
  const calculateTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const originalAmountAutoFilledRef = useRef(false);
  const continuousEntryRef = useRef(false);

  const memberOptions = useMemo(
    () =>
      (ledger?.members ?? []).map((member) => ({
        label: member.username,
        value: member.username,
      })),
    [ledger?.members],
  );

  const defaultDetails = useMemo(
    () =>
      (ledger?.members ?? []).map((member) => ({
        username: member.username,
        fundedRatio: member.defaultFundedRatio,
        amount: '',
      })),
    [ledger?.members],
  );

  const resetEntryForm = useCallback(
    (details: LedgerEntryDetailFormValues[] = defaultDetails) => {
      originalAmountAutoFilledRef.current = false;
      form.resetFields();
      form.setFieldsValue({
        originalCurrency: currencyOptions[0]?.value,
        settlementCurrency: currencyOptions[0]?.value,
        tagIds: [],
        details: details.map(({ username, paymentChain, fundedRatio }) => ({
          username,
          paymentChain,
          fundedRatio,
          amount: '',
        })),
      });
    },
    [currencyOptions, defaultDetails, form],
  );

  const tagNameMap = useMemo(() => {
    const result = new Map<number, string>();
    for (const group of tagSetOptions) {
      for (const option of group.options) {
        result.set(option.value, option.label);
      }
    }
    return result;
  }, [tagSetOptions]);

  const accountNameMap = useMemo(
    () => new Map(accountOptions.map((option) => [option.value, option.label])),
    [accountOptions],
  );

  const categoryNameMap = useMemo(
    () =>
      new Map(categoryOptions.map((option) => [option.value, option.label])),
    [categoryOptions],
  );

  useEffect(() => {
    if (!ledgerId) {
      history.replace('/ledger/ledger');
      return;
    }

    Promise.all([
      getLedger(ledgerId),
      categoryApi.enums(),
      currencyApi.enums(),
      tagSetApi.list({ current: 1, pageSize: 1000, noPage: true }),
      accountApi.list({ current: 1, pageSize: 1000, noPage: true }),
    ])
      .then(
        ([
          ledgerResponse,
          categoryResponse,
          currencyResponse,
          tagSetResponse,
          accountResponse,
        ]) => {
          setLedger(ledgerResponse.data);
          setCategories(categoryResponse.data);
          setCategoryOptions(flattenCategoryOptions(categoryResponse.data));
          setCategoryQueryOptions(
            flattenCategoryQueryOptions(categoryResponse.data),
          );
          setCurrencyOptions(currencyResponse.data);
          setTagSetOptions(
            tagSetResponse.data.list.map((tagSet) => ({
              allowMultiple: tagSet.allowMultiple,
              label: `${tagSet.name}${tagSet.required ? '（必选）' : ''}`,
              name: tagSet.name,
              options: (tagSet.items ?? []).map((item) => ({
                label: item.name,
                value: item.id!,
              })),
              required: tagSet.required,
            })),
          );
          setAccountOptions(
            accountResponse.data.list.map((account) => ({
              label: account.name,
              value: account.id!,
            })),
          );
        },
      )
      .catch(() => {
        history.replace('/ledger/ledger');
      });
  }, [ledgerId]);

  useEffect(() => {
    if (!modalVisible) return;

    if (currentRecord) {
      originalAmountAutoFilledRef.current = false;
      form.setFieldsValue({
        ...currentRecord,
        date: currentRecord.date ? dayjs(currentRecord.date) : undefined,
        tagIds: currentRecord.tags?.map((tag) => tag.tagId) ?? [],
        details: currentRecord.details ?? [],
      });
    } else {
      resetEntryForm();
    }
  }, [modalVisible, currentRecord, form, resetEntryForm]);

  const recalculateFundedAmounts = useCallback(() => {
    const settlementAmount = form.getFieldValue('settlementAmount');
    const details = (form.getFieldValue('details') ??
      []) as LedgerEntryDetailFormValues[];
    if (details.length === 0) return;
    if (!settlementAmount) {
      form.setFieldValue(
        'details',
        details.map((detail) => ({
          ...detail,
          amount: '',
        })),
      );
      return;
    }

    const settlementCents = amountToCents(settlementAmount);
    if (settlementCents === undefined || settlementCents === 0n) return;

    /*
     * 1. 先兑现锁定行：比例锁定行由比例反算金额；金额锁定行保留手动金额。
     *    后续的均分只会作用于未锁定行，绝不覆盖用户的手动输入。
     */
    const amounts = details.map((detail) => {
      if (detail.allocationLock === 'ratio') {
        const ratio = detail.fundedRatio
          ? ratioToUnits(detail.fundedRatio)
          : undefined;
        return ratio === undefined
          ? undefined
          : (settlementCents * ratio + 5000n) / 10000n;
      }
      if (detail.allocationLock === 'amount') {
        return detail.amount ? amountToCents(detail.amount) : undefined;
      }
      return undefined;
    });
    if (
      details.some(
        (detail, index) =>
          detail.allocationLock && amounts[index] === undefined,
      )
    ) {
      messageApi.open({
        type: 'error',
        content: '锁定的承担比例或金额不合法',
      });
      return;
    }

    const lockedAmount = amounts.reduce<bigint>(
      (total, amount) => total + (amount ?? 0n),
      0n,
    );
    if (lockedAmount > settlementCents) {
      messageApi.open({
        type: 'error',
        content: '锁定的承担金额不能超过结算金额',
      });
      return;
    }

    /* 2. 未锁定行均分余额；按分取整后，把所有余分交给第一位未锁定人。 */
    const unlockedIndexes = details.flatMap((detail, index) =>
      detail.allocationLock ? [] : [index],
    );
    const remaining = settlementCents - lockedAmount;
    if (unlockedIndexes.length === 0 && remaining !== 0n) {
      messageApi.open({
        type: 'error',
        content: '锁定金额之和必须等于结算金额',
      });
      return;
    }
    const average = unlockedIndexes.length
      ? remaining / BigInt(unlockedIndexes.length)
      : 0n;
    const remainder = unlockedIndexes.length
      ? remaining % BigInt(unlockedIndexes.length)
      : 0n;
    unlockedIndexes.forEach((index, position) => {
      amounts[index] = average + (position === 0 ? remainder : 0n);
    });

    /*
     * 3. 金额确定后，统一反算比例。比例保留两位小数；最后把舍入差额交给
     *    第一位未锁定人（没有未锁定人时交给第一行），以保证总计恰好为 100.00%。
     */
    const ratios = amounts.map((amount) =>
      ratioUnitsFromCents(amount ?? 0n, settlementCents),
    );
    const ratioAdjustmentIndex = unlockedIndexes[0] ?? 0;
    ratios[ratioAdjustmentIndex] +=
      10000n - ratios.reduce((total, ratio) => total + ratio, 0n);
    form.setFieldValue(
      'details',
      details.map((detail, index) => ({
        ...detail,
        amount: centsToAmount(amounts[index] ?? 0n),
        fundedRatio: ratioUnitsToString(ratios[index]),
      })),
    );
  }, [form, messageApi]);

  const scheduleRecalculateFundedAmounts = useCallback(() => {
    if (calculateTimerRef.current) {
      clearTimeout(calculateTimerRef.current);
    }
    calculateTimerRef.current = setTimeout(() => {
      recalculateFundedAmounts();
    }, 0);
  }, [recalculateFundedAmounts]);

  const onSettlementAmountChange = useCallback(
    (value: DecimalInputValue) => {
      const originalCurrency = form.getFieldValue('originalCurrency');
      const settlementCurrency = form.getFieldValue('settlementCurrency');
      const originalAmount = form.getFieldValue('originalAmount');

      if (
        originalCurrency &&
        originalCurrency === settlementCurrency &&
        (isEmptyFormValue(originalAmount) ||
          originalAmountAutoFilledRef.current)
      ) {
        form.setFieldValue('originalAmount', value ?? undefined);
        originalAmountAutoFilledRef.current = !isEmptyFormValue(value);
      }

      scheduleRecalculateFundedAmounts();
    },
    [form, scheduleRecalculateFundedAmounts],
  );

  const onOriginalAmountChange = useCallback(() => {
    originalAmountAutoFilledRef.current = false;
  }, []);

  useEffect(
    () => () => {
      if (calculateTimerRef.current) {
        clearTimeout(calculateTimerRef.current);
      }
    },
    [],
  );

  const validateRequiredTagSets = useCallback(
    async (_: unknown, value?: number[]) => {
      const selectedTagIds = new Set(value ?? []);
      const missingTagSet = tagSetOptions.find(
        (tagSet) =>
          tagSet.required &&
          !tagSet.options.some((option) => selectedTagIds.has(option.value)),
      );
      if (missingTagSet) {
        throw new Error(`请选择${missingTagSet.name}`);
      }
    },
    [tagSetOptions],
  );

  const columns: ProColumns<LibraFortune.Ledger.LedgerEntryDTO>[] = [
    {
      dataIndex: 'date',
      title: '日期',
      valueType: 'date',
      width: 100,
      fixed: 'left',
      search: false,
    },
    {
      dataIndex: 'dateRange',
      title: '日期',
      valueType: 'dateRange',
      hideInTable: true,
      search: {
        transform: (value: string[]) => ({
          dateBegin: value?.[0],
          dateEnd: value?.[1],
        }),
      },
    },
    {
      dataIndex: 'name',
      title: '条目名称',
      width: 120,
      fixed: 'left',
      copyable: true,
    },
    {
      dataIndex: 'category',
      title: '分类',
      valueType: 'select',
      fieldProps: {
        options: categoryQueryOptions,
      },
      renderText: (_, record) =>
        categoryNameMap.get(record.category.at(-1)!) ?? record.category.at(-1),
      width: 100,
    },
    {
      dataIndex: 'originalAmount',
      title: '原始金额',
      align: 'right',
      search: false,
      width: 100,
      render: (_, record) => (
        <span>
          {record.originalAmount} {record.originalCurrency}
        </span>
      ),
    },
    {
      dataIndex: 'settlementAmount',
      title: '结算金额',
      align: 'right',
      search: false,
      width: 100,
      render: (_, record) => (
        <span>
          {record.settlementAmount} {record.settlementCurrency}
        </span>
      ),
    },
    {
      dataIndex: 'tagIdIn',
      title: '标签',
      valueType: 'select',
      hideInTable: true,
      fieldProps: {
        mode: 'multiple',
        options: tagSetOptions,
      },
    },
    {
      dataIndex: 'tags',
      title: '标签',
      search: false,
      width: 220,
      render: (_, record) => {
        const tags = record.tags ?? [];
        if (tags.length === 0) return <span>-</span>;
        return (
          <Space size={[0, 4]} wrap>
            {tags.map((tag) => (
              <Tag key={`${record.id}-${tag.tagId}`}>
                {tagNameMap.get(tag.tagId) ?? tag.tagId}
              </Tag>
            ))}
          </Space>
        );
      },
    },
    {
      dataIndex: 'details',
      title: '分担明细',
      search: false,
      width: 200,
      render: (_, record) => {
        const details = record.details ?? [];
        if (details.length === 0) return <span>-</span>;
        return (
          <Space size={[0, 4]} wrap>
            {details.map((detail) => (
              <Tag key={`${record.id}-${detail.username}`}>
                {detail.username} {detail.amount} / {detail.fundedRatio}%
              </Tag>
            ))}
          </Space>
        );
      },
    },
    {
      key: 'paymentChain',
      title: '付款链',
      search: false,
      width: 280,
      render: (_, record) => {
        const detailsWithPaymentChain = (record.details ?? []).filter(
          (detail) => detail.paymentChain?.length,
        );
        if (detailsWithPaymentChain.length === 0) return <span>-</span>;
        return (
          <Space size={[0, 4]} wrap>
            {detailsWithPaymentChain.map((detail) => (
              <Tag key={`${record.id}-${detail.username}`}>
                {detail.username}：
                {(detail.paymentChain ?? [])
                  .map(
                    (accountId) =>
                      accountNameMap.get(accountId) ?? `账户 #${accountId}`,
                  )
                  .join(' → ')}
              </Tag>
            ))}
          </Space>
        );
      },
    },
    {
      key: 'action',
      title: '操作',
      fixed: 'right',
      valueType: 'option',
      width: 180,
      render: (_, record) => (
        <Space>
          <Button
            icon={<EditOutlined />}
            onClick={() => onUpdateButtonClick(record)}
          >
            编辑
          </Button>
          <Popconfirm
            title="确认删除该条目？"
            okText="删除"
            cancelText="取消"
            onConfirm={() => onRemoveButtonClick(record.id!)}
          >
            <Button icon={<DeleteOutlined />} danger>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const onRequest: NonNullable<
    ProTableProps<
      LibraFortune.Ledger.LedgerEntryDTO,
      LedgerEntrySearchParams
    >['request']
  > = async (params) => {
    if (!ledgerId) {
      return { data: [], success: false, total: 0 };
    }
    const { pageSize, current, name, dateBegin, dateEnd, ...query } = params;
    const response = await entryApi.list(ledgerId, {
      current: current!,
      pageSize: pageSize!,
      noPage: false,
      nameLike: name,
      dateBegin,
      dateEnd,
      ...query,
    });

    return {
      data: response.data.list,
      success: response.code < 1000,
      total: response.data.total,
    };
  };

  const onFinish = async (values: LedgerEntryFormValues): Promise<boolean> => {
    if (!ledgerId) return false;
    const isContinuousEntry = continuousEntryRef.current;
    continuousEntryRef.current = false;
    try {
      const record: LibraFortune.Ledger.LedgerEntryDTO = {
        id: values.id,
        ledgerId,
        category: values.category!,
        date: toDateString(values.date!),
        name: values.name!,
        originalAmount: values.originalAmount!,
        originalCurrency: values.originalCurrency!,
        settlementAmount: values.settlementAmount!,
        settlementCurrency: values.settlementCurrency!,
        remark: values.remark,
        tags: sortTagIds(values.tagIds ?? [], tagSetOptions).map((tagId) => ({
          tagId,
        })),
        details: (values.details ?? []).map(
          ({ allocationLock: _, ...detail }) => detail,
        ) as LibraFortune.Ledger.LedgerEntryDetailDTO[],
      };
      const fn = record.id ? entryApi.update : entryApi.create;
      await fn(ledgerId, record);
      actionRef.current?.reload();
      messageApi.open({
        type: 'success',
        content: record.id ? '更新成功' : '新增成功',
      });
      if (isContinuousEntry) {
        setCurrentRecord(undefined);
        resetEntryForm(values.details);
        return false;
      }
      return true;
    } catch {
      return false;
    }
  };

  const onCreateButtonClick = () => {
    const dateRange = searchFormRef.current?.getFieldValue('dateRange');
    setDatePickerDefaultValue(dateRange?.[0] ? dayjs(dateRange[0]) : undefined);
    setCurrentRecord(undefined);
    setModalVisible(true);
  };

  const onClearButtonClick = () => {
    setCurrentRecord(undefined);
    resetEntryForm();
  };

  const onContinuousEntryButtonClick = async () => {
    try {
      await form.validateFields();
      continuousEntryRef.current = true;
      form.submit();
    } catch {
      // 表单校验失败时，由表单展示字段错误。
    }
  };

  const onUpdateButtonClick = async (
    record: LibraFortune.Ledger.LedgerEntryDTO,
  ) => {
    if (!ledgerId || !record.id) return;
    const response = await entryApi.get(ledgerId, record.id);
    setCurrentRecord(response.data);
    setModalVisible(true);
  };

  const onRemoveButtonClick = (id: number) => {
    if (!ledgerId) return;
    entryApi.remove(ledgerId, id).then(() => {
      actionRef.current?.reload();
      messageApi.open({
        type: 'success',
        content: '删除成功',
      });
    });
  };

  return (
    <PageContainer
      title={ledger ? `账本条目：${ledger.name}` : '账本条目'}
      extra={[
        <Button
          key="back"
          icon={<RollbackOutlined />}
          onClick={() => history.push('/ledger/ledger')}
        >
          返回账本
        </Button>,
      ]}
    >
      {contextHolder}
      <ProTable<LibraFortune.Ledger.LedgerEntryDTO, LedgerEntrySearchParams>
        actionRef={actionRef}
        columns={columns}
        defaultSize="small"
        formRef={searchFormRef}
        rowKey="id"
        request={onRequest}
        search={{
          labelWidth: 'auto',
        }}
        scroll={{ x: 1340 }}
        toolBarRender={() => [
          <Button
            key="create"
            type="primary"
            icon={<PlusOutlined />}
            onClick={onCreateButtonClick}
          >
            新增条目
          </Button>,
        ]}
      />
      <ModalForm<LedgerEntryFormValues>
        form={form}
        title={currentRecord?.id ? '编辑条目' : '新增条目'}
        open={modalVisible}
        onOpenChange={(open) => {
          setModalVisible(open);
          if (!open) {
            setCurrentRecord(undefined);
          }
        }}
        onFinish={onFinish}
        submitter={{
          searchConfig: {
            submitText: currentRecord?.id ? '保存' : '记账',
          },
          render: (_, dom) => {
            if (currentRecord?.id) {
              return [dom[0], dom[1]];
            }
            return [
              dom[0],
              <Button key="clear" onClick={onClearButtonClick}>
                清空
              </Button>,
              dom[1],
              <Button
                key="continuous-entry"
                onClick={onContinuousEntryButtonClick}
              >
                连续记账
              </Button>,
            ];
          },
        }}
        modalProps={{
          destroyOnHidden: true,
          width: 1200,
        }}
      >
        <ProFormText name="id" label="ID" hidden />
        <Space align="baseline">
          <ProFormDatePicker
            name="date"
            label="交易日期"
            rules={[{ required: true }]}
            fieldProps={{
              defaultPickerValue: datePickerDefaultValue,
              format: 'YYYY-MM-DD',
            }}
          />
          <ProFormText
            name="name"
            label="条目名称"
            placeholder="请输入条目名称"
            fieldProps={{
              style: {
                width: 320,
              },
            }}
            rules={[{ required: true }]}
          />
        </Space>
        <Form.Item
          name="category"
          label="分类"
          rules={[{ required: true, message: '请选择分类' }]}
        >
          <CategoryTagSelector categories={categories} />
        </Form.Item>
        <Space align="baseline">
          <ProFormDigit
            name="originalAmount"
            label="原始消费金额"
            min={0}
            fieldProps={{
              onChange: onOriginalAmountChange,
              precision: 2,
              stringMode: true,
              step: '0.01',
            }}
            rules={[{ required: true }]}
          />
          <ProFormSelect
            name="originalCurrency"
            label="原始消费货币"
            options={currencyOptions}
            rules={[{ required: true }]}
          />
          <ProFormDigit
            name="settlementAmount"
            label="结算金额"
            min={0}
            fieldProps={{
              onChange: (value) =>
                onSettlementAmountChange(value as unknown as DecimalInputValue),
              precision: 2,
              stringMode: true,
              step: '0.01',
            }}
            rules={[{ required: true }]}
          />
          <ProFormSelect
            name="settlementCurrency"
            label="结算货币"
            options={currencyOptions}
            rules={[{ required: true }]}
          />
        </Space>
        <Form.Item
          name="tagIds"
          label="标签"
          rules={[{ validator: validateRequiredTagSets }]}
        >
          <TagSetTagSelector tagSets={tagSetOptions} />
        </Form.Item>
        <Form.Item label="分担明细" required>
          <Form.List name="details">
            {(fields, { add, remove }) => (
              <Space direction="vertical" style={{ width: '100%' }}>
                <Table
                  columns={[
                    {
                      dataIndex: 'username',
                      title: '分担人',
                      width: 180,
                      render: (_, field) => (
                        <>
                          <Form.Item name={[field.name, 'id']} hidden />
                          <Form.Item
                            name={[field.name, 'allocationLock']}
                            hidden
                          />
                          <Form.Item
                            name={[field.name, 'username']}
                            rules={[{ required: true }]}
                            style={{ marginBottom: 0 }}
                          >
                            <AutoComplete
                              options={memberOptions}
                              placeholder="输入用户名或选择账本成员"
                            />
                          </Form.Item>
                        </>
                      ),
                    },
                    {
                      dataIndex: 'paymentChain',
                      title: '付款链',
                      width: 360,
                      render: (_, field) => (
                        <Form.Item
                          name={[field.name, 'paymentChain']}
                          style={{ marginBottom: 0 }}
                        >
                          <PaymentChainEditor accountOptions={accountOptions} />
                        </Form.Item>
                      ),
                    },
                    {
                      dataIndex: 'fundedRatio',
                      title: '承担比例',
                      width: 140,
                      render: (_, field) => (
                        <Form.Item
                          name={[field.name, 'fundedRatio']}
                          rules={[{ required: true }]}
                          style={{ marginBottom: 0 }}
                        >
                          <InputNumber
                            addonAfter="%"
                            max={100}
                            min={0}
                            onChange={(value) => {
                              const details = (form.getFieldValue('details') ??
                                []) as LedgerEntryDetailFormValues[];
                              form.setFieldValue(
                                'details',
                                details.map((detail, index) =>
                                  index === field.name
                                    ? {
                                        ...detail,
                                        fundedRatio: value ?? '',
                                        allocationLock: isEmptyFormValue(value)
                                          ? undefined
                                          : 'ratio',
                                      }
                                    : detail,
                                ),
                              );
                              scheduleRecalculateFundedAmounts();
                            }}
                            precision={2}
                            step="0.01"
                            stringMode
                            style={{ width: '100%' }}
                          />
                        </Form.Item>
                      ),
                    },
                    {
                      dataIndex: 'amount',
                      title: '承担金额',
                      width: 140,
                      render: (_, field) => (
                        <Form.Item
                          name={[field.name, 'amount']}
                          rules={[{ required: true }]}
                          style={{ marginBottom: 0 }}
                        >
                          <InputNumber
                            min={0}
                            precision={2}
                            step="0.01"
                            stringMode
                            style={{ width: '100%' }}
                            onChange={(value) => {
                              const details = (form.getFieldValue('details') ??
                                []) as LedgerEntryDetailFormValues[];
                              form.setFieldValue(
                                'details',
                                details.map((detail, index) =>
                                  index === field.name
                                    ? {
                                        ...detail,
                                        amount: value ?? '',
                                        allocationLock: isEmptyFormValue(value)
                                          ? undefined
                                          : 'amount',
                                      }
                                    : detail,
                                ),
                              );
                              scheduleRecalculateFundedAmounts();
                            }}
                          />
                        </Form.Item>
                      ),
                    },
                    {
                      key: 'action',
                      title: '操作',
                      width: 112,
                      render: (_, field) => (
                        <Space size={4}>
                          <Switch
                            checked={Boolean(
                              detailValues[field.name]?.allocationLock,
                            )}
                            size="small"
                            onChange={(locked) => {
                              const details = (form.getFieldValue('details') ??
                                []) as LedgerEntryDetailFormValues[];
                              form.setFieldValue(
                                'details',
                                details.map((detail, index) =>
                                  index === field.name
                                    ? {
                                        ...detail,
                                        allocationLock: locked
                                          ? 'amount'
                                          : undefined,
                                      }
                                    : detail,
                                ),
                              );
                              scheduleRecalculateFundedAmounts();
                            }}
                          />
                          <Button
                            danger
                            icon={<DeleteOutlined />}
                            onClick={() => remove(field.name)}
                          />
                        </Space>
                      ),
                    },
                  ]}
                  dataSource={fields}
                  pagination={false}
                  rowKey="key"
                  size="small"
                />
                <Button
                  icon={<PlusOutlined />}
                  onClick={() => {
                    add({ amount: '', fundedRatio: '0.00' });
                    scheduleRecalculateFundedAmounts();
                  }}
                >
                  新增分担人
                </Button>
              </Space>
            )}
          </Form.List>
        </Form.Item>
        <ProFormTextArea
          name="remark"
          label="备注"
          placeholder="请输入备注"
          fieldProps={{
            autoSize: {
              minRows: 3,
              maxRows: 6,
            },
          }}
        />
      </ModalForm>
    </PageContainer>
  );
};

export default LedgerEntry;
