<template>
  <base-layout>
    <view class="p-4 bg-neutral-50 min-h-screen">
      <!-- 活动推荐 -->
      <view class="mb-8">
        <view class="flex items-center justify-between mb-4">
          <text class="text-xl font-bold text-primary-800">附近活动</text>
          <view class="flex items-center gap-2">
            <button
              class="bg-primary-500 text-white rounded-full p-2 shadow-md"
              @click="refreshActivities"
              :disabled="loadingActivities"
            >
              <uni-icons
                :type="loadingActivities ? 'spinner-cycle' : 'refresh'"
                size="24"
                :class="loadingActivities ? 'animate-spin' : ''"
                color="white"
              />
            </button>
          </view>
        </view>

        <view v-if="loadingActivities" class="flex justify-center py-8">
          <uni-icons type="spinner-cycle" size="28" class="animate-spin text-primary-500" />
        </view>

        <view v-else class="grid grid-cols-1 gap-3">
          <view 
            v-for="(activity, index) in filteredActivities" 
            :key="index"
            class="bg-white rounded-xl shadow-md mb-4"
            @click="goToActivityDetail(activity.activity_recommendation_id)"
          >
            <image 
              src="https://www.codeflying.net/preview/scenery-sun-down.jpg" 
              mode="aspectFill" 
              class="w-full h-40 rounded-t-xl"
            />
            <view class="p-3">
              <text class="block font-semibold text-neutral-900 truncate">{{ activity.title }}</text>
              <view class="flex items-center mt-2">
                <uni-icons type="calendar-filled" size="16" color="#757575" />
                <text class="text-sm text-neutral-600 ml-1">{{ formatDateTime(activity.activity_time) }}</text>
              </view>
              <view class="flex items-center mt-1">
                <uni-icons type="location-filled" size="16" color="#757575" />
                <text class="text-sm text-neutral-600 ml-1 truncate">{{ activity.location_address }}</text>
              </view>
              <view class="mt-3 flex justify-between items-center">
                <uni-tag 
                  :text="activity.suitable_people || '所有年龄'" 
                  size="small" 
                  type="primary" 
                  circle
                />
                <view 
                  class="bg-primary-500 text-white text-sm rounded-full px-3 py-1 flex items-center"
                  @click.stop="addActivityReminder(activity)"
                >
                  <uni-icons type="plus" size="14" color="white" class="mr-1" />
                  添加提醒
                </view>
              </view>
            </view>
          </view>
        </view>

        <view v-if="!loadingActivities && filteredActivities.length == 0" class="bg-white rounded-xl p-6 text-center">
          <uni-icons type="info" size="28" color="#BDBDBD" />
          <text class="block mt-2 text-neutral-500">暂无附近活动</text>
        </view>
      </view>

      <!-- 饮食推荐 -->
      <view class="mb-8">
        <view class="flex items-center mb-4">
          <text class="text-xl font-bold text-primary-800">饮食推荐</text>
          <uni-icons type="cart-filled" size="20" color="#009EFF" class="ml-2" />
        </view>

        <view v-if="loadingDiets" class="flex justify-center py-8">
          <uni-icons type="spinner-cycle" size="28" class="animate-spin text-primary-500" />
        </view>

        <view v-else class="grid grid-cols-2 gap-3">
          <view 
            v-for="(diet, index) in filteredDiets" 
            :key="index"
            class="bg-white rounded-xl shadow-md overflow-hidden mb-4"
            @click="goToDietDetail(diet.diet_recommendation_id)"
          >
            <image 
              src="https://www.codeflying.net/preview/fresh-apple.jpg" 
              mode="aspectFill" 
              class="w-full h-32"
            />
            <view class="p-3">
              <text class="block font-semibold text-neutral-900 truncate">{{ diet.title }}</text>
              <view class="flex items-center mt-2">
                <uni-tag 
                  :text="diet.difficulty || '简单'" 
                  size="small" 
                  type="success" 
                  circle
                  class="mr-2"
                />
                <uni-tag 
                  :text="diet.required_time || '30分钟'" 
                  size="small" 
                  type="warning" 
                  circle
                />
              </view>
              <view 
                class="w-full mt-3 bg-secondary-500 text-white text-sm rounded-full py-1 flex items-center justify-center"
                @click.stop="addDietReminder(diet)"
              >
                <uni-icons type="plus" size="14" color="white" class="mr-1" />
                加入计划
              </view>
            </view>
          </view>
        </view>

        <view v-if="!loadingDiets && filteredDiets.length == 0" class="bg-white rounded-xl p-6 text-center">
          <uni-icons type="info" size="28" color="#BDBDBD" />
          <text class="block mt-2 text-neutral-500">暂无饮食推荐</text>
        </view>
      </view>

      <!-- 药品推荐 -->
      <view>
        <view class="flex items-center mb-4">
          <text class="text-xl font-bold text-primary-800">药品推荐</text>
          <uni-icons type="medal-filled" size="20" color="#009EFF" class="ml-2" />
        </view>

        <view v-if="loadingMedicines" class="flex justify-center py-8">
          <uni-icons type="spinner-cycle" size="28" class="animate-spin text-primary-500" />
        </view>

        <view v-else class="grid grid-cols-1 gap-3">
          <view 
            v-for="(medicine, index) in filteredMedicines" 
            :key="index"
            class="bg-white rounded-xl shadow-md p-4 mb-4"
          >
            <view class="flex items-start">
              <image 
                src="https://www.codeflying.net/preview/doctor-patient.jpg" 
                mode="aspectFill" 
                class="w-16 h-16 rounded-lg mr-3"
              />
              <view class="flex-1">
                <text class="block font-semibold text-neutral-900">{{ medicine.title }}</text>
                <text class="block text-sm text-neutral-500 mt-1 line-clamp-2">{{ medicine.usage_guide }}</text>
                <view class="mt-3 flex justify-between items-center">
                  <text class="text-xs text-neutral-400">{{ medicine.nearby_pharmacy_info || '附近药店有售' }}</text>
                  <view 
                    class="bg-danger-500 text-white text-sm rounded-full px-3 py-1 flex items-center"
                    @click.stop="addMedicineReminder(medicine)"
                  >
                    <uni-icons type="plus" size="14" color="white" class="mr-1" />
                    用药提醒
                  </view>
                </view>
              </view>
            </view>
          </view>
        </view>

        <view v-if="!loadingMedicines && filteredMedicines.length == 0" class="bg-white rounded-xl p-6 text-center">
          <uni-icons type="info" size="28" color="#BDBDBD" />
          <text class="block mt-2 text-neutral-500">暂无药品推荐</text>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 数据状态
