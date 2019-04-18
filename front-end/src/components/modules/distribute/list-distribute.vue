<template>
  <v-container class="app-wrapper" grid-list-md fluid pa-3>
    <v-form ref="formSearch">
      <v-layout row wrap>
        <v-flex xs12>
          <v-card>
            <v-card-text>
              <v-layout row wrap>
                <v-flex xs12>
                  <v-radio-group
                    v-model="conditionSearch.status"
                    row
                    class="no_margin_top"
                    @change="onSearch"
                  >
                    <v-radio label="Sending" :value="DOCUMENT_STATUS.SENDING" />
                    <v-radio
                      label="Received"
                      :value="DOCUMENT_STATUS.RECEIVED"
                    />
                    <v-radio
                      label="Cancel step Receive"
                      :value="DOCUMENT_STATUS.CANCEL_STEP_RECEIVED"
                    />
                  </v-radio-group>
                </v-flex>
              </v-layout>
              <v-layout row wrap style="margin-top: -20px">
                <v-flex md3>
                  <cd-date-picker
                    v-model="conditionSearch.date"
                    :custom="date_picker_config"
                    icon="event"
                    label="Date"
                  />
                </v-flex>
                <v-flex md1 text-xs-left mt-2>
                  <v-btn
                    color="primary"
                    :loading="loadingUi.loadingSearch"
                    :disabled="loadingUi.loadingSearch"
                    @click="onSearch"
                  >
                    Search
                  </v-btn>
                </v-flex>
                <v-flex md8 text-xs-right mt-2>
                  <span
                    style="margin-right:10px; color:red"
                    class="font-weight-medium"
                  >
                    Total : {{ result_data.choosing1.length }}
                  </span>
                  <v-btn
                    color="blue-grey darken-1"
                    :dark="conditionSearch.status == 'sending'"
                    :loading="loadingUi.bntReceive"
                    :disabled="
                      loadingUi.bntReceive == true ||
                        conditionSearch.status != DOCUMENT_STATUS.SENDING
                    "
                    @click="onReceive"
                  >
                    Receive
                  </v-btn>

                  <v-btn
                    color="primary"
                    :disabled="
                      loadingUi.bntReceive == true ||
                        conditionSearch.status != DOCUMENT_STATUS.RECEIVED
                    "
                    @click="openPoupDistribute"
                  >
                    Distribute
                  </v-btn>
                </v-flex>
              </v-layout>
            </v-card-text>
            <v-card-text class="no_padding_top">
              <custom-table
                v-model="table_data"
                :headers="table_document.headers"
                :custom="table_document.options"
                :selected.sync="result_data.choosing1"
              />
            </v-card-text>
            <v-dialog
              v-model="dialog"
              :scrollable="false"
              width="auto"
              content-class="overflow_hidden"
              lazy
            >
              <v-form ref="formCd" class="overflow_hidden">
                <v-card class="app-wrapper popup" flat>
                  <v-layout
                    align-center
                    fill-height
                    class="header white--text ui_title"
                  >
                    <v-flex md7>
                      <v-card-title class=" subheading white--text title">
                        CSR Distribution
                      </v-card-title>
                    </v-flex>
                    <v-flex md5>
                      <v-layout row align-center justify-end fill-height>
                        <v-btn flat icon @click="handleClose">
                          <v-icon color="white">close</v-icon>
                        </v-btn>
                      </v-layout>
                    </v-flex>
                  </v-layout>

                  <v-card-text class="body">
                    <v-layout row wrap>
                      <v-flex md12 class="text_right">
                        <span
                          style="margin-right:10px; color:red"
                          class="font-weight-medium"
                        >
                          Total Document: {{ result_data.choosing1.length }}
                        </span>
                        <span
                          style="margin-right:10px; color:#003399"
                          class="font-weight-medium"
                        >
                          Total CSR: {{ result_data.total2 }}
                        </span>
                        <v-btn
                          color="primary"
                          :loading="loadingUi.bntDistribute"
                          :disabled="loadingUi.bntDistribute"
                          @click="onDistribute"
                        >
                          Distribute
                        </v-btn>
                      </v-flex>
                    </v-layout>
                    <custom-table
                      v-model="table_data_distribution"
                      :headers="table_distribution.headers"
                      :custom="table_distribution.options"
                      :selected.sync="csr_selected"
                      @choosing="onChoosingCsr"
                    >
                      <template slot="editable" slot-scope="props">
                        <v-text-field
                          v-model.number="props.record[props.column]"
                          :disabled="props.record['disabled']"
                          class="count-editable"
                          @keypress="acceptDigit"
                        />
                      </template>
                    </custom-table>
                  </v-card-text>
                </v-card>
              </v-form>
            </v-dialog>
          </v-card>
        </v-flex>
      </v-layout>
    </v-form>
  </v-container>
