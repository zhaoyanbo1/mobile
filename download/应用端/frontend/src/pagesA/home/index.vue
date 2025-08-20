<template>
  <base-layout>
    <view class="p-4 bg-neutral-50 min-h-screen">
      <!-- 健康状态看板 -->
      <view class="bg-white rounded-2xl shadow-lg p-6 mb-4">
        <text class="text-xl font-bold text-primary-800 mb-4">健康状态</text>
        <view class="flex justify-center items-center mb-4">
          <!-- 圆形进度图 -->
          <view class="relative w-48 h-48 rounded-full border-8 border-primary-200 flex items-center justify-center">
            <view class="absolute w-40 h-40 rounded-full border-8 border-primary-500 flex items-center justify-center">
              <text class="text-3xl font-bold text-primary-700">{{ healthScore }}%</text>
            </view>
          </view>
        </view>
        <view class="grid grid-cols-2 gap-4">
          <view class="bg-primary-50 p-3 rounded-lg">
            <text class="block text-sm text-primary-700">血压</text>
            <text class="block text-lg font-bold text-primary-800">{{ bloodPressure || '--/--' }}</text>
          </view>
          <view class="bg-success-50 p-3 rounded-lg">
            <text class="block text-sm text-success-700">血糖</text>
            <text class="block text-lg font-bold text-success-800">{{ bloodSugar || '--' }}</text>
          </view>
        </view>
      </view>

      <!-- 紧急提醒 -->
      <view class="bg-white rounded-2xl shadow-lg p-6 mb-4">
        <view class="flex justify-between items-center mb-4">
          <text class="text-xl font-bold text-primary-800">紧急提醒</text>
          <uni-icons type="right" size="20" color="#8B8B8B" @click="goToCalendar" />
        </view>
        
        <view v-if="urgentReminders.length > 0">
          <view 
            v-for="(reminder, index) in urgentReminders.slice(0, 3)" 
            :key="reminder.reminder_item_id" 
            class="mb-4 last:mb-0 border-b border-neutral-100 pb-4 last:border-b-0 last:pb-0"
            @click="goToReminderDetail(reminder)">
            <view class="flex items-start">
              <uni-badge 
                :text="getReminderType(reminder.reminder_type_enum_id)" 
                size="small" 
                :type="getBadgeType(reminder.reminder_type_enum_id)" 
                class="mr-2" 
              />
              <view class="flex-1">
                <text class="block text-lg font-medium text-neutral-900">{{ reminder.title }}</text>
                <text class="block text-sm text-neutral-500 mt-1">
                  <uni-icons type="calendar-filled" size="14" color="#9E9E9E" class="mr-1" />
                  {{ formatDateTime(reminder.reminder_time) }}
                </text>
              </view>
              <view 
                class="p-1 rounded-full" 
                :class="reminder.is_completed ? 'bg-success-100' : 'bg-warning-100'"
                @click.stop="toggleReminderStatus(reminder)">
                <uni-icons 
                  :type="reminder.is_completed ? 'checkmarkempty' : 'circle'" 
                  size="20" 
                  :color="reminder.is_completed ? '#1BAA5F' : '#FF9614'" 
                />
              </view>
            </view>
          </view>
        </view>
        <view v-else class="py-8 flex flex-col items-center justify-center">
          <uni-icons type="notification" size="40" color="#E0E0E0" />
          <text class="text-neutral-400 mt-2">暂无紧急提醒</text>
        </view>
      </view>

      <!-- 快捷入口 -->
      <view class="grid grid-cols-2 gap-4">
        <button 
          class="bg-primary-500 text-white rounded-xl shadow-md p-4 flex flex-col items-center"
          @click="goToQuestionnaireUpdate">
          <uni-icons type="headphones" size="28" color="white" />
          <text class="mt-2 font-medium">更新问卷</text>
        </button>
        <button 
          class="bg-secondary-500 text-white rounded-xl shadow-md p-4 flex flex-col items-center"
          @click="goToAddReminder">
          <uni-icons type="plus" size="28" color="white" />
          <text class="mt-2 font-medium">添加提醒</text>
        </button>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 数据
const urgentReminders = ref([]);
const healthData = ref({});
const healthScore = ref(85);
const bloodPressure = ref('120/80');
const bloodSugar = ref('5.2');

// 生命周期
onLoad(async () => {
  await fetchHealthData();
  await fetchUrgentReminders();
});

// 方法
async function fetchHealthData() {
  const userRes = await proxy.$cf.login.getLoginUser();
  if (!userRes.success) return;

  const res = await proxy.$cf.table.list({
    table_name: 'health_questionnaire',
    param: {
      user_info_user_info_id_1: userRes.data.user_info_id
    },
    order: 'creation_time desc',
    limit: 1
  });

  if (res.success && res.data.length > 0) {
    healthData.value = res.data[0];
    // 更新健康数据
    if (healthData.value.blood_pressure) {
      bloodPressure.value = healthData.value.blood_pressure;
    }
    if (healthData.value.blood_sugar) {
      bloodSugar.value = healthData.value.blood_sugar;
    }
  }
}

async function fetchUrgentReminders() {
  const userRes = await proxy.$cf.login.getLoginUser();
  if (!userRes.success) return;

  const now = new Date();
  const tomorrow = new Date(now);
  tomorrow.setDate(tomorrow.getDate() + 1);

  const res = await proxy.$cf.table.list({
    table_name: 'reminder_item',
    param: {
      user_info_user_info_id_1: userRes.data.user_info_id,
      is_completed: false
    },
    order: 'reminder_time asc'
  });

  if (res.success) {
    // 筛选今天或明天的提醒
    urgentReminders.value = res.data.filter(reminder => {
      const reminderTime = new Date(reminder.reminder_time);
      return reminderTime <= tomorrow;
    });
  }
}

function getReminderType(typeId) {
  switch(typeId) {
    case 1: return '用药';
    case 2: return '活动';
    case 3: return '饮食';
    default: return '提醒';
  }
}

function getBadgeType(typeId) {
  switch(typeId) {
    case 1: return 'danger';
    case 2: return 'primary';
    case 3: return 'success';
    default: return 'default';
  }
}

function formatDateTime(datetime) {
  if (!datetime) return '';
  const date = new Date(datetime);
  return `${date.getMonth()+1}月${date.getDate()}日 ${date.getHours()}:${date.getMinutes().toString().padStart(2, '0')}`;
}

async function toggleReminderStatus(reminder) {
  const updatedReminder = {
    ...reminder,
    is_completed: !reminder.is_completed,
    update_time: new Date().toISOString().slice(0, 19).replace('T', ' ')
  };

  const res = await proxy.$cf.table.update({
    table_name: 'reminder_item',
    param: updatedReminder
  });

  if (res.success) {
    await proxy.$cf.toast({
      message: `提醒已标记为${updatedReminder.is_completed ? '完成' : '待处理'}`,
      level: 'success'
    });
    await fetchUrgentReminders();
  }
}

function goToQuestionnaireUpdate() {
  proxy.$cf.navigate.to({
    url: '/pages/health_questionnaire_update/index',
    type: 'page'
  });
}

function goToAddReminder() {
  proxy.$cf.navigate.to({
    url: '/pages/calendar_add_reminder/index',
    type: 'page'
  });
}

function goToCalendar() {
  proxy.$cf.navigate.to({
    url: '/pages/calendar/index',
    type: 'page'
  });
}

function goToReminderDetail(reminder) {
  proxy.$cf.navigate.to({
    url: `/pages/calendar/index?highlight_reminder=${reminder.reminder_item_id}`,
    type: 'page'
  });
}
</script>