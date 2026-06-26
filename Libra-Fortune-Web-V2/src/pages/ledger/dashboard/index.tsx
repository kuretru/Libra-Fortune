import { PageContainer, StatisticCard } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import LatestSavingsCard from './components/LatestSavingsCard';
import MonthlyUserExpenseCard from './components/MonthlyUserExpenseCard';

const Dashboard: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const currentUsername =
    initialState?.currentUser?.userid ?? initialState?.currentUser?.name;

  return (
    <PageContainer title="账本面板">
      <StatisticCard.Group>
        <MonthlyUserExpenseCard username={currentUsername} />
        <LatestSavingsCard />
      </StatisticCard.Group>
    </PageContainer>
  );
};

export default Dashboard;
