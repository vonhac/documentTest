<template>
  <div>
    <div v-show="isAppLoading" class="waiting application">
      <v-progress-circular
        :color="loading_config.color"
        :size="loading_config.size"
        :width="loading_config.width"
        indeterminate
      />
    </div>
    <template v-if="isFeaturePage">
      <v-app>
        <navigate /><toolbar /><notifications />
        <v-content
          :style="drawerToggled ? { 'padding-left': `${width_menu}px` } : {}"
          class="page-container"
        >
          <div v-show="isFeatureLoading" class="waiting feature">
            <v-progress-circular
              :color="loading_config.color"
              :size="loading_config.size"
              :width="loading_config.width"
              :style="positionFeatureLoading"
              indeterminate
            />
          </div>
          <v-fade-transition mode="out-in"><router-view /> </v-fade-transition>
        </v-content>
        <alert /><notify />
      </v-app>
    </template>
    <template v-else>
      <transition>
        <keep-alive> <router-view /> </keep-alive>
      </transition>
    </template>
  </div>
</template>

<script>
import _ from 'lodash';
import APP_EVENT from 'core/app-events';
import { mapState, mapGetters, mapActions } from 'vuex';
import Navigate from './navigate';
import Toolbar from './toolbar';
import Notifications from './notifications';
import { Alert, Notify } from '../commons';
import { APPEVENT_FETCH_PERIODIC } from 'core/app-events';

import 'core/theme/default.styl';
import 'material-design-icons-iconfont/dist/material-design-icons.css';

import 'assets/css/global.css';
import 'assets/css/custom.css';
import 'assets/scss/style.scss';

export default {
  components: {
    Navigate,
    Toolbar,
    Alert,
    Notify,
    Notifications
  },

  data() {
    return {
      width_menu: 250,

      loading_config: {
        size: 100,
        width: 5,
        color: 'blue-grey'
      }
    };
  },

  computed: {
    ...mapState('global', ['drawerToggled']),
    ...mapGetters('global', ['isAppLoading', 'isFeatureLoading']),
    ...mapGetters('configurations', ['getParamFetching']),

    /**
     *  With drawer is shown, postion is moved right ${width_menu / 2} px
     */
    positionFeatureLoading() {
      let style = {};
      style.top = this.loading_config.size / 2;
      style.left = this.loading_config.size / 2;

      if (this.drawerToggled) {
        style.left -= this.width_menu / 2;
      }

      return {
        top: `calc(50% - ${style.top}px`,
        left: `calc(50% - ${style.left}px`
      };
    },

    isFeaturePage() {
      return this.$route.meta.requiresAuth;
    }
  },

  watch: {
    getParamFetching: {
      handler: 'listenFecthEvent',
      immediate: true
    }
  },

  /**
   * Register all default events
   * Clean all loading state
   */
  async created() {
    this.setAppLoading(true);
    await this.firstLoad();

    APP_EVENT.registerGlobalEvents();

    this.setAppLoading();
    this.setFeatureLoading();
  },

  methods: {
    ...mapActions('authentication', ['firstLoad']),
    ...mapActions('notifications', ['fetchNotify', 'retrieveUnread']),

    listenFecthEvent() {
      this.$bus.$off(APPEVENT_FETCH_PERIODIC);
      if (
        _.isNil(this.getParamFetching) ||
        !_.isNumber(Number(this.getParamFetching))
      )
        return;

      APP_EVENT.runPeriodicJob(this.getParamFetching);

      this.$bus.$on(APPEVENT_FETCH_PERIODIC, () => {
        if (this.isFeaturePage) {
          this.fetchNotify();
          this.retrieveUnread();
        }
      });
    }
  }
};
</script>

<style>
.page-container {
  background-color: #f1f1f1aa;
  height: calc(100%);
}

.waiting {
  width: 100%;
  height: 100%;
  background-color: #f1f1f1aa;
  position: fixed;
}

.waiting > div {
  margin: auto;
}

.waiting.application {
  z-index: 999;
}

.waiting.feature {
  position: absolute;
  height: 100%;
  z-index: 1;
}

.waiting.feature > div {
  position: fixed;
  z-index: 2;
}
</style>
