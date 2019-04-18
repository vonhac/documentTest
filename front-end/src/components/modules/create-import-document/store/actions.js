import { RESTClient } from 'core';
import { SALE_CODE_IN_DEPARTMENT } from './types';

export const searchDocument = async (dispatch, params) => {
  return await RESTClient.post('/api/import-document/search', params);
};

export const addDocument = async (dispatch, params) => {
  const result = await RESTClient.post('/api/import-document/add', params);
  return result;
};

export const updateDocument = async (dispatch, params) => {
  const result = await RESTClient.post('/api/import-document/update', params);
  return result;
};

export const loadcombobox = async ({ commit }, params) => {
  const result = await RESTClient.post(
    '/api/import-document/getlistcomboboxmodel',
    params
  );
  if (result.success) {
    let { listSaleAccount = [] } = result.data;
    commit(SALE_CODE_IN_DEPARTMENT, listSaleAccount);
  }
  return result;
};

export const checkSaleCode = async (dispatch, sales_cd) => {
  return await RESTClient.post(
    `/api/import-document/${sales_cd}/checksalecode`
  );
};

export const deleteDocument = async (dispatch, document_cd) => {
  return await RESTClient.post(`/api/import-document/${document_cd}/delete`);
};

export const importDocument = async (dispatch, params) => {
  return await RESTClient.post('/api/import-document/importDocument', params);
};

export const checkInvalidDocument = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/import-document/checkInvalidDocument',
    params
  );
};
export const sendCSRDocument = async (dispatch, requestObj) => {
  return await RESTClient.post('/api/import-document/sendCSR', requestObj);
};
