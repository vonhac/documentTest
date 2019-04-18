/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
import { RESTClient } from 'core';

export const loadDataDefault = async () => {
  return await RESTClient.get('/api/defer-rule/groupdefer-load');
};
/**
 * ADD Group defer
 */
export const addDataDefault = async (dispatch, params) => {
  return await RESTClient.post('/api/defer-rule/groupdefer-add', params);
};

export const editDataDefault = async (dispatch, params) => {
  return await RESTClient.post('/api/defer-rule/groupdefer-edit', params);
};

export const delDataDefault = async (dispatch, params) => {
  return await RESTClient.post('/api/defer-rule/groupdefer-del', params);
};

/**
 * ADD defer detail
 */
export const cbGroupCd = async (dispatch, params) => {
  return await RESTClient.get('/api/defer-rule/infodeferdetail', params);
};

export const addDeferDetail = async (dispatch, params) => {
  return await RESTClient.post('/api/defer-rule/deferdetail-add', params);
};

export const editDeferDetail = async (dispatch, params) => {
  return await RESTClient.post('/api/defer-rule/deferdetail-edit', params);
};

export const delDeferDetail = async (dispatch, params) => {
  return await RESTClient.post('/api/defer-rule/deferdetail-del', params);
};

export const searchDeferDetail = async (dispatch, group_cd) => {
  return await RESTClient.post(
    `/api/defer-rule/${group_cd}/deferdetail-searchid`
  );
};
export const searchDeferDetailNm = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/defer-rule/deferdetail-searchid-nm',
    params
  );
};

export const retrieveDeferGroups = async () => {
  return await RESTClient.get('/api/defer-rule/information');
};

export const retrieveDeferDetails = async (dispatch, group) => {
  return DEFER_DETAILS[group];
};

const DEFER_DETAILS = {};
