declare namespace LibraFortune.Metadata {
  type CurrencyDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    code: string;
    symbol: string;
    name: string;
  }

  type CategoryDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    parentId?: number;
    name: string;
    icon?: string;
    children?: CategoryDTO[];
  }

  type TagSetDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    name: string;
    required: boolean;
    allowMultiple: boolean;
    items?: TagSetItemDTO[];
  }

  type TagSetItemDTO = GalaxyWeb.BaseCreateUpdateDTO & {
    setId?: number;
    name: string;
  }

  type TagSetQuery = {
    nameLike?: string;
    required?: boolean;
    allowMultiple?: boolean;
    tagNameLike?: string;
  }
}
