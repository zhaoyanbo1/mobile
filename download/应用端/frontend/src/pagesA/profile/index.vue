<template>
  <base-layout>
    <view class="p-4 bg-neutral-50 min-h-screen">
      <!-- 用户信息头部 -->
      <view class="bg-white rounded-xl shadow-md p-4 mb-4 flex items-center">
        <uni-icons type="person-filled" size="36" color="primary-500" />
        <view class="ml-4">
          <text class="text-xl font-bold text-primary-800">{{ userInfo.username || '用户' }}</text>
          <text class="block text-sm text-neutral-500">{{ userInfo.phone_number }}</text>
        </view>
      </view>

      <!-- 健康数据可视化 -->
      <view class="bg-white rounded-xl shadow-md p-4 mb-4">
        <view class="flex items-center justify-between mb-4">
          <text class="text-lg font-bold text-primary-800">健康数据</text>
          <button 
            class="bg-primary-500 text-white rounded-full px-3 text-sm"
            @click="updateQuestionnaire"
          >
            更新
          </button>
        </view>
        
        <view v-if="healthData" class="grid grid-cols-2 gap-4">
          <view class="bg-primary-50 rounded-lg p-3">
            <text class="block text-sm text-primary-700">年龄</text>
            <text class="block text-2xl font-bold text-primary-800">{{ healthData.age || '--' }}</text>
          </view>
          <view class="bg-success-50 rounded-lg p-3">
            <text class="block text-sm text-success-700">运动频率</text>
            <text class="block text-2xl font-bold text-success-800">{{ healthData.exercise_frequency || '--' }}</text>
          </view>
          <view class="bg-warning-50 rounded-lg p-3">
            <text class="block text-sm text-warning-700">饮食偏好</text>
            <text class="block text-2xl font-bold text-warning-800">{{ healthData.diet_preference || '--' }}</text>
          </view>
          <view class="bg-danger-50 rounded-lg p-3">
            <text class="block text-sm text-danger-700">慢性病</text>
            <text class="block text-2xl font-bold text-danger-800">{{ healthData.chronic_disease ? '有' : '无' }}</text>
          </view>
        </view>
        <view v-else class="py-8 text-center text-neutral-400">
          <text class="block">暂无健康数据</text>
        </view>
      </view>

      <!-- 紧急联系人 -->
      <view class="bg-white rounded-xl shadow-md p-4 mb-4">
        <view class="flex items-center justify-between mb-4">
          <text class="text-lg font-bold text-primary-800">紧急联系人</text>
          <uni-icons type="plus" size="20" color="primary-500" @click="addEmergencyContact" />
        </view>
        
        <view v-if="emergencyContacts.length > 0">
          <view 
            v-for="contact in emergencyContacts" 
            :key="contact.emergency_contact_id"
            class="flex items-center justify-between p-3 mb-2 border-b border-neutral-100 last:border-0"
          >
            <view>
              <text class="block font-medium text-neutral-900">{{ contact.name }}</text>
              <text class="block text-sm text-neutral-500">{{ contact.phone_number }}</text>
            </view>
            <uni-icons 
              type="phone-filled" 
              size="20" 
              color="primary-500" 
              @click="callContact(contact.phone_number)"
            />
          </view>
        </view>
        <view v-else class="py-8 text-center text-neutral-400">
          <text class="block">暂无紧急联系人</text>
        </view>
      </view>

      <!-- 系统设置 -->
      <view class="bg-white rounded-xl shadow-md p-4">
        <text class="text-lg font-bold text-primary-800 block mb-4">系统设置</text>
        
        <uni-forms :modelValue="systemSettings" label-position="top">
          <uni-forms-item name="reminder_volume" label="提醒音量">
            <slider 
              v-model="systemSettings.reminder_volume" 
              :value="systemSettings.reminder_volume || 50"
              min="0" 
              max="100" 
              activeColor="#2EB8FF"
              backgroundColor="#E0E0E0"
              block-size="16"
              block-color="#2EB8FF"
              @change="handleVolumeChange"
            />
          </uni-forms-item>
          
          <uni-forms-item name="font_size" label="字体大小">
            <slider 
              v-model="systemSettings.font_size" 
              :value="systemSettings.font_size || 16"
              min="12" 
              max="24" 
              activeColor="#2EB8FF"
              backgroundColor="#E0E0E0"
              block-size="16"
              block-color="#2EB8FF"
              @change="handleFontSizeChange"
            />
          </uni-forms-item>
          
          <uni-forms-item name="questionnaire_exported" label="数据导出">
            <view class="flex items-center justify-between">
              <text class="text-sm text-neutral-600">导出健康问卷数据</text>
              <button 
                class="bg-primary-500 text-white rounded-full px-4 text-sm"
                @click="exportQuestionnaire"
                :disabled="systemSettings.questionnaire_exported"
              >
                {{ systemSettings.questionnaire_exported ? '已导出' : '立即导出' }}
              </button>
            </view>
          </uni-forms-item>
        </uni-forms>
      </view>
    </view>

    <!-- 添加联系人弹窗 -->
    <uni-popup ref="addContactPopup" type="center" class="z-50">
      <view class="bg-white rounded-xl p-6 w-80">
        <text class="text-lg font-bold text-primary-800 block mb-4">添加紧急联系人</text>
        
        <uni-forms :modelValue="newContact" label-position="top">
          <uni-forms-item name="name" label="姓名" required>
            <uni-easyinput 
              v-model="newContact.name" 
              type="text" 
              placeholder="请输入姓名"
              class="w-full box-border"
            />
          </uni-forms-item>
          
          <uni-forms-item name="phone_number" label="电话号码" required>
            <uni-easyinput 
              v-model="newContact.phone_number" 
              type="text" 
              placeholder="请输入电话号码"
              class="w-full box-border"
            />
          </uni-forms-item>
          
          <button 
            class="w-full bg-primary-500 text-white rounded-full py-2 mt-4"
            @click="saveEmergencyContact"
          >
            保存联系人
          </button>
        </uni-forms>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 响应式数据
