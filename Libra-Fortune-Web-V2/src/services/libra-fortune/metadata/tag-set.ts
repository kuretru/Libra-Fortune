import {request} from '@umijs/max';

const endpointPrefix = '/api/metadata/tag_sets';

export async function get(id: number) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Metadata.TagSetDTO>>(
    `${endpointPrefix}/${id}`, {
      method: 'GET',
    },
  );
}

export async function list(page: GalaxyWeb.PaginationQuery) {
  return request<
    GalaxyWeb.ApiResponse<
      GalaxyWeb.PaginationResponse<LibraFortune.Metadata.TagSetDTO>
    >
  >(`${endpointPrefix}`, {
    method: 'GET',
    params: {
      ...page,
    },
  });
}

export async function create(record: LibraFortune.Metadata.TagSetDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Metadata.TagSetDTO>>(
    `${endpointPrefix}`, {
      method: 'POST',
      data: record,
    },
  );
}

export async function update(record: LibraFortune.Metadata.TagSetDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Metadata.TagSetDTO>>(
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

export async function createItem(
  setId: number,
  record: LibraFortune.Metadata.TagSetItemDTO,
) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Metadata.TagSetItemDTO>>(
    `${endpointPrefix}/${setId}/items`, {
      method: 'POST',
      data: record,
    },
  );
}

export async function updateItem(
  setId: number,
  record: LibraFortune.Metadata.TagSetItemDTO,
) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Metadata.TagSetItemDTO>>(
    `${endpointPrefix}/${setId}/items/${record.id}`, {
      method: 'PUT',
      data: {
        ...record,
        setId,
      },
    },
  );
}

export async function removeItem(setId: number, id: number) {
  return request<GalaxyWeb.ApiResponse<string>>(
    `${endpointPrefix}/${setId}/items/${id}`, {
      method: 'DELETE',
    },
  );
}
