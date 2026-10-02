import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormDatePicker,
  ProFormDigit,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import { Button, Form, message, Popconfirm, Space } from 'antd';
import dayjs, { type Dayjs } from 'dayjs';
import React, { useEffect, useMemo, useRef, useState } from 'react';
import * as accountApi from '@/services/libra-fortune/account/account';
import * as transferApi from '@/services/libra-fortune/account/transfer';
import * as currencyApi from '@/services/libra-fortune/metadata/currency';
import { formatAmount } from '@/utils/format';

type AccountTransferSearchParams = LibraFortune.Account.AccountTransferQuery & {
  dateRange?: string[];
};

type AccountTransferFormValues = Partial<
  Omit<LibraFortune.Account.AccountTransferDTO, 'date'>
> & {
  date?: Dayjs | string;
};

const toDateString = (value: Dayjs | string) =>
  dayjs.isDayjs(value) ? value.format('YYYY-MM-DD') : value;

const AccountTransfer: React.FC = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const actionRef = useRef<ActionType | null>(null);
  const [form] = Form.useForm<AccountTransferFormValues>();
  const [modalVisible, setModalVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<
    LibraFortune.Account.AccountTransferDTO | undefined
  >(undefined);
  const [accounts, setAccounts] = useState<LibraFortune.Account.AccountDTO[]>(
    [],
  );
  const [currencyOptions, setCurrencyOptions] = useState<
    GalaxyWeb.EnumDTO<string>[]
  >([]);

  useEffect(() => {
    Promise.all([
      accountApi.list({
        current: 1,
        pageSize: 1000,
        noPage: true,
      }),
      currencyApi.enums(),
    ]).then(([accountResponse, currencyResponse]) => {
      setAccounts(accountResponse.data.list);
      setCurrencyOptions(currencyResponse.data);
    });
  }, []);

  const accountNameMap = useMemo(
    () => new Map(accounts.map((account) => [account.id, account.name])),
    [accounts],
  );

  const accountOptions = useMemo(
    () =>
      accounts.map((account) => ({
        label: account.name,
        value: account.id!,
      })),
    [accounts],
  );

  const transferAccountOptions = useMemo(
    () =>
      accounts
        .filter((account) => account.canHoldFunds)
        .map((account) => ({
          label: account.name,
          value: account.id!,
        })),
    [accounts],
  );

  const columns: ProColumns<LibraFortune.Account.AccountTransferDTO>[] = [
    {
      dataIndex: 'date',
      title: '日期',
      valueType: 'date',
      fixed: 'left',
      width: 112,
      search: false,
    },
    {
      dataIndex: 'dateRange',
      title: '日期范围',
      valueType: 'dateRange',
      hideInTable: true,
      search: {
        transform: (value: string[]) => ({
          dateBegin: value?.[0],
          dateEnd: value?.[1],
        }),
      },
    },
    {
      dataIndex: 'name',
      title: '转账名称',
      copyable: true,
      width: 160,
      search: {
        transform: (value: string) => ({ nameLike: value }),
      },
    },
    {
      dataIndex: 'sourceAccountId',
      title: '转出账户',
      valueType: 'select',
      fieldProps: {
        options: accountOptions,
      },
      width: 140,
      renderText: (value: number) => accountNameMap.get(value) ?? value,
    },
    {
      dataIndex: 'sourceAmount',
      title: '转出金额',
      align: 'right',
      search: false,
      width: 140,
      render: (_, record) => (
        <span>
          {formatAmount(record.sourceAmount)} {record.sourceCurrency}
        </span>
      ),
    },
    {
      dataIndex: 'sourceCurrency',
      title: '转出货币',
      valueType: 'select',
      hideInTable: true,
      fieldProps: {
        options: currencyOptions,
      },
    },
    {
      dataIndex: 'targetAccountId',
      title: '转入账户',
      valueType: 'select',
      fieldProps: {
        options: accountOptions,
      },
      width: 140,
      renderText: (value: number) => accountNameMap.get(value) ?? value,
    },
    {
      dataIndex: 'targetAmount',
      title: '转入金额',
      align: 'right',
      search: false,
      width: 140,
      render: (_, record) => (
        <span>
          {formatAmount(record.targetAmount)} {record.targetCurrency}
        </span>
      ),
    },
    {
      dataIndex: 'targetCurrency',
      title: '转入货币',
      valueType: 'select',
      hideInTable: true,
      fieldProps: {
        options: currencyOptions,
      },
    },
    {
      dataIndex: 'remark',
      title: '备注',
      search: false,
      ellipsis: true,
      width: 200,
      renderText: (value?: string) => value || '-',
    },
    {
      key: 'action',
      title: '操作',
      fixed: 'right',
      valueType: 'option',
      width: 180,
      render: (_, record) => (
        <Space>
          <Button
            icon={<EditOutlined />}
            onClick={() => onUpdateButtonClick(record)}
          >
            编辑
          </Button>
          <Popconfirm
            title="确认删除该转账记录？"
            okText="删除"
            cancelText="取消"
            onConfirm={() => onRemoveButtonClick(record.id!)}
          >
            <Button icon={<DeleteOutlined />} danger>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const onRequest: NonNullable<
    ProTableProps<
      LibraFortune.Account.AccountTransferDTO,
      AccountTransferSearchParams
    >['request']
  > = async (params) => {
    const response = await transferApi.list({
      current: params.current ?? 1,
      pageSize: params.pageSize ?? 20,
      dateBegin: params.dateBegin,
      dateEnd: params.dateEnd,
      nameLike: params.nameLike,
      sourceAccountId: params.sourceAccountId,
      sourceCurrency: params.sourceCurrency,
      targetAccountId: params.targetAccountId,
      targetCurrency: params.targetCurrency,
    });

    return {
      data: response.data.list,
      success: response.code < 1000,
      total: response.data.total,
    };
  };

  const onFinish = async (
    values: AccountTransferFormValues,
  ): Promise<boolean> => {
    if (!values.date) {
      return false;
    }
    if (
      values.sourceAccountId === values.targetAccountId &&
      values.sourceCurrency === values.targetCurrency
    ) {
      messageApi.open({
        type: 'error',
        content: '相同账户仅允许不同货币之间换汇',
      });
      return false;
    }

    const record: LibraFortune.Account.AccountTransferDTO = {
      id: values.id,
      date: toDateString(values.date),
      name: values.name!,
      sourceAccountId: values.sourceAccountId!,
      sourceAmount: values.sourceAmount!,
      sourceCurrency: values.sourceCurrency!,
      targetAccountId: values.targetAccountId!,
      targetAmount: values.targetAmount!,
      targetCurrency: values.targetCurrency!,
      remark: values.remark,
    };

    try {
      const fn = record.id ? transferApi.update : transferApi.create;
      await fn(record);
      actionRef.current?.reload();
      messageApi.open({
        type: 'success',
        content: record.id ? '更新成功' : '新增成功',
      });
      return true;
    } catch {
      return false;
    }
  };

  const onCreateButtonClick = () => {
    const defaultCurrency =
      currencyOptions.find((option) => option.value === 'CNY')?.value ??
      currencyOptions[0]?.value;
    setCurrentRecord(undefined);
    form.resetFields();
    form.setFieldsValue({
      date: dayjs(),
      sourceCurrency: defaultCurrency,
      targetCurrency: defaultCurrency,
    });
    setModalVisible(true);
  };

  const onUpdateButtonClick = (
    record: LibraFortune.Account.AccountTransferDTO,
  ) => {
    setCurrentRecord(record);
    form.resetFields();
    form.setFieldsValue({
      ...record,
      date: dayjs(record.date),
    });
    setModalVisible(true);
  };

  const onRemoveButtonClick = (id: number) => {
    transferApi.remove(id).then(() => {
      actionRef.current?.reload();
      messageApi.open({
        type: 'success',
        content: '删除成功',
      });
    });
  };

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<
        LibraFortune.Account.AccountTransferDTO,
        AccountTransferSearchParams
      >
        actionRef={actionRef}
        columns={columns}
        defaultSize="small"
        request={onRequest}
        rowKey="id"
        search={{ labelWidth: 'auto' }}
        scroll={{ x: 1200 }}
        headerTitle="账户转账记录"
        toolBarRender={() => [
          <Button
            key="create"
            type="primary"
            icon={<PlusOutlined />}
            onClick={onCreateButtonClick}
          >
            新增转账
          </Button>,
        ]}
      />
      <ModalForm<AccountTransferFormValues>
        form={form}
        title={currentRecord?.id ? '编辑转账' : '新增转账'}
        open={modalVisible}
        onOpenChange={(open) => {
          setModalVisible(open);
          if (!open) {
            setCurrentRecord(undefined);
          }
        }}
        onFinish={onFinish}
        modalProps={{
          destroyOnHidden: true,
          width: 760,
        }}
      >
        <ProFormText name="id" label="ID" hidden />
        <Space align="baseline" size="middle" wrap>
          <ProFormDatePicker
            name="date"
            label="转账日期"
            rules={[{ required: true }]}
            fieldProps={{
              disabledDate: (current: Dayjs) => current.isAfter(dayjs(), 'day'),
              format: 'YYYY-MM-DD',
            }}
          />
          <ProFormText
            name="name"
            label="转账名称"
            placeholder="请输入转账名称"
            fieldProps={{ style: { width: 320 } }}
            rules={[
              { required: true, whitespace: true },
              { max: 32, message: '转账名称不能超过32个字符' },
            ]}
          />
        </Space>
        <Space align="baseline" size="middle" wrap>
          <ProFormSelect
            name="sourceAccountId"
            label="转出账户"
            options={transferAccountOptions}
            fieldProps={{ style: { width: 180 } }}
            rules={[{ required: true }]}
          />
          <ProFormDigit
            name="sourceAmount"
            label="转出金额"
            min={0.01}
            fieldProps={{
              precision: 2,
              stringMode: true,
              step: '0.01',
              style: { width: 180 },
            }}
            rules={[{ required: true }]}
          />
          <ProFormSelect
            name="sourceCurrency"
            label="转出货币"
            options={currencyOptions}
            fieldProps={{ style: { width: 140 } }}
            rules={[{ required: true }]}
          />
        </Space>
        <Space align="baseline" size="middle" wrap>
          <ProFormSelect
            name="targetAccountId"
            label="转入账户"
            options={transferAccountOptions}
            fieldProps={{ style: { width: 180 } }}
            rules={[{ required: true }]}
          />
          <ProFormDigit
            name="targetAmount"
            label="转入金额"
            min={0.01}
            fieldProps={{
              precision: 2,
              stringMode: true,
              step: '0.01',
              style: { width: 180 },
            }}
            rules={[{ required: true }]}
          />
          <ProFormSelect
            name="targetCurrency"
            label="转入货币"
            options={currencyOptions}
            fieldProps={{ style: { width: 140 } }}
            rules={[{ required: true }]}
          />
        </Space>
        <ProFormTextArea
          name="remark"
          label="备注"
          placeholder="可记录手续费等补充信息"
        />
      </ModalForm>
    </PageContainer>
  );
};

export default AccountTransfer;
