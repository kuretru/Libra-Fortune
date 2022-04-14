import BaseService from '../base-service';

class LedgerUserService extends BaseService<API.Ledger.LedgerUserDTO, API.Ledger.LedgerUserQuery> {
  constructor(ledgerId: string) {
    super(`/ledgers/${ledgerId}/users`);
  }
}

export default LedgerUserService;
