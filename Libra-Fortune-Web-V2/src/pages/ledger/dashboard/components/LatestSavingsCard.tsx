import { StatisticCard } from '@ant-design/pro-components';
import { useEffect, useState } from 'react';
import * as dashboardApi from '@/services/libra-fortune/ledger/dashboard';

const formatAmount = (value?: string) => Number(value ?? 0).toFixed(2);

const LatestSavingsCard: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [date, setDate] = useState<string>();
  const [amount, setAmount] = useState('0.00');

  useEffect(() => {
    setLoading(true);
    dashboardApi
      .latestAccountBalances()
      .then((response) => {
        setDate(response.data.date);
        setAmount(formatAmount(response.data.totalBalance));
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <StatisticCard
      loading={loading}
      statistic={{
        title: '最新储蓄总额',
        value: amount,
        prefix: '¥',
      }}
      footer={`统计日期：${date ?? '-'}`}
    />
  );
};

export default LatestSavingsCard;
