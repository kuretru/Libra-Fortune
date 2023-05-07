import { galaxyCallback } from '@/services/galaxy-oauth2-client/service';
import { ProSkeleton } from '@ant-design/pro-components';
import { history, useModel, useSearchParams } from '@umijs/max';
import { message } from 'antd';
import React from 'react';
import { flushSync } from 'react-dom';

const LoginCallback: React.FC = () => {
  const { initialState, setInitialState } = useModel('@@initialState');
  let [searchParams] = useSearchParams();

  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();

    if (userInfo) {
      flushSync(() => {
        setInitialState((state: any) => ({ ...state, currentUser: userInfo }));
      });
    }
  };

  const login = async () => {
    const record: Galaxy.OAuth2.Client.OAuth2AuthorizeResponseDTO = {
      code: searchParams.get('code')!,
      state: searchParams.get('state')!,
    };
    const response = await galaxyCallback(record);
    console.log(`登录成功，userId=${response.data.userId}`);

    if (response.code === 100) {
      const defaultLoginSuccessMessage = '登录成功！';
      message.success(defaultLoginSuccessMessage);
      localStorage.setItem('userId', response.data.userId);
      localStorage.setItem('accessTokenId', response.data.accessToken.id);
      localStorage.setItem('accessToken', response.data.accessToken.secret);

      await fetchUserInfo();

      // 向redirect跳转并传递其余search参数
      const redirect = localStorage.getItem('redirect');

      if (redirect) {
        localStorage.removeItem('redirect');
        const urlSearchParams = new URLSearchParams(redirect);
        const pathname = urlSearchParams.get('redirect') ?? '/welcome';
        urlSearchParams.delete('redirect');
        history.push({
          pathname: pathname,
          search: urlSearchParams.toString(),
        });
      } else {
        history.push('/welcome');
      }
    }
  };

  login();

  return (
    <div
      style={{
        background: '#fafafa',
        padding: 24,
      }}
    >
      <ProSkeleton type="result" />
    </div>
  );
};

export default LoginCallback;
