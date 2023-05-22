import { request } from '@umijs/max';
import BaseService from './base-service';

abstract class BaseSequenceService<
  T extends API.BaseDTO,
  Q extends API.PaginationQuery,
  V extends API.BaseDTO = T,
> extends BaseService<T, Q, V> {
  async reorder(idList: string[]): Promise<API.ApiResponse<string>> {
    return request<API.ApiResponse<string>>(`/api${this.url}/reorder`, {
      method: 'put',
      data: idList,
    });
  }
}

export default BaseSequenceService;
