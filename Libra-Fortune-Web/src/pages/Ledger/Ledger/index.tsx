import BasePage from '@/components/BasePage';
import UserNicknameWithAvatar from '@/components/UserNicknameWithAvatar';
import { getLedgerType } from '@/services/libra-fortune/enum';
import LedgerService from '@/services/libra-fortune/ledger/ledger';
import { AppstoreAddOutlined, RightCircleOutlined, UserOutlined } from '@ant-design/icons';
import {
  ModalForm,
  PageContainer,
  ProColumns,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useNavigate } from '@umijs/max';
import { Button, Space } from 'antd';
import { useState } from 'react';
import LedgerCategory from './ledger-category';
import LedgerMember from './ledger-member';

const Ledger: React.FC = () => {
  const navigate = useNavigate();
  const [ledgerId, setLedgerId] = useState('');
  const [managerType, setManagerType] = useState('member');
  const [modalOpen, setModalOpen] = useState(false);

  const fetchLedgerType = async () => {
    const response = await getLedgerType();
    return response.data;
  };

  const onMemberManagerButtonClick = (record: API.Ledger.LedgerDTO) => {
    setLedgerId(record.id!);
    setManagerType('member');
    setModalOpen(true);
  };

  const onCategoryManagerButtonClick = (record: API.Ledger.LedgerDTO) => {
    setLedgerId(record.id!);
    setManagerType('category');
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
      render: (_, record) => (
        <UserNicknameWithAvatar
          user={record as any}
          nickname={(record as API.Ledger.LedgerVO).ownerNickname}
          avatar={(record as API.Ledger.LedgerVO).ownerAvatar}
        />
      ),
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
    },
    {
      align: 'center',
      dataIndex: 'go',
      search: false,
      title: 'Go',
      width: 160,
      render: (_, record) => {
        return (
          <Button
            icon={<RightCircleOutlined />}
            key="go"
            onClick={() => navigate(`/ledgers/${record.id!}/entries`)}
            type="primary"
          >
            开始记账
          </Button>
        );
      },
    },
    {
      align: 'center',
      key: 'manager',
      search: false,
      title: '管理',
      width: 280,
      render: (_, record) => {
        return (
          <Space>
            {record.type.startsWith('CO_') && (
              <Button
                icon={<UserOutlined />}
                key="memberManager"
                onClick={() => onMemberManagerButtonClick(record)}
              >
                成员管理
              </Button>
            )}
            <Button
              icon={<AppstoreAddOutlined />}
              key="categoryManager"
              onClick={() => onCategoryManagerButtonClick(record)}
            >
              分类管理
            </Button>
          </Space>
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
        modalProps={{ maskClosable: false }}
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
        {managerType === 'member' && <LedgerMember ledgerId={ledgerId} />}
        {managerType === 'category' && <LedgerCategory ledgerId={ledgerId} />}
      </ModalForm>
    </PageContainer>
  );
};

export default Ledger;
