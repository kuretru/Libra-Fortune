import BasePage from '@/components/BasePage';
import PaymentChannelService from '@/services/libra-fortune/user/payment-channel';
import { PageContainer, ProColumns, ProFormText } from '@ant-design/pro-components';

const PaymentChannel: React.FC = () => {
  const columns: ProColumns<API.User.PaymentChannelDTO>[] = [
    {
      align: 'center',
      copyable: true,
      dataIndex: 'name',
      title: '支付渠道名称',
      width: 240,
    },
    {
      align: 'center',
      dataIndex: 'empty',
      search: false,
      title: '',
    },
  ];

  const formItem = () => {
    return (
      <>
        <ProFormText
          label="用户ID"
          disabled
          hidden
          name="userId"
          initialValue={localStorage.getItem('userId')}
          rules={[{ max: 36, required: true }]}
          tooltip="最长36位"
          width="lg"
        />
        <ProFormText
          label="支付渠道名称"
          name="name"
          placeholder="请输入名称"
          rules={[{ max: 16, required: true }]}
          tooltip="最长16位"
          width="lg"
        />
      </>
    );
  };

  return (
    <PageContainer>
      <BasePage<API.User.PaymentChannelDTO, API.User.PaymentChannelQuery>
        pageName="支付渠道"
        service={new PaymentChannelService()}
        columns={columns}
        formItem={formItem()}
      />
    </PageContainer>
  );
};

export default PaymentChannel;
