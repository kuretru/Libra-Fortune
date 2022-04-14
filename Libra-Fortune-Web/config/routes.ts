export default [
  {
    path: '/users',
    layout: false,
    routes: [
      {
        path: '/users',
        routes: [
          { component: './User/Login', name: '登录', path: '/users/login' },
          { component: './User/Callback', name: '回调', path: '/users/login/callback' },
        ],
      },
      { component: './404' },
    ],
  },
  { component: './Welcome', icon: 'smile', path: '/welcome', name: '欢迎' },
  {
    icon: 'snippets',
    name: '账本管理',
    routes: [
      {
        component: './Ledger/Ledger',
        name: '账本管理',
        path: '/ledgers',
      },
      {
        component: './Ledger/Category',
        hideInMenu: true,
        name: '账本分类管理',
        path: '/ledgers/:ledgerId/categories',
      },
      {
        component: './Ledger/User',
        hideInMenu: true,
        name: '账本用户管理',
        path: '/ledgers/:ledgerId/users',
      },
    ],
  },
  { path: '/', redirect: '/welcome' },
  { component: './404' },
];
