import Vue from 'vue';
import _ from 'lodash';
import UpdateData from './update-data';
import { mapActions } from 'vuex';

Vue.mixin(UpdateData);
Vue.mixin({
  data() {
    return {
      LISTENER_KEYBOARD_FUNC: {}
    };
  },

  computed: {
    isMobile: {
      get() {
        return this.$vuetify.breakpoint.smAndDown;
      },
      set() {}
    }
  },

  beforeDestroy() {
    if (_.isEmpty(this.LISTENER_KEYBOARD_FUNC)) return;

    _.forIn(this.LISTENER_KEYBOARD_FUNC, (callbacks, event_name) => {
      if (_.isFunction(callbacks)) {
        this.$bus.$off(event_name, callbacks);
      } else if (_.isArray(callbacks)) {
        callbacks.forEach(func => this.$bus.$off(event_name, func));
      }
    });
  },

  methods: {
    ...mapActions('global', [
      'setAppLoading',
      'setFeatureLoading',
      'registerFetch',
      'unregisterFetch'
    ]),

    /**
     * A global configuration to listen combination event from keyboard
     * @param { key: Char | required, ctrl: Boolean, alt: Boolean, shift: Boolean } target
     * @param Function callback
     */
    registerKeyCombination(
      target = { key: '', ctrl: false, alt: false, shift: false },
      callback,
      options
    ) {
      if (!_.isFunction(callback)) return false;
      if (!/^[A-Za-z]{1}$/.test(target.key)) return false;
      let event_name = '';
      if (target.ctrl) event_name += 'CTRL_';
      if (target.alt) event_name += 'ALT_';
      if (target.shift) event_name += 'SHIFT_';
      if (event_name == '') return false;

      event_name += _.toUpper(target.key);
      const execFunction = () => callback(options);

      if (_.isNil(this.LISTENER_KEYBOARD_FUNC[event_name])) {
        this.LISTENER_KEYBOARD_FUNC[event_name] = [execFunction];
      } else {
        this.LISTENER_KEYBOARD_FUNC[event_name].push(execFunction);
      }

      this.$bus.$on(event_name, execFunction);
      return true;
    }
  }
});

export * from './date-formater';
