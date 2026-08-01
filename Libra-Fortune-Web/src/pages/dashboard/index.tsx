import { LeftOutlined, RightOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import { Button, Col, DatePicker, Row, Select, Space, Typography } from 'antd';
import dayjs, { type Dayjs } from 'dayjs';
import { useEffect, useMemo, useState } from 'react';
import * as categoryApi from '@/services/libra-fortune/metadata/category';
import CategoryExpensePieCard from './components/CategoryExpensePieCard';
import DailyExpenseLineCard from './components/DailyExpenseLineCard';
import IncomeStatisticCard from './components/IncomeStatisticCard';
import LatestSavingsCard from './components/LatestSavingsCard';
import MonthlyUserExpenseCard from './components/MonthlyUserExpenseCard';
import TopExpenseCard from './components/TopExpenseCard';
import type {
  CategoryLabelMaps,
  DashboardTimeRange as DashboardCardTimeRange,
} from './components/types';

type DashboardTimeDimension = LibraFortune.Ledger.DashboardTimeDimension;

type DashboardTimeRange = {
  dimension: DashboardTimeDimension;
  custom: boolean;
} & DashboardCardTimeRange;

const dimensionOptions: {
  label: string;
  value: DashboardTimeDimension;
  unit: dayjs.ManipulateType;
  previous: string;
  next: string;
}[] = [
  {
    label: '按年',
    value: 'year',
    unit: 'year',
    previous: '上一年',
    next: '下一年',
  },
  {
    label: '按月',
    value: 'month',
    unit: 'month',
    previous: '上一月',
    next: '下一月',
  },
  {
    label: '按周',
    value: 'week',
    unit: 'week',
    previous: '上一周',
    next: '下一周',
  },
  {
    label: '按日',
    value: 'day',
    unit: 'day',
    previous: '上一日',
    next: '下一日',
  },
];

const getDimensionOption = (dimension: DashboardTimeDimension) =>
  dimensionOptions.find((item) => item.value === dimension) ??
  dimensionOptions[1];

const getPeriodRange = (
  baseDate: Dayjs,
  dimension: DashboardTimeDimension,
): [Dayjs, Dayjs] => {
  const unit = getDimensionOption(dimension).unit;
  return [baseDate.startOf(unit), baseDate.endOf(unit)];
};

const inferDimension = (
  dateBegin: Dayjs,
  dateEnd: Dayjs,
): DashboardTimeDimension => {
  if (dateBegin.isSame(dateEnd, 'day')) {
    return 'day';
  }
  if (
    dateBegin.isSame(dateBegin.startOf('week'), 'day') &&
    dateEnd.isSame(dateBegin.add(6, 'day'), 'day')
  ) {
    return 'week';
  }
  if (
    dateBegin.isSame(dateBegin.startOf('month'), 'day') &&
    dateEnd.isSame(dateBegin.endOf('month'), 'day')
  ) {
    return 'month';
  }
  if (
    dateBegin.isSame(dateBegin.startOf('year'), 'day') &&
    dateEnd.isSame(dateBegin.endOf('year'), 'day')
  ) {
    return 'year';
  }
  return 'day';
};

const Dashboard: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const currentUsername =
    initialState?.currentUser?.userid ?? initialState?.currentUser?.name;
  const [timeDimension, setTimeDimension] =
    useState<DashboardTimeDimension>('month');
  const [dateRange, setDateRange] = useState<[Dayjs, Dayjs]>(() =>
    getPeriodRange(dayjs(), 'month'),
  );
  const [customRange, setCustomRange] = useState(false);
  const [categoryLabels, setCategoryLabels] = useState<CategoryLabelMaps>({
    categoryIdL1: new Map(),
    categoryIdL2: new Map(),
  });
  const dimensionOption = getDimensionOption(timeDimension);
  const timeRange = useMemo<DashboardTimeRange>(
    () => ({
      dateBegin: dateRange[0].format('YYYY-MM-DD'),
      dateEnd: dateRange[1].format('YYYY-MM-DD'),
      dimension: timeDimension,
      custom: customRange,
    }),
    [customRange, dateRange, timeDimension],
  );

  useEffect(() => {
    let mounted = true;
    categoryApi
      .list({ current: 1, pageSize: 1000, noPage: true })
      .then((response) => {
        if (!mounted) {
          return;
        }
        const categoryIdL1 = new Map<number, string>();
        const categoryIdL2 = new Map<number, string>();
        for (const category of response.data.list) {
          if (category.id) {
            categoryIdL1.set(category.id, category.name);
          }
          for (const child of category.children ?? []) {
            if (child.id) {
              categoryIdL2.set(child.id, `${category.name} / ${child.name}`);
            }
          }
        }
        setCategoryLabels({ categoryIdL1, categoryIdL2 });
      })
      .catch(() => undefined);

    return () => {
      mounted = false;
    };
  }, []);

  const updatePeriod = (dimension: DashboardTimeDimension, baseDate: Dayjs) => {
    setTimeDimension(dimension);
    setDateRange(getPeriodRange(baseDate, dimension));
    setCustomRange(false);
  };

  const shiftPeriod = (amount: -1 | 1) => {
    const unit = dimensionOption.unit;
    updatePeriod(timeDimension, dateRange[0].add(amount, unit));
  };

  const onDateRangeChange = (value: null | [Dayjs | null, Dayjs | null]) => {
    if (!value?.[0] || !value[1]) {
      return;
    }
    const nextRange: [Dayjs, Dayjs] = [value[0], value[1]];
    setDateRange(nextRange);
    setTimeDimension(inferDimension(nextRange[0], nextRange[1]));
    setCustomRange(true);
  };

  return (
    <PageContainer title="账本面板">
      <Space
        align="center"
        size={12}
        style={{ marginBottom: 16, width: '100%' }}
        wrap
      >
        <Typography.Text type="secondary">统计周期</Typography.Text>
        <Button
          disabled={customRange}
          icon={<LeftOutlined />}
          onClick={() => shiftPeriod(-1)}
        >
          {dimensionOption.previous}
        </Button>
        <Select
          options={dimensionOptions.map(({ label, value }) => ({
            label,
            value,
          }))}
          style={{ width: 104 }}
          value={timeDimension}
          onChange={(nextDimension) =>
            updatePeriod(nextDimension, dateRange[0])
          }
        />
        <DatePicker.RangePicker
          allowClear={false}
          value={dateRange}
          onChange={onDateRangeChange}
        />
        <Button
          disabled={customRange}
          icon={<RightOutlined />}
          iconPlacement="end"
          onClick={() => shiftPeriod(1)}
        >
          {dimensionOption.next}
        </Button>
      </Space>
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={8} xl={6}>
          <MonthlyUserExpenseCard
            timeRange={timeRange}
            username={currentUsername}
          />
        </Col>
        <Col xs={24} sm={12} lg={8} xl={6}>
          <IncomeStatisticCard timeRange={timeRange} />
        </Col>
        <Col xs={24} sm={12} lg={8} xl={6}>
          <LatestSavingsCard />
        </Col>
        <Col xs={24} lg={14} xl={16}>
          <TopExpenseCard
            categoryLabels={categoryLabels}
            timeRange={timeRange}
          />
        </Col>
        <Col xs={24} lg={10} xl={8}>
          <CategoryExpensePieCard
            categoryLabels={categoryLabels}
            timeRange={timeRange}
          />
        </Col>
        <Col xs={24}>
          <DailyExpenseLineCard timeRange={timeRange} />
        </Col>
      </Row>
    </PageContainer>
  );
};

export default Dashboard;
