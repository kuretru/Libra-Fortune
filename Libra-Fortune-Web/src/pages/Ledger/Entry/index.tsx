import BasePage from '@/components/BasePage';
import UserNicknameWithAvatar from '@/components/UserNicknameWithAvatar';
import LedgerCategoryService from '@/services/libra-fortune/ledger/ledger-category';
import LedgerEntryService from '@/services/libra-fortune/ledger/ledger-entry';
import LedgerMemberService from '@/services/libra-fortune/ledger/ledger-member';
import LedgerTagService from '@/services/libra-fortune/ledger/ledger-tag';
import { getMoneySymbol } from '@/utils/money-utils';
import {
  PageContainer,
  ProColumns,
  ProForm,
  ProFormDatePicker,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  RequestOptionsType,
} from '@ant-design/pro-components';
import dayjs from 'dayjs';
import LedgerEntryDetail from './entry-detail';

interface LedgerEntryProps {
  ledgerId: string;
}

const LedgerEntry: React.FC<LedgerEntryProps> = (props) => {
  const categoryService = new LedgerCategoryService('df147163-1ff9-4a83-b0eb-4b581918dc4c');
  const memberService = new LedgerMemberService('df147163-1ff9-4a83-b0eb-4b581918dc4c');
  const tagService = new LedgerTagService();
  let membersMap: Record<string, API.Ledger.LedgerMemberVO> = {};

  const currencyType = [
    { label: '人民币', value: 'CNY' },
    { label: '美元', value: 'USD' },
    { label: '欧元', value: 'EUR' },
  ];

  const fetchCategories = async () => {
    const data = (await categoryService.list()).data;
    const result: RequestOptionsType[] = [];
    data.forEach((record) => {
      result.push({ label: record.name, value: record.id! });
    });
    return result;
  };

  const fetchMembers = async () => {
    const data = (await memberService.list()).data;
    membersMap = {};

    const result: RequestOptionsType[] = [];
    data.forEach((record) => {
      result.push({ label: <UserNicknameWithAvatar user={record} />, value: record.userId });
      membersMap[record.userId] = record;
    });
    return result;
  };

  const fetchMyTags = async () => {
    const data = (await tagService.list()).data;
    const result: RequestOptionsType[] = [];
    data.forEach((record) => {
      result.push({ label: record.name, value: record.id! });
    });
    return result;
  };

  const columns: ProColumns<API.Ledger.LedgerEntryVO>[] = [
    {
      align: 'center',
      copyable: true,
      dataIndex: 'date',
      title: '日期',
      valueType: 'dateRange',
      width: 100,
      render: (_, record) => record.date,
    },
    {
      align: 'center',
      copyable: true,
      dataIndex: 'name',
      title: '名称',
      width: 120,
    },
    {
      align: 'center',
      copyable: true,
      dataIndex: 'total',
      title: '金额总计',
      width: 120,
      renderText: (_, record) => {
        return `${getMoneySymbol(record.currencyType)} ${record.total.toFixed(2)}`;
      },
    },
    {
      dataIndex: 'currencyType',
      title: '货币类型',
      valueType: 'select',
      fieldProps: {
        options: currencyType,
      },
      hideInTable: true,
    },
    {
      align: 'center',
      copyable: true,
      dataIndex: 'categoryId',
      valueType: 'select',
      request: fetchCategories,
      title: '分类',
      width: 120,
      renderText: (_, record) => record.category.name,
    },
    {
      dataIndex: 'details',
      search: false,
      title: '明细',
      render: (_, record) => {
        return <LedgerEntryDetail data={record.details} />;
      },
      width: 600,
    },
    {
      align: 'center',
      copyable: true,
      dataIndex: 'remark',
      ellipsis: true,
      search: false,
      title: '备注',
    },
  ];

  const formItem = () => {
    return (
      <>
        <ProFormText
          label="账本ID"
          disabled
          // hidden
          name="ledgerId"
          initialValue={'df147163-1ff9-4a83-b0eb-4b581918dc4c'}
          rules={[{ max: 36, required: true }]}
          tooltip="最长36位"
          width="lg"
        />
        <ProForm.Group>
          <ProFormDatePicker
            label="日期"
            name="date"
            placeholder="请选择日期"
            fieldProps={{ disabledDate: (date) => date > dayjs().endOf('day') }}
            rules={[{ required: true }]}
            tooltip="请输入日期"
            width="sm"
          />
          <ProFormText
            label="条目名称"
            name="name"
            placeholder="请输入名称"
            rules={[{ max: 16, required: true }]}
            tooltip="最长16位"
            width="md"
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormSelect
            label="货币类型"
            name="currencyType"
            initialValue="CNY"
            options={currencyType}
            placeholder="请选择货币类型"
            rules={[{ required: true }]}
            tooltip="请选择货币类型"
            width="sm"
          />
          <ProFormDigit
            label="金额总计"
            name="total"
            placeholder="请输入金额总计"
            fieldProps={{ precision: 2 }}
            rules={[{ required: true }]}
            tooltip="请输入金额总计"
            width="md"
          />
        </ProForm.Group>
        <ProFormSelect
          label="条目分类"
          name="categoryId"
          placeholder="请选择条目分类"
          request={fetchCategories}
          rules={[{ required: true }]}
          tooltip="请选择条目分类"
          width="sm"
        />
        <ProFormTextArea
          label="备注"
          name="remark"
          placeholder="请输入备注"
          initialValue=""
          fieldProps={{
            allowClear: true,
            maxLength: 64,
            showCount: true,
          }}
          rules={[{ max: 64, required: false }]}
          tooltip="最长64位"
          width="xl"
        />
        <ProFormList
          label="条目明细"
          name="details"
          required
          // initialValue={[
          //   {
          //     value: '333',
          //     label: '333',
          //   },
          // ]}
        >
          <ProFormGroup>
            <ProFormSelect
              label="分担人"
              name="userId"
              placeholder="请选择分担人"
              request={fetchMembers}
              rules={[{ required: true }]}
              tooltip="请选择分担人"
              width="xs"
            />
            <ProFormDependency name={['userId']}>
              {({ userId }) => {
                const options: API.EnumDTO[] = [];
                membersMap[userId]?.paymentChannels?.forEach((paymentChannel) => [
                  options.push({ label: paymentChannel.name, value: paymentChannel.id! }),
                ]);
                const initialValue: string = options.length > 0 ? options[0].value : '';
                return (
                  <ProFormSelect
                    label="支出渠道"
                    name="paymentChannelId"
                    placeholder="请选择支出渠道"
                    initialValue={initialValue}
                    options={options}
                    rules={[{ required: true }]}
                    tooltip="请选择支出渠道"
                    width="xs"
                  />
                );
              }}
            </ProFormDependency>
            <ProFormDigit
              label="分担比例"
              name="fundedRatio"
              placeholder="请输入分担比例"
              fieldProps={{ precision: 2 }}
              addonAfter="%"
              max={100}
              rules={[{ required: true }]}
              tooltip="请输入分担比例"
              width="xs"
            />
            <ProFormDigit
              label="分担金额"
              name="amount"
              placeholder="请输入分担金额"
              fieldProps={{ precision: 2 }}
              rules={[{ required: true }]}
              tooltip="请输入分担金额"
              width="sm"
            />
          </ProFormGroup>
          <ProFormDependency name={['userId']}>
            {({ userId }) => {
              if (userId === localStorage.getItem('userId')) {
                return (
                  <ProFormGroup>
                    <ProFormSelect
                      mode="multiple"
                      allowClear
                      label="标签"
                      name="tags"
                      placeholder="请选择标签"
                      request={fetchMyTags}
                      tooltip="请选择标签"
                      width="xl"
                    />
                  </ProFormGroup>
                );
              }
            }}
          </ProFormDependency>
        </ProFormList>
      </>
    );
  };

  const transformFormValues = (record: API.Ledger.LedgerEntryDTO): API.Ledger.LedgerEntryDTO => {
    record.details.forEach((detail) => {
      if (detail.tags) {
        const tagDTO: API.Ledger.LedgerEntryDetailTagDTO[] = [];
        detail.tags.forEach((tag) => tagDTO.push({ tagId: tag }));
        detail.tags = tagDTO;
      }
    });
    return record;
  };

  return (
    <PageContainer>
      <BasePage<API.Ledger.LedgerEntryDTO, API.Ledger.LedgerEntryQuery, API.Ledger.LedgerEntryVO>
        pageName="账本条目"
        service={new LedgerEntryService('df147163-1ff9-4a83-b0eb-4b581918dc4c')}
        columns={columns as ProColumns<API.Ledger.LedgerEntryDTO | API.Ledger.LedgerEntryVO>[]}
        formItem={formItem()}
        transformFormValues={transformFormValues}
        tableProps={{
          size: 'small',
        }}
      />
    </PageContainer>
  );
};

export default LedgerEntry;
