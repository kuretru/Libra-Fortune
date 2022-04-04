export default [
  {
    path: '/users',
    layout: false,
    routes: [
      {
        path: '/users',
        routes: [
          { name: '登录', path: '/users/login', component: './user/Login' },
          { name: '回调', path: '/users/login/callback', component: './user/Callback' },
        ],
      },
      { component: './404' },
    ],
  },
  { path: '/welcome', name: '欢迎', icon: 'smile', component: './Welcome' },
  {
    path: '/ledgers',
    name: '账本管理',
    icon: 'snippets',
    routes: [
      {
        path: '/ledgers',
        component: './Ledger/Ledger',
        name: '账本管理',
      },
    ],
  },

  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    component: './Admin',
    routes: [
      { path: '/admin/sub-page', name: '二级管理页', icon: 'smile', component: './Welcome' },
      { component: './404' },
    ],
  },
  { name: '查询表格', icon: 'table', path: '/list', component: './TableList' },
  { path: '/', redirect: '/welcome' },
  { component: './404' },
];