const userInfo = ref({});
const healthData = ref(null);
const emergencyContacts = ref([]);
const systemSettings = ref({
  reminder_volume: 50,
  font_size: 16,
  questionnaire_exported: false
});
const newContact = ref({
  name: '',
  phone_number: ''
});
const addContactPopup = ref(null);

// 获取用户数据
const fetchUserData = async () => {
  const userRes = await proxy.$cf.login.getLoginUser();
  if (userRes.success) {
    userInfo.value = userRes.data;
    await fetchHealthData(userRes.data.user_info_id);
    await fetchEmergencyContacts(userRes.data.user_info_id);
    await fetchSystemSettings(userRes.data.user_info_id);
  }
};

// 获取健康数据
const fetchHealthData = async (userId) => {
  const res = await proxy.$cf.table.list({
    table_name: 'health_questionnaire',
    param: {
      user_info_user_info_id_1: userId
    },
    order: 'creation_time desc',
    limit: 1
  });
  
  if (res.success && res.data.length > 0) {
    healthData.value = res.data[0];
  }
};

// 获取紧急联系人
const fetchEmergencyContacts = async (userId) => {
  const res = await proxy.$cf.table.list({
    table_name: 'emergency_contact',
    param: {
      user_info_user_info_id_1: userId
    }
  });
  
  if (res.success) {
    emergencyContacts.value = res.data;
  }
};

// 获取系统设置
const fetchSystemSettings = async (userId) => {
  const res = await proxy.$cf.table.list({
    table_name: 'system_settings',
    param: {
      user_info_user_info_id_1: userId
    }
  });
  
  if (res.success && res.data.length > 0) {
    systemSettings.value = res.data[0];
  }
};

