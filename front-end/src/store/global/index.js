import * as CONSTANT from './types';
import * as getters from './getters';
import * as actions from './actions';

const state = {
  drawerToggled: true,

  loading_app: false,
  loading_feature: false,

  messagePoll: []
};

const showMsg = (state, msgInfo) => {
  let snackbar_info = {
    code: new Date().getTime(),
    show: true,
    ...msgInfo
  };

  state.messagePoll.push(snackbar_info);
  if (state.messagePoll.length > 5) {
    state.messagePoll.shift();
  }
};

const mutations = {
  [CONSTANT.TOGGLE_APP_DRAWER](state, flag) {
    state.drawerToggled = flag;
  },

  [CONSTANT.SET_FEATURE_LOADING](state, flag) {
    state.loading_feature = flag;
  },

  [CONSTANT.SET_APPLICATION_LOADING](state, flag) {
    state.loading_app = flag;
  },

  [CONSTANT.ADD_SUCCESS_MSG](state, message) {
    showMsg(state, { message, color: 'green' });
  },

  [CONSTANT.ADD_ERROR_MSG](state, message) {
    showMsg(state, { message, color: 'red' });
  },

  [CONSTANT.ADD_WARNING_MSG](state, message) {
    showMsg(state, { message, color: 'orange' });
  },

  [CONSTANT.CLEAR_MESSAGE](state, code) {
    state.messagePoll = state.messagePoll.filter(
      message => message.code != code
    );
  },

  [CONSTANT.CLEAR_ALL_MESSAGE](state) {
    state.messagePoll = [];
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
