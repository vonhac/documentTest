<template>
  <div>
    <v-navigation-drawer v-model="drawer" v-bind="attrsDrawer">
      <Header :title="$t('notifications.header')" @close="hiddenControl" />
      <v-tabs
        centered
        active-class="primary_color"
        height="48"
        class="tab_control"
      >
        <v-tabs-slider color="primary" />
        <v-tab @click="isUnread = false">All messages</v-tab>
        <v-tab @click="isUnread = true">Unread messages</v-tab>
      </v-tabs>
      <v-layout
        v-show="isUnread"
        wrap
        row
        pb-3
        class="message_wrapper no_padding"
      >
        <template v-for="group in groupNotify(unreadNotify)">
          <v-flex :key="`unread_${group.time}`" xs12 class="notify_group px-3">
            {{ moment(group.time).format('DD/MM/YYYY') }}
          </v-flex>
          <template v-for="notify in group.array">
            <v-flex
              :key="`${notify.id_no}_${notify.id}`"
              class="notify text-truncate text-no-wrap"
              xs12
              px-3
              @click="viewDetail(notify)"
            >
              <p
                :class="[
                  'no_margin notify_status',
                  !notify.read ? 'active' : ''
                ]"
              />
              <p class="ma-0 ml-3" v-html="notify.message" />
            </v-flex>
          </template>
        </template>
        <v-flex v-if="unreadCount > 0" xs12 text-xs-center text-sm-left>
          <v-btn
            :loading="waiting_btns.read_all"
            :disabled="waiting_btns.read_all"
            flat
            small
            color="primary"
            @click="readAll"
          >
            {{ $t('notifications.buttons.ignore') }}
          </v-btn>
        </v-flex>
      </v-layout>
      <v-layout
        v-show="!isUnread"
        wrap
        row
        pb-3
        class="message_wrapper no_padding"
      >
        <template v-for="group in groupNotify(allNotify)">
          <v-flex :key="group.time" xs12 class="notify_group px-3">
            {{ moment(group.time).format('DD/MM/YYYY') }}
          </v-flex>
          <template v-for="notify in group.array">
            <v-flex
              :key="notify.id"
              class="notify text-truncate text-no-wrap"
              xs12
              px-3
              @click="viewDetail(notify)"
            >
              <p
                :class="[
                  'no_margin notify_status',
                  !notify.read ? 'active' : ''
                ]"
              />
              <p class="ma-0 ml-3" v-html="notify.message" />
            </v-flex>
          </template>
        </template>
        <v-flex xs12 text-xs-center text-sm-left>
          <v-btn
            v-show="!stopFetching"
            :loading="waiting_btns.fetching"
            :disabled="waiting_btns.fetching"
            flat
            small
            color="primary"
            @click="loadMoreNotify"
          >
            {{
              allNotify.length == 0
                ? $t('notifications.buttons.reload_fetching')
                : $t('notifications.buttons.fetch_more')
            }}
          </v-btn>
        </v-flex>
      </v-layout>
    </v-navigation-drawer>
    <v-navigation-drawer v-model="drawerDetail" v-bind="attrsDrawer">
      <Header :title="$t('notifications.detail_header')" hidden-close-icon />
      <v-layout row wrap px-3 pt-4 pb-3 class="body-2 row-data">
        <v-flex xs4 v-text="$t('notifications.labels.id_no')" />
        <v-flex
          xs8
          text-xs-right
          class="primary_color"
          v-text="notifyDetail.id_no"
        />
      </v-layout>
      <v-layout row wrap px-3 py-3 class="body-2 row-data">
        <v-flex xs4 v-text="$t('notifications.labels.read_date')" />
        <v-flex xs8 text-xs-right v-text="notifyDetail.read_date" />
      </v-layout>
      <v-layout row wrap px-3 py-3 class="body-2 row-data">
        <v-flex xs4 v-text="$t('notifications.labels.created_date')" />
        <v-flex xs8 text-xs-right v-text="notifyDetail.created_date" />
      </v-layout>
      <v-layout row wrap px-3 pt-3 pb-4 class="body-2 row-data">
        <v-flex xs4 v-text="$t('notifications.labels.message')" />
        <v-flex xs8 text-xs-right v-html="notifyDetail.message" />
      </v-layout>
      <v-layout row wrap>
        <v-flex xs6 text-xs-left>
          <v-btn small flat color="primary" @click="drawerDetail = false">
            {{ $t('notifications.buttons.back') }}
          </v-btn>
        </v-flex>
        <v-flex xs6 text-xs-right>
          <v-btn color="primary" @click="goToDetail">
            {{ $t('notifications.buttons.detail') }}
          </v-btn>
        </v-flex>
      </v-layout>
    </v-navigation-drawer>
  </div>
