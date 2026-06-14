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
}
