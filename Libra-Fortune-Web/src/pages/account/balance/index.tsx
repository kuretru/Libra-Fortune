import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons';
import {
  type ActionType,
  ModalForm,
  PageContainer,
  type ProColumns,
  ProFormDatePicker,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import { Button, Form, Input, message, Popconfirm, Space } from 'antd';
import dayjs, { type Dayjs } from 'dayjs';
import React, { useEffect, useMemo, useRef, useState } from 'react';
import { list, save } from '@/services/libra-fortune/account/balance';
import { formatAmount } from '@/utils/format';

type AccountBalanceSearchParams = LibraFortune.Account.AccountBalanceQuery & {
  dateRange?: string[];
};

type AccountBalanceFormValues = {
  date?: Dayjs | string;
  balances?: Record<string, string | null>;
};

type AccountBalanceTableRecord = {
  key: string;
  date: string;
  totalBalance: string;
  items: LibraFortune.Account.AccountBalanceItemDTO[];
  [accountBalance: `account-${number}`]: string | undefined;
};

const formatBalance = (value?: string) =>
  value === undefined ? '-' : formatAmount(value);

const formatDate = (value: Dayjs | string) =>
  dayjs.isDayjs(value) ? value.format('YYYY-MM-DD') : value;

const buildBalanceValues = (
  items?: LibraFortune.Account.AccountBalanceItemDTO[],
) =>
  Object.fromEntries(
    (items ?? []).map((item) => [item.accountId.toString(), item.balance]),
  );

const buildLatestBalanceValues = (
  balances: LibraFortune.Account.AccountBalanceDateDTO[],
) => {
  const latestBalance = balances.reduce<
    LibraFortune.Account.AccountBalanceDateDTO | undefined
  >(
    (latest, balance) =>
      latest === undefined || balance.date > latest.date ? balance : latest,
    undefined,
  );

  return buildBalanceValues(latestBalance?.items);
};

const balanceRules = [
  {
    pattern: /^\d+(\.\d{1,2})?$/,
    message: '请输入非负金额，最多两位小数',
  },
];

const AccountBalance: React.FC = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const actionRef = useRef<ActionType | null>(null);
  const [form] = Form.useForm<AccountBalanceFormValues>();
  const [modalVisible, setModalVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<
    AccountBalanceTableRecord | undefined
  >(undefined);
  const [accounts, setAccounts] = useState<LibraFortune.Account.AccountDTO[]>(
    [],
  );
  const [latestBalanceValues, setLatestBalanceValues] = useState<
    Record<string, string>
  >({});

  useEffect(() => {
    if (!modalVisible) return;

    if (currentRecord) {
      form.setFieldsValue({
        date: dayjs(currentRecord.date),
        balances: buildBalanceValues(currentRecord.items),
      });
    } else {
      form.resetFields();
      form.setFieldsValue({ date: dayjs(), balances: latestBalanceValues });
    }
  }, [modalVisible, currentRecord, form, latestBalanceValues]);

  const columns = useMemo<ProColumns<AccountBalanceTableRecord>[]>(
    () => [
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
        dataIndex: 'totalBalance',
        title: '总余额',
        align: 'right',
        fixed: 'left',
        width: 120,
        search: false,
        render: (_, record) => formatBalance(record.totalBalance),
      },
      ...accounts.map<ProColumns<AccountBalanceTableRecord>>((account) => {
        const dataIndex = `account-${account.id!}` as const;
        return {
          dataIndex,
          title: account.name,
          align: 'right',
          search: false,
          width: 140,
          render: (_, record) => formatBalance(record[dataIndex]),
        };
      }),
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
              title="确认删除该日期的余额快照？"
              okText="删除"
              cancelText="取消"
              onConfirm={() => onRemoveButtonClick(record)}
            >
              <Button icon={<DeleteOutlined />} danger>
                删除
              </Button>
            </Popconfirm>
          </Space>
        ),
      },
    ],
    [accounts],
  );

  const onRequest: NonNullable<
    ProTableProps<
      AccountBalanceTableRecord,
      AccountBalanceSearchParams
    >['request']
  > = async (params) => {
    const response = await list({
      dateBegin: params.dateBegin,
      dateEnd: params.dateEnd,
    });
    const result = response.data;
    setAccounts(result.accounts);
    setLatestBalanceValues(buildLatestBalanceValues(result.balances));

    return {
      data: result.balances.map((balance) => ({
        key: balance.date,
        date: balance.date,
        totalBalance: balance.totalBalance,
        items: balance.items,
        ...Object.fromEntries(
          balance.items.map((item) => [
            `account-${item.accountId}`,
            item.balance,
          ]),
        ),
      })),
      success: response.code < 1000,
      total: result.balances.length,
    };
  };

  const onFinish = async (
    values: AccountBalanceFormValues,
  ): Promise<boolean> => {
    if (!values.date) {
      return false;
    }

    const balances = accounts
      .map((account) => ({
        accountId: account.id!,
        balance: values.balances?.[account.id!.toString()],
      }))
      .filter(
        (item): item is LibraFortune.Account.AccountBalanceItemDTO =>
          item.balance !== undefined &&
          item.balance !== null &&
          item.balance !== '',
      );

    try {
      await save({
        date: formatDate(values.date),
        balances,
      });
      actionRef.current?.reload();
      messageApi.open({
        type: 'success',
        content: currentRecord ? '更新成功' : '新增成功',
      });
      return true;
    } catch {
      return false;
    }
  };

  const onCreateButtonClick = async () => {
    setCurrentRecord(undefined);
    try {
      const response = await list({});
      setAccounts(response.data.accounts);
      setLatestBalanceValues(buildLatestBalanceValues(response.data.balances));
    } catch {
      // The modal can still open with the balances already loaded in the table.
    }
    setModalVisible(true);
  };

  const onUpdateButtonClick = (record: AccountBalanceTableRecord) => {
    setCurrentRecord(record);
    setModalVisible(true);
  };

  const onRemoveButtonClick = (record: AccountBalanceTableRecord) => {
    save({
      date: record.date,
      balances: [],
    }).then(() => {
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
      <ProTable<AccountBalanceTableRecord, AccountBalanceSearchParams>
        actionRef={actionRef}
        columns={columns}
        defaultSize="small"
        request={onRequest}
        rowKey="key"
        pagination={false}
        scroll={{ x: 'max-content' }}
        search={{ labelWidth: 'auto' }}
        headerTitle="账户余额快照"
        toolBarRender={() => [
          <Button
            key="create"
            type="primary"
            icon={<PlusOutlined />}
            onClick={onCreateButtonClick}
          >
            新增快照
          </Button>,
        ]}
      />
      <ModalForm<AccountBalanceFormValues>
        form={form}
        title={currentRecord ? '编辑余额快照' : '新增余额快照'}
        open={modalVisible}
        layout="horizontal"
        labelAlign="left"
        labelCol={{ flex: '120px' }}
        wrapperCol={{ flex: 1 }}
        onOpenChange={(open) => {
          setModalVisible(open);
          if (!open) {
            setCurrentRecord(undefined);
          }
        }}
        onFinish={onFinish}
        modalProps={{
          destroyOnHidden: true,
          width: 560,
        }}
      >
        <ProFormDatePicker
          name="date"
          label="快照日期"
          rules={[{ required: true }]}
          fieldProps={{
            disabled: !!currentRecord,
            format: 'YYYY-MM-DD',
          }}
        />
        {accounts.map((account) => (
          <Form.Item
            key={account.id}
            name={['balances', account.id!.toString()]}
            label={account.name}
            labelAlign="left"
            rules={balanceRules}
            style={{ marginBottom: 12 }}
          >
            <Input allowClear inputMode="decimal" />
          </Form.Item>
        ))}
      </ModalForm>
    </PageContainer>
  );
};

export default AccountBalance;
