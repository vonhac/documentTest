<template>
  <v-container class="app-wrapper" grid-list-md fluid pa-3>
    <v-form ref="formSearch">
      <v-layout row wrap class="block">
        <v-flex xs12>
          <v-card>
            <v-card-text>
              <v-layout row wrap>
                <v-flex xs6 sm3 md2 align-self-center>
                  <p class="sub-title no_margin">
                    {{ $t('configurations.sub_headers.deadline') }}
                  </p>
                </v-flex>
                <v-flex xs3 sm3>
                  <cd-number-text-field
                    v-model="deadline.value"
                    :suffix="$t('configurations.postfix.day')"
                  />
                </v-flex>
                <v-flex xs3 sm2 class="mt-2" text-xs-left>
                  <v-btn
                    :disabled="deadline.oldValue == deadline.value"
                    color="primary"
                    @click="saveNewDeadline"
                  >
                    {{ $t('global.buttons.save') }}
                  </v-btn>
                </v-flex>
              </v-layout>
            </v-card-text>
          </v-card>
        </v-flex>
        <v-flex xs12>
          <v-card>
            <v-card-text>
              <v-layout row wrap>
                <v-flex xs6 sm3 md2 align-self-center>
                  <p class="sub-title no_margin">
                    {{ $t('configurations.sub_headers.fetching') }}
                  </p>
                </v-flex>
                <v-flex xs3 sm3>
                  <cd-number-text-field
                    v-model="fetching.value"
                    :suffix="$t('configurations.postfix.minute')"
                  />
                </v-flex>
                <v-flex xs3 sm2 class="mt-2" text-xs-left>
                  <v-btn
                    :disabled="fetching.oldValue == fetching.value"
                    color="primary"
                    @click="updateFethingTime"
                  >
                    {{ $t('global.buttons.save') }}
                  </v-btn>
                </v-flex>
              </v-layout>
            </v-card-text>
          </v-card>
        </v-flex>
      </v-layout>
    </v-form>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  data() {
    return {
      deadline: {},
      fetching: {}
    };
  },

  computed: {
    ...mapGetters('configurations', ['getParamDeadline', 'getParamFetching'])
  },

  watch: {
    getParamDeadline: {
      handler() {
        this.deadline = {
          value: Number(this.getParamDeadline),
          oldValue: Number(this.getParamDeadline)
        };
      },
      immediate: true
    },

    getParamFetching: {
      handler() {
        this.fetching = {
          value: Number(this.getParamFetching),
          oldValue: Number(this.getParamFetching)
        };
      },
      immediate: true
    }
  },

  methods: {
    ...mapActions('configurations', [
      'setParamDeadline',
      'setParamPeriodicFetch'
    ]),

    async saveNewDeadline() {
      try {
        if (this.deadline.value > 90) {
          return this.$notify.warning(
            this.$t('configurations.messages.deadline', { no_day: 90 })
          );
        }
        let rs = await this.setParamDeadline(`${this.deadline.value}`);
        if (rs.success) {
          this.deadline.oldValue = this.deadline.value;
          this.$notify.success(rs.message);
        } else {
          this.$notify.error(rs.message);
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      }
    },

    async updateFethingTime() {
      try {
        if (this.fetching.value > 30) {
          return this.$notify.warning(
            this.$t('configurations.messages.fetching', { no_minute: 30 })
          );
        }

        let rs = await this.setParamPeriodicFetch(`${this.fetching.value}`);
        if (rs.success) {
          this.fetching.oldValue = this.fetching.value;
          this.$notify.success(rs.message);
        } else {
          this.$notify.error(rs.message);
        }
      } catch (e) {
        this.$notify.error(this.$t('global.messages.unknown_error'));
      }
    }
  }
};
</script>
