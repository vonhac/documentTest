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
            <v-icon>close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-card-text class="app-wrapper">
          <v-layout row wrap>
            <v-flex md12>
              <v-select
                v-model="formData.group_cd"
                v-validate="'required'"
                :items="search_group_defer"
                :label="labels.group_defer"
                :data-vv-name="labels.group_defer"
                :error-messages="errors.first(labels.group_defer)"
                item-text="group_nm"
                item-value="group_cd"
              />
            </v-flex>
          </v-layout>
          <v-layout row wrap>
            <v-flex md12>
              <v-text-field
                v-model="formData.defer_cd"
                v-validate="'required'"
                :label="labels.defer_code"
                :data-vv-name="labels.defer_code"
                :error-messages="errors.first(labels.defer_code)"
              />
            </v-flex>
          </v-layout>
          <v-layout row wrap>
            <v-flex md12>
              <v-text-field
                v-model="formData.defer_nm"
                v-validate="'required'"
                :label="labels.defer_name"
                :data-vv-name="labels.defer_name"
                :error-messages="errors.first(labels.defer_name)"
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
      formData: {
        id: '',
        defer_cd: '',
        group_cd: '',
        created_by: '',
        modified_by: '',
        defer_nm: ''
      },
      mode: 'add',
      search_group_defer: [],
      meta_data: {
        add: {
          title: 'Add Defer',
          nameBnt: 'Save'
        },
        edit: {
          title: 'Edit Defer',
          nameBnt: 'Save'
        }
      },

      labels: {
        defer_code: this.$t('defer_page.labels.defer_code'),
        defer_name: this.$t('defer_page.labels.defer_name'),
        group_defer: this.$t('defer_page.labels.group_defer')
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
  },

  methods: {
    ...mapActions('deferRule', ['addDeferDetail', 'editDeferDetail']),
    emitClosePopup() {
      this.$refs.formAddEdit.reset();
      this.$validator.reset();
      this.$emit('close', {
        statusSave: this.statusSave,
        objectSave: this.objectSave
      });
    },
    loadData() {
      if (this.parameter.data != null) {
        if (this.parameter.mode == 'add') {
          this.search_group_defer = this.parameter.cbData;
        } else if (this.parameter.mode == 'edit') {
          this.search_group_defer = this.parameter.cbData;
          this.formData = {
            defer_cd: this.parameter.data.defer_cd,
            group_cd: this.parameter.data.group_cd,
            defer_nm: this.parameter.data.defer_nm,
            id: this.parameter.data.id
          };
        }
      }
      this.objectSave = [];
    },

    handleClose() {
      this.dialog = false;
    },

    async handleSaveDataClick() {
      const res = await this.$validator.validateAll();
      if (res == true) {
        if (this.parameter.mode == 'add') {
          let result = await this.addDeferDetail(this.formData);
          if (result.success) {
            this.dialog = false;
            this.$notify.success(this.$t('defer_page.messages.success.add'));
          } else {
            this.$notify.error(this.$t('defer_page.messages.error.add'));
          }
        } else if (this.parameter.mode == 'edit') {
          let result = await this.editDeferDetail(this.formData);
          if (result.success) {
            this.dialog = false;
            this.$notify.success(this.$t('defer_page.messages.success.edit'));
          } else {
            this.$notify.error(this.$t('defer_page.messages.error.edit'));
          }
        }
      }
    }
  }
};
</script>

<style></style>
