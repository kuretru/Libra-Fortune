import type { RequestOptions } from '@@/plugin-request/request';
import type { RequestConfig } from '@umijs/max';
import { history } from '@umijs/max';
import { message } from 'antd';
import { ACCESS_TOKEN_STORAGE_KEY } from '@/services/cloud-sso';

// 错误处理方案： 错误类型
// enum ErrorShowType {
//   SILENT = 0,
//   WARN_MESSAGE = 1,
//   ERROR_MESSAGE = 2,
//   NOTIFICATION = 3,
//   REDIRECT = 9,
// }

// 与后端约定的响应数据格式
// interface ResponseStructure {
//   success: boolean;
//   data: unknown;
//   errorCode?: number;
//   errorMessage?: string;
//   showType?: ErrorShowType;
// }

const isApiResponse = (
  data: unknown,
): data is GalaxyWeb.ApiResponse<unknown> => {
  if (!data || typeof data !== 'object') {
    return false;
  }
  const response = data as Partial<GalaxyWeb.ApiResponse<unknown>>;
  return (
    typeof response.code === 'number' && typeof response.message === 'string'
  );
};

const buildBizError = (response: GalaxyWeb.ApiResponse<unknown>) => {
  const error: any = new Error(response.message);
  error.name = 'BizError';
  error.info = response;
  return error;
};

const parseApiResponse = (data: unknown) => {
  if (isApiResponse(data)) {
    return data;
  }
  if (typeof data !== 'string') {
    return undefined;
  }
  try {
    const parsed = JSON.parse(data);
    return isApiResponse(parsed) ? parsed : undefined;
  } catch {
    return undefined;
  }
};

const normalizeErrorResponse = (error: any) => {
  const infoResponse = parseApiResponse(error?.info);
  if (error?.name === 'BizError' && infoResponse) {
    return infoResponse;
  }
  for (const data of [error?.data, error?.response?.data]) {
    const response = parseApiResponse(data);
    if (response) {
      return response;
    }
  }
  return undefined;
};

const getErrorHandleType = (
  response: GalaxyWeb.ApiResponse<unknown>,
): GalaxyWeb.ErrorHandleType => {
  const data = response.data as GalaxyWeb.ApiError | undefined;
  return data?.handleType ?? 'error';
};

const redirectToLogin = () => {
  if (typeof window !== 'undefined') {
    window.localStorage.removeItem(ACCESS_TOKEN_STORAGE_KEY);
  }
  const { pathname, search, hash } = history.location;
  history.replace(
    `/user/login?redirect=${encodeURIComponent(pathname + search + hash)}`,
  );
};

const handleApiErrorResponse = (response: GalaxyWeb.ApiResponse<unknown>) => {
  const content = `[${response.code}]${response.message}`;
  switch (getErrorHandleType(response)) {
    case 'ignore':
      return;
    case 'info':
      message.info(content);
      return;
    case 'warn':
      message.warning(content);
      return;
    case 'login':
      redirectToLogin();
      return;
    default:
      message.error(content);
  }
};

const getHttpErrorMessage = (error: any) => {
  const status = error?.response?.status;
  return typeof status === 'number'
    ? `请求失败（HTTP ${status}）`
    : '请求失败，请稍后重试。';
};

/**
 * @name 错误处理
 * pro 自带的错误处理， 可以在这里做自己的改动
 * @doc https://umijs.org/docs/max/request#配置
 */
export const errorConfig: RequestConfig = {
  // 错误处理： umi@3 的错误处理方案。
  errorConfig: {
    // 错误抛出
    errorThrower: (res) => {
      // 处理200请求的业务异常
      const response = res as unknown as GalaxyWeb.ApiResponse<any>;
      if (!isApiResponse(response) || response.code < 1000) {
        return;
      }
      throw buildBizError(response);
    },
    // 错误接收及处理
    errorHandler: (error: any, opts: any) => {
      if (opts?.skipErrorHandler) throw error;
      const apiResponse = normalizeErrorResponse(error);
      if (apiResponse) {
        handleApiErrorResponse(apiResponse);
      } else if (error.response) {
        // Axios 的错误
        // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
        const fallbackMessage = getHttpErrorMessage(error);
        message.error(fallbackMessage);
      } else if (typeof navigator !== 'undefined' && !navigator.onLine) {
        const fallbackMessage = '网络不可用，请检查网络连接后重试。';
        message.error(fallbackMessage);
      } else if (error.request) {
        const fallbackMessage = '服务未响应，请稍后重试。';
        message.error(fallbackMessage);
      } else {
        const fallbackMessage = '请求失败，请稍后重试。';
        message.error(fallbackMessage);
      }
    },
  },

  // 请求拦截器
  requestInterceptors: [
    (config: RequestOptions) => {
      const token =
        typeof window !== 'undefined'
          ? window.localStorage.getItem(ACCESS_TOKEN_STORAGE_KEY)
          : null;
      const headers = {
        ...config.headers,
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      };
      const url = config?.url;
      return { ...config, url, headers };
    },
  ],

  // 响应拦截器
  responseInterceptors: [
    (response) => {
      const apiResponse = parseApiResponse(response.data);
      if (apiResponse && apiResponse.code >= 1000) {
        throw buildBizError(apiResponse);
      }
      return response;
    },
  ],
};
