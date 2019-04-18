import { RESTClient } from 'core';
export const saveData = async (dispatch, params) => {
  return await RESTClient.post('/api/branch-sips', params);
};
export const getalldata = async (dispatch, staff_cd) => {
  return await RESTClient.get('/api/branch-sips', { staff_cd });
};

export const updateData = async (dispatch, params) => {
  return await RESTClient.put('/api/branch-sips/updateDataBranchSip', params);
};

export const removeBranch = async (dispatch, ids) => {
  return await RESTClient.put('/api/branch-sips/branch', ids);
};
export const getAllbranchSip = async () => {
  return await RESTClient.get('/api/branch-sips/getAllbranchSip');
};
