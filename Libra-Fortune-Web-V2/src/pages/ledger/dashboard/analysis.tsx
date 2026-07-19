import { DeleteOutlined, PlusOutlined, RedoOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import {
  Button,
  Col,
  DatePicker,
  Drawer,
  Form,
  Input,
  InputNumber,
  message,
  Row,
  Segmented,
  Select,
  Space,
  Table,
  type TableColumnsType,
  Tag,
  Typography,
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

type OrderByFormValue = {
  field?: string;
  mode?: DashboardOrderBy['mode'];
};

type FilterTreeNode<T extends string> =
  LibraFortune.Ledger.DashboardFilterQuery<T>;

type AnalysisFormValues = {
  dateRange: [Dayjs, Dayjs];
  timeDimension: DashboardTimeDimension;
  metrics: DashboardMetric[];
  dimensions?: DashboardDimension[];
  orderBy?: OrderByFormValue[];
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

const compactFormItemStyle = { marginBottom: 8 };

const metricFilterOperatorValues = new Set<DashboardFilterOperator>([
  'equal',
  'not_equal',
  'gt',
  'gte',
  'lt',
  'lte',
]);

const toOptions = <T extends string>(
  values: GalaxyWeb.EnumDTO<T>[] = [],
): Option<T>[] =>
  values.map((item) => ({
    label: item.label,
    value: item.value,
  }));

const formatTimeDimensionValue = (
  dimension: DashboardTimeDimension,
  value?: string | number,
) => {
  if (value === undefined) {
    return '-';
  }
  if (dimension !== 'week') {
    return value;
  }
  const weekBegin = dayjs(String(value));
  if (!weekBegin.isValid()) {
    return value;
  }
  const weekEnd = weekBegin.add(6, 'day');
  const weekEndFormat = weekBegin.isSame(weekEnd, 'year')
    ? weekBegin.isSame(weekEnd, 'month')
      ? 'MM-DD'
      : 'MM-DD'
    : 'YYYY-MM-DD';
  return `${weekBegin.format('YYYY-MM-DD')} ~ ${weekEnd.format(weekEndFormat)}`;
};

const createFilterGroup = <T extends string>(): FilterTreeNode<T> => ({
  logic: 'and',
  children: [],
});

const createFilterLeaf = <T extends string>(
  operator: DashboardFilterOperator,
): FilterTreeNode<T> => ({
  operator,
  values: [],
});

const isFilterGroup = <T extends string>(node: FilterTreeNode<T>) =>
  node.children !== undefined || node.logic !== undefined;

const isMultiValueOperator = (operator?: DashboardFilterOperator) =>
  operator === 'in' || operator === 'not_in';

const cloneFilterNode = <T extends string>(
  node: FilterTreeNode<T>,
): FilterTreeNode<T> => ({
  ...node,
  children: node.children?.map(cloneFilterNode),
  values: node.values ? [...node.values] : undefined,
});

const updateFilterNodeAtPath = <T extends string>(
  root: FilterTreeNode<T>,
  path: number[],
  updater: (node: FilterTreeNode<T>) => FilterTreeNode<T>,
): FilterTreeNode<T> => {
  if (!path.length) {
    return updater(cloneFilterNode(root));
  }
  const [index, ...rest] = path;
  const nextRoot = cloneFilterNode(root);
  nextRoot.children = [...(nextRoot.children ?? [])];
  nextRoot.children[index] = updateFilterNodeAtPath(
    nextRoot.children[index],
    rest,
    updater,
  );
  return nextRoot;
};

const removeFilterNodeAtPath = <T extends string>(
  root: FilterTreeNode<T>,
  path: number[],
): FilterTreeNode<T> => {
  if (!path.length) {
    return root;
  }
  const parentPath = path.slice(0, -1);
  const index = path[path.length - 1];
  return updateFilterNodeAtPath(root, parentPath, (node) => ({
    ...node,
    children: (node.children ?? []).filter(
      (_, childIndex) => childIndex !== index,
    ),
  }));
};

const addFilterChildAtPath = <T extends string>(
  root: FilterTreeNode<T>,
  path: number[],
  child: FilterTreeNode<T>,
): FilterTreeNode<T> =>
  updateFilterNodeAtPath(root, path, (node) => ({
    ...node,
    logic: node.logic ?? 'and',
    children: [...(node.children ?? []), child],
  }));

const flattenCategories = (
  categories: LibraFortune.Metadata.CategoryDTO[],
): LibraFortune.Metadata.CategoryDTO[] =>
  categories.flatMap((category) => [
    category,
    ...flattenCategories(category.children ?? []),
  ]);

type FilterTreeEditorProps<T extends string> = {
  value: FilterTreeNode<T>;
  onChange: (value: FilterTreeNode<T>) => void;
  fieldOptions: Option<T>[];
  operatorOptions: Option<DashboardFilterOperator>[];
  logicOptions: Option<LibraFortune.Ledger.DashboardFilterLogic>[];
  defaultLeafOperator: DashboardFilterOperator;
  renderValueControl: (
    node: FilterTreeNode<T>,
    path: number[],
    updateNode: (
      path: number[],
      updater: (node: FilterTreeNode<T>) => FilterTreeNode<T>,
    ) => void,
  ) => React.ReactNode;
};

const FilterTreeEditor = <T extends string>({
  value,
  onChange,
  fieldOptions,
  operatorOptions,
  logicOptions,
  defaultLeafOperator,
  renderValueControl,
}: FilterTreeEditorProps<T>) => {
  const updateNode = (
    path: number[],
    updater: (node: FilterTreeNode<T>) => FilterTreeNode<T>,
  ) => {
    onChange(updateFilterNodeAtPath(value, path, updater));
  };

  const renderNode = (node: FilterTreeNode<T>, path: number[]) => {
    if (isFilterGroup(node)) {
      return (
        <div
          key={path.join('-') || 'root'}
          style={{
            border: '1px solid #f0f0f0',
            borderRadius: 6,
            marginTop: path.length ? 4 : 0,
            padding: 6,
          }}
        >
          <Row align="middle" gutter={6}>
            <Col flex="none">
              <Typography.Text type="secondary">
                {path.length ? '分组' : '根分组'}
              </Typography.Text>
            </Col>
            <Col flex="none">
              <Segmented
                options={(logicOptions.length
                  ? logicOptions
                  : [
                      { label: '并且', value: 'and' },
                      { label: '或者', value: 'or' },
                    ]
                ).map((item) => ({ label: item.label, value: item.value }))}
                value={node.logic ?? 'and'}
                onChange={(nextLogic) =>
                  updateNode(path, (current) => ({
                    ...current,
                    logic:
                      nextLogic as LibraFortune.Ledger.DashboardFilterLogic,
                    children: current.children ?? [],
                  }))
                }
              />
            </Col>
            <Col flex="auto">
              <Space wrap>
                <Button
                  icon={<PlusOutlined />}
                  onClick={() =>
                    onChange(
                      addFilterChildAtPath(
                        value,
                        path,
                        createFilterLeaf<T>(defaultLeafOperator),
                      ),
                    )
                  }
                  size="small"
                >
                  条件
                </Button>
                <Button
                  icon={<PlusOutlined />}
                  onClick={() =>
                    onChange(
                      addFilterChildAtPath(value, path, createFilterGroup<T>()),
                    )
                  }
                  size="small"
                >
                  分组
                </Button>
                {path.length > 0 && (
                  <Button
                    icon={<DeleteOutlined />}
                    onClick={() =>
                      onChange(removeFilterNodeAtPath(value, path))
                    }
                    size="small"
                  />
                )}
              </Space>
            </Col>
          </Row>
          <div style={{ marginTop: 4, paddingLeft: path.length ? 8 : 0 }}>
            {(node.children ?? []).map((child, index) =>
              renderNode(child, [...path, index]),
            )}
            {!node.children?.length && (
              <Typography.Text type="secondary">暂无过滤条件</Typography.Text>
            )}
          </div>
        </div>
      );
    }

    return (
      <Row
        align="middle"
        gutter={6}
        key={path.join('-')}
        style={{ marginTop: 4 }}
      >
        <Col xs={24} md={7}>
          <Select
            placeholder="字段"
            value={node.name}
            options={fieldOptions}
            onChange={(name) =>
              updateNode(path, (current) => ({
                ...current,
                name,
                values: [],
              }))
            }
          />
        </Col>
        <Col xs={24} md={6}>
          <Select
            placeholder="条件"
            value={node.operator}
            options={operatorOptions}
            onChange={(operator) =>
              updateNode(path, (current) => ({
                ...current,
                operator,
                values: [],
              }))
            }
          />
        </Col>
        <Col xs={20} md={9}>
          {renderValueControl(node, path, updateNode)}
        </Col>
        <Col xs={4} md={2}>
          <Button
            icon={<DeleteOutlined />}
            onClick={() => onChange(removeFilterNodeAtPath(value, path))}
          />
        </Col>
      </Row>
    );
  };

  return renderNode(value, []);
};

const normalizeFilterTree = <T extends string>(
  node: FilterTreeNode<T>,
  fieldLabel: string,
  selectedFields?: Set<T>,
  isRoot = true,
): LibraFortune.Ledger.DashboardFilterQuery<T> | undefined => {
  if (isFilterGroup(node)) {
    if (!node.logic) {
      throw new Error(`${fieldLabel}分组缺少逻辑`);
    }
    const children = (node.children ?? []).flatMap((child) => {
      const normalized = normalizeFilterTree(
        child,
        fieldLabel,
        selectedFields,
        false,
      );
      return normalized ? [normalized] : [];
    });
    if (!children.length) {
      if (isRoot) {
        return undefined;
      }
      throw new Error(`${fieldLabel}分组至少需要一个条件`);
    }
    return { logic: node.logic, children };
  }

  if (!node.name) {
    throw new Error(`${fieldLabel}条件缺少字段`);
  }
  if (selectedFields && !selectedFields.has(node.name)) {
    throw new Error(`${fieldLabel}字段必须在已选字段中`);
  }
  if (!node.operator) {
    throw new Error(`${fieldLabel}条件缺少操作符`);
  }
  const values = (node.values ?? []).filter((value) => value !== '');
  if (!values.length) {
    throw new Error(`${fieldLabel}条件缺少值`);
  }
  if (!isMultiValueOperator(node.operator) && values.length !== 1) {
    throw new Error(`${fieldLabel}当前操作符只能指定一个值`);
  }
  return {
    name: node.name,
    operator: node.operator,
    values,
  };
};

const DashboardAnalysis: React.FC = () => {
  const [form] = Form.useForm<AnalysisFormValues>();
  const { initialState } = useModel('@@initialState');
  const currentUsername =
    initialState?.currentUser?.userid ?? initialState?.currentUser?.name;
  const [messageApi, contextHolder] = message.useMessage();
  const [submitting, setSubmitting] = useState(false);
  const [queryDrawerOpen, setQueryDrawerOpen] = useState(true);
  const [result, setResult] = useState<LibraFortune.Ledger.DashboardLedgerBO[]>(
    [],
  );
  const [dimensionFilterTree, setDimensionFilterTree] = useState<
    FilterTreeNode<DashboardDimension>
  >(createFilterGroup<DashboardDimension>());
  const [metricFilterTree, setMetricFilterTree] = useState<
    FilterTreeNode<DashboardMetric>
  >(createFilterGroup<DashboardMetric>());
  const [selectedFields, setSelectedFields] = useState<SelectedFields>({
    timeDimension: 'month',
    dimensions: [],
    metrics: ['fundedSum'],
  });
  const [timeDimensionOptions, setTimeDimensionOptions] = useState<
    Option<DashboardTimeDimension>[]
  >([]);
  const [metricOptions, setMetricOptions] = useState<Option<DashboardMetric>[]>(
    [],
  );
  const [dimensionOptions, setDimensionOptions] = useState<
    Option<DashboardDimension>[]
  >([]);
  const [filterLogicOptions, setFilterLogicOptions] = useState<
    Option<LibraFortune.Ledger.DashboardFilterLogic>[]
  >([]);
  const [dimensionFilterOperatorOptions, setDimensionFilterOperatorOptions] =
    useState<Option<DashboardFilterOperator>[]>([]);
  const [metricFilterOperatorOptions, setMetricFilterOperatorOptions] =
    useState<Option<DashboardFilterOperator>[]>([]);
  const [orderByModeOptions, setOrderByModeOptions] = useState<
    Option<DashboardOrderBy['mode']>[]
  >([]);
  const [ledgerOptions, setLedgerOptions] = useState<Option<number>[]>([]);
  const [categoryOptions, setCategoryOptions] = useState<Option<number>[]>([]);
  const [entryTypeOptions, setEntryTypeOptions] = useState<Option<string>[]>(
    [],
  );
  const [usernameOptions, setUsernameOptions] = useState<Option<string>[]>([]);
  const [tagOptions, setTagOptions] = useState<Option<number>[]>([]);
  const [tagSetOptions, setTagSetOptions] = useState<Option<number>[]>([]);
  const watchedTimeDimension = Form.useWatch('timeDimension', form) ?? 'month';
  const selectedMetrics =
    (Form.useWatch('metrics', form) as DashboardMetric[] | undefined) ?? [];
  const selectedDimensions =
    (Form.useWatch('dimensions', form) as DashboardDimension[] | undefined) ??
    [];
  const titleMap = useMemo(
    () =>
      new Map<string, string>([
        ...timeDimensionOptions.map(
          (item) => [item.value, item.label] as const,
        ),
        ...metricOptions.map((item) => [item.value, item.label] as const),
        ...dimensionOptions.map((item) => [item.value, item.label] as const),
      ]),
    [dimensionOptions, metricOptions, timeDimensionOptions],
  );
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
    [selectedDimensions, selectedMetrics, titleMap, watchedTimeDimension],
  );

  useEffect(() => {
    Promise.all([
      dashboardApi.enums(),
      ledgerApi.list({ current: 1, pageSize: 1000, noPage: true }),
      ledgerApi.enums(),
      categoryApi.list({ current: 1, pageSize: 1000, noPage: true }),
      tagSetApi.list({ current: 1, pageSize: 1000, noPage: true }),
    ])
      .then(
        ([
          dashboardEnumResponse,
          ledgerResponse,
          enumResponse,
          categoryResponse,
          tagSetResponse,
        ]) => {
          const dashboardEnums = dashboardEnumResponse.data;
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

          setTimeDimensionOptions(toOptions(dashboardEnums.timeDimensions));
          setMetricOptions(toOptions(dashboardEnums.metrics));
          setDimensionOptions(toOptions(dashboardEnums.dimensions));
          setFilterLogicOptions(toOptions(dashboardEnums.filterLogics));
          setDimensionFilterOperatorOptions(
            toOptions(dashboardEnums.filterOperators),
          );
          setMetricFilterOperatorOptions(
            toOptions(dashboardEnums.filterOperators).filter((item) =>
              metricFilterOperatorValues.has(item.value),
            ),
          );
          setOrderByModeOptions(toOptions(dashboardEnums.orderByModes));

          const nextLedgerOptions = ledgers.flatMap((ledger) =>
            ledger.id ? [{ label: ledger.name, value: ledger.id }] : [],
          );
          setLedgerOptions(nextLedgerOptions);
          setCategoryOptions(
            categories.flatMap((category) =>
              category.id ? [{ label: category.name, value: category.id }] : [],
            ),
          );
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

          setDimensionFilterTree((current) => {
            if (current.children?.length) {
              return current;
            }
            const children: FilterTreeNode<DashboardDimension>[] = [];
            if (nextLedgerOptions[0]) {
              children.push({
                name: 'ledgerId',
                operator: 'in',
                values: [String(nextLedgerOptions[0].value)],
              });
            }
            children.push(
              {
                name: 'type',
                operator: 'in',
                values: ['expense'],
              },
              {
                name: 'settlementCurrency',
                operator: 'in',
                values: ['CNY'],
              },
            );
            if (currentUsername) {
              children.push({
                name: 'username',
                operator: 'in',
                values: [currentUsername],
              });
            }
            return { logic: 'and', children };
          });
        },
      )
      .catch((error) => {
        messageApi.error(error?.message ?? '加载筛选项失败');
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
      originalCurrency: new Map(),
      settlementCurrency: new Map(),
      username: new Map(
        usernameOptions.map((item) => [item.value, item.label]),
      ),
      tagItemId: new Map(tagOptions.map((item) => [item.value, item.label])),
      tagSetId: new Map(tagSetOptions.map((item) => [item.value, item.label])),
    }),
    [
      categoryOptions,
      entryTypeOptions,
      ledgerOptions,
      tagSetOptions,
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
        render: (value: string | number | undefined) =>
          formatTimeDimensionValue(selectedFields.timeDimension, value),
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
    [labelMaps, selectedFields, titleMap],
  );

  const getDimensionValueOptions = (
    name?: DashboardDimension,
  ): Option<string>[] | undefined => {
    const mapNumberOptions = (options: Option<number>[]) =>
      options.map((item) => ({
        label: item.label,
        value: String(item.value),
      }));
    switch (name) {
      case 'ledgerId':
        return mapNumberOptions(ledgerOptions);
      case 'categoryIdL1':
      case 'categoryIdL2':
        return mapNumberOptions(categoryOptions);
      case 'type':
        return entryTypeOptions;
      case 'originalCurrency':
      case 'settlementCurrency':
        return undefined;
      case 'username':
        return usernameOptions;
      case 'tagItemId':
        return mapNumberOptions(tagOptions);
      case 'tagSetId':
        return mapNumberOptions(tagSetOptions);
      default:
        return [];
    }
  };

  const renderDimensionValueControl = (
    node: FilterTreeNode<DashboardDimension>,
    path: number[],
    updateNode: (
      path: number[],
      updater: (
        node: FilterTreeNode<DashboardDimension>,
      ) => FilterTreeNode<DashboardDimension>,
    ) => void,
  ) => {
    const multiple = isMultiValueOperator(node.operator);
    const options = getDimensionValueOptions(node.name);
    if (options === undefined) {
      if (multiple) {
        return (
          <Select
            allowClear
            mode="tags"
            placeholder="值"
            value={node.values ?? []}
            onChange={(value) =>
              updateNode(path, (current) => ({
                ...current,
                values: value,
              }))
            }
          />
        );
      }
      return (
        <Input
          allowClear
          placeholder="值"
          value={node.values?.[0]}
          onChange={(event) =>
            updateNode(path, (current) => ({
              ...current,
              values: event.target.value ? [event.target.value] : [],
            }))
          }
        />
      );
    }
    return (
      <Select
        allowClear
        mode={multiple ? 'multiple' : undefined}
        options={options}
        placeholder="值"
        showSearch
        value={multiple ? (node.values ?? []) : node.values?.[0]}
        onChange={(value) =>
          updateNode(path, (current) => ({
            ...current,
            values: Array.isArray(value) ? value : value ? [value] : [],
          }))
        }
      />
    );
  };

  const renderMetricValueControl = (
    node: FilterTreeNode<DashboardMetric>,
    path: number[],
    updateNode: (
      path: number[],
      updater: (
        node: FilterTreeNode<DashboardMetric>,
      ) => FilterTreeNode<DashboardMetric>,
    ) => void,
  ) => (
    <InputNumber
      placeholder="金额"
      style={{ width: '100%' }}
      value={node.values?.[0]}
      onChange={(value) =>
        updateNode(path, (current) => ({
          ...current,
          values:
            value === undefined || value === null || value === ''
              ? []
              : [String(value)],
        }))
      }
    />
  );

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
    let dimensionsFilter: DimensionFilter | undefined;
    let metricsFilter: MetricFilter | undefined;
    try {
      dimensionsFilter = normalizeFilterTree(dimensionFilterTree, '维度过滤');
      metricsFilter = normalizeFilterTree(
        metricFilterTree,
        '指标过滤',
        new Set(values.metrics),
      );
    } catch (error: any) {
      messageApi.error(error?.message ?? '过滤条件不合法');
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
        dimensionsFilter,
        metricsFilter,
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
        <Space>
          <Button
            icon={<RedoOutlined />}
            loading={submitting}
            onClick={() => form.submit()}
          />
          <Button type="primary" onClick={() => setQueryDrawerOpen(true)}>
            查询条件
          </Button>
        </Space>
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
            orderBy: [{ field: 'time:month', mode: 'asc' }],
          }}
          onFinish={onFinish}
        >
          <Row gutter={[12, 4]}>
            <Col xs={24} md={8}>
              <Form.Item
                label="时间维度"
                name="timeDimension"
                rules={[{ required: true }]}
                style={compactFormItemStyle}
              >
                <Select options={timeDimensionOptions} />
              </Form.Item>
            </Col>
            <Col xs={24} md={16}>
              <Form.Item
                label="交易日期"
                name="dateRange"
                rules={[{ required: true }]}
                style={compactFormItemStyle}
              >
                <DatePicker.RangePicker style={{ width: '100%' }} />
              </Form.Item>
            </Col>

            <Col xs={24}>
              <Form.Item
                label="指标"
                name="metrics"
                rules={[{ required: true }]}
                style={compactFormItemStyle}
              >
                <Select mode="multiple" options={metricOptions} />
              </Form.Item>
            </Col>

            <Col xs={24}>
              <Form.Item
                label="维度"
                name="dimensions"
                style={compactFormItemStyle}
              >
                <Select allowClear mode="multiple" options={dimensionOptions} />
              </Form.Item>
            </Col>

            <Col xs={24}>
              <Form.Item label="维度过滤" style={compactFormItemStyle}>
                <FilterTreeEditor<DashboardDimension>
                  value={dimensionFilterTree}
                  onChange={setDimensionFilterTree}
                  fieldOptions={dimensionOptions}
                  operatorOptions={dimensionFilterOperatorOptions}
                  logicOptions={filterLogicOptions}
                  defaultLeafOperator="in"
                  renderValueControl={renderDimensionValueControl}
                />
              </Form.Item>
            </Col>

            <Col xs={24}>
              <Form.Item label="指标过滤" style={compactFormItemStyle}>
                <FilterTreeEditor<DashboardMetric>
                  value={metricFilterTree}
                  onChange={setMetricFilterTree}
                  fieldOptions={selectedMetricOptions}
                  operatorOptions={metricFilterOperatorOptions}
                  logicOptions={filterLogicOptions}
                  defaultLeafOperator="gte"
                  renderValueControl={renderMetricValueControl}
                />
              </Form.Item>
            </Col>

            <Col xs={24}>
              <Form.Item label="排序" style={compactFormItemStyle}>
                <Form.List name="orderBy">
                  {(fields, { add, remove }) => (
                    <>
                      {fields.map((field) => (
                        <Row
                          align="middle"
                          gutter={6}
                          key={field.key}
                          style={{ marginBottom: 4 }}
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
