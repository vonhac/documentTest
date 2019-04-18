<template>
  <v-dialog v-model="dialog" width="600px" lazy>
    <v-form ref="formAddEdit" v-model="valid">
      <v-card style="background-color: #FFFFFF;">
        <v-toolbar dark color="#009688" tabs>
          <v-toolbar-title>Export Invalid Document</v-toolbar-title>
          <v-spacer />
          <v-toolbar-items>
            <v-btn icon dark @click="handleClose">
              <v-icon>close</v-icon>
            </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-card-text class="app-wrapper">
          <v-layout row wrap>
            <v-flex md12> <span>Check F1 Invalid.</span> </v-flex>
          </v-layout>
          <v-layout row wrap>
            <v-flex md12> <span>Check Home System Invalid.</span> </v-flex>
          </v-layout>
          <v-layout row wrap>
            <v-flex md12>
              <span>Check Management Document Tool Invalid.</span>
            </v-flex>
          </v-layout>
          <v-layout row wrap mt5>
            <v-flex md12 class="text_right">
              <v-btn class="btn_ui" @click="checkInvalid">Check Invalid</v-btn>
              <v-btn class="btn_ui" @click="exportInvalid">
                Export Invalid Document
              </v-btn>
            </v-flex>
          </v-layout>
        </v-card-text>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
import { Exporter } from '@chidoan/excel-utils';
import { mapActions } from 'vuex';
import _ from 'lodash';
import { DOCUMENT_STATUS } from 'core';

export default {
  $_veeValidate: {
    validator: 'new'
  },
  props: {
    show: {
      type: Boolean,
      default: true
    },
    parameter: {
      type: Object,
      default: null
    }
  },

  data() {
    return {
      dialog: false,
      objectSave: [],
      statusSave: false,
      valid: true,
      checkInvalidData: [],
      tableData: [],
      errorData: [],
      document_list: []
    };
  },

  watch: {
    show(value) {
      this.dialog = value;
      if (this.dialog) {
        this.loadData();
      }
    },

    dialog() {
      if (!this.dialog) {
        this.emitClosePopup();
      }
    }
  },

  mounted() {},

  created() {
    this.dialog = this.show;
  },

  methods: {
    ...mapActions('global', [
      'setFeatureLoading',
      'resetNotify',
      'showSuccessMsg',
      'showErrorMsg',
      'showWarningMsg'
    ]),
    ...mapActions('createImportDocument', ['checkInvalidDocument']),
    loadData() {
      this.checkInvalidData = this.parameter.checkInvalidDocument;
      this.tableData = this.parameter.tableData;
    },
    emitClosePopup() {
      this.$refs.formAddEdit.reset();
      this.$validator.reset();
      this.$emit('close', {
        statusSave: this.statusSave,
        data: this.document_list
      });
    },

    handleClose() {
      this.dialog = false;
    },

    async checkInvalid() {
      if (!this.checkInvalidData) {
        this.showWarningMsg('Please select document to validate');
        return;
      }
      let resultImport = await this.checkInvalidDocument(this.checkInvalidData);
      if (resultImport.success) {
        this.document_list = resultImport.data;
        this.showSuccessMsg('Check invalid success');
      } else {
        this.showErrorMsg('Check invalid got error');
      }
    },

    exportInvalid() {
      let exporter = new Exporter('list_invalid_document');
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
        customer_name: {
          text: 'Customer Name',
          type: 'String',
          width: 160
        },
        product_name: {
          text: 'Product Name',
          type: 'String',
          width: 160
        },
        sales_code: {
          text: 'Sales Code',
          type: 'String',
          width: 160
        },
        sales_name: {
          text: 'Sales Name',
          type: 'String',
          width: 160
        },
        sales_channel: {
          text: 'Sales Channel',
          type: 'String',
          width: 160
        },
        status: {
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

      var arrData = this.document_list;
      var updateData = _.map(this.tableData, function(o) {
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

      var invalidData = _.filter(updateData, function(o) {
        return o['status_cd'] == DOCUMENT_STATUS.INVALID;
      });

      exporter.addSheet(headers, invalidData, 'import');
      let result = exporter.exportExcel();

      if (result.success) {
        this.showSuccessMsg('Export success');
      } else {
        this.showErrorMsg('Export got error');
      }
    }
  }
};
</script>

<style scope>
.text_right {
  text-align: right !important;
}
</style>
