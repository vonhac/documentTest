import Vue from 'vue';
import Vuex from 'vuex';
import _ from 'lodash';

import global from './global';
import notifications from './notifications';

Vue.use(Vuex);
const STORE_MODULE_POSTFIX = '/store/index.js';
const requireModule = require.context('components/modules', true, /\.js$/);
const modules = {
  global,
  notifications
};

requireModule.keys().forEach(filename => {
  if (!_.endsWith(filename, STORE_MODULE_POSTFIX)) return;

  let folderPath = _.replace(filename, STORE_MODULE_POSTFIX, '');
  if (folderPath == '') return;

  let moduleName = _.camelCase(
    _.replace(_.last(_.split(folderPath, '/')), '/', '')
  );
  modules[moduleName] =
    requireModule(filename).default || requireModule(filename);
});

export default new Vuex.Store({
  modules,
  strict: process.env.NODE_ENV !== 'production'
});
