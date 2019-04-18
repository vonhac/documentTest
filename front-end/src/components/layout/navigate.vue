<template>
  <v-navigation-drawer
    v-model="drawer"
    :width="width"
    disable-resize-watcher
    :class="{
      'navigation-drawer': !isMobile
    }"
    fixed
  >
    <v-toolbar v-if="isMobile" height="56" flat class="logo">
      <img src="/logo.png" />
    </v-toolbar>
    <v-divider></v-divider>

    <v-list class="no_padding" dense>
      <v-divider />
      <div v-for="(item, i) in menus" :key="i">
        <v-list-group
          v-if="item.childs"
          :prepend-icon="item.icon"
          :group="item.group"
          no-action
          :active-class="
            active.group == item.group ? 'primary_color' : 'no_active_group'
          "
        >
          <v-list-tile slot="activator">
            <v-list-tile-title class="no_select">{{
              $t(item.text)
            }}</v-list-tile-title>
          </v-list-tile>
          <v-list-tile
            v-for="(childItem, j) in item.childs"
            :key="j"
            ripple="ripple"
            :class="[
              active.tab == childItem.name ? 'active_tab primary_color' : ''
            ]"
            @click="goTo(childItem.path)"
          >
            <v-list-tile-title class="no_select">{{
              $t(childItem.text)
            }}</v-list-tile-title>
          </v-list-tile>
        </v-list-group>
        <v-list-tile
          v-else
          ripple="ripple"
          :class="[active.tab == item.name ? 'active_tab primary_color' : '']"
          @click="goTo(item.path)"
        >
          <v-list-tile-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title class="no_select">{{
              $t(item.text)
            }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </div>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import ROUTES from 'router/routes';
import { mapState, mapActions } from 'vuex';

export default {
  props: {
    width: {
      type: Number,
      default: 250
    }
  },

  data() {
    return {
      active: {},
      menus: []
    };
  },

  computed: {
    ...mapState('global', ['drawerToggled']),

    drawer: {
      get() {
        return this.drawerToggled;
      },

      set(val) {
        this.toggleDrawer(val);
      }
    }
  },

  watch: {
    $route(to) {
      this.active = {
        tab: to.name,
        group: to.meta.group
      };
    }
  },

  created() {
    if (this.isMobile) this.toggleDrawer(false);

    this.readListMenuInRoutes();
    this.active = {
      tab: this.$route.name,
      group: this.$route.meta.group
    };
  },

  methods: {
    ...mapActions('global', ['toggleDrawer']),

    goTo(path) {
      this.$router.push({ path: path });
    },

    readListMenuInRoutes() {
      for (let i in ROUTES) {
        let route = ROUTES[i];
        if (route.meta && route.meta.menu) {
          let item = {
            text: route.meta.title,
            icon: route.meta.icon || 'done',
            path: route.path
          };

          if (!route.meta.group) {
            item.name = route.name;
          } else if (route.meta.group && route.meta.super) {
            let childItems = this.readChildrenMenus(route.meta.group);
            if (childItems.length > 0) {
              item.childs = childItems;
              item.group = route.meta.group;
            }
          } else {
            continue;
          }

          this.menus.push(item);
        }
      }
    },

    readChildrenMenus(group) {
      let childItems = [];
      for (let i in ROUTES) {
        let route = ROUTES[i];

        if (route.meta && !route.meta.super && route.meta.group === group) {
          childItems.push({
            text: route.meta.title,
            name: route.name,
            group: route.meta.group,
            path: route.path
          });
        }
      }

      return childItems;
    }
  }
};
</script>

<style scoped>
.navigation-drawer {
  top: 56px;
}

.avatar_img {
  border: 2px solid var(--v-primary-base);
}

.active_tab {
  background: #e1e1e1;
}

.active_tab i {
  color: #00695c;
}

.logo {
  background-color: var(--v-primary-base) !important;
}

.logo img {
  margin: 0 auto;
  height: 40px;
  filter: brightness(0) invert(1);
  -webkit-filter: brightness(0) invert(1);
}
</style>
