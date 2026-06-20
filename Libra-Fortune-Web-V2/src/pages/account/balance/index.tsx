import {
  type ActionType,
  PageContainer,
  type ProColumns,
  ProTable,
  type ProTableProps,
} from '@ant-design/pro-components';
import React, { useMemo, useRef, useState } from 'react';
import { list } from '@/services/libra-fortune/account/balance';
import { formatNumber } from '@/utils/format';

type AccountBalanceSearchParams = LibraFortune.Account.AccountBalanceQuery & {
  dateRange?: string[];
};

type AccountBalanceTableRecord = {
  key: string;
  date: string;
  totalBalance: string;
  [accountBalance: `account-${number}`]: string | undefined;
};

const formatBalance = (value?: string) =>
  value === undefined ? '-' : formatNumber(value);

const AccountBalance: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [accounts, setAccounts] = useState<LibraFortune.Account.AccountDTO[]>(
    [],
  );

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
        renderText: formatBalance,
      },
      ...accounts.map<ProColumns<AccountBalanceTableRecord>>((account) => ({
        dataIndex: `account-${account.id}`,
        title: account.name,
        align: 'right',
        search: false,
        width: 140,
        renderText: formatBalance,
      })),
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

    return {
      data: result.balances.map((balance) => ({
        key: balance.date,
        date: balance.date,
        totalBalance: balance.totalBalance,
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

  return (
    <PageContainer>
      <ProTable<AccountBalanceTableRecord, AccountBalanceSearchParams>
        actionRef={actionRef}
        columns={columns}
        request={onRequest}
        rowKey="key"
        pagination={false}
        scroll={{ x: 'max-content' }}
        search={{ labelWidth: 'auto' }}
        headerTitle="账户余额快照"
      />
    </PageContainer>
  );
};

export default AccountBalance;
