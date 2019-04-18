<template>
  <div :class="['number_text_filed', icon == '' ? '' : 'show']">
    <div class="icon_format">{{ icon }}</div>
    <v-text-field
      v-model="formatVal"
      v-bind="customProps"
      @keypress="preventText"
      @blur="formatText"
    />
  </div>
</template>

<script>
import _ from 'lodash';
const NTF_PROPS = ['value', 'type', 'after', 'format', 'icon'];
export default {
  props: {
    value: {
      type: Number,
      default: 0
    },

    type: {
      type: String,
      default: 'int'
    },

    after: {
      type: Number,
      default: 2
    },

    format: {
      type: String,
      default: 'en'
    },

    icon: {
      type: String,
      default: ''
    }
  },

  data() {
    return {
      formatVal: '',
      numberVal: 0
    };
  },

  computed: {
    customProps() {
      return _.omit(this.$attrs, NTF_PROPS);
    }
  },

  watch: {
    value() {
      this.numberVal = this.value;
      this.formatVal = this.formatToString(this.value);
    }
  },

  created() {
    this.numberVal = this.value;
    this.formatVal = this.formatToString(this.value);
  },

  methods: {
    formatText() {
      if (this.formatVal !== '' || this.formatVal > Number.MAX_SAFE_INTEGER) {
        try {
          this.numberVal = this.parse(this.formatVal);
          this.formatVal = this.formatToString(this.numberVal);
          this.$emit('input', this.numberVal);
        } catch (e) {
          this.formatVal = this.formatToString(this.numberVal);
        }
      } else {
        this.formatVal = this.formatToString(this.numberVal);
      }
    },

    preventText(event) {
      let isPrevent = false;
      switch (this.type) {
        case 'int':
          if (
            !(event.key >= 0 && event.key <= 9 && this.formatVal.length < 15)
          ) {
            isPrevent = true;
          }
          break;
        case 'float':
          if (
            !(
              ((event.key >= 0 && event.key <= 9) || event.key == '.') &&
              this.formatVal.length < 15
            )
          ) {
            isPrevent = true;
          }
          break;
      }

      if (isPrevent) {
        event.preventDefault();
      }
    },

    parse(str) {
      switch (this.type) {
        case 'int':
          return parseInt(str.replace(/[^0-9]/g, ''));
        case 'float':
          var val = parseFloat(str.replace(/[^0-9.]/g, '')).toFixed(this.after);
          return parseFloat(val);
      }
    },

    formatToString(val) {
      switch (this.type) {
        case 'int':
          return new Intl.NumberFormat(this.format).format(val);
        case 'float':
          return val.toFixed(this.after).replace(/\d(?=(\d{3})+\.)/g, '$&,');
      }
    }
  }
};
</script>

<style>
.number_text_filed {
  position: relative;
}

.number_text_filed .icon_format {
  position: absolute;
  left: 0;
  top: 17px;
  display: none;
}

.number_text_filed.show .icon_format {
  display: block;
}

.number_text_filed.show .v-input__control input {
  padding-left: 15px !important;
}
</style>
