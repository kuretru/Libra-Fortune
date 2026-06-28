import { PageContainer } from '@ant-design/pro-components';
import {
  Button,
  Card,
  Col,
  DatePicker,
  Form,
  message,
  Row,
  Select,
  Table,
  Tag,
  type TableColumnsType,
} from 'antd';
import dayjs, { type Dayjs } from 'dayjs';
import { useEffect, useMemo, useState } from 'react';
import { useModel } from '@umijs/max';
import * as dashboardApi from '@/services/libra-fortune/ledger/dashboard';
import * as ledgerApi from '@/services/libra-fortune/ledger/ledger';
import * as categoryApi from '@/services/libra-fortune/metadata/category';
import * as tagSetApi from '@/services/libra-fortune/metadata/tag-set';

type AnalysisFormValues = {
  dateRange: [Dayjs, Dayjs];
  sumMode: string;
  groupBy: string[];
  ledgerId?: number[];
  category?: string[];
  type?: string[];
  username?: string[];
  tagId?: number[];
  tagSetId?: number[];
};

type Option<Value extends string | number = string | number> = {
  label: string;
  value: Value;
};

const sumModeOptions: Option<string>[] = [
  { label: '原始金额', value: 'original' },
  { label: '结算金额', value: 'settlement' },
  { label: '分担金额', value: 'funded' },
];

const groupByOptions: Option<string>[] = [
  { label: '年', value: 'year' },
  { label: '月', value: 'month' },
  { label: '日', value: 'day' },
  { label: '账本', value: 'ledgerId' },
  { label: '一级分类', value: 'categoryIdL1' },
  { label: '二级分类', value: 'categoryIdL2' },
  { label: '条目类型', value: 'type' },
  { label: '用户', value: 'username' },
  { label: '标签项', value: 'tagId' },
];

const groupByTitleMap = new Map(groupByOptions.map((item) => [item.value, item.label]));

const flattenCategories = (
  categories: LibraFortune.Metadata.CategoryDTO[],
): LibraFortune.Metadata.CategoryDTO[] =>
  categories.flatMap((category) => [
    category,
    ...flattenCategories(category.children ?? []),
  ]);

const buildCategoryFilterOptions = (
  categories: LibraFortune.Metadata.CategoryDTO[],
  parentName?: string,
): Option<string>[] =>
  categories.flatMap((category) => {
    if (!category.id) {
      return buildCategoryFilterOptions(
        category.children ?? [],
        parentName ? `${parentName} / ${category.name}` : category.name,
      );
    }

    const isRoot = !parentName;
    const label = parentName ? `${parentName} / ${category.name}` : category.name;

    return [
      {
        label,
        value: `${isRoot ? 'categoryIdL1' : 'categoryIdL2'}:${category.id}`,
      },
      ...buildCategoryFilterOptions(category.children ?? [], label),
    ];
  });

const numberFilterKeys = ['ledgerId', 'tagId', 'tagSetId'] as const;

const stringFilterKeys = ['type', 'username'] as const;

