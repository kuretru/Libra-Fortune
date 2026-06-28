import {request} from '@umijs/max';

const endpointPrefix = (ledgerId: number) => `/api/ledgers/${ledgerId}/entries`;

export async function get(ledgerId: number, id: number) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Ledger.LedgerEntryDTO>>(
    `${endpointPrefix(ledgerId)}/${id}`, {
      method: 'GET',
    },
  );
}

export async function list(
  ledgerId: number,
  page: GalaxyWeb.PaginationQuery & LibraFortune.Ledger.LedgerEntryQuery,
) {
  const {tagIdIn, ...params} = page;
  const tagIdQuery = tagIdIn
    ?.map((tagId) => `tagIdIn=${encodeURIComponent(tagId)}`)
    .join('&');

  return request<
    GalaxyWeb.ApiResponse<
      GalaxyWeb.PaginationResponse<LibraFortune.Ledger.LedgerEntryDTO>
    >
  >(`${endpointPrefix(ledgerId)}${tagIdQuery ? `?${tagIdQuery}` : ''}`, {
    method: 'GET',
    params,
  });
}

export async function create(
  ledgerId: number,
  record: LibraFortune.Ledger.LedgerEntryDTO,
) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Ledger.LedgerEntryDTO>>(
    endpointPrefix(ledgerId), {
      method: 'POST',
      data: record,
    },
  );
}

export async function update(
  ledgerId: number,
  record: LibraFortune.Ledger.LedgerEntryDTO,
) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Ledger.LedgerEntryDTO>>(
    `${endpointPrefix(ledgerId)}/${record.id}`, {
      method: 'PUT',
      data: record,
    },
  );
}

export async function remove(ledgerId: number, id: number) {
  return request<GalaxyWeb.ApiResponse<string>>(
    `${endpointPrefix(ledgerId)}/${id}`, {
      method: 'DELETE',
    },
  );
}
