import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import MonthlyUserExpenseCard from './components/MonthlyUserExpenseCard';

const Dashboard: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const currentUsername =
    initialState?.currentUser?.userid ?? initialState?.currentUser?.name;

  return (
    <PageContainer title="账本面板">
      <MonthlyUserExpenseCard username={currentUsername} />
    </PageContainer>
  );
};

export default Dashboard;
