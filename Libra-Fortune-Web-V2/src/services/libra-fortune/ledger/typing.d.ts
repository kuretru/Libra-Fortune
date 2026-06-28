declare namespace LibraFortune.Ledger {
  type LedgerDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    owner?: string;
    name: string;
    members?: LedgerMemberDTO[];
  }

  type LedgerMemberDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    ledgerId?: number;
    username: string;
    defaultFundedRatio: string;
  }

  type LedgerEntryDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    ledgerId?: number;
    categoryIdL1: number;
    categoryIdL2: number;
    type: string;
    date: string;
    name: string;
    originalAmount: string;
    originalCurrency: string;
    settlementAmount: string;
    settlementCurrency: string;
    remark?: string;
    tags?: LedgerEntryTagDTO[];
    details?: LedgerEntryDetailDTO[];
  }

  type LedgerEntryTagDTO = GalaxyWeb.BaseCreateDTO & {
    ledgerId?: number;
    tagId: number;
  }

  type LedgerEntryDetailDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    entryId?: number;
    username: string;
    lockType: string;
    paymentChain?: number[];
    fundedRatio: string;
    amount: string;
  }

  type LedgerEnumDTO = {
    entryTypes: GalaxyWeb.EnumDTO<string>[];
    detailLockTypes: GalaxyWeb.EnumDTO<string>[];
  }

  type LedgerQuery = {
    nameLike?: string;
  }

  type LedgerEntryQuery = {
    categoryIdL1?: number;
    categoryIdL2?: number;
    type?: string;
    dateBegin?: string;
    dateEnd?: string;
    nameLike?: string;
    originalCurrency?: string;
    settlementCurrency?: string;
    tagIdIn?: number[];
  }

  type DashboardLedgerBO = {
    year?: string;
    month?: string;
    day?: string;
    ledgerId?: number;
    categoryIdL1?: number;
    categoryIdL2?: number;
    type?: string;
    username?: string;
    tagId?: number;
    sum: string;
  }

  type DashboardLedgerQuery = {
    dateBegin: string;
    dateEnd: string;
    sumMode: string;
    groupBy: string[];
    filter?: DashboardFilter;
  }

  type DashboardFilter = {
    ledgerId?: number[];
    categoryIdL1?: number[];
    categoryIdL2?: number[];
    type?: string[];
    username?: string[];
    tagId?: number[];
    tagSetId?: number[];
  }

  type DashboardAccountBalanceBO = {
    date?: string;
    totalBalance?: string;
  }
}
