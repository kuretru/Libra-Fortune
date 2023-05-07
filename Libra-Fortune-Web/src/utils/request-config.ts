import { type RequestOptions } from '@@/plugin-request/request';
import type { RequestConfig } from '@umijs/max';
import { message } from 'antd';

/**
 * 用于注入AccessToken请求拦截器
 * @param options request的原始配置选项
 * @returns 添加过AccessToken的配置选项
 */
const accessTokenRequestInterceptor = (options: RequestOptions) => {
  const id = localStorage.getItem('accessTokenId');
  if (id) {
    options.headers!['Access-Token-ID'] = id;
    options.headers!['Access-Token'] = localStorage.getItem('accessToken') ?? '';
  }
  return options;
};

/**
 * 全局Request配置
 * @doc https://umijs.org/docs/max/request#运行时配置
 */
export const requestConfig: RequestConfig = {
  timeout: 1000,
  // 错误处理： umi@3 的错误处理方案。
  errorConfig: {
    // 错误抛出
    errorThrower: (res: any) => {
      const response: API.ApiResponse<any> = res.data;
      if (!response.code) {
        // 响应格式错误
        const error = new Error('未知的响应数据格式');
        error.name = 'UnknownResponseError';
        throw error;
      }
      if (response.code >= 10000) {
        // 非正常请求抛出异常，然后由errorHandler统一处理
        const error: any = new Error('请求成功，但产生业务异常');
        error.name = 'BusinessError';
        // 复用Axios错误的处理逻辑
        error.response = {
          data: response,
        };
        throw error;
      }
    },
    // 错误接收及处理
    errorHandler: (error: any, opts: RequestOptions) => {
      if (opts?.skipErrorHandler) {
        throw error;
      }

      if (error.name === 'UnknownResponseError') {
        message.error(error.message);
      } else if (error.response) {
        // Axios的错误
        if (error.response.status === 404) {
          // 请求到了服务器，但是路径错误
          message.error('404 - 请求路径错误');
        } else if (error.response.data) {
          // 请求成功发出且服务器也做出了响应，但状态代码超出了 2xx 的范围 -> 业务异常
          const response: API.ApiResponse<string> = error.response.data;
          message.error(`[${response.code}]${response.message} - ${response.data}`);
        } else {
          // 请求未到服务器，或产生未被服务端全局捕获的其他异常
          message.error(`请求失败：${error.response.status} - ${error.response.statusText}`);
        }
      } else if (error.request) {
        // 请求已经成功发起，但没有收到响应
        // error.request 在浏览器中是 XMLHttpRequest 的实例，
        // 而在node.js中是 http.ClientRequest 的实例
        message.error('请求超时，请检查本地网络连接，或等待服务端恢复后重试');
      } else {
        // 发送请求时出了点问题
        message.error('发送请求失败，请检查代码，或向管理/开发人员反馈错误');
      }
    },
  },

  // 请求拦截器
  requestInterceptors: [accessTokenRequestInterceptor],

  // 响应拦截器
  responseInterceptors: [],
};
