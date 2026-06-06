export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        name: '登录',
        path: '/user/login',
        component: './user/login',
      },
    ],
  },
  {
    path: '/welcome',
    name: '欢迎',
    icon: 'smile',
    component: './Welcome',
  },
  {
    name: '元数据管理',
    icon: 'database',
    path: '/metadata',
    routes: [
      {
        name: '货币',
        path: '/metadata/currency',
        component: './metadata/currency',
      },
      {
        name: '分类',
        path: '/metadata/category',
        component: './metadata/category',
      },
      {
        name: '标签',
        path: '/metadata/tag-set',
        component: './metadata/tag-set',
      },
    ],
  },
  {
    name: '账户管理',
    icon: 'accountBook',
    path: '/account',
    routes: [
      {
        name: '账户',
        path: '/account/account',
        component: './account/account',
      },
    ],
  },
  {
    name: '账本管理',
    icon: 'book',
    path: '/ledger',
    routes: [
      {
        name: '账本',
        path: '/ledger/ledger',
        component: './ledger/ledger',
      },
    ],
  },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      {
        path: '/admin',
        redirect: '/admin/sub-page',
      },
      {
        path: '/admin/sub-page',
        name: '二级管理页',
        component: './Admin',
      },
    ],
  },
  {
    name: '查询表格',
    icon: 'table',
    path: '/list',
    component: './table-list',
  },
  {
    path: '/',
    redirect: '/welcome',
  },
  {
    component: './exception/404',
    layout: false,
    path: './*',
  },
];
