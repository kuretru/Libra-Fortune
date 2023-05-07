import BaseService from '@/services/galaxy-web/base-service';

class LedgerService extends BaseService<API.Ledger.LedgerDTO, API.Ledger.LedgerQuery> {
  constructor() {
    super('/ledgers');
  }
}

export default LedgerService;
