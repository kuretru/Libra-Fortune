declare namespace LibraFortune.Metadata {
  type CurrencyDTO = GalaxyWeb.BaseDTO & {
    code: string;
    symbol: string;
    name: string;
  }

  type CategoryDTO = GalaxyWeb.BaseDTO & {
    parentId?: number;
    name: string;
    icon?: string;
    children?: CategoryDTO[];
  }
}
