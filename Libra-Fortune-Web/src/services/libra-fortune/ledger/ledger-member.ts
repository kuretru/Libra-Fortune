import BaseService from '@/services/galaxy-web/base-service';

class LedgerMemberService extends BaseService<
  API.Ledger.LedgerMemberDTO,
  API.Ledger.LedgerMemberQuery,
  API.Ledger.LedgerMemberVO
> {
  constructor(ledgerId: string) {
    super(`/ledgers/${ledgerId}/members`);
  }
}

export default LedgerMemberService;
