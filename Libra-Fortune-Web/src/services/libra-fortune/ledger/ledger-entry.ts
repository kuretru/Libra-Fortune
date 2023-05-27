import BaseService from '@/services/galaxy-web/base-service';

class LedgerEntryService extends BaseService<
  API.Ledger.LedgerEntryDTO,
  API.Ledger.LedgerEntryQuery,
  API.Ledger.LedgerEntryVO
> {
  constructor(ledgerId: string) {
    super(`/ledgers/${ledgerId}/entries`);
  }

  fromServer(records: API.Ledger.LedgerEntryVO[]) {
    records.forEach((record) => {
      record.total /= 100;
      record.details.forEach((detail) => {
        detail.fundedRatio /= 100;
        detail.amount /= 100;
      });
    });
    return records;
  }

  toServer(record: API.Ledger.LedgerEntryDTO) {
    record.total = Number.parseInt((record.total * 100).toFixed(0));
    record.details.forEach((detail) => {
      detail.fundedRatio = Number.parseInt((detail.fundedRatio * 100).toFixed(0));
      detail.amount = Number.parseInt((detail.amount * 100).toFixed(0));
    });
    return record;
  }

  async list(
    params?: API.Ledger.LedgerEntryQuery,
  ): Promise<API.ApiResponse<API.Ledger.LedgerEntryVO[]>> {
    return super.list(params).then((response) => {
      response.data = this.fromServer(response.data);
      return response;
    });
  }

  async listByPage(
    params: API.Ledger.LedgerEntryQuery & API.PaginationQuery,
  ): Promise<API.ProTableData<API.Ledger.LedgerEntryVO>> {
    return super.listByPage(params).then((response) => {
      response.data = this.fromServer(response.data);
      return response;
    });
  }

  async create(
    record: API.Ledger.LedgerEntryDTO,
  ): Promise<API.ApiResponse<API.Ledger.LedgerEntryDTO>> {
    return super.create(this.toServer(record));
  }

  async update(
    record: API.Ledger.LedgerEntryDTO,
  ): Promise<API.ApiResponse<API.Ledger.LedgerEntryDTO>> {
    return super.update(this.toServer(record));
  }
}

export default LedgerEntryService;
