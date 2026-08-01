import {
  DeleteOutlined,
  EditOutlined,
  PlusOutlined,
  RollbackOutlined,
} from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  type ProFormInstance,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import { history, useParams } from '@umijs/max';
import { Button, Form, message, Popconfirm, Space, Tag } from 'antd';
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
import * as ledgerApi from '@/services/libra-fortune/ledger/ledger';
import * as categoryApi from '@/services/libra-fortune/metadata/category';
import * as currencyApi from '@/services/libra-fortune/metadata/currency';
import * as tagSetApi from '@/services/libra-fortune/metadata/tag-set';
import CategoryTagSelector from './components/CategoryTagSelector';
import LedgerEntryFormModal from './components/LedgerEntryFormModal';
import type {
  CategorySelectorValue,
  GroupedTagOption,
  Option,
} from './components/types';

type BatchCategoryFormValues = {
  categoryIds?: CategorySelectorValue;
};

type CategoryCascaderOption = {
  label: string;
  value: number;
  children?: CategoryCascaderOption[];
};

type LedgerEntrySearchParams = LibraFortune.Ledger.LedgerEntryQuery & {
  categoryIds?: number[];
  dateRange?: string[];
  name?: string;
  tagId?: number[];
};

const buildCategoryQueryOptions = (
  records: GalaxyWeb.EnumDTO<number>[],
): CategoryCascaderOption[] =>
  records.map((record) => ({
    label: record.label,
    value: record.value,
    children: record.children?.map((child) => ({
      label: child.label,
      value: child.value,
    })),
  }));

const buildCategoryNameMap = (
  categories: GalaxyWeb.EnumDTO<number>[],
): Map<number, string> =>
  new Map(
    categories.flatMap((category) =>
      (category.children ?? []).map((child) => [
        child.value,
        `${category.label} / ${child.label}`,
      ]),
    ),
  );

const parseCategoryQueryValue = (value?: (number | string)[]) => {
  const [rawCategoryIdL1, rawCategoryIdL2] = value ?? [];
  const categoryIdL1 = rawCategoryIdL1 ? Number(rawCategoryIdL1) : undefined;
  const categoryIdL2 = rawCategoryIdL2 ? Number(rawCategoryIdL2) : undefined;
  return {
    categoryIdL1: Number.isInteger(categoryIdL1) ? categoryIdL1 : undefined,
    categoryIdL2: Number.isInteger(categoryIdL2) ? categoryIdL2 : undefined,
  };
};

const parsePositiveInteger = (value: string | null): number | undefined => {
  if (!value) return undefined;
  const result = Number(value);
  return Number.isInteger(result) && result > 0 ? result : undefined;
};

const parseCategoryPath = (value: string | null): number[] | undefined => {
  const [categoryIdL1, categoryIdL2] =
    value?.split(',').map((item) => parsePositiveInteger(item)) ?? [];
  if (!categoryIdL1) return undefined;
  return categoryIdL2 ? [categoryIdL1, categoryIdL2] : [categoryIdL1];
};

const parseSearchParams = (search: string): LedgerEntrySearchParams => {
  const params = new URLSearchParams(search);
  const dateBegin = params.get('dateBegin');
  const dateEnd = params.get('dateEnd');
  const categoryIdL1 = parsePositiveInteger(params.get('categoryIdL1'));
  const categoryIdL2 = parsePositiveInteger(params.get('categoryIdL2'));
  const categoryIds =
    parseCategoryPath(params.get('categoryIds')) ??
    (categoryIdL1
      ? [categoryIdL1, categoryIdL2].filter(
          (categoryId): categoryId is number => categoryId !== undefined,
        )
      : undefined);
  const tagId = [...params.getAll('tagIdIn'), ...params.getAll('tagItemId')]
    .flatMap((value) => value.split(','))
    .flatMap((value) => {
      const tagId = parsePositiveInteger(value);
      return tagId ? [tagId] : [];
    });

  return {
    categoryIds,
    dateRange: dateBegin && dateEnd ? [dateBegin, dateEnd] : undefined,
    name: params.get('name') ?? params.get('nameLike') ?? undefined,
    originalCurrency: params.get('originalCurrency') ?? undefined,
    settlementCurrency: params.get('settlementCurrency') ?? undefined,
    tagIdIn: tagId.length ? tagId : undefined,
    tagSetId: parsePositiveInteger(params.get('tagSetId')),
    type: params.get('type') ?? undefined,
    username: params.get('username') ?? undefined,
  };
};

