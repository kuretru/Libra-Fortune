import { StatisticCard } from '@ant-design/pro-components';
import { Typography, theme } from 'antd';
import { useEffect, useMemo, useState } from 'react';
import * as dashboardApi from '@/services/libra-fortune/ledger/dashboard';
import { formatAmount } from '@/utils/format';
import type { CategoryLabelMaps, DashboardTimeRange } from './types';

type CategoryExpensePieCardProps = {
  categoryLabels: CategoryLabelMaps;
  timeRange: DashboardTimeRange;
};

type PieSlice = {
  amount: number;
  categoryId?: number;
  categoryName: string;
  color: string;
  percent: number;
};

const CategoryExpensePieCard: React.FC<CategoryExpensePieCardProps> = ({
  categoryLabels,
  timeRange,
}) => {
  const { token } = theme.useToken();
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<LibraFortune.Ledger.DashboardLedgerBO[]>([]);
  const colorPalette = useMemo(
    () => [
      token.blue,
      token.green,
      token.gold,
      token.red,
      token.purple,
      token.cyan,
      token.magenta,
      token.geekblue,
      token.lime,
      token.orange,
    ],
    [
      token.blue,
      token.cyan,
      token.geekblue,
      token.gold,
      token.green,
      token.lime,
      token.magenta,
      token.orange,
      token.purple,
      token.red,
    ],
  );

  useEffect(() => {
    const query: LibraFortune.Ledger.DashboardQuery = {
      time: {
        dateBegin: timeRange.dateBegin,
        dateEnd: timeRange.dateEnd,
        dimension: null,
      },
      metrics: ['fundedSum'],
      dimensions: ['categoryIdL1'],
      dimensionsFilter: {
        logic: 'and',
        children: [
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
        ],
      },
      orderBy: [{ type: 'metric', name: 'fundedSum', mode: 'descend' }],
    };

    let mounted = true;
    setLoading(true);
    dashboardApi
      .ledger(query)
      .then((response) => {
        if (mounted) {
          setData(response.data);
        }
      })
      .finally(() => {
        if (mounted) {
          setLoading(false);
        }
      });

    return () => {
      mounted = false;
    };
  }, [timeRange.dateBegin, timeRange.dateEnd]);

  const slices = useMemo(() => {
    const total = data.reduce(
      (sum, item) => sum + Number(item.fundedSum ?? 0),
      0,
    );
    if (total <= 0) {
      return [] as PieSlice[];
    }
    return data.map((item, index) => {
      const amount = Number(item.fundedSum ?? 0);
      return {
        amount,
        categoryId: item.categoryIdL1,
        categoryName:
          (item.categoryIdL1 &&
            categoryLabels.categoryIdL1.get(item.categoryIdL1)) ||
          '未分类',
        color: colorPalette[index % colorPalette.length],
        percent: (amount / total) * 100,
      };
    });
  }, [categoryLabels, colorPalette, data]);

  const pieBackground = useMemo(() => {
    if (!slices.length) {
      return token.colorFillTertiary;
    }
    let cursor = 0;
    const segments = slices.map((slice) => {
      const start = cursor;
      cursor += slice.percent;
      return `${slice.color} ${start}% ${cursor}%`;
    });
    return `conic-gradient(${segments.join(', ')})`;
  }, [slices, token.colorFillTertiary]);

  return (
    <StatisticCard
      chart={
        <div
          style={{
            display: 'flex',
            flexWrap: 'wrap',
            gap: 20,
            minHeight: 260,
          }}
        >
          <div
            aria-label="一级分类支出占比饼图"
            role="img"
            style={{
              alignSelf: 'center',
              aspectRatio: '1 / 1',
              background: pieBackground,
              borderRadius: '50%',
              boxShadow: `inset 0 0 0 1px ${token.colorBorderSecondary}`,
              flex: '0 0 180px',
              maxWidth: 220,
              minWidth: 160,
            }}
          />
          <div
            style={{
              display: 'flex',
              flex: '1 1 220px',
              flexDirection: 'column',
              gap: 8,
              minWidth: 0,
            }}
          >
            {slices.length ? (
              slices.map((slice) => (
                <div
                  key={`${slice.categoryId ?? 'empty'}-${slice.categoryName}`}
                  style={{
                    alignItems: 'center',
                    display: 'grid',
                    gap: 8,
                    gridTemplateColumns: '12px minmax(0, 1fr) auto',
                  }}
                >
                  <span
                    style={{
                      backgroundColor: slice.color,
                      borderRadius: 2,
                      height: 12,
                      width: 12,
                    }}
                  />
                  <Typography.Text ellipsis>
                    {slice.categoryName}
                    {slice.categoryId ? (
                      <Typography.Text type="secondary">
                        {' '}
                        #{slice.categoryId}
                      </Typography.Text>
                    ) : null}
                  </Typography.Text>
                  <Typography.Text>
                    {slice.percent.toFixed(1)}% / ¥{formatAmount(slice.amount)}
                  </Typography.Text>
                </div>
              ))
            ) : (
              <Typography.Text type="secondary">暂无支出数据</Typography.Text>
            )}
          </div>
        </div>
      }
      chartPlacement="bottom"
      loading={loading}
      statistic={{
        title: <strong>一级分类占比</strong>,
        description: '按支出金额占比',
      }}
    />
  );
};

export default CategoryExpensePieCard;
