declare namespace API.Ledger {
  type LedgerDTO = API.BaseDTO & {
    name: string;
    ownerId: string;
    remark: string;
    type: string;
  };

  type LedgerQuery = API.PaginationQuery & {
    name?: string;
    ownerId?: string;
    type?: string;
  };

  type LedgerCategoryDTO = API.BaseDTO & {
    ledgerId: string;
    name: string;
  };

  type LedgerCategoryQuery = API.PaginationQuery & {
    ledgerId?: string;
    name?: string;
  };

  type LedgerUserDTO = API.BaseDTO & {
    ledgerId: string;
    userId: string;
    isWritable: boolean;
  };

  type LedgerUserQuery = API.PaginationQuery & {
    ledgerId: string;
    userId: string;
  };
}
