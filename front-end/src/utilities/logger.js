import _ from 'lodash';
import moment from 'moment';

const LOGGER_TARGET = console;
const DEBUG_FLAG = 'DEBUG_MODE';
const DEBUG_LEVELS = {
  ERROR: 0,
  INFO: 1,
  WARN: 2,
  DEBUG: 3
};
const getDate = () => moment().format('MM/DD/YYYY H:MM:SS');
const combineMessage = (lv, target, message) => {
  let result = `[${lv}] : ${getDate()} :`;
  if (!_.isNil(target)) result += ` ${target} :`;
  return result + ` ${message}`;
};

export default class {
  constructor(target) {
    this.isDevMode = process.env.NODE_ENV == 'development';
    this.debugLevel = DEBUG_LEVELS.ERROR;

    if (_.isString(target)) this.target = target;
    let level = localStorage.getItem(DEBUG_FLAG);
    if (!_.isNil(level) && !_.isNil(DEBUG_LEVELS[level])) {
      this.debugLevel = DEBUG_LEVELS[level];
    }
  }

  error(message) {
    LOGGER_TARGET.log(combineMessage('ERROR', this.target, message));
  }

  info(message) {
    if (this.isDevMode || this.debugLevel >= DEBUG_LEVELS.INFO)
      LOGGER_TARGET.log(combineMessage('INFO', this.target, message));
  }

  warn(message) {
    if (this.isDevMode || this.debugLevel >= DEBUG_LEVELS.WARN)
      LOGGER_TARGET.log(combineMessage('WARN', this.target, message));
  }

  debug(message) {
    if (this.debugLevel == DEBUG_LEVELS.DEBUG)
      LOGGER_TARGET.log(combineMessage('DEBUG', this.target, message));
  }
}
