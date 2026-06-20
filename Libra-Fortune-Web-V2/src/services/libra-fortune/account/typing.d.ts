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

  type AccountBalanceQuery = {
    dateBegin?: string;
    dateEnd?: string;
  }

  type AccountBalanceItemDTO = {
    accountId: number;
    balance: string;
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
