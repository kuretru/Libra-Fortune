import { request } from 'umi';

export async function galaxyAuthorize(record: Galaxy.OAuth2.Client.OAuth2AuthorizeRequestDTO) {
  return request('/oauth2/galaxy/authorize', {
    method: 'POST',
    data: record,
  });
}
