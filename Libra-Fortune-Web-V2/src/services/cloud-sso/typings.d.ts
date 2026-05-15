declare namespace CloudSSO {
  type JwtPayload = {
    iss: string;
    sub: string;
    aud: string;
    exp: number;
    iat: number;
    auth_time: number;
    acr: string;
    jti: string;
    email: string;
    email_verified: boolean;
    name: string;
    given_name: string;
    preferred_username: string;
    nickname: string;
    groups: string[];
  }
  type CurrentUser = {
    username: string;
    nickname: string;
    email: string;
  }
}
