import { RESTClient } from 'core';

export const searchDistributeDocument = async (dispatch, params) => {
  const result = await RESTClient.post(
    '/api/distribute-document/search',
    params
  );
  return result;
};

export const receivedDocument = async (dispatch, params) => {
  const result = await RESTClient.post(
    '/api/distribute-document/receivedDocument',
    params
  );
  return result;
};

export const getCsrList = async (dispatch, params) => {
  const result = await RESTClient.post(
    '/api/distribute-document/getCsrList',
    params
  );
  return result;
};

export const distributeDocument = async (dispatch, params) => {
  const result = await RESTClient.post(
    '/api/distribute-document/distribute',
    params
  );
  return result;
};
export const actionReceive = async () => {
  // const result = await RESTClient.post('');
  const result = {
    success: true,
    data: [],
    message: 'successful!'
  };
  return result;
};
