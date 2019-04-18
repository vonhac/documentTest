<template>
  <v-container
    :class="['app-wrapper', isMobile ? 'no_padding' : 'pa-3']"
    grid-list-md
    fluid
  >
    <v-form ref="formSearch">
      <v-layout :class="['block', isMobile ? 'no_padding' : '']" row wrap>
        <v-flex :class="[isMobile ? 'no_padding' : '']" xs12>
          <v-card>
            <v-card-text>
              <p class="sub-title no_margin">Search condition</p>
              <v-layout row wrap>
                <v-flex md3>
                  <v-text-field
                    v-model="filter_conditions.id_no"
                    label="ID No"
                    clearable
                  />
                </v-flex>
                <v-flex md3>
                  <v-text-field
                    v-model="filter_conditions.sale_cd"
                    label="Sale code"
                    clearable
                  />
                </v-flex>
                <v-flex md3>
                  <cd-date-picker
                    v-model="filter_conditions.sending_date"
                    :custom="date_picker_config"
                    icon="event"
                    label="Sending Date"
                  />
                </v-flex>
                <v-flex md3>
                  <cd-date-picker
                    v-model="filter_conditions.distribute_date"
                    :custom="date_picker_config"
                    icon="event"
                    label="Distribute Date"
                  />
                </v-flex>
              </v-layout>
              <v-layout row wrap>
                <v-flex md3>
                  <v-select
                    v-model="filter_conditions.channel"
                    label="Sale Channel"
                    :items="combobox_data.channels"
                    item-text="department_nm"
                    item-value="department_cd"
                  />
                </v-flex>
                <v-flex md3>
                  <v-select
                    v-model="filter_conditions.status"
                    label="Status"
                    :items="combobox_data.status"
                    item-text="status_nm"
                    item-value="status_cd"
                  />
                </v-flex>
                <v-flex md3>
                  <v-select
                    v-model="filter_conditions.BPO_checking"
                    label="BPO check Y/N"
                    :items="combobox_data.BPO_checking"
                  />
                </v-flex>
                <v-flex md3 class="mt-2 text_left">
                  <v-btn
                    :loading="waitting.filter"
                    :disabled="waitting.filter"
                    color="primary"
                    @click="filter"
                  >
                    Search
                  </v-btn>
                  <v-btn
                    :loading="waitting.export"
                    :disabled="waitting.export"
                    color="blue-grey darken-1"
                    dark
                    @click="exportExcel"
                  >
                    Export Excel
                  </v-btn>
                </v-flex>
              </v-layout>
            </v-card-text>
            <v-card-text class="no_padding_top">
              <p class="sub-title">Document informations</p>
              <cd-perfect-table
                v-model="table_data"
                :headers="headers"
                :custom="options"
                :filter="filter_config"
              >
                <template slot="editable" slot-scope="props">
                  <v-checkbox
                    v-model="props.record[props.column]"
                    color="primary"
                    readonly
                  />
                </template>
              </cd-perfect-table>
            </v-card-text>
          </v-card>
        </v-flex>
      </v-layout>
      <information-detail
        v-model="detail_dialog.open"
        :parameter="detail_dialog.model"
        @reload="reloadPage"
      />
    </v-form>
  </v-container>
</template>

<script>
import _ from 'lodash';
import moment from 'moment';
import { mapGetters, mapActions } from 'vuex';
import { DATE_FORMATER } from 'core/mixins';
import { EXCEL_FILENAMES } from 'core/constant';
import { Exporter } from '@chidoan/excel-utils';
import InformationDetail from './information-detail';

