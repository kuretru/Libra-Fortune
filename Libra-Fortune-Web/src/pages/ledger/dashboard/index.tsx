import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import { Col, Row } from 'antd';
import LatestSavingsCard from './components/LatestSavingsCard';
import MonthlyUserExpenseCard from './components/MonthlyUserExpenseCard';

const Dashboard: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const currentUsername =
    initialState?.currentUser?.userid ?? initialState?.currentUser?.name;

  return (
    <PageContainer title="账本面板">
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={8} xl={6}>
          <MonthlyUserExpenseCard username={currentUsername} />
        </Col>
        <Col xs={24} sm={12} lg={8} xl={6}>
          <LatestSavingsCard />
        </Col>
      </Row>
    </PageContainer>
  );
};

export default Dashboard;
