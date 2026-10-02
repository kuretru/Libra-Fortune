declare namespace LibraFortune.Account {
  type AccountDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    owner?: string;
    name: string;
    canHoldFunds: boolean;
    icon?: string;
  }

  type AccountQuery = {
    owner?: string;
    nameLike?: string;
    canHoldFunds?: boolean;
  }

  type AccountTransferDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    owner?: string;
    date: string;
    name: string;
    sourceAccountId: number;
    sourceAmount: string;
    sourceCurrency: string;
    targetAccountId: number;
    targetAmount: string;
    targetCurrency: string;
    remark?: string;
  }

  type AccountTransferQuery = {
    dateBegin?: string;
    dateEnd?: string;
    nameLike?: string;
    sourceAccountId?: number;
    sourceCurrency?: string;
    targetAccountId?: number;
    targetCurrency?: string;
  }

  type AccountBalanceQuery = {
    dateBegin?: string;
    dateEnd?: string;
  }

  type AccountBalanceItemDTO = {
    accountId: number;
    balance: string;
  }

  type AccountBalanceRequest = {
    date: string;
    balances: AccountBalanceItemDTO[];
  }

  type AccountBalanceDateDTO = {
    date: string;
    totalBalance: string;
    items: AccountBalanceItemDTO[];
  }

  type AccountBalanceResultDTO = {
    accounts: AccountDTO[];
    balances: AccountBalanceDateDTO[];
  }
}
