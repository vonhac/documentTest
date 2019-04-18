<template>
  <v-dialog
    v-model="value"
    width="800px"
    v-bind="{
      persistent: !isMobile,
      fullscreen: isMobile
    }"
    lazy
  >
    <v-card>
      <v-toolbar dark color="primary" tabs>
        <v-btn v-if="isMobile" icon @click="closeDeferDialog">
          <v-icon>{{ $vuetify.icons.BACK_TO_HOME }}</v-icon>
        </v-btn>
        <v-toolbar-title>{{
          $t('tracking_information.detail_page.defer_popup.title')
        }}</v-toolbar-title>
        <v-spacer />
        <v-btn
          color="white"
          style="color: var(--v-primary-base)"
          @click="sendDefer"
        >
          Defer
        </v-btn>
        <v-btn v-if="!isMobile" icon dark @click="closeDeferDialog">
          <v-icon>{{ $vuetify.icons.CLOSE }}</v-icon>
        </v-btn>
      </v-toolbar>
      <v-card-text class="app-wrapper">
        <v-container grid-list-md fluid class="no_padding">
          <v-layout wrap row>
            <v-flex md5>
              <v-select
                v-model="defer_model.group"
                :items="defer_groups"
                label="Group"
                item-text="group_nm"
                item-value="group_cd"
                @change="loadDeferDetails($event)"
              />
            </v-flex>
            <v-flex md7>
              <v-select
                v-model="defer_model.reason"
                :items="defer_details"
                item-text="defer_nm"
                item-value="defer_cd"
                label="Reason"
              />
            </v-flex>
            <v-flex md10>
              <v-text-field v-model="defer_model.note" label="Note" />
            </v-flex>
            <v-flex md2>
              <v-btn color="blue-grey darken-1" dark @click="addDeferNote">
                Add
              </v-btn>
            </v-flex>
            <v-flex md12>
              <p class="sub-title">Defer notes</p>
              <cd-perfect-table
                v-model="table_defer.data"
                :headers="table_defer.headers"
                :custom="table_defer.options"
              />
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import _ from 'lodash';
import { mapActions } from 'vuex';
import { DATE_FORMATER } from 'core/mixins';
import { DOCUMENT_STATUS } from 'core';

export default {
  $_veeValidate: {
    validator: 'new'
  },

  mixins: [DATE_FORMATER],

  props: {
    value: Boolean,

    document_cd: {
      type: String,
      default: ''
    }
  },

  data() {
    return {
      defer_groups: [
        {
          group_cd: '',
          group_nm: ''
        }
      ],
      defer_details: [],
      status_defer: {},
      defer_model: {},

      table_defer: {
        data: [],

        headers: {
          group_cd:
            'tracking_information.detail_page.defer_popup.model.group_cd',
          defer_cd:
            'tracking_information.detail_page.defer_popup.model.defer_cd',
          defer_nm:
            'tracking_information.detail_page.defer_popup.model.defer_nm',
          created_date:
            'tracking_information.detail_page.defer_popup.model.created_date',
          description:
            'tracking_information.detail_page.defer_popup.model.description'
        },

        options: {
          key: 'defer_cd',
          check: true,
          fixed_row_page: true,
          page_size: [5]
        }
      }
    };
  },

  watch: {
    async value() {
      if (
        this.value &&
        (_.isEmpty(this.defer_groups) || _.isEmpty(this.defer_details))
      ) {
        await this.loadGroups();
        await this.loadDeferDetails(this.defer_model.group);
      }
    }
  },

  methods: {
    ...mapActions('deferRule', [
      'cbGroupCd',
      'searchDeferDetail',
      'retrieveDeferGroups',
      'retrieveDeferDetails'
    ]),
    ...mapActions('trackingInformation', [
      'addDeferDocuments',
      'changeStatusDefer'
    ]),
    async loadGroups() {
      try {
        let response = await this.cbGroupCd();
        if (!response.success) {
          this.$notify.error(response.message);
          this.defer_groups = [];
          this.defer_model.group = null;
          return;
        }
        this.defer_groups = response.data;
        this.defer_model.group = this.defer_groups[0].group_cd;
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      }
    },

    async loadDeferDetails(event) {
      if (event != null) {
        let result = await this.searchDeferDetail(event);
        if (result.data != null) {
          this.defer_details = result.data;
          this.defer_model.reason = this.defer_details[0].defer_cd;
        } else {
          this.defer_details = [];
        }
      } else {
        this.defer_model.reason = '';
      }
    },

    /**
     *  ADD DATA IN LIST
     */
    addDeferNote() {
      let isExist = this.table_defer.data.findIndex(defer => {
        return defer.defer_cd == this.defer_model.reason;
      });
      if (isExist >= 0) {
        this.$notify.warning(
          `Defer code [${this.defer_model.reason}] is added`
        );
      } else {
        let selected = _.find(
          this.defer_details,
          detail => detail.defer_cd == this.defer_model.reason
        );
        this.table_defer.data.push({
          group_cd: this.defer_model.group,
          defer_cd: selected.defer_cd,
          defer_nm: selected.defer_nm,
          created_date: this.FULLDATETIME(),
          description: this.defer_model.note,
          document_cd: this.document_cd
        });
        this.status_defer = {
          status_cd: DOCUMENT_STATUS.DEFER,
          document_cd: this.document_cd,
          description: '',
          file_name: ''
        };
      }
      this.defer_model.note = '';
    },

    // ADD DATA WHEN DOCUMENT IS STATUS 'DISTRIBUTED OR MODIFIED'
    async sendDefer() {
      this.setAppLoading(true);
      try {
        let result = await this.addDeferDocuments(this.table_defer.data);
        await this.changeStatusDefer(this.status_defer);
        if (result > 0) {
          this.table_defer.data = [];
          this.$emit('input', false);
          this.$emit('reload');
          this.$notify.success(
            this.$t('tracking_information.messages.success.add_defer_document')
          );
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.setAppLoading();
      }
    },

    closeDeferDialog() {
      if (this.table_defer.data.length > 0) {
        this.$alert
          .warn()
          .setTitle('tracking_information.alerts.title')
          .setMessage('tracking_information.alerts.discard.message')
          .setControls({
            yes: {
              text: 'tracking_information.alerts.discard.buttons.yes',
              style: 'normal',
              dark: true
            },
            no: {
              text: 'tracking_information.alerts.discard.buttons.no',
              color: 'info'
            }
          })
          .setCallback(chose => {
            switch (chose) {
              case 'yes':
                this.table_defer.data = [];
                this.$emit('input', false);
                break;
              case 'no':
                // Ignore
                break;
            }
          })
          .open();
      } else {
        this.$emit('input', false);
      }
    }
  }
};
</script>
