<template>
  <!-- H5 preview (optional) -->
  <web-view id="foo" v-if="isTrue" :src="previewUrl" class="w-[640px] h-[480px]"></web-view>

  <!-- Password login -->
  <view v-if="login_type === 'passwd'" class="w-full flex flex-col items-center p-4 box-border">
    <view class="w-full max-w-[320px] text-[15px] flex justify-start border-b border-neutral-200 mb-5">
      <h2 class="text-primary-500 mb-2">{{ show_title }}</h2>
    </view>

    <uni-forms
        ref="formRef"
        class="max-w-[320px] w-full mx-auto"
        :modelValue="form"
        :rules="rules"
        label-width="auto"
        status-icon
    >
      <uni-forms-item name="phone">
        <view class="w-full flex items-center justify-center relative">
          <uni-easyinput
              :placeholder="$t('login.enter_user_name')"
              v-model="form.phone"
              class="placeholder-neutral-400 bg-neutral-50 rounded-[30px] border-none"
          />
        </view>
      </uni-forms-item>

      <uni-forms-item name="password">
        <view class="w-full flex items-center justify-center relative">
          <uni-easyinput
              :placeholder="$t('login.enter_pass_wd')"
              type="password"
              v-model="form.password"
              class="placeholder-neutral-400 bg-neutral-50 rounded-[30px] border-none"
          />
        </view>
      </uni-forms-item>

      <uni-forms-item>
        <button
            id="login_ref_button_kf"
            class="bg-primary-500 hover:bg-primary-600 text-white w-full h-[55px] mt-9 rounded-[30px]
                 shadow-md flex items-center justify-center transition-colors duration-300"
            @click="submitForm"
        >
          {{ $t('login.login_btn') }}
        </button>
      </uni-forms-item>
    </uni-forms>

    <!-- footer slot (shows Sign up button from parent) -->
    <view class="max-w-[320px] w-full mx-auto mt-4">
      <slot name="footer"></slot>
    </view>

    <h5 v-show="is_register" @click="to_page" class="text-primary-500 text-[13px] font-medium cursor-pointer mt-5">
      <span class="text-text-primary-900 font-medium">{{ $t('login.login_msg') }}</span>
      {{ $t('login.login_reg') }}
    </h5>
  </view>

  <!-- SMS login -->
  <view v-else-if="login_type === 'sms'" class="flex flex-col items-center p-4 box-border">
    <view class="w-full max-w-[320px] text-[15px] flex justify-start border-b border-neutral-200 mb-5">
      <h2 class="text-primary-500 mb-2">{{ show_title }}</h2>
    </view>

    <uni-forms
        :modelValue="form_sms"
        :rules="rules_sms"
        ref="loginForm_sms"
        class="max-w-[320px] w-full mx-auto"
        label-width="auto"
        status-icon
    >
      <uni-forms-item name="phone">
        <view class="w-full flex items-center justify-center relative">
          <uni-easyinput
              v-model="form_sms.phone"
              :placeholder="$t('login.enter_phone')"
              type="number"
              maxlength="11"
              class="placeholder-neutral-400 bg-neutral-50 rounded-[30px] border-none"
          />
        </view>
      </uni-forms-item>

      <uni-forms-item name="code">
        <view class="w-full flex items-center justify-center relative">
          <uni-easyinput
              class="w-[50px] placeholder-neutral-400 bg-neutral-50 rounded-[30px] border-none"
              v-model="form_sms.code"
              :placeholder="$t('login.enter_verification_code')"
          />
          <button
              class="absolute right-0 bg-transparent text-primary-600 text-[12px] border-none"
              @click="sendcode"
              :disabled="countdown > 0"
          >
            {{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
          </button>
        </view>
      </uni-forms-item>

      <button
          class="bg-primary-500 hover:bg-primary-600 text-white w-full h-[55px] mt-9 rounded-[30px]
               shadow-md flex items-center justify-center transition-colors duration-300"
          @click="submitForm_sms"
      >
        {{ $t('login.login_btn') }}
      </button>
    </uni-forms>

    <!-- footer slot for SMS mode as well -->
    <view class="max-w-[320px] w-full mx-auto mt-4">
      <slot name="footer"></slot>
    </view>

    <h5 v-show="is_register" @click="to_page" class="text-primary-500 text-[13px] font-medium cursor-pointer mt-5">
      <span class="text-text-primary-900 font-medium">{{ $t('login.login_msg') }}</span>
      {{ $t('login.login_reg') }}
    </h5>
  </view>

  <!-- H5 login -->
  <view v-else-if="login_type === 'h5'" class="flex flex-col items-center p-4 box-border">
    <button class="bg-success-500 hover:bg-success-600 text-white rounded-full px-8 py-3 text-lg" @click="login_click">
      {{ $t('login.wx_login') }}
    </button>
  </view>

  <!-- Wechat login -->
  <view v-else class="flex flex-col items-center p-4 box-border">
    <view class="w-full h-[188px] flex justify-center items-center text-[22px] font-medium text-text-primary-800">
      {{ props.title }}
    </view>

    <button
        class="bg-primary-500 hover:bg-primary-600 text-white rounded-[10px] w-[512rpx] h-[88rpx] text-[36rpx] flex items-center justify-center"
        open-type="getPhoneNumber"
        @getphonenumber="getPhoneNumber"
        v-if="radio1"
    >
      {{ $t('login.wx_login') }}
    </button>

    <button
        class="bg-primary-500 hover:bg-primary-600 text-white rounded-[10px] w-[512rpx] h-[88rpx] text-[36rpx] flex items-center justify-center"
        v-else
        @click="btnF1"
    >
      {{ $t('login.wx_login') }}
    </button>

    <view class="radios flex justify-center text-[28rpx] mt-[48rpx] h-[300rpx]">
      <label class="radio flex justify-center items-center">
        <radio class="scale-[0.74] mr-[18rpx]" @click="isCheck" value="radio1" :checked="radio1" />
        <span class="text-neutral-400">
          {{ $t('login.read_and_agree') }}
          <span class="text-primary-500 hover:text-primary-600 cursor-pointer" @click.stop="services">
            《{{ $t('login.service_agreement') }}》
          </span>
          <span>及</span>
          <span class="text-primary-500 hover:text-primary-600 cursor-pointer" @click.stop="policy">
            《{{ $t('login.privacy_agreement') }}》
          </span>
        </span>
      </label>
    </view>

    <view class="text-neutral-400 text-[13px] mt-[166px]">
      {{ end_content_show }}
    </view>
  </view>

  <view
      v-if="login_type === 'passwd' || login_type === 'sms' || login_type === 'h5'"
      class="w-full flex justify-center text-[12px] text-neutral-400 mb-1"
  >
    {{ end_content_show }}
  </view>
</template>

<!-- Keep UniApp styleIsolation option -->
<script>
export default {
  options: {
    styleIsolation: 'shared'
  }
}
</script>

<script setup>
// Comments in English for readability
import { getCurrentInstance, ref } from 'vue'
import { onShow, onLoad } from '@dcloudio/uni-app'
import service from '@/utils/request.js'

const { proxy } = getCurrentInstance()

/* ---------- UI state ---------- */
const radio1 = ref(false)
const isTrue = ref(false) // web-view toggle if needed
const previewUrl = ref('')

const isCheck = () => {
  radio1.value = !radio1.value
}

/* ---------- Emits & Props ---------- */
const emit = defineEmits(['loginSuccess', 'loginFail'])

const props = defineProps({
  login_type: { type: String, default: 'passwd' },
  show_title: { type: String, default: 'Login' }, // fixed typo
  relevanceTable: { type: String, required: true },
  is_register: { type: [String, Boolean], required: false, default: '' },
  register_page: { type: String, required: false, default: '' },
  title: { type: String, required: false, default: '' },
  end_content: { type: String, required: false, default: '' }
})

/* ---------- Footer text logic ---------- */
const end_content_show = ref('')
let left_title = import.meta.env.VITE_APP_NAME
let app_vip = import.meta.env.VITE_APP_VIP === 'true'
if (app_vip) {
  end_content_show.value = ''
} else {
  end_content_show.value = props.end_content
  left_title = 'CodeFlying!'
}

/* ---------- Navigation & Agreements ---------- */
const services = () => {
  proxy.$navigate('/pagesA/services/index', false)
}
const policy = () => {
  proxy.$navigate('/pagesA/policy/index', false)
}
const btnF1 = () => {
  uni.showToast({
    icon: 'none',
    title: proxy.$tt('login.agreement_toast'),
    duration: 2000
  })
}

/* ---------- Form states ---------- */
const formRef = ref(null)
const form = ref({
  phone: '',
  password: ''
})

const form_sms = ref({
  phone: '',
  code: null
})
const loginForm_sms = ref(null)

const rules = ref({
  phone: {
    rules: [
      { required: true, errorMessage: proxy.$tt('login.enter_user_name') },
      { minLength: 1, maxLength: 100, errorMessage: proxy.$tt('login.username_length_rule_text') }
    ]
  },
  password: {
    rules: [
      { required: true, errorMessage: proxy.$tt('login.enter_pass_wd') },
      { minLength: 3, maxLength: 18, errorMessage: proxy.$tt('login.password_length_rule_text') }
    ]
  }
})

const rules_sms = {
  phone: { rules: [{ required: true, errorMessage: proxy.$tt('login.enter_phone') }] },
  code: { rules: [{ required: true, errorMessage: proxy.$tt('login.enter_verification_code') }] }
}

/* ---------- SMS code ---------- */
const countdown = ref(0)
const isphoneValid = ref(false)
const intervalId = ref(null)

const validatephone = () => {
  if (form_sms.value.phone != null) {
    isphoneValid.value = /^1[3-9]\d{9}$/.test(form_sms.value.phone)
  }
}

const sendcode = async () => {
  validatephone()
  if (!isphoneValid.value) {
    uni.showToast({ title: proxy.$tt('login.phone_number_format_error_text'), icon: 'none' })
    return
  }

  try {
    const res = await proxy.$cf.login.getCodeByPhone(form_sms.value.phone)
    uni.showToast({ title: proxy.$tt('login.verification_code_success_text'), icon: 'success' })
    if (res.code === 0 && res.data) form_sms.value.code = res.data

    countdown.value = 60
    intervalId.value = setInterval(() => {
      if (countdown.value > 0) countdown.value--
      else clearInterval(intervalId.value)
    }, 1000)
  } catch (e) {
    uni.showToast({ title: proxy.$tt('login.verification_code_fail_text'), icon: 'none' })
  }
}

/* ---------- Submit: password ---------- */
const submitForm = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    form.value.relevanceTable = props.relevanceTable
    form.value.openId = openid.value
    const res = await proxy.$cf.login.loginPasswd(form.value)
    login_success(res)
  } catch (err) {
    // validate or api error
    login_error(err)
  }
}

