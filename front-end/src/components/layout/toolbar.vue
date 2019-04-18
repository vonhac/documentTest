<template>
  <v-toolbar color="primary" fixed dark app height="56">
    <div v-if="!isMobile" class="no_padding account_info">
      <div />
      <img class="logo" src="/logo.png" />
    </div>
    <v-toolbar-side-icon @click.stop="toggleDrawer" />
    <v-toolbar-title>
      {{ $t($route.meta.header) }}
    </v-toolbar-title>
    <v-spacer />

    <v-badge color="red" :class="computedClassBadge">
      <template v-if="unreadCount > 0" slot="badge">
        <span style="cursor: pointer" @click="openNotifications">
          {{ unreadCount }}
        </span>
      </template>
      <v-icon @click="openNotifications">
        mail_outline
      </v-icon>
    </v-badge>
    <v-btn v-if="isMobile" icon ripple @click="sheet = true">
      <v-icon>more_vert</v-icon>
    </v-btn>
    <template v-else>
      <v-toolbar-items class="mr-3">
        <v-btn flat class="personal" @click="$router.push('/personal')">
          <v-icon left>people</v-icon>
          {{ authUser.account_id | uppercase }}
        </v-btn>
      </v-toolbar-items>
      <v-btn icon ripple @click="exitApp">
        <v-icon>{{ $vuetify.icons.LOGOUT }}</v-icon>
      </v-btn>
    </template>
    <v-bottom-sheet v-model="sheet">
      <v-list>
        <v-list-tile @click="$router.push('/personal')">
          <v-list-tile-action>
            <v-icon color="primary">people</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>{{
              authUser.account_id | uppercase
            }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile @click="exitApp">
          <v-list-tile-action>
            <v-icon color="primary">{{ $vuetify.icons.LOGOUT }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>{{
              $t('global.labels.logout')
            }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-bottom-sheet>
  </v-toolbar>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  data() {
    return {
      sheet: false
    };
  },

  computed: {
    ...mapGetters('authentication', ['authUser']),
    ...mapGetters('notifications', ['unreadCount']),

    computedClassBadge() {
      return {
        no_select: true,
        'mt-2': this.unreadCount > 0,
        'mr-5': this.unreadCount > 0 && !this.isMobile,
        'mr-4': this.unreadCount == 0 && !this.isMobile,
        'mr-3': this.unreadCount > 0 && this.isMobile,
        'mr-2': this.unreadCount == 0 && this.isMobile
      };
    }
  },

  async created() {
    await this.getAllParameters();
  },

  methods: {
    ...mapActions('global', ['toggleDrawer']),
    ...mapActions('authentication', ['logout']),
    ...mapActions('notifications', ['openNotifications']),
    ...mapActions('configurations', ['getAllParameters']),

    exitApp() {
      this.logout();
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.account_info {
  width: 250px;
  margin-left: -24px !important;
  max-height: 100%;
  display: flex;
  border-right: 1px solid #00554a;
  user-select: none;
  position: relative;
}

.account_info div {
  position: absolute;
  width: 100%;
  height: 100%;
  opacity: 0;
  z-index: 10;
}

.logo {
  margin: 8px auto;
  height: 40px;
  filter: brightness(0) invert(1);
  -webkit-filter: brightness(0) invert(1);
}

.personal {
  background-color: var(--v-primary-darken1);
}

.personal:hover {
  background-color: var(--v-primary-lighten1);
}
</style>
