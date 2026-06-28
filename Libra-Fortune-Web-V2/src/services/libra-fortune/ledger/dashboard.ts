import {request} from '@umijs/max';

const endpointPrefix = '/api/dashboards';

export async function sum(query: LibraFortune.Ledger.DashboardLedgerQuery) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Ledger.DashboardLedgerBO[]>>(
    `${endpointPrefix}/ledgers/sum`, {
      method: 'POST',
      data: query,
    },
  );
}

export async function latestAccountBalances() {
  return request<
    GalaxyWeb.ApiResponse<LibraFortune.Ledger.DashboardAccountBalanceBO>
  >(
    `${endpointPrefix}/account-balances/latest`,
    {
      method: 'GET',
    },
  );
}
