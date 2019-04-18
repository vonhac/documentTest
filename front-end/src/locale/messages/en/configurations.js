export default {
  header: 'Configuration parameters',

  sub_headers: {
    deadline: 'Deadline parameter',
    fetching: 'Notify fetching time'
  },

  labels: {
    deadline: 'Deadline'
  },

  postfix: {
    day: 'Days',
    minute: 'Minutes'
  },

  messages: {
    deadline: 'The value of deadline must be smaller than {no_day} days',
    fetching:
      'The value of fetching time must be smaller than {no_minute} minutes'
  }
};
