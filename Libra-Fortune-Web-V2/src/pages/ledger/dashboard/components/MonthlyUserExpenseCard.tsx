import { StatisticCard } from '@ant-design/pro-components';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import * as dashboardApi from '@/services/libra-fortune/ledger/dashboard';

type MonthlyUserExpenseCardProps = {
  username?: string;
};

const formatAmount = (value?: string) => Number(value ?? 0).toFixed(2);

const MonthlyUserExpenseCard: React.FC<MonthlyUserExpenseCardProps> = ({
  username,
}) => {
  const [loading, setLoading] = useState(false);
  const [amount, setAmount] = useState('0.00');

  useEffect(() => {
    if (!username) {
      setAmount('0.00');
      return;
    }

    const now = dayjs();
    setLoading(true);
    dashboardApi
      .sum({
        dateBegin: now.startOf('month').format('YYYY-MM-DD'),
        dateEnd: now.endOf('month').format('YYYY-MM-DD'),
        sumMode: 'funded',
        groupBy: ['username'],
        filter: {
          type: ['expense'],
          username: [username],
        },
      })
      .then((response) => {
        setAmount(formatAmount(response.data[0]?.sum));
      })
      .finally(() => {
        setLoading(false);
      });
  }, [username]);

  return (
    <StatisticCard
      loading={loading}
      statistic={{
        title: <strong>本月支出</strong>,
        value: amount,
        prefix: '¥',
      }}
    />
  );
};

export default MonthlyUserExpenseCard;
