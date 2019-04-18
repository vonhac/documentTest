<template>
  <v-dialog v-model="dialog" width="600px" persistent lazy>
    <v-form ref="formAddEdit" v-model="valid">
      <v-card style="background-color: #FFFFFF;">
        <v-toolbar dark color="primary" tabs>
          <v-toolbar-title>{{ meta_data[mode].title }}</v-toolbar-title>
          <v-spacer />
          <v-toolbar-items>
            <v-btn icon dark @click="handleSaveDataClick">
              {{ meta_data[mode].nameBnt }}
            </v-btn>
            <v-btn icon dark @click="dialog = false">
              <v-icon>close</v-icon>
            </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-card-text class="app-wrapper">
          <v-layout row wrap>
            <v-flex xs12>
              <v-text-field
                v-model="filter_conditions.id_no"
                v-validate="'required|min:8'"
                :error-messages="errors.collect('Id No')"
                label="Id No"
                data-vv-name="Id No"
                required
              />
            </v-flex>
            <v-flex xs12>
              <v-text-field
                v-model="filter_conditions.customer_name"
                v-validate="'required'"
                :error-messages="errors.collect('Customer Name')"
                label="Customer Name"
                data-vv-name="Customer Name"
                required
              />
            </v-flex>
            <v-flex xs12>
              <v-select
                v-model="filter_conditions.products"
                v-validate="'required'"
                :items="combobox_data.products"
                label="Product Name"
                data-vv-name="Product Name"
                item-text="product_nm"
                item-value="product_cd"
                required
              />
            </v-flex>
            <v-flex xs12>
              <v-text-field
                ref="sale_code"
                v-model="filter_conditions.sales_code"
                :disabled="!isAdminUser"
                :append-icon="verifyResultIcon"
                label="Sale Code"
                @blur="handleBlurCheckSaleCode"
              />
            </v-flex>
            <v-flex xs12>
              <v-select
                v-model="filter_conditions.place"
                :items="combobox_data.place"
                label="Send to Place"
                item-text="branch_nm"
                item-value="branch_id"
                :disabled="!isSale"
              />
            </v-flex>
            <v-flex xs12>
              <v-checkbox
                v-model="filter_conditions.isPartner"
                label="Partner"
                :disabled="isSale"
              />
            </v-flex>
          </v-layout>
        </v-card-text>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import _ from 'lodash';

export default {
  $_veeValidate: {
    validator: 'new'
  },
  props: {
    show: {
      type: Boolean,
      default: false
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
      verifyResultIcon: '',
      combobox_data: {
        products: [],
        place: [
          {
            branch_id: null,
            branch_nm: ''
          }
        ]
      },
      account_level: {},
      filter_conditions: {
        products: null,
        sales_code: '',
        place: null,
        isPartner: false
      },

      mode: 'add',
      btn_loading: {
        sale_code: false
      },

      meta_data: {
        add: {
          title: 'Add Document',
          nameBnt: 'Save'
        },
        edit: {
          title: 'Edit Document',
          nameBnt: 'Save'
        }
      }
    };
  },

  computed: {
    ...mapGetters('authentication', ['authUser', 'isAdminUser']),
    ...mapGetters('createImportDocument', ['sale_codes']),

    isSale() {
      if (this.authUser.csr) {
        return false;
      }
      return true;
    }
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
        this.$refs.formAddEdit.reset();
        this.$validator.reset();
        this.emitClosePopup();
      } else {
        if (!_.isNil(this.parameter)) {
          this.mode = this.parameter.mode;
        }
      }
    },

    'filter_conditions.sales_code'() {
      this.verifyResultIcon = '';
    }
  },

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
    ...mapActions('createImportDocument', [
      'addDocument',
      'updateDocument',
      'checkSaleCode'
    ]),
    emitClosePopup() {
      this.$emit('close', {
        statusSave: this.statusSave,
        objectSave: this.objectSave
      });
    },

    loadData() {
      if (this.parameter.data != null) {
        this.filter_conditions = {
          id: this.parameter.data.id,
          id_no: this.parameter.data.id_no,
          customer_name: this.parameter.data.customer_nm,
          products: this.parameter.data.product_id,
          sales_code: this.parameter.data.sales_cd,
          place: this.parameter.data.branch_id,
          isPartner: this.parameter.data.partner,
          document_cd: this.parameter.data.document_cd
        };
        this.combobox_data.products = this.parameter.data.productlist;
        if (!_.isNil(this.parameter.data.placelist)) {
          this.combobox_data.place = _.concat(
            this.combobox_data.place,
            this.parameter.data.placelist
          );
        }
      }
      if (this.parameter.mode == 'add') {
        this.filter_conditions.sales_code = this.authUser.account_id;
        this.filter_conditions.isPartner = false;
        let { product_cd = null } = _.head(this.combobox_data.products);
        this.filter_conditions.products = product_cd;
        if (!this.isSale) {
          this.filter_conditions.place = this.authUser.branch_id;
        }
      }
      this.objectSave = [];
    },

    async handleBlurCheckSaleCode() {
      try {
        let rs = await this.checkSaleCode(this.filter_conditions.sales_code);
        if (rs.success) {
          this.verifyResultIcon = 'check';
          return true;
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      }
      return false;
    },

    async handleSaveDataClick() {
      // check isPartner of CSR
      if (
        this.filter_conditions.isPartner == false &&
        this.authUser.admin == 1
      ) {
        if (this.filter_conditions.sales_code.length == 0) {
          this.$notify.warning('Sale Code Invalid');
          return;
        }
      }
      if (this.filter_conditions.id_no.length < 8) {
        this.$notify.warning('Please enter 8 characters in the column IdNo');
        return;
      }
      const res = await this.$validator.validateAll();
      try {
        let result = {};
        this.statusSave = false;
        let selectedProduct = _.find(
          this.combobox_data.products,
          detail => detail.product_cd == this.filter_conditions.products
        );
        let selectedBranch = _.find(
          this.combobox_data.place,
          detail => detail.branch_id == this.filter_conditions.place
        );
        if (res == true) {
          let postParam = {
            document_cd: this.filter_conditions.document_cd,
            id_no: this.filter_conditions.id_no,
            customer_nm: this.filter_conditions.customer_name,
            product_id: selectedProduct.product_cd,
            product_nm: selectedProduct.product_nm,
            sales_cd: this.filter_conditions.sales_code,
            branch_id: selectedBranch.branch_id,
            branch_nm: selectedBranch.branch_nm,
            partner: this.filter_conditions.isPartner,
            manager: this.authUser.super_visor_id,
            channel_id: this.authUser.department
          };
          if (this.parameter.mode == 'edit') {
            this.objectSave = [];
            result = await this.updateDocument(postParam);
            if (result.success) {
              this.$notify.success('Update success');
            } else {
              this.$notify.error(result.message);
              return false;
            }
          }
          if (this.parameter.mode == 'add') {
            result = await this.addDocument(postParam);
            if (result.success) {
              this.$notify.success('Add document success');
            } else {
              this.$notify.error(result.message);
              return false;
            }
          }
          this.statusSave = true;
          this.objectSave.push(Object.assign({}, this.filter_conditions));
          this.dialog = false;
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      }
    }
  }
};
</script>
<style></style>
