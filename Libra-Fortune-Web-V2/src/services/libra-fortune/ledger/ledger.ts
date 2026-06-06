import {request} from '@umijs/max';

const endpointPrefix = '/api/ledgers';

export async function get(id: number) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Ledger.LedgerDTO>>(
    `${endpointPrefix}/${id}`, {
      method: 'GET',
    },
  );
}

export async function list(
  page: GalaxyWeb.PaginationQuery & LibraFortune.Ledger.LedgerQuery,
) {
  return request<
    GalaxyWeb.ApiResponse<
      GalaxyWeb.PaginationResponse<LibraFortune.Ledger.LedgerDTO>
    >
  >(`${endpointPrefix}`, {
    method: 'GET',
    params: {
      ...page,
    },
  });
}

export async function create(record: LibraFortune.Ledger.LedgerDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Ledger.LedgerDTO>>(
    `${endpointPrefix}`, {
      method: 'POST',
      data: record,
    },
  );
}

export async function update(record: LibraFortune.Ledger.LedgerDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Ledger.LedgerDTO>>(
    `${endpointPrefix}/${record.id}`, {
      method: 'PUT',
      data: record,
    },
  );
}

export async function remove(id: number) {
  return request<GalaxyWeb.ApiResponse<string>>(`${endpointPrefix}/${id}`, {
    method: 'DELETE',
  });
}
