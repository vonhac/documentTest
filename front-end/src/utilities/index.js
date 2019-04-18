import * as Checker from './validator';
import * as FileUtils from './file-utils';
import DateFormatter from './date-formatter';
import Notification from './notify/notification';
import Alert from './notify/alert';
import CommonIcons from './global-icons';
import Logger from './logger';

const Notify = new Notification();

export * from './common';

export {
  CommonIcons,
  Checker,
  DateFormatter,
  Notify,
  Alert,
  Logger,
  FileUtils
};
