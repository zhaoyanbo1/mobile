<template>
  <base-layout>
    <view class="min-h-screen flex flex-col items-center justify-center bg-gradient-to-b from-primary-50 to-white p-6">
      <!-- App Logo -->
      <view class="mb-12 flex flex-col items-center">
        <image 
          src="https://www.codeflying.net/preview/expert-health.jpg" 
          mode="aspectFill" 
          class="w-32 h-32 rounded-full shadow-lg mb-4 border-4 border-white"
        />
        <text class="text-3xl font-bold text-primary-700">健康伴侣</text>
        <text class="text-neutral-500 mt-2">老年人健康管理助手</text>
      </view>

      <!-- Login Form -->
      <view class="w-full max-w-md bg-white rounded-2xl shadow-xl p-8 mb-8">
        <text class="text-2xl font-bold text-center text-neutral-800 mb-8">欢迎回来</text>
        
        <base-login 
          login_type="passwd" 
          show_title="登录您的账户"
          relevanceTable="user_info"
          @loginSuccess="handleLoginSuccess"
          @loginFail="handleLoginFail"
        >
          <template #footer>
            <view class="mt-6 text-center">
              <text class="text-sm text-neutral-500">还没有账号？</text>
              <text 
                class="text-sm font-medium text-primary-600 underline"
                @click="showRegisterPopup"
              >
                立即注册
              </text>
            </view>
          </template>
        </base-login>
      </view>

      <!-- 功能特点 -->
      <view class="w-full max-w-md">
        <view class="grid grid-cols-3 gap-4">
          <view class="flex flex-col items-center p-4 bg-white rounded-xl shadow-sm">
            <uni-icons type="calendar-filled" size="24" color="#2EB8FF" />
            <text class="mt-2 text-xs text-center text-neutral-600">用药提醒</text>
          </view>
          <view class="flex flex-col items-center p-4 bg-white rounded-xl shadow-sm">
            <uni-icons type="heart-filled" size="24" color="#FF3B3B" />
            <text class="mt-2 text-xs text-center text-neutral-600">健康追踪</text>
          </view>
          <view class="flex flex-col items-center p-4 bg-white rounded-xl shadow-sm">
            <uni-icons type="location-filled" size="24" color="#1BAA5F" />
            <text class="mt-2 text-xs text-center text-neutral-600">活动指南</text>
          </view>
        </view>
      </view>

      <!-- 注册弹窗 -->
      <uni-popup ref="registerPopup" type="center">
        <view class="bg-white p-6 rounded-xl w-80">
          <text class="text-xl font-bold text-neutral-800 mb-4">用户注册</text>
          
          <uni-forms :modelValue="registerForm" label-position="top">
            <uni-forms-item required label="手机号" name="phone_number">
              <uni-easyinput type="text" v-model="registerForm.phone_number" placeholder="请输入手机号"/>
            </uni-forms-item>
            
            <uni-forms-item required label="用户名" name="username">
              <uni-easyinput type="text" v-model="registerForm.username" placeholder="请输入用户名"/>
            </uni-forms-item>
            
            <uni-forms-item required label="密码" name="password">
              <uni-easyinput type="password" v-model="registerForm.password" placeholder="请输入密码"/>
            </uni-forms-item>
            
            <button 
              class="w-full bg-primary-500 text-white rounded-full mt-4"
              @click="handleRegister"
            >
              注册
            </button>
          </uni-forms>
        </view>
      </uni-popup>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const registerForm = ref({
  phone_number: '',
  username: '',
  password: ''
});

const registerPopup = ref(null);

const handleLoginSuccess = () => {
  proxy.$cf.toast({
    message: '登录成功',
    level: 'success',
    duration: 1500
  });
  
  setTimeout(() => {
    proxy.$cf.navigate.to({
      url: '/pages/home/index',
      type: 'page'
    });
  }, 1500);
};

const handleLoginFail = () => {
  proxy.$cf.toast({
    message: '登录失败，请检查账号密码',
    level: 'error'
  });
};

const showRegisterPopup = () => {
  registerPopup.value.open();
};

const handleRegister = async () => {
  if (!registerForm.value.phone_number || !registerForm.value.password) {
    proxy.$cf.toast({
      message: '手机号和密码不能为空',
      level: 'error'
    });
    return;
  }

  try {
    const res = await proxy.$cf.register.register({
      table_name: 'user_info',
      param: registerForm.value
    });

    if (res.success) {
      proxy.$cf.toast({
        message: '注册成功',
        level: 'success'
      });
      registerPopup.value.close();
    }
  } catch (error) {
    proxy.$cf.toast({
      message: '注册失败',
      level: 'error'
    });
  }
};
</script>