/* ---------- Submit: SMS ---------- */
const submitForm_sms = async () => {
  async function save_login_message() {
    const loin_sms_value = JSON.stringify({ phone: form_sms.value.phone, code: form_sms.value.code })
    return await uni.setStorage({ key: 'login_sms_key', data: loin_sms_value })
  }

  const formEl = loginForm_sms.value
  if (!formEl) return

  try {
    await formEl.validate()
    form_sms.value.relevanceTable = props.relevanceTable
    const res = await proxy.$cf.login.loginBySms(form_sms.value)
    await save_login_message()
    login_success(res)
  } catch (err) {
    await save_login_message()
    login_error(err)
  }
}

/* ---------- Login result handlers ---------- */
function login_error(err) {
  emit('loginFail', err)
}

const login_success = async (res) => {
  uni.showToast({
    title: proxy.$tt('login.login_success_text'),
    icon: 'success',
    duration: 2000
  })

  await proxy.$cf.globalVariable.write({ variableName: 'h5_token', value: res.data })

  try {
    const me = await proxy.$cf.login.getLoginUser()
    const user = me.data
    await proxy.$cf.globalVariable.write({ variableName: 'currentUser', value: user })
    emit('loginSuccess', user)
  } catch (err) {
    emit('loginFail', err)
  }
}

