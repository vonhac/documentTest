<template>
  <v-container class="app-wrapper" grid-list-md fluid text-xs-center pa-3>
    <v-form ref="formSearch">
      <v-layout row wrap class="block">
        <v-flex xs12>
          <v-card>
            <v-card-text>
              <v-layout row wrap>
                <v-flex md12 text-xs-right>
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
      <add-edit-group-defer
        :show="showPopUpAdd"
        :parameter="parameterPoup"
        @close="handleClosePopupAdd"
      />
      <add-edit-group-defer
        :show="showPopUpEdit"
        :parameter="parameterPoup"
        @close="handleClosePopupEdit"
      />
    </v-form>
  </v-container>
</template>

<script>
import AddEditGroupDefer from './add-edit-group-defer';
import { mapActions } from 'vuex';

export default {
  components: {
    AddEditGroupDefer
  },
  data() {
    return {
      showPopUpAdd: false,
      showPopUpEdit: false,
      showPopUpRemove: false,
      rowRemove: {},
      parameterPoup: {
        title: '',
        nameBnt: '',
        iconName: '',
        message: '',
        mode: '',
        data: {}
      },
      table_data: [],

      headers: {
        id: 'defer_page.tables.group.id',
        group_cd: 'defer_page.tables.group.group_cd',
        group_nm: 'defer_page.tables.group.group_nm'
      },
      alert_controls: {
        delete: {
          delete: {
            text: 'defer_page.alertsGroupDefer.delete.buttons.yes',
            color: 'primary',
            style: 'normal',
            dark: true
          },
          close: {
            text: 'defer_page.alertsGroupDefer.delete.buttons.close',
            color: 'info'
          }
        }
      },

      options: {
        key: 'id',
        fixed_row_page: true,
        autoNo: true,
        hiddens: ['id'],
        page_size: [10],
        center: ['id', 'group_cd'],
        actions: {
          edit: {
            color: 'warning',
            icon: this.$vuetify.icons.UPDATE,
            text_menu: 'View detail',
            scope: ['record', 'db-click.row', 'menu_context'],
            callback: (val, record) => {
              this.showPopUpEdit = true;
              this.parameterPoup.mode = 'edit';
              this.parameterPoup.data = record;
            }
          },

          remove: {
            icon: this.$vuetify.icons.DELETE,
            text_menu: 'Delete this group',
            scope: ['record', 'menu_context'],
            callback: this.removeGroup
          }
        }
      }
    };
  },

  async created() {
    this.loadDataDefault1();
  },

  methods: {
    ...mapActions('deferRule', ['loadDataDefault', 'delDataDefault']),

    async loadDataDefault1() {
      this.setFeatureLoading(true);
      try {
        let results = await this.loadDataDefault();
        if (results.data != null) {
          this.table_data = results.data;
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.setFeatureLoading();
      }
    },
    async handleBtnAddEditGroupDeferClick() {
      this.parameterPoup.mode = 'add';
      this.parameterPoup.data = null;
      this.showPopUpAdd = true;
    },
    handleClosePopupAdd() {
      this.loadDataDefault1();
      this.showPopUpAdd = false;
    },

    async removeGroup(val, record) {
      this.parameterPoup.data = record;
      this.$alert
        .info()
        .setTitle('defer_page.alertsGroupDefer.title')
        .setMessage('defer_page.alertsGroupDefer.delete.message', {
          agree: this.parameterPoup.data.group_cd
        })
        .setControls(this.alert_controls.delete)
        .setCallback(this.agreeToRemove, this.parameterPoup.data)
        .open();
    },
    async agreeToRemove(event, params) {
      let rs = {};
      switch (event) {
        case 'delete':
          rs = await this.delDataDefault(params);
          if (rs.success) {
            this.$notify.success(rs.message);
          } else {
            this.$notify.error(rs.message);
          }
          this.loadDataDefault1();
          break;
        case 'close':
          break;
      }
    },

    handleClosePopupEdit() {
      this.loadDataDefault1();
      this.showPopUpEdit = false;
    }
  }
};
</script>
