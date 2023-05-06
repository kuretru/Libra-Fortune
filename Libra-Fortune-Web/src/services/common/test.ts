import { request } from '@@/plugin-request';

export async function ping() {
  return request('/api/ping', {
    method: 'GET',
  });
}

export async function exception() {
  return request('/api/exception', {
    method: 'GET',
  });
}
