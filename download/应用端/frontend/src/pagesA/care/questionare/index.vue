<template>
  <base-layout>
    <view class="theme-health p-4 bg-[linear-gradient(180deg,_#FAFAF8_0%,_#FFFFFF_60%)] min-h-screen">

      <!-- 进度条 -->
      <view class="mb-6">
        <view class="flex justify-between items-center mb-2">
          <text class="text-[14px] text-neutral-600">{{ currentProgress }}%</text>
          <text class="text-[14px] text-[var(--brand-sage-700)]">
            {{ currentQuestionIndex + 1 }}/{{ questions.length }}
          </text>
        </view>
        <view class="h-[10px] bg-neutral-200 rounded-full overflow-hidden">
          <view
              class="h-full transition-all duration-300 bg-[var(--brand-sage-600)]"
              :style="{ width: currentProgress + '%' }"
          />
        </view>
      </view>

      <!-- 当前问题卡片 -->
      <view class="bg-white rounded-[20px] shadow-soft p-6 mb-6 border border-[var(--card-border)]">
        <view class="flex justify-between items-start mb-4">
          <text class="text-[18px] font-extrabold text-neutral-800 leading-snug">
            {{ currentQuestion.text }}
          </text>
          <uni-icons
              type="sound"
              size="24"
              :color="brandSage"
              @click="playQuestionAudio(currentQuestion)"
              class="active:scale-110 transition-transform"
          />
        </view>

        <!-- 问题输入区域 -->
        <uni-forms :modelValue="formData" label-position="top" class="form-styled">
          <uni-forms-item :name="currentQuestion.field" :label="''">
            <!-- 多选 -->
            <template v-if="currentQuestion.type === 'checkbox'">
              <checkbox-group :value="formData[currentQuestion.field]" @change="e => onCheckboxChange(currentQuestion.field, e)">
                <label
                    v-for="option in currentQuestion.options"
                    :key="option.value"
                    class="flex items-center mb-2 option-line"
                >
                  <checkbox :value="option.value" class="cf-checkbox"/>
                  <text class="ml-3 text-[16px] text-neutral-800">{{ option.text }}</text>
                </label>
              </checkbox-group>
            </template>

            <!-- 下拉选择 -->
            <template v-else>
              <uni-data-select
                  v-model="formData[currentQuestion.field]"
                  :localdata="currentQuestion.options"
                  placeholder="Please select an option"
                  clearable
                  class="w-full cf-select"
              />
            </template>
          </uni-forms-item>
        </uni-forms>
      </view>

      <!-- 导航按钮 -->
      <view class="flex flex-col gap-3">
        <view class="flex justify-between">
          <button
              class="btn-secondary flex-1 mr-2"
              :disabled="currentQuestionIndex === 0"
              @click="prevQuestion"
          >
            <uni-icons type="arrow-left" class="mr-1" />
            Last question
          </button>

          <button
              v-if="currentQuestionIndex < questions.length - 1"
              class="btn-primary flex-1"
              @click="nextQuestion"
          >
            Next question
            <uni-icons type="arrow-right" class="ml-1" />
          </button>

          <button
              v-else
              class="btn-primary flex-1 ml-2"
              @click="submitForm"
          >
            <uni-icons type="checkmarkempty" class="mr-1" />
            Submit
          </button>
        </view>
      </view>

      <!-- 保存成功弹窗 -->
      <uni-popup ref="successPopup" type="center">
        <view class="bg-white p-6 rounded-[18px] w-80 shadow-soft border border-[var(--card-border)]">
          <view class="flex flex-col items-center">
            <uni-icons type="headphones" size="48" :color="brandSage" class="mb-4" />
            <text class="text-[18px] font-extrabold text-neutral-800 mb-2">Saved successfully!</text>
            <text class="text-neutral-600 text-center mb-2 text-[14px]">Total Score: {{ formData.total_score }}</text>
            <text class="text-neutral-600 text-center mb-6 text-[14px]">Risk Level: {{ formData.risk_level }}</text>
            <button class="btn-primary w-full" @click="goToCare">
              Return
            </button>
          </view>
        </view>
      </uni-popup>
    </view>
  </base-layout>
</template>

<script setup>
import { ref, computed, getCurrentInstance } from 'vue'
import { onLoad } from '@dcloudio/uni-app'

const { proxy } = getCurrentInstance()

/* 主题主色（用于内联） */
const brandSage = 'var(--brand-sage-600)'

