import _ from 'lodash';
import { RESTClient } from 'core';
import {
  ALL_PARAMETERS,
  PARAM_FETCHING,
  PARAM_DEADLINE,
  DOCUMENT_DEADLINE,
  PERIODIC_FETCH_NOTIFY
} from './types';

export const getAllParameters = async ({ commit }) => {
  const response = await RESTClient.get('/api/parameters');
  if (response.success) {
    let param_deadline = _.find(response.data, {
      param_cd: DOCUMENT_DEADLINE
    });
    let param_fetching = _.find(response.data, {
      param_cd: PERIODIC_FETCH_NOTIFY
    });
    commit(ALL_PARAMETERS, { param_deadline, param_fetching });
    commit(PARAM_DEADLINE, param_deadline.value);
    commit(PARAM_FETCHING, param_fetching.value);
  }
  return response;
};

export const getParamDeadline = async ({ commit }) => {
  const response = await RESTClient.get('/api/parameters/deadline');
  if (response.success) {
    const { value = null } = response.data;
    commit(PARAM_DEADLINE, value);
  }
  return response;
};

export const setParamDeadline = async ({ commit }, value) => {
  const response = await RESTClient.put('/api/parameters/deadline', value);
  if (response.success) {
    commit(PARAM_DEADLINE, value);
  }
  return response;
};

export const getParamPeriodicFetch = async ({ commit }) => {
  const response = await RESTClient.get('/api/parameters/periodic-fetch');
  if (response.success) {
    const { value = null } = response.data;
    commit(PARAM_FETCHING, value);
  }
  return response;
};

export const setParamPeriodicFetch = async ({ commit }, value) => {
  const response = await RESTClient.put(
    '/api/parameters/periodic-fetch',
    value
  );
  if (response.success) {
    commit(PARAM_FETCHING, value);
  }
  return response;
};
