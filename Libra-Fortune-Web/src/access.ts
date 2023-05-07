/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(
  initialState: { currentUser?: Galaxy.OAuth2.System.UserDTO } | undefined,
) {
  const { currentUser } = initialState ?? {};
  return {
    // canAdmin: currentUser && currentUser.access === 'admin',
    canAdmin: currentUser,
  };
}
