import _ from 'lodash';
import axios from 'axios';
import store from 'store';
import router from 'router';
import { REQUEST_HEADER_AUTH } from './constant';
import { HTTPClient } from './http-client';

const retrieveServerIp = () => {
  let serverIp = process.env.VUE_APP_SERVER_BASE_IP;
  let serverPort = process.env.VUE_APP_SERVER_BASE_PORT;
  if (process.env.NODE_ENV == 'development' && serverPort) {
    if (serverIp) return `${location.protocol}//${serverIp}:${serverPort}/`;
    return `${location.protocol}//${location.hostname}:${serverPort}/`;
  }
  return process.env.VUE_APP_SERVER_BASE_LOCATION || '/';
};

axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.put['Content-Type'] = 'application/json';
export const RESTClient = new HTTPClient(
  axios.create({
    baseURL: retrieveServerIp(),
    responseType: 'json'
  })
);

const REQUEST_INTERCEPTOR = {
  request: config => {
    let isLoginAPI = _.endsWith(config.url, '/login/auth');
    if (!isLoginAPI && store.getters['authentication/isExpired']) {
      store.dispatch('authentication/logout');
      router.push({ path: '/401' });
      return null;
    }

    store.dispatch('authentication/updateLastAccess');
    if (_.isNil(config.headers.common[REQUEST_HEADER_AUTH])) {
      config.headers.common[REQUEST_HEADER_AUTH] =
        store.getters['authentication/getAccessToken'] || '';
    }
    return config;
  }
};
const RESPONSE_INTERCEPTOR = {
  error: error => {
    let response = error.response;
    if (!_.isNil(response) && response.status == 401) {
      store.dispatch('authentication/logout');
      router.push({ path: '/401' });
    } else if (!_.isNil(error.message) && error.message === 'Network Error') {
      response = { status: 0 };
    } else {
      response = {};
    }
    return response;
  }
};
RESTClient.setInterceptors(REQUEST_INTERCEPTOR, RESPONSE_INTERCEPTOR);

export { REQUEST_METHOD, readRequestParams } from './http-client';
export { DOCUMENT_STATUS } from './document-status';
