import { request } from '@umijs/max';

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
