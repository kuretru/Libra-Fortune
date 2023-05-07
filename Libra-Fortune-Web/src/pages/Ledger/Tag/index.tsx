import { ProColumns, ProFormText } from "@ant-design/pro-components";
import BasePage from "@/components/BasePage";
import LedgerTagService from "@/services/libra-fortune/ledger/ledger-tag";

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
          disabled
          hidden
          label="用户ID"
          name="userId"
          initialValue={localStorage.getItem("userId")}
          width="lg"
        />
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
    <BasePage<API.Ledger.LedgerTagDTO, API.Ledger.LedgerTagQuery>
      pageName="账本标签"
      service={new LedgerTagService()}
      columns={columns}
      formItem={formItem()}
    />
  );
}

export default LedgerTag;
