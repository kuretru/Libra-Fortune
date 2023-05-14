import BasePage from '@/components/BasePage';
import { getLedgerType } from '@/services/libra-fortune/enum';
import LedgerService from '@/services/libra-fortune/ledger/ledger';
import { UserOutlined } from '@ant-design/icons';
import {
  ModalForm,
  PageContainer,
  ProColumns,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { Avatar, Button, Space } from 'antd';
import { useState } from 'react';
import LedgerMember from './ledger-member';

const Ledger: React.FC = () => {
  const [modalOpen, setModalOpen] = useState(false);
  const [ledgerId, setLedgerId] = useState('');

  const fetchLedgerType = async () => {
    const response = await getLedgerType();
    return response.data;
  };

  const onUserManagerButtonClick = (record: API.Ledger.LedgerDTO) => {
    setLedgerId(record.id!);
    setModalOpen(true);
  };

  const columns: ProColumns<API.Ledger.LedgerDTO>[] = [
    {
      align: 'center',
      copyable: true,
      dataIndex: 'name',
      title: '账本名称',
      width: 240,
    },
    {
      align: 'center',
      dataIndex: 'owner',
      title: '账本所有者',
      width: 120,
      render: (_, record) => {
        return (
          <Space>
            <Avatar
              src={<img src={(record as API.Ledger.LedgerVO).ownerAvatar} alt="ownerAvatar" />}
            />
            {(record as API.Ledger.LedgerVO).ownerNickname}
          </Space>
        );
      },
    },
    {
      align: 'center',
      dataIndex: 'type',
      title: '账本类型',
      request: fetchLedgerType,
      valueType: 'select',
      width: 120,
    },
    {
      align: 'center',
      dataIndex: 'remark',
      search: false,
      title: '备注',
      width: 240,
    },
    {
      align: 'center',
      key: 'manager',
      search: false,
      title: '管理',
      width: 120,
      render: (_, record) => {
        if (!record.type.startsWith('CO_')) {
          return;
        }
        return (
          <Button
            icon={<UserOutlined />}
            key="userManager"
            onClick={() => onUserManagerButtonClick(record)}
            type="primary"
          >
            成员管理
          </Button>
        );
      },
    },
  ];

  const formItem = () => {
    return (
      <>
        <ProFormText
          label="账本所有者"
          disabled
          hidden
          name="ownerId"
          initialValue={localStorage.getItem('userId')}
          rules={[{ max: 36, required: true }]}
          tooltip="最长36位"
          width="lg"
        />
        <ProFormText
          label="账本名称"
          name="name"
          placeholder="请输入名称"
          rules={[{ max: 16, required: true }]}
          tooltip="最长16位"
          width="lg"
        />
        <ProFormSelect
          label="账本类型"
          name="type"
          placeholder="请选择账本类型"
          request={fetchLedgerType}
          rules={[{ required: true }]}
          tooltip="请选择账本类型"
          width="lg"
        />
        <ProFormTextArea
          label="备注"
          name="remark"
          placeholder="请输入备注"
          initialValue={''}
          fieldProps={{
            allowClear: true,
            maxLength: 64,
            showCount: true,
          }}
          rules={[{ max: 64, required: false }]}
          tooltip="最长64位"
          width="xl"
        />
      </>
    );
  };

  return (
    <PageContainer>
      <BasePage<API.Ledger.LedgerDTO, API.Ledger.LedgerQuery>
        pageName="账本"
        service={new LedgerService()}
        columns={columns}
        formItem={formItem()}
      />
      <ModalForm
        open={modalOpen}
        onOpenChange={setModalOpen}
        submitter={{
          searchConfig: {
            resetText: '关闭',
          },
          submitButtonProps: {
            style: {
              display: 'none',
            },
          },
        }}
      >
        <LedgerMember ledgerId={ledgerId} />
      </ModalForm>
    </PageContainer>
  );
};

export default Ledger;
