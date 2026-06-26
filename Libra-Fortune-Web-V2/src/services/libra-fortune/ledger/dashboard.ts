import {request} from '@umijs/max';

const endpointPrefix = '/api/dashboards';

export async function sum(query: LibraFortune.Ledger.DashboardLedgerQuery) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Ledger.DashboardLedgerBO[]>>(
    `${endpointPrefix}/sum`, {
      method: 'POST',
      data: query,
    },
  );
}
