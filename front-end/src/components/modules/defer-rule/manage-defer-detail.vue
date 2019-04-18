<template>
  <v-container class="app-wrapper" grid-list-md fluid text-xs-center pa-3>
    <v-form ref="formSearch">
      <v-layout row wrap class="block">
        <v-flex xs12>
          <v-card>
            <v-card-text>
              <v-layout row wrap>
                <v-flex md3>
                  <v-select
                    v-model="formData.group_cd"
                    :label="$t('defer_page.labels.group_defer')"
                    :items="search_group_defer"
                    item-text="group_nm"
                    item-value="group_cd"
                    brown-auto
                  >
                  </v-select>
                </v-flex>
                <v-flex md3>
                  <v-text-field
                    v-model="formData.defer_nm"
                    :label="$t('defer_page.labels.defer_name')"
                  ></v-text-field>
                </v-flex>
                <v-flex md3 class="mt-2" text-xs-left>
                  <v-btn dark color="blue-grey darken-1" @click="idSearch">
                    {{ $t('defer_page.buttons.search') }}
                  </v-btn>
                </v-flex>
                <v-flex md3 xs12 class="mt-2" text-md-right text-xs-left>
                  <v-btn
                    color="primary"
                    @click="handleBtnAddEditGroupDeferClick"
                  >
                    {{ $t('defer_page.buttons.add') }}
                  </v-btn>
                </v-flex>
              </v-layout>
            </v-card-text>
            <v-card-text class="no_padding_top">
              <cd-perfect-table
                v-model="table_data"
                :headers="headers"
                :custom="options"
              />
            </v-card-text>
          </v-card>
        </v-flex>
      </v-layout>
      <add-edit-defer
        :show="showPopUpAdd"
        :parameter="parameterPoup"
        @close="handleClosePopupAdd"
      />
      <add-edit-defer
        :show="showPopUpEdit"
        :parameter="parameterPoup"
        @close="handleClosePopupEdit"
      />
    </v-form>
  </v-container>
</template>

<script>
import _ from 'lodash';
import AddEditDefer from './add-edit-defer-detail';
import { mapActions } from 'vuex';

export default {
  components: {
    AddEditDefer
  },
  data() {
    return {
      showPopUpAdd: false,
      showPopUpEdit: false,
      parameterPoup: {
        title: '',
        nameBnt: '',
        iconName: '',
        mode: '',
        data: {},
        cbData: {}
      },

      table_data: [],

      formData: {
        group_cd: null,
        group_nm: null,
        defer_nm: null
      },
      search_group_defer: [],

      headers: {
        id: 'defer_page.tables.detail.id',
        defer_cd: 'defer_page.tables.detail.defer_cd',
        group_cd: 'defer_page.tables.detail.group_cd',
        defer_nm: 'defer_page.tables.detail.defer_nm'
      },

      alert_controls: {
        delete: {
          delete: {
            text: 'defer_page.alertsDetailDefer.delete.buttons.yes',
            color: 'primary',
            style: 'normal',
            dark: true
          },
          close: {
            text: 'defer_page.alertsDetailDefer.delete.buttons.close',
            color: 'info'
          }
        }
      },
      options: {
        key: 'id',
        autoNo: true,
        fixed_row_page: true,
        hiddens: ['id'],
        page_size: [10],
        center: ['id', 'defer_cd', 'group_cd'],
        actions: {
          edit: {
            color: 'warning',
            icon: this.$vuetify.icons.UPDATE,
            scope: ['record', 'db-click.row', 'menu_context'],
            text_menu: 'View detail',
            callback: (val, record) => {
              this.showPopUpEdit = true;
              this.parameterPoup.mode = 'edit';
              this.parameterPoup.data = record;
            }
          },

          remove: {
            icon: this.$vuetify.icons.DELETE,
            scope: 'record',
            callback: this.removeDetail
          }
        }
      }
    };
  },

  async created() {
    this.getCbGroupCd();
  },

  methods: {
    ...mapActions('deferRule', [
      'cbGroupCd',
      'searchDeferDetail',
      'delDeferDetail',
      'searchDeferDetailNm'
    ]),

    async getCbGroupCd() {
      this.setFeatureLoading(true);
      try {
        let results = await this.cbGroupCd();
        if (results.data != null) {
          this.parameterPoup.cbData = _.cloneDeep(results.data);
          this.search_group_defer = _.concat(
            {
              group_cd: null,
              group_nm: '- All -'
            },
            _.cloneDeep(results.data)
          );
          this.idSearch();
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.setFeatureLoading();
      }
    },

    async idSearch() {
      let results = await this.searchDeferDetailNm(this.formData);
      if (results.success) {
        this.table_data = results.data;
      } else {
        this.table_data = [];
      }
    },

    async handleBtnAddEditGroupDeferClick() {
      this.parameterPoup.mode = 'add';
      this.showPopUpAdd = true;
    },

    handleClosePopupAdd() {
      this.idSearch();
      this.showPopUpAdd = false;
    },

    handleClosePopupEdit() {
      this.idSearch();
      this.showPopUpEdit = false;
    },
    async removeDetail(val, record) {
      this.parameterPoup.data = record;
      this.$alert
        .info()
        .setTitle('defer_page.alertsDetailDefer.title')
        .setMessage('defer_page.alertsDetailDefer.delete.message', {
          agree: this.parameterPoup.data.defer_nm
        })
        .setControls(this.alert_controls.delete)
        .setCallback(this.removeDefer, this.parameterPoup.data)
        .open();
    },
    async removeDefer(event, params) {
      switch (event) {
        case 'delete':
          await this.delDeferDetail(params);
          this.idSearch();
          this.$notify.success(this.$t('defer_page.messages.success.delete'));
          break;
        case 'close':
          break;
      }
    }
  }
};
</script>
