import { ProList } from '@ant-design/pro-components';
import { Space, Tag } from 'antd';

interface LedgerEntryDetailProps {
  data: API.Ledger.LedgerEntryDetailVO[];
}

const LedgerEntryDetail: React.FC<LedgerEntryDetailProps> = (props) => {
  return (
    <ProList<API.Ledger.LedgerEntryDetailVO>
      dataSource={props.data}
      metas={{
        title: {
          dataIndex: ['user', 'nickname'],
        },
        avatar: {
          dataIndex: ['user', 'avatar'],
        },
        subTitle: {
          render(_, entity) {
            if (entity.tags) {
              return (
                <Space>
                  {entity.tags.map((tag) => (
                    <Tag key={tag.id}>{tag.name}</Tag>
                  ))}
                </Space>
              );
            }
            return '';
          },
        },
        description: {
          render(_, entity) {
            return `从【${entity.paymentChannel.name}】支出了【${entity.amount.toFixed(
              2,
            )}】元，占比【${entity.fundedRatio.toFixed(2)}】%`;
          },
        },
      }}
    />
  );
};

export default LedgerEntryDetail;
