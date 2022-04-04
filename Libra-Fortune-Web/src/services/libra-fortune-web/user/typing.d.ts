declare namespace API.User {
  type UserDTO = API.BaseDTO & {
    nickname: string;
    avatar: string;
    geminiId: string;
  };
  type UserLoginDTO = {
    userId: string;
    accessToken: {
      id: string;
      secret: string;
    };
  };
}
