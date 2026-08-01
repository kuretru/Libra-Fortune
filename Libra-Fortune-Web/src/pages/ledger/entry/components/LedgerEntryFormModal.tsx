import {
  CloseOutlined,
  DeleteOutlined,
  DragOutlined,
  LockOutlined,
  PlusOutlined,
  UnlockOutlined,
} from '@ant-design/icons';
import {
  ModalForm,
  ProFormDatePicker,
  ProFormDigit,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import {
  AutoComplete,
  Button,
  Form,
  InputNumber,
  message,
  Select,
  Space,
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
import * as entryApi from '@/services/libra-fortune/ledger/entry';
import CategoryTagSelector from './CategoryTagSelector';
import type {
  DetailLockTypes,
  GroupedTagOption,
  LedgerEntryDetailFormValues,
  LedgerEntryFormValues,
  Option,
} from './types';

type LedgerEntryFormModalProps = {
  accountOptions: Option<number>[];
  categories: GalaxyWeb.EnumDTO<number>[];
  currencyOptions: Option<string>[];
  currentRecord?: LibraFortune.Ledger.LedgerEntryDTO;
  datePickerDefaultValue?: Dayjs;
  detailLockTypeOptions: Option<string>[];
  entryTypeOptions: Option<string>[];
  ledgerId?: number;
  members: LibraFortune.Ledger.LedgerMemberDTO[];
  open: boolean;
  tagSetOptions: GroupedTagOption[];
  onOpenChange: (open: boolean) => void;
  onSaved: () => void;
};

type PaymentChainEditorProps = {
  accountOptions: Option<number>[];
  onChange?: (value?: number[]) => void;
  value?: number[];
};

type TagSetTagSelectorProps = {
  tagSets: GroupedTagOption[];
  value?: number[];
  onChange?: (value?: number[]) => void;
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
    <Space vertical size={8} style={{ width: '100%' }}>
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
    <Space vertical size={10}>
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

const LedgerEntryFormModal: React.FC<LedgerEntryFormModalProps> = ({
  accountOptions,
  categories,
  currencyOptions,
  currentRecord,
  datePickerDefaultValue,
  detailLockTypeOptions,
  entryTypeOptions,
  ledgerId,
  members,
  open,
  tagSetOptions,
  onOpenChange,
  onSaved,
}) => {
  const [messageApi, contextHolder] = message.useMessage();
  const [form] = Form.useForm<LedgerEntryFormValues>();
  const detailValues = Form.useWatch('details', form) ?? [];
  const calculateTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const originalAmountAutoFilledRef = useRef(false);
  const continuousEntryRef = useRef(false);

  const memberOptions = useMemo(
    () =>
      members.map((member) => ({
        label: member.username,
        value: member.username,
      })),
    [members],
  );

  const defaultDetails = useMemo(
    () =>
      members.map((member) => ({
        username: member.username,
        fundedRatio: member.defaultFundedRatio,
        amount: '',
      })),
    [members],
  );

  const detailLockTypes = useMemo<DetailLockTypes>(() => {
    const getValue = (value: string) =>
      detailLockTypeOptions.find((option) => option.value === value)?.value ??
      value;
    return {
      unlock: getValue('unlock'),
      ratio: getValue('lock_ratio'),
      amount: getValue('local_amount'),
    };
  }, [detailLockTypeOptions]);

  const detailLockTypeLabelMap = useMemo(
    () =>
      new Map(
        detailLockTypeOptions.map((option) => [option.value, option.label]),
      ),
    [detailLockTypeOptions],
  );

  const defaultEntryType = useMemo(
    () =>
      entryTypeOptions.find((option) => option.value === 'expense')?.value ??
      'expense',
    [entryTypeOptions],
  );

  const resetEntryForm = useCallback(
    (details: LedgerEntryDetailFormValues[] = defaultDetails) => {
      originalAmountAutoFilledRef.current = false;
      form.resetFields();
      form.setFieldsValue({
        originalCurrency: currencyOptions[0]?.value,
        settlementCurrency: currencyOptions[0]?.value,
        type: defaultEntryType,
        tagIds: [],
        details: details.map(({ username, paymentChain, fundedRatio }) => ({
          username,
          lockType: detailLockTypes.unlock,
          paymentChain,
          fundedRatio,
          amount: '',
        })),
      });
    },
    [
      currencyOptions,
      defaultDetails,
      defaultEntryType,
      detailLockTypes.unlock,
      form,
    ],
  );

  useEffect(() => {
    if (!open) return;

    if (currentRecord) {
      originalAmountAutoFilledRef.current = false;
      form.setFieldsValue({
        ...currentRecord,
        categoryIds: {
          categoryIdL1: currentRecord.categoryIdL1,
          categoryIdL2: currentRecord.categoryIdL2,
        },
        date: currentRecord.date ? dayjs(currentRecord.date) : undefined,
        tagIds: currentRecord.tags?.map((tag) => tag.tagId) ?? [],
        details: (currentRecord.details ?? []).map((detail) => ({
          ...detail,
          lockType: detail.lockType ?? detailLockTypes.unlock,
        })),
      });
    } else {
      resetEntryForm();
    }
  }, [open, currentRecord, detailLockTypes.unlock, form, resetEntryForm]);

  const recalculateFundedAmounts = useCallback(() => {
    const settlementAmount = form.getFieldValue('settlementAmount');
    const details = (form.getFieldValue('details') ??
      []) as LedgerEntryDetailFormValues[];
    if (details.length === 0) return;
    if (!settlementAmount) return;

    const settlementCents = amountToCents(settlementAmount);
    if (settlementCents === undefined || settlementCents === 0n) return;

    const amounts = details.map((detail) => {
      if (detail.lockType === detailLockTypes.ratio) {
        const ratio = detail.fundedRatio
          ? ratioToUnits(detail.fundedRatio)
          : undefined;
        return ratio === undefined
          ? undefined
          : (settlementCents * ratio + 5000n) / 10000n;
      }
      if (detail.lockType === detailLockTypes.amount) {
        return detail.amount ? amountToCents(detail.amount) : undefined;
      }
      return undefined;
    });
    if (
      details.some(
        (detail, index) =>
          detail.lockType !== detailLockTypes.unlock &&
          amounts[index] === undefined,
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

    const unlockedIndexes = details.flatMap((detail, index) =>
      detail.lockType === detailLockTypes.unlock ? [index] : [],
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

    const ratios = amounts.map((amount, index) => {
      const detail = details[index];
      if (detail.lockType === detailLockTypes.ratio) {
        return ratioToUnits(detail.fundedRatio ?? '') ?? 0n;
      }
      return ratioUnitsFromCents(amount ?? 0n, settlementCents);
    });
    const ratioAdjustmentIndex = details.findIndex(
      (detail) => detail.lockType !== detailLockTypes.ratio,
    );
    if (ratioAdjustmentIndex >= 0) {
      ratios[ratioAdjustmentIndex] +=
        10000n - ratios.reduce((total, ratio) => total + ratio, 0n);
    }
    form.setFieldValue(
      'details',
      details.map((detail, index) => ({
        ...detail,
        amount: centsToAmount(amounts[index] ?? 0n),
        fundedRatio:
          detail.lockType === detailLockTypes.ratio
            ? detail.fundedRatio
            : ratioUnitsToString(ratios[index]),
      })),
    );
  }, [detailLockTypes, form, messageApi]);

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

      if (isEmptyFormValue(value)) {
        if (calculateTimerRef.current) {
          clearTimeout(calculateTimerRef.current);
          calculateTimerRef.current = null;
        }
        return;
      }

      scheduleRecalculateFundedAmounts();
    },
    [form, scheduleRecalculateFundedAmounts],
  );

  const onOriginalAmountChange = useCallback(() => {
    originalAmountAutoFilledRef.current = false;
  }, []);

  const toggleDetailLockType = useCallback(
    (detailIndex: number, lockType: string) => {
      const details = (form.getFieldValue('details') ??
        []) as LedgerEntryDetailFormValues[];
      form.setFieldValue(
        'details',
        details.map((detail, index) =>
          index === detailIndex
            ? {
                ...detail,
                lockType:
                  detail.lockType === lockType
                    ? detailLockTypes.unlock
                    : lockType,
              }
            : detail,
        ),
      );
      scheduleRecalculateFundedAmounts();
    },
    [detailLockTypes.unlock, form, scheduleRecalculateFundedAmounts],
  );

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

  const onFinish = async (values: LedgerEntryFormValues): Promise<boolean> => {
    if (!ledgerId) return false;
    const isContinuousEntry = continuousEntryRef.current;
    continuousEntryRef.current = false;
    try {
      const record: LibraFortune.Ledger.LedgerEntryDTO = {
        id: values.id,
        ledgerId,
        categoryIdL1: values.categoryIds!.categoryIdL1!,
        categoryIdL2: values.categoryIds!.categoryIdL2!,
        type: values.type!,
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
        details: (values.details ?? []).map((detail) => ({
          ...detail,
          lockType: detail.lockType ?? detailLockTypes.unlock,
        })) as LibraFortune.Ledger.LedgerEntryDetailDTO[],
      };
      const fn = record.id ? entryApi.update : entryApi.create;
      await fn(ledgerId, record);
      onSaved();
      messageApi.open({
        type: 'success',
        content: record.id ? '更新成功' : '新增成功',
      });
      if (isContinuousEntry) {
        resetEntryForm(values.details);
        return false;
      }
      return true;
    } catch {
      return false;
    }
  };

  const onClearButtonClick = () => {
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

  return (
    <>
      {contextHolder}
      <ModalForm<LedgerEntryFormValues>
        form={form}
        title={currentRecord?.id ? '编辑条目' : '新增条目'}
        open={open}
        onOpenChange={onOpenChange}
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
          <ProFormSelect
            name="type"
            label="条目类型"
            options={entryTypeOptions}
            rules={[{ required: true }]}
          />
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
          name="categoryIds"
          label="分类"
          rules={[
            {
              validator: async (_, value) => {
                if (value?.categoryIdL1 && value?.categoryIdL2) {
                  return;
                }
                throw new Error('请选择分类');
              },
            },
          ]}
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
              <Space vertical style={{ width: '100%' }}>
                <Table
                  columns={[
                    {
                      dataIndex: 'username',
                      title: '分担人',
                      width: 180,
                      render: (_, field) => (
                        <>
                          <Form.Item name={[field.name, 'id']} hidden />
                          <Form.Item name={[field.name, 'lockType']} hidden />
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
                      width: 160,
                      render: (_, field) => {
                        const isLocked =
                          detailValues[field.name]?.lockType ===
                          detailLockTypes.ratio;
                        const lockLabel =
                          detailLockTypeLabelMap.get(detailLockTypes.ratio) ??
                          '锁比例';
                        return (
                          <Form.Item
                            name={[field.name, 'fundedRatio']}
                            rules={[{ required: true }]}
                            style={{ marginBottom: 0 }}
                          >
                            <InputNumber
                              suffix={
                                <Space size={0}>
                                  <span>%</span>
                                  <Button
                                    aria-label={lockLabel}
                                    icon={
                                      isLocked ? (
                                        <LockOutlined />
                                      ) : (
                                        <UnlockOutlined />
                                      )
                                    }
                                    size="small"
                                    title={
                                      isLocked ? `解除${lockLabel}` : lockLabel
                                    }
                                    type={isLocked ? 'primary' : 'text'}
                                    onClick={() =>
                                      toggleDetailLockType(
                                        field.name,
                                        detailLockTypes.ratio,
                                      )
                                    }
                                  />
                                </Space>
                              }
                              max={100}
                              min={0}
                              onChange={(value) => {
                                const details = (form.getFieldValue(
                                  'details',
                                ) ?? []) as LedgerEntryDetailFormValues[];
                                form.setFieldValue(
                                  'details',
                                  details.map((detail, index) =>
                                    index === field.name
                                      ? { ...detail, fundedRatio: value ?? '' }
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
                        );
                      },
                    },
                    {
                      dataIndex: 'amount',
                      title: '承担金额',
                      width: 140,
                      render: (_, field) => {
                        const isLocked =
                          detailValues[field.name]?.lockType ===
                          detailLockTypes.amount;
                        const lockLabel =
                          detailLockTypeLabelMap.get(detailLockTypes.amount) ??
                          '锁金额';
                        return (
                          <Form.Item
                            name={[field.name, 'amount']}
                            rules={[{ required: true }]}
                            style={{ marginBottom: 0 }}
                          >
                            <InputNumber
                              suffix={
                                <Button
                                  aria-label={lockLabel}
                                  icon={
                                    isLocked ? (
                                      <LockOutlined />
                                    ) : (
                                      <UnlockOutlined />
                                    )
                                  }
                                  size="small"
                                  title={
                                    isLocked ? `解除${lockLabel}` : lockLabel
                                  }
                                  type={isLocked ? 'primary' : 'text'}
                                  onClick={() =>
                                    toggleDetailLockType(
                                      field.name,
                                      detailLockTypes.amount,
                                    )
                                  }
                                />
                              }
                              min={0}
                              precision={2}
                              step="0.01"
                              stringMode
                              style={{ width: '100%' }}
                              onChange={(value) => {
                                const details = (form.getFieldValue(
                                  'details',
                                ) ?? []) as LedgerEntryDetailFormValues[];
                                form.setFieldValue(
                                  'details',
                                  details.map((detail, index) =>
                                    index === field.name
                                      ? { ...detail, amount: value ?? '' }
                                      : detail,
                                  ),
                                );
                                scheduleRecalculateFundedAmounts();
                              }}
                            />
                          </Form.Item>
                        );
                      },
                    },
                    {
                      key: 'action',
                      title: '操作',
                      width: 48,
                      render: (_, field) => (
                        <Button
                          danger
                          icon={<DeleteOutlined />}
                          onClick={() => remove(field.name)}
                        />
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
                    add({
                      amount: '',
                      fundedRatio: '0.00',
                      lockType: detailLockTypes.unlock,
                    });
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
    </>
  );
};

export default LedgerEntryFormModal;