</template>

<script>
import CustomTable from 'components/commons/table-hung-pham';
import { mapActions } from 'vuex';
import _ from 'lodash';
import moment from 'moment';
import { DOCUMENT_STATUS } from 'core';
import { STD_DATE_FORMAT } from 'core/constant';

export default {
  components: {
    CustomTable
  },

  data() {
    return {
      dialog: false,

      loadingUi: {
        loadingSearch: false,
        bntReceive: false,
        bntDistribute: false
      },

      conditionSearch: {
        status: '',
        date: null
      },
      csr_selected: [],

      result_data: {
        choosing1: [],
        total2: 0,
        choosingTableData: []
      },

      table_data: [],
      table_data_distribution: [],

      distribute_data_arr: [],
      document_data_arr: [],

      DOCUMENT_STATUS: DOCUMENT_STATUS,

      table_document: {
        headers: {
          key_index: 'key_index',
          id_no: 'Id No',
          customer_nm: 'Customer Name',
          product_nm: 'Product',
          created_by: 'Create',
          sales_cd: 'Sales Code',
          sales_nm: 'Sales Name',
          sales_chnl: 'Sales Channel',
          status_cd: 'Status',
          send_date: 'Send Date',
          branch_nm: 'Place',
          received_by: 'Received By',
          invalid_description: 'Duplicate Description',
          received_date: 'Received Date'
        },

        options: {
          key: 'key_index',
          autoNo: true,
          checker: true,
          initCheckbox: true,
          fixed_row_page: true,
          disabledLoadCombobox: true,
          getTableData: this.getDocumentData,
          page_size: [10, 20],
          checkDuplicate: (val, key, mapCheckbox, showAlert) => {
            var selectedRow = this.result_data.choosing1;
            if (val == false) {
              return true;
            }
            var checkItems = _.filter(this.table_data, function(o) {
              return o['key_index'] == key;
            });
            if (checkItems.length == 0) {
              return true;
            }
            var checkItem = checkItems[0];
            var selectedItems = _.filter(selectedRow, function(o) {
              return o['id_no'] == checkItem['id_no'];
            });
            if (selectedItems.length > 0) {
              if (showAlert) {
                this.showErrorMsg('Duplicate Id No');
              }
              return false;
            }
            return true;
          },
          hiddens: ['key_index'],
          center: [
            'id_no',
            'customer_nm',
            'product_nm',
            'created_by',
            'sales_cd',
            'sales_nm',
            'sales_chnl',
            'status_cd',
            'send_date',
            'branch_nm',
            'received_by',
            'invalid_description',
            'received_date'
          ],

          decorates: {
            text: {
              send_date: val => {
                return this.formatDate(val);
              },
              received_date: val => {
                return this.formatDate(val);
              }
            },
            class: {
              customer_nm: 'text_center full_text',
              product_nm: 'text_center full_text',
              branch_nm: 'text_center full_text',
              sales_nm: 'text_center full_text',
              send_date: 'text_center full_text',
              sales_chnl: 'text_center full_text',
              invalid_description: 'text_center full_text',
              received_date: 'text_center full_text'
            }
          }
        },

        filter_config: {
          id_no: {
            type: 'STRING',
            width: '100px'
          },
          customer_nm: {
            type: 'STRING',
            width: '250px',
            alias: 'Name'
          },
          sales_cd: {
            type: 'STRING',
            width: '250px',
            alias: 'Code'
          }
        }
      },
      date_picker_config: {
        date_format: {
          model: 'DD/MM/YYYY',
          shown_up: 'DD-MM-YYYY'
        },
        clear: true
      },
      table_distribution: {
        headers: {
          key_index: 'key_index',
          account_id: 'CSR Code',
          fullname: 'Name',
          sale_chnl: 'Channel',
          branch_nm: 'Branch/Sip',
          doc_no: 'Total Document in Month',
          count: 'Count'
        },

        options: {
          key: 'key_index',
          autoNo: true,
          checker: true,
          fixed_row_page: true,
          page_size: [10],
          disabledLoadCombobox: true,
          getTableData: this.getDistributeTableData,
          hiddens: ['key_index'],
          editables: ['count'],
          center: [
            'csr_code',
            'name',
            'sale_chnl',
            'branch_nm',
            'count',
            'doc_no'
          ]
        }
      }
    };
  },
  watch: {
    table_data_distribution: {
      handler() {
        this.countTotal2();
      },
      deep: true
    },

    csr_selected() {
      this.countTotal2();
      var data_table = this.distribute_data_arr;
      var csr_data = this.csr_selected;
      for (var i = 0; i < data_table.length; i++) {
        var item = data_table[i];
        item['disabled'] = true;
        for (var j = 0; j < csr_data.length; j++) {
          var selectItem = csr_data[j];
          if (selectItem['key_index'] == item['key_index']) {
            item['disabled'] = false;
            break;
          }
        }
      }
    },

    'result_data.choosing1': () => {}
  },

  async created() {
    //this.onSearch();
  },

  methods: {
    ...mapActions('distribute', [
      'actionSearch',
      'actionSearchCSR',
      'actionReceive',
      'searchDistributeDocument',
      'receivedDocument',
      'getCsrList',
      'distributeDocument'
    ]),
    ...mapActions('global', [
      'setFeatureLoading',
      'resetNotify',
      'showSuccessMsg',
      'showErrorMsg',
      'showWarningMsg'
    ]),
    async onSearch() {
      if (
        this.conditionSearch.status == null ||
        this.conditionSearch.status == ''
      ) {
        this.showErrorMsg('Please select status to search document');
        return;
      }
      this.setFeatureLoading(true);
      var result = await this.searchDistributeDocument({
        status_cd: this.conditionSearch.status,
        date_param: this.conditionSearch.date
      });
      if (result.success) {
        if (result.data == null) {
          result.data = [];
        }
        var arrData = result.data;
        arrData.map(item => {
          item['key_index'] = item['document_cd'].toString();
        });
        this.table_data = arrData;
      } else {
        this.showErrorMsg('Search error');
        this.setFeatureLoading(false);
      }
      this.setFeatureLoading(false);
    },

    async onSearchCSR() {
      var result = await this.actionSearchCSR();
      if (result.success) {
        if (result.data == null) {
          result.data = [];
        }
        this.table_data_distribution = result.data;
      } else {
        this.$notify.error(result.message);
      }
      this.csr_selected = [];
    },

    async onReceive() {
      // this.loadingUi.bntReceive = true;
      this.setFeatureLoading(true);
      var paramData = _.filter(this.result_data.choosing1, function(o) {
        return o['status_cd'] == DOCUMENT_STATUS.SENDING;
      });
      if (paramData.length == 0) {
        this.showErrorMsg('Missing data');
        return;
      }
      let resultImport = await this.receivedDocument(paramData);
      if (resultImport.success) {
        this.document_list = resultImport.data;
        this.showSuccessMsg('Received success');
      } else {
        this.showErrorMsg('Received got error');
      }
      // this.loadingUi.bntReceive = false;
      this.onSearch();
      this.setFeatureLoading(false);
    },

    async onDistribute() {
      this.setFeatureLoading(true);
      var totalDistribute = this.result_data.total2;
      var totalReceivedDocs = this.result_data.choosing1.length;

      if (this.csr_selected.length == 0) {
        this.showWarningMsg('please select csr to distribute document');
        this.loadingUi.bntDistribute = false;
        return;
      }
      if (totalDistribute > 0 && totalDistribute != totalReceivedDocs) {
        this.showWarningMsg('total select document not equal total distribute');
        this.loadingUi.bntDistribute = false;
        return;
      } else if (totalDistribute == 0) {
        var totalSelectCsr = this.csr_selected.length;
        if (totalReceivedDocs < totalSelectCsr) {
          this.showWarningMsg(
            'total select document less than total select csr'
          );
          this.setFeatureLoading(false);
          return;
        }
      }
      var paramData = {
        list_csr: this.csr_selected,
        list_document: this.result_data.choosing1,
        total_received: totalReceivedDocs,
        total_distribute: totalDistribute
      };
      let distributeResult = await this.distributeDocument(paramData);
      if (distributeResult.success) {
        this.showSuccessMsg('Distribute success');
        this.loadingUi.bntDistribute = false;
        this.dialog = false;
        this.onSearch();
      } else {
        this.showErrorMsg('Distribute got error');
      }
    },

    getDistributeTableData(data) {
      this.distribute_data_arr = data;
    },

    getDocumentTableData(data) {
      this.document_data_arr = data;
    },
    countTotal2() {
      this.result_data.total2 = 0;
      var distribute_arr = this.distribute_data_arr;
      this.csr_selected.forEach(item => {
        var row = _.find(distribute_arr, { key_index: item['key_index'] });
        let val = Number(row.count);
        if (_.isFinite(val)) {
          this.result_data.total2 += val;
        }
      });
    },

    handleClose() {
      this.dialog = false;
    },

    async openPoupDistribute() {
      var paramData = _.filter(this.result_data.choosing1, function(o) {
        return o['status_cd'] == DOCUMENT_STATUS.RECEIVED;
      });
      if (paramData.length == 0) {
        this.showErrorMsg('Missing data');
        return;
      }

      this.dialog = true;
      var result = await this.getCsrList();
      if (result.success) {
        var arrData = result.data;
        arrData.map(item => {
          item['key_index'] = item['account_id'];
          item['disabled'] = true;
        });
        this.table_data_distribution = arrData;
      } else {
        this.showErrorMsg('Search Error');
      }
      this.csr_selected = [];
    },

    onChoosingCsr(data, data_table) {
      this.csr_selected = data;
      for (var i = 0; i < data_table.length; i++) {
        var item = data_table[i];
        item['disabled'] = true;
        for (var j = 0; j < data.length; j++) {
          var selectItem = data[j];
          if (selectItem['key_index'] == item['key_index']) {
            item['disabled'] = true;
            break;
          }
        }
      }
      // this.csr_selected = data;
      // for (var i = 0; i < data.length; i++) {
      //   var item = data[i];
      //   item['disabled'] = false;
      // }

      // var uncheckCsr = _.filter(this.table_data_distribution, function(o) {
      //   for (var i = 0; i < data.length; i++) {
      //     var item = data[i];
      //     if (item['key_index'] == o['key_index']) {
      //       return false;
      //     }
      //   }
      //   return true;
      // });

      // for (var i = 0; i < uncheckCsr.length; i++) {
      //   var item = uncheckCsr[i];
      //   item['disabled'] = true;
      // }
    },

    acceptDigit(event) {
      if (event.keyCode >= 48 && event.keyCode <= 57) {
        return;
      }
      event.preventDefault();
    },

    formatDate(val) {
      if (val != '' && val != null) {
        var DATE_DEFAULT_FORMAT = {
          INPUT: STD_DATE_FORMAT,
          OUTPUT: 'DD/MM/YYYY hh:mm'
        };
        let dateVal = moment(val, DATE_DEFAULT_FORMAT.INPUT);
        if (dateVal.isValid()) {
          return dateVal.format(DATE_DEFAULT_FORMAT.OUTPUT);
        }
      }
    }
  }
};
</script>

<style scope>
.count-editable {
  padding: 0 !important;
}

.count-editable .v-text-field__details {
  display: none;
}

.count-editable input {
  text-align: center;
}
</style>
