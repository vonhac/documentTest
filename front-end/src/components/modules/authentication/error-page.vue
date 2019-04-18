<template>
  <v-app class="dark">
    <v-content>
      <v-container class="bg_color" fluid fill-height>
        <v-layout align-center justify-center column class="error_content">
          <v-flex
            :style="computedCssCode"
            md12
            class="error_code font-weight-light"
          >
            {{ information.code }}
          </v-flex>
          <v-flex
            :style="{
              'font-size': isMobile ? '15px' : '25px'
            }"
            md12
            class="error_message"
          >
            {{ $t(information.message) }}
          </v-flex>
          <v-flex md12 class="error_btn">
            <v-btn
              outline
              :color="information.btn_color"
              @click="goTo(information.redirect)"
            >
              <v-icon left>{{ information.btn_icon }}</v-icon>
              {{ information.btn_text }}
            </v-btn>
          </v-flex>
        </v-layout>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      error_map: {
        401: {
          code: 401,
          message: 'error_page.401',
          btn_icon: this.$vuetify.icons.LOGIN,
          btn_text: 'LOGIN',
          btn_color: 'blue-grey',
          redirect: '/login'
        },
        403: {
          code: 403,
          message: 'error_page.403',
          btn_icon: this.$vuetify.icons.HOME,
          btn_text: 'GO HOME',
          btn_color: '#00695c',
          redirect: '/'
        },
        404: {
          code: 404,
          message: 'error_page.404',
          btn_icon: this.$vuetify.icons.HOME,
          btn_text: 'GO HOME',
          btn_color: '#00695c',
          redirect: '/'
        },
        500: {
          code: 500,
          message: 'error_page.500',
          btn_icon: this.$vuetify.icons.HOME,
          btn_text: 'GO HOME',
          btn_color: '#00695c',
          redirect: '/'
        }
      },

      information: {}
    };
  },

  computed: {
    computedCssCode() {
      return {
        'font-size': this.isMobile ? '80px' : '150px',
        'max-height': this.isMobile ? '80px' : '150px',
        'line-height': this.isMobile ? '80px' : '150px'
      };
    }
  },

  created() {
    this.information = this.error_map[this.$route.meta.code];
  },

  methods: {
    goTo(path) {
      this.$router.push({ path });
    }
  }
};
</script>

<style scoped>
.error_code {
  text-align: center;
  margin-bottom: 20px;
}

.error_message {
  text-transform: uppercase;
  max-height: 30px;
  text-align: center;
  color: #999;
}

.bg_color {
  background-color: #e5e5e5;
}

.error_content {
  max-height: 450px;
}

.error_btn {
  margin-top: 15px;
  padding: 10px 40px 0;
  border-top: 1px solid #999;
}
</style>