const stringifySearchParams = (params: Record<string, unknown>): string => {
  const searchParams = new URLSearchParams();
  for (const [name, rawValue] of Object.entries(params)) {
    const values = Array.isArray(rawValue) ? rawValue : [rawValue];
    for (const value of values) {
      if (value !== undefined && value !== null && value !== '') {
        searchParams.append(name, String(value));
      }
    }
  }
  return searchParams.toString();
};

const LedgerEntry: React.FC = () => {
  const params = useParams();
  const ledgerId = useMemo(() => {
    const value = Number(params.ledgerId);
    return Number.isInteger(value) && value > 0 ? value : undefined;
  }, [params.ledgerId]);
  const initialSearchParams = useMemo(
    () => parseSearchParams(history.location.search),
    [],
  );

  const [messageApi, contextHolder] = message.useMessage();
  const [modalVisible, setModalVisible] = useState(false);
  const [batchCategoryVisible, setBatchCategoryVisible] = useState(false);
  const [selectedEntryIds, setSelectedEntryIds] = useState<number[]>([]);
  const [currentRecord, setCurrentRecord] = useState<
    LibraFortune.Ledger.LedgerEntryDTO | undefined
  >(undefined);
  const [ledger, setLedger] = useState<
    LibraFortune.Ledger.LedgerDTO | undefined
  >(undefined);
  const [categories, setCategories] = useState<GalaxyWeb.EnumDTO<number>[]>([]);
  const [categoryQueryOptions, setCategoryQueryOptions] = useState<
    CategoryCascaderOption[]
  >([]);
  const [currencyOptions, setCurrencyOptions] = useState<Option<string>[]>([]);
  const [tagSetOptions, setTagSetOptions] = useState<GroupedTagOption[]>([]);
  const [accountOptions, setAccountOptions] = useState<Option<number>[]>([]);
  const [detailLockTypeOptions, setDetailLockTypeOptions] = useState<
    Option<string>[]
  >([]);
  const [entryTypeOptions, setEntryTypeOptions] = useState<Option<string>[]>(
    [],
  );
  const [datePickerDefaultValue, setDatePickerDefaultValue] = useState<
    Dayjs | undefined
  >(undefined);
  const [batchCategoryForm] = Form.useForm<BatchCategoryFormValues>();
  const actionRef = useRef<ActionType | null>(null);
  const searchFormRef =
    useRef<ProFormInstance<LedgerEntrySearchParams>>(undefined);

  const onSearchReset = useCallback(() => {
    const searchForm = searchFormRef.current;
    if (!searchForm) return;
    searchForm.setFieldsValue({
      categoryIds: undefined,
      dateRange: undefined,
      name: undefined,
      originalCurrency: undefined,
      settlementCurrency: undefined,
      tagIdIn: undefined,
      tagSetId: undefined,
      type: undefined,
      username: undefined,
    });
    const { pathname, hash } = history.location;
    history.replace({ pathname, search: '', hash });
    searchForm.submit();
  }, []);

  const memberOptions = useMemo(
    () =>
      (ledger?.members ?? []).map((member) => ({
        label: member.username,
        value: member.username,
      })),
    [ledger?.members],
  );

  const entryTypeLabelMap = useMemo(
    () =>
      new Map(entryTypeOptions.map((option) => [option.value, option.label])),
    [entryTypeOptions],
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
    () => buildCategoryNameMap(categories),
    [categories],
  );

  useEffect(() => {
    if (!ledgerId) {
      history.replace('/ledger/ledger');
      return;
    }

    Promise.all([
      ledgerApi.get(ledgerId),
      categoryApi.enums(),
      currencyApi.enums(),
      tagSetApi.list({ current: 1, pageSize: 1000, noPage: true }),
      accountApi.list({ current: 1, pageSize: 1000, noPage: true }),
      ledgerApi.enums(),
    ])
      .then(
        ([
          ledgerResponse,
          categoryResponse,
          currencyResponse,
          tagSetResponse,
          accountResponse,
          ledgerEnumResponse,
        ]) => {
          setLedger(ledgerResponse.data);
          setCategories(categoryResponse.data);
          setCategoryQueryOptions(
            buildCategoryQueryOptions(categoryResponse.data),
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
              value: tagSet.id!,
            })),
          );
          setAccountOptions(
            accountResponse.data.list.map((account) => ({
              label: account.name,
              value: account.id!,
            })),
          );
          setDetailLockTypeOptions(ledgerEnumResponse.data.detailLockTypes);
          setEntryTypeOptions(ledgerEnumResponse.data.entryTypes);
        },
      )
      .catch(() => {
        history.replace('/ledger/ledger');
      });
  }, [ledgerId]);

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
      dataIndex: 'type',
      title: '类型',
      valueType: 'select',
      fieldProps: {
        options: entryTypeOptions,
      },
      width: 84,
      renderText: (value: string) => entryTypeLabelMap.get(value) ?? value,
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
      dataIndex: 'categoryIds',
      title: '分类',
      valueType: 'cascader',
      fieldProps: {
        changeOnSelect: true,
        displayRender: (labels: string[]) => labels.join(' > '),
        options: categoryQueryOptions,
        placeholder: '请选择分类',
        showSearch: {
          filter: (
            inputValue: string,
            path: CategoryCascaderOption[],
          ): boolean =>
            path.some((option) =>
              option.label.toLowerCase().includes(inputValue.toLowerCase()),
            ),
        },
      },
      search: {
        transform: (value: number[]) => parseCategoryQueryValue(value),
      },
      renderText: (_, record) =>
        categoryNameMap.get(record.categoryIdL2) ?? record.categoryIdL2,
      width: 100,
    },
    {
      dataIndex: 'originalAmount',
      title: '原始金额',
      align: 'right',
      search: false,
      sorter: true,
      width: 100,
      render: (_, record) => (
        <span>
          {record.originalAmount} {record.originalCurrency}
        </span>
      ),
    },
    {
      dataIndex: 'originalCurrency',
      title: '原始消费货币',
      valueType: 'select',
      hideInTable: true,
      fieldProps: {
        options: currencyOptions,
      },
    },
    {
      dataIndex: 'settlementAmount',
      title: '结算金额',
      align: 'right',
      search: false,
      sorter: true,
      width: 100,
      render: (_, record) => (
        <span>
          {record.settlementAmount} {record.settlementCurrency}
        </span>
      ),
    },
    {
      dataIndex: 'settlementCurrency',
      title: '结算货币',
      valueType: 'select',
      hideInTable: true,
      fieldProps: {
        options: currencyOptions,
      },
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
      dataIndex: 'tagSetId',
      title: '标签组',
      valueType: 'select',
      hideInTable: true,
      fieldProps: {
        options: tagSetOptions.map((tagSet) => ({
          label: tagSet.name,
          value: tagSet.value,
        })),
      },
    },
    {
      dataIndex: 'username',
      title: '分担人',
      valueType: 'select',
      hideInTable: true,
      fieldProps: {
        options: memberOptions,
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
  > = async (params, sorter) => {
    if (!ledgerId) {
      return { data: [], success: false, total: 0 };
    }
    const {
      pageSize,
      current,
      name,
      dateBegin,
      dateEnd,
      categoryIds,
      ...query
    } = params;
    const sortField = (['originalAmount', 'settlementAmount'] as const).find(
      (field) => sorter[field],
    );
    const sortOrder = sortField ? (sorter[sortField] ?? undefined) : undefined;
    const nextSearch = stringifySearchParams({
      categoryIdL1: query.categoryIdL1,
      categoryIdL2: query.categoryIdL2,
      type: query.type,
      dateBegin,
      dateEnd,
      nameLike: name,
      originalCurrency: query.originalCurrency,
      settlementCurrency: query.settlementCurrency,
      username: query.username,
      tagSetId: query.tagSetId,
      tagIdIn: query.tagIdIn,
    });
    if (history.location.search.replace(/^\?/, '') !== nextSearch) {
      const { pathname, hash } = history.location;
      history.replace({ pathname, search: nextSearch, hash });
    }
    const response = await entryApi.list(ledgerId, {
      current: current!,
      pageSize: pageSize!,
      noPage: false,
      nameLike: name,
      dateBegin,
      dateEnd,
      ...query,
      sortField: sortOrder ? sortField : undefined,
      sortOrder,
    });

    return {
      data: response.data.list,
      success: response.code < 1000,
      total: response.data.total,
    };
  };

  const onCreateButtonClick = () => {
    const dateRange = searchFormRef.current?.getFieldValue('dateRange');
    setDatePickerDefaultValue(dateRange?.[0] ? dayjs(dateRange[0]) : undefined);
    setCurrentRecord(undefined);
    setModalVisible(true);
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
      setSelectedEntryIds((oldEntryIds) =>
        oldEntryIds.filter((entryId) => entryId !== id),
      );
      actionRef.current?.reload();
      messageApi.open({
        type: 'success',
        content: '删除成功',
      });
    });
  };

  const onBatchCategoryFinish = async (
    values: BatchCategoryFormValues,
  ): Promise<boolean> => {
    if (!ledgerId || selectedEntryIds.length === 0) return false;
    const categoryIds = values.categoryIds;
    if (!categoryIds?.categoryIdL1 || !categoryIds.categoryIdL2) return false;

    try {
      const response = await entryApi.batchUpdateCategory(ledgerId, {
        entryIds: selectedEntryIds,
        categoryIdL1: categoryIds.categoryIdL1,
        categoryIdL2: categoryIds.categoryIdL2,
      });
      setSelectedEntryIds([]);
      actionRef.current?.reload();
      messageApi.open({
        type: 'success',
        content: `已修改 ${response.data} 条账目分类`,
      });
      return true;
    } catch {
      return false;
    }
  };

  const onSelectedEntryIdsChange = (selectedRowKeys: React.Key[]) => {
    const nextEntryIds = selectedRowKeys.flatMap((key) => {
      const entryId = Number(key);
      return Number.isInteger(entryId) && entryId > 0 ? [entryId] : [];
    });
    if (nextEntryIds.length > 1000) {
      messageApi.open({
        type: 'warning',
        content: '单次最多选择 1000 条账目',
      });
      return;
    }
    setSelectedEntryIds(nextEntryIds);
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
        form={{
          initialValues: initialSearchParams,
        }}
        onReset={onSearchReset}
        rowKey="id"
        rowSelection={{
          fixed: true,
          preserveSelectedRowKeys: true,
          selectedRowKeys: selectedEntryIds,
          onChange: onSelectedEntryIdsChange,
        }}
        request={onRequest}
        search={{
          labelWidth: 'auto',
        }}
        scroll={{ x: 1340 }}
        tableAlertOptionRender={() => (
          <Space size={4}>
            <Button
              size="small"
              type="primary"
              onClick={() => {
                batchCategoryForm.resetFields();
                setBatchCategoryVisible(true);
              }}
            >
              修改分类
            </Button>
            <Button
              size="small"
              type="text"
              onClick={() => setSelectedEntryIds([])}
            >
              清空选择
            </Button>
          </Space>
        )}
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
      <ModalForm<BatchCategoryFormValues>
        form={batchCategoryForm}
        title={`批量修改分类（${selectedEntryIds.length} 条）`}
        open={batchCategoryVisible}
        onOpenChange={(open) => {
          setBatchCategoryVisible(open);
          if (!open) {
            batchCategoryForm.resetFields();
          }
        }}
        onFinish={onBatchCategoryFinish}
        submitter={{
          searchConfig: {
            submitText: '确认修改',
          },
        }}
        modalProps={{
          destroyOnHidden: true,
          width: 720,
        }}
      >
        <Form.Item
          name="categoryIds"
          label="分类"
          rules={[
            {
              validator: async (_, value?: CategorySelectorValue) => {
                if (value?.categoryIdL1 && value.categoryIdL2) {
                  return;
                }
                throw new Error('请选择分类');
              },
            },
          ]}
        >
          <CategoryTagSelector categories={categories} />
        </Form.Item>
      </ModalForm>
      <LedgerEntryFormModal
        accountOptions={accountOptions}
        categories={categories}
        currencyOptions={currencyOptions}
        currentRecord={currentRecord}
        datePickerDefaultValue={datePickerDefaultValue}
        detailLockTypeOptions={detailLockTypeOptions}
        entryTypeOptions={entryTypeOptions}
        ledgerId={ledgerId}
        members={ledger?.members ?? []}
        open={modalVisible}
        tagSetOptions={tagSetOptions}
        onOpenChange={(open) => {
          setModalVisible(open);
          if (!open) {
            setCurrentRecord(undefined);
          }
        }}
        onSaved={() => actionRef.current?.reload()}
      />
    </PageContainer>
  );
};

export default LedgerEntry;
