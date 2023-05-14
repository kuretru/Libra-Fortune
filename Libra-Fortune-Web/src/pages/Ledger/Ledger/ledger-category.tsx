import BasePage from '@/components/BasePage';
import LedgerCategoryService from '@/services/libra-fortune/ledger/ledger-category';
import { ProColumns, ProFormText } from '@ant-design/pro-components';
import { useEffect, useState } from 'react';

interface LedgerMemberProps {
  ledgerId: string;
}

const LedgerCategory: React.FC<LedgerMemberProps> = (props) => {
  const [ledgerCategoryService, setLedgerCategoryService] = useState(
    new LedgerCategoryService(props.ledgerId),
  );

  useEffect(() => {
    setLedgerCategoryService(new LedgerCategoryService(props.ledgerId));
  }, [props.ledgerId]);

  const columns: ProColumns<API.Ledger.LedgerCategoryDTO>[] = [
    {
      align: 'center',
      copyable: true,
      dataIndex: 'name',
      title: '分类名称',
      width: 240,
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
          label="分类名称"
          name="name"
          rules={[{ max: 32, required: true }]}
          tooltip="最长32位"
          width="lg"
        />
      </>
    );
  };

  return (
    <BasePage<API.Ledger.LedgerCategoryDTO, API.Ledger.LedgerCategoryQuery>
      pageName="账本分类"
      service={ledgerCategoryService}
      columns={columns}
      formItem={formItem()}
      tableProps={{ options: { density: false, fullScreen: false, setting: false } }}
    />
  );
};

export default LedgerCategory;
