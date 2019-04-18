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
              <v-text-field
                v-model="formData.group_cd"
                v-validate="'required'"
                :label="$t('defer_page.labels.group_code')"
                :error-messages="errors.collect('Group Code')"
                data-vv-name="Group Code"
                required
              />
            </v-flex>
          </v-layout>
          <v-layout row wrap>
            <v-flex md12>
              <v-text-field
                v-model="formData.group_nm"
                v-validate="'required'"
                :label="$t('defer_page.labels.group_name')"
                :error-messages="errors.collect('Group Name')"
                data-vv-name="Group Name"
                required
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
        group_cd: '',
        group_nm: '',
        created_by: '',
        created_date: '',
        modified_by: '',
        modified_date: ''
      },
      mode: 'add',

      meta_data: {
        add: {
          title: 'Add Group Defer',
          nameBnt: 'Save'
        },
        edit: {
          title: 'Edit Group Defer',
          nameBnt: 'Save'
        },
        deleted: {
          title: 'Delete Group Defer',
          nameBnt: 'Delete'
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
  },

  methods: {
    ...mapActions('deferRule', ['addDataDefault', 'editDataDefault']),

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
    loadData() {
      if (this.parameter.data != null) {
        this.formData = {
          group_cd: this.parameter.data.group_cd,
          group_nm: this.parameter.data.group_nm,
          id: this.parameter.data.id
        };
      }
      this.objectSave = [];
    },
    async handleSaveDataClick() {
      const res = await this.$validator.validateAll();
      try {
        if (res == true) {
          if (this.parameter.mode === 'edit') {
            let result = await this.editDataDefault(this.formData);
            if (result.success) {
              this.$notify.success(this.$t('defer_page.messages.success.edit'));
              this.dialog = false;
            } else {
              this.$notify.error(result.message);
            }
          } else {
            let results = await this.addDataDefault(this.formData);
            if (results.success) {
              this.$notify.success(this.$t('defer_page.messages.success.add'));
              this.dialog = false;
            } else {
              this.$notify.error(results.message);
            }
          }
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      }
    }
  }
};
</script>

<style></style>
