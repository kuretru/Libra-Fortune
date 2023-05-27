import BaseSequencePage from '@/components/BaseSequencePage';
import LedgerTagService from '@/services/libra-fortune/user/ledger-tag';
import { PageContainer, ProColumns, ProFormText } from '@ant-design/pro-components';

const LedgerTag: React.FC = () => {
  const columns: ProColumns<API.Ledger.LedgerTagDTO>[] = [
    {
      align: 'center',
      copyable: true,
      dataIndex: 'name',
      title: '标签名称',
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
          label="标签名称"
          name="name"
          placeholder="请输入名称"
          rules={[{ max: 32, required: true }]}
          tooltip="最长32位"
          width="lg"
        />
      </>
    );
  };

  return (
    <PageContainer>
      <BaseSequencePage<API.Ledger.LedgerTagDTO, API.Ledger.LedgerTagQuery>
        pageName="账本标签"
        service={new LedgerTagService()}
        columns={columns}
        formItem={formItem()}
      />
    </PageContainer>
  );
};

export default LedgerTag;
