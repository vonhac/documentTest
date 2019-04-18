<template>
  <v-dialog v-model="dialog" persistent width="600px" lazy>
    <v-form ref="formAddEdit" v-model="valid">
      <v-card>
        <v-toolbar dark color="primary" tabs>
          <v-toolbar-title>{{ meta_data[mode].title }}</v-toolbar-title>
          <v-spacer />
          <v-btn
            color="white"
            style="color: var(--v-primary-base)"
            @click="handleSaveDataClick"
          >
            {{ meta_data[mode].nameBnt }}
          </v-btn>
          <v-btn icon dark @click="handleClose">
            <v-icon>{{ $vuetify.icons.CLOSE }}</v-icon>
          </v-btn>
        </v-toolbar>
        <v-card-text class="app-wrapper">
          <v-layout row wrap>
            <v-flex md12>
              <v-text-field
                ref="staffCode"
                v-model="formData.staff_cd"
                :rules="[v => !!v || 'Staff is required']"
                :label="$t('area_manager.popup.labels.staff_cd')"
                :error-messages="validationBag.staffCode"
                maxlength="20"
              />
            </v-flex>
          </v-layout>
          <v-layout row wrap>
            <v-flex md12>
              <v-text-field
                ref="staffName"
                v-model="formData.staff_nm"
                :label="$t('area_manager.popup.labels.staff_nm')"
                maxlength="250"
              />
            </v-flex>
          </v-layout>
          <v-layout row wrap>
            <v-flex md12>
              <v-select
                v-model="getallitem.selected"
                :items="getallitem.items"
                :label="$t('area_manager.popup.labels.branch_sip')"
                :readonly="readOnlyCombo"
              />
            </v-flex>
          </v-layout>
        </v-card-text>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
import _ from 'lodash';
import { mapActions } from 'vuex';

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
      readOnlyCombo: false,
      formData: {},
      validationBag: {
        staffCode: []
      },
      mode: 'add',
      getallitem: {
        selected: null,
        items: [
          {
            text: 'All',
            value: null
          }
        ]
      },
      meta_data: {
        add: {
          title: 'Add - Manage Branch/Sip',
          nameBnt: 'Save'
        },
        edit: {
          title: 'Edit - Manage Branch/Sip',
          nameBnt: 'Save'
        }
      }
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
      } else {
        if (!_.isNil(this.parameter)) {
          this.mode = this.parameter.mode;
        }
      }
    }
  },

  mounted() {},

  created() {
    this.dialog = this.show;
    this.getbranchsip();
  },

  methods: {
    ...mapActions('informationOfStaff', [
      'saveData',
      'updateData',
      'getAllbranchSip'
    ]),

    emitClosePopup() {
      this.$refs.formAddEdit.reset();
      this.$validator.reset();
      this.getallitem.selected = null;
      this.validationBag.staffCode = [];
      this.$emit('close', {
        statusSave: this.statusSave,
        objectSave: this.objectSave
      });
    },

    loadData() {
      if (this.parameter.data != null) {
        this.formData = this.parameter.data;
        this.getallitem.selected = this.parameter.data.branch_sip_id;
      }
      if (this.parameter.mode == 'edit') {
        this.readOnlyCombo = true;
      } else {
        this.readOnlyCombo = false;
      }
      this.objectSave = [];
    },

    handleClose() {
      this.dialog = false;
    },

    async getbranchsip() {
      let branchsip = await this.getAllbranchSip();
      this.getallitem.items = _.concat(
        this.getallitem.items,
        branchsip.data.map(element => {
          return {
            text: element.brach_nml,
            value: element.branch_id
          };
        })
      );
    },

    async handleSaveDataClick() {
      var result = {};
      this.formData.branch_sip_id = this.getallitem.selected;
      if (this.parameter.mode == 'add') {
        result = await this.saveData(this.formData);
        if (result.success) {
          if (result.message == 'Duplicated') {
            this.validationBag.staffCode.push(
              'Duplicated Branch Sip, Please Choose another Branch Sip'
            );
          } else {
            this.$notify.success(result.message);
            this.dialog = false;
          }
        } else {
          this.validationBag.staffCode.push(result.message);
        }
      } else {
        result = await this.updateData(this.formData);
        if (result.success) {
          if (result.message == 'Duplicated') {
            this.validationBag.staffCode.push(
              'Duplicated Branch Sip, Please Choose another Branch Sip'
            );
          } else {
            this.$notify.success(result.message);
            this.dialog = false;
          }
        } else {
          this.validationBag.staffCode.push(result.message);
        }
      }
    }
  }
};
</script>

<style></style>
