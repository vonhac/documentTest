import _ from 'lodash';
import { REQUEST_HEADER_AUTH } from './constant';

export const REQUEST_METHOD = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE'
};

var GLOBAL_LOCALE = null;
export const setRestClientLocale = i18n => {
  if (_.isNil(i18n)) throw 'Please provide correct i18n instance.';
  if (_.isNil(GLOBAL_LOCALE)) {
    GLOBAL_LOCALE = i18n;
  }
};

export class HTTPClient {
  constructor(axios) {
    if (!_.isObject(axios) && _.isEmpty(axios)) {
      throw 'Please provide correct AXIOS instance.';
    }
    this.axiosInstance = axios;
  }

  setAccessToken(access_token) {
    if (_.isString(access_token) && _.trim(access_token) != '') {
      this.axiosInstance.defaults.headers.common[
        REQUEST_HEADER_AUTH
      ] = `${access_token}`;
    } else {
      _.omit(this.axiosInstance.defaults.headers.common, [REQUEST_HEADER_AUTH]);
    }
  }

  setInterceptors(request = {}, response = {}) {
    injectInterceptor(this.axiosInstance, {
      ...request,
      ...{ type: 'request' }
    });
    injectInterceptor(this.axiosInstance, {
      ...response,
      ...{ type: 'response' }
    });
  }

  async get(url, requestParams = {}, headers = {}) {
    if (!_.isString(url)) throw 'String value of URL must correct';
    return await sendRequest(
      this.axiosInstance,
      url,
      REQUEST_METHOD.GET,
      headers,
      requestParams,
      {}
    );
  }

  async post(url, requestBody = {}, headers = {}) {
    if (!_.isString(url)) throw 'String value of URL must correct';
    return await sendRequest(
      this.axiosInstance,
      url,
      REQUEST_METHOD.POST,
      headers,
      {},
      requestBody
    );
  }

  async put(url, requestBody = {}, headers = {}) {
    if (!_.isString(url)) throw 'String value of URL must correct';
    return await sendRequest(
      this.axiosInstance,
      url,
      REQUEST_METHOD.PUT,
      headers,
      {},
      requestBody
    );
  }

  async delete(url, requestParams = {}, headers = {}) {
    if (!_.isString(url)) throw 'String value of URL must correct';
    return await sendRequest(
      this.axiosInstance,
      url,
      REQUEST_METHOD.DELETE,
      headers,
      requestParams,
      {}
    );
  }

  async upload(url, requestBody = {}, headers = {}) {
    if (!_.isString(url)) throw 'String value of URL must correct';
    headers['Content-Type'] = 'multipart/form-data';
    return await sendRequest(
      this.axiosInstance,
      url,
      REQUEST_METHOD.POST,
      headers,
      {},
      requestBody
    );
  }

  async request(
    url,
    method,
    headers = {},
    requestParams = {},
    requestBody = {}
  ) {
    if (!_.includes(_.keys(REQUEST_METHOD), method) || !_.isString(url)) {
      throw 'HTTPClient only support [GET, POST, PUT, DELETE]';
    }
    return await sendRequest(
      this.axiosInstance,
      url,
      method,
      headers,
      requestParams,
      requestBody
    );
  }
}

const SUPPORT_REQ_BODY = [REQUEST_METHOD.POST, REQUEST_METHOD.PUT];

const sendRequest = async (
  axios,
  url,
  method,
  headers = {},
  requestParams,
  requestBody
) => {
  let params = {};
  if (!_.isEmpty(requestParams)) {
    params = new URLSearchParams(requestParams);
  }

  let data = {};
  if (_.includes(SUPPORT_REQ_BODY, method)) {
    data = requestBody;
  }

  let transformRequest = [
    data => {
      if (_.isString(data) || isFormData(data)) {
        return data;
      } else if (_.isObject(data)) {
        return JSON.stringify(data);
      }
      throw 'Request body must belong with [String, Object, FormData]';
    }
  ];

  let transformResponse = [
    response => {
      return response;
    }
  ];
  let opts = {
    method,
    params,
    headers: {
      ...axios.defaults.headers.common,
      ...axios.defaults.headers[method],
      ...headers
    },
    data: data || {},
    transformRequest,
    transformResponse
  };

  return readRestResponse(await axios.request(url, opts));
};

const toObject = urlParams => {
  if (urlParams instanceof URLSearchParams) {
    let result = {};
    for (let pair of urlParams.entries()) {
      result[pair[0]] = pair[1];
    }
  }
  return {};
};

const injectInterceptor = (axios, interceptor) => {
  var successHandler = interceptor[interceptor.type];
  if (!_.isFunction(successHandler)) successHandler = success => success;

  var failHandler = interceptor.error;
  if (!_.isFunction(failHandler)) failHandler = error => error;

  axios.interceptors[interceptor.type].use(successHandler, failHandler);
};

const isFormData = val => {
  return typeof FormData !== 'undefined' && val instanceof FormData;
};

/**
 * The first check empty response to ignore CORS request
 */
const readRestResponse = resp => {
  if (_.isNil(resp) || !_.isNumber(resp.status))
    return {
      success: false,
      data: null,
      message: localeMessage('http_client.errors.4xx')
    };

  switch (resp.status) {
    case 0:
      return {
        success: false,
        data: null,
        message: localeMessage('http_client.errors.000'),
        http_code: 0
      };

    case 200:
      return resp.data;

    case 304:
      return {
        success: false,
        data: null,
        message: localeMessage('http_client.errors.304'),
        http_code: 304
      };

    // case 401:
    //   This case was handled in response interceptor
    //   break;

    case 403:
      return {
        success: false,
        data: null,
        message: localeMessage('http_client.errors.403'),
        http_code: 403
      };

    case 404:
      return {
        success: false,
        data: null,
        message: localeMessage('http_client.errors.404'),
        http_code: 404
      };

    case 415:
      return {
        success: false,
        data: null,
        message: localeMessage('http_client.errors.415'),
        http_code: 415
      };

    case 500:
      return {
        success: false,
        data: null,
        message: localeMessage('http_client.errors.500'),
        http_code: 500
      };

    default:
      return processUnknownCode(resp.status);
  }
};

const processUnknownCode = code => {
  if (100 <= code && code < 200) {
    return {
      success: false,
      message: localeMessage('http_client.errors.1xx'),
      http_code: code
    };
  } else if (200 <= code && code < 300) {
    return {
      success: false,
      message: localeMessage('http_client.errors.2xx'),
      http_code: code
    };
  } else if (300 <= code && code < 400) {
    return {
      success: false,
      message: localeMessage('http_client.errors.3xx'),
      http_code: code
    };
  } else if (400 <= code && code < 500) {
    return {
      success: false,
      message: localeMessage('http_client.errors.4xx'),
      http_code: code
    };
  } else if (500 <= code && code < 600) {
    return {
      success: false,
      message: localeMessage('http_client.errors.5xx'),
      http_code: code
    };
  }
};

const localeMessage = message => {
  if (_.isNil(GLOBAL_LOCALE)) return message;
  try {
    return GLOBAL_LOCALE.t(message);
  } catch (e) {
    return message;
  }
};

export const readRequestParams = params => {
  if (_.isNil(params)) return {};

  if (_.startsWith(params, 'http://') || _.startsWith(params, 'https://')) {
    let url = URL(params);
    return toObject(new URLSearchParams(url.search));
  }

  if (_.isString(params)) {
    return toObject(new URLSearchParams(params));
  }

  return {};
};
