// @ts-ignore
/* eslint-disable */

declare namespace API.Ledger {
  type LedgerTagDTO = API.BaseDTO & {
    name: string;
  };

  type LedgerTagQuery = API.PaginationQuery & {
    name?: string;
  };
}
