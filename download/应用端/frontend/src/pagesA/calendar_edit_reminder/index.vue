<template>
  <base-layout>
    <view class="p-4 bg-neutral-50 min-h-screen">
      <!-- 顶部标题 -->
      <view class="mb-6 flex items-center justify-between">
        <text class="text-2xl font-bold text-primary-700">编辑提醒事项</text>
        <uni-icons type="close" size="24" color="#666" @click="goBack" />
      </view>

      <!-- 编辑表单 -->
      <uni-forms :modelValue="formData" label-position="top" label-width="100%" class="bg-white rounded-xl shadow-md p-4 mb-4">
        <!-- 提醒类型 -->
        <uni-forms-item required label="提醒类型" name="reminder_type_enum_id">
          <uni-data-select 
            v-model="formData.reminder_type_enum_id"
            :localdata="reminderTypes"
            placeholder="请选择提醒类型"
            class="w-full"
          ></uni-data-select>
        </uni-forms-item>

        <!-- 标题 -->
        <uni-forms-item required label="标题" name="title">
          <uni-easyinput 
            v-model="formData.title" 
            type="text" 
            placeholder="请输入提醒标题"
            class="w-full box-border"
          />
        </uni-forms-item>

        <!-- 描述 -->
        <uni-forms-item label="描述" name="description">
          <uni-easyinput 
            v-model="formData.description" 
            type="textarea" 
            placeholder="请输入提醒详情"
            class="w-full box-border"
          />
        </uni-forms-item>

        <!-- 提醒时间 -->
        <uni-forms-item required label="提醒时间" name="reminder_time">
          <uni-datetime-picker 
            type="datetime" 
            v-model="formData.reminder_time"
            class="w-full"
          />
        </uni-forms-item>

        <!-- 药品照片 (仅显示在用药提醒类型) -->
        <uni-forms-item 
          v-if="formData.reminder_type_enum_id == 1" 
          label="药品照片" 
          name="medicine_photo"
        >
          <base-upload 
            :limit="1" 
            mode="grid" 
            @success="handleUploadSuccess"
            @delete="handleDeletePhoto"
            :initialFiles="initialFiles"
            class="w-full mb-4"
          />
        </uni-forms-item>

        <!-- 用药量 (仅显示在用药提醒类型) -->
        <uni-forms-item 
          v-if="formData.reminder_type_enum_id == 1" 
          label="用药量" 
          name="medicine_dosage"
        >
          <uni-easyinput 
            v-model="formData.medicine_dosage" 
            type="text" 
            placeholder="例如：1片"
            class="w-full box-border mb-4"
          />
        </uni-forms-item>

        <!-- 地图位置 (仅显示在活动提醒类型) -->
        <uni-forms-item 
          v-if="formData.reminder_type_enum_id == 2" 
          label="活动地点" 
          name="location"
        >
          <view class="mb-4">
            <button 
              class="bg-primary-500 text-white rounded-full px-4 flex items-center"
              @click="showLocationPicker"
            >
              <uni-icons type="location-filled" class="mr-2" color="#fff" />
              选择地点
            </button>
          </view>
          <view v-if="formData.location_address" class="text-sm text-neutral-600 mb-4">
            {{ formData.location_address }}
          </view>
          <base-map-view 
            v-if="formData.location_latitude && formData.location_longitude"
            :latitude="formData.location_latitude"
            :longitude="formData.location_longitude"
            :markers="locationMarkers"
            class="h-48 rounded-lg border border-neutral-200 mb-4"
          />
        </uni-forms-item>

        <!-- 完成状态 -->
        <uni-forms-item label="完成状态" name="is_completed">
          <view class="flex items-center">
            <switch 
              :checked="formData.is_completed" 
              @change="(e) => formData.is_completed = e.detail.value" 
              color="#2EB8FF"
            />
            <text class="ml-2 text-neutral-700">
              {{ formData.is_completed ? '已完成' : '待完成' }}
            </text>
          </view>
        </uni-forms-item>

        <!-- 保存按钮 -->
        <button 
          class="w-full bg-primary-500 text-white rounded-xl shadow-md mt-6 font-medium"
          @click="saveReminder"
          :disabled="loading"
        >
          <uni-icons v-if="loading" type="spinner-cycle" class="animate-spin mr-2" />
          {{ loading ? '保存中...' : '保存修改' }}
        </button>
      </uni-forms>

      <!-- 位置选择弹窗 -->
      <uni-popup ref="locationPopup" type="bottom" class="z-50">
        <view class="bg-white rounded-t-xl p-4 h-96">
          <view class="flex justify-between items-center mb-4">
            <text class="text-lg font-bold">选择地点</text>
            <uni-icons type="close" size="20" @click="closeLocationPicker" />
          </view>
          <base-map-view 
            :latitude="currentLocation.latitude"
            :longitude="currentLocation.longitude"
            :markers="locationMarkers"
            @mapTap="handleMapTap"
            class="flex-1 mb-4"
          />
          <button 
            class="w-full bg-primary-500 text-white rounded-xl"
            @click="confirmLocation"
          >
            确认地点
          </button>
        </view>
      </uni-popup>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 表单数据
const formData = ref({
  reminder_item_id: null,
  reminder_type_enum_id: null,
  title: '',
  description: '',
  reminder_time: '',
  is_completed: false,
  medicine_photo: [],
  medicine_dosage: '',
  location_latitude: null,
  location_longitude: null,
  location_address: ''
});

