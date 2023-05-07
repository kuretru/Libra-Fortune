import Footer from '@/components/Footer';
import { Question } from '@/components/RightContent';
import { AvatarDropdown, AvatarName } from '@/components/RightContent/AvatarDropdown';
import { LinkOutlined } from '@ant-design/icons';
import { ProSettings, SettingDrawer } from '@ant-design/pro-components';
import type { RequestConfig, RunTimeLayoutConfig } from '@umijs/max';
import { history, Link } from '@umijs/max';
import { AvatarProps, message } from 'antd';
import React from 'react';
import defaultSettings from '../config/defaultSettings';
import { requestConfig } from './utils/request-config';
import { appendSearchParams } from './utils/request-utils';
import { getUserInfo } from './utils/user-utils';

const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/users/login';
const callbackPath = '/users/login/callback';
const nonLoginPaths = [loginPath, callbackPath];

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 */
export async function getInitialState(): Promise<{
  settings?: Partial<ProSettings>;
  currentUser?: Galaxy.OAuth2.System.UserDTO;
  loading?: boolean;
  fetchUserInfo?: () => Promise<Galaxy.OAuth2.System.UserDTO | undefined>;
}> {
  // 如果不是登录页面，执行
  const { location } = history;
  if (!nonLoginPaths.includes(location.pathname)) {
    const currentUser = await getUserInfo();
    return {
      settings: defaultSettings as Partial<ProSettings>,
      currentUser: currentUser,
      fetchUserInfo: getUserInfo,
    };
  }
  return {
    settings: defaultSettings as Partial<ProSettings>,
    fetchUserInfo: getUserInfo,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    actionsRender: () => [<Question key="doc" />],
    avatarProps: {
      src: initialState?.currentUser?.avatar,
      title: <AvatarName />,
      render: (_: AvatarProps, avatarChildren: React.ReactNode) => {
        return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
      },
    },
    waterMarkProps: {
      content: initialState?.currentUser?.nickname,
    },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history;
      // 如果没有登录，重定向到 login
      if (!initialState?.currentUser && !nonLoginPaths.includes(location.pathname)) {
        message.error('没有用户信息，请登录');
        history.push({
          pathname: loginPath,
          search: appendSearchParams({ redirect: location.pathname }),
        });
      }
    },
    layoutBgImgList: [
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/D2LWSqNny4sAAAAAAAAAAAAAFl94AQBr',
        left: 85,
        bottom: 100,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/C2TWRpJpiC0AAAAAAAAAAAAAFl94AQBr',
        bottom: -68,
        right: -45,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/F6vSTbj8KpYAAAAAAAAAAAAAFl94AQBr',
        bottom: 0,
        left: 0,
        width: '331px',
      },
    ],
    links: isDev
      ? [
          <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
            <LinkOutlined />
            <span>OpenAPI 文档</span>
          </Link>,
        ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          <SettingDrawer
            disableUrlParams
            enableDarkTheme
            settings={initialState?.settings}
            onSettingChange={(settings) => {
              setInitialState((preInitialState) => ({
                ...preInitialState,
                settings,
              }));
            }}
          />
        </>
      );
    },
    ...initialState?.settings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request: RequestConfig = {
  ...requestConfig,
};
