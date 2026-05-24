import {jwtDecode} from 'jwt-decode';

const OAUTH_STATE_STORAGE_KEY = 'cloudSso.state';
const OAUTH_REDIRECT_STORAGE_KEY = 'cloudSso.redirect';
const ACCESS_TOKEN_STORAGE_KEY = 'accessToken';
const SSO_CLIENT_ID = 'U9XLAUhQIiyfwscUfdQwRmnBJvfTIbcAxXuMZJYa';
const SSO_AUTHORIZE_URL = 'https://sso.i5zhen.com/application/o/authorize/';
const SSO_TOKEN_URL = 'https://sso.i5zhen.com/application/o/token/';

const getRedirectUri = () => `${window.location.origin}/user/login`;

export async function currentUser(): Promise<API.CurrentUser> {
  const token = localStorage.getItem(ACCESS_TOKEN_STORAGE_KEY);

  if (!token) {
    throw new Error('没有JWT-Token');
  }

  const payload = jwtDecode<CloudSSO.JwtPayload>(token);

  if (Date.now() >= payload.exp * 1000) {
    throw new Error('JWT-Token已过期');
  }

  return {
    userid: payload.sub,
    name: payload.name || payload.nickname || payload.preferred_username,
    email: payload.email,
    access: payload.groups?.includes('admin') ? 'admin' : 'user',
  };
}

export function authorization(redirect: string) {
  const state = crypto.randomUUID();
  sessionStorage.setItem(OAUTH_STATE_STORAGE_KEY, state);
  sessionStorage.setItem(OAUTH_REDIRECT_STORAGE_KEY, redirect);

  const params = new URLSearchParams({
    response_type: 'code',
    client_id: SSO_CLIENT_ID,
    redirect_uri: getRedirectUri(),
    scope: 'openid profile email',
    state,
  });

  window.location.assign(`${SSO_AUTHORIZE_URL}?${params.toString()}`);
}

export async function accessToken(code: string, state: string): Promise<string> {
  const currentState = sessionStorage.getItem(OAUTH_STATE_STORAGE_KEY);
  if (!currentState) {
    throw new Error('state不存在，请重新登录');
  } else if (currentState !== state) {
    throw new Error('state不匹配，请重新登录');
  }

  const body = new URLSearchParams({
    grant_type: 'authorization_code',
    code,
    redirect_uri: getRedirectUri(),
    client_id: SSO_CLIENT_ID,
  });

  const response = await fetch(SSO_TOKEN_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body,
  });
  const result = (await response.json().catch(() => ({}))) as
    | CloudSSO.AccessTokenResult
    | CloudSSO.ErrorResponse;

  if (!response.ok || 'error' in result) {
    const error = result as CloudSSO.ErrorResponse;
    throw new Error(
      error.error_description || error.error || `SSO token 请求失败: ${response.status}`,
    );
  }

  localStorage.setItem(ACCESS_TOKEN_STORAGE_KEY, result.access_token);
  sessionStorage.removeItem(OAUTH_STATE_STORAGE_KEY);

  const redirect = sessionStorage.getItem(OAUTH_REDIRECT_STORAGE_KEY);
  sessionStorage.removeItem(OAUTH_REDIRECT_STORAGE_KEY);
  return redirect || '/welcome';
}
