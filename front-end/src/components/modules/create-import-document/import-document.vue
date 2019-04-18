<template>
  <div>
    <v-dialog v-model="dialog" width="700px" persistent lazy>
      <v-form ref="formAddEdit" v-model="valid">
        <v-card style="background-color: #FFFFFF;">
          <v-toolbar dark color="primary" tabs>
            <v-toolbar-title>Import Document</v-toolbar-title>
            <v-spacer />
            <v-toolbar-items>
              <v-btn icon dark @click="handleClose">
                <v-icon>close</v-icon>
              </v-btn>
            </v-toolbar-items>
          </v-toolbar>
          <v-card-text class="app-wrapper">
            <v-layout row wrap>
              <v-flex md12 class="text_left">
                <label>Upload file</label>
              </v-flex>
            </v-layout>
            <v-layout row wrap>
              <v-flex md12 class="text_left">
                <input
                  ref="uploadFile"
                  type="file"
                  placeholder="Không có tập nào được chọn"
                />
              </v-flex>
            </v-layout>
            <v-layout row wrap>
              <v-flex md12 class="text_left">
                <p>
                  <span v-if="status == ''"> </span>
                  <span v-else-if="status === 'success'" class="success-text">
                    Successful
                  </span>
                  <span v-else-if="status === 'fail'" class="fail-text">
                    Import Failed
                  </span>
                </p>
              </v-flex>
            </v-layout>
            <v-layout row wrap>
              <v-flex md12 class="text_right">
                <v-btn
                  color="primary"
                  :loading="waitting.import"
                  :disabled="waitting.import"
                  @click="uploadFile"
                >
                  Import Document
                </v-btn>
              </v-flex>
            </v-layout>
          </v-card-text>
        </v-card>
      </v-form>
    </v-dialog>
  </div>
</template>

<script>
import { Importer, Exporter } from '@chidoan/excel-utils';
import { mapActions, mapGetters } from 'vuex';
import _ from 'lodash';

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
      export_loading: false,
      dialog: false,
      objectSave: [],
      statusSave: false,
      valid: true,
      error_list: [],
      status: '',
      waitting: {
        import: false
      }
    };
  },
  computed: {
    ...mapGetters('authentication', ['authUser'])
  },
  watch: {
    show(value) {
      this.dialog = value;
      this.status = '';
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
    ...mapActions('createImportDocument', ['importDocument']),
    emitClosePopup() {
      this.$refs.formAddEdit.reset();
      this.$validator.reset();
      this.$emit('close', {
        statusSave: this.statusSave,
        objectSave: this.objectSave
      });
    },

    handleClose() {
      this.dialog = false;
    },

    async uploadFile() {
      console.log(this.authUser);
      this.waitting.import = true;
      this.status = '';
      let file = this.$refs.uploadFile.files[0];
      if (file == undefined) {
        this.$notify.warning('Please select file to upload');
        return;
      }
      if (file.name.indexOf('.xls') == -1) {
        this.$notify.warning('Filename Invalid');
        return;
      }
      let result = await Importer.readXLSX(file, ['Sheet1']);
      var viewRawData = Importer.correctJsonPropertyName(
        result.data['Sheet1'],
        {
          id_no: { name: 'ID No', type: 'String' },
          customer_nm: { name: 'Customer Name', type: 'String' },
          product_id: { name: 'Product code', type: 'String' },
          sales_cd: { name: 'Sale Code', type: 'String' },
          branch_id: { name: 'Send to place', type: 'String' },
          partner: { name: 'Partner', type: 'String' }
        }
      );
      if (!viewRawData.success) {
        this.$notify.warning('Data is incorrect');
        return;
      }

      if (viewRawData.data != null && viewRawData.data.length > 0) {
        let v_data = viewRawData.data;
        v_data.forEach(row => {
          if (!this.authUser.csr) {
            row.partner = false;
          } else {
            let { partner = 'N' } = row;
            row.partner = false;
            if (_.trim(partner) == 'Y' || _.trim(partner) == 'y') {
              row.partner = true;
            }
          }
        });
        let resultImport = await this.importDocument(v_data);
        if (resultImport.success) {
          this.status = 'success';
          this.$notify.success('import success');
          this.statusSave = true;
          this.handleClose();
        } else {
          this.error_list = resultImport.data.list_error;
          this.handleBtnExportClick();
          this.status = 'fail';
          this.$notify.warning('import got error');
        }
      } else {
        this.$notify.error(
          'you can not import when data in file import is null'
        );
      }
      this.waitting.import = false;
    },

    handleBtnExportClick() {
      let exporter = new Exporter('list_errors_import');
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#1f4e78'
      });
      let headers = {
        id_no: {
          text: 'ID No',
          type: 'String',
          width: 80
        },
        customer_nm: {
          text: 'Customer Name',
          type: 'String',
          width: 80
        },
        product_id: {
          text: 'Product code',
          type: 'String',
          width: 80
        },
        sales_cd: {
          text: 'Sale Code',
          type: 'String',
          width: 80
        },
        branch_id: {
          text: 'Send to place',
          type: 'String',
          width: 80
        },
        message: {
          text: 'Reason Error',
          type: 'String',
          width: 160
        }
      };
      exporter.addSheet(headers, this.error_list, 'import');
      exporter.exportExcel();
    }
  }
};
</script>

<style scope>
.text_right {
  text-align: right !important;
}

.text_left {
  text-align: left !important;
}

.success-text {
  color: #919ae6;
}

.fail-text {
  color: #d76571;
}
</style>
