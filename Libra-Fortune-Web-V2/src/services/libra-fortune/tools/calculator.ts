import {request} from '@umijs/max';

const endpointPrefix = '/api/tools/calculator';

export async function add(record: LibraFortune.Tools.CalculatorDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Tools.CalculatorResult>>(
    `${endpointPrefix}/add`, {
      method: 'POST',
      data: record,
    },
  );
}

export async function subtract(record: LibraFortune.Tools.CalculatorDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Tools.CalculatorResult>>(
    `${endpointPrefix}/subtract`, {
      method: 'POST',
      data: record,
    },
  );
}

export async function multiply(record: LibraFortune.Tools.CalculatorDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Tools.CalculatorResult>>(
    `${endpointPrefix}/multiply`, {
      method: 'POST',
      data: record,
    },
  );
}

export async function divide(record: LibraFortune.Tools.CalculatorDTO) {
  return request<GalaxyWeb.ApiResponse<LibraFortune.Tools.CalculatorResult>>(
    `${endpointPrefix}/divide`, {
      method: 'POST',
      data: record,
    },
  );
}
