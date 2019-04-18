<template>
  <v-container class="app-wrapper" grid-list-md fluid pa-3>
    <v-form ref="formSearch">
      <v-layout row wrap>
        <v-flex xs12>
          <v-card>
            <v-card-text>
              <p class="sub-title no_margin">Search condition</p>
              <v-layout row wrap>
                <v-flex md4 xs12>
                  <v-select
                    v-model="conditionSearch.distributeCsrModel.selected"
                    :items="conditionSearch.csrDistributeList.items"
                    label="CSR Distribution"
                    item-text="fullData"
                    item-value="account_id"
                  />
                </v-flex>
                <v-flex md3 xs12>
                  <v-select
                    v-model="conditionSearch.statusModel.selected"
                    :items="conditionSearch.status_list"
                    label="Status"
                    item-text="status_dmt_nm"
                    item-value="status_dmt_cd"
                  />
                </v-flex>
                <v-flex md3 xs8>
                  <cd-date-picker
                    v-model="conditionSearch.distributeDate"
                    :custom="date_picker_config"
                    icon="event"
                    label="Distribute date"
                  />
                </v-flex>
                <v-flex md2 xs4 text-xs-right text-md-left mt-2>
                  <v-btn
                    color="primary"
                    :loading="loadingUi.loadingSearch"
                    :disabled="loadingUi.loadingSearch"
                    @click="onSearch"
                  >
                    Search
                  </v-btn>
                </v-flex>
              </v-layout>
            </v-card-text>
            <v-card-text class="no_padding_top">
              <p class="sub-title no_margin">Retrieval</p>
              <v-layout row wrap>
                <v-flex xs8 md4>
                  <v-select
                    v-model="saveForm.cb_retrieval.selected"
                    :items="computedListCsrRetrival"
                    label="CSR Retrieval"
                    item-text="fullData"
                    item-value="account_id"
                  />
                </v-flex>

                <v-flex xs4 text-xs-right md8 text-md-left mt-2>
                  <v-btn
                    color="primary"
                    :loading="loadingUi.loadingRetrieve"
                    :disabled="loadingUi.loadingRetrieve"
                    @click="onRetrieve"
                  >
                    Retrieve
                  </v-btn>
                </v-flex>
              </v-layout>
            </v-card-text>
            <v-card-text class="no_padding_top">
              <v-layout row wrap text-xs-left>
                <v-flex md12>
                  <span style=" color:red" class="font-weight-medium">
                    Total Choosing:
                    {{ this.saveForm.listDocumentRetrieved.length }}
                  </span>
                </v-flex>
              </v-layout>
              <custom-table
                v-model="table_data"
                :headers="headers"
                :custom="options"
                :selected.sync="saveForm.listDocumentRetrieved"
                @choosing="onChoosing"
              />
            </v-card-text>
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
import { DOCUMENT_STATUS } from 'core';
export default {
  components: {
    CustomTable
  },

  data() {
    return {
      loadingUi: { loadingSearch: false, loadingRetrieve: false },
      searchConditions: {
        distribute_csr: '',
        distribute_date: '',
        status_cd: ''
      },
      conditionSearch: {
        csrDistributeList: [],
        distributeCsrModel: {
          selected: null,
          items: []
        },
        distributeCsr: '',
        statusList: [],
        statusModel: {
          selected: null,
          items: []
        },
        status: '',
        distributeDate: null
      },
      saveForm: {
        cb_retrieval: {
          selected: null,
          items: []
        },
        listDocumentRetrieved: []
      },

      DOCUMENT_STATUS: DOCUMENT_STATUS,

      table_data: [],
      date_picker_config: {
        date_format: {
          model: 'DD/MM/YYYY',
          shown_up: 'DD-MM-YYYY'
        },
        clear: true
      },

      headers: {
        key_index: 'key_index',
        id_no: 'Id No',
        customer_nm: 'Customer Name',
        product_nm: 'Product',
        sales_cd: 'Sales Code',
        sales_nm: 'Sales Name',
        sales_chnl: 'Sales Channel',
        distribute_csr: 'CSR Distributior',
        distributed_date: 'Distributed Date',
        retrieval_csr: 'Retrieved from CSR',
        status_date_current: 'Retrieved Date',
        status_cd: 'Status'
      },

      options: {
        key: 'key_index',
        autoNo: true,
        checker: true,
        fixed_row_page: true,
        page_size: [8],
        hiddens: ['key_index'],
        center: [
          'id_no',
          'customer_nm',
          'product_nm',
          'sales_cd',
          'sales_nm',
          'sales_chnl',
          'distribute_csr',
          'distributed_date',
          'retrieval_csr',
          'status_date_current',
          'status_cd'
        ],
        decorates: {
          text: {},
          class: {
            id_no: 'text_center full_text',
            customer_nm: 'text_left full_text',
            product_nm: 'text_left full_text',
            sales_cd: 'text_center full_text',
            sales_nm: 'text_left full_text',
            sales_chnl: 'text_left full_text',
            distribute_csr: 'text_left full_text',
            distributed_date: 'text_right full_text',
            retrieval_csr: 'text_left full_text',
            status_date_current: 'text_right full_text',
            status_cd: 'text_center full_text'
          }
        }
      }
    };
  },

  computed: {
    computedListCsrRetrival() {
      if (_.isArray(this.saveForm.cb_retrieval.items)) {
        return this.saveForm.cb_retrieval.items.filter(
          el =>
            el.account_id != this.conditionSearch.distributeCsrModel.selected
        );
      }
      return [];
    }
  },
  async created() {
    this.pageSetup();
    // this.onSearch();
  },

  methods: {
    ...mapActions('global', [
      'setFeatureLoading',
      'resetNotify',
      'showSuccessMsg',
      'showErrorMsg',
      'showWarningMsg'
    ]),
    ...mapActions('retrieval', [
      'getDocument',
      'actionRetrieve',
      'searchRetrieval',
      'loadcombobox',
      'retrievalDocument'
    ]),
    async pageSetup() {
      var result = await this.loadcombobox();
      if (result.success) {
        result.data.list_csr = result.data.list_csr.map(row => {
          return {
            ...row,
            ...{
              fullData: `${row.account_id} - ${row.fullname}`
            }
          };
        });
        this.saveForm.cb_retrieval.items = _.cloneDeep(result.data.list_csr);
        this.conditionSearch.status_list = this.addAllOption(
          result.data.list_status
        );
        this.conditionSearch.csrDistributeList.items = this.addAllOption1(
          result.data.list_csr
        );
      }
    },
    async onSearch() {
      this.setFeatureLoading(true);

      if (this.conditionSearch.distributeCsrModel == null) {
        this.searchConditions.distribute_csr = '';
      } else {
        this.searchConditions.distribute_csr = this.conditionSearch.distributeCsrModel.selected;
      }
      if (this.conditionSearch.statusModel == null) {
        this.searchConditions.status_cd = '';
      } else {
        this.searchConditions.status_cd = this.conditionSearch.statusModel.selected;
      }
      this.searchConditions.distribute_date = this.conditionSearch.distributeDate;
      var result = await this.searchRetrieval(this.searchConditions);

      if (result.success) {
        var arrData = result.data;
        arrData.map(item => {
          item['key_index'] = item['document_cd'].toString();
        });
        this.table_data = arrData;
      } else {
        this.showErrorMsg('Search Error');
      }
      this.setFeatureLoading();
    },

    async onRetrieve() {
      var listRetrieved = this.saveForm.listDocumentRetrieved;
      if (listRetrieved.length == 0) {
        this.showErrorMsg('Please select document to retrieve');
        return;
      }

      var selectRetrieval = this.saveForm.cb_retrieval.selected;

      if (selectRetrieval == null) {
        this.showErrorMsg('Please select CSR Retrieval');
        return;
      }

      selectRetrieval = this.saveForm.cb_retrieval.selected;
      var duplicateRetrieval = _.filter(listRetrieved, function(o) {
        return o['retrieval_csr'] == selectRetrieval;
      });

      if (duplicateRetrieval.length > 0) {
        this.showErrorMsg('Duplicate CSR Retrieval');
        return;
      }

      var result = await this.retrievalDocument({
        csrId: selectRetrieval,
        data: listRetrieved
      });
      if (result.success) {
        this.showSuccessMsg('Retrieval document success');
        this.onSearch();
      } else {
        this.showErrorMsg('Retrieval document fail');
      }
    },
    onChoosing(param) {
      this.saveForm.listDocumentRetrieved = param;
    },
    addAllOption(arr) {
      var arrOptions = _.cloneDeep(arr);
      arrOptions.unshift({ status_dmt_cd: '', status_dmt_nm: 'All' });
      return arrOptions;
    },
    addAllOption1(arr) {
      var arrOptions = _.cloneDeep(arr);
      arrOptions.unshift({ account_id: '', fullData: 'All' });
      return arrOptions;
    }
  }
};
</script>

<style scope></style>
