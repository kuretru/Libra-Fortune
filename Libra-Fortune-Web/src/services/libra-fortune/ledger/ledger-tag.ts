import BaseService from '@/services/galaxy-web/base-service';

class LedgerTagService extends BaseService<API.Ledger.LedgerTagDTO, API.Ledger.LedgerTagQuery> {
  constructor() {
    super('/ledgers/tags');
  }
}

export default LedgerTagService;