/* ---------- Wechat / H5 ---------- */
const openid = ref(null)
const phone = ref('')

const appId = import.meta.env.VITE_WX_APP_ID
const callback = import.meta.env.VITE_WX_CALL_BACK

const getPhoneNumber = (e) => {
  if (e.detail.errMsg === 'getPhoneNumber:fail user deny') {
    uni.showToast({ title: proxy.$tt('login.deny_authorization_text'), icon: 'none', duration: 2000 })
    return
  }

  uni.login({
    success: () => {
      service({
        url: '/login/phone',
        method: 'get',
        data: { code: e.detail.code, relevanceTable: props.relevanceTable }
      }).then((res) => {
        if (res.code === 0) {
          phone.value = res.data
          login_click()
        }
      })
    }
  })
}

function login_click() {
  const url = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=${encodeURIComponent(
      callback
  )}&response_type=code&scope=snsapi_base&state=codeflying#wechat_redirect`

  // #ifdef MP-WEIXIN
  if (props.login_type === 'wechat') {
    uni.login({
      provider: 'weixin',
      onlyAuthorize: true,
      success: (event) => {
        uni.request({
          url: import.meta.env.VITE_APP_SERVICE_API + '/login/wxApp',
          method: 'POST',
          data: { code: event.code, phone: phone.value, relevanceTable: props.relevanceTable },
          success: (res) => {
            if (res.data.code === 0) {
              uni.setStorageSync('h5_token', res.data.data)
              login_success(res)
            } else {
              login_error()
            }
          }
        })
      },
      fail: (err) => login_error(err)
    })
  }
  // #endif

  // #ifdef H5
  if (props.login_type === 'h5') {
    window.location.href = url
  }
  // #endif
}

