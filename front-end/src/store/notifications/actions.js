import _ from 'lodash';
import moment from 'moment';
import { STD_DATE_FORMAT } from 'core/constant';
import { RESTClient } from 'core';
import {
  UNREAD_NOTIFY,
  ALL_NOTIFY,
  SHOW_DRAWER,
  POSITION_FETCHING,
  STOP_FETCHING,
  SEARCHING_VALUE
} from './types';

export const fetchNotify = async ({ commit, getters }, options) => {
  let { page = 1, isNext = false } = options || {};
  if (!_.isNumber(page)) return;
  if (isNext && getters.allNotify.length > 0) {
    page = getters.currentPosFetching + 1;
  }

  const response = await RESTClient.get('/api/notifications', { page });
  if (response.success && _.isArray(response.data)) {
    if (_.isEmpty(response.data)) {
      commit(STOP_FETCHING, true);
      return;
    }
    commit(POSITION_FETCHING, page);

    let allNotify = _.unionBy(response.data, getters.allNotify, 'id');
    allNotify = allNotify.map(notify => {
      notify.created_date_ms = moment(
        notify.created_date,
        STD_DATE_FORMAT
      ).valueOf();
      return notify;
    });

    commit(ALL_NOTIFY, _.orderBy(allNotify, 'created_date_ms', 'desc'));
  }
  return response;
};

export const reloadFetchingNotify = ({ commit }) => {
  commit(POSITION_FETCHING);
  commit(STOP_FETCHING);

  RESTClient.get('/api/notifications', { page: 1 }).then(response => {
    if (response.success && _.isArray(response.data)) {
      commit(ALL_NOTIFY, response.data);
    }
  });
};

export const retrieveUnread = async ({ commit }) => {
  const response = await RESTClient.get('/api/notifications/unread');
  if (response.success && _.isArray(response.data)) {
    commit(UNREAD_NOTIFY, response.data);
  }
  return response;
};

export const openNotifications = ({ commit }, flag = true) => {
  if (!_.isBoolean(flag)) flag = true;
  commit(SHOW_DRAWER, flag);
};

export const changeStateNotify = ({ commit, getters }, id) => {
  if (!_.isNumber(id) || id <= 0) return;
  RESTClient.put(`/api/notifications/${id}`).then(response => {
    if (response.success) {
      let allNotify = _.cloneDeep(getters.allNotify);
      allNotify.forEach(msg => {
        if (msg.id == id) {
          msg.read = true;
          msg.read_date = moment().format(STD_DATE_FORMAT);
        }
      });
      commit(ALL_NOTIFY, allNotify);

      let unreadNotify = getters.unreadNotify.filter(msg => msg.id != id);
      commit(UNREAD_NOTIFY, unreadNotify);
    }
  });
};

export const readAllNotify = async ({ commit, getters }) => {
  const response = await RESTClient.put('/api/notifications/all-notify');
  if (response.success) {
    let allNotify = _.cloneDeep(getters.allNotify);
    allNotify.forEach(msg => (msg.read = true));
    commit(ALL_NOTIFY, allNotify);
    commit(UNREAD_NOTIFY, []);
  }
};

export const storeSearchingValue = ({ commit }, value = null) => {
  _.isString(value) ? commit(SEARCHING_VALUE, value) : commit(SEARCHING_VALUE);
};
