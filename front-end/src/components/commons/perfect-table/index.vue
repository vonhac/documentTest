<template>
  <div :style="loadCustomCss()">
    <div :class="['perfect_table', options.outline ? 'elevation-1' : '']">
      <v-data-table
        :headers="columns"
        :items="data_table"
        :pagination.sync="pagination"
        :sort-icon="$vuetify.icons.SORT"
        :custom-sort="sortFunction"
        hide-actions
      >
        <template slot="headers" slot-scope="props">
          <tr>
            <template v-for="header in ignoreHidden(props.headers)">
              <th
                v-if="header.value === '$checker'"
                :key="header.text"
                :style="{
                  'min-width': header.width,
                  'max-width': !options.checker_options.all_check
                    ? 'unset'
                    : header.width
                }"
              >
                <v-checkbox
                  v-show="options.checker_options.all_check"
                  v-model="all_check"
                  :ripple="false"
                  height="24"
                  class="checker_col"
                  @change="selectAll"
                />
                <template v-if="!options.checker_options.all_check">
                  {{ $t(header.text) }}
                </template>
              </th>

              <v-hover
                v-else-if="enableFilterCol(header.value)"
                :key="header.text"
              >
                <th
                  slot-scope="{ hover }"
                  class="filter column px-2 text-xs-center"
                  :style="{ 'min-width': header.width }"
                >
                  {{ $t(header.text) }}
                  <v-expand-transition
                    v-if="
                      filter_options.state &&
                        search_map[header.value] != undefined
                    "
                  >
                    <v-text-field
                      v-show="holdFitlerControl(hover, header.value)"
                      :ref="`filter_${header.value}`"
                      v-model="search_map[header.value].value"
                      :label="header.text"
                      :prefix="`${header.alias} `"
                      :append-icon="search_map[header.value].operation.icon"
                      solo
                      class="filter_wrapper"
                      @input="filterColumnToResult(header.value)"
                      @click:append="openOperationList(header.value, $event)"
                    />
                  </v-expand-transition>
                </th>
              </v-hover>

              <th
                v-else
                :key="header.text"
                :class="[
                  'column px-2 text-xs-center',
                  header.sortable
                    ? pagination.descending
                      ? 'sortable desc'
                      : 'sortable asc'
                    : '',
                  header.value === pagination.sortBy ? 'active' : ''
                ]"
                @click="changeSort(header.sortable, header.value)"
              >
                {{ $t(header.text) }}
                <v-icon v-if="header.sortable" small>
                  {{ $vuetify.icons.SORT }}
                </v-icon>
              </th>
            </template>
          </tr>
        </template>
        <template slot="items" slot-scope="props">
          <tr :class="computedDecorateRow(props.item)">
            <td
              v-for="col in ignoreHidden(columns)"
              :key="col.value"
              :style="styleForField(col, props.item[col.value])"
              :class="[
                enableFilterCol(col.value) ? 'filter' : '',
                computedDefaultClass(col.value),
                options.decorates.class[col.value] || '',
                col.value == '$drag_row' ? 'row_handle' : '',
                col.align == 'center' ? 'text-xs-center' : 'text-xs-left',
                col.value == '$actions' ? 'none-select' : ''
              ]"
              @contextmenu="handleEvent(props.item, col.value, $event, true)"
              @click="handleEvent(props.item, col.value, $event, false)"
              @dblclick="handleDBClickEvent(props.item, col.value, $event)"
              @mouseenter="hoverHandle($event, false)"
              @mouseleave="hoverHandle($event, true)"
            >
              <template v-if="col.value == '$drag_row'">
                <v-icon small>{{ $vuetify.icons.DRAG }}</v-icon>
              </template>
              <template v-else-if="col.value == '$checker'">
                <check-box
                  :id="props.item[options.key]"
                  :key="props.item[options.key]"
                  :table="table_id"
                  class="checker_col"
                  @change="
                    val => {
                      triggerSelected(val, props.item);
                      selectRow(val, props.item[options.key], true);
                    }
                  "
                />
              </template>
              <template v-else-if="col.value == '$actions'">
                <div class="layout justify-center">
                  <div
                    v-for="(action, index) in actions.row"
                    :key="index"
                    :class="[
                      'align-self-center',
                      index < actions.row.length - 1 ? 'mr-2' : ''
                    ]"
                  >
                    <template v-if="!ignoreActions(action, props.item)">
                      <v-btn
                        v-if="action.text != undefined"
                        small
                        outline
                        color="primary"
                        @click="callback(action, props.item, $event)"
                      >
                        <v-icon
                          v-if="action.icon != undefined"
                          small
                          class="mr-2"
                        >
                          {{ action.icon }}
                        </v-icon>
                        {{ action.text }}
                      </v-btn>
                      <v-icon
                        v-else
                        small
                        :color="
                          action.color != undefined ? action.color : 'primary'
                        "
                        @click="callback(action, props.item, $event)"
                      >
                        {{ action.icon }}
                      </v-icon>
                    </template>
                  </div>
                </div>
              </template>
              <template v-else-if="col.editable">
                <slot
                  :record="props.item"
                  :column="col.value"
                  name="editable"
                />
              </template>
              <template v-else>
                {{ computedCellValue(col.value, props.item[col.value]) }}
              </template>
            </td>
          </tr>
          <template v-if="showEmptyRow(props.index)">
            <template v-for="r in computedEmptyRow">
              <tr :key="r" class="empty-row">
                <td colspan="100" />
              </tr>
            </template>
          </template>
        </template>
        <template slot="no-data">
          <template v-if="options.fixed_row_page">
            <tr v-for="r in pagination.rowsPerPage" :key="r" class="empty-row">
              <td
                v-if="r == Math.floor(pagination.rowsPerPage / 2 + 0.5)"
                colspan="100"
              >
                <v-layout align-center justify-center row fill-height>
                  <div>{{ $t('global.table.message.no_data') }}</div>
                </v-layout>
              </td>
              <td v-else colspan="100" />
            </tr>
          </template>
          <tr v-else>
            <td class="empty-row" colspan="100">
              <v-layout align-center justify-center row fill-height>
                <div>{{ $t('global.table.message.no_data') }}</div>
              </v-layout>
            </td>
          </tr>
        </template>
      </v-data-table>
      <div class="table_footer px-4">
        <div
          v-if="filter_options.turn_on"
          class="inline mr-4 filtering_controller"
          @click="changeFilterController"
        >
          <v-icon :color="filter_options.state ? 'primary' : '#b1b1b1'">
            fa-filter
          </v-icon>
        </div>
        <template v-if="!options.fixed_page">
          <div class="inline mr-4">{{ $t('global.table.page_size') }}</div>
          <div class="inline page_size">
            <v-select
              v-model="pagination.rowsPerPage"
              :items="options.page_size"
            />
          </div>
        </template>
        <div class="pagging">
          <v-pagination
            v-model="pagination.page"
            :length="calcTotalPage"
            :total-visible="paggingVisibles"
          ></v-pagination>
        </div>
      </div>
    </div>
    <v-menu
      v-if="filter_options.turn_on"
      v-model="choose_model.show"
      :position-x="choose_model.x"
      :position-y="choose_model.y"
      transition="slide-y-transition"
      bottom
      left
      absolute
    >
      <v-list class="menu-v-list">
        <v-list-tile
          v-for="(operation, key) in choose_model.operations"
          :key="key"
          avatar
          @click="chooseOperation(choose_model.column, key, $event)"
        >
          <v-list-tile-action>
            <v-icon>{{ operation.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>{{ operation.text }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-menu>
    <v-menu
      v-model="menu_context.show"
      :position-x="menu_context.x"
      :position-y="menu_context.y"
      :transition="false"
      absolute
      offset-y
    >
      <v-list class="menu-v-list">
        <v-list-tile
          v-for="(item, index) in menu_context.shown_options"
          :key="index"
          @click="menuContextAction(item.code, item.callback)"
        >
          <v-list-tile-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>{{ $t(item.text) }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-menu>
  </div>
</template>

<script>
import _ from 'lodash';
import TableDragger from 'table-dragger';
import CheckBox from './checkbox';
import { Logger } from 'utilities';
import { FILTER_ULTIS } from './filter-compare';
import PERFECT_TABLE_FILTER_OPERATIONS from './filter-operations';
import {
  PERFECT_TABLE_HOVER_COLOR,
  MENU_CONTROLS,
  PERFECT_TABLE_ACTION_SCOPE,
  PERFECT_TABLE_PAGE_SIZE,
  PERFECT_TABLE_FILTER_TYPES
} from './table-constants';

const LOGGER = new Logger('PERFECT_TABLE');
const MERGE_CONFIG_DEEP_ATTR = ['checker_options', 'decorates'];
export default {
  components: {
    CheckBox
  },

  props: {
    value: {
      type: Array,
      default: () => {
        return [];
      }
    },

    headers: {
      type: Object,
      default: () => {
        return {};
      }
    },

    custom: {
      type: Object,
      default: () => {
        return {};
      }
    },

    selected: {
      type: Array,
      default: () => {
        return [];
      }
    },

    /**
     * Filter configuration structure
     * {
     *    a: 'STRING',
     *    b: 'string',
     *    c: 'BOOL',
     *    d: 'bool',
     *    e: 'NUMBER',
     *    f: 'number',
     *    g: 'DATE'
     *    h: {
     *      type: 'date',
     *      width: '100px',
     *      alias: '<NAME FOR SHOWN UP>'
     *    }
     * }
     */
    filter: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },

  data() {
    return {
      table_id: `${new Date().getTime()}`,
      reload: `${new Date().getTime()}`,

      columns: [],

      data_table: [],

      options: {
        key: null,

        sortable: [],
        /**
         * { column: String, desc: Boolean | true }
         */
        default_sort: {},
        hiddens: [],
        editables: [],

        autoNo: false,
        checker: false,
        checker_options: {
          all_check: true,
          alias_name: ''
        },
        dragable: false,

        outline: true,
        center: [],
        decorates: {
          null_value: false,
          text: {},
          class: {},
          default: '',
          hover: _.assign(_.cloneDeep(PERFECT_TABLE_HOVER_COLOR), {
            only_cell: false
          })
        },

        fixed_row_page: false,
        fixed_page: false,
        page_size: PERFECT_TABLE_PAGE_SIZE,

        /**
         * {
         *   color: String,
         *   icon: String,
         *   scope: PERFECT_TABLE_ACTION_SCOPE,
         *   text: String,
         *   text_menu: String,
         *   callback: Function(VALUE_OF_KEY, OBJECT),
         *   isHidden: Function(VALUE_OF_KEY, OBJECT) <Hidden this action with special condition>
         * }
         */
        actions: {}
      },

      pagination: {
        rowsPerPage: 5,
        page: 1,
        totalItems: this.value.length
      },

      actions: {
        row: [],
        global: [],
        click: [],
        db_click: [],
        menu_context: [],
        selected_row: []
      },

      all_check: false,
      map_checkbox: {},

      filter_options: {
        turn_on: false,
        state: false
      },
      search_map: {},
      choose_model: {
        show: false,
        x: 0,
        y: 0,
        column: '',
        current: {},
        operations: []
      },

      menu_context: {
        show: false,
        x: 0,
        y: 0,
        target_col: '',
        target_record: {},
        options: [],
        shown_options: []
      }
    };
  },

  computed: {
    paggingVisibles() {
      return this.$vuetify.breakpoint.smAndDown ? 5 : 7;
    },

    calcTotalPage() {
      let total = _.floor(
        this.pagination.totalItems / this.pagination.rowsPerPage
      );
      if (this.pagination.totalItems % this.pagination.rowsPerPage > 0) {
        total++;
      }
      return total;
    },

    computedEmptyRow() {
      let lastRows = this.data_table.length % this.pagination.rowsPerPage;
      return this.pagination.rowsPerPage - lastRows;
    }
  },

  watch: {
    value() {
      this.data_table = this.cloneDataTable(this.value);
      this.filtering();
      this.loadCheckbox();
    },

    data_table: {
      handler() {
        this.pagination.totalItems = this.data_table.length;
        for (let i in this.data_table) {
          let row = this.data_table[i];
          row.$no = Number(i) + 1;
          if (_.isNil(row[this.options.key])) {
            return LOGGER.error(`Data is not valid [${JSON.stringify(row)}]`);
          }

          let index = _.findIndex(this.value, {
            [this.options.key]: row[this.options.key]
          });
          _.assign(this.value[index], this.removeControlCol(row));
        }
      },
      deep: true
    },

    'filter_options.state': {
      handler() {
        if (this.filter_options.state) {
          this.filtering();
        } else {
          this.data_table = _.cloneDeep(this.value);
        }
      },
      deep: true
    },

    'pagination.rowsPerPage': {
      handler() {
        if (this.calcTotalPage == 0) {
          this.pagination.page = 1;
        } else if (this.pagination.page > this.calcTotalPage) {
          this.pagination.page = this.calcTotalPage;
        }
        this.reloadCheckbox();
      },
      deep: true
    },

    'pagination.page': {
      handler() {
        this.reloadCheckbox();
      },
      deep: true
    },

    selected: {
      handler() {
        this.loadCheckbox();
        this.reloadCheckbox();
      },
      deep: true
    }
  },

  created() {
    this.mergeConfigs();
    this.readColumns();
    this.verifyBrowserPermission();
    this.data_table = this.cloneDataTable(this.value);
    this.loadCheckbox();
    this.readFilterOptions();
    this.loadActions();
    this.loadPaging();
  },

  updated() {
    this.$nextTick(() => {
      if (this.data_table.length > 0) {
        this.tableDragable();
      }
    });
  },

  methods: {
    mergeConfigs() {
      let custom = _.cloneDeep(this.custom);

      _.forIn(this.options, (value, key) => {
        if (!_.isNil(custom[key]) && _.includes(MERGE_CONFIG_DEEP_ATTR, key)) {
          this.options[key] = { ...this.options[key], ...custom[key] };
          custom = _.omit(custom, key);
        }
      });
      this.options = { ...this.options, ...custom };
    },

    cloneDataTable(data) {
      let cloneData = _.cloneDeep(data);
      return cloneData.map(record => {
        if (!_.isNil(record[this.options.key])) {
          record[this.options.key] = _.toString(record[this.options.key]);
        }
        return record;
      });
    },

    readColumns() {
      if (_.isNil(this.options.key))
        return LOGGER.error('Please provide a valid column name as KEY');

      if (_.isEmpty(this.headers))
        return LOGGER.error('Please provide array to describe the header');

      if (_.isBoolean(this.options.checker) && this.options.checker) {
        this.columns.push({
          text: this.options.checker_options.alias_name,
          align: 'center',
          value: '$checker',
          width: '70px'
        });
      }
      this.columns.push({
        text: 'global.table.columns.no',
        align: 'center',
        sortable: false,
        value: '$no',
        width: '50px'
      });

      let hasHiddens = !_.isEmpty(this.options.hiddens);
      if (_.isBoolean(this.options.autoNo) && !this.options.autoNo) {
        this.options.hiddens.push('$no');
      }
      for (let column in this.headers) {
        if (hasHiddens && _.includes(this.options.hiddens, column)) {
          continue;
        }
        let editable = _.includes(this.options.editables, column);
        this.columns.push({
          text: this.headers[column],
          align: this.isTextCenter(column),
          sortable: this.isSortable(column),
          editable,
          value: column,
          width: this.calcWidthColumn(column),
          alias: this.retrieveAlias(column)
        });
      }
    },

    verifyBrowserPermission() {
      navigator.permissions.query({ name: 'clipboard-write' }).then(result => {
        if (result.state == 'granted' || result.state == 'prompt') {
          this.menu_context.options = _.concat(
            this.menu_context.options,
            MENU_CONTROLS.COPY_INTO_CLIPBORAD()
          );
        }
      });
    },

    loadCheckbox(value) {
      if (_.isBoolean(this.options.checker) && this.options.checker) {
        this.map_checkbox = {};
        for (let index in this.value) {
          let el = this.value[index];
          let keyRow = el[this.options.key];

          this.map_checkbox[keyRow] = this.readSelected(keyRow, value);
          this.selectRow(this.map_checkbox[keyRow], keyRow, null, true);
        }
      }
    },

    reloadCheckbox() {
      if (!_.isBoolean(this.options.checker) || !this.options.checker) return;
      this.$nextTick(() => {
        this.data_table.forEach(el => {
          let key = el[this.options.key];
          this.$bus.$emit(
            `select_${key}_${this.table_id}`,
            this.map_checkbox[key]
          );
        });
      });
    },

    readSelected(key, value) {
      if (_.isBoolean(value)) return value;

      if (_.isArray(this.selected)) {
        let result = this.selected.filter(row => row[this.options.key] == key);
        return result.length > 0;
      }
      return false;
    },

    selectAll(select) {
      if (_.isBoolean(select) && !select) {
        this.all_check = false;
        this.$emit('update:selected', []);
      } else {
        this.$emit('update:selected', this.collectSelectedRow(true));
      }
      for (let key in this.map_checkbox) {
        this.map_checkbox[key] = this.all_check;
      }
      this.$bus.$emit(`select_all_${this.table_id}`, this.all_check);
    },

    isAllCheck() {
      var isAll = true;
      _.forIn(this.map_checkbox, val => {
        if (!val) isAll = false;
      });
      return isAll;
    },

    selectRow(val, key, isStop, isIgonre) {
      this.map_checkbox[key] = val;
      this.reload = `${new Date().getTime()}`;

      this.all_check = this.isAllCheck();
      if (_.isNil(isStop)) {
        this.$bus.$emit(`select_${key}_${this.table_id}`, val);
      }

      if (_.isNil(isIgonre)) {
        this.$emit('update:selected', this.collectSelectedRow());
      }
    },

    collectSelectedRow(isAll) {
      if (isAll) {
        if (this.all_check) {
          return _.cloneDeep(this.data_table);
        } else {
          return [];
        }
      } else {
        let result = [];
        for (let key in this.map_checkbox) {
          if (this.map_checkbox[key]) {
            let record = _.find(this.data_table, { [this.options.key]: key });
            if (!_.isNil(record)) result.push(record);
          }
        }
        return result;
      }
    },

    readFilterOptions() {
      if (_.isObject(this.filter) && !_.isEmpty(this.filter)) {
        this.filter_options.turn_on = true;
        let filterCols = _.keys(this.filter).filter(
          key =>
            !_.isNil(this.headers[key]) &&
            !_.includes(this.options.hiddens, key)
        );
        filterCols.forEach(column => {
          let type = this.validFilterTypes(this.filter[column]);
          this.search_map[column] = {
            value: null,
            key: this.options.key,
            type,
            column,
            results: [],
            operation: PERFECT_TABLE_FILTER_OPERATIONS.DEFAULTS[type]
          };
        });
      }
    },

    loadActions() {
      if (_.isBoolean(this.options.dragable) && this.options.dragable) {
        this.columns = _.concat(
          {
            text: '',
            align: 'center',
            sortable: false,
            value: '$drag_row',
            width: '20px'
          },
          this.columns
        );
      }
      if (!_.isNil(this.options.actions) && !_.isEmpty(this.options.actions)) {
        for (let key in this.options.actions) {
          let action = this.options.actions[key];
          if (!_.isFunction(action.callback)) {
            LOGGER.error(`Action [${key}] not contain callback function`);
            continue;
          }

          let scopes = _.defaultTo(
            action.scope,
            PERFECT_TABLE_ACTION_SCOPE.RECORD
          );
          if (_.isArray(scopes)) {
            scopes.forEach(scope => this.actionRegister(scope, action));
          } else if (_.isString(scopes)) {
            this.actionRegister(scopes, action);
          }
        }
      }

      if (!_.isEmpty(this.actions.row)) {
        this.columns.push({
          text: 'global.table.columns.action',
          align: 'center',
          sortable: false,
          value: '$actions'
        });
      }
    },

    actionRegister(scope, origin, key) {
      scope = _.toLower(scope);
      let action = { ...origin, ...{ scope } };

      switch (scope) {
        case PERFECT_TABLE_ACTION_SCOPE.RECORD:
          this.actions.row.push(action);
          break;

        case PERFECT_TABLE_ACTION_SCOPE.GLOBAL:
          this.actions.global.push(action);
          break;

        case PERFECT_TABLE_ACTION_SCOPE.MENU_CONTEXT:
          this.menu_context.options.push({
            code: key,
            icon: action.icon || 'gps_fixed',
            text: action.text_menu,
            callback: action.callback,
            isHidden: action.isHidden
          });
          break;

        case PERFECT_TABLE_ACTION_SCOPE.SELECTED_ROW:
          if (!_.isBoolean(this.options.checker) || !this.options.checker) {
            LOGGER.warn(
              `The action [${key}] with selected_row scope which is ignored`
            );
            break;
          }

          this.actions.selected_row.push(action);
          break;

        case PERFECT_TABLE_ACTION_SCOPE.CLICK.ROW:
          this.actions.click.push(action);
          break;

        case PERFECT_TABLE_ACTION_SCOPE.CLICK.CELL:
          if (
            _.isString(action.target) &&
            (_.startsWith(action.target, '$') ||
              _.isNil(this.headers[action.target]))
          ) {
            break;
          }
          this.actions.click.push(action);
          break;

        case PERFECT_TABLE_ACTION_SCOPE.DB_CLICK.ROW:
          this.actions.db_click.push(action);
          break;

        case PERFECT_TABLE_ACTION_SCOPE.DB_CLICK.CELL:
          if (
            _.isString(action.target) &&
            (_.startsWith(action.target, '$') ||
              _.isNil(this.headers[action.target]))
          ) {
            break;
          }
          this.actions.db_click.push(action);
          break;
      }
    },

    ignoreActions(action, record) {
      if (_.isFunction(action.isHidden)) {
        try {
          let hidden = action.isHidden(
            record[this.options.key],
            this.removeControlCol(record)
          );
          if (_.isBoolean(hidden) && hidden == true) {
            return true;
          }
        } catch (e) {
          LOGGER.error(
            `Hidden condition has a problem with record [${JSON.stringify(
              this.removeControlCol(record)
            )}]`
          );
        }
      }

      return false;
    },

    callback(action, record, event) {
      event.preventDefault();
      event.stopPropagation();

      record = this.removeControlCol(record);
      action.callback(record[this.options.key], record, event);
    },

    handleEvent(record, col, event, rightClick) {
      let key = record[this.options.key];
      event.preventDefault();
      if (rightClick) {
        this.menu_context.show = false;
        this.menu_context.x = event.clientX;
        this.menu_context.y = event.clientY;
        this.menu_context.target_col = col;
        this.menu_context.target_record = record;

        this.loadMenuContext(record);
        this.$nextTick(() => {
          if (this.menu_context.shown_options.length == 0) return;
          this.menu_context.show = true;
        });
      } else {
        let result = this.removeControlCol(record);
        for (let index in this.actions.click) {
          let action = this.actions.click[index];
          if (action.scope == PERFECT_TABLE_ACTION_SCOPE.CLICK.CELL) {
            if (
              _.startsWith(col, '$') ||
              _.isNil(action.target) ||
              action.target !== col
            ) {
              continue;
            }
          } else {
            if (col == '$actions' || col == '$checker') continue;
          }
          action.callback(key, result, event);
        }
      }
    },

    handleDBClickEvent(record, column, event) {
      var key = record[this.options.key];
      this.actions.db_click.forEach(action => {
        let result = this.removeControlCol(record);
        if (action.scope == PERFECT_TABLE_ACTION_SCOPE.DB_CLICK.CELL) {
          if (_.startsWith(column, '$')) return;
          if (_.isNil(action.target) || action.target !== column) return;
        } else if (column == '$actions' || column == '$checker') return;
        action.callback(key, result, event);
      });
    },

    triggerSelected(checked, record) {
      var key = record[this.options.key];
      let result = this.removeControlCol(record);
      this.actions.selected_row.forEach(action => {
        action.callback(key, result, checked);
      });
    },

    loadMenuContext(record) {
      this.menu_context.shown_options = [];
      if (_.isBoolean(this.options.checker) && this.options.checker) {
        let key = record[this.options.key];
        let selectOption = MENU_CONTROLS.SELECT_ROW();
        if (this.map_checkbox[key]) {
          selectOption = MENU_CONTROLS.UNSELECT_ROW();
        }
        this.menu_context.shown_options.push(selectOption);
      }
      this.menu_context.options.forEach(option => {
        if (!this.ignoreActions(option, record))
          this.menu_context.shown_options.push(option);
      });
    },

    menuContextAction(code, callback) {
      let record = this.removeControlCol(this.menu_context.target_record);
      let key = record[this.options.key];
      switch (code) {
        case '$COPY_CELL':
          if (_.startsWith(this.menu_context.target_col, '$')) {
            this.copyToClipBoard(null);
          } else {
            this.copyToClipBoard(record[this.menu_context.target_col]);
          }
          break;
        case '$COPY_ROW':
          this.copyToClipBoard(JSON.stringify(record));
          break;
        case '$SELECT_ROW':
          this.selectRow(!this.map_checkbox[key], key);
          this.triggerSelected(
            this.map_checkbox[key],
            this.menu_context.target_record
          );
          break;
        default:
          if (_.isFunction(callback)) {
            callback(key, record);
          }
          break;
      }
    },

    removeControlCol(record) {
      let needRemove = [];
      for (let key in record) {
        if (_.startsWith(key, '$')) {
          needRemove.push(key);
        }
      }
      return _.omit(record, needRemove);
    },

    copyToClipBoard(data) {
      navigator.permissions.query({ name: 'clipboard-write' }).then(result => {
        if (result.state == 'granted' || result.state == 'prompt') {
          if (_.isNil(data)) data = '<NO_DATA>';
          navigator.clipboard.writeText(data);
        }
      });
    },

    tableDragable() {
      if (_.isBoolean(this.options.dragable) && this.options.dragable) {
        var dragger = TableDragger(this.$el.getElementsByTagName('table')[0], {
          mode: 'row',
          dragHandler: '.row_handle',
          onlyBody: true,
          animation: 300
        });
        dragger.on('drop', () => {});
      }
    },

    openOperationList(column, event) {
      event.preventDefault();
      this.focusTextField(`filter_${column}`);

      this.choose_model = {
        show: false,
        column,
        current: this.search_map[column].operation,
        operations:
          PERFECT_TABLE_FILTER_OPERATIONS[this.search_map[column].type]
      };
      this.choose_model.x = event.clientX;
      this.choose_model.y = event.clientY;
      this.$nextTick(() => {
        this.choose_model.show = true;
      });
    },

    chooseOperation(column, choice, event) {
      event.preventDefault();
      this.focusTextField(`filter_${column}`);
      let type = this.search_map[column].type;
      this.search_map[column].operation =
        PERFECT_TABLE_FILTER_OPERATIONS[type][choice];
      this.filterColumnToResult(column);
    },

    filtering() {
      if (!this.filter_options.turn_on) return;

      _.forIn(this.search_map, (value, column) => {
        this.filterColunm(column);
      });
      this.getResult();
    },

    filterColunm(column) {
      let condition = this.search_map[column];
      this.search_map[column].results = [];
      if (!FILTER_ULTIS.ignore[condition.type](condition.value)) {
        this.value.forEach(el => {
          if (!_.isNil(el[column])) {
            FILTER_ULTIS.filter(this.search_map[column], el);
          }
        });
      }
    },

    filterColumnToResult(column) {
      this.filterColunm(column);
      this.getResult();
    },

    changeFilterController() {
      this.filter_options.state = !this.filter_options.state;

      if (this.filter_options.state) {
        this.filtering();
      } else {
        this.cleanFilterCache();
        this.data_table = _.cloneDeep(this.value);
      }
    },

    cleanFilterCache() {
      _.forIn(this.search_map, condition => {
        condition.value = null;
        condition.results = [];
      });
    },

    getResult() {
      let accepts = null;
      _.forIn(this.search_map, filter => {
        if (FILTER_ULTIS.ignore[filter.type](filter.value)) {
          filter.results = [];
        } else {
          if (accepts == null) {
            accepts = filter.results;
            return;
          }

          accepts = _.intersection(accepts, filter.results);
        }
      });
      if (accepts != null) {
        this.data_table = this.value.filter(el =>
          _.includes(accepts, el[this.options.key])
        );
      } else {
        this.data_table = _.cloneDeep(this.value);
      }

      // this.selectAll(false);
      this.reloadCheckbox();
    },

    validFilterTypes(colType) {
      if (_.isNil(colType)) return PERFECT_TABLE_FILTER_TYPES[0];
      let ignoreCaseVal = '';
      if (_.isObject(colType)) {
        if (_.isNil(colType.type)) return PERFECT_TABLE_FILTER_TYPES[0];
        else return _.toUpper(colType.type);
      } else {
        ignoreCaseVal = _.toUpper(colType);
      }
      if (PERFECT_TABLE_FILTER_TYPES.includes(ignoreCaseVal)) {
        return ignoreCaseVal;
      } else {
        return PERFECT_TABLE_FILTER_TYPES[0];
      }
    },

    loadCustomCss() {
      let hexRegex = /^#([A-Fa-f0-9]{3}|[A-Fa-f0-9]{6})$/;
      let options = _.cloneDeep(PERFECT_TABLE_HOVER_COLOR);

      if (_.isNil(this.options.decorates)) {
        this.options.decorates = {
          hover: {
            only_cell: false
          }
        };
      } else if (_.isNil(this.options.decorates.hover)) {
        this.options.decorates.hover = {
          only_cell: false
        };
      } else {
        let custom = _.cloneDeep(this.options.decorates.hover);
        if (_.isString(custom.color) && hexRegex.test(custom.color)) {
          options.COLOR = custom.color;
        }
        if (_.isString(custom.background) && hexRegex.test(custom.background)) {
          options.BACKGROUND = custom.background;
        }
      }

      return {
        '--t-empty-row': PERFECT_TABLE_HOVER_COLOR.BACKGROUND,
        '--t-hover-text': options.COLOR,
        '--t-hover': options.BACKGROUND
      };
    },

    hoverHandle(event, isHover) {
      let el = event.target;

      if (isHover) {
        if (this.options.decorates.hover.only_cell) {
          el.className = el.className.replace(' t-hover', '');
        } else {
          this.hoverRowHandle(el, isHover);
        }
      } else {
        if (this.options.decorates.hover.only_cell) {
          el.className += ' t-hover';
        } else {
          this.hoverRowHandle(el, isHover);
        }
      }
    },

    hoverRowHandle(el, isHover) {
      let childs = Array.from(el.parentNode.children);
      for (let index in childs) {
        if (isHover) {
          childs[index].className = childs[index].className.replace(
            ' t-hover',
            ''
          );
        } else {
          childs[index].className += ' t-hover';
        }
      }
    },

    focusTextField(control) {
      let component = this.$refs[control];
      if (component != undefined && component[0] != undefined) {
        let inputControl = component[0].$el.getElementsByTagName('input');
        inputControl[0].focus();
      }
    },

    ignoreHidden(headers) {
      return headers.filter(
        col =>
          _.isEmpty(this.options.hiddens) ||
          !_.includes(this.options.hiddens, col.value)
      );
    },

    isSortable(col) {
      return (
        _.isArray(this.options.sortable) &&
        _.includes(this.options.sortable, col)
      );
    },

    isTextCenter(col) {
      return _.isArray(this.options.center) &&
        _.includes(this.options.center, col)
        ? 'center'
        : 'left';
    },

    calcWidthColumn(col) {
      if (
        !_.isNil(this.filter[col]) &&
        _.isObject(this.filter[col]) &&
        !_.isNil(this.filter[col].width)
      ) {
        return this.filter[col].width;
      } else {
        return '100px';
      }
    },

    retrieveAlias(col) {
      if (
        _.isNil(this.filter) ||
        _.isNil(this.filter[col]) ||
        _.isNil(this.filter[col].alias)
      )
        return this.headers[col];

      return this.filter[col].alias;
    },

    enableFilterCol(col) {
      if (_.isNil(this.filter) || _.isNil(this.filter[col])) return false;

      return true;
    },

    computedDefaultClass(column) {
      if (!_.startsWith(column, '$')) return this.options.decorates.default;
      return '';
    },

    computedDecorateRow(record) {
      this.reload ? true : false;

      let key = record[this.options.key];
      var result = [];
      if (_.isBoolean(this.options.checker) && this.options.checker) {
        if (this.map_checkbox[key]) {
          result.push('t-hover');
        }
      }

      if (
        _.isNil(this.options.decorates.row) ||
        !_.isArray(this.options.decorates.row)
      ) {
        return result;
      }

      this.options.decorates.row.forEach(el => {
        if (_.isFunction(el.condition) && el.condition(record)) {
          result.push(el.class);
        }
      });
      return result;
    },

    showEmptyRow(index) {
      index++;
      return (
        this.options.fixed_row_page &&
        this.pagination.page == this.calcTotalPage &&
        index == this.pagination.totalItems % this.pagination.rowsPerPage
      );
    },

    holdFitlerControl(hover, column) {
      let component = this.$refs[`filter_${column}`];
      let isFocus = false;
      if (component != undefined && component[0] != undefined) {
        isFocus = component[0].isFocused;
      }
      let val = this.search_map[column].value;
      if (val == '') {
        this.search_map[column].value = null;
      }

      return hover || isFocus || this.search_map[column].value != null;
    },

    changeSort(sortable, column) {
      if (this.filter_options.state && !_.isNil(this.search_map[column]))
        return;

      if (!sortable) return;
      if (this.pagination.sortBy === column) {
        this.pagination.descending = !this.pagination.descending;
      } else {
        this.pagination.sortBy = column;
        this.pagination.descending = false;
      }
      this.reloadCheckbox();
    },

    computedCellValue(col, val) {
      if (
        _.isNil(this.options.decorates) ||
        _.isNil(this.options.decorates.text) ||
        _.isNil(this.options.decorates.text[col])
      ) {
        return val;
      }
      return this.options.decorates.text[col](val);
    },

    styleForField(col, val) {
      let style = {};
      if (col.value == '$checker') {
        style = {
          'min-width': col.width,
          'max-width': col.width
        };
      }

      if (
        this.options.decorates.null_value &&
        !_.startsWith(col.value, '$') &&
        _.isNil(val)
      ) {
        style.background = '#fff5f5';
      }
      return style;
    },

    loadPaging() {
      if (_.isArray(this.options.page_size)) {
        if (this.options.page_size.length == 0) {
          this.options.page_size = PERFECT_TABLE_PAGE_SIZE;
        } else if (this.options.page_size.length == 1) {
          this.options.fixed_page = true;
        }
      } else {
        this.options.page_size = PERFECT_TABLE_PAGE_SIZE;
      }
      this.pagination.rowsPerPage = this.options.page_size[0];

      if (
        !_.isNil(this.options.default_sort) &&
        !_.isNil(this.headers[this.options.default_sort.column])
      ) {
        this.pagination.descending = _.defaultTo(
          this.options.default_sort.desc,
          false
        );
        this.pagination.sortBy = this.options.default_sort.column;
      } else if (!_.isEmpty(this.sortable)) {
        this.pagination.descending = false;
        this.pagination.sortBy = this.sortable[0];
      } else {
        this.pagination.descending = false;
        this.pagination.sortBy = this.options.key;
      }
    },

    sortFunction(items, index, isDescending) {
      items = _.sortBy(items, [index]);
      if (isDescending) _.reverse(items);
      items.forEach((row, index) => {
        row.$no = Number(index) + 1;
      });
      return items;
    }
  }
};
</script>

<style>
.perfect_table {
  overflow: hidden;
}

.perfect_table .no-padding {
  padding: 0 !important;
}

.perfect_table .no-margin {
  margin: 0 !important;
}

.perfect_table .none-select {
  user-select: none;
}

.perfect_table .v-datatable thead tr {
  background-color: #e1e1e1;
}

.perfect_table .v-datatable thead tr th.filter,
.perfect_table .v-datatable tbody tr td.filter {
  padding: 0 !important;
}

.perfect_table .v-datatable tbody tr td.filter {
  padding: 0 24px !important;
}

.perfect_table .v-datatable thead tr:first-child {
  border-bottom: 2px solid var(--v-primary-base) !important;
}

.perfect_table .v-datatable thead tr th {
  color: var(--v-primary-lighten1);
  font-size: 13px;
  font-weight: 600;
}

.perfect_table .v-datatable thead th:hover,
.perfect_table .v-datatable thead th.sortable:hover {
  color: var(--v-primary-base);
}

.perfect_table .theme--light.v-table tbody tr:hover {
  background-color: #00000000;
}

.perfect_table .v-datatable tbody tr.t-hover,
.perfect_table .v-datatable tbody tr td.t-hover {
  color: var(--t-hover-text) !important;
  background-color: var(--t-hover) !important;
}

.perfect_table .v-datatable td:not(:last-child) {
  border-right: 1px solid #0000001f !important;
}

.perfect_table .v-datatable thead th.sortable.active i,
.perfect_table .v-datatable thead th.sortable.active {
  color: var(--v-primary-base) !important;
}

.perfect_table .v-datatable .checker_col {
  height: 24px;
  justify-content: center;
}

.perfect_table .v-datatable .checker_col .v-input--selection-controls__input {
  margin: 0;
}

.perfect_table .table_footer {
  width: 100%;
  height: 52px;
  line-height: 52px;
  border-top: 1px solid #0000001f;
  background-color: white;
  font-size: 12px;
  font-weight: 600;
  color: #0000008a;
  position: relative;
  display: flex;
}

.perfect_table .table_footer .pagging {
  position: absolute;
  right: 16px;
  top: 0;
  height: 52px;
  padding: 5px 0;
}

.perfect_table .table_footer .pagging .v-pagination__item {
  line-height: 34px;
}

.perfect_table .page_size {
  display: flex;
}

.perfect_table .table_footer .filtering_controller {
  cursor: pointer;
  user-select: none;
  font-size: 16px;
}

.perfect_table .page_size .v-select {
  padding: 0;
  margin: 0;
  max-width: 70px;
  align-self: center;
}

.perfect_table .table_footer .v-input__slot {
  margin: 0;
}

.perfect_table .table_footer .v-text-field__details {
  display: none;
}

.perfect_table .table_footer .v-select__selection {
  font-size: 12px;
}

.perfect_table .empty-row {
  border: 0 !important;
}

.perfect_table .empty-row td {
  background-color: var(--t-empty-row);
}

.perfect_table .filter {
  position: relative;
}

.perfect_table .filter .filter_wrapper {
  bottom: 4px;
  left: 0;
  position: absolute;
  font-size: 500;
}

.perfect_table .filter .filter_wrapper .v-input__slot input,
.perfect_table .filter .filter_wrapper .v-input__slot label {
  font-weight: 400;
  font-size: 15px;
}

.perfect_table .filter .filter_wrapper .v-input__slot {
  margin: 0;
}

.perfect_table .filter .filter_wrapper .v-text-field__details {
  display: none;
}

.perfect_table .filter .filter_wrapper .v-icon {
  font-size: 16px;
  opacity: 0.9 !important;
}

.menu-v-list {
  max-height: 240px;
  overflow-y: overlay;
  padding: 0 !important;
}

.menu-v-list .v-list__tile {
  height: 40px;
  padding-left: 0 !important;
}

.menu-v-list .v-list__tile:hover .v-icon,
.menu-v-list .v-list__tile:hover .v-list__tile__title {
  color: var(--v-warning-base) !important;
}

.menu-v-list .v-list__tile__action {
  min-width: unset;
  justify-content: center;
  width: 40px;
}

.menu-v-list .v-icon {
  font-size: 16px !important;
}

.menu-v-list .v-list__tile__title {
  font-size: 13px !important;
}
</style>
