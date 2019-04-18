/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:57:42
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-03 13:21:07
 */
import * as getters from './getters';
import * as actions from './actions';
import { SALE_CODE_IN_DEPARTMENT } from './types';

const state = {
  sale_codes: []
};
const mutations = {
  [SALE_CODE_IN_DEPARTMENT](state, data) {
    state.sale_codes = data || [];
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
