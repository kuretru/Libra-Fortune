import type { RequestOptions } from '@@/plugin-request/request';
import type { RequestConfig } from '@umijs/max';
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
      const { code, message, data } =
        res as unknown as GalaxyWeb.ApiResponse<any>;
      if (code < 1000) {
        return;
      }
      const error: any = new Error(`[${code}]${message}`);
      error.name = 'BizError';
      error.info = { code, message, data };
      throw error;
    },
    // 错误接收及处理
    errorHandler: (error: any, opts: any) => {
      if (opts?.skipErrorHandler) throw error;
      // 我们的 errorThrower 抛出的错误。
      if (error.name === 'BizError') {
        message.error(error.message);
        // const errorInfo: ResponseStructure | undefined = error.info;
        // if (errorInfo) {
        //   const {errorMessage, errorCode} = errorInfo;
        //   switch (errorInfo.showType) {
        //     case ErrorShowType.SILENT:
        //       // do nothing
        //       break;
        //     case ErrorShowType.WARN_MESSAGE:
        //       message.warning(errorMessage);
        //       break;
        //     case ErrorShowType.ERROR_MESSAGE:
        //       message.error(errorMessage);
        //       break;
        //     case ErrorShowType.NOTIFICATION:
        //       notification.open({
        //         title: errorCode,
        //         description: errorMessage,
        //       });
        //       break;
        //     case ErrorShowType.REDIRECT:
        //       window.location.href = '/user/login';
        //       break;
        //     default:
        //       message.error(errorMessage);
        //   }
        // }
      } else if (error.response) {
        // Axios 的错误
        // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
        const { data } = error.response;
        if (data.code) {
          message.error(`[${data.code}]${data.message}`);
        } else {
          message.error(data);
        }
      } else if (typeof navigator !== 'undefined' && !navigator.onLine) {
        message.error('网络不可用，请检查网络连接后重试。');
      } else if (error.request) {
        message.error('None response! Please retry.');
      } else {
        message.error('Request error, please retry.');
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
  responseInterceptors: [],
};
