import _ from 'lodash';

export const getAccessToken = state => state.access_token;
export const getLastAccess = state => state.token_last_access;
export const getExpiredIn = state => state.expired_in;
export const isExpired = state => {
  if (!_.isNumber(state.token_last_access) || !_.isNumber(state.expired_in))
    return false;
  return new Date().getTime() - state.token_last_access > state.expired_in;
};
export const authUser = state => {
  return _.isNil(state.account) ? { account_id: 'anonymous' } : state.account;
};
export const isAdminUser = state => {
  let { admin = 0 } = state.account || {};
  return admin == 1 ? true : false;
};
