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

  type LedgerQuery = {
    nameLike?: string;
  }
}
