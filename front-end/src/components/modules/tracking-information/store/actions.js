/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by: CHI DOAN
 * @Last Modified time: 2019-03-03
 */
import _ from 'lodash';
import { RESTClient } from 'core';
import { correctRequestParams } from 'utilities';

export const retrieveChannelAndStatus = async () => {
  return await RESTClient.get('/api/tracking-information/channels');
};

export const filterDocuments = async (dispatch, conditions) => {
  let request_body = {
    department_cd: conditions.channel,
    status_cd: conditions.status,
    distributed_date: conditions.distribute_date,
    sending_date: conditions.sending_date,
    sale_cd: conditions.sale_cd,
    document_cd: conditions.document_cd,
    id_no: conditions.id_no
  };
  if (!_.isNil(conditions.BPO_checking)) {
    request_body.bpo_checking = conditions.BPO_checking;
  }
  return await RESTClient.get(
    '/api/tracking-information/documents',
    correctRequestParams(request_body, { ignoreEmpty: true })
  );
};

export const loadDocuments = async (dispatch, document_cd) => {
  return await RESTClient.post(
    `/api/tracking-information/documents/${document_cd}/loaddocuments`
  );
};

export const retrieveStatusHistory = async (dispatch, doc_no) => {
  return await RESTClient.post(
    `/api/tracking-information/documents/${doc_no}/history-status`
  );
};

export const retrieveDeferHistory = async (dispatch, doc_no) => {
  return await RESTClient.post(
    `/api/tracking-information/documents/${doc_no}/defers`
  );
};

export const addDeferDocuments = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/tracking-information/documents/adddefers',
    params
  );
};

export const updateDeferDocuments = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/tracking-information/documents/updatedefers',
    params
  );
};
export const updateDeferDocumentsList = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/tracking-information/documents/updatedeferslist',
    params
  );
};

export const addDefersStatusHistory = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/tracking-information/documents/addstatusdeferhistory',
    params
  );
};

export const changeStatusDefer = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/tracking-information/documents/changestatusdefer',
    params
  );
};

export const changeStatusPass = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/tracking-information/documents/changestatuspass',
    params
  );
};

export const changeStatusModified = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/tracking-information/documents/changestatusmodified',
    params
  );
};

export const changeStatusCancel = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/tracking-information/documents/changestatuscancel',
    params
  );
};

export const addAppID = async (dispatch, params) => {
  return await RESTClient.post(
    '/api/tracking-information/documents/addappid',
    params
  );
};

export const uploadFileModified = async (dispatch, params) => {
  return await RESTClient.upload(
    `/api/tracking-information/documents/upload/${params.filename}`,
    params.formData
  );
};

export const viewAttachedFile = async (dispatch, filename) => {
  return await RESTClient.get(
    `/api/tracking-information/documents/attachment-file/${filename}`
  );
};

export const getHeaderExcelFile = () => {
  return {
    document_cd: {
      text: 'ID',
      type: 'Number',
      width: 50
    },
    id_no: {
      text: 'ID NO',
      type: 'String',
      width: 50
    },
    customer_nm: {
      text: 'CUSTOMER NAME',
      type: 'String',
      width: 50
    },
    product_nm: {
      text: 'PRODUCT NAME',
      type: 'String'
    },
    sales_cd: {
      text: 'SALES CODE',
      type: 'String'
    },
    sales_nm: {
      text: 'SALES NAME',
      type: 'String'
    },
    manager: {
      text: 'MANAGER',
      type: 'String'
    },
    sales_chnl: {
      text: 'SALES CHANNEL',
      type: 'String'
    },
    send_date: {
      text: 'SEND DATE',
      type: 'String'
    },
    csr_code: {
      text: 'CSR CODE',
      type: 'String'
    },
    distributed_date: {
      text: 'DISTRIBUTED DATE',
      type: 'String'
    },
    created_by: {
      text: 'CREATED BY',
      type: 'String'
    },
    status_cd: {
      text: 'STATUS',
      type: 'String'
    },
    branch_id: {
      text: 'BRANCH SIP',
      type: 'String'
    },
    note_bpo: {
      text: 'NOTE BPO',
      type: 'String'
    },
    description: {
      text: 'DESCRIPTION',
      type: 'String'
    }
  };
};