// 更新问卷
const updateQuestionnaire = () => {
  proxy.$cf.navigate.to({
    url: '/pages/health_questionnaire_update/index',
    type: 'page'
  });
};

// 添加紧急联系人
const addEmergencyContact = () => {
  newContact.value = {
    name: '',
    phone_number: ''
  };
  addContactPopup.value.open();
};

// 保存紧急联系人
const saveEmergencyContact = async () => {
  if (!newContact.value.name || !newContact.value.phone_number) {
    proxy.$cf.toast({ message: '请填写所有必填字段', level: 'error' });
    return;
  }
  
  const userRes = await proxy.$cf.login.getLoginUser();
  if (!userRes.success) return;
  
  const res = await proxy.$cf.table.add({
    table_name: 'emergency_contact',
    param: {
      user_info_user_info_id_1: userRes.data.user_info_id,
      name: newContact.value.name,
      phone_number: newContact.value.phone_number
    }
  });
  
  if (res.success) {
    proxy.$cf.toast({ message: '联系人保存成功', level: 'success' });
    addContactPopup.value.close();
    await fetchEmergencyContacts(userRes.data.user_info_id);
  }
};

// 拨打电话
const callContact = (phoneNumber) => {
  uni.makePhoneCall({
    phoneNumber: phoneNumber
  });
};

// 音量改变
const handleVolumeChange = async (e) => {
  systemSettings.value.reminder_volume = e.detail.value;
  await saveSystemSettings();
};

// 字体大小改变
const handleFontSizeChange = async (e) => {
  systemSettings.value.font_size = e.detail.value;
  await saveSystemSettings();
};

// 保存系统设置
const saveSystemSettings = async () => {
  const userRes = await proxy.$cf.login.getLoginUser();
  if (!userRes.success) return;
  
  // 检查是否已有设置
  const checkRes = await proxy.$cf.table.list({
    table_name: 'system_settings',
    param: {
      user_info_user_info_id_1: userRes.data.user_info_id
    }
  });
  
  if (checkRes.success && checkRes.data.length > 0) {
    // 更新现有设置
    const updateRes = await proxy.$cf.table.update({
      table_name: 'system_settings',
      param: {
        system_settings_id: checkRes.data[0].system_settings_id,
        ...systemSettings.value
      }
    });
    
    if (!updateRes.success) {
      proxy.$cf.toast({ message: '设置更新失败', level: 'error' });
    }
  } else {
    // 创建新设置
    const createRes = await proxy.$cf.table.add({
      table_name: 'system_settings',
      param: {
        user_info_user_info_id_1: userRes.data.user_info_id,
        ...systemSettings.value
      }
    });
    
    if (!createRes.success) {
      proxy.$cf.toast({ message: '设置保存失败', level: 'error' });
    }
  }
};

// 导出问卷数据
const exportQuestionnaire = async () => {
  proxy.$cf.loading.showLoading({ title: '正在导出...' });
  
  try {
    // 模拟导出过程
    await new Promise(resolve => setTimeout(resolve, 1500));
    
    // 更新导出状态
    const userRes = await proxy.$cf.login.getLoginUser();
    if (!userRes.success) return;
    
    const checkRes = await proxy.$cf.table.list({
      table_name: 'system_settings',
      param: {
        user_info_user_info_id_1: userRes.data.user_info_id
      }
    });
    
    if (checkRes.success && checkRes.data.length > 0) {
      await proxy.$cf.table.update({
        table_name: 'system_settings',
        param: {
          system_settings_id: checkRes.data[0].system_settings_id,
          questionnaire_exported: true
        }
      });
      
      systemSettings.value.questionnaire_exported = true;
      proxy.$cf.toast({ message: '导出成功', level: 'success' });
    }
  } catch (error) {
    proxy.$cf.toast({ message: '导出失败', level: 'error' });
  } finally {
    proxy.$cf.loading.hideLoading();
  }
};

onLoad(() => {
  fetchUserData();
});
</script>