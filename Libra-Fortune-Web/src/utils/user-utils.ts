import { get as getUser } from '@/services/galaxy-oauth2-client/user';
import { history } from '@umijs/max';
import { appendSearchParams } from './request-utils';

const loginPath = '/users/login';

/**
 * 请求用户信息
 * @returns 用户信息
 */
export async function getUserInfo(): Promise<Galaxy.OAuth2.System.UserDTO | undefined> {
  try {
    const userId = localStorage.getItem('userId');
    if (!userId) return undefined;
    const response = await getUser(userId);
    return response.data;
  } catch (error) {
    console.log('login failed');
    console.log(error);
    history.push({
      pathname: loginPath,
      search: appendSearchParams({ redirect: location.pathname }),
    });
  }
  return undefined;
}

export function clearUserLoginState() {
  localStorage.removeItem('userId');
  localStorage.removeItem('accessTokenId');
  localStorage.removeItem('accessToken');
}
