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
    categoryId: number;
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
    paymentChain?: number[];
    fundedRatio: string;
    amount: string;
  }

  type LedgerQuery = {
    nameLike?: string;
  }

  type LedgerEntryQuery = {
    categoryIdIn?: number[];
    dateBegin?: string;
    dateEnd?: string;
    nameLike?: string;
    originalCurrency?: string;
    settlementCurrency?: string;
    tagIdIn?: number[];
  }
}
