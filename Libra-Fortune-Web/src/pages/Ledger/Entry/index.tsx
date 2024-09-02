import BasePage from '@/components/BasePage';
import UserNicknameWithAvatar from '@/components/UserNicknameWithAvatar';
import LedgerService from '@/services/libra-fortune/ledger/ledger';
import LedgerCategoryService from '@/services/libra-fortune/ledger/ledger-category';
import LedgerEntryService from '@/services/libra-fortune/ledger/ledger-entry';
import LedgerMemberService from '@/services/libra-fortune/ledger/ledger-member';
import LedgerTagService from '@/services/libra-fortune/user/ledger-tag';
import { getMoneySymbol } from '@/utils/money-utils';
import {
  FormListActionType,
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
import { useParams } from '@umijs/max';
import dayjs from 'dayjs';
import { useEffect, useRef, useState } from 'react';
import LedgerEntryDetail from './entry-detail';

const LedgerEntry: React.FC = () => {
  const params = useParams();
  const formListActionRef = useRef<FormListActionType>();

  const [ledger, setLedger] = useState<API.Ledger.LedgerDTO>();
  const [categories, setCategories] = useState<RequestOptionsType[]>([]);
  const [members, setMembers] = useState<RequestOptionsType[]>([]);
  const [myTags, setMyTags] = useState<RequestOptionsType[]>([]);
  const [membersVo, setMembersV0] = useState<API.Ledger.LedgerMemberVO[]>([]);
  const [membersMap, setMembersMap] = useState<Record<string, API.Ledger.LedgerMemberVO>>();

  useEffect(() => {
    if (!params.ledgerId) {
      return;
    }

    new LedgerService().get(params.ledgerId!).then((response) => {
      setLedger(response.data);
    });

    new LedgerCategoryService(params.ledgerId!).list().then((response) => {
      const categories: RequestOptionsType[] = [];
      response.data.forEach((record) => {
        categories.push({ label: record.name, value: record.id! });
      });
      setCategories(categories);
    });

    new LedgerMemberService(params.ledgerId!).list().then((response) => {
      const members: RequestOptionsType[] = [];
      const membersMap: Record<string, API.Ledger.LedgerMemberVO> = {};
      response.data.forEach((record) => {
        members.push({ label: <UserNicknameWithAvatar user={record} />, value: record.userId });
        membersMap[record.userId] = record;
      });
      setMembers(members);
      setMembersMap(membersMap);
      setMembersV0(response.data);
    });

    new LedgerTagService().list().then((response) => {
      const myTags: RequestOptionsType[] = [];
      response.data.forEach((record) => {
        myTags.push({ label: record.name, value: record.id! });
      });
      setMyTags(myTags);
    });
  }, []);

  const initialLedgerDetailValue = () => {
    const result: Omit<API.Ledger.LedgerEntryDetailDTO, 'amount'>[] = [];
    membersVo.forEach((record) => {
      result.push({
        userId: record.userId,
        paymentChannelId: record.paymentChannels?.[0]?.id ?? '',
        fundedRatio: record.defaultFundedRatio,
      });
    });
    return result;
  };

  const currencyType = [
    { label: '人民币', value: 'CNY' },
    { label: '美元', value: 'USD' },
    { label: '欧元', value: 'EUR' },
  ];

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
      request: async () => categories,
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
          hidden
          name="ledgerId"
          initialValue={params.ledgerId!}
          rules={[{ max: 36, required: true }]}
          tooltip="最长36位"
          width="xl"
        />
        <ProForm.Group size={8}>
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
        <ProForm.Group size={8}>
          <ProFormSelect
            label="条目分类"
            name="categoryId"
            placeholder="请选择条目分类"
            request={async () => categories}
            rules={[{ required: true }]}
            tooltip="请选择条目分类"
            width="sm"
          />
          <ProFormSelect
            label="货币类型"
            name="currencyType"
            initialValue="CNY"
            options={currencyType}
            placeholder="请选择货币类型"
            rules={[{ required: true }]}
            tooltip="请选择货币类型"
            width="xs"
          />
          <ProFormDigit
            label="金额总计"
            name="total"
            placeholder="请输入金额总计"
            rules={[{ required: true }]}
            tooltip="请输入金额总计"
            width="sm"
            fieldProps={{
              precision: 2,
              onChange(value: number | null) {
                const details = formListActionRef.current?.getList();
                if (!details || details.length === 0) {
                  return;
                }

                const total = value ?? 0;
                let sum = 0;
                details.forEach((detail: API.Ledger.LedgerEntryDetailDTO) => {
                  const fundedRatio = detail?.fundedRatio ?? 0;
                  detail.amount = Math.floor(total * fundedRatio) / 100;
                  sum += detail.amount;
                });

                // 将剩下的1分钱加到Owner头上，如果Owner不在列表中，加在第1个人头上
                const rest = total - sum;
                details[0].amount += rest;
              },
            }}
          />
        </ProForm.Group>
        <ProFormTextArea
          label="备注"
          name="remark"
          initialValue=""
          placeholder="请输入备注"
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
          initialValue={initialLedgerDetailValue()}
          actionRef={formListActionRef}
          required
          min={1}
          max={members.length}
        >
          <ProForm.Group>
            <ProFormSelect
              label="分担人"
              name="userId"
              placeholder="请选择分担人"
              request={async () => members}
              rules={[{ required: true }]}
              tooltip="请选择分担人"
              width="xs"
            />
            <ProFormDependency name={['userId']}>
              {({ userId }) => {
                const options: API.EnumDTO[] = [];
                membersMap?.[userId]?.paymentChannels?.forEach((paymentChannel) => [
                  options.push({ label: paymentChannel.name, value: paymentChannel.id! }),
                ]);
                return (
                  <ProFormSelect
                    label="支出渠道"
                    name="paymentChannelId"
                    placeholder="请选择支出渠道"
                    options={options}
                    rules={[{ required: true }]}
                    tooltip="请选择支出渠道"
                    width="sm"
                  />
                );
              }}
            </ProFormDependency>
            {/* </ProForm.Group>
          <ProForm.Group> */}
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
            <ProFormDependency name={['total']} ignoreFormListField>
              {({ total }) => {
                return (
                  <ProFormDependency name={['fundedRatio']}>
                    {({ fundedRatio }) => {
                      return (
                        <ProFormDigit
                          label="分担金额"
                          name="amount"
                          initialValue={(total ?? 0) * (fundedRatio ?? 0)}
                          placeholder="请输入分担金额"
                          fieldProps={{ precision: 2 }}
                          rules={[{ required: true }]}
                          tooltip="请输入分担金额"
                          width="xs"
                        />
                      );
                    }}
                  </ProFormDependency>
                );
              }}
            </ProFormDependency>
          </ProForm.Group>
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
                      request={async () => myTags}
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

  const tableValueToFormValue = (record: API.Ledger.LedgerEntryVO): API.Ledger.LedgerEntryDTO => {
    const details: API.Ledger.LedgerEntryDetailDTO[] = [];
    record.details.forEach((detailVo) => {
      const detail: API.Ledger.LedgerEntryDetailDTO = {
        id: detailVo.id!,
        entryId: record.id!,
        userId: detailVo.user.id!,
        paymentChannelId: detailVo.paymentChannel.id!,
        fundedRatio: detailVo.fundedRatio,
        amount: detailVo.amount,
      };
      if (detailVo.tags && detailVo.tags.length > 0) {
        const tags: string[] = [];
        detailVo.tags.forEach((tagVo) => {
          tags.push(tagVo.id!);
        });
        detail.tags = tags;
      }
      details.push(detail);
    });
    const result: API.Ledger.LedgerEntryDTO = {
      id: record.id!,
      ledgerId: params.ledgerId!,
      categoryId: record.category.id!,
      date: record.date,
      name: record.name,
      total: record.total,
      currencyType: record.currencyType,
      remark: record.remark,
      details: details,
    };
    return result;
  };

  return (
    <PageContainer content={`当前帐本：${ledger?.name}`}>
      <BasePage<API.Ledger.LedgerEntryDTO, API.Ledger.LedgerEntryQuery, API.Ledger.LedgerEntryVO>
        pageName="账本条目"
        service={new LedgerEntryService(params.ledgerId!)}
        columns={columns as ProColumns<API.Ledger.LedgerEntryDTO | API.Ledger.LedgerEntryVO>[]}
        formItem={formItem()}
        tableValueToFormValue={tableValueToFormValue}
        tableProps={{
          size: 'small',
        }}
      />
    </PageContainer>
  );
};

export default LedgerEntry;
