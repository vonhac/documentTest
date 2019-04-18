import NAMING from './routes-naming';

export default [
  {
    path: '*',
    redirect: {
      path: '/404'
    }
  },
  {
    path: '/401',
    meta: {
      code: 401,
      title: 'Unauthorized'
    },
    name: 'Unauthorized',
    component: () => import('modules/authentication/error-page')
  },
  {
    path: '/403',
    meta: {
      code: 403,
      title: 'Access Denied'
    },
    name: 'AccessDenied',
    component: () => import('modules/authentication/error-page')
  },
  {
    path: '/404',
    meta: {
      code: 404,
      title: 'Not Found'
    },
    name: 'NotFound',
    component: () => import('modules/authentication/error-page')
  },
  {
    path: '/500',
    meta: {
      code: 500,
      title: 'Internal Error Server'
    },
    name: 'ServerError',
    component: () => import('modules/authentication/error-page')
  },
  {
    path: '/login',
    meta: {
      title: 'Login page'
    },
    name: 'Login',
    component: () => import('modules/authentication')
  },
  {
    path: '/',
    meta: {
      title: 'menu.dashboard',
      requiresAuth: true,
      menu: true,
      icon: 'home',
      header: 'page_headers.dashboard'
    },
    name: NAMING.DASHBOARD,
    component: () => import('modules/dashboard')
  },
  {
    path: '/personal',
    meta: {
      title: 'menu.personal',
      requiresAuth: true,
      header: 'page_headers.personal'
    },
    name: NAMING.PERSONAL,
    component: () => import('modules/personal')
  },
  {
    path: '/defer-rule',
    redirect: {
      path: '/defer-rule/manage-group'
    },
    meta: {
      title: 'menu.defer_rule.parent',
      menu: true,
      super: true,
      group: 'defer-rule',
      icon: 'code'
    }
  },
  {
    path: '/defer-rule/manage-group',
    meta: {
      title: 'menu.defer_rule.group',
      requiresAuth: true,
      menu: true,
      group: 'defer-rule',
      header: 'page_headers.defer_rule.group'
    },
    name: NAMING.DEFER_GROUP,
    component: () => import('modules/defer-rule/manage-group.vue')
  },
  {
    path: '/defer-rule/manage-defer-detail',
    meta: {
      title: 'menu.defer_rule.detail',
      requiresAuth: true,
      menu: true,
      group: 'defer-rule',
      header: 'page_headers.defer_rule.detail'
    },
    name: NAMING.DEFER_DETAIL,
    component: () => import('modules/defer-rule/manage-defer-detail.vue')
  },
  {
    path: '/configurations',
    meta: {
      title: 'menu.configurations',
      requiresAuth: true,
      menu: true,
      icon: 'settings',
      header: 'page_headers.configurations'
    },
    name: NAMING.CONFIGURATIONS,
    component: () => import('modules/configurations')
  },
  {
    path: '/area-manager',
    meta: {
      title: 'menu.area_manager',
      requiresAuth: true,
      menu: true,
      icon: 'group',
      header: 'page_headers.area_manager'
    },
    name: NAMING.AREA_MANAGER,
    component: () => import('modules/information-of-staff/area-manager.vue')
  },
  {
    path: '/create-import-document',
    meta: {
      title: 'menu.import_document',
      requiresAuth: true,
      menu: true,
      icon: 'book',
      header: 'page_headers.import_document'
    },
    name: NAMING.CREATE_IMPORT_DOCUMENT,
    component: () => import('modules/create-import-document/list-document.vue')
  },
  {
    path: '/distribute',
    meta: {
      title: 'menu.distribute',
      requiresAuth: true,
      menu: true,
      icon: 'call_split',
      header: 'page_headers.distribute'
    },
    name: NAMING.DISTRIBUTE,
    component: () => import('modules/distribute/list-distribute.vue')
  },
  {
    path: '/retrieval',
    meta: {
      title: 'menu.retrieval',
      requiresAuth: true,
      menu: true,
      icon: 'reply',
      header: 'page_headers.retrieval'
    },
    name: NAMING.RETRIEVAL,
    component: () => import('modules/retrieval/list-retrieval.vue')
  },
  {
    path: '/note-bpo-check-document',
    meta: {
      title: 'menu.bpo_check_doc',
      requiresAuth: true,
      menu: true,
      icon: 'notes',
      header: 'page_headers.bpo_check_doc'
    },
    name: NAMING.BPO_CHECK_DOCUMENT,
    component: () =>
      import('modules/note-bpo-check-document/list-bpo-check-document.vue')
  },
  {
    path: '/tracking-information',
    meta: {
      title: 'menu.tracking_info',
      requiresAuth: true,
      menu: true,
      icon: 'image_search',
      header: 'page_headers.tracking_info'
    },
    name: NAMING.TRACKING_INFORMATION,
    component: () => import('modules/tracking-information')
  },
  {
    path: '/tat-tracking',
    meta: {
      title: 'menu.tat_tracking',
      requiresAuth: true,
      menu: true,
      icon: 'done_all',
      header: 'page_headers.tat_tracking'
    },
    name: NAMING.TAT_TRACKING,
    component: () => import('modules/tat-tracking/main.vue')
  }
];
