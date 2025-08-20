<template>
  <base-layout>
    <view class="p-4 bg-neutral-50 min-h-screen">
      <!-- 活动封面图 -->
      <image 
        :src="get_resource_url('https://www.codeflying.net/preview/outdoors-basketball.jpg')" 
        mode="aspectFill" 
        class="w-full h-48 rounded-xl shadow-md mb-4"
      />
      
      <!-- 活动标题 -->
      <view class="bg-white rounded-xl shadow-sm p-4 mb-4">
        <text class="text-2xl font-bold text-primary-800">{{ activityDetail.title }}</text>
      </view>
      
      <!-- 活动基本信息 -->
      <view class="bg-white rounded-xl shadow-sm p-4 mb-4">
        <view class="flex items-center mb-3">
          <uni-icons type="calendar-filled" size="20" color="#616161" class="mr-2" />
          <text class="text-neutral-700">{{ formatDateTime(activityDetail.activity_time) }}</text>
        </view>
        
        <view class="flex items-center mb-3">
          <uni-icons type="location-filled" size="20" color="#616161" class="mr-2" />
          <text class="text-neutral-700">{{ activityDetail.location_address }}</text>
        </view>
        
        <view class="flex items-center">
          <uni-icons type="person-filled" size="20" color="#616161" class="mr-2" />
          <text class="text-neutral-700">适宜人群: {{ activityDetail.suitable_people || '所有年龄段' }}</text>
        </view>
      </view>
      
      <!-- 地图导航 -->
      <view class="bg-white rounded-xl shadow-sm p-4 mb-6">
        <text class="block text-lg font-semibold text-primary-800 mb-3">活动地点</text>
        <base-map-view 
          :latitude="activityDetail.location_latitude" 
          :longitude="activityDetail.location_longitude"
          :markers="markers"
          class="h-48 rounded-lg overflow-hidden"
          @mapTap="handleMapTap"
        />
        <button 
          class="w-full bg-primary-500 text-white rounded-full py-2 mt-3 flex items-center justify-center"
          @click="startNavigation"
        >
          <uni-icons type="navigate-filled" size="18" color="white" class="mr-2" />
          <text>开始导航</text>
        </button>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();
const activityDetail = ref({});
const markers = ref([]);

// 格式化日期时间
const formatDateTime = (datetime) => {
  if (!datetime) return '';
  const date = new Date(datetime);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 获取活动详情
const fetchActivityDetail = async (id) => {
  const res = await proxy.$cf.table.get({
    table_name: 'activity_recommendation',
    param: { activity_recommendation_id: id }
  });
  
  if (res.success && res.data) {
    activityDetail.value = res.data;
    initMapMarkers();
  } else {
    proxy.$cf.toast({ message: '加载活动详情失败', level: 'error' });
  }
};

// 初始化地图标记
const initMapMarkers = () => {
  if (!activityDetail.value.location_latitude || !activityDetail.value.location_longitude) return;
  
  markers.value = [{
    id: 1,
    latitude: activityDetail.value.location_latitude,
    longitude: activityDetail.value.location_longitude,
    iconPath: "/static/mark_icon.png",
    width: 40,
    height: 40
  }];
};

// 处理地图点击
const handleMapTap = (e) => {
  console.log('地图点击位置:', e.latitude, e.longitude);
};

// 开始导航
const startNavigation = async () => {
  if (!activityDetail.value.location_latitude || !activityDetail.value.location_longitude) {
    proxy.$cf.toast({ message: '缺少位置信息', level: 'warning' });
    return;
  }
  
  // 获取当前位置
  const locationRes = await proxy.$cf.location.getCurrentLocation();
  if (!locationRes.success) {
    proxy.$cf.toast({ message: '获取当前位置失败', level: 'error' });
    return;
  }
  
  // 调用导航
  proxy.$cf.map.open_amap_nav({
    latitude: activityDetail.value.location_latitude,
    longitude: activityDetail.value.location_longitude,
    address: activityDetail.value.location_address
  });
};

onLoad((options) => {
  if (options.activity_recommendation_id) {
    fetchActivityDetail(options.activity_recommendation_id);
  } else {
    proxy.$cf.toast({ message: '无效的活动ID', level: 'error' });
    proxy.$cf.navigate.back();
  }
});

// 资源URL处理
const get_resource_url = (path) => {
  return proxy.get_resource_url(path);
};
</script>