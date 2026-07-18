import {request} from '@umijs/max';

const endpointPrefix = '/api/dashboards';

export async function ledger(query: LibraFortune.Ledger.DashboardQuery) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Ledger.DashboardLedgerBO[]>>(
    `${endpointPrefix}/ledgers`,
    {
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