const DEFAULT_CONDITIONS = {
  id_no: null,
  channel: null,
  status: null,
  sale_cd: '',
  distribute_date: null,
  sending_date: null
};
export default {
  components: {
    InformationDetail
  },

  mixins: [DATE_FORMATER],

  data() {
    return {
      acv: 'male',

      filter_conditions: _.cloneDeep(DEFAULT_CONDITIONS),

      combobox_data: {
        channels: [
          {
            department_nm: '- All -',
            department_cd: null
          }
        ],
        status: [
          {
            status_nm: '- All -',
            status_cd: null
          }
        ],
        BPO_checking: [
          {
            text: '- All -',
            value: null
          },
          {
            text: 'No',
            value: false
          },
          {
            text: 'Yes',
            value: true
          }
        ]
      },

      detail_dialog: {
        open: false,
        model: {}
      },

      table_data: [],

      headers: {
        document_cd: '',
        id_no: 'tracking_information.main_page.table.id_no',
        customer_nm: 'tracking_information.main_page.table.customer_nm',
        app_id: 'tracking_information.main_page.table.app_id',
        product_nm: 'tracking_information.main_page.table.product_nm',
        sales_cd: 'tracking_information.main_page.table.sales_cd',
        sales_nm: 'tracking_information.main_page.table.sales_nm',
        manager: 'tracking_information.main_page.table.manager',
        sales_chnl: 'tracking_information.main_page.table.sales_chnl',
        distribute_csr: 'tracking_information.main_page.table.distribute_csr',
        send_date: 'tracking_information.main_page.table.send_date',
        distributed_date:
          'tracking_information.main_page.table.distributed_date',
        created_by: 'tracking_information.main_page.table.created_by',
        branch_nm: 'tracking_information.main_page.table.branch_nm',
        status_nm: 'tracking_information.main_page.table.status_cd',
        bpo_check: 'tracking_information.main_page.table.bpo_check',
        description: 'tracking_information.main_page.table.description'
      },

      filter_config: {
        customer_nm: {
          type: 'STRING',
          width: '200px',
          alias: ''
        },
        product_nm: {
          type: 'STRING',
          width: '200px',
          alias: ''
        },
        manager: {
          type: 'STRING',
          width: '100px',
          alias: ''
        },
        created_by: {
          type: 'STRING',
          width: '100px',
          alias: ''
        },
        branch_nm: {
          type: 'STRING',
          width: '150px',
          alias: ''
        },
        status_nm: {
          type: 'STRING',
          width: '100px',
          alias: ''
        }
      },

      options: {
        key: 'document_cd',
        fixed_row_page: true,
        page_size: [10],
        editables: ['note_bpo'],
        default_sort: {
          column: 'document_cd',
          desc: true
        },
        hiddens: ['document_cd'],
        center: [
          'document_cd',
          'id_no',
          'customer_nm',
          'product_nm',
          'sales_cd',
          'sales_nm',
          'manager',
          'sales_chnl',
          'distribute_csr',
          'send_date',
          'distributed_date',
          'created_by',
          'status_nm',
          'branch_nm',
          'note_bpo',
          'description'
        ],
        actions: {
          view: {
            color: 'warning',
            icon: this.$vuetify.icons.UPDATE,
            scope: ['record', 'db-click.row'],
            callback: this.viewDetailDocument
          }
        },

        decorates: {
          default: 'full_text'
        }
      },

      date_picker_config: {
        date_format: {
          model: 'DD/MM/YYYY',
          shown_up: 'DD-MM-YYYY'
        },
        clear: true
      },

      waitting: {
        filter: false,
        export: false
      }
    };
  },

  computed: {
    ...mapGetters('notifications', ['getSearchValue'])
  },

  watch: {
    getSearchValue: {
      handler() {
        if (this.getSearchValue == null) return;

        this.filter_conditions = _.cloneDeep(DEFAULT_CONDITIONS);
        this.filter_conditions.id_no = this.getSearchValue;
        this.filter();
        this.storeSearchingValue();
      },
      immediate: true
    }
  },

  created() {
    this.prepare();
  },

  methods: {
    ...mapActions('trackingInformation', [
      'filterDocuments',
      'retrieveChannelAndStatus',
      'getHeaderExcelFile'
    ]),
    ...mapActions('notifications', ['storeSearchingValue']),

    async prepare() {
      this.setFeatureLoading(true);
      try {
        let result = await this.retrieveChannelAndStatus();
        if (!result.success) {
          this.$notify.error(result.message);
          return;
        }

        if (_.isNil(result.data)) {
          this.$notify.warning(this.$t('global.messages.empty_data'));
          return;
        }

        if (!_.isNil(result.data.list_channel_cd)) {
          this.combobox_data.channels = _.concat(
            this.combobox_data.channels,
            result.data.list_channel_cd
          );
        }

        if (!_.isNil(result.data.list_status_cd)) {
          this.combobox_data.status = _.concat(
            this.combobox_data.status,
            result.data.list_status_cd
          );
        }
        let { channels = null } = _.head(this.combobox_data.channels);
        this.filter_conditions.channel = channels;
        let { status = null } = _.head(this.combobox_data.status);
        this.filter_conditions.status = status;
        this.filter_conditions.BPO_checking = null;
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.setFeatureLoading();
      }
    },

    viewDetailDocument(key, document) {
      this.detail_dialog.model = document;
      this.detail_dialog.open = true;
    },

    async filter() {
      this.setFeatureLoading(true);
      this.waitting.filter = true;
      try {
        let result = await this.filterDocuments(this.filter_conditions);
        if (result.success) {
          if (_.isArray(result.data) && result.data.length > 0) {
            this.table_data = result.data.map(row => {
              return {
                ...row,
                ...{
                  document_cd: _.toString(row.document_cd)
                }
              };
            });
            this.table_data.forEach(row => {
              let { bpo_check = true } = row;
              row.bpo_check = 'Y';
              if (bpo_check == false) {
                row.bpo_check = 'N';
              }
            });
          } else {
            this.table_data = [];
            this.$notify.warning(this.$t('global.messages.empty_data'));
          }
        } else {
          this.table_data = [];
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.waitting.filter = false;
        this.setFeatureLoading();
      }
    },
    reloadPage() {
      this.filter();
    },

    async exportExcel() {
      this.setFeatureLoading(true);
      this.waitting.export = true;
      try {
        let filename = `${
          EXCEL_FILENAMES.TRACKING_INFO_REPORT
        }_${moment().format('DD-MM-YYYY')}`;

        let exporter = new Exporter(filename);
        exporter.setConfigurations({
          REPLACE_UNKNOWN_TO_EMPTY: true,
          DECORATE_BORDER: true,
          DECORATE_HEADER_COLOR: '#1f4e78'
        });

        let headers = await this.getHeaderExcelFile();
        exporter.addSheet(headers, this.table_data, 'TRACKING INFOMATION');
        let result = exporter.exportExcel();
        if (result.success) {
          this.$notify.success(
            this.$t('global.messages.export_excel_success', {
              filename: `${filename}.xls`
            })
          );
        } else {
          this.$notify.error(result.message);
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.waitting.export = false;
        this.setFeatureLoading();
      }
    }
  }
};
</script>

<style scope>
.style_note {
  margin-top: 20px;
  text-align: right;
}
</style>
