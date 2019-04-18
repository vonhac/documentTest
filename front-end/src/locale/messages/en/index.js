const modules = {};
const requireModule = require.context('.', false, /\.js$/);
requireModule.keys().forEach(filename => {
  // Ignore this file
  if (filename.includes('index.js')) return;

  let moduleName = filename
    .replace(/(\.\/)(.+)(.js)/gi, '$2')
    .replace('-', '_');
  modules[moduleName] = requireModule(filename).default;
});

export default {
  ...modules,

  menu: {
    dashboard: 'Dashboard',
    personal: 'Personal',
    defer_rule: {
      parent: 'Defer Rule',
      group: 'Group Management',
      detail: 'Detail Defer Management'
    },
    configurations: 'Configurations',
    area_manager: 'Area Manager',
    tracking_info: 'Tracking Information',
    import_document: 'Import Document',
    distribute: 'Distribute',
    retrieval: 'Retrieval',
    bpo_check_doc: 'BPO check Document',
    tat_tracking: 'TAT Tracking'
  },

  page_headers: {
    dashboard: 'Dashboard',
    personal: 'Personal',
    defer_rule: {
      group: 'Group Defer Management',
      detail: 'Detail Defer Management'
    },
    configurations: 'Configuration parameters',
    area_manager: 'Manage Branch/Sip',
    tracking_info: 'Tracking Information',
    import_document: 'Document Of Sales Channel',
    distribute: 'Receive and Distribute Applications',
    retrieval: 'Manage Distributed and Retrieval Applications',
    bpo_check_doc: 'Note To Check BPO check Applications',
    tat_tracking: 'TAT Report'
  },

  global: {
    loading: 'Loading ...',

    labels: {
      logout: 'Sign out'
    },

    messages: {
      open_pdf_file: {
        success: 'Please follow new tab in your browser to view',
        error: 'Your browser reject to open new tab'
      },

      empty_data: 'No available data',
      unknown_error: 'A surprise error from browser',
      export_excel_success: 'Success to export file [{filename}]'
    },

    alert: {
      title: 'Alert',
      default_confirm: {
        title: 'Confirmation',
        message: 'Do you want to continue?',
        yes_btn: 'Continue',
        no_btn: 'No'
      }
    },

    buttons: {
      save: 'Save',
      add: 'Add',
      update: 'Update',
      remote: 'Delete',
      export: 'Export {type}'
    },

    table: {
      columns: {
        no: 'No.',
        action: 'Actions'
      },
      page_size: 'Rows: ',
      message: {
        no_data: 'NO DATA'
      },
      filter: {
        start_with: 'Start with',
        not_start_with: 'Not start with',
        end_with: 'End with',
        not_end_with: 'Not end with',
        includes: 'Includes',
        not_includes: 'Not includes',

        equals: 'Equals',
        not_equals: 'Not equals',
        less_than: 'Less than',
        less_than_equal: 'Less than or equals',
        greater_than: 'Greater than',
        greater_than_equal: 'Greater than or equals'
      },
      menu_context: {
        copy_cell: 'Copy selected CELL',
        copy_row: 'Copy selected ROW',
        select_row: 'Select row',
        unselect_row: 'Remove select row'
      }
    },

    validation: {
      require: '%{0} is required',
      min_length: '%{0} must be at least %{1} characters',
      max_length: '%{0} can contain up to %{1} characters'
    }
  },

  http_client: {
    errors: {
      '000': 'Can not connect to Server API',
      '1xx': 'Continuing process',
      '2xx': 'Request success',
      '3xx': 'Request redirection',
      '4xx': 'Unknown client error',
      '5xx': 'Unknown server error',

      304: 'Server API do not accept CORS from this address',
      403: 'Must be authenticate befor accessing this API',
      404: 'The request URL do not exist on server API',
      415: 'Server not support that method for URL',
      500: 'Has an error for this request. Refer admin to resolve'
    }
  },

  error_page: {
    401: 'OOPS! You must be authenticate befor accessing again',
    403: 'You don have permission on that page',
    404: 'The URL do not exist on server',
    500: 'Sorry something went wrong'
  }
};