/* ======= 你的原有逻辑（未动） ======= */
const questions = [
  { field: 'adl', text: 'Can you independently complete basic daily activities (such as dressing, bathing and eating)?', type: 'select', required: true, options: [
      { value: 0, text: 'Completely independent' },
      { value: 2, text: 'Need a little help' },
      { value: 3, text: 'Need a lot of help' },
      { value: 4, text: 'Completely dependent' }
    ]},
  { field: 'mobility_out', text: 'Can you go out independently (for a walk, shopping, community activities)?', type: 'select', required: true, options: [
      { value: 0, text: 'Often can' }, { value: 1, text: 'Occasionally' }, { value: 2, text: 'Rarely' }, { value: 3, text: 'Completely impossible' }
    ]},
  { field: 'falls', text: 'Have you ever fallen down in the past year?', type: 'select', required: true, options: [
      { value: 0, text: 'No' }, { value: 2, text: 'Once' }, { value: 4, text: 'Twice or more' }
    ]},
  { field: 'weight_loss', text: 'Have you lost more than 4 kilograms in weight within 3 months?', type: 'select', required: true, options: [
      { value: 0, text: 'No' }, { value: 3, text: 'Yes' }
    ]},
  { field: 'diseases', text: 'Do you have any of the following major diseases?', type: 'checkbox', required: false, options: [
      { value: 'Hypertension', text: 'Hypertension' }, { value: 'Diabetes', text: 'Diabetes' }, { value: 'Heart disease', text: 'Heart disease' },
      { value: 'Stroke', text: 'Stroke' }, { value: 'Gout', text: 'Gout' }, { value: 'Osteoporosis', text: 'Osteoporosis' },
      { value: 'Alzheimer', text: 'Alzheimer' }, { value: 'Depressive disorder', text: 'Depressive disorder' }
    ]},
  { field: 'pa_minutes', text: 'What is your average daily exercise or walking time?', type: 'select', required: true, options: [
      { value: 0, text: 'More than 1 hour' }, { value: 1, text: '30–60 Minutes' }, { value: 2, text: '10–30 Minutes' }, { value: 3, text: 'Less than 10 Minutes' }
    ]},
  { field: 'pa_willingness', text: 'Would you like to try some light exercises (walking, stretching, Tai Chi)?', type: 'select', required: true, options: [
      { value: 0, text: 'Yes' }, { value: 1, text: 'Not sure' }, { value: 2, text: 'No' }
    ]},
  { field: 'flu_vaccine', text: 'Did you get the flu vaccine last year?', type: 'select', required: true, options: [
      { value: 0, text: 'Yes' }, { value: 1, text: 'No' }
    ]},
  { field: 'polypharmacy', text: 'Are you currently taking three or more medications every day?', type: 'select', required: true, options: [
      { value: 0, text: 'No' }, { value: 2, text: 'Yes' }
    ]},
  { field: 'social', text: 'How many times do you communicate with your family or friends each week?', type: 'select', required: true, options: [
      { value: 0, text: 'Almost every day' }, { value: 1, text: '2 to 3 times a week' }, { value: 2, text: '1 time a week' }, { value: 3, text: 'Rarely or none' }
    ]},
  { field: 'fv_serves', text: 'How many servings of fruits/vegetables do you eat approximately every day?', type: 'select', required: true, options: [
      { value: 0, text: '5 or more' }, { value: 1, text: '3–4' }, { value: 2, text: '2–3' }, { value: 3, text: '0–1' }
    ]}
]

const formData = ref({})
const currentQuestionIndex = ref(0)
const currentQuestion = computed(() => questions[currentQuestionIndex.value])
const currentProgress = computed(() => Math.round((currentQuestionIndex.value + 1) / questions.length * 100))
const healthQuestionnaireId = ref(null)
const loginUserId = ref(null)

const initFormData = () => {
  formData.value = {
    adl: '', mobility_out: '', falls: '', weight_loss: '', diseases: [],
    pa_minutes: '', pa_willingness: '', flu_vaccine: '', polypharmacy: '',
    social: '', fv_serves: ''
  }
}
const onCheckboxChange = (field, e) => { formData.value[field] = e.detail.value }

const playQuestionAudio = async (question) => {
  try {
    const res = await proxy.$cf.text.tts({ text: question.text })
    if (res.success) new proxy.$audioPlayer({ src: res.data, autoplay: true })
  } catch { proxy.$cf.toast({ message: 'Voice playback failed!', level: 'error' }) }
}

const prevQuestion = () => { if (currentQuestionIndex.value > 0) currentQuestionIndex.value-- }
const nextQuestion = () => {
  const q = currentQuestion.value
  if (q.required) {
    const val = formData.value[q.field]
    if (val === '' || val === null || val === undefined) {
      proxy.$cf.toast({ message: 'please choose one', level: 'warning' })
      return
    }
  }
  if (currentQuestionIndex.value < questions.length - 1) currentQuestionIndex.value++
}

const computeScore = (answers) => {
  let total = 0
  total += Number(answers.adl || 0)
  total += Number(answers.mobility_out || 0)
  total += Number(answers.falls || 0)
  total += Number(answers.weight_loss || 0)
  const diseaseCount = Math.min(Array.isArray(answers.diseases) ? answers.diseases.length : 0, 6)
  total += diseaseCount
  total += Number(answers.pa_minutes || 0)
  total += Number(answers.pa_willingness || 0)
  total += Number(answers.flu_vaccine || 0)
  total += Number(answers.polypharmacy || 0)
  total += Number(answers.social || 0)
  total += Number(answers.fv_serves || 0)
  let risk = ''
  if (total <= 6) risk = 'Low risk'
  else if (total <= 14) risk = 'Moderate risk'
  else if (total <= 22) risk = 'High risk'
  else risk = 'Extremely high risk'
  return { totalScore: total, riskLevel: risk }
}

