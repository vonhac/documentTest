export default {
  messages: {
    success: {
      delete: 'Deleted successfully',
      add: 'Saved successfully',
      edit: 'Edited successfully'
    },
    error: {
      delete: 'Delete failed',
      add: 'Save failed',
      edit: 'Edit failed',
      data: 'No data'
    }
  },

  alertsGroupDefer: {
    title: 'Do you want to delete this group defer?',
    delete: {
      message: 'Agree to delete this group defer [<b>{agree}</b>]',
      buttons: {
        yes: 'Continue',
        close: 'Close'
      }
    }
  },

  alertsDetailDefer: {
    title: 'Do you want to delete this detail defer?',
    delete: {
      message: 'Agree to delete this detail defer [<b>{agree}</b>]',
      buttons: {
        yes: 'Continue',
        close: 'Close'
      }
    }
  },

  headers: {
    detail_page: 'Detail Defer',
    group_page: 'Group Defer'
  },

  buttons: {
    add: 'Add new',
    search: 'Search'
  },

  labels: {
    group_code: 'Group Code',
    group_name: 'Group Name',
    group_defer: 'Group Defer',
    defer_code: 'Defer Code',
    defer_name: 'Defer Name'
  },

  tables: {
    detail: {
      id: 'Order',
      defer_cd: 'Defer Code',
      group_cd: 'Group Defer',
      defer_nm: 'Defer Name'
    },
    group: {
      id: 'Order',
      group_cd: 'Group Code',
      group_nm: 'Group Name'
    }
  }
};
