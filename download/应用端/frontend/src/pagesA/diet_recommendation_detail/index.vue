<template>
  <base-layout>
    <view class="p-4 bg-neutral-50 min-h-screen">
      <!-- 食谱封面图片 -->
      <image 
        src="https://www.codeflying.net/preview/homemade-bread.jpg" 
        mode="aspectFill" 
        class="w-full h-48 rounded-xl shadow-md mb-4"
      />
      
      <!-- 食谱基本信息卡片 -->
      <view class="bg-white rounded-xl shadow-md p-4 mb-4">
        <text class="text-2xl font-bold text-primary-800 block mb-2">{{ recipe.title }}</text>
        
        <view class="flex flex-col gap-2 mb-3">
          <view class="flex items-center gap-2">
            <uni-icons type="headphones" size="18" color="#FF9614" />
            <text class="text-neutral-700">难度: {{ recipe.difficulty || '暂无' }}</text>
          </view>
          <view class="flex items-center gap-2">
            <uni-icons type="headphones" size="18" color="#1BAA5F" />
            <text class="text-neutral-700">所需时间: {{ recipe.required_time || '暂无' }}</text>
          </view>
        </view>
        
        <view class="flex items-center gap-2 mb-3">
          <uni-icons type="calendar-filled" size="18" color="#616161" />
          <text class="text-neutral-600">创建时间: {{ formatDate(recipe.creation_time) }}</text>
        </view>
      </view>
      
      <!-- 食谱详情卡片 -->
      <view class="bg-white rounded-xl shadow-md p-4 mb-4">
        <text class="text-lg font-semibold text-primary-700 block mb-3">食谱详情</text>
        <text class="text-neutral-700">{{ recipe.description || '暂无详细描述' }}</text>
      </view>
      
      <!-- 操作按钮 -->
      <view class="flex justify-center px-4 mb-4">
        <button 
          class="bg-primary-500 text-white rounded-full px-6 shadow-lg flex items-center gap-2"
          @click="goBack"
        >
          <uni-icons type="left" size="20" color="white" />
          <text>返回推荐列表</text>
        </button>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();
const recipe = ref({
  title: '',
  difficulty: '',
  required_time: '',
  description: '',
  creation_time: ''
});

// 获取页面参数中的食谱ID
const routeParams = ref({});
onLoad((options) => {
  routeParams.value = options;
  fetchRecipeDetail();
});

// 获取食谱详情
async function fetchRecipeDetail() {
  if (!routeParams.value.diet_recommendation_id) return;
  
  const res = await proxy.$cf.table.get({
    table_name: 'diet_recommendation',
    param: { diet_recommendation_id: routeParams.value.diet_recommendation_id }
  });
  
  if (res.success && res.data) {
    recipe.value = res.data;
  } else {
    proxy.$cf.toast({ message: '加载食谱详情失败', level: 'error' });
  }
}

// 格式化日期
function formatDate(dateString) {
  if (!dateString) return '暂无';
  const date = new Date(dateString);
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`;
}

// 返回推荐页面
function goBack() {
  proxy.$cf.navigate.to({
    url: '/pages/recommend/index',
    type: 'page'
  });
}
</script>