import BaseService from '@/services/galaxy-web/base-service';

class LedgerTagService extends BaseService<API.Ledger.LedgerTagDTO, API.Ledger.LedgerTagQuery> {
  constructor() {
    super('/ledgers/tags');
  }

  async list(
    params?: API.Ledger.LedgerTagQuery,
  ): Promise<API.ApiResponse<API.Ledger.LedgerTagDTO[]>> {
    params!.userId = localStorage.getItem('userId')!;
    return super.list(params);
  }

  async listByPage(
    params: API.Ledger.LedgerTagQuery & API.PaginationQuery,
  ): Promise<API.ProTableData<API.Ledger.LedgerTagDTO>> {
    params!.userId = localStorage.getItem('userId')!;
    return super.listByPage(params);
  }
}

export default LedgerTagService;
