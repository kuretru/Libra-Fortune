import BasePage from '@/components/BasePage';
import UserNicknameWithAvatar from '@/components/UserNicknameWithAvatar';
import LedgerMemberService from '@/services/libra-fortune/ledger/ledger-member';
import { ProColumns, ProFormDigit, ProFormText } from '@ant-design/pro-components';
import { useEffect, useState } from 'react';

interface LedgerMemberProps {
  ledgerId: string;
}

const LedgerMember: React.FC<LedgerMemberProps> = (props) => {
  const [ledgerMemberService, setLedgerMemberService] = useState(
    new LedgerMemberService(props.ledgerId),
  );

  useEffect(() => {
    setLedgerMemberService(new LedgerMemberService(props.ledgerId));
  }, [props.ledgerId]);

  const columns: ProColumns<API.Ledger.LedgerMemberVO>[] = [
    {
      align: 'center',
      dataIndex: 'owner',
      title: '用户名',
      render: (_, record) => <UserNicknameWithAvatar user={record} />,
    },
    {
      align: 'center',
      dataIndex: 'defaultFundedRatio',
      title: '默认分担比例',
      render: (_, record) => `${record.defaultFundedRatio.toFixed(2)} %`,
    },
  ];

  const formItem = () => {
    return (
      <>
        <ProFormText
          label="账本ID"
          disabled
          hidden
          initialValue={props.ledgerId}
          name="ledgerId"
          rules={[{ max: 36, required: true }]}
          tooltip="最长36位"
          width="lg"
        />
        <ProFormText
          label="用户ID"
          disabled
          name="userId"
          rules={[{ max: 36, required: true }]}
          tooltip="最长36位"
          width="lg"
        />
        <ProFormDigit
          label="默认分担比例"
          name="defaultFundedRatio"
          placeholder="请输入默认分担比例"
          fieldProps={{ precision: 2 }}
          addonAfter="%"
          max={100}
          rules={[{ required: true }]}
          tooltip="请输入分担比例"
          width="xs"
        />
      </>
    );
  };

  return (
    <BasePage<API.Ledger.LedgerMemberDTO, API.Ledger.LedgerMemberQuery, API.Ledger.LedgerMemberVO>
      pageName="账本成员"
      service={ledgerMemberService}
      columns={columns as ProColumns<API.Ledger.LedgerMemberDTO | API.Ledger.LedgerMemberVO>[]}
      formItem={formItem()}
      tableProps={{ options: { density: false, fullScreen: false, setting: false }, search: false }}
    />
  );
};

export default LedgerMember;
