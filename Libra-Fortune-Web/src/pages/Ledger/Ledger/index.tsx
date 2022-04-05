import React from 'react';
import type { ProColumns } from '@ant-design/pro-table';
import { ProFormSelect, ProFormText } from '@ant-design/pro-form';
import LedgerService from '@/services/libra-fortune-web/ledger/ledger';
import BasePage from '@/components/BasePage';

class Ledger extends React.Component {
  ledgerType = {
    COMMON: '个人账本',
    CO_COMMON: '合作账本',
    FINANCIAL: '理财账本',
    CO_FINANCIAL: '合作理财账本',
  };
  columns: ProColumns<API.Ledger.LedgerDTO>[] = [
    {
      align: 'center',
      copyable: true,
      dataIndex: 'name',
      title: '名称',
      width: 240,
    },
    {
      align: 'center',
      copyable: true,
      dataIndex: 'type',
      title: '类型',
      valueEnum: this.ledgerType,
      valueType: 'select',
      width: 240,
    },
    {
      align: 'left',
      copyable: true,
      dataIndex: 'remark',
      search: false,
      title: '描述',
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
        <ProFormSelect
          label="类型"
          name="type"
          placeholder="请选择账本类型"
          rules={[{ required: true }]}
          valueEnum={this.ledgerType}
          width="lg"
        />
        <ProFormText
          label="备注"
          name="remark"
          placeholder="请输入备注"
          rules={[{ max: 64, required: true }]}
          tooltip="最长64位"
          width="lg"
        />
        <ProFormText
          disabled
          hidden
          initialValue={localStorage.getItem('userId')}
          label="管理员"
          name="ownerId"
          placeholder="请输入管理员"
          rules={[{ max: 36, required: true }]}
          tooltip="最长36位"
          width="lg"
        />
      </>
    );
  };

  render() {
    return (
      <BasePage<API.Ledger.LedgerDTO, API.Ledger.LedgerQuery>
        pageName="账本"
        service={new LedgerService()}
        columns={this.columns}
        formItem={this.formItem()}
      />
    );
  }
}

export default Ledger;