const DashboardAnalysis: React.FC = () => {
  const [form] = Form.useForm<AnalysisFormValues>();
  const { initialState } = useModel('@@initialState');
  const currentUsername =
    initialState?.currentUser?.userid ?? initialState?.currentUser?.name;
  const [messageApi, contextHolder] = message.useMessage();
  const [loadingOptions, setLoadingOptions] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [result, setResult] = useState<LibraFortune.Ledger.DashboardLedgerBO[]>(
    [],
  );
  const [selectedGroupBy, setSelectedGroupBy] = useState<string[]>(['month']);
  const [ledgerOptions, setLedgerOptions] = useState<Option<number>[]>([]);
  const [categoryOptions, setCategoryOptions] = useState<Option<number>[]>([]);
  const [categoryFilterOptions, setCategoryFilterOptions] = useState<
    Option<string>[]
  >([]);
  const [entryTypeOptions, setEntryTypeOptions] = useState<Option<string>[]>([]);
  const [usernameOptions, setUsernameOptions] = useState<Option<string>[]>([]);
  const [tagOptions, setTagOptions] = useState<Option<number>[]>([]);
  const [tagSetOptions, setTagSetOptions] = useState<Option<number>[]>([]);

  useEffect(() => {
    setLoadingOptions(true);
    Promise.all([
      ledgerApi.list({ current: 1, pageSize: 1000, noPage: true }),
      ledgerApi.enums(),
      categoryApi.list({ current: 1, pageSize: 1000, noPage: true }),
      tagSetApi.list({ current: 1, pageSize: 1000, noPage: true }),
    ])
      .then(([ledgerResponse, enumResponse, categoryResponse, tagSetResponse]) => {
        const ledgers = ledgerResponse.data.list;
        const categoryTree = categoryResponse.data.list;
        const categories = flattenCategories(categoryTree);
        const tagSets = tagSetResponse.data.list;
        const usernames = Array.from(
          new Set(
            ledgers.flatMap((ledger) =>
              (ledger.members ?? []).map((member) => member.username),
            ),
          ),
        ).sort();

        const nextLedgerOptions = ledgers.flatMap((ledger) =>
          ledger.id ? [{ label: ledger.name, value: ledger.id }] : [],
        );
        setLedgerOptions(nextLedgerOptions);
        setCategoryOptions(
          categories.flatMap((category) =>
            category.id ? [{ label: category.name, value: category.id }] : [],
          ),
        );
        setCategoryFilterOptions(buildCategoryFilterOptions(categoryTree));
        setEntryTypeOptions(
          enumResponse.data.entryTypes.map((item) => ({
            label: item.label,
            value: item.value,
          })),
        );
        setUsernameOptions(
          usernames.map((username) => ({ label: username, value: username })),
        );
        setTagSetOptions(
          tagSets.flatMap((tagSet) =>
            tagSet.id ? [{ label: tagSet.name, value: tagSet.id }] : [],
          ),
        );
        setTagOptions(
          tagSets.flatMap((tagSet) =>
            (tagSet.items ?? []).flatMap((tag) =>
              tag.id
                ? [{ label: `${tagSet.name} / ${tag.name}`, value: tag.id }]
                : [],
            ),
          ),
        );

        const defaultValues: Partial<AnalysisFormValues> = {};
        if (!form.getFieldValue('ledgerId')?.length && nextLedgerOptions[0]) {
          defaultValues.ledgerId = [nextLedgerOptions[0].value];
        }
        if (!form.getFieldValue('username')?.length && currentUsername) {
          defaultValues.username = [currentUsername];
        }
        if (Object.keys(defaultValues).length) {
          form.setFieldsValue(defaultValues);
        }
      })
      .catch((error) => {
        messageApi.error(error?.message ?? '加载筛选项失败');
      })
      .finally(() => {
        setLoadingOptions(false);
      });
  }, [currentUsername, form, messageApi]);

  const labelMaps = useMemo(
    () => ({
      ledgerId: new Map(ledgerOptions.map((item) => [item.value, item.label])),
      categoryIdL1: new Map(categoryOptions.map((item) => [item.value, item.label])),
      categoryIdL2: new Map(categoryOptions.map((item) => [item.value, item.label])),
      type: new Map(entryTypeOptions.map((item) => [item.value, item.label])),
      username: new Map(usernameOptions.map((item) => [item.value, item.label])),
      tagId: new Map(tagOptions.map((item) => [item.value, item.label])),
    }),
    [
      categoryOptions,
      entryTypeOptions,
      ledgerOptions,
      tagOptions,
      usernameOptions,
    ],
  );

  const columns = useMemo<TableColumnsType<LibraFortune.Ledger.DashboardLedgerBO>>(
    () => [
      ...selectedGroupBy.map((groupBy) => ({
        dataIndex: groupBy,
        title: groupByTitleMap.get(groupBy) ?? groupBy,
        render: (value: string | number | undefined) => {
          if (value === undefined) {
            return '-';
          }
          const label = labelMaps[groupBy as keyof typeof labelMaps]?.get(value as never);
          return label ? (
            <span>
              {label} <Tag>{value}</Tag>
            </span>
          ) : (
            value
          );
        },
      })),
      {
        dataIndex: 'sum',
        title: '合计',
        align: 'right',
        render: (value: string) => `¥${Number(value ?? 0).toFixed(2)}`,
      },
    ],
    [labelMaps, selectedGroupBy],
  );

  const onFinish = async (values: AnalysisFormValues) => {
    setSubmitting(true);
    const filter: LibraFortune.Ledger.DashboardFilter = {};

    for (const key of numberFilterKeys) {
      if (values[key]?.length) {
        filter[key] = values[key];
      }
    }
    for (const key of stringFilterKeys) {
      if (values[key]?.length) {
        filter[key] = values[key];
      }
    }
    for (const category of values.category ?? []) {
      const [key, id] = category.split(':');
      if (key === 'categoryIdL1' || key === 'categoryIdL2') {
        filter[key] = [...(filter[key] ?? []), Number(id)];
      }
    }

    try {
      const response = await dashboardApi.sum({
        dateBegin: values.dateRange[0].format('YYYY-MM-DD'),
        dateEnd: values.dateRange[1].format('YYYY-MM-DD'),
        sumMode: values.sumMode,
        groupBy: values.groupBy,
        filter,
      });
      setSelectedGroupBy(values.groupBy);
      setResult(response.data);
    } catch (error: any) {
      messageApi.error(error?.message ?? '查询失败');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <PageContainer title="分析页">
      {contextHolder}
      <Card style={{ marginBottom: 16 }}>
        <Form<AnalysisFormValues>
          form={form}
          layout="vertical"
          initialValues={{
            dateRange: [dayjs().startOf('year'), dayjs()],
            sumMode: 'funded',
            groupBy: ['month'],
          }}
          onFinish={onFinish}
        >
          <Row gutter={16}>
            <Col xs={24} md={12} xl={8}>
              <Form.Item
                label="交易日期"
                name="dateRange"
                rules={[{ required: true }]}
              >
                <DatePicker.RangePicker style={{ width: '100%' }} />
              </Form.Item>
            </Col>
            <Col xs={24} md={12} xl={8}>
              <Form.Item
                label="求和字段"
                name="sumMode"
                rules={[{ required: true }]}
              >
                <Select options={sumModeOptions} />
              </Form.Item>
            </Col>
            <Col xs={24} md={12} xl={8}>
              <Form.Item
                label="分组依据"
                name="groupBy"
                rules={[{ required: true }]}
              >
                <Select mode="multiple" options={groupByOptions} />
              </Form.Item>
            </Col>
            <Col xs={24} md={12} xl={8}>
              <Form.Item label="账本" name="ledgerId">
                <Select
                  allowClear
                  loading={loadingOptions}
                  mode="multiple"
                  options={ledgerOptions}
                />
              </Form.Item>
            </Col>
            <Col xs={24} md={12} xl={8}>
              <Form.Item label="分类" name="category">
                <Select
                  allowClear
                  loading={loadingOptions}
                  mode="multiple"
                  options={categoryFilterOptions}
                />
              </Form.Item>
            </Col>
            <Col xs={24} md={12} xl={8}>
              <Form.Item label="条目类型" name="type">
                <Select
                  allowClear
                  loading={loadingOptions}
                  mode="multiple"
                  options={entryTypeOptions}
                />
              </Form.Item>
            </Col>
            <Col xs={24} md={12} xl={8}>
              <Form.Item label="用户" name="username">
                <Select
                  allowClear
                  loading={loadingOptions}
                  mode="multiple"
                  options={usernameOptions}
                />
              </Form.Item>
            </Col>
            <Col xs={24} md={12} xl={8}>
              <Form.Item label="标签组" name="tagSetId">
                <Select
                  allowClear
                  loading={loadingOptions}
                  mode="multiple"
                  options={tagSetOptions}
                />
              </Form.Item>
            </Col>
            <Col xs={24} md={12} xl={8}>
              <Form.Item label="标签项" name="tagId">
                <Select
                  allowClear
                  loading={loadingOptions}
                  mode="multiple"
                  options={tagOptions}
                />
              </Form.Item>
            </Col>
            <Col xs={24}>
              <Button htmlType="submit" loading={submitting} type="primary">
                查询
              </Button>
            </Col>
          </Row>
        </Form>
      </Card>
      <Table
        columns={columns}
        dataSource={result}
        loading={submitting}
        pagination={false}
        rowKey={(_, index) => String(index)}
        scroll={{ x: 'max-content' }}
      />
    </PageContainer>
  );
};

export default DashboardAnalysis;