const activities = ref([]);
const filteredActivities = ref([]);
const diets = ref([]);
const filteredDiets = ref([]);
const medicines = ref([]);
const filteredMedicines = ref([]);
const userLocation = ref(null);
const userHealthData = ref(null);

const loadingActivities = ref(false);
const loadingDiets = ref(false);
const loadingMedicines = ref(false);

// 获取当前位置
const getCurrentLocation = async () => {
  try {
    const res = await proxy.$cf.location.getCurrentLocation();
    if (res.success) {
      userLocation.value = {
        latitude: res.latitude,
        longitude: res.longitude
      };
    }
  } catch (error) {
    console.error('获取位置失败:', error);
  }
};

// 格式化日期时间
const formatDateTime = (datetimeStr) => {
  if (!datetimeStr) return '';
  const date = new Date(datetimeStr);
  return date.toLocaleDateString('zh-CN', { 
    month: 'short', 
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 获取活动推荐
const fetchActivities = async () => {
  loadingActivities.value = true;
  try {
    const res = await proxy.$cf.table.list({
      table_name: 'activity_recommendation'
    });
    
    if (res.success) {
      activities.value = res.data;
      filteredActivities.value = res.data;
    }
  } catch (error) {
    console.error('获取活动失败:', error);
  } finally {
    loadingActivities.value = false;
  }
};

// 获取饮食推荐
const fetchDiets = async () => {
  loadingDiets.value = true;
  try {
    const res = await proxy.$cf.table.list({
      table_name: 'diet_recommendation'
    });
    
    if (res.success) {
      diets.value = res.data;
      filteredDiets.value = res.data;
    }
  } catch (error) {
    console.error('获取饮食推荐失败:', error);
  } finally {
    loadingDiets.value = false;
  }
};

// 获取药品推荐
const fetchMedicines = async () => {
  loadingMedicines.value = true;
  try {
    const res = await proxy.$cf.table.list({
      table_name: 'medicine_recommendation'
    });
    
    if (res.success) {
      medicines.value = res.data;
      filteredMedicines.value = res.data;
    }
  } catch (error) {
    console.error('获取药品推荐失败:', error);
  } finally {
    loadingMedicines.value = false;
  }
};

// 刷新活动列表
const refreshActivities = async () => {
  await fetchActivities();
  proxy.$cf.toast({ message: '活动列表已刷新', level: 'success' });
};

// 添加活动提醒，自动填写完整信息
const addActivityReminder = async (activity) => {
  const confirm = await proxy.$cf.model({
    title: "确认添加",
    message: `确定要添加\"${activity.title}\"活动提醒吗？`,
    confirmText: "确定",
    cancelText: "取消"
  });
  
  if (confirm.confirm) {
    const reminderTime = activity.activity_time || (new Date()).toISOString().slice(0, 19).replace('T', ' ');
    const params = {
      reminder_type_enum_id: 2,
      title: activity.title,
      description: `活动时间：${reminderTime}\n地点：${activity.location_address}`,
      reminder_time: reminderTime,
      location_latitude: activity.location_latitude,
      location_longitude: activity.location_longitude,
      location_address: activity.location_address
    };
    proxy.$cf.navigate.to({
      url: `/pages/calendar_add_reminder/index?autoFill=${encodeURIComponent(JSON.stringify(params))}`,
      type: 'page'
    });
  }
};

// 添加饮食提醒，自动填写类型、标题和描述，用户选日期
const addDietReminder = async (diet) => {
  const confirm = await proxy.$cf.model({
    title: "确认添加",
    message: `确定要添加\"${diet.title}\"饮食计划提醒吗？`,
    confirmText: "确定",
    cancelText: "取消"
  });
  
  if (confirm.confirm) {
    const params = {
      reminder_type_enum_id: 3,
      title: diet.title,
      description: `饮食难度：${diet.difficulty || '未知'}，预计时间：${diet.required_time || '未知'}`
    };
    proxy.$cf.navigate.to({
      url: `/pages/calendar_add_reminder/index?autoFill=${encodeURIComponent(JSON.stringify(params))}`,
      type: 'page'
    });
  }
};

// 添加药品提醒，自动填写类型、标题和描述，用户选日期
const addMedicineReminder = async (medicine) => {
  const confirm = await proxy.$cf.model({
    title: "确认添加",
    message: `确定要添加\"${medicine.title}\"用药提醒吗？`,
    confirmText: "确定",
    cancelText: "取消"
  });
  
  if (confirm.confirm) {
    const params = {
      reminder_type_enum_id: 1,
      title: medicine.title,
      description: medicine.usage_guide || ''
    };
    proxy.$cf.navigate.to({
      url: `/pages/calendar_add_reminder/index?autoFill=${encodeURIComponent(JSON.stringify(params))}`,
      type: 'page'
    });
  }
};

// 跳转活动详情
const goToActivityDetail = (id) => {
  proxy.$cf.navigate.to({
    url: `/pages/activity_recommendation_detail/index?activity_recommendation_id=${id}`,
    type: 'page'
  });
};

// 跳转食谱详情
const goToDietDetail = (id) => {
  proxy.$cf.navigate.to({
    url: `/pages/diet_recommendation_detail/index?diet_recommendation_id=${id}`,
    type: 'page'
  });
};

onLoad(async () => {
  await getCurrentLocation();
  await fetchActivities();
  await fetchDiets();
  await fetchMedicines();
});
</script>
