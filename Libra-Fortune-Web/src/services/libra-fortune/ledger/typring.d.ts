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

  type LedgerTagVO = API.BaseDTO & {
    name: string;
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
    nickname: string;
    avatar: string;
    paymentChannels?: API.User.PaymentChannelVO[];
    tags?: API.Ledger.LedgerTagVO[];
  };

  type LedgerCategoryDTO = API.BaseDTO & {
    ledgerId: string;
    name: string;
  };

  type LedgerCategoryQuery = API.PaginationQuery & {
    ledgerId?: string;
    name?: string;
  };

  type LedgerCategoryVO = API.BaseDTO & {
    name: string;
  };

  type LedgerEntryDTO = API.BaseDTO & {
    ledgerId: string;
    categoryId: string;
    date: string;
    name: string;
    total: number;
    currencyType: string;
    remark: string;
    details: LedgerEntryDetailDTO[];
  };

  type LedgerEntryQuery = API.PaginationQuery & {
    ledgerId: string;
    categoryId: string;
    date: string;
    name: string;
    total: number;
    currencyType: string;
  };

  type LedgerEntryVO = API.BaseDTO & {
    date: string;
    name: string;
    total: number;
    currencyType: string;
    remark: string;
    category: LedgerCategoryVO;
    details: LedgerEntryDetailVO[];
  };

  type LedgerEntryDetailDTO = API.BaseDTO & {
    entryId?: string;
    userId: string;
    paymentChannelId: string;
    fundedRatio: number;
    amount: number;
    tags?: API.LedgerEntryDetailTagDTO[];
  };

  type LedgerEntryDetailVO = API.BaseDTO & {
    fundedRatio: number;
    amount: number;
    user: API.User.UserVO;
    paymentChannel: API.User.PaymentChannelVO;
    tags?: API.Ledger.LedgerTagVO[];
  };

  type LedgerEntryDetailTagDTO = API.BaseDTO & {
    entryDetailId?: string;
    tagId: string;
  };
}
