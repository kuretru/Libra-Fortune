import Settings from '@/../config/defaultSettings';
import Footer from '@/components/Footer';
import { galaxyAuthorize } from '@/services/galaxy-oauth2-client/service';
import { LoginForm } from '@ant-design/pro-components';
import { useEmotionCss } from '@ant-design/use-emotion-css';
import { Helmet, useSearchParams } from '@umijs/max';
import { Skeleton } from 'antd';
import React from 'react';

const Login: React.FC = () => {
  let [searchParams] = useSearchParams();

  const containerCss = useEmotionCss(() => {
    return {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    };
  });

  const handleSubmit = async () => {
    // 保存传递过来的redirect参数，回调完成后跳转回原来的页面
    if (searchParams.has('redirect')) {
      localStorage.setItem('redirect', searchParams.toString());
    }

    const record: Galaxy.OAuth2.Client.OAuth2AuthorizeRequestDTO = {
      scopes: ['email'],
    };
    const response = await galaxyAuthorize(record);
    window.location.href = response.data;
  };

  return (
    <div className={containerCss}>
      <Helmet>
        <title>
          {'登录'}-{Settings.title}
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
          title={Settings.title}
          subTitle={'Spring Boot + Ant Design Pro 实现的家庭账本'}
          initialValues={{
            autoLogin: true,
          }}
          onFinish={async () => {
            await handleSubmit();
          }}
        >
          <div
            style={{
              marginBottom: 24,
            }}
          >
            <Skeleton active />
            转到【双子·身份验证中心】登录
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};
export default Login;
