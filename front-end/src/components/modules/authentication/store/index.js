import {
  RESET_AUTH_STATE,
  CURRENT_ACCOUNT,
  ACCESS_TOKEN,
  TOKEN_EXPIRED_IN,
  TOKEN_LAST_ACCESS,
  ACCOUNT_PERMISSION
} from './types';
import * as actions from './actions';
import * as getters from './getters';
import { LOCAL_STORAGE_ACCESS_TOKEN } from 'core/constant';

const state = {
  account: null,
  access_token: localStorage.getItem(LOCAL_STORAGE_ACCESS_TOKEN),
  expired_in: Number.NaN,
  token_last_access: Number.NaN,
  permissions: null
};

const mutations = {
  [RESET_AUTH_STATE](state) {
    state.account = null;
    state.access_token = null;
    state.expired_in = Number.NaN;
    state.token_last_access = Number.NaN;
    state.permissions = null;
  },

  [CURRENT_ACCOUNT](state, account) {
    state.account = account;
  },

  [ACCOUNT_PERMISSION](state, permissions = null) {
    state.permissions = permissions;
  },

  [ACCESS_TOKEN](state, token) {
    state.access_token = token;
  },

  [TOKEN_EXPIRED_IN](state, expired_in) {
    state.expired_in = expired_in;
  },

  [TOKEN_LAST_ACCESS](state, time) {
    state.token_last_access = time;
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
};
