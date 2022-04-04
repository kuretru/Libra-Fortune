import { request } from 'umi';

async function get(id: string): Promise<API.ApiResponse<API.User.UserDTO>> {
  return request<API.ApiResponse<API.User.UserDTO>>(`/api/users/${id}`, {
    method: 'get',
  });
}

export { get };
