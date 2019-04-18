import { RESTClient } from 'core';
import { correctRequestParams } from 'utilities';

export const getDocumentSts = async () => {
  return await RESTClient.get('/api/TAT-Tracking/Document-Sts');
};
export const getDataBasicWithCondition = async (dispatch, params) => {
  let listparams = {
    id_no: params.id_no,
    document_sts: params.document_sts,
    base_on_step_data: params.base_on_step_data,
    from_dt: params.from_dt,
    to_dt: params.to_dt
  };
  return await RESTClient.get(
    '/api/TAT-Tracking',
    correctRequestParams(listparams)
  );
};
