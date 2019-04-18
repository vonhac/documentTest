export default {
  DOCUMENT: {
    headers: {
      id_no: 'Id No',
      customer_name: 'Customer Name',
      product: 'Product',
      create: 'Create',
      sales_code: 'Sales Code',
      sales_name: 'Sales Name',
      sales_channel: 'Sales Channel',
      status: 'Status',
      send_date: 'Send Date',
      place: 'Place',
      received_by: 'Received By',
      duplicate_description: 'Duplicate Description',
      retrieved_date: 'Retrieved Date'
    },

    options: {
      key: 'id_no',
      autoNo: true,
      checker: true,
      fixed_row_page: true,
      page_size: [10, 20],
      center: [
        'id_no',
        'product',
        'create',
        'sales_code',
        'sales_name',
        'sales_channel',
        'status',
        'send_date',
        'place',
        'received_by',
        'retrieved_date'
      ],
      decorates: {
        class: {
          customer_name: 'full_text',
          product: 'full_text',
          place: 'full_text',
          sales_name: 'full_text',
          send_date: 'full_text',
          sales_channel: 'full_text',
          duplicate_description: 'full_text'
        }
      }
    },

    filter_config: {
      id_no: {
        type: 'STRING',
        width: '200px'
      },
      customer_name: {
        type: 'STRING',
        width: '250px',
        alias: 'Name'
      },
      sales_code: {
        type: 'STRING',
        width: '250px',
        alias: 'Code'
      }
    }
  },

  CSR: {
    headers: {
      csr_code: 'CSR Code',
      name: 'Name',
      channel: 'Channel',
      branch_sip: 'Branch/Sip',
      count: 'Count'
    },

    options: {
      key: 'csr_code',
      autoNo: false,
      checker: true,
      fixed_row_page: true,
      page_size: [10],
      editables: ['count'],
      center: ['csr_code', 'name', 'channel', 'branch_sip', 'count']
    }
  }
};