</template>

<script>
import _ from 'lodash';
import moment from 'moment';
import Header from './header';
import { mapActions, mapGetters } from 'vuex';
import { STD_DATE_FORMAT } from 'core/constant';
import { ROUTES_NAMING } from 'router';

export default {
  components: {
    Header
  },

  data() {
    return {
      moment,
      isUnread: false,
      drawerDetail: false,
      notifyDetail: {},

      waiting_btns: {
        read_all: false,
        fetching: false
      }
    };
  },

  computed: {
    ...mapGetters('notifications', [
      'unreadCount',
      'unreadNotify',
      'allNotify',
      'isShowController',
      'stopFetching'
    ]),

    drawer: {
      get() {
        return this.isShowController;
      },

      set(val) {
        this.openNotifications(val);
      }
    },

    attrsDrawer() {
      let width = 400;
      if (this.$vuetify.breakpoint.xsOnly)
        width = this.$vuetify.breakpoint.width;
      if (this.$vuetify.breakpoint.smOnly) width = 300;

      return {
        height: this.$vuetify.breakpoint.height,
        'mobile-break-point': this.$vuetify.breakpoint.thresholds.sm,
        width,
        fixed: true,
        right: true,
        'disable-resize-watcher': true,
        class: 'app-wrapper overflow_hidden'
      };
    }
  },

  async created() {
    await this.fetchNotify();
    await this.retrieveUnread();
  },

  methods: {
    ...mapActions('notifications', [
      'fetchNotify',
      'retrieveUnread',
      'changeStateNotify',
      'openNotifications',
      'readAllNotify',
      'reloadFetchingNotify',
      'storeSearchingValue'
    ]),

    groupNotify(array) {
      let result = [];
      array.forEach(row => {
        let time = Number.NaN;
        try {
          time = moment(row.created_date, 'DD/MM/YYYY').valueOf();
        } catch (e) {
          time = Number.NaN;
        }

        if (_.isNumber(time)) {
          let target = _.find(result, { time });
          if (_.isNil(target)) {
            result.push({
              time,
              array: [row]
            });
          } else {
            target.array.push(row);
          }
        }
      });
      return result;
    },

    async readAll() {
      this.waiting_btns.read_all = true;
      await this.readAllNotify();
      this.waiting_btns.read_all = false;
    },

    async loadMoreNotify() {
      this.waiting_btns.fetching = true;
      await this.fetchNotify({ isNext: true });
      this.waiting_btns.fetching = false;
    },

    viewDetail(notify) {
      if (!notify.read) {
        this.changeStateNotify(notify.id);
      }
      let clone = _.cloneDeep(notify);
      if (_.isNil(clone.read_date))
        clone.read_date = moment().format(STD_DATE_FORMAT);
      this.notifyDetail = clone;
      this.drawerDetail = true;
    },

    hiddenControl() {
      this.drawerDetail = false;
      this.openNotifications(false);
      this.reloadFetchingNotify();
    },

    goToDetail() {
      this.storeSearchingValue(this.notifyDetail.id_no);
      // TODO: checking permission before move to tracking page
      if (this.$route.name != ROUTES_NAMING.TRACKING_INFORMATION) {
        this.$router.push({ name: ROUTES_NAMING.TRACKING_INFORMATION });
      }
    }
  }
};
</script>

<style scoped>
.notify {
  border-bottom: 1px solid #e1e1e1;
  line-height: 50px !important;
  color: #555;
  cursor: pointer;
}

.notify p {
  display: inline-block;
}

.notify:hover {
  background-color: #f1f1f1;
}

.notify:first-child {
  border-top: 2px solid #e1e1e1;
}

.notify_group {
  background-color: #e1e1e1;
  font-weight: 500;
  line-height: 30px;
}

.message_wrapper {
  display: block;
  height: calc(100% - 108px);
  overflow-x: hidden;
  overflow-y: scroll;
}

.tab_control {
  border-bottom: 3px solid #999;
}

.notify_status {
  width: 10px;
  height: 10px;
  border-radius: 5px;
  background-color: #b1b1b1;
}

.notify_status.active {
  background-color: var(--v-success-base);
}

.row-data div:first-child {
  font-weight: 100;
}
</style>
