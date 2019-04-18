export default {
  messages: {
    success: {
      add_defer_document: 'The data was insert successfully!'
    },
    errors: {
      missing_attachment: 'Please provide a valid document'
    },

    warnings: {
      cancel_step_pass: 'You cancelled the approving process',
      cancel_step_cancel: 'You cancelled the rejecting process',
      empty_condition: 'Please provide conditions at lease one before filtering'
    }
  },

  alerts: {
    title: 'Are you sure?',
    pass: {
      message: 'Approve for customer [<b>{agree}</b>]',
      buttons: {
        yes: 'Continue',
        close: 'Close'
      }
    },
    cancel: {
      message: 'Reject customer [<b>{agree}</b>]',
      buttons: {
        cancel: 'Cancel',
        close: 'Close'
      }
    },
    discard: {
      message: 'Defer notes will lost when you continue',
      buttons: {
        yes: 'Yes',
        no: 'No'
      }
    }
  },

  main_page: {
    table: {
      id_no: 'ID No',
      customer_nm: 'Customer Name',
      app_id: 'App ID',
      product_nm: 'Product Name',
      sales_cd: 'Sales Code',
      sales_nm: 'Sales Name',
      manager: 'Manager',
      sales_chnl: 'Sales Channel',
      distribute_csr: 'CSR code',
      send_date: 'Send Date',
      distributed_date: 'Distributed Date',
      created_by: 'Created By',
      branch_nm: 'Branch/SIP',
      status_cd: 'Status',
      bpo_check: 'Note BPO',
      description: 'Description'
    }
  },

  detail_page: {
    title: 'Information Detail',
    st_bpo_check_application: 'BPO Check Application',
    st_status: 'Current Status',
    st_history_status: 'Status History',
    st_deadline: 'Deadline:  ',

    buttons: {
      modifier: 'Modified',
      defer: 'Defer',
      pass: 'Pass',
      cancel: 'Cancel',
      matching: 'Matching'
    },

    model: {
      id_no: 'ID No',
      customer_nm: 'Customer Name',
      app_id: 'App ID',
      product_nm: 'Product Name',
      sales_cd: 'Sales Code',
      sales_nm: 'Sales Name',
      sales_chnl: 'Sales Channel',
      distribute_csr: 'Distributed CSR',
      retrieval_csr: 'Retrieval from CSR',
      created_by: 'Created By',
      received_by: 'Receive By',
      status_cd: 'Status',
      note_cancel: 'Note cancel',
      modified_content: 'Modified Content'
    },

    defer_popup: {
      title: 'Defer document',
      model: {
        group_cd: 'Defer Group',
        defer_cd: 'Defer Code',
        defer_nm: 'Defer Name',
        created_date: 'Created Date',
        description: 'Note'
      }
    },

    modified_popup: {
      title: 'Response Defer',
      attachment_file: 'Attachment',
      content: 'Modified Content'
    },

    cancel_popup: {
      title: ' will be change status to Cancel?',
      content: 'Cancel Content'
    },
    add_appID: {
      title: ' Do you want to save?',
      content: 'Add AppID'
    },

    table_defer: {
      id: 'Order',
      defer_cd: 'Defer code',
      group_cd: 'Group Defer',
      defer_nm: 'Defer Name',
      created_date: 'Created Date',
      description: 'Note'
    },

    table_history: {
      status_cd: 'Status',
      created_date: 'Date time'
    }
  }
};
