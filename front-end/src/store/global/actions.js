import * as CONSTANT from './types';
import _ from 'lodash';
import store from 'store';

export const toggleDrawer = ({ commit }, flag) => {
  if (_.isBoolean(flag)) {
    commit(CONSTANT.TOGGLE_APP_DRAWER, flag);
  } else {
    let status = store.getters['global/drawerToggled'];
    commit(CONSTANT.TOGGLE_APP_DRAWER, !status);
  }
};

export const setAppLoading = ({ commit }, flag = false) => {
  commit(CONSTANT.SET_APPLICATION_LOADING, flag);
};

export const setFeatureLoading = ({ commit }, flag = false) => {
  commit(CONSTANT.SET_FEATURE_LOADING, flag);
};

export const showSuccessMsg = ({ commit }, msg) => {
  commit(CONSTANT.ADD_SUCCESS_MSG, msg);
};

export const showErrorMsg = ({ commit }, msg) => {
  commit(CONSTANT.ADD_ERROR_MSG, msg);
};

export const showWarningMsg = ({ commit }, msg) => {
  commit(CONSTANT.ADD_WARNING_MSG, msg);
};

export const resetNotify = ({ commit }, code) => {
  commit(CONSTANT.CLEAR_MESSAGE, code);
};

export const resetAllNotify = ({ commit }) => {
  commit(CONSTANT.CLEAR_ALL_MESSAGE);
};
