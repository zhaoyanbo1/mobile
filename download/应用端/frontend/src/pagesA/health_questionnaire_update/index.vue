<template>
  <base-layout>
    <view class="p-4 bg-neutral-50 min-h-screen">
      <!-- 进度条 -->
      <view class="mb-6">
        <view class="flex justify-between items-center mb-2">
          <text class="text-sm text-neutral-600">进度: {{ currentProgress }}%</text>
          <text class="text-sm text-primary-500">{{ currentQuestionIndex + 1 }}/{{ questions.length }}</text>
        </view>
        <view class="h-2 bg-neutral-200 rounded-full overflow-hidden">
          <view 
            class="h-full bg-primary-500 transition-all duration-300" 
            :style="{ width: currentProgress + '%' }"
          ></view>
        </view>
      </view>

      <!-- 当前问题卡片 -->
      <view class="bg-white rounded-xl shadow-lg p-6 mb-6">
        <view class="flex justify-between items-start mb-4">
          <text class="text-lg font-semibold text-neutral-800">{{ currentQuestion.text }}</text>
          <uni-icons 
            type="sound" 
            size="24" 
            color="#009EFF"
            @click="playQuestionAudio(currentQuestion)"
            class="active:scale-110 transition-transform"
          />
        </view>

        <!-- 问题输入区域 -->
        <uni-forms :modelValue="formData" label-position="top">
          <uni-forms-item :name="currentQuestion.field" :label="currentQuestion.text">
            <template v-if="currentQuestion.type === 'string' && currentQuestion.options">
              <uni-data-select
                v-model="formData[currentQuestion.field]"
                :localdata="currentQuestion.options"
                placeholder="请选择"
                clearable
                class="w-full"
              />
            </template>
            <template v-else-if="currentQuestion.type === 'number' && currentQuestion.options">
              <uni-data-select
                v-model="formData[currentQuestion.field]"
                :localdata="currentQuestion.options"
                placeholder="请选择"
                clearable
                class="w-full"
              />
            </template>
            <template v-else>
              <uni-easyinput 
                type="text" 
                v-model="formData[currentQuestion.field]" 
                :placeholder="'请输入' + currentQuestion.text"
                class="w-full box-border"
              />
            </template>
          </uni-forms-item>
        </uni-forms>
      </view>

      <!-- 导航按钮 -->
      <view class="flex flex-col gap-3">
        <view class="flex justify-between">
          <button 
            class="bg-neutral-200 text-neutral-700 rounded-full px-4 flex-1 mr-2"
            :disabled="currentQuestionIndex === 0"
            @click="prevQuestion"
          >
            <uni-icons type="arrow-left" class="mr-1" />
            上一题
          </button>
          <button 
            v-if="currentQuestionIndex < questions.length - 1"
            class="bg-primary-500 text-white rounded-full px-4 flex-1 ml-2"
            @click="nextQuestion"
          >
            下一题
            <uni-icons type="arrow-right" class="ml-1" />
          </button>
          <button 
            v-else
            class="bg-success-500 text-white rounded-full px-4 flex-1 ml-2"
            @click="submitForm"
          >
            <uni-icons type="checkmarkempty" class="mr-1" />
            提交
          </button>
        </view>
      </view>

      <!-- 保存成功弹窗 -->
      <uni-popup ref="successPopup" type="center">
        <view class="bg-white p-6 rounded-xl w-80">
          <view class="flex flex-col items-center">
            <uni-icons type="headphones" size="48" color="#1BAA5F" class="mb-4" />
            <text class="text-xl font-semibold text-neutral-800 mb-2">保存成功!</text>
            <text class="text-neutral-600 text-center mb-6">您的健康问卷已成功保存</text>
            <button 
              class="bg-success-500 text-white rounded-full px-8 w-full"
              @click="goToHome"
            >
              返回首页
            </button>
          </view>
        </view>
      </uni-popup>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

