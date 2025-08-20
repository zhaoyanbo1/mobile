<template>
  <base-layout>
    <view class="p-4 bg-neutral-50 min-h-screen">
      <!-- 顶部标题和添加按钮 -->
      <view class="flex items-center justify-between mb-4">
        <text class="text-2xl font-bold text-primary-800">提醒事项</text>
        <button
          class="bg-primary-500 text-white rounded-full p-2 shadow-md"
          @click="goToAddReminder"
        >
          <uni-icons type="plus" size="24" color="white" />
        </button>
      </view>

      <!-- 月份日历视图 -->
      <view class="mb-4">
        <view class="flex justify-between items-center mb-2">
          <button class="text-primary-500" @click="prevMonth">上一月</button>
          <text class="text-lg font-semibold text-neutral-800">{{ year }}年{{ month + 1 }}月</text>
          <button class="text-primary-500" @click="nextMonth">下一月</button>
        </view>
        <view class="grid grid-cols-7 gap-1 text-center text-sm text-neutral-600">
          <text>日</text>
          <text>一</text>
          <text>二</text>
          <text>三</text>
          <text>四</text>
          <text>五</text>
          <text>六</text>
        </view>
        <view class="grid grid-cols-7 gap-1 text-center">
          <view
            v-for="(day, index) in calendarDays"
            :key="index"
            class="p-2 rounded-lg cursor-pointer"
            :class="{
              'text-neutral-400': !day.inCurrentMonth,
              'bg-primary-500 text-white': selectedDate === day.dateStr,
              'hover:bg-primary-100': day.inCurrentMonth && selectedDate !== day.dateStr
            }"
            @click="selectDate(day.dateStr)"
          >
            <text>{{ day.day }}</text>
            <uni-badge
              v-if="day.reminderCount > 0"
              :text="day.reminderCount"
              size="small"
              type="danger"
              class="mt-1 mx-auto"
            />
          </view>
        </view>
      </view>

      <!-- 选中日期的提醒事项列表 -->
      <scroll-view
        scroll-y
        class="h-[50vh]"
        @refresherrefresh="handleRefresh"
        refresher-enabled
      >
        <view v-if="loading" class="flex justify-center py-8">
          <uni-icons type="spinner-cycle" size="28" class="animate-spin text-primary-500" />
        </view>

        <view v-else-if="reminders.length === 0" class="flex flex-col items-center justify-center py-16">
          <uni-icons type="calendar" size="48" color="#BDBDBD" />
          <text class="text-neutral-500 mt-4">暂无提醒事项</text>
        </view>

        <view v-else>
          <view
            v-for="item in reminders"
            :key="item.reminder_item_id"
            class="bg-white rounded-xl shadow-md p-4 mb-4"
          >
            <view class="flex justify-between items-start">
              <text class="text-lg font-semibold text-primary-800">{{ item.title }}</text>
              <view
                class="p-1 rounded-full"
                :class="item.is_completed ? 'bg-success-100' : 'bg-warning-100'"
                @click.stop="toggleComplete(item)"
              >
                <uni-icons
                  :type="item.is_completed ? 'checkmarkempty' : 'circle'"
                  size="18"
                  :color="item.is_completed ? 'success' : 'warning'"
                />
              </view>
            </view>
            <text class="block text-sm text-neutral-600 mt-1">{{ formatDateTime(item.reminder_time) }}</text>
            <text v-if="item.description" class="block text-sm text-neutral-500 mt-2">{{ item.description }}</text>
          </view>
        </view>
      </scroll-view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();
const today = new Date();

const year = ref(today.getFullYear());
const month = ref(today.getMonth()); // 0-based
const selectedDate = ref(formatDate(today));
const calendarDays = ref([]);
const reminders = ref([]);
const loading = ref(false);
let userId = null;

function formatDate(date) {
  return date.toISOString().slice(0, 10);
}

function daysInMonth(y, m) {
  return new Date(y, m + 1, 0).getDate();
}

function getFirstWeekday(y, m) {
  return new Date(y, m, 1).getDay();
}

async function getLoginUser() {
  const res = await proxy.$cf.login.getLoginUser();
  if (res.success) {
    userId = res.data.user_info_id;
  }
}

function generateCalendar() {
  const firstDayWeekday = getFirstWeekday(year.value, month.value);
  const totalDays = daysInMonth(year.value, month.value);

  const days = [];

  // 上个月的补足天数
  const prevMonth = month.value === 0 ? 11 : month.value - 1;
  const prevYear = month.value === 0 ? year.value - 1 : year.value;
  const prevMonthDays = daysInMonth(prevYear, prevMonth);

  for (let i = firstDayWeekday - 1; i >= 0; i--) {
    const dayNum = prevMonthDays - i;
    const date = new Date(prevYear, prevMonth, dayNum);
    days.push({ day: dayNum, dateStr: formatDate(date), inCurrentMonth: false, reminderCount: 0 });
  }

  // 本月天数
  for (let i = 1; i <= totalDays; i++) {
    const date = new Date(year.value, month.value, i);
    days.push({ day: i, dateStr: formatDate(date), inCurrentMonth: true, reminderCount: 0 });
  }

  // 下个月补足天数，保证7*6格
  const nextMonth = month.value === 11 ? 0 : month.value + 1;
  const nextYear = month.value === 11 ? year.value + 1 : year.value;
  const nextDaysCount = 42 - days.length;
  for (let i = 1; i <= nextDaysCount; i++) {
    const date = new Date(nextYear, nextMonth, i);
    days.push({ day: i, dateStr: formatDate(date), inCurrentMonth: false, reminderCount: 0 });
  }

  calendarDays.value = days;
}

async function fetchReminders() {
  if (!userId) return;
  loading.value = true;
  try {
    const res = await proxy.$cf.table.list({
      table_name: 'reminder_item',
      param: { user_info_user_info_id_1: userId }
    });
    if (res.success) {
      const allReminders = res.data;
      // 统计每个日期提醒数量
      calendarDays.value.forEach(day => {
        day.reminderCount = allReminders.filter(r => r.reminder_time.startsWith(day.dateStr)).length;
      });
      // 加载选中日期提醒
      reminders.value = allReminders.filter(r => r.reminder_time.startsWith(selectedDate.value));
    }
  } catch (error) {
    proxy.$cf.toast({ message: '加载提醒失败', level: 'error' });
  } finally {
    loading.value = false;
  }
}

function selectDate(dateStr) {
  selectedDate.value = dateStr;
  fetchReminders();
}

function prevMonth() {
  if (month.value === 0) {
    year.value--;
    month.value = 11;
  } else {
    month.value--;
  }
  generateCalendar();
  fetchReminders();
}

function nextMonth() {
  if (month.value === 11) {
    year.value++;
    month.value = 0;
  } else {
    month.value++;
  }
  generateCalendar();
  fetchReminders();
}

async function toggleComplete(item) {
  const updated = { ...item, is_completed: !item.is_completed };
  const res = await proxy.$cf.table.update({
    table_name: 'reminder_item',
    param: updated
  });
  if (res.success) {
    fetchReminders();
  }
}

function goToAddReminder() {
  proxy.$cf.navigate.to({
    url: `/pages/calendar_add_reminder/index?reminder_time=${selectedDate.value} 08:00:00`,
    type: 'page'
  });
}

async function handleRefresh() {
  await fetchReminders();
  proxy.$cf.toast({ message: '刷新成功', level: 'success' });
}

onLoad(async () => {
  await getLoginUser();
  generateCalendar();
  await fetchReminders();
});
</script>
