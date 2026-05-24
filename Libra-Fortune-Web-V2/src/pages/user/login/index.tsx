import { LoginForm, ProFormCheckbox } from '@ant-design/pro-components';
import { Helmet, useModel } from '@umijs/max';
import { App, Flex, Skeleton, Tabs } from 'antd';
import { createStyles } from 'antd-style';
import React, { startTransition, useEffect } from 'react';
import { Footer } from '@/components';
import { accessToken, authorization } from '@/services/cloud-sso';
import Settings from '../../../../config/defaultSettings';

const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});

const Login: React.FC = () => {
  const { initialState, setInitialState } = useModel('@@initialState');
  const { styles } = useStyles();
  const { message } = App.useApp();

  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();
    if (userInfo) {
      startTransition(() => {
        setInitialState((s) => ({
          ...s,
          currentUser: userInfo,
        }));
      });
    }
  };

  useEffect(() => {
    const completeSsoLogin = async () => {
      const searchParams = new URL(window.location.href).searchParams;
      const code = searchParams.get('code');
      const state = searchParams.get('state');

      if (!code || !state) return;

      try {
        const redirect = await accessToken(code, state);
        await fetchUserInfo();
        window.location.replace(redirect);
      } catch (error) {
        window.history.replaceState(null, '', '/user/login');
        message.error(error instanceof Error ? error.message : 'SSO 登录失败');
      }
    };

    completeSsoLogin();
  }, [message]);

  const handleSubmit = async () => {
    const searchParams = new URL(window.location.href).searchParams;
    const redirect = searchParams.get('redirect') || '';
    authorization(redirect);
  };

  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          登录
          {Settings.title && ` - ${Settings.title}`}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          logo={<img alt="logo" src="/logo.svg" />}
          title="天秤·财富"
          subTitle={'家庭多人记账中心'}
          onFinish={async () => {
            await handleSubmit();
          }}
        >
          <Tabs
            activeKey="sso"
            centered
            items={[
              {
                key: 'sso',
                label: 'SSO登录',
              },
            ]}
          />

          <Flex gap="large" vertical>
            <Skeleton.Input active={false} block={true} />
            <Skeleton.Input active={true} block={true} />
          </Flex>
          <div
            style={{
              marginTop: 24,
              marginBottom: 24,
            }}
          >
            <ProFormCheckbox noStyle name="autoLogin">
              自动登录
            </ProFormCheckbox>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Login;
