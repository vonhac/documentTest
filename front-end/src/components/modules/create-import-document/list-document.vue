<template>
  <v-container class="app-wrapper" grid-list-md fluid pa-3>
    <v-form ref="formSearch">
      <v-layout row wrap class="block">
        <v-flex xs12>
          <v-card>
            <v-card-text>
              <p class="sub-title no_margin">Search condition</p>
              <v-layout row wrap>
                <v-flex md2 mr-4>
                  <v-text-field
                    v-model="filter_conditions.idNo"
                    label="ID No"
                  />
                </v-flex>
                <v-flex md2 mr-4>
                  <cd-date-picker
                    v-model="filter_conditions.createdDate"
                    :custom="date_picker_config"
                    icon="event"
                    label="Create Date"
                  />
                </v-flex>
                <v-flex md2 mr-4>
                  <v-text-field
                    v-model="filter_conditions.sales_code"
                    label="Sale Code"
                  />
                </v-flex>
                <v-flex md2 mr-4>
                  <v-select
                    v-model="filter_conditions.status"
                    :items="combobox_data.status"
                    label="Status"
                    item-text="status_dmt_nm"
                    item-value="status_dmt_cd"
                  />
                </v-flex>
                <v-flex md2 class="mt-2 text_left">
                  <v-btn color="primary" @click="handleBtnSearchClick">
                    Search
                  </v-btn>
                </v-flex>
              </v-layout>
              <v-layout row wrap>
                <v-flex sm12 xs6 class="mt-2 text_left">
                  <v-btn color="primary" @click="handleBtnAddNewClick">
                    Add New
                  </v-btn>
                  <v-btn color="primary" @click="handleBtnImportDocumentClick">
                    Import Document
                  </v-btn>
                  <v-btn
                    :disabled="!hasSelected"
                    :dark="hasSelected"
                    :loading="waitting.checkvalid"
                    color="blue-grey darken-1"
                    @click="handleBtnCheckInvalidDocumentClick"
                  >
                    Check Document
                  </v-btn>
                  <v-btn
                    dark
                    color="blue-grey darken-1"
                    @click="exportExcelInvalid"
                  >
                    Export Invalid Document
                  </v-btn>
                  <v-btn
                    :disabled="!hasSelected"
                    color="primary"
                    @click="sendCSR"
                  >
                    Send CSR
                  </v-btn>
                </v-flex>
              </v-layout>
            </v-card-text>
            <v-card-text class="no_padding_top">
              <custom-table
                v-model="table_data"
                :headers="headers"
                :custom="options"
                :selected.sync="parameterPoup.checkData"
              />
            </v-card-text>
          </v-card>
        </v-flex>
      </v-layout>
      <export-invalid-document
        :show="showPopUpExportDocument"
        :parameter="parameterPoup"
        @close="handleClosePopupExportDocument"
      />
      <import-document
        :show="showPopUpImportDocument"
        :parameter="parameterPoup"
        @close="handleClosePopupImportDocument"
      />
      <add-edit-document
        :show="showPopUpAdd"
        :parameter="parameterPoup"
        @close="handleClosePopupAdd"
      />
      <add-edit-document
        :show="showPopUpEdit"
        :parameter="parameterPoup"
        @close="handleClosePopupEdit"
      />
    </v-form>

    <v-dialog v-model="sendCSRDialog" width="500" persistent lazy>
      <v-container fluid grid-list-md class="no_padding">
        <v-card>
          <v-card-text class="confirm_title primary_color">
            <v-icon color="primary" medium>{{ $vuetify.icons.INFO }}</v-icon>
            Notification
          </v-card-text>
          <v-card-text>
            <v-layout row wrap>
              <v-flex xs12 class="confirm_text">
                {{
                  isMissingPlace
                    ? 'Please choose a place before sending'
                    : 'Do you want to send to CSR/ Y/N'
                }}
              </v-flex>
              <v-flex v-if="isMissingPlace" xs12>
                <v-autocomplete
                  v-model="filter_conditions.place"
                  :items="combobox_data.place"
                  label="Branch/Sip"
                  item-text="branch_nm"
                  item-value="branch_id"
                />
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex text-xs-right>
                <v-btn small color="primary" @click="sendToCSR">Continue</v-btn>
                <v-btn
                  small
                  color="blue-grey darken-1"
                  flat
                  @click="sendCSRDialog = false"
                >
                  Cancel
                </v-btn>
              </v-flex>
            </v-layout>
          </v-card-text>
        </v-card>
      </v-container>
    </v-dialog>
  </v-container>
