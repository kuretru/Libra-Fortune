import BaseSequenceService from '@/services/galaxy-web/base-sequence-service';

class LedgerTagService extends BaseSequenceService<
  API.Ledger.LedgerTagDTO,
  API.Ledger.LedgerTagQuery
> {
  constructor() {
    super('/ledgers/tags');
  }
}

export default LedgerTagService;
