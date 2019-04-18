<template>
  <v-app class="dark">
    <v-content>
      <v-container
        :class="{
          bg_color: !$vuetify.breakpoint.xsOnly,
          bg_width_color: $vuetify.breakpoint.xsOnly,
          no_padding: $vuetify.breakpoint.xsOnly
        }"
        fluid
        fill-height
        :align-start="$vuetify.breakpoint.xsOnly"
        justify-center
        grid-list-md
      >
        <v-card
          :width="computedWidth"
          class="wrapper_content"
          :height="computedHeight"
          :class="{
            no_shadow: $vuetify.breakpoint.xsOnly,
            'pt-5': $vuetify.breakpoint.xsOnly
          }"
        >
          <v-card-text class="no_padding">
            <v-layout row fill-height wrap>
              <v-flex md12 class>
                <v-layout row fill-height wrap>
                  <v-flex
                    xs10
                    offset-xs1
                    :style="{ color: $vuetify.theme.primary }"
                    class="headline welcome"
                  >
                    Document Management Tool
                  </v-flex>
                  <v-flex xs10 offset-xs1 class="no_padding">
                    <v-text-field
                      ref="username"
                      v-model="username"
                      :rules="rules.username"
                      :prepend-icon="$vuetify.icons.USER"
                      placeholder="Username"
                      browser-autocomplete="off"
                      autofocus
                      @keypress.enter="loginForm"
                    />
                  </v-flex>
                  <v-flex xs10 offset-xs1 class="no_padding">
                    <v-text-field
                      ref="password"
                      v-model="password"
                      :rules="rules.password"
                      :prepend-icon="$vuetify.icons.PASSWORD"
                      v-bind="visiblePassword"
                      :type="visible && isMobile ? 'text' : 'password'"
                      placeholder="Password"
                      browser-autocomplete="off"
                      @keypress.enter="loginForm"
                      @click:append="visible = !visible"
                    />
                  </v-flex>
                  <v-flex xs10 offset-xs1 class="no_padding mt-3">
                    <v-layout
                      justify-space-between
                      align-center
                      row
                      fill-height
                    >
                      <v-flex>
                        <v-btn
                          small
                          flat
                          :color="$vuetify.theme.primary"
                          class="no_margin"
                          @click="forgotPassword"
                        >
                          forgot password
                        </v-btn>
                      </v-flex>
                      <v-flex style="text-align:right">
                        <v-btn
                          class="no_margin_hori"
                          outline
                          :color="$vuetify.theme.primary"
                          @click="loginForm"
                        >
                          <v-icon left>{{ $vuetify.icons.BTN_LOGIN }}</v-icon>
                          login
                        </v-btn>
                      </v-flex>
                    </v-layout>
                  </v-flex>
                </v-layout>
              </v-flex>
            </v-layout>
          </v-card-text>
          <v-card-text :class="['waiting', waiting ? 'show' : '']">
            <v-layout align-center justify-center fill-height>
              <v-progress-circular
                v-if="login_response == ''"
                indeterminate
                :size="100"
                color="primary"
              ></v-progress-circular>
              <template v-else>
                <v-layout row wrap>
                  <v-flex xs12 class="text-xs-center login_response title">
                    {{ login_response }}
                  </v-flex>
                  <v-flex xs12 class="text-xs-center">
                    <v-btn color="primary" outline @click="reLogin">
                      <v-icon left> {{ $vuetify.icons.RELOAD }} </v-icon>
                      retry
                    </v-btn>
                  </v-flex>
                </v-layout>
              </template>
            </v-layout>
          </v-card-text>
        </v-card>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  data() {
    return {
      username: '',
      password: '',
      remember_me: false,
      visible: false,

      tab_login: 1,

      waiting: false,
      login_response: '',

      rules: {
        username: [
          v => !!v || 'Username is required',
          v => v.length >= 4 || 'Username must be at least 5 characters'
        ],
        password: [v => !!v || 'Password is required']
      }
    };
  },

  computed: {
    visiblePassword() {
      if (this.$vuetify.breakpoint.xsOnly) {
        return {
          'append-icon': this.visible ? 'visibility' : 'visibility_off'
        };
      }
      return {};
    },

    computedWidth() {
      return this.$vuetify.breakpoint.xsOnly
        ? this.$vuetify.breakpoint.width
        : 600;
    },

    computedHeight() {
      return this.$vuetify.breakpoint.xsOnly
        ? this.$vuetify.breakpoint.height
        : null;
    }
  },

  created() {
    this.wait(1000);
  },

  methods: {
    ...mapActions('authentication', ['login']),

    wait(timeout = 0) {
      var _self = this;
      _self.waiting = true;
      if (timeout > 0) {
        setTimeout(() => {
          _self.waiting = false;
        }, timeout);
      }
    },

    async loginForm() {
      if (
        this.$checker.hasText(this.username) &&
        this.$checker.hasText(this.password)
      ) {
        this.wait();
        let response = await this.login({
          username: this.username,
          password: this.password,
          remember_me: this.remember_me
        });
        if (response.success) {
          this.$router.push('/');
          this.login_response = '';
        } else {
          this.login_response = response.message;
        }
      } else {
        this.$checker.emptyString(this.username)
          ? this.$refs.username.focus()
          : this.$refs.password.focus();
      }
    },

    reLogin() {
      this.password = '';
      this.remember_me = false;
      this.waiting = false;
      this.login_response = '';
      this.$refs.password.focus();
    },

    forgotPassword() {}
  }
};
</script>

<style scoped>
.bg_color {
  background-color: #e5e5e5;
}

.bg_width_color {
  background-color: #fff;
}

.wrapper_content {
  position: relative;
  padding: 50px 0 30px;
}

.welcome {
  padding: 0 0 20px !important;
}

.last_row {
  border-top: 1px solid #ccc;
  padding: 15px 0 20px !important;
}

.waiting {
  z-index: -1;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  background: white;
  transform: scale(0.5, 0.5);

  transition: all 0.2s ease-out;
}

.waiting.show {
  opacity: 1;
  transform: scale(1, 1);
  z-index: 1000;
}

.login_response {
  line-height: 50px !important;
  color: var(--v-error-base);
}
</style>
