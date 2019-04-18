<template>
  <div>
    <v-dialog v-model="value" fullscreen persistent lazy>
      <v-card>
        <v-toolbar fixed dark color="primary" height="60px">
          <v-btn icon @click="closeDetailPage">
            <v-icon>{{ $vuetify.icons.BACK_TO_HOME }}</v-icon>
          </v-btn>
          <v-toolbar-title>
            {{ $t('tracking_information.detail_page.title') }}
          </v-toolbar-title>
          <v-spacer />
          <v-toolbar-items>
            <template v-if="isMobile && computedActions().length > 0">
              <v-menu offset-x right z-index="999">
                <v-list>
                  <v-list-tile
                    v-for="(control, index) in computedActions()"
                    :key="index"
                    class="px-1"
                    @click="control.action"
                  >
                    <v-list-tile-action>
                      <v-icon color="primary">{{ control.icon }}</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                      <v-list-tile-title>
                        {{ $t(control.text) }}
                      </v-list-tile-title>
                    </v-list-tile-content>
                  </v-list-tile>
                </v-list>
                <template v-slot:activator="{ on }">
                  <v-btn icon v-on="on">
                    <v-icon>more_vert</v-icon>
                  </v-btn>
                </template>
              </v-menu>
            </template>
            <template v-else-if="!isMobile">
              <v-btn
                v-for="(control, index) in computedActions()"
                :key="index"
                flat
                @click="control.action"
              >
                {{ $t(control.text) }}
              </v-btn>
            </template>
          </v-toolbar-items>
        </v-toolbar>
        <v-card-text
          class="app-wrapper no_padding"
          style="padding-top: 60px !important"
        >
          <v-layout class="tab-title">
            <div :class="{ active: active_tab == 1 }" @click="active_tab = 1">
              Summary
            </div>
            <div :class="{ active: active_tab == 2 }" @click="active_tab = 2">
              Defer List
            </div>
            <v-spacer />
            <div v-if="!isMobile" class="no_margin">
              {{
                $t('tracking_information.detail_page.st_deadline') + deadline
              }}
            </div>
          </v-layout>
          <v-container class="no_margin" grid-list-md fluid>
            <v-window v-model="active_tab" touchless>
              <v-window-item :value="1">
                <v-layout row wrap>
                  <v-flex v-if="isMobile" xs12>
                    <v-text-field
                      v-model="deadline"
                      :prefix="
                        $t('tracking_information.detail_page.st_deadline')
                      "
                      readonly
                      class="no_padding"
                    />
                  </v-flex>
                  <v-flex md5 xs12>
                    <p :class="['sub-title', isMobile ? 'mt-3' : '']">
                      {{
                        $t(
                          'tracking_information.detail_page.st_bpo_check_application'
                        )
                      }}
                    </p>
                    <v-layout row wrap>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.id_no"
                          readonly
                          :label="
                            $t('tracking_information.detail_page.model.id_no')
                          "
                        />
                      </v-flex>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.customer_nm"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.customer_nm'
                            )
                          "
                        />
                      </v-flex>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.app_id"
                          readonly
                          :label="
                            $t('tracking_information.detail_page.model.app_id')
                          "
                        />
                      </v-flex>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.product_nm"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.product_nm'
                            )
                          "
                        />
                      </v-flex>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.sales_cd"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.sales_cd'
                            )
                          "
                        />
                      </v-flex>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.sales_nm"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.sales_nm'
                            )
                          "
                        />
                      </v-flex>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.sales_chnl"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.sales_chnl'
                            )
                          "
                        />
                      </v-flex>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.distribute_csr"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.distribute_csr'
                            )
                          "
                        />
                      </v-flex>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.retrieval_csr"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.retrieval_csr'
                            )
                          "
                        />
                      </v-flex>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.created_by"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.created_by'
                            )
                          "
                        />
                      </v-flex>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.received_by"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.received_by'
                            )
                          "
                        />
                      </v-flex>
                    </v-layout>
                  </v-flex>
                  <v-flex md5 offset-md1 xs12>
                    <p :class="['sub-title', isMobile ? 'mt-4' : '']">
                      {{ $t('tracking_information.detail_page.st_status') }}
                    </p>
                    <v-layout wrap row>
                      <v-flex md12>
                        <v-text-field
                          v-model="document_detail.status_nm"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.status_cd'
                            )
                          "
                        />
                      </v-flex>
                      <v-flex v-if="document_detail.status_cd == 'CANCEL'" md12>
                        <v-text-field
                          v-model="note_cancel"
                          readonly
                          :label="
                            $t(
                              'tracking_information.detail_page.model.note_cancel'
                            )
                          "
                        />
                      </v-flex>
                      <template
                        v-if="isCSR && document_detail.status_cd == 'MODIFIED'"
                      >
                        <v-textarea
                          v-model="document_detail.modified_content"
                          :append-icon="$vuetify.icons.VIEW"
                          :label="
                            $t(
                              'tracking_information.detail_page.model.modified_content'
                            )
                          "
                          rows="3"
                          readonly
                          @click:append="viewAttachmentFile"
                        />
                      </template>
                    </v-layout>
                    <p :class="['sub-title', isMobile ? 'mt-4' : 'mt-5']">
                      {{
                        $t('tracking_information.detail_page.st_history_status')
                      }}
                    </p>
                    <cd-perfect-table
                      v-model="table_status_history.data"
                      :headers="table_status_history.headers"
                      :custom="table_status_history.options"
                    />
                  </v-flex>
                </v-layout>
              </v-window-item>
              <v-window-item :value="2">
                <v-layout row wrap>
                  <v-flex v-if="isSaleShown" md12>
                    <cd-perfect-table
                      key="editable"
                      v-model="table_defer_ediable_data"
                      :headers="table_defer_ediable.headers"
                      :custom="table_defer_ediable.options"
                      :selected.sync="table_defer_ediable_checkbox"
                    />
                  </v-flex>
                  <v-flex v-else md12>
                    <cd-perfect-table
                      key="not_editable"
                      v-model="table_defer_data"
                      :headers="table_defer.headers"
                      :custom="table_defer.options"
                    />
                  </v-flex>
                </v-layout>
              </v-window-item>
            </v-window>
          </v-container>
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog
      v-if="isSaleShown"
      v-model="dialog_modified"
      width="700px"
      persistent
      lazy
    >
      <v-card>
        <v-toolbar dark color="primary" tabs>
          <v-toolbar-title>{{
            $t('tracking_information.detail_page.modified_popup.title')
          }}</v-toolbar-title>
          <v-spacer />
          <v-btn icon dark @click="dialog_modified = false">
            <v-icon>{{ $vuetify.icons.CLOSE }}</v-icon>
          </v-btn>
        </v-toolbar>
        <v-card-text class="app-wrapper no_padding_bottom">
          <v-layout wrap row>
            <v-flex md8>
              <input
                ref="attachment_file"
                type="file"
                accept="application/pdf"
                hidden
                @change="pickFile"
              />
              <v-text-field
                v-model="modified_info.attachment_file"
                :label="
                  $t(
                    'tracking_information.detail_page.modified_popup.attachment_file'
                  )
                "
                :append-icon="
                  modified_info.attachment_file == ''
                    ? $vuetify.icons.ATTACH_FILE
                    : $vuetify.icons.CLOSE
                "
                pr
                hint="Only can attach a PDF file"
                persistent-hint
                readonly
                @click:append="openWindowDialog"
              />
            </v-flex>
            <v-flex md12>
              <v-textarea
                v-model="modified_info.content"
                :label="
                  $t('tracking_information.detail_page.modified_popup.content')
                "
                no-resize
                rows="4"
              />
            </v-flex>
          </v-layout>
        </v-card-text>
        <v-card-text class="no_padding btn-controls">
          <v-layout justify-end>
            <v-btn color="primary" @click="responseDefer">
              <v-icon>{{ $vuetify.icons.SEND }}</v-icon>
            </v-btn>
          </v-layout>
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog v-model="dialog_ContactCancel" width="700px" lazy>
      <v-card>
        <v-toolbar dark color="primary" tabs>
          <v-toolbar-title>
            {{ this.parameter.customer_nm }}
            {{ $t(`tracking_information.detail_page.cancel_popup.title`) }}
          </v-toolbar-title>
          <v-spacer />
          <v-btn icon dark @click="dialog_ContactCancel = false">
            <v-icon>{{ $vuetify.icons.CLOSE }}</v-icon>
          </v-btn>
        </v-toolbar>
        <v-card-text>
          <v-textarea
            v-model="cancel_info.input"
            :label="$t('tracking_information.detail_page.cancel_popup.content')"
            no-resize
            rows="1"
          >
          </v-textarea>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" flat @click="dialogInput"> Yes </v-btn>
          <v-btn color="primary" flat @click="dialog_ContactCancel = false">
            No
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="dialog_appID" width="700px" lazy>
      <v-card>
        <v-toolbar dark color="primary" tabs>
          <v-toolbar-title>
            {{ this.parameter.customer_nm }}
            {{ $t(`tracking_information.detail_page.add_appID.title`) }}
          </v-toolbar-title>
          <v-spacer />
        </v-toolbar>
        <v-card-text>
          <v-text-field
            v-model="add_app_id"
            :label="$t('tracking_information.detail_page.add_appID.content')"
          >
          </v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" flat @click="dialog_addAppID"> Yes </v-btn>
          <v-btn color="primary" flat @click="dialog_appID = false"> No </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <defer-insert-popup
      v-model="dialog_defer"
      :document_cd="parameter.document_cd"
      :status_cd="parameter.status_cd"
      @reload="reloadHistoryDefers"
    />
  </div>
