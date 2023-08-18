import { request } from '@umijs/max';

export async function galaxyAuthorize(
  record: Galaxy.OAuth2.Client.OAuth2AuthorizeRequestDTO,
): Promise<API.ApiResponse<string>> {
  return request('/api/oauth2/galaxy/authorize', {
    method: 'POST',
    data: record,
  });
}

export async function galaxyCallback(
  record: Galaxy.OAuth2.Client.OAuth2AuthorizeResponseDTO,
): Promise<API.ApiResponse<Galaxy.OAuth2.System.UserLoginDTO>> {
  return request('/api/oauth2/galaxy/callback', {
    method: 'POST',
    data: record,
  });
}
