<template>
  <v-container class="app-wrapper" grid-list-md fluid pa-3>
    <v-form ref="formSearch">
      <v-card>
        <v-card-text class="no_padding_vert">
          <v-layout row wrap>
            <v-flex md2>
              <v-text-field v-model="conditionSearch.id_no" label="ID No" />
            </v-flex>
            <v-flex md2>
              <v-select
                v-model="DocumentSts.selected"
                :items="DocumentSts.items"
                label="Document Status"
              />
            </v-flex>
            <v-flex md2>
              <v-select
                v-model="baseOnStepData.selected"
                :items="baseOnStepData.items"
                label="Based On Step "
                @change="changeDataOnStepData"
              />
            </v-flex>
            <v-flex md2>
              <cd-date-picker
                v-model="conditionSearch.from_dt"
                :smaller="conditionSearch.to_dt"
                :custom="date_picker_config"
                label="From"
                icon="event"
                :readonly="readOnlyDate"
              />
            </v-flex>
            <v-flex md2>
              <cd-date-picker
                v-model="conditionSearch.to_dt"
                :greater="conditionSearch.from_dt"
                :custom="date_picker_config"
                label="To"
                icon="event"
                :readonly="readOnlyDate"
              />
            </v-flex>
            <v-flex md2 class="mt-3 text_left">
              <v-btn
                class="btn_ui"
                :loading="loadingUi.loadingSearch"
                :disabled="loadingUi.loadingSearch"
                @click="onSearch"
              >
                Search
              </v-btn>
              <v-btn class="btn_ui" @click="onExport">Export</v-btn>
            </v-flex>
          </v-layout>
        </v-card-text>
        <v-card-text>
          <v-layout row wrap>
            <v-flex
              md12
              class="subheading font-weight-medium primary_color"
              text-xs-left
            >
              TAT Base On Date
            </v-flex>
          </v-layout>

          <cd-perfect-table
            v-model="table_dataTatOnDate"
            :headers="headersTatOnDate"
            :custom="optionsheadersTatOnDate"
          />
        </v-card-text>

        <v-card-text>
          <v-layout row wrap>
            <v-flex
              md12
              class="subheading font-weight-medium primary_color"
              text-xs-left
            >
              TAT Base On Period
            </v-flex>
          </v-layout>
          <cd-perfect-table
            v-model="table_data_TatOnPeriod"
            :headers="headersTatOnPeriod"
            :custom="optionsheadersTatOnPeriod"
          />
        </v-card-text>
      </v-card>
    </v-form>
  </v-container>
</template>

<script>
import _ from 'lodash';
import moment from 'moment';
import { mapActions } from 'vuex';
import { Exporter } from '@chidoan/excel-utils';
import CONSTANT from './constant';

