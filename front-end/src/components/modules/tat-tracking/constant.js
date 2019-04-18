export default {
  header: {
    id_no: {
      text: 'Id No',
      type: 'String',
      width: 200
    },
    customer_nm: {
      text: 'Customer Name',
      type: 'String',
      width: 200
    },
    agree_id: {
      text: 'AgreeID',
      type: 'String',
      width: 150
    },
    product_nm: {
      text: 'Product',
      type: 'String',
      width: 200
    },
    sales_cd: {
      text: 'Sales Code',
      type: 'String',
      width: 200
    },
    data_entry: {
      text: 'Data Entry',
      type: 'String',
      width: 150
    },
    branch_sip: {
      text: 'Branch/Sip',
      type: 'String',
      width: 200
    },
    upload_dt: {
      text: 'Upload Date',
      type: 'String',
      width: 200
    },
    received_dt: {
      text: 'Received Date',
      type: 'String',
      width: 150
    },
    distributed_dt: {
      text: 'Distributed Date',
      type: 'String',
      width: 200
    },
    last_modified: {
      text: 'Last modified',
      type: 'String',
      width: 200
    },
    pending_dt: {
      text: 'Pending Date',
      type: 'String',
      width: 150
    },
    und_dt: {
      text: 'UND Date',
      type: 'String',
      width: 200
    },
    pdoc_dt: {
      text: 'PDOC Date',
      type: 'String',
      width: 150
    },
    finish_dt: {
      text: 'Finish Date',
      type: 'String',
      width: 200
    },
    document_sts: {
      text: 'Document Status',
      type: 'String',
      width: 200
    },
    f1_sts: {
      text: 'F1 Status',
      type: 'String',
      width: 150
    }
  },
  headersPeriod: {
    id_no: {
      text: 'Id No',
      type: 'String',
      width: 200
    },
    customer_nm: {
      text: 'Customer Name',
      type: 'String',
      width: 200
    },
    agree_id: {
      text: 'AgreeID',
      type: 'String',
      width: 150
    },
    product_nm: {
      text: 'Product',
      type: 'String',
      width: 200
    },
    sales_cd: {
      text: 'Sales Code',
      type: 'String',
      width: 200
    },
    data_entry: {
      text: 'Data Entry',
      type: 'String',
      width: 150
    },
    branch_sip: {
      text: 'Branch/Sip',
      type: 'String',
      width: 200
    },
    data_entry_sale: {
      text: 'Data Entry-Sale',
      type: 'String',
      width: 200
    },
    data_entry_csr: {
      text: 'Data EntryCSR',
      type: 'String',
      width: 150
    },
    und_day: {
      text: 'UND',
      type: 'String',
      width: 200
    },
    pdoc_day: {
      text: 'P.DOC',
      type: 'String',
      width: 200
    },
    disbursement_day: {
      text: 'Disbursement',
      type: 'String',
      width: 150
    },
    days_from_sale: {
      text: 'Days from Sale',
      type: 'String',
      width: 200
    },
    days_from_csr: {
      text: 'Days from CSR',
      type: 'String',
      width: 150
    },
    days_from_modified: {
      text: 'Days from modified',
      type: 'String',
      width: 200
    },
    document_sts: {
      text: 'Document Status',
      type: 'String',
      width: 200
    },
    f1_sts: {
      text: 'F1 Status',
      type: 'String',
      width: 150
    }
  },
  filename: 'TAT Tracking',

  baseOnStepData: {
    selected: '',
    items: [
      {
        value: 'null',
        text: 'All'
      },
      {
        value: '1',
        text: 'Upload'
      },
      {
        value: '2',
        text: 'Received'
      },
      {
        value: '3',
        text: 'Pending'
      },
      {
        value: '4',
        text: 'UND'
      },
      {
        value: '5',
        text: 'POS'
      },
      {
        value: '6',
        text: 'PDOC'
      },
      {
        value: '7',
        text: 'Finish'
      }
    ]
  }
};
