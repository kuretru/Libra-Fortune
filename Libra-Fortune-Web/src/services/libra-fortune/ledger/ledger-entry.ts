import BaseService from '@/services/galaxy-web/base-service';

class LedgerEntryService extends BaseService<
  API.Ledger.LedgerEntryDTO,
  API.Ledger.LedgerEntryQuery,
  API.Ledger.LedgerEntryVO
> {
  constructor(ledgerId: string) {
    super(`/ledgers/${ledgerId}/entries`);
  }
}

export default LedgerEntryService;
