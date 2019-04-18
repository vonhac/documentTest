import SHA256 from 'sha256';
import I18N from 'locale';
import router from 'router';
import { RESTClient } from 'core';
import { Checker } from 'utilities';
import * as TYPES from './types';
import * as CONSTANTS from 'core/constant';

export const firstLoad = async ({ commit }) => {
  let access_token = localStorage.getItem(CONSTANTS.LOCAL_STORAGE_ACCESS_TOKEN);
  if (!Checker.hasText(access_token)) return signOut(commit, '/login');

  let now = new Date().getTime();
  let last_access = Number(
    localStorage.getItem(CONSTANTS.LOCAL_STORAGE_LAST_ACCESS)
  );
  let expired_in = Number(
    localStorage.getItem(CONSTANTS.LOCAL_STORAGE_EXPIRES_IN)
  );

  if (Checker.isNumber(last_access) && Checker.isNumber(expired_in)) {
    let isExpired = now - expired_in > last_access;
    if (isExpired) return signOut(commit, '/401');
  }

  let response = await RESTClient.post('/authenticate', access_token);
  if (response.success) {
    handleAuthResponse(commit, response);
  } else {
    signOut(commit, '/401');
  }
};

export const login = async ({ commit }, account) => {
  let result = await RESTClient.post('/login/auth', {
    username: account.username,
    password: SHA256(account.password)
  });

  if (result.success) {
    handleAuthResponse(commit, result);
    return { success: true };
  }
  return {
    success: false,
    message: result.message || I18N.t('login_page.errors.login')
  };
};

export const logout = ({ commit }) => {
  localStorage.clear();
  commit(TYPES.RESET_AUTH_STATE);
  RESTClient.setAccessToken();
};

export const updateLastAccess = ({ commit }) => {
  let now = new Date().getTime();
  commit(TYPES.TOKEN_LAST_ACCESS, now);
  localStorage.setItem(CONSTANTS.LOCAL_STORAGE_LAST_ACCESS, now);
};

const handleAuthResponse = (commit, resp) => {
  let respData = resp.data || {};
  RESTClient.setAccessToken(respData.access_token);

  let now = new Date().getTime();
  if (!Checker.isNumber(respData.expires_in)) respData.expires_in = 45;
  respData.expires_in = respData.expires_in * 60 * 1000;

  commit(TYPES.CURRENT_ACCOUNT, respData.user_info);
  commit(TYPES.ACCOUNT_PERMISSION, respData.user_permissions);
  commit(TYPES.ACCESS_TOKEN, respData.access_token);
  commit(TYPES.TOKEN_EXPIRED_IN, respData.expires_in);
  commit(TYPES.TOKEN_LAST_ACCESS, now);

  localStorage.setItem(
    CONSTANTS.LOCAL_STORAGE_ACCESS_TOKEN,
    respData.access_token
  );
  localStorage.setItem(CONSTANTS.LOCAL_STORAGE_EXPIRES_IN, respData.expires_in);
  localStorage.setItem(CONSTANTS.LOCAL_STORAGE_LAST_ACCESS, now);
};

const signOut = (commit, url) => {
  logout({ commit });
  router.push(url);
};
