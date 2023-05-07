import { request } from '@umijs/max';

export async function getLedgerType(): Promise<API.ApiResponse<API.EnumDTO[]>> {
  return request('/api/ledgers/types', {
    method: 'GET',
  });
}
