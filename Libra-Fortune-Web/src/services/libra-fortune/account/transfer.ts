import { request } from '@umijs/max';

const endpointPrefix = '/api/accounts/transfers';

export async function get(id: number) {
  return request<
    GalaxyWeb.ApiResponse<LibraFortune.Account.AccountTransferDTO>
  >(`${endpointPrefix}/${id}`, {
    method: 'GET',
  });
}

export async function list(
  page: GalaxyWeb.PaginationQuery & LibraFortune.Account.AccountTransferQuery,
) {
  return request<
    GalaxyWeb.ApiResponse<
      GalaxyWeb.PaginationResponse<LibraFortune.Account.AccountTransferDTO>
    >
  >(endpointPrefix, {
    method: 'GET',
    params: page,
  });
}

export async function create(
  record: LibraFortune.Account.AccountTransferDTO,
) {
  return request<
    GalaxyWeb.ApiResponse<LibraFortune.Account.AccountTransferDTO>
  >(endpointPrefix, {
    method: 'POST',
    data: record,
  });
}

export async function update(
  record: LibraFortune.Account.AccountTransferDTO,
) {
  return request<
    GalaxyWeb.ApiResponse<LibraFortune.Account.AccountTransferDTO>
  >(`${endpointPrefix}/${record.id}`, {
    method: 'PUT',
    data: record,
  });
}

export async function remove(id: number) {
  return request<GalaxyWeb.ApiResponse<string>>(`${endpointPrefix}/${id}`, {
    method: 'DELETE',
  });
}
