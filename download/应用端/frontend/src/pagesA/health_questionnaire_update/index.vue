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
            <template v-if="currentQuestion.type === 'checkbox'">
              <checkbox-group :value="formData[currentQuestion.field]" @change="e => onCheckboxChange(currentQuestion.field, e)">
                <label v-for="option in currentQuestion.options" :key="option.value" class="flex items-center mb-2">
                  <checkbox :value="option.value" />
                  <text class="ml-2">{{ option.text }}</text>
                </label>
              </checkbox-group>
            </template>
<!--            <template v-else-if="currentQuestion.type === 'number' && currentQuestion.options">-->
            <template v-else>
              <uni-data-select
                v-model="formData[currentQuestion.field]"
                :localdata="currentQuestion.options"
                placeholder="Please select an option"
                clearable
                class="w-full"
              />
            </template>
<!--            <template v-else>-->
<!--              <uni-easyinput -->
<!--                type="text" -->
<!--                v-model="formData[currentQuestion.field]" -->
<!--                :placeholder="'请输入' + currentQuestion.text"-->
<!--                class="w-full box-border"-->
<!--              />-->
<!--            </template>-->
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
            <text class="text-xl font-semibold text-neutral-800 mb-2">Saved successfully!</text>
<!--            <text class="text-neutral-600 text-center mb-6">您的健康问卷已成功保存</text>-->
<!--            <button -->
            <text class="text-neutral-600 text-center mb-2">Total Score: {{ formData.total_score }}</text>
            <text class="text-neutral-600 text-center mb-6">Risk Level: {{ formData.risk_level }}</text>
            <button
              class="bg-success-500 text-white rounded-full px-8 w-full"
              @click="goToHome"
            >
              Return to the homepage
            </button>
          </view>
        </view>
      </uni-popup>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const questions = [
  {
    field: 'adl',
    text: 'Can you independently complete basic daily activities (such as dressing, bathing and eating)?',
    type: 'select',
    required: true,
    options: [
      { value: 0, text: 'Completely independent' },
      { value: 2, text: 'Need a little help' },
      { value: 3, text: 'Need a lot of help' },
      { value: 4, text: 'Completely dependent' }
    ]
  },
  {
    field: 'mobility_out',
    text: 'Can you go out independently (for a walk, shopping, community activities)?',
    type: 'select',
    required: true,
    options: [
      { value: 0, text: 'Often can' },
      { value: 1, text: 'Occasionally' },
      { value: 2, text: 'Rarely' },
      { value: 3, text: 'Completely impossible' }
    ]
  },
  {
    field: 'falls',
    text: 'Have you ever fallen down in the past year?',
    type: 'select',
    required: true,
    options: [
      { value: 0, text: 'No' },
      { value: 2, text: 'Once' },
      { value: 4, text: 'Twice or more' }
    ]
  },
  {
    field: 'weight_loss',
    text: 'Have you lost more than 4 kilograms in weight within 3 months?',
    type: 'select',
    required: true,
    options: [
      { value: 0, text: 'No' },
      { value: 3, text: 'Yes' }
    ]
  },
  {
    field: 'diseases',
    text: 'Do you have any of the following major diseases?',
    type: 'checkbox',
    required: false,
    options: [
      // { value: '无', text: '无' },
      { value: 'Hypertension', text: 'Hypertension' },
      { value: 'Diabetes', text: 'Diabetes' },
      { value: 'Heart disease', text: 'Heart disease' },
      { value: 'Stroke', text: 'Stroke' },
      { value: 'Gout', text: 'Gout' },
      { value: 'Osteoporosis', text: 'Osteoporosis' },
      { value: 'Alzheimer', text: 'Alzheimer' },
      { value: 'Depressive disorder', text: 'Depressive disorder' }
    ]
  },
  {
    field: 'pa_minutes',
    text: 'What is your average daily exercise or walking time?',
    type: 'select',
    required: true,
    options: [
      { value: 0, text: 'More than 1 hour' },
      { value: 1, text: '30–60 Minutes' },
      { value: 2, text: '10–30 Minutes' },
      { value: 3, text: 'Less than 10 Minutes' }
    ]
  },
  {
    field: 'pa_willingness',
    text: 'Would you like to try some light exercises (walking, stretching, Tai Chi)?',
    type: 'select',
    required: true,
    options: [
      { value: 0, text: 'Yes(0)' },
      { value: 1, text: 'Not sure(1)' },
      { value: 2, text: 'No(2)' }
    ]
  },
  {
    field: 'flu_vaccine',
    text: 'Did you get the flu vaccine last year?',
    type: 'select',
    required: true,
    options: [
      { value: 0, text: 'Yes' },
      { value: 1, text: 'No' }
    ]
  },
  {
    field: 'polypharmacy',
    text: 'Are you currently taking three or more medications every day?',
    type: 'select',
    required: true,
    options: [
      { value: 0, text: 'No' },
      { value: 2, text: 'Yes' }
    ]
  },
  {
    field: 'social',
    text: 'How many times do you communicate with your family or friends each week?',
    type: 'select',
    required: true,
    options: [
      { value: 0, text: 'Almost every day' },
      { value: 1, text: '2 to 3 times a week' },
      { value: 2, text: '1 time a week' },
      { value: 3, text: 'Rarely or none' }
    ]
  },
  {
    field: 'fv_serves',
    text: 'How many servings of fruits/vegetables do you eat approximately every day?',
    type: 'select',
    required: true,
    options: [
      { value: 0, text: '5 or more' },
      { value: 1, text: '3–4' },
      { value: 2, text: '2–3' },
      { value: 3, text: '0–1' }
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
  // const data = {};
  // questions.forEach(q => {
  //   data[q.field] = '';
  // });
  // formData.value = data;
  formData.value = {
    adl: '',
    mobility_out: '',
    falls: '',
    weight_loss: '',
    diseases: [],
    pa_minutes: '',
    pa_willingness: '',
    flu_vaccine: '',
    polypharmacy: '',
    social: '',
    fv_serves: ''
  };
};

const onCheckboxChange = (field, e) => {
  formData.value[field] = e.detail.value;
};

// 播放问题语音
const playQuestionAudio = async (question) => {
  try {
    const res = await proxy.$cf.text.tts({ text: question.text });
    if (res.success) {
      new proxy.$audioPlayer({ src: res.data, autoplay: true });
    }
  } catch (error) {
    proxy.$cf.toast({ message: 'Voice playback failed!', level: 'error' });
  }
};

// 上一个问题
const prevQuestion = () => {
  if (currentQuestionIndex.value > 0) currentQuestionIndex.value--;
};

// 下一个问题
const nextQuestion = () => {
  const q = currentQuestion.value;
  if (q.required) {
    const val = formData.value[q.field];
    if (val === '' || val === null || val === undefined) {
      proxy.$cf.toast({ message: 'please choose one', level: 'warning' });
      return;
    }
  }
  if (currentQuestionIndex.value < questions.length - 1) currentQuestionIndex.value++;
};

const computeScore = (answers) => {
  let total = 0;
  total += Number(answers.adl || 0);
  total += Number(answers.mobility_out || 0);
  total += Number(answers.falls || 0);
  total += Number(answers.weight_loss || 0);
  const diseaseCount = Math.min(Array.isArray(answers.diseases) ? answers.diseases.length : 0, 6);
  total += diseaseCount;
  total += Number(answers.pa_minutes || 0);
  total += Number(answers.pa_willingness || 0);
  total += Number(answers.flu_vaccine || 0);
  total += Number(answers.polypharmacy || 0);
  total += Number(answers.social || 0);
  total += Number(answers.fv_serves || 0);
  let risk = '';
  if (total <= 6) risk = 'Low risk';
  else if (total <= 14) risk = 'Moderate risk';
  else if (total <= 22) risk = 'High risk';
  else risk = 'Extremely high risk';
  return { totalScore: total, riskLevel: risk };
};

// 提交表单
const submitForm = async () => {
  for (const q of questions) {
    const val = formData.value[q.field];
    if (q.required && (val === '' || val === null || val === undefined)) {
      proxy.$cf.toast({ message: `Please finish${q.text}`, level: 'error' });
      return;
    }
  }
  try {
    proxy.$cf.loading.showLoading({ title: 'Saving...' });

    // 计算分数与风险等级并写回到表单
    const { totalScore, riskLevel } = computeScore(formData.value);
    formData.value.total_score = totalScore;
    formData.value.risk_level = riskLevel;


    if (Array.isArray(formData.value.diseases)) {
      formData.value.diseases = formData.value.diseases.join(',');
    }

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
        //更新后重新加载一次，确保和DB保持一致
        // await loadQuestionnaireData(healthQuestionnaireId.value);
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
        // await loadQuestionnaireData(healthQuestionnaireId.value);
        proxy.$refs.successPopup.open();
      }
    }
  } catch (error) {
    proxy.$cf.toast({ message: 'Failed to save the questionnaire', level: 'error' });
  } finally {
    proxy.$cf.loading.hideLoading();
  }
};

// 返回首页
const goToHome = () => {
  proxy.$refs.successPopup.close();
  proxy.$cf.navigate.to({ url: '/pages/home/index', type: 'page' });
};

// 加载现有问卷数据
const loadQuestionnaireData = async (id) => {
  try {
    const res = await proxy.$cf.request('/api/data/invoke', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: {
        table: 'health_questionnaire',
        method: 'detail',
        id: id
      }
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
    proxy.$cf.toast({ message: 'Failed to load the questionnaire', level: 'error' });
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