</template>

<script>
import { Exporter } from '@chidoan/excel-utils';
import ExportInvalidDocument from './export-invalid-document';
import ImportDocument from './import-document';
import AddEditDocument from './add-edit-document';
import CustomTable from 'components/commons/table-hung-pham';
import { mapActions, mapGetters } from 'vuex';
import moment from 'moment';
import _ from 'lodash';
import { DOCUMENT_STATUS } from 'core';
import { STD_DATE_FORMAT } from 'core/constant';

export default {
  components: {
    CustomTable,
    ExportInvalidDocument,
    ImportDocument,
    AddEditDocument
  },
  data() {
    return {
      sendCSRDialog: false,
      csrData: [],
      waitting: {
        checkvalid: false
      },

      searchData: {},
      combobox_data: {
        status: [
          {
            status_dmt_nm: '- All -',
            status_dmt_cd: null
          }
        ],
        place: [],
        productlist: []
      },
      filter_conditions: {
        status: {
          status_dmt_nm: '- All -',
          status_dmt_cd: null
        },
        sales_code: null,
        place: null,
        productlist: {}
      },
      dialog_modified: false,
      showPopUpExportDocument: false,
      showPopUpImportDocument: false,
      showPopUpRemove: false,
      showPopUpAdd: false,
      showPopUpEdit: false,
      parameterPoup: {
        title: '',
        nameBnt: '',
        iconName: '',
        mode: '',
        data: {},
        checkData: [],
        checkInvalidDocument: []
      },
      table_data: [],
      tableData: [],
      document_list: [],

      date_picker_config: {
        date_format: {
          model: 'MM/DD/YYYY',
          shown_up: 'DD-MM-YYYY'
        },
        clear: true
      },

      headers: {
        key: '',
        id_no: 'Id No',
        customer_nm: 'Customer Name',
        product_nm: 'Product Name',
        sales_cd: 'Sales Code',
        sales_nm: 'Sales Name',
        sales_chnl: 'Sales Channel',
        status_cd: 'Status',
        invalid_description: 'InValid Description',
        branch_nm: 'Send to Place',
        created_date: 'Create Date',
        created_by: 'Create By'
      },

      options: {
        key: 'document_cd',
        autoNo: true,
        checker: true,
        fixed_row_page: true,
        page_size: [10],
        default_sort: {
          column: 'key',
          desc: true
        },
        hiddens: ['key'],

        center: [
          'document_cd',
          'id_no',
          'customer_nm',
          'product_nm',
          'sales_cd',
          'sales_nm',
          'sales_chnl',
          'status_cd',
          'invalid_description',
          'branch_nm',
          'created_date',
          'created_by'
        ],
        actions: {
          edit: {
            color: 'warning',
            icon: this.$vuetify.icons.UPDATE,
            scope: 'record',
            callback: (val, record) => {
              this.showPopUpEdit = true;
              this.parameterPoup.mode = 'edit';
              this.parameterPoup.data = record;
              this.parameterPoup.data.productlist = this.combobox_data.productlist;
              this.parameterPoup.data.placelist = this.combobox_data.place;
            },
            isHidden: function(key, item) {
              if (item['status_cd'] != DOCUMENT_STATUS.ORIGINAL) {
                return true;
              }
              return false;
            }
          },

          remove: {
            icon: this.$vuetify.icons.DELETE,
            scope: 'record',
            callback: this.remove
          }
        },

        decorates: {
          text: {
            created_date: val => {
              if (val != '' && val != null) {
                var DATE_DEFAULT_FORMAT = {
                  INPUT: STD_DATE_FORMAT,
                  OUTPUT: 'MM/DD/YYYY hh:mm'
                };
                let dateVal = moment(val, DATE_DEFAULT_FORMAT.INPUT);
                if (dateVal.isValid()) {
                  return dateVal.format(DATE_DEFAULT_FORMAT.OUTPUT);
                }
              }
            }
          },

          class: {
            id_no: 'text_center full_text',
            customer_nm: 'text_left full_text',
            product_nm: 'text_left full_text',
            sales_cd: 'text_center full_text',
            sales_nm: 'text_left full_text',
            sales_chnl: 'text_left full_text',
            status_cd: 'text_center full_text',
            invalid_description: 'text_left full_text',
            branch_nm: 'text_left full_text',
            created_date: 'text_right full_text',
            created_by: 'text_left full_text'
          }
        }
      }
    };
  },
  computed: {
    ...mapGetters('authentication', ['authUser']),

    hasSelected() {
      return (
        _.isArray(this.parameterPoup.checkData) &&
        !_.isEmpty(this.parameterPoup.checkData)
      );
    },

    isMissingPlace() {
      if (this.authUser.csr) return false;

      let missingPlace = _.filter(
        this.parameterPoup.checkData,
        row => row.branch_id == '' || row.branch_id == null
      );
      return missingPlace.length > 0;
    }
  },

  created() {
    this.pageSetup();
  },

  methods: {
    ...mapActions('createImportDocument', [
      'searchDocument',
      'loadcombobox',
      'deleteDocument',
      'sendCSRDocument',
      'checkInvalidDocument'
    ]),
    ...mapGetters('global', ['notifications']),
    async pageSetup() {
      var result = await this.loadcombobox();
      if (result.success) {
        if (!_.isNil(result.data.list_status)) {
          this.combobox_data.status = _.concat(
            this.combobox_data.status,
            result.data.list_status
          );
        }
        this.filter_conditions.created_date = null;
        this.filter_conditions.idNo = null;
        this.filter_conditions.status = null;
        this.filter_conditions.sales_code = null;
        if (!_.isNil(result.data.list_branch)) {
          this.combobox_data.place = _.concat(
            [
              {
                branch_nm: '',
                branch_id: null
              }
            ],
            result.data.list_branch
          );
        }
        this.combobox_data.productlist = result.data.list_product;
      }
    },
    async handleBtnCheckInvalidDocumentClick() {
      this.waitting.checkvalid = true;
      let invalidData = _.filter(
        this.parameterPoup.checkData,
        row => row.status_cd == DOCUMENT_STATUS.ORIGINAL
      );
      if (invalidData.length == 0) {
        this.$notify.warning('Please select document with ORIGINAL status');
      } else {
        let resultImport = await this.checkInvalidDocument(invalidData);
        if (resultImport.success) {
          this.document_list = resultImport.data;
          this.$notify.success('Check invalid success');
          this.handleBtnSearchClick();
        } else {
          this.$notify.error('Check invalid got error');
        }
      }
      this.waitting.checkvalid = false;
    },

    exportExcelInvalid() {
      let exporter = new Exporter('LIST_INVALID_DOCUMENT');
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#1f4e78'
      });
      let headers = {
        id_no: {
          text: 'Id No',
          type: 'String',
          width: 80
        },
        customer_nm: {
          text: 'Customer Name',
          type: 'String',
          width: 160
        },
        product_nm: {
          text: 'Product Name',
          type: 'String',
          width: 160
        },
        sales_cd: {
          text: 'Sales Code',
          type: 'String',
          width: 160
        },
        sales_nm: {
          text: 'Sales Name',
          type: 'String',
          width: 160
        },
        sales_chnl: {
          text: 'Sales Channel',
          type: 'String',
          width: 160
        },
        status_cd: {
          text: 'Status',
          type: 'String',
          width: 160
        },
        invalid_description: {
          text: 'Invalid Description',
          type: 'String',
          width: 160
        },
        branch_id: {
          text: 'Branch Id',
          type: 'String',
          width: 160
        }
      };

      var arrData = this.table_data;
      var updateData = _.map(this.table_data, function(o) {
        var item = _.filter(arrData, { document_cd: o.document_cd });
        if (
          item != undefined &&
          item.length > 0 &&
          (item[0]['status_cd'] == DOCUMENT_STATUS.INVALID ||
            item[0]['status_cd'] == DOCUMENT_STATUS.VALID)
        ) {
          item[0]['key_index'] = item[0]['document_cd'].toString();
          return item[0];
        }
        return o;
      });
      var invalidData = _.filter(updateData, function(o) {
        return o['status_cd'] == DOCUMENT_STATUS.INVALID;
      });

      exporter.addSheet(headers, invalidData, 'import');
      if (_.size(invalidData) == 0) {
        this.$notify.warning('No INVALID document on table');
        return;
      }
      let result = exporter.exportExcel();
      if (result.success) {
        this.$notify.success('Export success');
      } else {
        this.$notify.error('Export got error');
      }
    },

    handleClosePopupExportDocument(result) {
      var arrData = result.data;
      var updateData = _.map(this.table_data, function(o) {
        var item = _.filter(arrData, { ID: o.ID });
        if (
          item != undefined &&
          item.length > 0 &&
          (item[0]['status_cd'] == DOCUMENT_STATUS.INVALID ||
            item[0]['status_cd'] == DOCUMENT_STATUS.VALID)
        ) {
          item[0]['key_index'] = item[0]['id'].toString();
          return item[0];
        }
        return o;
      });
      this.table_data = [];
      this.table_data = updateData;
      this.showPopUpExportDocument = false;
    },

    handleBtnImportDocumentClick() {
      this.showPopUpImportDocument = true;
    },

    handleClosePopupImportDocument(result) {
      this.showPopUpImportDocument = false;
      if (result != undefined && result.statusSave) {
        this.handleBtnSearchClick();
      }
    },

    handleBtnAddNewClick() {
      this.showPopUpAdd = true;
      this.parameterPoup.mode = 'add';
      this.parameterPoup.data = {};
      this.parameterPoup.data.isPartner = false;
      this.parameterPoup.data.productlist = this.combobox_data.productlist;
      this.parameterPoup.data.placelist = this.combobox_data.place;
    },

    async handleBtnSearchClick() {
      this.setFeatureLoading(true);
      this.parameterPoup.checkData = [];
      if (this.filter_conditions.status == null) {
        this.searchData.status_cd = null;
      } else {
        this.searchData.status_cd = this.filter_conditions.status;
      }
      this.searchData.sales_cd = this.filter_conditions.sales_code;
      this.searchData.id_no = this.filter_conditions.idNo;
      this.searchData.created_date_search = this.filter_conditions.createdDate;
      var result = await this.searchDocument(this.searchData);
      if (result.success) {
        var arrData = result.data;
        arrData.map(item => {
          item['key_index'] = item['document_cd'].toString();
        });
        this.table_data = result.data;
      } else {
        this.$notify.error('Search Error');
      }
      this.setFeatureLoading(false);
    },

    handleClosePopupAdd(result) {
      this.showPopUpAdd = false;
      if (result != undefined && result.statusSave) {
        this.handleBtnSearchClick();
      }
    },

    handleClosePopupEdit(result) {
      this.showPopUpEdit = false;
      if (result != undefined && result.statusSave) {
        this.handleBtnSearchClick();
      }
    },

    async handleClosePopupRemove() {
      let result = await this.deleteDocument(this.rowRemove.document_cd);
      if (result.success) {
        this.$notify.success('Delete success');
        this.handleBtnSearchClick();
      } else {
        this.$notify.success('Delete fail');
      }
    },

    remove(val, record) {
      this.rowRemove = record;
      var self = this;
      this.$alert
        .info()
        .setTitle('Notification')
        .setMessage('Are you sure you want to delete ?')
        .setControls({
          true: {
            text: 'global.alert.default_confirm.yes_btn',
            color: 'primary',
            style: 'normal',
            dark: true
          },
          false: {
            text: 'global.alert.default_confirm.no_btn',
            color: 'info'
          }
        })
        .setCallback(val => {
          if (val == 'true') {
            self.handleClosePopupRemove();
          }
        })
        .open();
    },

    sendCSR() {
      this.csrData = _.filter(
        this.parameterPoup.checkData,
        row => row.status_cd == DOCUMENT_STATUS.VALID
      );
      if (this.csrData == null || this.csrData.length == 0) {
        this.$notify.warning(
          'Please select document with VALID status at least'
        );
        return;
      }

      let { branch_id = null } = _.head(this.combobox_data.place) || {};
      this.filter_conditions.place = branch_id;
      this.sendCSRDialog = true;
    },

    async sendToCSR() {
      if (this.isMissingPlace && _.isNil(this.filter_conditions.place)) {
        this.$notify.warning('Please select a place before sending');
        return;
      }

      if (this.authUser.csr)
        this.filter_conditions.place = this.authUser.branch_id;

      this.setAppLoading(true);
      let result = await this.sendCSRDocument({
        send_to_place: this.filter_conditions.place,
        models: this.csrData
      });

      this.setAppLoading();
      this.sendCSRDialog = false;
      if (result.success) {
        this.$notify.success('Send CSR success');
        this.handleBtnSearchClick();
      } else {
        this.$notify.error(result.message);
      }
    },

    addAllOption(arr) {
      var arrOptions = _.cloneDeep(arr);
      arrOptions.unshift({ id: 'all', text: 'All' });
      return arrOptions;
    }
  }
};
</script>

<style scope>
.style_note {
  margin-top: 20px;
  text-align: right;
}

.confirm_title {
  text-align: left;
  color: #444;
  font-size: 22px;
  background-color: #e1e1e1;
}

.confirm_text {
  line-height: 40px !important;
  color: #999;
  text-align: left;
  font-size: 18px;
}
</style>
