import _ from 'lodash';
import Vue from 'vue';
import { Checker } from 'utilities';

export const APPEVENT_SPECIAL_KEYS = {
  TAB: 'onPressTab',
  ENTER: 'onPressEnter',
  ESCAPE: 'onPressEscape',
  END: 'onPressEnd',
  HOME: 'onPressHome',
  INSERT: 'onPressInsert',
  DELETE: 'onPressDelete'
};
export const APPEVENT_ACTIVATE = 'onActived';
export const APPEVENT_ALERT_INFO = 'onAlertInfo';
export const APPEVENT_ALERT_WARN = 'onAlertWarn';
export const APPEVENT_ALERT_ERROR = 'onAlertError';
export const APPEVENT_FETCH_PERIODIC = 'onFetchPeriodic';

const ACTIVATE_EVENTS = ['keyup', 'mousemove', 'mousewheel', 'click'];
const ALL_EVENTS = [
  APPEVENT_ALERT_INFO,
  APPEVENT_ALERT_WARN,
  APPEVENT_ALERT_ERROR
];

class ApplicationEvent {
  constructor(bus, { events = [], options = {} }) {
    this.$event_bus = bus;
    if (events.length > 0) {
      this.events = events;
    } else {
      this.events = ACTIVATE_EVENTS;
    }

    this.options = options;
  }

  /**
   * Listen global events
   */
  registerGlobalEvents() {
    var _event_bus = this.$event_bus;
    for (let i in this.events) {
      let event_name = this.events[i];
      if (ACTIVATE_EVENTS.includes(event_name)) {
        if (event_name === 'keyup') {
          window.addEventListener(event_name, event => {
            EventUtils.handleSpecialKey(_event_bus, event);
            // _event_bus.$emit(APPEVENT_ACTIVATE);
          });
        } else {
          // window.addEventListener(
          //   event_name,
          //   () => {
          //     _event_bus.$emit(APPEVENT_ACTIVATE);
          //   },
          //   { passive: true }
          // );
        }
      }
    }
  }

  /**
   * Run periodic job
   * @param periodic_time: Number <minutes>
   */
  runPeriodicJob(periodic_time = 5) {
    EventUtils.periodicEvent(
      this.$event_bus,
      periodic_time * 60,
      APPEVENT_FETCH_PERIODIC
    );
  }

  publishEvent = (event, data) => {
    if (Checker.hasText(event) && ALL_EVENTS.includes(event)) {
      this.$event_bus.$emit(event, data);
      return true;
    }
    return false;
  };
}

// Apply global app event
export const bus = new Vue();
const APP_EVENT = new ApplicationEvent(bus, {});
export default APP_EVENT;

class EventUtils {
  static handleSpecialKey(_event_bus, event) {
    let key_name = _.toUpper(event.key);
    if (!_.isNil(APPEVENT_SPECIAL_KEYS[key_name])) {
      _event_bus.$emit(APPEVENT_SPECIAL_KEYS[key_name]);
    }

    if (/^[A-Za-z]{1}$/.test(key_name)) {
      let event_name = '';
      if (event.ctrlKey) event_name += 'CTRL_';
      if (event.altKey) event_name += 'ALT_';
      if (event.shiftKey) event_name += 'SHIFT_';
      if (event_name == '') return;

      event_name += _.toUpper(key_name);
      _event_bus.$emit(event_name);
    }
  }

  static periodicEvent(_event_bus, time, event_name) {
    if (!_.isNil(EventUtils.JOB_ID)) clearInterval(EventUtils.JOB_ID);
    EventUtils.JOB_ID = setInterval(() => {
      _event_bus.$emit(event_name);
    }, time * 1000);
  }
}
