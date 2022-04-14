import React from 'react';
import type { IRouteComponentProps } from 'umi';
import type { ProColumns } from '@ant-design/pro-table';
import { ProFormSwitch, ProFormText } from '@ant-design/pro-form';
import LedgerUserService from '@/services/libra-fortune-web/ledger/ledger-user';
import LedgerService from '@/services/libra-fortune-web/ledger/ledger';
import BasePage from '@/components/BasePage';
import { CheckOutlined, CloseOutlined } from '@ant-design/icons';

interface IState {
  ledgerName: string;
}

class LedgerUser extends React.Component<IRouteComponentProps<{ ledgerId: string }>, IState> {
  ledgerId = this.props.match.params.ledgerId;
  async fetchLedgerName() {
    const ledgerService = new LedgerService();
    const ledger = (await ledgerService.get(this.ledgerId)).data;
    this.setState({ ledgerName: ledger.name });
  }

  constructor(props: IRouteComponentProps<{ ledgerId: string }>) {
    super(props);
    console.log(this.props);
    this.state = {
      ledgerName: '账本用户',
    };
    this.fetchLedgerName();
  }

  columns: ProColumns<API.Ledger.LedgerUserDTO>[] = [
    {
      align: 'center',
      copyable: true,
      dataIndex: 'userId',
      title: '用户',
      width: 240,
    },
    {
      align: 'center',
      dataIndex: 'isWritable',
      search: false,
      title: '可写',
      width: 80,
      render: (_, record) => (record.isWritable ? <CheckOutlined /> : <CloseOutlined />),
    },
  ];

  formItem = () => {
    return (
      <>
        <ProFormText
          disabled
          hidden
          initialValue={this.ledgerId}
          label="关联账本"
          name="ledgerId"
          placeholder="请输入关联账本"
          rules={[{ max: 36, required: true }]}
          tooltip="最长36位"
          width="lg"
        />
        <ProFormText
          label="关联用户"
          name="userId"
          placeholder="请输入关联用户"
          rules={[{ max: 36, required: true }]}
          tooltip="最长36位"
          width="lg"
        />
        <ProFormSwitch
          checkedChildren={<CheckOutlined />}
          unCheckedChildren={<CloseOutlined />}
          label="可写"
          name="isWritable"
          placeholder="该用户是否可写该账本"
          rules={[{ required: true }]}
        />
      </>
    );
  };

  render() {
    return (
      <BasePage<API.Ledger.LedgerUserDTO, API.Ledger.LedgerUserQuery>
        pageName={this.state.ledgerName + '-用户'}
        service={new LedgerUserService(this.ledgerId)}
        columns={this.columns}
        formItem={this.formItem()}
      />
    );
  }
}

export default LedgerUser;
