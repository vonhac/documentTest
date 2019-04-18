<template>
  <v-container class="app-wrapper" grid-list-md fluid pa-3>
    <v-form ref="formSearch">
      <v-layout row wrap class="block">
        <v-flex xs12>
          <v-card>
            <v-card-text>
              <v-layout row wrap>
                <v-flex md2>
                  <v-select
                    v-model="SaleChannel.selected"
                    :items="SaleChannel.items"
                    label="Sale Channel"
                  />
                </v-flex>
                <v-flex md2>
                  <v-text-field v-model="formdata.sale_cd" label="Sale Code" />
                </v-flex>
                <v-flex md2>
                  <cd-date-picker
                    v-model="formdata.distribute_dt"
                    :custom="date_picker_config"
                    label="Distribute Date"
                  />
                </v-flex>
                <v-flex md2>
                  <v-select
                    v-model="BPO_checking.selected"
                    :items="BPO_checking.items"
                    label="BPO Check Y/N"
                  />
                </v-flex>
                <v-flex md2 class="mt-2">
                  <v-btn color="primary" @click="SearchBPODocument">
                    Search
                  </v-btn>
                </v-flex>
              </v-layout>
            </v-card-text>
            <v-card-text class="no_padding_top">
              <cd-perfect-table
                v-model="table_data"
                :headers="headers"
                :custom="options"
                :selected.sync="table_selected"
              />
            </v-card-text>
          </v-card>
        </v-flex>
      </v-layout>
    </v-form>
  </v-container>
</template>

<script>
import _ from 'lodash';
import { mapActions } from 'vuex';
import moment from 'moment';
import { STD_DATE_FORMAT } from 'core/constant';

export default {
  data() {
    return {
      moment,
      date_picker_config: {
        date_format: {
          model: STD_DATE_FORMAT,
          shown_up: 'MM/DD/YYYY'
        },
        clear: true
      },
      table_selected: [],
      formdata: {
        sale_cd: null,
        distribute_dt: null,
        sale_channel: null
      },
      BPO_checking: {
        items: [
          {
            text: '- All -',
            value: null
          },
          {
            text: 'No',
            value: 0
          },
          {
            text: 'Yes',
            value: 1
          }
        ],
        selected: null
      },
      SaleChannel: {
        selected: null,
        items: [
          {
            text: 'All',
            value: null
          }
        ]
      },
      table_data: [],
      headers: {
        id_no: 'Id No',
        customer_nm: 'Customer Name',
        product_nm: 'Product',
        sale_cd: 'Sales Code',
        sale_nm: 'Sales Name',
        sale_channel: 'Sales Channel',
        send_dt: 'Send Date',
        data_emtry: 'Data Emtry',
        distribute_dt: 'Distribute Date',
        document_sts: 'Status'
      },

      options: {
        key: 'document_id',
        autoNo: true,
        checker: true,
        checker_options: {
          all_check: false,
          alias_name: 'BPO Check'
        },
        fixed_row_page: true,
        page_size: [10],
        center: ['id_no', 'document_id', 'sale_cd'],
        actions: {
          BPOUpdateCheck: {
            scope: 'selected_row',
            callback: this.BPOCheckDocument
          }
        },
        default_sort: {
          column: 'distribute_dt',
          desc: false
        },

        decorates: {
          default: 'full_text_table',
          class: {
            send_dt: 'text_right',
            distribute_dt: 'text_right'
          }
        }
      }
    };
  },

  async created() {
    this.loadBPODocument();
    this.getSaleChannel();
  },

  methods: {
    ...mapActions('noteBpoCheckDocument', [
      'getBPODocumentData',
      'getAllSaleChannel',
      'BPOCheckDocument'
    ]),

    async getSaleChannel() {
      let result = await this.getAllSaleChannel();
      this.SaleChannel.items = _.concat(
        this.SaleChannel.items,
        result.data.map(element => {
          return {
            text: element.department_nm,
            value: element.department_cd
          };
        })
      );
    },
    async loadBPODocument() {
      this.setFeatureLoading(true);
      try {
        let result = await this.getBPODocumentData(this.formdata);
        if (result.success) {
          if (result.data != null && result.data.length > 0) {
            this.table_data = result.data;
            this.table_selected = this.table_data.filter(
              row => row.bpo_check == true
            );
          } else {
            this.table_data = [];
            this.$notify.warning(result.message);
          }
        } else {
          this.table_data = [];
          this.$notify.error(result.message);
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.setFeatureLoading();
      }
    },

    async SearchBPODocument() {
      this.setFeatureLoading(true);
      try {
        this.formdata.sale_channel = this.SaleChannel.selected;
        this.formdata.bpo_check = this.BPO_checking.selected;
        let result = await this.getBPODocumentData(this.formdata);
        if (result.success) {
          if (result.data != null && result.data.length > 0) {
            this.table_data = result.data;
            this.table_selected = this.table_data.filter(
              row => row.bpo_check == true
            );
          } else {
            this.table_data = [];
            this.$notify.warning('There is no data');
          }
        } else {
          this.table_data = [];
          this.$notify.error(result.message);
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.setFeatureLoading();
      }
    }
  }
};
</script>

<style scope>
.full_text_table {
  white-space: nowrap;
}

.text_left {
  text-align: left !important;
}

.text_center {
  text-align: center !important;
}

.text_right {
  text-align: right !important;
}

.style_note {
  margin-top: 20px;
  text-align: right;
}
</style>
