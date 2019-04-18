<template>
  <v-toolbar height="56" color="primary no_select">
    <v-toolbar-side-icon v-if="!hiddenCloseIcon" @click="$emit('close')">
      <v-icon color="white">{{ $vuetify.icons.CLOSE }}</v-icon>
    </v-toolbar-side-icon>
    <v-toolbar-title class="white--text">
      {{ title }}
    </v-toolbar-title>
    <v-spacer />
    <v-badge color="red" :class="computedClassBadge">
      <template v-if="unreadCount > 0" slot="badge">
        <span>{{ unreadCount }}</span>
      </template>
      <v-icon color="white">
        mail_outline
      </v-icon>
    </v-badge>
  </v-toolbar>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  props: {
    hiddenCloseIcon: {
      type: Boolean,
      default: false
    },

    title: {
      type: String,
      default: ''
    }
  },

  computed: {
    ...mapGetters('notifications', ['unreadCount']),

    computedClassBadge() {
      return {
        'mt-2': this.unreadCount > 0,
        no_select: true,
        'mr-2': this.unreadCount > 0 && !this.isMobile,
        'mr-4': this.unreadCount > 0 && this.isMobile
      };
    }
  }
};
</script>
