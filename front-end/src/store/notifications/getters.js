import _ from 'lodash';

export const allNotify = state => {
  if (_.isArray(state.all_notify)) return state.all_notify;
  return [];
};
export const unreadNotify = state => {
  if (_.isArray(state.unread_notify)) return state.unread_notify;
  return [];
};
export const unreadCount = state => {
  if (_.isArray(state.unread_notify)) return state.unread_notify.length;
  return 0;
};
export const isShowController = state => state.show_drawer;
export const currentPosFetching = state => state.pos_fetching;
export const stopFetching = state => state.stop_fetching;
export const getSearchValue = state => state.searching_value;
