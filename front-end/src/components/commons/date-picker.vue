<template>
  <v-menu
    ref="menu"
    v-model="menu"
    :close-on-content-click="false"
    :nudge-right="40"
    lazy
    transition="scale-transition"
    offset-y
    full-width
    min-width="290px"
  >
    <slot :model="show" name="activator">
      <v-text-field
        slot="activator"
        v-model="show"
        :label="label"
        :prepend-icon="icon"
        :append-icon="options.clear && hasText ? 'cancel' : ''"
        readonly
        @click:append="clearValue"
      />
    </slot>
    <v-date-picker
      v-model="picker"
      :allowed-dates="allowedDate"
      :color="options.color"
      :readonly="readonly || locked"
      no-titl
      scrollable
      @input="pickDate"
    />
  </v-menu>
</template>

<script>
import _ from 'lodash';
import moment from 'moment';
import { Logger } from 'utilities';

const DEFAULT_OPTIONS = {
  color: 'primary',
  clear: false,
  date_format: {
    model: 'MM/DD/YYYY',
    shown_up: 'MM/DD/YYYY'
  }
};
const V_PICKER_FORMAT = 'YYYY-MM-DD';
const LOGGER = new Logger('DATE_PICKER');

export default {
  props: {
    value: {
      type: String,
      default: ''
    },

    label: {
      type: String,
      default: ''
    },

    icon: {
      type: String,
      default: ''
    },

    greater: {
      type: String,
      default: ''
    },

    smaller: {
      type: String,
      default: ''
    },

    equal: {
      type: Boolean,
      default: true
    },

    readonly: {
      type: Boolean,
      default: false
    },

    custom: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },

  data() {
    return {
      locked: false,

      menu: false,

      show: '',

      picker: '',

      hasText: false,

      options: {}
    };
  },

  watch: {
    value() {
      this.update(this.value);
      this.hasText = false;
      if (_.isString(this.value) && this.value.trim() !== '') {
        this.hasText = true;
      }
    }
  },

  created() {
    this.mergeOptions();
    this.update(this.value);
  },

  methods: {
    mergeOptions() {
      this.options = _.assign(_.cloneDeep(DEFAULT_OPTIONS), this.custom);
      this.options.date_format = _.assign(
        _.cloneDeep(DEFAULT_OPTIONS.date_format),
        this.custom.date_format
      );

      if (_.isObject(this.options.date_format)) {
        if (!_.isString(this.options.date_format.model)) {
          this.locked = true;
          return LOGGER.warn(
            `Please provide right date-format [${
              this.options.date_format.model
            }] for OUTBOUND`
          );
        }
        if (!_.isString(this.options.date_format.shown_up)) {
          this.locked = true;
          return LOGGER.warn(
            `Please provide right date-format [${
              this.options.date_format.shown_up
            }] for INBOUND`
          );
        }
        return;
      }
      this.locked = true;
    },

    clearValue() {
      this.$emit('input', null);
      this.update(null);
    },

    update(val) {
      if (this.locked == true) return;

      if (this.isValidDate(val)) {
        this.show = this.convertToShownUp(val, this.options.date_format.model);
        this.picker = this.convertToPicker(val);
        this.hasText = true;
      } else {
        this.show = '';
        this.picker = null;
        this.hasText = false;
      }
    },

    pickDate() {
      this.menu = false;
      this.show = this.convertToShownUp(this.picker, V_PICKER_FORMAT);
      this.$emit('input', this.convertToUpdate(this.picker));
    },

    convertToUpdate(date) {
      if (!date) return null;

      let momentDate = moment(date, V_PICKER_FORMAT);
      if (momentDate.isValid()) {
        return momentDate.format(this.options.date_format.model);
      }
      return null;
    },

    convertToPicker(date) {
      if (!date) return null;

      let momentDate = moment(date, this.options.date_format.model);
      if (momentDate.isValid()) {
        return momentDate.format(V_PICKER_FORMAT);
      }
      return null;
    },

    convertToShownUp(date, format) {
      if (!date) return '';
      let momentDate = moment(date, format);
      if (momentDate.isValid()) {
        return momentDate.format(this.options.date_format.shown_up);
      }
      return '';
    },

    isValidDate(date) {
      let momentDate = moment(date, this.options.date_format.model);
      return momentDate.isValid();
    },

    allowedDate(val) {
      let more = false;
      let less = false;

      if (_.isNil(this.greater) || this.greater == '') {
        more = true;
      } else {
        if (this.isValidDate(this.greater)) {
          more = this.equal
            ? this.createDate(val).isSameOrAfter(this.createDate(this.greater))
            : this.createDate(val).isAfter(this.createDate(this.greater));
        } else {
          more = false;
        }
      }

      if (_.isNil(this.smaller) || this.smaller == '') {
        less = true;
      } else {
        if (this.isValidDate(this.smaller)) {
          less = this.equal
            ? this.createDate(val).isSameOrBefore(this.createDate(this.smaller))
            : this.createDate(val).isBefore(this.createDate(this.smaller));
        } else {
          less = false;
        }
      }
      return more && less;
    },

    createDate(date) {
      let momentDate = moment(date, this.options.date_format.model);
      return momentDate;
    }
  }
};
</script>