/* ---------- Lifecycle ---------- */
onShow(async () => {})

function isWeixinBrowser() {
  return typeof window !== 'undefined' && window.navigator.userAgent.toLowerCase().includes('micromessenger')
}

onLoad(async (options) => {
  // #ifdef MP-WEIXIN
  uni.login({
    success: async (res) => {
      const code = res.code
      const response = await proxy.$cf.login.getOpenIdByCode(code)
      if (response.success && response.data != null) openid.value = response.data
    },
    fail: (error) => {
      console.error('登录失败', error)
    }
  })
  // #endif

  // #ifdef H5
  const openidUrlParam = options.openid
  if ((openidUrlParam == null || openidUrlParam == undefined) && isWeixinBrowser()) {
    const res = await proxy.$cf.login.getMpUrl()
    if (res.success && res.data != null) {
      proxy.$cf.navigate.to({ url: res.data, type: 'external' })
    }
  } else if (isWeixinBrowser() && openidUrlParam != null && openidUrlParam != undefined) {
    openid.value = openidUrlParam
  }
  // #endif

  // Check token
  try {
    const t = await proxy.$cf.globalVariable.read({ variableName: 'h5_token' })
    if (t.success) emit('loginSuccess')
    else login_setting()
  } catch (e) {
    login_setting()
  }
})

/* ---------- Runtime config ---------- */
function login_setting() {
  proxy.$cf.setting.login().then((res) => {
    const configList = res.data
    if (!shouldShow('login.show.account', configList)) {
      form.value.phone = ''
      form_sms.value.phone = ''
    } else {
      // demo defaults (only when allowed by server config)
      form.value.phone = '18852718858'
      form.value.password = '123456'
      form_sms.value.phone = '18852718858'
    }
  })
}

function shouldShow(nameKey, configList) {
  const item = configList.find((i) => i.name === nameKey)
  return item && item.content === 'true'
}
</script>

<style>
/* Minimal, necessary overrides only */
::v-deep .uni-input-input {
  color: #999 !important;
}
::v-deep .uni-easyinput__content-input {
  padding-left: 20px !important;
}
::v-deep .uni-forms-item_error {
  width: 100%;
}
::v-deep.is-input-error-border .uni-easyinput__placeholder-class {
  color: #999;
}
</style>
