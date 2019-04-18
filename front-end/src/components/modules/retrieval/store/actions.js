import { RESTClient } from 'core';

export const searchRetrieval = async (dispatch, params) => {
  const result = await RESTClient.post(
    '/api/retrieval-document/search',
    params
  );
  return result;
};

export const loadcombobox = async () => {
  const result = await RESTClient.post(
    '/api/retrieval-document/getlistcomboboxmodel'
  );
  return result;
};

export const retrievalDocument = async (dispatch, params) => {
  const result = await RESTClient.post(
    '/api/retrieval-document/retrievalDocument/' + params.csrId,
    params.data
  );
  return result;
};

export const getDocument = async () => {
  // const result = await service.getInformation(params);
  const result = {
    success: true,
    data: [
      {
        id_no: '001',
        customert_name: 'Tran ngoc duy A',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      },
      {
        id_no: '002',
        customert_name: 'Tran ngoc duy',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      },
      {
        id_no: '003',
        customert_name: 'Tran ngoc duy',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      },
      {
        id_no: '004',
        customert_name: 'Tran ngoc duy',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      },
      {
        id_no: '005',
        customert_name: 'Tran ngoc duy',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      },
      {
        id_no: '006',
        customert_name: 'Tran ngoc duy',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      },
      {
        id_no: '007',
        customert_name: 'Tran ngoc duy',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      },
      {
        id_no: '008',
        customert_name: 'Tran ngoc duy',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      },
      {
        id_no: '009',
        customert_name: 'Tran ngoc duy',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      },
      {
        id_no: '010',
        customert_name: 'Tran ngoc duy',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      },
      {
        id_no: '011',
        customert_name: 'Tran ngoc duy',
        product: 'CD loan',
        sales_code: 'DRS0001',
        sales_name: 'Linh thu',
        sales_channe: 'Direct sales',
        CSR_distributior: 'MAFC0001',
        distributior_date: '01/10/2018',
        CSR_retrieved: '',
        retrieved_date: '',
        status: 'Distributed'
      }
    ]
  };
  return result;
};

export const actionRetrieve = async () => {
  return {
    success: true,
    data: [],
    message: 'successful!'
  };
};
