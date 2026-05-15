import { jwtDecode } from 'jwt-decode';

export async function currentUser(): Promise<CloudSSO.CurrentUser> {
  const accessToken = localStorage.getItem('accessToken');

  if (!accessToken) {
    throw new Error('没有JWT-Token');
  }

  const payload = jwtDecode<CloudSSO.JwtPayload>(accessToken);

  if (Date.now() >= payload.exp * 1000) {
    throw new Error('JWT-Token已过期');
  }

  return {
    username: payload.sub,
    nickname: payload.name,
    email: payload.email,
  };
}
