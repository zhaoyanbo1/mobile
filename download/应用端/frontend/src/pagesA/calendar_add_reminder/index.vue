<template>
  <base-layout>
    <view class="p-4 bg-neutral-50 min-h-screen">
      <uni-forms :modelValue="formData" label-position="top" label-width="100%">
        <!-- 提醒类型选择 -->
        <uni-forms-item required label="提醒类型" name="reminder_type_enum_id">
          <uni-data-select
            v-model="formData.reminder_type_enum_id"
            :localdata="reminderTypes"
            placeholder="请选择提醒类型"
            clearable
            @change="handleTypeChange"
          ></uni-data-select>
        </uni-forms-item>

        <!-- 提醒标题 -->
        <uni-forms-item required label="标题" name="title">
          <uni-easyinput
            type="text"
            v-model="formData.title"
            placeholder="请输入提醒标题"
            class="w-full box-border"
          />
        </uni-forms-item>

        <!-- 提醒内容 -->
        <uni-forms-item label="内容描述" name="description">
          <uni-easyinput
            type="textarea"
            v-model="formData.description"
            placeholder="请输入提醒内容（可选）"
            class="w-full box-border"
          />
        </uni-forms-item>

        <!-- 提醒时间 -->
        <uni-forms-item required label="提醒时间" name="reminder_time">
          <uni-datetime-picker
            type="datetime"
            v-model="formData.reminder_time"
            placeholder="请选择日期和时间"
          />
        </uni-forms-item>

        <!-- 用药提醒相关字段 -->
        <template v-if="formData.reminder_type_enum_id == 1">
          <uni-forms-item label="药品照片" name="medicine_photo">
            <base-upload
              :limit="1"
              mode="grid"
              @success="handleUploadSuccess"
              @delete="handleUploadDelete"
              :initialFiles="medicinePhotos"
            />
          </uni-forms-item>

          <uni-forms-item label="用药剂量" name="medicine_dosage">
            <uni-easyinput
              type="text"
              v-model="formData.medicine_dosage"
              placeholder="请输入用药剂量（如：1片）"
              class="w-full box-border"
            />
          </uni-forms-item>
        </template>

        <!-- 活动提醒相关字段 -->
        <template v-if="formData.reminder_type_enum_id == 2">
          <uni-forms-item label="活动地点" name="location_address">
            <view class="flex flex-col gap-2 mb-4">
              <uni-easyinput
                type="text"
                v-model="formData.location_address"
                placeholder="请输入活动地点"
                class="w-full box-border"
                @focus="showMap = true"
              />
              <view class="flex justify-center">
                <uni-icons 
                  type="map" 
                  size="24" 
                  color="#2EB8FF"
                  @click="showMap = true"
                />
              </view>
            </view>
          </uni-forms-item>

          <uni-popup ref="mapPopup" type="bottom" class="z-50">
            <view class="bg-white rounded-t-xl p-4 h-96">
              <view class="flex justify-between items-center mb-4">
                <text class="text-lg font-bold text-neutral-900">选择地点</text>
                <text class="text-primary-500" @click="showMap = false">完成</text>
              </view>
              <base-map-view
                :latitude="currentLocation.latitude"
                :longitude="currentLocation.longitude"
                @mapTap="handleMapTap"
                class="h-80"
              />
            </view>
          </uni-popup>
        </template>

        <!-- 保存按钮 -->
        <button
          class="w-full bg-primary-500 text-white rounded-xl shadow-md mt-6"
          @click="handleSubmit"
          :disabled="loading"
        >
          <uni-icons
            v-if="loading"
            type="spinner-cycle"
            size="20"
            class="animate-spin mr-2"
          />
          <text>{{ loading ? '保存中...' : '保存提醒' }}</text>
        </button>
      </uni-forms>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 表单数据
const formData = ref({
  reminder_type_enum_id: '',
  title: '',
  description: '',
  reminder_time: '',
  medicine_photo: [],
  medicine_dosage: '',
  location_latitude: '',
  location_longitude: '',
  location_address: '',
  is_completed: false
});

// 药品照片
const medicinePhotos = ref([]);

