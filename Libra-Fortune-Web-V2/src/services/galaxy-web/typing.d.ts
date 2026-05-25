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
    createTime: string;
    createBy: string;
    updateTime: string;
    updateBy: string;
  }
  type EnumDTO = {
    label: string;
    value: string;
  }
}
