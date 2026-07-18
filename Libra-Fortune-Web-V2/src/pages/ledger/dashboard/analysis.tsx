import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import {
  Button,
  Col,
  DatePicker,
  Drawer,
  Form,
  InputNumber,
  message,
  Row,
  Select,
  Space,
  Table,
  type TableColumnsType,
  Tag,
} from 'antd';
import dayjs, { type Dayjs } from 'dayjs';
import { useEffect, useMemo, useState } from 'react';
import * as dashboardApi from '@/services/libra-fortune/ledger/dashboard';
import * as ledgerApi from '@/services/libra-fortune/ledger/ledger';
import * as categoryApi from '@/services/libra-fortune/metadata/category';
import * as tagSetApi from '@/services/libra-fortune/metadata/tag-set';
import { formatAmount } from '@/utils/format';

type DashboardMetric = LibraFortune.Ledger.DashboardMetric;
type DashboardDimension = LibraFortune.Ledger.DashboardDimension;
type DashboardTimeDimension = LibraFortune.Ledger.DashboardTimeDimension;
type DashboardFilterOperator = LibraFortune.Ledger.DashboardFilterOperator;
type DashboardOrderBy = LibraFortune.Ledger.DashboardOrderBy;
type DimensionFilter =
  LibraFortune.Ledger.DashboardFilterQuery<DashboardDimension>;
type MetricFilter = LibraFortune.Ledger.DashboardFilterQuery<DashboardMetric>;

type MetricFilterFormValue = {
  name?: DashboardMetric;
  operator?: DashboardFilterOperator;
  value?: number | string | null;
};

type OrderByFormValue = {
  field?: string;
  mode?: DashboardOrderBy['mode'];
};

type AnalysisFormValues = {
  dateRange: [Dayjs, Dayjs];
  timeDimension: DashboardTimeDimension;
  metrics: DashboardMetric[];
  dimensions?: DashboardDimension[];
  metricFilters?: MetricFilterFormValue[];
  orderBy?: OrderByFormValue[];
  ledgerId?: number[];
  category?: string[];
  type?: string[];
  username?: string[];
  tagId?: number[];
};

type SelectedFields = {
  timeDimension: DashboardTimeDimension;
  dimensions: DashboardDimension[];
  metrics: DashboardMetric[];
};

type Option<Value extends string | number = string | number> = {
  label: string;
  value: Value;
};

const timeDimensionOptions: Option<DashboardTimeDimension>[] = [
  { label: '年', value: 'year' },
  { label: '月', value: 'month' },
  { label: '日', value: 'day' },
];

const metricOptions: Option<DashboardMetric>[] = [
  { label: '原始金额', value: 'originalSum' },
  { label: '结算金额', value: 'settlementSum' },
  { label: '分担金额', value: 'fundedSum' },
];

const dimensionOptions: Option<DashboardDimension>[] = [
  { label: '账本', value: 'ledgerId' },
  { label: '一级分类', value: 'categoryIdL1' },
  { label: '二级分类', value: 'categoryIdL2' },
  { label: '条目类型', value: 'type' },
  { label: '用户', value: 'username' },
  { label: '标签项', value: 'tagId' },
];

const metricFilterOperatorOptions: Option<DashboardFilterOperator>[] = [
  { label: '等于', value: 'equal' },
  { label: '不等于', value: 'not_equal' },
  { label: '大于', value: 'gt' },
  { label: '大于等于', value: 'gte' },
  { label: '小于', value: 'lt' },
  { label: '小于等于', value: 'lte' },
];

const orderByModeOptions: Option<DashboardOrderBy['mode']>[] = [
  { label: '升序', value: 'asc' },
  { label: '降序', value: 'desc' },
];

const titleMap = new Map<string, string>([
  ...timeDimensionOptions.map((item) => [item.value, item.label] as const),
  ...metricOptions.map((item) => [item.value, item.label] as const),
  ...dimensionOptions.map((item) => [item.value, item.label] as const),
]);

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
    const label = parentName
      ? `${parentName} / ${category.name}`
      : category.name;

    return [
      {
        label,
        value: `${isRoot ? 'categoryIdL1' : 'categoryIdL2'}:${category.id}`,
      },
      ...buildCategoryFilterOptions(category.children ?? [], label),
    ];
  });

