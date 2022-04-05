import type { RequestConfig, RunTimeLayoutConfig } from 'umi';
import { ErrorShowType, history } from 'umi';
import type { RequestOptionsInit } from 'umi-request';
import { PageLoading, SettingDrawer } from '@ant-design/pro-layout';
import type { Settings as LayoutSettings } from '@ant-design/pro-layout';
import RightContent from '@/components/RightContent';
import Footer from '@/components/Footer';
import { LinkOutlined } from '@ant-design/icons';
import defaultSettings from '../config/defaultSettings';
import { get as getUser } from '@/services/libra-fortune-web/user';

const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/users/login';
const callbackPath = '/users/login/callback';

/** 获取用户信息比较慢的时候会展示一个 loading */
export const initialStateConfig = {
  loading: <PageLoading />,
};

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUser;
  loading?: boolean;
  fetchUserInfo?: () => Promise<API.User.UserDTO | undefined>;
}> {
  const fetchUserInfo = async () => {
    try {
      const userid = localStorage.getItem('userId');
      if (!userid) return undefined;
      const msg = await getUser(userid);
      return msg.data;
    } catch (error) {
      history.push({
        pathname: loginPath,
        query: {
          redirect: history.location.pathname,
          ...history.location.query,
        },
      });
    }
    return undefined;
  };
  // 如果是登录页面，不执行
  if (history.location.pathname !== loginPath && history.location.pathname !== callbackPath) {
    const currentUser = await fetchUserInfo();
    return {
      settings: defaultSettings,
      currentUser,
      fetchUserInfo,
    };
  }
  return {
    settings: defaultSettings,
    fetchUserInfo,
  };
}

// Request的AccessToken拦截器
const accessTokenInterceptor = (url: string, options: RequestOptionsInit) => {
  const id = localStorage.getItem('accessTokenId');
  if (!id) {
    return {
      url: `${url}`,
      options: options,
    };
  }

  const authHeader = {
    'Access-Token-ID': id,
    'Access-Token': localStorage.getItem('accessToken'),
  };
  return {
    url: `${url}`,
    options: { ...options, interceptors: true, headers: authHeader },
  };
};

// 全局Request配置
export const request: RequestConfig = {
  errorConfig: {
    adaptor: (resData: any) => {
      if (resData.code && resData.code >= 10000) {
        return {
          ...resData,
          success: false,
          errorCode: String(resData.code),
          errorMessage: `${resData.message}: ${resData.data}`,
          showType: ErrorShowType.ERROR_MESSAGE,
        };
      }
      return {
        ...resData,
        success: true,
      };
    },
  },
  requestInterceptors: [accessTokenInterceptor],
};

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    rightContentRender: () => <RightContent />,
    disableContentMargin: false,
    waterMarkProps: {
      content: initialState?.currentUser?.name,
    },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history;
      // 如果没有登录，重定向到 login
      if (
        !initialState?.currentUser &&
        location.pathname !== loginPath &&
        location.pathname !== callbackPath
      ) {
        history.push({
          pathname: loginPath,
          query: {
            redirect: history.location.pathname,
            ...history.location.query,
          },
        });
      }
    },
    links: isDev
      ? [
          <a
            key="github"
            target="_blank"
            href="https://github.com/kuretru/Libra-Fortune/"
            rel="noreferrer"
          >
            <LinkOutlined />
            <span>GitHub</span>
          </a>,
        ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children, props) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          {!props.location?.pathname?.includes('/login') && (
            <SettingDrawer
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState: any) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            />
          )}
        </>
      );
    },
    ...initialState?.settings,
  };
};
