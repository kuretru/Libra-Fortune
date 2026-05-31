import {request} from '@umijs/max';

const endpointPrefix = '/api/metadata/categories';

export async function enums() {
  return request<GalaxyWeb.ApiResponse<GalaxyWeb.EnumDTO[]>>(
    `${endpointPrefix}/enums`, {
      method: 'GET',
    },
  );
}

export async function get(id: number) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Metadata.CategoryDTO>>(
    `${endpointPrefix}/${id}`, {
      method: 'GET',
    },
  );
}

export async function list(page: GalaxyWeb.PaginationQuery) {
  return request<
    GalaxyWeb.ApiResponse<
      GalaxyWeb.PaginationResponse<LibraFortune.Metadata.CategoryDTO>
    >
  >(`${endpointPrefix}`, {
    method: 'GET',
    params: {
      ...page,
    },
  });
}

export async function create(record: LibraFortune.Metadata.CategoryDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Metadata.CategoryDTO>>(
    `${endpointPrefix}`, {
      method: 'POST',
      data: record,
    },
  );
}

export async function update(record: LibraFortune.Metadata.CategoryDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Metadata.CategoryDTO>>(
    `${endpointPrefix}/${record.id}`, {
      method: 'PUT',
      data: record,
    },
  );
}

export async function remove(id: number) {
  return request<GalaxyWeb.ApiResponse<string>>(
    `${endpointPrefix}/${id}`, {
      method: 'DELETE',
    });
}
