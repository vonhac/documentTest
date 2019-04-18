<template>
  <v-container class="app-wrapper" grid-list-xs fluid pa-3>
    <v-card>
      <v-card-text>
        <p class="sub-title no_margin">Personal Information</p>
        <v-layout wrap row>
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              v-model="authUser.account_id"
              :label="$t('personal.labels.account')"
              readonly
            />
          </v-flex>
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              v-model="authUser.fullname"
              :label="$t('personal.labels.name')"
              readonly
            />
          </v-flex>
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              v-model="authUser.department"
              :label="$t('personal.labels.department')"
              readonly
            />
          </v-flex>
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              v-model="authUser.branch_id"
              :label="$t('personal.labels.branch')"
              readonly
            />
          </v-flex>
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              v-model="authUser.area"
              :label="$t('personal.labels.area')"
              readonly
            />
          </v-flex>
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              v-model="authUser.team"
              :label="$t('personal.labels.team')"
              readonly
            />
          </v-flex>
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              v-model="authUser.super_visor_id"
              :label="$t('personal.labels.super_visor_id')"
              readonly
            />
          </v-flex>
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              v-model="authUser.position_company_id"
              :label="$t('personal.labels.position_company_id')"
              readonly
            />
          </v-flex>
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              v-model="authUser.level"
              :label="$t('personal.labels.level')"
              readonly
            />
          </v-flex>
          <v-flex xs12 sm4 offset-sm1>
            <v-checkbox
              v-model="authUser.admin"
              :label="$t('personal.labels.admin')"
              :true-value="1"
              :false-value="0"
              :ripple="false"
              color="primary"
              readonly
            />
          </v-flex>
        </v-layout>
      </v-card-text>
    </v-card>
    <v-card class="mt-3">
      <v-card-text>
        <p class="sub-title no_margin">Change password</p>
        <v-layout wrap row>
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              ref="password"
              v-model="changePassReq.password"
              :label="$t('login_page.attrs.password')"
              :rules="validationRules.password"
              type="password"
              @keypress.enter="changePass"
            />
          </v-flex>
          <v-flex hidden-xs-only sm6 />
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              ref="newPassword"
              v-model="changePassReq.new_password"
              :label="$t('login_page.attrs.new_pass')"
              :rules="validationRules.newPassword"
              type="password"
              @keypress.enter="changePass"
            />
          </v-flex>
          <v-flex hidden-xs-only sm6 />
          <v-flex xs12 sm4 offset-sm1>
            <v-text-field
              ref="confirmedPassword"
              v-model="changePassReq.verify_pass"
              :label="$t('login_page.attrs.verify_pass')"
              :rules="validationRules.confirm"
              type="password"
              @keypress.enter="changePass"
            />
          </v-flex>
          <v-flex hidden-xs-only sm6 />
          <v-flex xs12 sm10 offset-sm1 mt-2>
            <v-btn
              :loading="loadingBtn"
              color="primary"
              style="margin-left: 0px"
              @click="changePass"
            >
              {{ $t('global.buttons.save') }}
            </v-btn>
          </v-flex>
        </v-layout>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import _ from 'lodash';
import { RESTClient } from 'core';
import { mapGetters } from 'vuex';
import SHA256 from 'sha256';

export default {
  data() {
    return {
      changePassReq: {
        username: null,
        password: null,
        new_password: null,
        verify_pass: null
      },
      loadingBtn: false,
      validationRules: {
        password: [
          v => v != '' || `${this.$t('login_page.attrs.password')} is required`
        ],
        newPassword: [
          v => v != '' || `${this.$t('login_page.attrs.new_pass')} is required`,
          v =>
            v == null ||
            v != this.changePassReq.password ||
            `${this.$t('login_page.attrs.new_pass')} must be different to old`,
          v => v == null || v.length >= 8 || 'Minimum 8 characters',
          v =>
            v == null || /[A-Z]+/.test(v) || 'At least 1 upper case character',
          v => v == null || /[0-9]+/.test(v) || 'At least 1 digit',
          v =>
            v == null ||
            /[#?!@$%^&*-+.]+/.test(v) ||
            'At least special charater in #?!@$%^&*-+.'
        ],
        confirm: [
          v =>
            v == this.changePassReq.new_password ||
            'Confirmed value do not match new password'
        ]
      }
    };
  },

  computed: {
    ...mapGetters('authentication', ['authUser'])
  },

  created() {
    this.changePassReq.username = this.authUser.account_id;
  },

  methods: {
    validate() {
      if (
        _.isNil(this.changePassReq.password) ||
        _.trim(this.changePassReq.password) === ''
      ) {
        this.$refs.password.focus();
        return false;
      }

      let newPassword = this.changePassReq.new_password;
      if (
        _.isNil(newPassword) ||
        _.trim(newPassword) === '' ||
        newPassword === this.changePassReq.password ||
        newPassword.length < 8 ||
        !/[A-Z]+/.test(newPassword) ||
        !/[0-9]+/.test(newPassword) ||
        !/[#?!@$%^&*-+.]+/.test(newPassword)
      ) {
        this.$refs.newPassword.focus();
        return false;
      }

      if (this.changePassReq.new_password != this.changePassReq.verify_pass) {
        this.$refs.confirmedPassword.focus();
        return false;
      }

      return true;
    },

    async changePass() {
      if (!this.validate()) return;
      this.setFeatureLoading(true);
      this.loadingBtn = true;
      try {
        let response = await RESTClient.put('/user-password', {
          username: this.changePassReq.username,
          password: SHA256(this.changePassReq.password),
          new_password: SHA256(this.changePassReq.new_password)
        });
        if (response.success) {
          this.$notify.success(response.message);
          this.changePassReq = {
            ...this.changePassReq,
            ...{
              password: null,
              new_password: null,
              verify_pass: null
            }
          };
        } else {
          this.$notify.error(response.message);
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      } finally {
        this.$refs.password.focus();
        this.loadingBtn = false;
        this.setFeatureLoading();
      }
    }
  }
};
</script>
