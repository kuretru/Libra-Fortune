import React from 'react';
import type { IRouteComponentProps } from 'umi';
import type { ProColumns } from '@ant-design/pro-table';
import { ProFormText } from '@ant-design/pro-form';
import LedgerCategoryService from '@/services/libra-fortune-web/ledger/ledger-category';
import LedgerService from '@/services/libra-fortune-web/ledger/ledger';
import BasePage from '@/components/BasePage';

interface IState {
  ledgerName: string;
}

class LedgerCategory extends React.Component<IRouteComponentProps<{ ledgerId: string }>, IState> {
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
      ledgerName: '账本分类',
    };
    this.fetchLedgerName();
  }

  columns: ProColumns<API.Ledger.LedgerCategoryDTO>[] = [
    {
      align: 'center',
      copyable: true,
      dataIndex: 'name',
      title: '名称',
      width: 240,
    },
  ];

  formItem = () => {
    return (
      <>
        <ProFormText
          label="名称"
          name="name"
          placeholder="请输入账本名称"
          rules={[{ max: 16, required: true }]}
          tooltip="最长16位"
          width="lg"
        />
        <ProFormText
          disabled
          hidden
          initialValue={this.ledgerId}
          label="所属账本"
          name="ledgerId"
          placeholder="请输入所属账本"
          rules={[{ max: 36, required: true }]}
          tooltip="最长36位"
          width="lg"
        />
      </>
    );
  };

  render() {
    return (
      <BasePage<API.Ledger.LedgerCategoryDTO, API.Ledger.LedgerCategoryQuery>
        pageName={this.state.ledgerName + '-分类'}
        service={new LedgerCategoryService(this.ledgerId)}
        columns={this.columns}
        formItem={this.formItem()}
      />
    );
  }
}

export default LedgerCategory;