export default {
  data() {
    return {
      readOnlyDate: true,
      baseOnStepData: {
        selected: '',
        items: [
          {
            value: null,
            text: 'None'
          },
          {
            value: 'UPLOAD',
            text: 'Upload'
          },
          {
            value: 'RECEIVED',
            text: 'Received'
          },
          {
            value: 'PENDING',
            text: 'Pending'
          },
          {
            value: 'UND',
            text: 'UND'
          },
          {
            value: 'POS',
            text: 'POS'
          },
          {
            value: 'PDOC',
            text: 'PDOC'
          },
          {
            value: 'FINISH',
            text: 'Finish'
          }
        ]
      },
      moment,
      date_picker_config: {
        date_format: {
          model: 'YYYY-MM-DD',
          shown_up: 'DD-MM-YYYY'
        },
        clear: false
      },

      loadingUi: { loadingSearch: false },
      conditionSearch: {
        id_no: '',
        document_status: '',
        base_on_step: '',
        from: '',
        to: ''
      },
      table_dataTatOnDate: [],
      DocumentSts: {
        selected: '',
        items: [
          {
            text: 'All',
            value: null
          }
        ]
      },
      headersTatOnDate: {
        id_no: 'Id No',
        customer_nm: 'Customer Name',
        agree_id: 'AgreeID',
        product_nm: 'Product',
        sales_cd: 'Sales Code',
        data_entry: 'Data Entry',
        branch_sip: 'Branch/Sip',
        upload_dt: 'Upload Date',
        received_dt: 'Received Date',
        distributed_dt: 'Distributed Date',
        last_modified: 'Last Modifed',
        pending_dt: 'Pending Date',
        und_dt: 'UND Date',
        pdoc_dt: 'PDOC Date',
        finish_dt: 'Finish Date',
        document_sts: 'Document Status',
        f1_sts: 'F1 Status'
      },

      optionsheadersTatOnDate: {
        key: 'id_no',
        autoNo: true,
        checker: false,
        fixed_row_page: false,
        page_size: [10],
        center: [
          'id_no',
          'agree_id',
          'sales_cd',
          'data_entry',
          'upload_dt',
          'received_dt',
          'distributed_dt',
          'last_modified',
          'pending_dt',
          'und_dt',
          'pdoc_dt',
          'finish_dt',
          'document_sts',
          'f1_sts'
        ],

        decorates: {
          default: 'full_text',
          class: {
            upload_dt: 'text_right',
            received_dt: 'text_right',
            distributed_dt: 'text_right',
            last_modified: 'text_right',
            pending_dt: 'text_right',
            und_dt: 'text_right',
            pdoc_dt: 'text_right',
            finish_dt: 'text_right'
          }
        }
      },

      headersTatOnPeriod: {
        id_no: 'Id No',
        customer_nm: 'Customer Name',
        agree_id: 'AgreeID',
        product_nm: 'Product',
        sales_cd: 'Sales Code',
        data_entry: 'Data Entry Code',
        branch_sip: 'Branch/Sip',
        data_entry_sale: 'Data Entry-Sale',
        data_entry_csr: 'Data Entry-CSR',
        und_day: 'UND',
        pdoc_day: 'P.DOC',
        disbursement_day: 'Disbursement',
        days_from_sale: 'Days from Sale',
        days_from_csr: 'Days from CSR',
        days_from_modified: 'Days from Modified',
        document_sts: 'Document Status',
        f1_sts: 'F1 Status'
      },

      table_data_TatOnPeriod: [],

      optionsheadersTatOnPeriod: {
        key: 'id_no',
        autoNo: true,
        checker: false,
        fixed_row_page: false,
        page_size: [10],
        center: [
          'id_no',
          'agree_id',
          'sales_code',
          'data_entry_code',
          'branch_sip',
          'data_entry_sale',
          'data_entryCSR',
          'und',
          'P_doc',
          'disbursement',
          'days_from_sale',
          'days_from_cSR',
          'days_from_modified',
          'document_status:',
          'f1_tatus'
        ],
        decorates: {
          default: 'full_text'
        }
      }
    };
  },

  async created() {
    // this.conditionSearch.from_dt = moment(
    //   new Date().getTime() - 604800000
    // ).format('YYYY-MM-DD');
    // this.conditionSearch.to_dt = moment().format('YYYY-MM-DD');
    this.DocumentStsData();
  },

  methods: {
    ...mapActions('tatTracking', [
      'actionSearch',
      'getDocumentSts',
      'getDataBasicWithCondition'
    ]),

    async DocumentStsData() {
      let result = await this.getDocumentSts();
      this.DocumentSts.items = _.concat(
        this.DocumentSts.items,
        result.data.map(element => {
          return {
            text: element.status_nm,
            value: element.status_cd
          };
        })
      );
    },

    async onSearch() {
      this.setFeatureLoading(true);
      this.conditionSearch.base_on_step_data = this.baseOnStepData.selected;
      this.conditionSearch.document_sts = this.DocumentSts.selected;
      let result = await this.getDataBasicWithCondition(this.conditionSearch);
      this.table_dataTatOnDate = result.data.listTATDate;
      this.table_data_TatOnPeriod = result.data.listTATPeriod;
      this.setFeatureLoading(false);
    },
    changeDataOnStepData() {
      this.readOnlyDate = false;
      if (this.baseOnStepData.selected == null) {
        this.readOnlyDate = true;
        this.conditionSearch.to_dt = '';
        this.conditionSearch.from_dt = '';
      }
    },
    async onExport() {
      var exporter = new Exporter(CONSTANT.filename);
      exporter.addSheet(
        CONSTANT.headers,
        this.table_dataTatOnDate,
        'TAT Basic On Date'
      );
      exporter.addSheet(
        CONSTANT.headersPeriod,
        this.table_data_TatOnPeriod,
        'TAT Basic On Period'
      );
      var result = exporter.exportExcel();
      if (result.success) {
        // console.log("Exporte file is successful");
      } else {
        // console.error(result.message);
      }
    }
  }
};
</script>
