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
  type AccessTokenResult = {
    access_token: string;
    token_type: string;
    scope: string;
    expires_in: number;
    id_token: string;
  }
  type ErrorResponse = {
    error: string;
    error_description: string;
    request_id?: string;
  }
}
