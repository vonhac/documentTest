<template>
  <v-container class="app-wrapper" grid-list-md fluid text-xs-center pa-3>
    <v-form ref="formSearch">
      <v-layout row wrap class="block">
        <v-flex xs12>
          <v-card>
            <v-card-text>
              <v-layout row wrap>
                <v-flex md2 xs6>
                  <v-text-field
                    v-model="staff_cd"
                    label="Staff code"
                    @keydown.enter="btnSearch"
                  />
                </v-flex>
                <v-flex md2 xs6 class="mt-2" text-xs-left>
                  <v-btn color="primary" @click="btnSearch">Search</v-btn>
                </v-flex>
                <v-spacer />
                <v-flex md6 text-md-right xs12 text-xs-left>
                  <v-btn
                    class="mt-2"
                    color="primary"
                    @click="handleBtnAddEditAreaManageClick"
                  >
                    {{ $t('area_manager.buttons.add') }}
                  </v-btn>
                  <v-btn
                    class="mt-2"
                    color="blue-grey darken-1"
                    dark
                    @click="deleteSelection"
                  >
                    {{ $t('area_manager.buttons.remove') }}
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
      <add-edit-area-manage
        :show="showPopUpAdd"
        :parameter="parameterPoup"
        @close="handleClosePopupAdd"
      />
      <add-edit-area-manage
        :show="showPopUpEdit"
        :parameter="parameterPoup"
        @close="handleClosePopupEdit"
      />
    </v-form>
  </v-container>
</template>

<script>
import _ from 'lodash';
import { mapActions } from 'vuex';
import AddEditAreaManage from './add-edit-area-manage';

export default {
  components: {
    AddEditAreaManage
  },
  data() {
    return {
      staff_cd: '',
      table_selected: [],
      showPopUpAdd: false,
      showPopUpEdit: false,
      parameterPoup: {
        title: '',
        nameBnt: '',
        iconName: '',
        mode: '',
        data: {}
      },
      staffCode: '',
      table_data: [],
      headers: {
        staff_cd: 'area_manager.table.headers.staff_cd',
        staff_nm: 'area_manager.table.headers.staff_nm',
        branch_sip: 'area_manager.table.headers.branch_sip'
      },

      options: {
        key: 'id_staff_branch',
        autoNo: true,
        checker: true,
        fixed_row_page: true,
        page_size: [10],
        center: ['staff_cd'],
        actions: {
          edit: {
            color: 'warning',
            icon: this.$vuetify.icons.UPDATE,
            scope: 'record',
            callback: this.viewArea
          }
        },
        decorates: {
          default: 'full_text'
        }
      }
    };
  },

  created() {
    this.loaddata();
  },

  methods: {
    ...mapActions('informationOfStaff', ['getalldata', 'removeBranch']),
    readColumns() {},
    viewArea(val, record) {
      this.showPopUpEdit = true;
      this.parameterPoup.mode = 'edit';
      this.parameterPoup.data = record;
    },

    async handleBtnAddEditAreaManageClick() {
      this.parameterPoup.mode = 'add';
      this.parameterPoup.data = null;
      this.showPopUpAdd = true;
    },

    handleClosePopupAdd() {
      this.showPopUpAdd = false;
      this.loaddata();
    },

    handleClosePopupEdit() {
      this.showPopUpEdit = false;
      this.loaddata();
    },

    async deleteSelection() {
      if (_.isNil(this.table_selected) || this.table_selected.length == 0) {
        this.$notify.warning('Please choose row');
        return;
      } else {
        this.$alert.CONFIRM(async next => {
          if (next) {
            let result = await this.removeBranch(
              this.table_selected.map(selected => selected.id_staff_branch)
            );
            if (result.success) {
              this.$notify.success(result.message);
              await this.loaddata();
            } else {
              this.$notify.warning(result.message);
            }
          }
        });
      }
    },

    async loaddata() {
      this.setFeatureLoading(true);
      try {
        let result = await this.getalldata(this.staff_cd);
        if (result.success) {
          this.table_data = _.isArray(result.data) ? result.data : [];
        } else {
          this.$notify.error(result.message);
          this.table_data = [];
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.setFeatureLoading();
      }
    },

    async btnSearch(e) {
      e.preventDefault();
      this.setFeatureLoading(true);
      try {
        let result = await this.getalldata(this.staff_cd);
        if (result.success) {
          if (result.data.length > 0 && result.data != null) {
            this.table_data = _.isArray(result.data) ? result.data : [];
          } else {
            this.$notify.warning('There is no data');
            this.table_data = [];
          }
        } else {
          this.$notify.error(result.message);
          this.table_data = [];
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