// 问卷问题定义，除姓名外均为下拉选择，包含预设选项
const questions = [
  {
    field: 'name',
    text: '姓名',
    type: 'string',
    required: true
  },
  {
    field: 'age',
    text: '年龄段',
    type: 'number',
    required: true,
    options: [
      { value: 60, text: '60-64岁' },
      { value: 65, text: '65-69岁' },
      { value: 70, text: '70-74岁' },
      { value: 75, text: '75-79岁' },
      { value: 80, text: '80岁及以上' }
    ]
  },
  {
    field: 'residence',
    text: '居住情况',
    type: 'string',
    required: false,
    options: [
      { value: '独居', text: '独居' },
      { value: '与家人同住', text: '与家人同住' },
      { value: '养老院', text: '养老院' },
      { value: '其他', text: '其他' }
    ]
  },
  {
    field: 'chronic_disease',
    text: '慢性病',
    type: 'string',
    required: false,
    options: [
      { value: '无', text: '无' },
      { value: '高血压', text: '高血压' },
      { value: '糖尿病', text: '糖尿病' },
      { value: '冠心病', text: '冠心病' },
      { value: '关节炎', text: '关节炎' },
      { value: '哮喘', text: '哮喘' },
      { value: '其他', text: '其他' }
    ]
  },
  {
    field: 'allergy_history',
    text: '过敏史',
    type: 'string',
    required: false,
    options: [
      { value: '无', text: '无' },
      { value: '花粉过敏', text: '花粉过敏' },
      { value: '青霉素', text: '青霉素' },
      { value: '尘螨', text: '尘螨' },
      { value: '阿司匹林', text: '阿司匹林' },
      { value: '其他', text: '其他' }
    ]
  },
  {
    field: 'common_medication',
    text: '常用药',
    type: 'string',
    required: false,
    options: [
      { value: '无', text: '无' },
      { value: '降压药', text: '降压药' },
      { value: '胰岛素', text: '胰岛素' },
      { value: '止痛药', text: '止痛药' },
      { value: '吸入剂', text: '吸入剂' },
      { value: '阿司匹林', text: '阿司匹林' },
      { value: '维生素D', text: '维生素D' }
    ]
  },
  {
    field: 'diet_preference',
    text: '饮食偏好',
    type: 'string',
    required: false,
    options: [
      { value: '清淡', text: '清淡' },
      { value: '低糖', text: '低糖' },
      { value: '低盐', text: '低盐' },
      { value: '高蛋白', text: '高蛋白' },
      { value: '低脂', text: '低脂' },
      { value: '均衡', text: '均衡' }
    ]
  },
  {
    field: 'exercise_frequency',
    text: '运动频率',
    type: 'string',
    required: false,
    options: [
      { value: '每天', text: '每天' },
      { value: '每周3次', text: '每周3次' },
      { value: '每周5次', text: '每周5次' },
      { value: '偶尔', text: '偶尔' },
      { value: '无', text: '无' }
    ]
  }
];

const formData = ref({});
const currentQuestionIndex = ref(0);
const currentQuestion = computed(() => questions[currentQuestionIndex.value]);
const currentProgress = computed(() => Math.round((currentQuestionIndex.value + 1) / questions.length * 100));
const healthQuestionnaireId = ref(null);
const loginUserId = ref(null);

// 初始化表单数据
const initFormData = () => {
  const data = {};
  questions.forEach(q => {
    data[q.field] = '';
  });
  formData.value = data;
};

// 播放问题语音
const playQuestionAudio = async (question) => {
  try {
    const res = await proxy.$cf.text.tts({
      text: question.text
    });
    if (res.success) {
      const audio = new proxy.$audioPlayer({
        src: res.data,
        autoplay: true
      });
    }
  } catch (error) {
    proxy.$cf.toast({ message: '语音播放失败', level: 'error' });
  }
};

// 上一个问题
const prevQuestion = () => {
  if (currentQuestionIndex.value > 0) {
    currentQuestionIndex.value--;
  }
};

// 下一个问题
const nextQuestion = () => {
  if (currentQuestionIndex.value < questions.length - 1) {
    currentQuestionIndex.value++;
  }
};

// 提交表单
const submitForm = async () => {
  try {
    proxy.$cf.loading.showLoading({ title: '保存中...' });
    
    // 设置用户ID和当前时间
    formData.value.user_info_user_info_id_1 = loginUserId.value;
    const now = new Date();
    formData.value.update_time = now.toISOString().slice(0, 19).replace('T', ' ');
    
    if (healthQuestionnaireId.value) {
      // 更新现有问卷
      formData.value.health_questionnaire_id = healthQuestionnaireId.value;
      const res = await proxy.$cf.table.update({
        table_name: 'health_questionnaire',
        param: formData.value
      });
      
      if (res.success) {
        proxy.$refs.successPopup.open();
      }
    } else {
      // 创建新问卷
      formData.value.creation_time = now.toISOString().slice(0, 19).replace('T', ' ');
      const res = await proxy.$cf.table.add({
        table_name: 'health_questionnaire',
        param: formData.value
      });
      
      if (res.success) {
        healthQuestionnaireId.value = res.data;
        proxy.$refs.successPopup.open();
      }
    }
  } catch (error) {
    proxy.$cf.toast({ message: '保存问卷失败', level: 'error' });
  } finally {
    proxy.$cf.loading.hideLoading();
  }
};

// 返回首页
const goToHome = () => {
  proxy.$refs.successPopup.close();
  proxy.$cf.navigate.to({
    url: '/pages/home/index',
    type: 'page'
  });
};

// 加载现有问卷数据
const loadQuestionnaireData = async (id) => {
  try {
    const res = await proxy.$cf.table.get({
      table_name: 'health_questionnaire',
      param: { health_questionnaire_id: id }
    });
    
    if (res.success && res.data) {
      // 填充表单数据
      questions.forEach(q => {
        if (res.data[q.field] !== undefined) {
          formData.value[q.field] = res.data[q.field];
        }
      });
    }
  } catch (error) {
    proxy.$cf.toast({ message: '加载问卷失败', level: 'error' });
  }
};

// 获取当前登录用户
const getLoginUser = async () => {
  const res = await proxy.$cf.login.getLoginUser();
  if (res.success && res.data) {
    loginUserId.value = res.data.user_info_id;
  }
};

onLoad(async (options) => {
  initFormData();
  await getLoginUser();
  
  if (options.health_questionnaire_id) {
    healthQuestionnaireId.value = options.health_questionnaire_id;
    await loadQuestionnaireData(options.health_questionnaire_id);
  }
});
</script>
