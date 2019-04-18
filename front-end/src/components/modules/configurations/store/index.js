import * as getters from './getters';
import * as actions from './actions';
import { ALL_PARAMETERS, PARAM_FETCHING, PARAM_DEADLINE } from './types';

const state = {
  all_parameters: {
    param_deadline: null,
    param_fetching: null
  },
  param_deadline: null,
  param_fetching: null
};

const mutations = {
  [ALL_PARAMETERS](state, payload) {
    state.all_parameters = payload || {
      param_deadline: null,
      param_fetching: null
    };
  },

  [PARAM_DEADLINE](state, payload = null) {
    state.param_deadline = payload;
    state.all_parameters.param_deadline = payload;
  },

  [PARAM_FETCHING](state, payload = null) {
    state.param_fetching = payload;
    state.all_parameters.param_fetching = payload;
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
