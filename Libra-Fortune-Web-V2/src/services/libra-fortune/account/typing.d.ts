declare namespace LibraFortune.Account {
  type AccountDTO = GalaxyWeb.BaseDTO & {
    owner?: string;
    name: string;
    icon?: string;
  }
}
