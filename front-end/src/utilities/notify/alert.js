import APP_EVENT from 'core/app-events';
import { Checker } from 'utilities';
import {
  APPEVENT_ALERT_INFO,
  APPEVENT_ALERT_WARN,
  APPEVENT_ALERT_ERROR
} from 'core/app-events';

const OPTIONS = {
  ESCAPE_TO_CLOSE: false,
  INTERVAL_UPDATE_MESSAGE: false,
  INTERVAL_TIME_UPDATE_MESSAGE: 990 // interval each 1s
};

export default class {
  static CONFIRM(callback) {
    YES_NO_ALERT.setCallback(choose => {
      callback(Checker.hasText(choose) && choose == 'true');
    }).open();
  }

  static info() {
    return new AlertBuilder(APPEVENT_ALERT_INFO);
  }

  static warn() {
    return new AlertBuilder(APPEVENT_ALERT_WARN);
  }

  static error() {
    return new AlertBuilder(APPEVENT_ALERT_ERROR);
  }
}

class AlertBuilder {
  constructor(event) {
    this.event_code = event;
  }

  setTitle(title, args) {
    if (Checker.hasText(title)) {
      this.title = {
        code: title,
        args
      };
      return this;
    }
    return null;
  }

  setMessage(message, args) {
    if (Checker.hasText(message)) {
      this.message = {
        code: message,
        args
      };
      return this;
    } else if (Checker.isFunction(message)) {
      this.message = {
        func: message,
        args
      };
      return this;
    }
    return null;
  }

  /**
   * Declear buttons to control this alert
   * {
   *   <control_key>: {
   *     text: 'global.alert.default_confirm.yes_btn',
   *     color: 'primary',
   *     style: 'NORMAL' | 'OUTLINE' | 'NONE',
   *     dark: true
   *   }
   * }
   * @param {*} controls
   */
  setControls(controls) {
    if (controls == null || !Checker.isObject(controls)) {
      return null;
    }
    this.controls = controls;
    return this;
  }

  setCallback(callback, data) {
    if (callback == null || !Checker.isFunction(callback)) {
      return null;
    }
    this.callback = callback;
    this.data = data;
    return this;
  }

  setOptions(options) {
    if (options == null || !Checker.isObject(options)) {
      return null;
    }
    this.options = options;
    return this;
  }

  prepare() {
    if (this.title == undefined)
      this.title = {
        code: 'global.alert.title'
      };

    if (this.message == undefined) return false;

    if (this.controls == undefined) this.controls = {};

    if (this.callback == undefined) this.callback = () => {};

    let defaults = Object.assign({}, OPTIONS);
    if (this.options == undefined) {
      this.options = defaults;
    } else {
      this.options = Object.assign(defaults, this.options);
    }

    return true;
  }

  open() {
    if (!this.prepare())
      throw new TypeError('Please provide a message before open alert.');

    APP_EVENT.publishEvent(this.event_code, this);
  }
}

const YES_NO_ALERT = new AlertBuilder(APPEVENT_ALERT_INFO);
YES_NO_ALERT.setTitle('global.alert.default_confirm.title')
  .setMessage('global.alert.default_confirm.message')
  .setControls({
    true: {
      text: 'global.alert.default_confirm.yes_btn',
      color: 'primary',
      style: 'normal',
      dark: true
    },
    false: {
      text: 'global.alert.default_confirm.no_btn',
      color: 'info'
    }
  });
