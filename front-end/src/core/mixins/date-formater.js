import _ from 'lodash';
import moment from 'moment';
import { STD_DATE_FORMAT } from 'core/constant';

export const DATE_FORMATER = {
  methods: {
    YYYYMMDD: date => {
      if (_.isNil(date)) return moment().format('YYYY-MM-DD');
      if (moment.isMoment(date)) return date.format('YYYY-MM-DD');
      return date;
    },

    DDMMYYYY: date => {
      if (_.isNil(date)) return moment().format('DD/MM/YYYY');
      if (moment.isMoment(date)) return date.format('DD/MM/YYYY');
      return date;
    },

    MMDDYYYY: date => {
      if (_.isNil(date)) return moment().format('MM/DD/YYYY');
      if (moment.isMoment(date)) return date.format('MM/DD/YYYY');
      return date;
    },

    FULLDATETIME: date => {
      if (_.isNil(date)) return moment().format(STD_DATE_FORMAT);
      if (moment.isMoment(date)) return date.format(STD_DATE_FORMAT);
      return date;
    }
  }
};
