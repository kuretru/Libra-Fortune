// @ts-ignore
/* eslint-disable */

declare namespace API.Ledger {
  type LedgerTagDTO = API.BaseDTO & {
    userId: string;
    name: string;
  };

  type LedgerTagQuery = API.PaginationQuery & {
    userId?: string;
    name?: string;
  };
}