const buildAndFilter = <T extends string>(
  children: LibraFortune.Ledger.DashboardFilterQuery<T>[],
): LibraFortune.Ledger.DashboardFilterQuery<T> | undefined => {
  if (!children.length) {
    return undefined;
  }
  return children.length === 1 ? children[0] : { logic: 'and', children };
};

const buildInFilter = <T extends string>(
  name: T,
  values?: Array<number | string>,
): LibraFortune.Ledger.DashboardFilterQuery<T> | undefined =>
  values?.length
    ? { name, operator: 'in', values: values.map((value) => String(value)) }
    : undefined;

const DashboardAnalysis: React.FC = () => {
  const [form] = Form.useForm<AnalysisFormValues>();
  const { initialState } = useModel('@@initialState');
  const currentUsername =
    initialState?.currentUser?.userid ?? initialState?.currentUser?.name;
  const [messageApi, contextHolder] = message.useMessage();
  const [loadingOptions, setLoadingOptions] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [queryDrawerOpen, setQueryDrawerOpen] = useState(true);
  const [result, setResult] = useState<LibraFortune.Ledger.DashboardLedgerBO[]>(
    [],
  );
  const [selectedFields, setSelectedFields] = useState<SelectedFields>({
    timeDimension: 'month',
    dimensions: [],
    metrics: ['fundedSum'],
  });
  const [ledgerOptions, setLedgerOptions] = useState<Option<number>[]>([]);
  const [categoryOptions, setCategoryOptions] = useState<Option<number>[]>([]);
  const [categoryFilterOptions, setCategoryFilterOptions] = useState<
    Option<string>[]
  >([]);
  const [entryTypeOptions, setEntryTypeOptions] = useState<Option<string>[]>(
    [],
  );
  const [usernameOptions, setUsernameOptions] = useState<Option<string>[]>([]);
  const [tagOptions, setTagOptions] = useState<Option<number>[]>([]);
  const watchedTimeDimension = Form.useWatch('timeDimension', form) ?? 'month';
  const selectedMetrics =
    (Form.useWatch('metrics', form) as DashboardMetric[] | undefined) ?? [];
  const selectedDimensions =
    (Form.useWatch('dimensions', form) as DashboardDimension[] | undefined) ??
    [];
  const selectedMetricOptions = useMemo(
    () =>
      metricOptions.filter((option) => selectedMetrics.includes(option.value)),
    [selectedMetrics],
  );
  const orderByFieldOptions = useMemo(
    () => [
      {
        label: `时间 / ${titleMap.get(watchedTimeDimension) ?? watchedTimeDimension}`,
        value: `time:${watchedTimeDimension}`,
      },
      ...selectedDimensions.map((dimension) => ({
        label: `维度 / ${titleMap.get(dimension) ?? dimension}`,
        value: `dimension:${dimension}`,
      })),
      ...selectedMetrics.map((metric) => ({
        label: `指标 / ${titleMap.get(metric) ?? metric}`,
        value: `metric:${metric}`,
      })),
    ],
    [selectedDimensions, selectedMetrics, watchedTimeDimension],
  );

  useEffect(() => {
    setLoadingOptions(true);
    Promise.all([
      ledgerApi.list({ current: 1, pageSize: 1000, noPage: true }),
      ledgerApi.enums(),
      categoryApi.list({ current: 1, pageSize: 1000, noPage: true }),
      tagSetApi.list({ current: 1, pageSize: 1000, noPage: true }),
    ])
      .then(
        ([ledgerResponse, enumResponse, categoryResponse, tagSetResponse]) => {
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
        },
      )
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
      categoryIdL1: new Map(
        categoryOptions.map((item) => [item.value, item.label]),
      ),
      categoryIdL2: new Map(
        categoryOptions.map((item) => [item.value, item.label]),
      ),
      type: new Map(entryTypeOptions.map((item) => [item.value, item.label])),
      username: new Map(
        usernameOptions.map((item) => [item.value, item.label]),
      ),
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

  const columns = useMemo<
    TableColumnsType<LibraFortune.Ledger.DashboardLedgerBO>
  >(
    () => [
      {
        dataIndex: selectedFields.timeDimension,
        title:
          titleMap.get(selectedFields.timeDimension) ??
          selectedFields.timeDimension,
      },
      ...selectedFields.dimensions.map((dimension) => ({
        dataIndex: dimension,
        title: titleMap.get(dimension) ?? dimension,
        render: (value: string | number | undefined) => {
          if (value === undefined) {
            return '-';
          }
          const label = labelMaps[dimension]?.get(value as never);
          return label ? (
            <span>
              {label} <Tag>{value}</Tag>
            </span>
          ) : (
            value
          );
        },
      })),
      ...selectedFields.metrics.map((metric) => ({
        align: 'right' as const,
        dataIndex: metric,
        title: titleMap.get(metric) ?? metric,
        render: (value: string | number | undefined) =>
          `¥${formatAmount(value)}`,
      })),
    ],
    [labelMaps, selectedFields],
  );

  const buildDimensionFilter = (
    values: AnalysisFormValues,
  ): DimensionFilter | undefined => {
    const filters: DimensionFilter[] = [];
    const ledgerIdFilter = buildInFilter('ledgerId', values.ledgerId);
    const typeFilter = buildInFilter('type', values.type);
    const usernameFilter = buildInFilter('username', values.username);
    const tagIdFilter = buildInFilter('tagId', values.tagId);

    if (ledgerIdFilter) {
      filters.push(ledgerIdFilter);
    }
    if (typeFilter) {
      filters.push(typeFilter);
    }
    if (usernameFilter) {
      filters.push(usernameFilter);
    }
    if (tagIdFilter) {
      filters.push(tagIdFilter);
    }

    const categoryFilterValues: Record<
      'categoryIdL1' | 'categoryIdL2',
      number[]
    > = {
      categoryIdL1: [],
      categoryIdL2: [],
    };
    for (const category of values.category ?? []) {
      const [key, id] = category.split(':');
      if (key === 'categoryIdL1' || key === 'categoryIdL2') {
        categoryFilterValues[key].push(Number(id));
      }
    }
    const categoryIdL1Filter = buildInFilter(
      'categoryIdL1',
      categoryFilterValues.categoryIdL1,
    );
    const categoryIdL2Filter = buildInFilter(
      'categoryIdL2',
      categoryFilterValues.categoryIdL2,
    );
    if (categoryIdL1Filter) {
      filters.push(categoryIdL1Filter);
    }
    if (categoryIdL2Filter) {
      filters.push(categoryIdL2Filter);
    }

    return buildAndFilter(filters);
  };

  const buildMetricFilter = (
    values: AnalysisFormValues,
  ): MetricFilter | undefined => {
    const filters = (values.metricFilters ?? []).flatMap((filter) => {
      if (
        !filter.name ||
        !filter.operator ||
        filter.value === undefined ||
        filter.value === null ||
        filter.value === ''
      ) {
        return [];
      }
      return [
        {
          name: filter.name,
          operator: filter.operator,
          values: [String(filter.value)],
        },
      ];
    });

    return buildAndFilter(filters);
  };

  const buildOrderBy = (values: AnalysisFormValues): DashboardOrderBy[] => {
    const dimensions = new Set(values.dimensions ?? []);
    const metrics = new Set(values.metrics);
    const orderBy: DashboardOrderBy[] = [];
    for (const item of values.orderBy ?? []) {
      if (!item.field || !item.mode) {
        continue;
      }
      const [type, name] = item.field.split(':');
      if (!name) {
        continue;
      }
      if (type === 'time' && name === values.timeDimension) {
        orderBy.push({ type: 'time', name, mode: item.mode });
        continue;
      }
      if (type === 'dimension' && dimensions.has(name as DashboardDimension)) {
        orderBy.push({ type: 'dimension', name, mode: item.mode });
        continue;
      }
      if (type === 'metric' && metrics.has(name as DashboardMetric)) {
        orderBy.push({ type: 'metric', name, mode: item.mode });
      }
    }

    return orderBy.length
      ? orderBy
      : [{ type: 'time', name: values.timeDimension, mode: 'asc' }];
  };

  const onFinish = async (values: AnalysisFormValues) => {
    const invalidMetricFilter = values.metricFilters?.find(
      (filter) => filter.name && !values.metrics.includes(filter.name),
    );
    if (invalidMetricFilter?.name) {
      messageApi.error('指标过滤字段必须在已选指标中');
      return;
    }

    setSubmitting(true);
    try {
      const response = await dashboardApi.ledger({
        time: {
          dateBegin: values.dateRange[0].format('YYYY-MM-DD'),
          dateEnd: values.dateRange[1].format('YYYY-MM-DD'),
          dimension: values.timeDimension,
        },
        metrics: values.metrics,
        dimensions: values.dimensions ?? [],
        dimensionsFilter: buildDimensionFilter(values),
        metricsFilter: buildMetricFilter(values),
        orderBy: buildOrderBy(values),
      });
      setSelectedFields({
        timeDimension: values.timeDimension,
        dimensions: values.dimensions ?? [],
        metrics: values.metrics,
      });
      setResult(response.data);
      setQueryDrawerOpen(false);
    } catch (error: any) {
      messageApi.error(error?.message ?? '查询失败');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <PageContainer
      extra={
        <Button type="primary" onClick={() => setQueryDrawerOpen(true)}>
          查询条件
        </Button>
      }
      title="分析"
    >
      {contextHolder}
      <Drawer
        destroyOnHidden={false}
        footer={
          <Space style={{ display: 'flex', justifyContent: 'flex-end' }}>
            <Button onClick={() => setQueryDrawerOpen(false)}>取消</Button>
            <Button
              form="dashboard-analysis-query-form"
              htmlType="submit"
              loading={submitting}
              type="primary"
            >
              查询
            </Button>
          </Space>
        }
        onClose={() => setQueryDrawerOpen(false)}
        open={queryDrawerOpen}
        title="查询条件"
        width="min(760px, 100vw)"
      >
        <Form<AnalysisFormValues>
          form={form}
          id="dashboard-analysis-query-form"
          layout="vertical"
          initialValues={{
            dateRange: [dayjs().startOf('year'), dayjs()],
            timeDimension: 'month',
            metrics: ['fundedSum'],
            dimensions: [],
            metricFilters: [],
            orderBy: [{ field: 'time:month', mode: 'asc' }],
          }}
          onFinish={onFinish}
        >
          <Row gutter={[16, 8]}>
            <Col xs={24} md={8}>
              <Form.Item
                label="时间维度"
                name="timeDimension"
                rules={[{ required: true }]}
              >
                <Select options={timeDimensionOptions} />
              </Form.Item>
            </Col>
            <Col xs={24} md={16}>
              <Form.Item
                label="交易日期"
                name="dateRange"
                rules={[{ required: true }]}
              >
                <DatePicker.RangePicker style={{ width: '100%' }} />
              </Form.Item>
            </Col>

            <Col xs={24}>
              <Form.Item
                label="指标"
                name="metrics"
                rules={[{ required: true }]}
              >
                <Select mode="multiple" options={metricOptions} />
              </Form.Item>
            </Col>

            <Col xs={24}>
              <Form.Item label="维度" name="dimensions">
                <Select allowClear mode="multiple" options={dimensionOptions} />
              </Form.Item>
            </Col>

            <Col xs={24}>
              <Form.Item label="维度过滤">
                <Row gutter={[8, 8]}>
                  <Col xs={24} md={12}>
                    <Form.Item name="ledgerId" noStyle>
                      <Select
                        allowClear
                        loading={loadingOptions}
                        mode="multiple"
                        options={ledgerOptions}
                        placeholder="账本"
                      />
                    </Form.Item>
                  </Col>
                  <Col xs={24} md={12}>
                    <Form.Item name="category" noStyle>
                      <Select
                        allowClear
                        loading={loadingOptions}
                        mode="multiple"
                        options={categoryFilterOptions}
                        placeholder="分类"
                      />
                    </Form.Item>
                  </Col>
                  <Col xs={24} md={12}>
                    <Form.Item name="type" noStyle>
                      <Select
                        allowClear
                        loading={loadingOptions}
                        mode="multiple"
                        options={entryTypeOptions}
                        placeholder="条目类型"
                      />
                    </Form.Item>
                  </Col>
                  <Col xs={24} md={12}>
                    <Form.Item name="username" noStyle>
                      <Select
                        allowClear
                        loading={loadingOptions}
                        mode="multiple"
                        options={usernameOptions}
                        placeholder="用户"
                      />
                    </Form.Item>
                  </Col>
                  <Col xs={24} md={12}>
                    <Form.Item name="tagId" noStyle>
                      <Select
                        allowClear
                        loading={loadingOptions}
                        mode="multiple"
                        options={tagOptions}
                        placeholder="标签项"
                      />
                    </Form.Item>
                  </Col>
                </Row>
              </Form.Item>
            </Col>

            <Col xs={24}>
              <Form.Item label="指标过滤">
                <Form.List name="metricFilters">
                  {(fields, { add, remove }) => (
                    <>
                      {fields.map((field) => (
                        <Row
                          align="middle"
                          gutter={8}
                          key={field.key}
                          style={{ marginBottom: 8 }}
                        >
                          <Col xs={24} md={8}>
                            <Form.Item name={[field.name, 'name']} noStyle>
                              <Select
                                placeholder="指标"
                                options={selectedMetricOptions}
                              />
                            </Form.Item>
                          </Col>
                          <Col xs={24} md={7}>
                            <Form.Item name={[field.name, 'operator']} noStyle>
                              <Select
                                placeholder="条件"
                                options={metricFilterOperatorOptions}
                              />
                            </Form.Item>
                          </Col>
                          <Col xs={20} md={7}>
                            <Form.Item name={[field.name, 'value']} noStyle>
                              <InputNumber
                                placeholder="金额"
                                style={{ width: '100%' }}
                              />
                            </Form.Item>
                          </Col>
                          <Col xs={4} md={2}>
                            <Button
                              icon={<DeleteOutlined />}
                              onClick={() => remove(field.name)}
                            />
                          </Col>
                        </Row>
                      ))}
                      <Button
                        icon={<PlusOutlined />}
                        onClick={() => add({ operator: 'gte' })}
                        type="dashed"
                      >
                        添加指标过滤
                      </Button>
                    </>
                  )}
                </Form.List>
              </Form.Item>
            </Col>

            <Col xs={24}>
              <Form.Item label="排序">
                <Form.List name="orderBy">
                  {(fields, { add, remove }) => (
                    <>
                      {fields.map((field) => (
                        <Row
                          align="middle"
                          gutter={8}
                          key={field.key}
                          style={{ marginBottom: 8 }}
                        >
                          <Col xs={24} md={14}>
                            <Form.Item name={[field.name, 'field']} noStyle>
                              <Select
                                placeholder="排序字段"
                                options={orderByFieldOptions}
                              />
                            </Form.Item>
                          </Col>
                          <Col xs={20} md={8}>
                            <Form.Item name={[field.name, 'mode']} noStyle>
                              <Select
                                placeholder="排序方向"
                                options={orderByModeOptions}
                              />
                            </Form.Item>
                          </Col>
                          <Col xs={4} md={2}>
                            <Button
                              icon={<DeleteOutlined />}
                              onClick={() => remove(field.name)}
                            />
                          </Col>
                        </Row>
                      ))}
                      <Button
                        icon={<PlusOutlined />}
                        onClick={() =>
                          add({
                            field: `time:${watchedTimeDimension}`,
                            mode: 'asc',
                          })
                        }
                        type="dashed"
                      >
                        添加排序
                      </Button>
                    </>
                  )}
                </Form.List>
              </Form.Item>
            </Col>
          </Row>
        </Form>
      </Drawer>
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
