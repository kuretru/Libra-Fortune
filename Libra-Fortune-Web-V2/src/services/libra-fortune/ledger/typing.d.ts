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
    week?: string;
    day?: string;
    ledgerId?: number;
    categoryIdL1?: number;
    categoryIdL2?: number;
    type?: string;
    username?: string;
    tagItemId?: number;
    originalSum?: string;
    settlementSum?: string;
    fundedSum?: string;
  }

  type DashboardTimeDimension = 'year' | 'month' | 'week' | 'day';

  type DashboardMetric = 'originalSum' | 'settlementSum' | 'fundedSum';

  type DashboardDimension =
    | 'ledgerId'
    | 'categoryIdL1'
    | 'categoryIdL2'
    | 'type'
    | 'username'
    | 'tagItemId';

  type DashboardFilterLogic = 'and' | 'or';

  type DashboardFilterOperator =
    | 'equal'
    | 'not_equal'
    | 'in'
    | 'not_in'
    | 'like'
    | 'not_like'
    | 'gt'
    | 'gte'
    | 'lt'
    | 'lte';

  type DashboardFilterQuery<T extends string> = {
    logic?: DashboardFilterLogic;
    children?: DashboardFilterQuery<T>[];
    name?: T;
    operator?: DashboardFilterOperator;
    values?: string[];
  }

  type DashboardOrderBy = {
    type: 'time' | 'metric' | 'dimension';
    name: string;
    mode: 'asc' | 'desc';
  }

  type DashboardQuery = {
    time: {
      dateBegin: string;
      dateEnd: string;
      dimension: DashboardTimeDimension;
    };
    metrics: DashboardMetric[];
    dimensions?: DashboardDimension[];
    metricsFilter?: DashboardFilterQuery<DashboardMetric>;
    dimensionsFilter?: DashboardFilterQuery<DashboardDimension>;
    orderBy?: DashboardOrderBy[];
    limit?: number;
  }

  type DashboardEnumDTO = {
    timeDimensions: GalaxyWeb.EnumDTO<DashboardTimeDimension>[];
    metrics: GalaxyWeb.EnumDTO<DashboardMetric>[];
    dimensions: GalaxyWeb.EnumDTO<DashboardDimension>[];
    filterLogics: GalaxyWeb.EnumDTO<DashboardFilterLogic>[];
    filterOperators: GalaxyWeb.EnumDTO<DashboardFilterOperator>[];
    orderByTypes: GalaxyWeb.EnumDTO<DashboardOrderBy['type']>[];
    orderByModes: GalaxyWeb.EnumDTO<DashboardOrderBy['mode']>[];
  }

  type DashboardAccountBalanceBO = {
    date?: string;
    totalBalance?: string;
  }
}
