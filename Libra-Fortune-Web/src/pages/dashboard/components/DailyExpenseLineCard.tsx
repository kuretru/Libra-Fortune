import { StatisticCard } from '@ant-design/pro-components';
import { Typography, theme } from 'antd';
import dayjs from 'dayjs';
import { useEffect, useMemo, useState } from 'react';
import * as dashboardApi from '@/services/libra-fortune/ledger/dashboard';
import { formatAmount } from '@/utils/format';
import type { DashboardTimeRange } from './types';

type DailyExpenseLineCardProps = {
  timeRange: DashboardTimeRange;
};

type DailyExpensePoint = {
  amount: number;
  date: string;
};

const chartWidth = 920;
const chartHeight = 280;
const padding = {
  top: 16,
  right: 28,
  bottom: 42,
  left: 72,
};

const buildDailyPoints = (
  dateBegin: string,
  dateEnd: string,
  data: LibraFortune.Ledger.DashboardLedgerBO[],
): DailyExpensePoint[] => {
  const amountByDate = new Map(
    data.flatMap((item) =>
      item.day ? [[item.day, Number(item.fundedSum ?? 0)] as const] : [],
    ),
  );
  const points: DailyExpensePoint[] = [];
  let current = dayjs(dateBegin);
  const end = dayjs(dateEnd);
  while (current.isValid() && !current.isAfter(end, 'day')) {
    const date = current.format('YYYY-MM-DD');
    points.push({
      amount: amountByDate.get(date) ?? 0,
      date,
    });
    current = current.add(1, 'day');
  }
  return points;
};

const getDateLabelIndexes = (count: number) => {
  if (count <= 1) {
    return [0];
  }
  const labelCount = Math.min(6, count);
  return Array.from({ length: labelCount }, (_, index) =>
    Math.round((index * (count - 1)) / (labelCount - 1)),
  );
};

const DailyExpenseLineCard: React.FC<DailyExpenseLineCardProps> = ({
  timeRange,
}) => {
  const { token } = theme.useToken();
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<LibraFortune.Ledger.DashboardLedgerBO[]>([]);

  useEffect(() => {
    const query: LibraFortune.Ledger.DashboardQuery = {
      time: {
        dateBegin: timeRange.dateBegin,
        dateEnd: timeRange.dateEnd,
        dimension: 'day',
      },
      metrics: ['fundedSum'],
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
      orderBy: [{ type: 'time', name: 'day', mode: 'ascend' }],
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

  const points = useMemo(
    () => buildDailyPoints(timeRange.dateBegin, timeRange.dateEnd, data),
    [data, timeRange.dateBegin, timeRange.dateEnd],
  );

  const totalAmount = useMemo(
    () => points.reduce((sum, point) => sum + point.amount, 0),
    [points],
  );

  const chart = useMemo(() => {
    const innerWidth = chartWidth - padding.left - padding.right;
    const innerHeight = chartHeight - padding.top - padding.bottom;
    const maxAmount = Math.max(...points.map((point) => point.amount), 0);
    const yMax = maxAmount > 0 ? maxAmount : 1;
    const getX = (index: number) =>
      padding.left +
      (points.length <= 1
        ? innerWidth / 2
        : (index / (points.length - 1)) * innerWidth);
    const getY = (amount: number) =>
      padding.top + innerHeight - (amount / yMax) * innerHeight;
    const linePoints = points
      .map((point, index) => `${getX(index)},${getY(point.amount)}`)
      .join(' ');
    const fillPoints = points.length
      ? `${padding.left},${padding.top + innerHeight} ${linePoints} ${
          padding.left + innerWidth
        },${padding.top + innerHeight}`
      : '';
    const dateLabelIndexes = getDateLabelIndexes(points.length);
    const yTicks = [0, 0.5, 1].map((ratio) => ({
      label: formatAmount(yMax * (1 - ratio)),
      y: padding.top + innerHeight * ratio,
    }));

    return (
      <div style={{ overflowX: 'auto' }}>
        <svg
          aria-label="每日支出折线图"
          role="img"
          style={{ display: 'block', minWidth: 720, width: '100%' }}
          viewBox={`0 0 ${chartWidth} ${chartHeight}`}
        >
          {yTicks.map((tick) => (
            <g key={tick.y}>
              <line
                stroke={token.colorBorderSecondary}
                strokeDasharray="4 6"
                x1={padding.left}
                x2={chartWidth - padding.right}
                y1={tick.y}
                y2={tick.y}
              />
              <text
                fill={token.colorTextSecondary}
                fontSize={12}
                textAnchor="end"
                x={padding.left - 10}
                y={tick.y + 4}
              >
                {tick.label}
              </text>
            </g>
          ))}
          <line
            stroke={token.colorBorder}
            x1={padding.left}
            x2={padding.left}
            y1={padding.top}
            y2={padding.top + innerHeight}
          />
          <line
            stroke={token.colorBorder}
            x1={padding.left}
            x2={chartWidth - padding.right}
            y1={padding.top + innerHeight}
            y2={padding.top + innerHeight}
          />
          {fillPoints ? (
            <polygon
              fill={token.colorPrimaryBg}
              points={fillPoints}
              opacity={0.75}
            />
          ) : null}
          {linePoints ? (
            <polyline
              fill="none"
              points={linePoints}
              stroke={token.colorPrimary}
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={3}
            />
          ) : null}
          {points.map((point, index) =>
            point.amount > 0 ? (
              <circle
                cx={getX(index)}
                cy={getY(point.amount)}
                fill={token.colorBgContainer}
                key={point.date}
                r={3}
                stroke={token.colorPrimary}
                strokeWidth={2}
              >
                <title>
                  {point.date} ¥{formatAmount(point.amount)}
                </title>
              </circle>
            ) : null,
          )}
          {dateLabelIndexes.map((index) => {
            const point = points[index];
            if (!point) {
              return null;
            }
            return (
              <text
                fill={token.colorTextSecondary}
                fontSize={12}
                key={point.date}
                textAnchor="middle"
                x={getX(index)}
                y={chartHeight - 12}
              >
                {dayjs(point.date).format('MM-DD')}
              </text>
            );
          })}
        </svg>
      </div>
    );
  }, [
    points,
    token.colorBgContainer,
    token.colorBorder,
    token.colorBorderSecondary,
    token.colorPrimary,
    token.colorPrimaryBg,
    token.colorTextSecondary,
  ]);

  return (
    <StatisticCard
      chart={
        points.length ? (
          chart
        ) : (
          <Typography.Text type="secondary">暂无支出数据</Typography.Text>
        )
      }
      chartPlacement="bottom"
      loading={loading}
      statistic={{
        title: <strong>每日支出</strong>,
        value: formatAmount(totalAmount),
        prefix: '¥',
        description: '按日汇总',
      }}
    />
  );
};

export default DailyExpenseLineCard;
