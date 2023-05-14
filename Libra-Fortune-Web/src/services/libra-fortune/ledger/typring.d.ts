// @ts-ignore
/* eslint-disable */

declare namespace API.Ledger {
  type LedgerDTO = API.BaseDTO & {
    ownerId: string;
    name: string;
    type: string;
    remark: string;
  };

  type LedgerQuery = API.PaginationQuery & {
    ownerId?: string;
    name?: string;
    type?: string;
  };

  type LedgerVO = API.BaseDTO & {
    name: string;
    type: string;
    remark: string;
    ownerId: string;
    ownerNickname: string;
    ownerAvatar: string;
  };

  type LedgerTagDTO = API.BaseDTO & {
    name: string;
  };

  type LedgerTagQuery = API.PaginationQuery & {
    name?: string;
  };

  type LedgerMemberDTO = API.BaseDTO & {
    ledgerId: string;
    userId: string;
  };

  type LedgerMemberQuery = API.PaginationQuery & {
    ledgerId?: string;
  };

  type LedgerMemberVO = API.BaseDTO & {
    userId: string;
    userNickname: string;
    userAvatar: string;
  };
}
