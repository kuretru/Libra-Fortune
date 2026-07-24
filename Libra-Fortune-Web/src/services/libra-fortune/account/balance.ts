import { request } from '@umijs/max';

export async function list(query: LibraFortune.Account.AccountBalanceQuery) {
  return request<
    GalaxyWeb.ApiResponse<LibraFortune.Account.AccountBalanceResultDTO>
  >('/api/accounts/balances', {
    method: 'GET',
    params: query,
  });
}

export async function save(record: LibraFortune.Account.AccountBalanceRequest) {
  return request<GalaxyWeb.ApiResponse<null>>('/api/accounts/balances', {
    method: 'PUT',
    data: record,
  });
}
