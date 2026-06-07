declare namespace LibraFortune.Account {
  type AccountDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    owner?: string;
    name: string;
    icon?: string;
  }
}
