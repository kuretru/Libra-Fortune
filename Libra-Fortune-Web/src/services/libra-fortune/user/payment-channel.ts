import BaseService from '@/services/galaxy-web/base-service';

class PaymentChannelService extends BaseService<
  API.User.PaymentChannelDTO,
  API.User.PaymentChannelQuery
> {
  constructor() {
    super(`/payments/channels`);
  }

  async list(
    params?: API.User.PaymentChannelQuery,
  ): Promise<API.ApiResponse<API.User.PaymentChannelDTO[]>> {
    params!.userId = localStorage.getItem('userId')!;
    return super.list(params);
  }

  async listByPage(
    params: API.User.PaymentChannelQuery & API.PaginationQuery,
  ): Promise<API.ProTableData<API.User.PaymentChannelDTO>> {
    params!.userId = localStorage.getItem('userId')!;
    return super.listByPage(params);
  }
}

export default PaymentChannelService;
