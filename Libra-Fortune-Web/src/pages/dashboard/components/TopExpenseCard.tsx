import { StatisticCard } from '@ant-design/pro-components';
import { Table, type TableColumnsType, Typography } from 'antd';
import { useEffect, useMemo, useState } from 'react';
import * as dashboardApi from '@/services/libra-fortune/ledger/dashboard';
import { formatAmount } from '@/utils/format';
import type { CategoryLabelMaps, DashboardTimeRange } from './types';

type TopExpenseCardProps = {
  categoryLabels: CategoryLabelMaps;
  timeRange: DashboardTimeRange;
};

type TopExpenseRecord = {
  amount: number;
  categoryId?: number;
  categoryName: string;
  rank: number;
};

const TopExpenseCard: React.FC<TopExpenseCardProps> = ({
  categoryLabels,
  timeRange,
}) => {
  const [loading, setLoading] = useState(false);
  const [records, setRecords] = useState<TopExpenseRecord[]>([]);

  useEffect(() => {
    const query: LibraFortune.Ledger.DashboardQuery = {
      time: {
        dateBegin: timeRange.dateBegin,
        dateEnd: timeRange.dateEnd,
        dimension: null,
      },
      metrics: ['fundedSum'],
      dimensions: ['categoryIdL2'],
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
      limit: 20,
    };

    let mounted = true;
    setLoading(true);
    dashboardApi
      .ledger(query)
      .then((response) => {
        if (!mounted) {
          return;
        }
        setRecords(
          response.data.map((item, index) => ({
            amount: Number(item.fundedSum ?? 0),
            categoryId: item.categoryIdL2,
            categoryName:
              (item.categoryIdL2 &&
                categoryLabels.categoryIdL2.get(item.categoryIdL2)) ||
              '未分类',
            rank: index + 1,
          })),
        );
      })
      .finally(() => {
        if (mounted) {
          setLoading(false);
        }
      });

    return () => {
      mounted = false;
    };
  }, [categoryLabels, timeRange.dateBegin, timeRange.dateEnd]);

  const totalAmount = useMemo(
    () => formatAmount(records.reduce((sum, item) => sum + item.amount, 0)),
    [records],
  );

  const columns: TableColumnsType<TopExpenseRecord> = [
    {
      dataIndex: 'rank',
      title: '排名',
      width: 64,
    },
    {
      dataIndex: 'categoryName',
      ellipsis: true,
      title: '消费分类',
      render: (value: string, record) => (
        <Typography.Text>
          {value}
          {record.categoryId ? (
            <Typography.Text type="secondary">
              {' '}
              #{record.categoryId}
            </Typography.Text>
          ) : null}
        </Typography.Text>
      ),
    },
    {
      align: 'right',
      dataIndex: 'amount',
      title: '金额',
      width: 120,
      render: (value: number) => `¥${formatAmount(value)}`,
    },
  ];

  return (
    <StatisticCard
      chart={
        <Table<TopExpenseRecord>
          columns={columns}
          dataSource={records}
          pagination={false}
          rowKey={(record) => `${record.categoryId ?? 'empty'}-${record.rank}`}
          scroll={{ y: 360 }}
          size="small"
        />
      }
      chartPlacement="bottom"
      loading={loading}
      statistic={{
        title: <strong>Top20 消费</strong>,
        value: totalAmount,
        prefix: '¥',
        description: '按二级分类汇总',
      }}
    />
  );
};

export default TopExpenseCard;
