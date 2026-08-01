import { StatisticCard } from '@ant-design/pro-components';
import { useEffect, useState } from 'react';
import * as dashboardApi from '@/services/libra-fortune/ledger/dashboard';
import { formatAmount } from '@/utils/format';
import type { DashboardTimeRange } from './types';

type IncomeStatisticCardProps = {
  timeRange: DashboardTimeRange;
};

const IncomeStatisticCard: React.FC<IncomeStatisticCardProps> = ({
  timeRange,
}) => {
  const [loading, setLoading] = useState(false);
  const [amount, setAmount] = useState('0.00');

  useEffect(() => {
    const query: LibraFortune.Ledger.DashboardQuery = {
      time: {
        dateBegin: timeRange.dateBegin,
        dateEnd: timeRange.dateEnd,
        dimension: null,
      },
      metrics: ['fundedSum'],
      dimensionsFilter: {
        logic: 'and',
        children: [
          {
            name: 'type',
            operator: 'in',
            values: ['income'],
          },
          {
            name: 'settlementCurrency',
            operator: 'in',
            values: ['CNY'],
          },
        ],
      },
    };

    let mounted = true;
    setLoading(true);
    dashboardApi
      .ledger(query)
      .then((response) => {
        if (mounted) {
          setAmount(formatAmount(response.data[0]?.fundedSum));
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

  return (
    <StatisticCard
      loading={loading}
      statistic={{
        title: <strong>收入统计</strong>,
        value: amount,
        prefix: '¥',
      }}
    />
  );
};

export default IncomeStatisticCard;
