import BaseService from '../base-service';

class LedgerCategoryService extends BaseService<
  API.Ledger.LedgerCategoryDTO,
  API.Ledger.LedgerCategoryQuery
> {
  constructor(ledgerId: string) {
    super(`/ledgers/${ledgerId}/categories`);
  }
}

export default LedgerCategoryService;
