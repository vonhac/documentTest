import * as getters from './getters';
import * as actions from './actions';
import {
  UNREAD_NOTIFY,
  ALL_NOTIFY,
  SHOW_DRAWER,
  POSITION_FETCHING,
  STOP_FETCHING,
  SEARCHING_VALUE
} from './types';

const state = {
  unread_notify: [],
  all_notify: [],
  show_drawer: false,
  pos_fetching: 1,
  stop_fetching: false,
  searching_value: null
};

const mutations = {
  [UNREAD_NOTIFY](state, payload = []) {
    state.unread_notify = payload;
  },

  [ALL_NOTIFY](state, payload = []) {
    state.all_notify = payload;
  },

  [SHOW_DRAWER](state, payload = false) {
    state.show_drawer = payload;
  },

  [POSITION_FETCHING](state, payload = 1) {
    state.pos_fetching = payload;
  },

  [STOP_FETCHING](state, payload = false) {
    state.stop_fetching = payload;
  },

  [SEARCHING_VALUE](state, payload = null) {
    state.searching_value = payload;
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