const submitForm = async () => {
  for (const q of questions) {
    const val = formData.value[q.field]
    if (q.required && (val === '' || val === null || val === undefined)) {
      proxy.$cf.toast({ message: `Please finish${q.text}`, level: 'error' })
      return
    }
  }
  try {
    proxy.$cf.loading.showLoading({ title: 'Saving...' })
    const { totalScore, riskLevel } = computeScore(formData.value)
    formData.value.total_score = totalScore
    formData.value.risk_level = riskLevel

    if (Array.isArray(formData.value.diseases)) {
      formData.value.diseases = formData.value.diseases.join(',')
    }

    formData.value.user_info_user_info_id_1 = loginUserId.value
    const now = new Date()
    formData.value.update_time = now.toISOString().slice(0, 19).replace('T', ' ')

    if (healthQuestionnaireId.value) {
      formData.value.health_questionnaire_id = healthQuestionnaireId.value
      const res = await proxy.$cf.table.update({ table_name: 'health_questionnaire', param: formData.value })
      if (res.success) proxy.$refs.successPopup.open()
    } else {
      formData.value.creation_time = now.toISOString().slice(0, 19).replace('T', ' ')
      const res = await proxy.$cf.table.add({ table_name: 'health_questionnaire', param: formData.value })
      if (res.success) {
        healthQuestionnaireId.value = res.data
        proxy.$refs.successPopup.open()
      }
    }
  } catch {
    proxy.$cf.toast({ message: 'Failed to save the questionnaire', level: 'error' })
  } finally {
    proxy.$cf.loading.hideLoading()
  }
}

const goToCare = () => {
  proxy.$refs.successPopup.close()
  proxy.$cf.navigate.to({ url: '/pagesA/care/index', type: 'page' })
}

const loadQuestionnaireData = async (id) => {
  try {
    const res = await proxy.$cf.request('/api/data/invoke', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      data: { table: 'health_questionnaire', method: 'detail', id }
    })
    if (res.success && res.data) {
      questions.forEach(q => {
        if (res.data[q.field] !== undefined) formData.value[q.field] = res.data[q.field]
      })
    }
  } catch {
    proxy.$cf.toast({ message: 'Failed to load the questionnaire', level: 'error' })
  }
}

const getLoginUser = async () => {
  const res = await proxy.$cf.login.getLoginUser()
  if (res.success && res.data) loginUserId.value = res.data.user_info_id
}

onLoad(async (options) => {
  initFormData()
  await getLoginUser()
  if (options.health_questionnaire_id) {
    healthQuestionnaireId.value = options.health_questionnaire_id
    await loadQuestionnaireData(options.health_questionnaire_id)
  }
})
</script>

<style scoped>
/* ===== 主题色（与其它页面统一） ===== */
.theme-health{
  --brand-sage-700:#3F6D5A;
  --brand-sage-600:#6FA08F;
  --brand-sage-500:#90B2A1;
  --card-border:#EEEEEE;
}

/* 软阴影，与登录页一致 */
.shadow-soft{ box-shadow:0 8px 28px rgba(0,0,0,0.08); }

/* ===== 统一按钮样式 ===== */
.btn-primary{
  height:48px; border-radius:9999px;
  background:var(--brand-sage-600); color:#fff;
  font-weight:800; font-size:16px;
  display:flex; align-items:center; justify-content:center;
  box-shadow:0 6px 16px rgba(111,160,143,.35);
}
.btn-primary:active{ transform:translateY(.5px) scale(.99); }

.btn-secondary{
  height:48px; border-radius:9999px;
  background:#E5E7EB; color:#374151;
  font-weight:700; font-size:16px;
  display:flex; align-items:center; justify-content:center;
}

/* ===== 下拉/多选统一外观（和表单风格一致） ===== */
:deep(.cf-select){
  --bd:#E7E7E7;
  display:block;
  border:1px solid var(--bd);
  border-radius:14px;
  padding:2px 10px;
  background:#FAFAFA;
  font-size:16px;
  color:#111827;
}
:deep(.uni-data-select .uni-select__selector){
  font-size:16px !important;
}
.option-line{ min-height:40px; }

/* 放大 checkbox，主色为 Sage */
:deep(.cf-checkbox){ transform:scale(1.25); }
:deep(.uni-checkbox-input){
  border-radius:6px !important; border-color:var(--brand-sage-600) !important;
}
:deep(.uni-checkbox-input.uni-checkbox-input-checked){
  background:var(--brand-sage-600) !important; border-color:var(--brand-sage-600) !important;
}
</style>
