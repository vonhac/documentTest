import { RESTClient } from 'core';
import { correctRequestParams } from 'utilities';

export const getBPODocumentData = async (dispatch, params) => {
  let conditionSearch = {
    sale_channel: params.sale_channel,
    sale_cd: params.sale_cd,
    bpo_check: params.bpo_check,
    distribute_dt: params.distribute_dt
  };

  return await RESTClient.get(
    '/api/BPO-document',
    correctRequestParams(conditionSearch)
  );
};
export const getAllSaleChannel = async () => {
  return await RESTClient.get('/api/BPO-document/department-list');
};
export const BPOCheckDocument = async (dispatch, params) => {
  return await RESTClient.put('/api/BPO-document/BPOCheckDocument', params);
};
