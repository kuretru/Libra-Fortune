import BaseService from '@/services/galaxy-web/base-service';

class LedgerMemberService extends BaseService<
  API.Ledger.LedgerMemberDTO,
  API.Ledger.LedgerMemberQuery,
  API.Ledger.LedgerMemberVO
> {
  constructor(ledgerId: string) {
    super(`/ledgers/${ledgerId}/members`);
  }

  fromServer(records: API.Ledger.LedgerMemberVO[]) {
    records.forEach((record) => {
      record.defaultFundedRatio /= 100;
    });
    return records;
  }

  toServer(record: API.Ledger.LedgerMemberDTO) {
    record.defaultFundedRatio = Number.parseInt((record.defaultFundedRatio * 100).toFixed(0));
    return record;
  }

  async list(
    params?: API.Ledger.LedgerMemberQuery,
  ): Promise<API.ApiResponse<API.Ledger.LedgerMemberVO[]>> {
    return super.list(params).then((response) => {
      response.data = this.fromServer(response.data);
      return response;
    });
  }

  async listByPage(
    params: API.Ledger.LedgerMemberQuery & API.PaginationQuery,
  ): Promise<API.ProTableData<API.Ledger.LedgerMemberVO>> {
    return super.listByPage(params).then((response) => {
      response.data = this.fromServer(response.data);
      return response;
    });
  }

  async create(
    record: API.Ledger.LedgerMemberDTO,
  ): Promise<API.ApiResponse<API.Ledger.LedgerMemberDTO>> {
    return super.create(this.toServer(record));
  }

  async update(
    record: API.Ledger.LedgerMemberDTO,
  ): Promise<API.ApiResponse<API.Ledger.LedgerMemberDTO>> {
    return super.update(this.toServer(record));
  }

}

export default LedgerMemberService;