// 初始文件
const initialFiles = ref([]);

// 提醒类型
const reminderTypes = ref([
  { value: 1, text: '用药提醒' },
  { value: 2, text: '活动提醒' },
  { value: 3, text: '饮食提醒' }
]);

// 位置标记
const locationMarkers = ref([]);
const selectedLocation = ref(null);

// 当前用户位置
const currentLocation = ref({
  latitude: 0,
  longitude: 0
});

// 加载状态
const loading = ref(false);

// 弹窗引用
const locationPopup = ref(null);

// 获取当前用户位置
async function getCurrentPosition() {
  try {
    const res = await proxy.$cf.location.getCurrentLocation();
    if (res.success) {
      currentLocation.value = {
        latitude: res.latitude,
        longitude: res.longitude
      };
      
      if (!formData.value.location_latitude) {
        formData.value.location_latitude = res.latitude;
        formData.value.location_longitude = res.longitude;
        updateLocationMarkers();
      }
    }
  } catch (error) {
    console.error('获取当前位置失败:', error);
  }
}

// 更新位置标记
function updateLocationMarkers() {
  if (formData.value.location_latitude && formData.value.location_longitude) {
    locationMarkers.value = [{
      id: 1,
      latitude: formData.value.location_latitude,
      longitude: formData.value.location_longitude,
      iconPath: "/static/mark_icon.png",
      width: 40,
      height: 40
    }];
  }
}

// 显示位置选择器
function showLocationPicker() {
  locationPopup.value.open();
}

// 关闭位置选择器
function closeLocationPicker() {
  locationPopup.value.close();
}

// 处理地图点击
function handleMapTap(e) {
  selectedLocation.value = {
    latitude: e.latitude,
    longitude: e.longitude
  };
  
  locationMarkers.value = [{
    id: 1,
    latitude: e.latitude,
    longitude: e.longitude,
    iconPath: "/static/mark_icon.png",
    width: 40,
    height: 40
  }];
}

// 确认位置选择
async function confirmLocation() {
  if (selectedLocation.value) {
    formData.value.location_latitude = selectedLocation.value.latitude;
    formData.value.location_longitude = selectedLocation.value.longitude;
    
    try {
      const res = await proxy.$cf.location.getAddressByLocation({
        latitude: selectedLocation.value.latitude,
        longitude: selectedLocation.value.longitude
      });
      
      if (res.success) {
        formData.value.location_address = res.result.address;
      }
    } catch (error) {
      console.error('获取地址失败:', error);
    }
    
    closeLocationPicker();
  }
}

// 处理上传成功
function handleUploadSuccess(res) {
  if (res.success) {
    formData.value.medicine_photo = [{
      url: res.data.url
    }];
  }
}

// 处理删除照片
function handleDeletePhoto() {
  formData.value.medicine_photo = [];
}

// 加载提醒详情
async function loadReminderDetail(reminderId) {
  try {
    loading.value = true;
    const res = await proxy.$cf.table.get({
      table_name: 'reminder_item',
      param: { reminder_item_id: reminderId }
    });
    
    if (res.success && res.data) {
      const data = res.data;
      
      formData.value = {
        reminder_item_id: data.reminder_item_id,
        reminder_type_enum_id: data.reminder_type_enum_id,
        title: data.title,
        description: data.description,
        reminder_time: data.reminder_time,
        is_completed: data.is_completed,
        medicine_photo: data.medicine_photo || [],
        medicine_dosage: data.medicine_dosage || '',
        location_latitude: data.location_latitude,
        location_longitude: data.location_longitude,
        location_address: data.location_address || ''
      };
      
      if (data.medicine_photo && data.medicine_photo.length > 0) {
        initialFiles.value = data.medicine_photo.map(item => ({
          ...item,
          url: proxy.get_resource_url(item.url)
        }));
      }
      
      updateLocationMarkers();
    }
  } catch (error) {
    console.error('加载提醒失败:', error);
    proxy.$cf.toast({ message: '加载提醒失败', level: 'error' });
  } finally {
    loading.value = false;
  }
}

// 保存提醒
async function saveReminder() {
  try {
    loading.value = true;
    
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
    
    const userRes = await proxy.$cf.login.getLoginUser();
    if (!userRes.success) {
      proxy.$cf.toast({ message: '请先登录', level: 'error' });
      return;
    }
    
    const saveData = {
      ...formData.value,
      user_info_user_info_id_1: userRes.data.user_info_id
    };
    
    const updateRes = await proxy.$cf.table.update({
      table_name: 'reminder_item',
      param: saveData
    });
    
    if (updateRes.success) {
      proxy.$cf.toast({ message: '提醒更新成功', level: 'success' });
      proxy.$cf.navigate.to({
        url: '/pages/calendar/index',
        type: 'page'
      });
    } else {
      proxy.$cf.toast({ message: '更新提醒失败', level: 'error' });
    }
  } catch (error) {
    console.error('保存提醒失败:', error);
    proxy.$cf.toast({ message: '保存提醒失败', level: 'error' });
  } finally {
    loading.value = false;
  }
}

// 返回
function goBack() {
  proxy.$cf.navigate.to({
    url: '/pages/calendar/index',
    type: 'page'
  });
}

// 页面加载
onLoad((options) => {
  if (options.reminder_item_id) {
    loadReminderDetail(options.reminder_item_id);
  }
  getCurrentPosition();
});
</script>