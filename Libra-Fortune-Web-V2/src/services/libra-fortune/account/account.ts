import {request} from '@umijs/max';

const endpointPrefix = '/api/accounts';

export async function get(id: number) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Account.AccountDTO>>(
    `${endpointPrefix}/${id}`, {
      method: 'GET',
    },
  );
}

export async function list(
  page: GalaxyWeb.PaginationQuery & LibraFortune.Account.AccountQuery,
) {
  return request<
    GalaxyWeb.ApiResponse<
      GalaxyWeb.PaginationResponse<LibraFortune.Account.AccountDTO>
    >
  >(`${endpointPrefix}`, {
    method: 'GET',
    params: {
      ...page,
    },
  });
}

export async function create(record: LibraFortune.Account.AccountDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Account.AccountDTO>>(
    `${endpointPrefix}`, {
      method: 'POST',
      data: record,
    },
  );
}

export async function update(record: LibraFortune.Account.AccountDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Account.AccountDTO>>(
    `${endpointPrefix}/${record.id}`, {
      method: 'PUT',
      data: record,
    },
  );
}

export async function remove(id: number) {
  return request<GalaxyWeb.ApiResponse<string>>(
    `${endpointPrefix}/${id}`, {
      method: 'DELETE',
    });
}

export async function reorder(idList: number[]) {
  return request<GalaxyWeb.ApiResponse<string>>(`${endpointPrefix}/reorder`, {
    method: 'PUT',
    data: idList,
  });
}
