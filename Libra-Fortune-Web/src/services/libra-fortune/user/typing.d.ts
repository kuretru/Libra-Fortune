// @ts-ignore
/* eslint-disable */

declare namespace API.User {
  type UserVO = API.BaseDTO & {
    nickname: string;
    avatar: string;
  };

  type PaymentChannelDTO = API.BaseDTO & {
    userId: string;
    name: string;
  };

  type PaymentChannelQuery = API.PaginationQuery & {
    userId: string;
    name?: string;
  };

  type PaymentChannelVO = API.BaseDTO & {
    name: string;
  };
}
