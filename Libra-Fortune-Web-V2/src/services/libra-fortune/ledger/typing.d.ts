declare namespace LibraFortune.Ledger {
  type LedgerDTO = GalaxyWeb.BaseDTO & {
    owner?: string;
    name: string;
    members?: LedgerMemberDTO[];
  }

  type LedgerMemberDTO = GalaxyWeb.BaseDTO & {
    ledgerId?: number;
    username: string;
    defaultFundedRatio: number;
  }

  type LedgerQuery = {
    nameLike?: string;
  }
}
