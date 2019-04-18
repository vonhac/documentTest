import Vue from 'vue';
import VueRouter from 'vue-router';
import NProgress from 'nprogress';
import routes from './routes';
import i18n from 'locale';
import vueStore from 'store';
import { Checker } from 'utilities';

Vue.use(VueRouter);

const router = new VueRouter({
  mode: 'history',
  linkActiveClass: 'open active',
  routes
});

router.beforeEach(async (to, from, next) => {
  document.title = to.meta.title
    ? 'DMT - ' + i18n.t(to.meta.title)
    : 'Document Management Tool';
  NProgress.start();

  let isLoginPage = to.path.indexOf('/login') === 0;
  let token = vueStore.getters['authentication/getAccessToken'];
  let redirectTo = null;
  if (Checker.hasText(token)) {
    if (isLoginPage) redirectTo = '/';
  } else {
    if (Checker.isTrue(to.meta.requiresAuth)) {
      redirectTo = '/login';
      vueStore.dispatch('authentication/logout');
    }
  }
  Checker.hasText(redirectTo) ? next(redirectTo) : next();
});

router.afterEach(() => {
  NProgress.done();
});

export { default as ROUTES_NAMING } from './routes-naming';
export default router;