</template>

<script>
import _ from 'lodash';
import { DATE_FORMATER } from 'core/mixins';
import { DOCUMENT_STATUS } from 'core';
import { mapActions } from 'vuex';
import { mapGetters } from 'vuex';

import DeferInsertPopup from './insert-defer';
import { FileUtils } from 'utilities';

export default {
  components: { DeferInsertPopup },

  $_veeValidate: {
    validator: 'new'
  },

  mixins: [DATE_FORMATER],

  props: {
    value: {
      type: Boolean,
      default: false
    },

    parameter: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },

  data() {
    return {
      dialog_modified: false,
      dialog_defer: false,
      dialog_ContactCancel: false,
      dialog_appID: false,

      status_defer: {},
      reload_data: {
        document_cd: ''
      },
      dataChangeStatusPass: {
        channel_id: '',
        status_cd: '',
        document_cd: '',
        id_no: ''
      },
      dataStatusCancel: {},
      dataStatusHis: {
        status_cd: '',
        document_cd: '',
        created_date: '',
        modified_date: '',
        modified_by: '',
        created_by: '',
        file_name: '',
        description: ''
      },

      document_detail: {},

      modified_info: {
        attachment_file: ''
      },
      cancel_info: {},
      add_app_id: '',
      dataAddAppID: [],

      active_tab: 1,
      deadline: '',
      note_cancel: '',

      table_status_history: {
        data: [],

        headers: {
          id: '',
          status_cd: 'tracking_information.detail_page.table_history.status_cd',
          created_date:
            'tracking_information.detail_page.table_history.created_date'
        },

        options: {
          key: 'id',
          fixed_row_page: true,
          page_size: [5],
          center: ['created_date'],
          default_sort: {
            column: 'id',
            desc: true
          },
          hiddens: ['id'],

          decorates: {
            class: {
              created_date: 'full_text'
            }
          }
        }
      },

      table_defer_data: [],
      table_defer_ediable_data: [],

      table_defer_ediable_checkbox: [],
      table_defer: {
        headers: {
          id: 'tracking_information.detail_page.table_defer.id',
          defer_cd: 'tracking_information.detail_page.table_defer.defer_cd',
          group_cd: 'tracking_information.detail_page.table_defer.group_cd',
          defer_nm: 'tracking_information.detail_page.table_defer.defer_nm',
          created_date:
            'tracking_information.detail_page.table_defer.created_date',
          description:
            'tracking_information.detail_page.table_defer.description'
        },

        options: {
          key: 'id',
          autoNo: true,
          fixed_row_page: true,
          default_sort: {
            column: 'id',
            desc: true
          },
          hiddens: ['id'],
          page_size: [5],
          center: ['id', 'defer_cd', 'group_cd', 'defer_nm', 'description'],
          decorates: {
            default: 'full_text',
            class: {
              defer_nm: 'text_left',
              description: 'text_left'
            }
          }
        }
      },

      alert_controls: {
        pass: {
          pass: {
            text: 'tracking_information.alerts.pass.buttons.yes',
            color: 'primary',
            style: 'normal',
            dark: true
          },

          close: {
            text: 'tracking_information.alerts.pass.buttons.close',
            color: 'info'
          }
        },

        cancel: {
          cancel: {
            text: 'tracking_information.alerts.cancel.buttons.cancel',
            color: 'error',
            style: 'normal',
            dark: true
          },

          close: {
            text: 'tracking_information.alerts.cancel.buttons.close',
            color: 'info'
          }
        }
      }
    };
  },

  computed: {
    ...mapGetters('authentication', ['authUser']),

    isCSR() {
      return this.authUser.csr;
    },

    isShownDefer() {
      return (
        this.isCSR &&
        _.includes(
          [DOCUMENT_STATUS.DISTRIBUTED, DOCUMENT_STATUS.MODIFIED],
          this.document_detail.status_cd
        )
      );
    },

    isSaleShown() {
      let accept = this.document_detail.status_cd;
      return !this.isCSR && accept == DOCUMENT_STATUS.DEFER;
    },

    table_defer_ediable() {
      let configs = _.cloneDeep(this.table_defer);
      configs.options.checker = true;
      configs.options.actions = {
        abc: {
          scope: 'selected_row',
          callback: this.addReasonDefer
        }
      };
      return configs;
    }
  },

  watch: {
    value(val) {
      if (val) {
        this.active_tab = 1;
        this.loadData();
      }
    }
  },

  methods: {
    ...mapActions('trackingInformation', [
      'retrieveStatusHistory',
      'retrieveDeferHistory',
      'updateDeferDocuments',
      'filterDocuments',
      'addDeferDocuments',
      'updateDeferDocumentsList',
      'uploadFileModified',
      'viewAttachedFile',
      'changeStatusPass',
      'changeStatusModified',
      'changeStatusCancel',
      'changeStatusDefer',
      'loadDocuments',
      'addAppID'
    ]),

    computedActions() {
      let controls = [];
      if (this.isSaleShown)
        controls.push({
          icon: 'border_color',
          text: 'tracking_information.detail_page.buttons.modifier',
          action: () => {
            this.dialog_modified = true;
          }
        });

      if (this.isShownDefer) {
        controls.push({
          icon: 'reply_all',
          text: 'tracking_information.detail_page.buttons.defer',
          action: () => {
            this.dialog_defer = true;
          }
        });
        controls.push({
          icon: 'done',
          text: 'tracking_information.detail_page.buttons.pass',
          action: this.openPassConfirm
        });
        controls.push({
          icon: 'clear',
          text: 'tracking_information.detail_page.buttons.cancel',
          action: this.openCancelConfirm
        });
      }

      if (
        this.document_detail.status_cd == DOCUMENT_STATUS.PASS &&
        this.document_detail.app_id == null
      )
        controls.push({
          icon: 'border_color',
          text: 'tracking_information.detail_page.buttons.matching',
          action: () => {
            this.dialog_appID = true;
          }
        });
      return controls;
    },

    /** Show loading state until finished to retrieve data from API */
    async loadData() {
      this.setAppLoading(true);
      this.document_detail = {};
      this.table_defer_data = [];
      this.table_defer_ediable_data = [];
      if (
        _.isNil(this.parameter.document_cd) ||
        !_.isString(this.parameter.document_cd) ||
        this.$checker.emptyString(this.parameter.document_cd)
      ) {
        this.$notify.error(
          this.$t('tracking_information.messages.errors.missing_attachment')
        );
        this.$emit('input', false);
      } else {
        this.document_detail = _.cloneDeep(this.parameter);

        /* Load status */
        let respStatus = await this.retrieveStatusHistory(
          this.parameter.document_cd
        );
        if (respStatus.success && respStatus.data.length > 0) {
          this.table_status_history.data = respStatus.data;
          this.document_detail.status_cd = this.parameter.status_cd;
          this.document_detail.status_nm = this.parameter.status_nm;

          if (this.document_detail.status_cd == DOCUMENT_STATUS.MODIFIED) {
            this.document_detail.modified_content =
              respStatus.data[0].description;
            if (respStatus.data[0].file_name != null) {
              this.document_detail.file_name = respStatus.data[0].file_name;
            }
          } else if (this.document_detail.status_cd == DOCUMENT_STATUS.CANCEL) {
            this.note_cancel = respStatus.data[0].description;
          } else if (this.document_detail.status_cd == DOCUMENT_STATUS.DEFER) {
            this.deadline = respStatus.data[0].deadline_defer;
          }
        } else {
          this.$notify.error(respStatus.message);
        }

        /* Load defers */
        if (
          this.document_detail.status_cd == DOCUMENT_STATUS.DEFER ||
          this.document_detail.status_cd == DOCUMENT_STATUS.MODIFIED
        ) {
          let respDefer = await this.retrieveDeferHistory(
            this.parameter.document_cd
          );
          if (respDefer.success) {
            this.table_defer_data = _.cloneDeep(respDefer.data);
            this.table_defer_ediable_data = _.cloneDeep(respDefer.data);
            this.table_defer_ediable_checkbox = this.table_defer_ediable_data.filter(
              row => row.defer_check == true
            );
          } else {
            this.$notify.error(respDefer.message);
          }
        }
      }
      this.setAppLoading(false);
    },

    // CHANGE STATUS FROM DEFER TO MODIFIED
    async responseDefer() {
      this.setAppLoading(true);
      try {
        this.status_defer = {
          status_cd: DOCUMENT_STATUS.MODIFIED,
          document_cd: this.document_detail.document_cd,
          description: this.modified_info.content,
          file_name: this.modified_info.attachment_file
        };
        if (
          this.table_defer_ediable_checkbox.length ==
          this.table_defer_ediable_data.length
        ) {
          await this.updateDeferDocumentsList(
            this.table_defer_ediable_checkbox
          );
          let rs = await this.changeStatusModified(this.status_defer);
          if (rs.success) {
            this.$notify.success('Save Data Success');
            this.dialog_modified = false;
            this.modified_info = [];
            this.$refs.attachment_file.value = '';
            this.modified_info.attachment_file = '';
            this.reloadHistoryDefers();
          } else {
            this.$notify.error('Save Data Failed');
          }
        } else {
          this.$notify.warning('Please choose all');
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.setAppLoading();
      }
    },

    async uploadFile() {
      var str =
        this.document_detail.document_cd +
        '_' +
        this.document_detail.customer_nm;
      let filename = str;
      let formData = new FormData();
      formData.append('file', this.$refs.attachment_file.files[0]);
      let rs = await this.uploadFileModified({
        filename,
        formData
      });
      if (!rs.success) {
        this.$notify.error(rs.message);
      }
      return rs.success;
    },

    openWindowDialog() {
      if (this.$checker.hasText(this.modified_info.attachment_file)) {
        this.$refs.attachment_file.value = '';
        this.modified_info.attachment_file = '';
      } else {
        this.$refs.attachment_file.click();
      }
    },

    pickFile() {
      if (this.$refs.attachment_file.files.length > 0) {
        let filename = this.$refs.attachment_file.files[0].name;
        if (_.endsWith(_.toLower(filename), '.pdf')) {
          this.modified_info.attachment_file = filename;
        } else {
          this.$refs.attachment_file.value = '';
        }
      }
    },

    async viewAttachmentFile() {
      this.setAppLoading(true);
      try {
        if (
          this.document_detail.file_name == null ||
          this.document_detail.file_name.length < 0
        )
          return this.$notify.warning('Filename Invalid');
        let rs = await this.viewAttachedFile(this.document_detail.file_name);
        if (rs.success) {
          let buffer = FileUtils.base64ToArrayBuffer(rs.data);
          let file = new Blob([buffer], { type: 'application/pdf' });
          let fileURL = URL.createObjectURL(file);
          FileUtils.openNewTab(
            fileURL,
            `${this.document_detail.file_name}.pdf`
          );
          this.$notify.success(
            this.$t('global.messages.open_pdf_file.success')
          );
        } else {
          this.$$notify.warning(rs.message);
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.setAppLoading();
      }
    },

    // RELOAD HISTORY DEFER LIST AFTER CSR ADD NEW REASON AND PRESS BUTTON DEFER
    async reloadHistoryDefers() {
      // Load Documnet current After change status
      this.reload_data.document_cd = this.parameter.document_cd;
      let rs = await this.loadDocuments(this.reload_data.document_cd);
      if (!rs.success)
        return this.$notify.error(this.$t('global.messages.unknown_error'));
      this.document_detail = _.cloneDeep(rs.data[0]);

      /*Load Status history */
      let respStatus = await this.retrieveStatusHistory(
        this.parameter.document_cd
      );
      if (respStatus.success && respStatus.data.length > 0) {
        this.table_status_history.data = respStatus.data;
        this.document_detail.status_cd = rs.data[0].status_cd;
        this.document_detail.status_nm = rs.data[0].status_nm;

        if (this.document_detail.status_cd == DOCUMENT_STATUS.MODIFIED) {
          this.document_detail.modified_content = rs.data[0].description;
          this.document_detail.file_name = respStatus.data[0].file_name;
        } else if (this.document_detail.status_cd == DOCUMENT_STATUS.CANCEL) {
          this.note_cancel = respStatus.data[0].description;
        } else if (this.document_detail.status_cd == DOCUMENT_STATUS.DEFER) {
          this.deadline = respStatus.data[0].deadline_defer;
        }
      } else {
        this.$notify.error(respStatus.message);
      }
      /* Load defers */
      if (
        this.document_detail.status_cd == DOCUMENT_STATUS.DEFER ||
        this.document_detail.status_cd == DOCUMENT_STATUS.MODIFIED
      ) {
        let respDefer = await this.retrieveDeferHistory(
          this.parameter.document_cd
        );
        if (respDefer.success) {
          this.table_defer_data = _.cloneDeep(respDefer.data);
          this.table_defer_ediable_data = _.cloneDeep(respDefer.data);
          this.table_defer_ediable_checkbox = this.table_defer_ediable_data.filter(
            row => row.defer_check == true
          );
        } else {
          this.$notify.error(respDefer.message);
        }
      }
    },

    /**
     *  UPDATE HISTORY DEFER LIST AFTER SALER CHANGE STATUS REASON AND PRESS BUTTON MODIFIED
     */
    async addReasonDefer(key, record, value) {
      if (this.document_detail.status_cd == DOCUMENT_STATUS.DEFER) {
        await this.updateDeferDocuments({
          ...record,
          ...{ defer_check: value }
        });
      } else {
        this.$notify.warning(
          'This status current not Defer.Please check again!'
        );
      }
    },

    openPassConfirm() {
      this.$alert
        .info()
        .setTitle('tracking_information.alerts.title')
        .setMessage('tracking_information.alerts.pass.message', {
          agree: this.document_detail.customer_nm
        })
        .setControls(this.alert_controls.pass)
        .setCallback(this.changeToPass)
        .open();
    },

    async changeToPass(event) {
      switch (event) {
        case 'pass':
          this.setAppLoading(true);
          if (
            this.document_detail.status_cd == DOCUMENT_STATUS.MODIFIED ||
            this.document_detail.status_cd == DOCUMENT_STATUS.DISTRIBUTED
          ) {
            this.dataChangeStatusPass = {
              status_cd: DOCUMENT_STATUS.PASS,
              channel_id: this.parameter.channel_id,
              document_cd: this.document_detail.document_cd,
              id_no: this.document_detail.id_no
            };
            let rs = await this.changeStatusPass(this.dataChangeStatusPass);
            if (rs.success) {
              this.$notify.success(rs.message);
              this.reloadHistoryDefers();
            }
          } else {
            this.$notify.warning(
              'Current Status Document is not MODIFIED and DISTRIBUTED'
            );
          }
          this.setAppLoading();
          break;
        case 'close':
          this.$notify.warning(
            this.$t('tracking_information.messages.warnings.cancel_step_pass')
          );
          break;
        default: {
          this.setAppLoading();
        }
      }
    },

    async dialog_addAppID() {
      this.dataAddAppID = {
        add_app_id: this.add_app_id,
        document_cd: this.document_detail.document_cd
      };
      try {
        let rs = await this.addAppID(this.dataAddAppID);
        if (rs.success) {
          this.$notify.success(rs.message);
          this.dialog_appID = false;
          this.reloadHistoryDefers();
        } else this.$notify.error(rs.message);
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      }
    },
    openCancelConfirm() {
      this.dialog_ContactCancel = true;
    },
    async dialogInput() {
      this.setAppLoading(true);
      if (
        this.document_detail.status_cd == DOCUMENT_STATUS.MODIFIED ||
        this.document_detail.status_cd == DOCUMENT_STATUS.DISTRIBUTED
      ) {
        this.dataStatusCancel = {
          status_cd: DOCUMENT_STATUS.CANCEL,
          document_cd: this.document_detail.document_cd,
          description: this.cancel_info.input,
          file_name: ''
        };
        let rs = await this.changeStatusCancel(this.dataStatusCancel);
        if (rs.success) {
          this.reloadHistoryDefers();
          this.$notify.success(rs.message);
        }
      } else {
        this.$notify.warning(
          'Current Status Document is not MODIFIED and DISTRIBUTED'
        );
      }
      this.setAppLoading();
      this.dialog_ContactCancel = false;
    },
    closeDetailPage() {
      this.$emit('input', false);
      this.$emit('reload');
    }
  }
};
</script>

<style lang="scss" scoped>
.btn-controls {
  padding: 0 10px 10px !important;
}

.tab-title {
  padding: 10px 20px 10px;
  background: var(--v-accent-lighten2);
}

.tab-title > div:not(.spacer) {
  font-size: 20px;
  cursor: pointer;
  margin-right: 30px;
  color: var(--v-accent-darken2);
}

.tab-title > div:last-child {
  font-size: 16px;
  font-weight: 500;
  cursor: unset;
  color: var(--v-accent-darken5);
  line-height: 30px;
}

.tab-title > div:not(:last-child):hover {
  color: var(--v-secondary-base);
}

.tab-title > div.active {
  color: var(--v-primary-darken1) !important;
}
</style>
