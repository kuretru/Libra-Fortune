import { StatisticCard } from '@ant-design/pro-components';
import { Space, theme, Typography } from 'antd';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import * as dashboardApi from '@/services/libra-fortune/ledger/dashboard';

type MonthlyUserExpenseCardProps = {
  username?: string;
};

const formatAmount = (value?: string) => Number(value ?? 0).toFixed(2);
const expenseTagIds = {
  necessary: 1,
  reducible: 2,
  unnecessary: 3,
};
const expenseTagSetId = 1;

const SubStatistic: React.FC<{ title: string; value: string }> = ({
  title,
  value,
}) => {
  const { token } = theme.useToken();

  return (
    <Space size={8}>
      <Typography.Text type="secondary" style={{ fontSize: token.fontSizeSM }}>
        {title}
      </Typography.Text>
      <Typography.Text style={{ fontSize: token.fontSizeSM }}>
        ¥{value}
      </Typography.Text>
    </Space>
  );
};

const MonthlyUserExpenseCard: React.FC<MonthlyUserExpenseCardProps> = ({
  username,
}) => {
  const [loading, setLoading] = useState(false);
  const [amount, setAmount] = useState('0.00');
  const [necessaryAmount, setNecessaryAmount] = useState('0.00');
  const [reducibleAmount, setReducibleAmount] = useState('0.00');
  const [unnecessaryAmount, setUnnecessaryAmount] = useState('0.00');

  useEffect(() => {
    if (!username) {
      setAmount('0.00');
      setNecessaryAmount('0.00');
      setReducibleAmount('0.00');
      setUnnecessaryAmount('0.00');
      return;
    }

    const now = dayjs();
    const baseQuery = {
      dateBegin: now.startOf('month').format('YYYY-MM-DD'),
      dateEnd: now.endOf('month').format('YYYY-MM-DD'),
      sumMode: 'funded',
      groupBy: ['username'],
      filter: {
        type: ['expense'],
        username: [username],
      },
    };

    let mounted = true;
    setLoading(true);
    Promise.all([
      dashboardApi.sum(baseQuery),
      dashboardApi.sum({
        ...baseQuery,
        groupBy: ['tagId'],
        filter: {
          ...baseQuery.filter,
          tagSetId: [expenseTagSetId],
        },
      }),
    ])
      .then(([expenseResponse, tagSetItemResponse]) => {
        if (!mounted) {
          return;
        }

        const sumByTagId = new Map(
          tagSetItemResponse.data.map((item) => [
            item.tagId,
            formatAmount(item.sum),
          ]),
        );

        setAmount(formatAmount(expenseResponse.data[0]?.sum));
        setNecessaryAmount(sumByTagId.get(expenseTagIds.necessary) ?? '0.00');
        setReducibleAmount(sumByTagId.get(expenseTagIds.reducible) ?? '0.00');
        setUnnecessaryAmount(
          sumByTagId.get(expenseTagIds.unnecessary) ?? '0.00',
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
  }, [username]);

  return (
    <StatisticCard
      loading={loading}
      statistic={{
        title: <strong>本月支出</strong>,
        value: amount,
        prefix: '¥',
        description: (
          <Space direction="vertical" size={2}>
            <SubStatistic title="必要支出" value={necessaryAmount} />
            <SubStatistic title="必要可削减" value={reducibleAmount} />
            <SubStatistic title="非必要" value={unnecessaryAmount} />
          </Space>
        ),
      }}
    />
  );
};

export default MonthlyUserExpenseCard;