// 提醒类型选项
const reminderTypes = ref([]);

// 地图相关
const showMap = ref(false);
const currentLocation = ref({
  latitude: 0,
  longitude: 0
});

// 加载状态
const loading = ref(false);

// 获取当前用户信息
const loginUserId = ref(null);

// 获取提醒类型枚举
async function fetchReminderTypes() {
  const res = await proxy.$cf.table.list({
    table_name: 'reminder_type_enum'
  });
  if (res.success) {
    reminderTypes.value = res.data.map(item => ({
      value: item.reminder_type_enum_id,
      text: item.type_name
    }));
  }
}

// 获取当前位置
async function getCurrentLocation() {
  const res = await proxy.$cf.location.getCurrentLocation();
  if (res.success) {
    currentLocation.value = {
      latitude: res.latitude,
      longitude: res.longitude
    };
  }
}

// 处理提醒类型变化
function handleTypeChange() {
  // 重置相关字段
  formData.value.medicine_photo = [];
  formData.value.medicine_dosage = '';
  formData.value.location_latitude = '';
  formData.value.location_longitude = '';
  formData.value.location_address = '';
  medicinePhotos.value = [];
}

// 处理上传成功
function handleUploadSuccess(item) {
  if (item.success) {
    formData.value.medicine_photo.push({
      url: item.data.url
    });
  }
}

// 处理上传删除
function handleUploadDelete(e) {
  formData.value.medicine_photo.splice(e.index, 1);
}

// 处理地图点击
function handleMapTap(e) {
  formData.value.location_latitude = e.latitude;
  formData.value.location_longitude = e.longitude;
  
  // 根据坐标获取地址
  proxy.$cf.location.getAddressByLocation({
    latitude: e.latitude,
    longitude: e.longitude
  }).then(res => {
    if (res.success) {
      formData.value.location_address = res.result.address;
    }
  });
}

// 处理表单提交
async function handleSubmit() {
  if (loading.value) return;
  
  // 表单验证
  if (!formData.value.reminder_type_enum_id) {
    proxy.$cf.toast({ message: '请选择提醒类型', level: 'error' });
    return;
  }
  
  if (!formData.value.title) {
    proxy.$cf.toast({ message: '请输入标题', level: 'error' });
    return;
  }
  
  if (!formData.value.reminder_time) {
    proxy.$cf.toast({ message: '请选择提醒时间', level: 'error' });
    return;
  }
  
  // 活动提醒需要位置信息
  if (formData.value.reminder_type_enum_id == 2 && !formData.value.location_address) {
    proxy.$cf.toast({ message: '请选择活动地点', level: 'error' });
    return;
  }
  
  loading.value = true;
  
  try {
    // 获取当前用户
    const userRes = await proxy.$cf.login.getLoginUser();
    if (!userRes.success || !userRes.data) {
      proxy.$cf.toast({ message: '获取用户信息失败', level: 'error' });
      return;
    }
    
    // 准备保存数据
    const saveData = {
      ...formData.value,
      user_info_user_info_id_1: userRes.data.user_info_id
    };
    
    // 保存提醒事项
    const saveRes = await proxy.$cf.table.add({
      table_name: 'reminder_item',
      param: saveData
    });
    
    if (saveRes.success) {
      proxy.$cf.toast({ message: '提醒保存成功', level: 'success' });
      // 返回日历页面
      proxy.$cf.navigate.to({
        url: '/pages/calendar/index',
        type: 'page'
      });
    } else {
      proxy.$cf.toast({ message: saveRes.message || '保存提醒失败', level: 'error' });
    }
  } catch (error) {
    console.error('保存提醒出错:', error);
    proxy.$cf.toast({ message: '发生错误', level: 'error' });
  } finally {
    loading.value = false;
  }
}

// 初始化
onLoad(async () => {
  await fetchReminderTypes();
  await getCurrentLocation();
});

// 监听地图显示状态
watch(showMap, (val) => {
  if (val) {
    proxy.$refs.mapPopup.open();
  } else {
    proxy.$refs.mapPopup.close();
  }
});
</script>