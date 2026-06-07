declare namespace GalaxyWeb {
  type ApiResponse<T> = {
    code: number;
    message: string;
    data: T;
  }
  type PaginationResponse<T> = {
    list: T[];
    current: number;
    pageSize: number;
    total: number;
  }
  type EmptyQuery = {}
  type PaginationQuery = {
    current: number;
    pageSize: number;
    noPage?: boolean;
  }
  type BaseDTO = {
    id?: number;
  }
  type BaseCreateDTO = BaseDTO & {
    createTime: string;
    createBy: string;
  }
  type BaseCreateUpdateDTO = BaseCreateDTO & {
    updateTime: string;
    updateBy: string;
  }
  type EnumDTO<T = string> = {
    label: string;
    value: T;
    children?: EnumDTO<T>[];
  }
}
