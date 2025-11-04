<template>
  <base-layout>
    <!-- Theme wrapper -->
    <view
        class="theme-health min-h-screen flex flex-col items-center justify-center bg-[linear-gradient(180deg,_#FAFAF8_0%,_#FFFFFF_60%)] p-6"
    >
      <!-- App Logo / Title -->
      <view class="mb-10 flex flex-col items-center">
        <text class="text-[28px] leading-tight font-extrabold text-neutral-900 tracking-[0.2px]">
          Health Care
        </text>
        <text class="mt-2 text-[18px] font-semibold text-neutral-500">
          Welcome back
        </text>
      </view>

      <!-- Login Card -->
      <view
          class="login-shell w-full max-w-[420px] bg-white rounded-[20px] shadow-soft p-6 mb-8 border border-[var(--card-border)]"
      >
        <base-login
            login_type="passwd"
            show_title=""
            relevanceTable="user_info"
            @loginSuccess="handleLoginSuccess"
            @loginFail="handleLoginFail"
        >
          <template #footer>
            <view class="mt-5 space-y-3">
              <view class="text-center text-[16px] text-neutral-500">No account?</view>
              <view
                  role="button"
                  tabindex="0"
                  class="w-full rounded-full h-[48px] leading-[48px] border border-[var(--brand-sage-600)] text-center font-semibold text-[18px] text-[var(--brand-sage-700)] active:scale-[0.99] bg-white"
                  aria-label="Open sign-up modal"
                  @click="showRegisterPopup"
              >
                Sign up
              </view>
            </view>
          </template>
        </base-login>
      </view>

      <!-- Sign-up Modal -->
      <uni-popup ref="registerPopup" type="center" :mask-click="true">
        <view
            class="modal-card bg-white rounded-[18px] w-[92vw] max-w-[420px] shadow-soft border border-[var(--card-border)]"
        >
          <view class="px-6 pt-5 pb-3 border-b border-[var(--card-border)]">
            <text class="block text-[20px] font-extrabold text-neutral-900">Create account</text>
          </view>

          <view class="modal-scroll px-6 py-4">
            <uni-forms :modelValue="registerForm" label-position="top" class="form-styled">
              <uni-forms-item required label="Phone Number" name="phone_number">
                <uni-easyinput
                    type="text"
                    v-model="registerForm.phone_number"
                    placeholder="Enter phone number"
                />
              </uni-forms-item>

              <uni-forms-item required label="Username" name="username">
                <uni-easyinput
                    type="text"
                    v-model="registerForm.username"
                    placeholder="Enter username"
                />
              </uni-forms-item>

              <uni-forms-item required label="Password" name="password">
                <uni-easyinput
                    type="password"
                    v-model="registerForm.password"
                    placeholder="Enter password"
                />
              </uni-forms-item>

              <view class="grid grid-cols-1 gap-3 mt-1">
                <uni-forms-item label="Birthdate" name="birthdate">
                  <uni-datetime-picker
                      type="date"
                      v-model="registerForm.birthdate"
                      :end="todayStr"
                      return-type="string"
                      placeholder="Select birthdate"
                  />
                </uni-forms-item>

                <uni-forms-item label="Gender" name="gender">
                  <uni-easyinput type="text" v-model="registerForm.gender" placeholder="Enter gender" />
                </uni-forms-item>

                <uni-forms-item label="Address" name="address">
                  <uni-easyinput type="text" v-model="registerForm.address" placeholder="Enter address" />
                </uni-forms-item>

                <uni-forms-item label="Emergency Contact" name="emergency_contact">
                  <uni-easyinput
                      type="text"
                      v-model="registerForm.emergency_contact"
                      placeholder="Name / phone (optional)"
                  />
                </uni-forms-item>
              </view>

              <!-- 同意条款 -->
              <view class="agree-wrap">
                <checkbox-group @change="onAgreeChange">
                  <label class="agree-label">
                    <checkbox value="agree" :checked="agree" class="agree-checkbox" />
                    <text class="agree-text">
                      By continuing you agree to our Terms & Privacy.
                    </text>
                  </label>
                </checkbox-group>
              </view>

              <!-- 创建账号按钮 -->
              <button
                  type="button"
                  class="w-full rounded-full h-[48px] mt-2 font-semibold text-[16px] text-white bg-[var(--brand-sage-600)] active:scale-[0.99] shadow-press"
                  :disabled="!agree"
                  @click="handleRegister"
              >
                Create Account
              </button>
            </uni-forms>
          </view>
        </view>
      </uni-popup>
    </view>
  </base-layout>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
import { onShow } from '@dcloudio/uni-app'

const { proxy } = getCurrentInstance()

const todayStr = ref(new Date().toISOString().slice(0, 10))

/* 同意条款 */
const agree = ref(false)
const onAgreeChange = (e) => {
  agree.value = Array.isArray(e.detail?.value) && e.detail.value.includes('agree')
}

/* 注册表单 */
const registerForm = ref({
  phone_number: '',
  username: '',
  password: '',
  birthdate: '',
  gender: '',
  address: '',
  emergency_contact: ''
})

/* 弹窗 */
const registerPopup = ref(null)
const showRegisterPopup = () => registerPopup.value?.open()

/** 把所有可能的登录痕迹清掉 */
async function clearResidualAuth () {
  try {
    uni.removeStorageSync('token')
    uni.removeStorageSync('me')
    uni.removeStorageSync('user')
    uni.removeStorageSync('uid')
    uni.removeStorageSync('h5_token')
  } catch (e) {}

  try {
    await proxy?.$cf?.globalVariable?.write?.({ variableName: 'h5_token', value: '' })
  } catch (e) {}
  try {
    await proxy?.$cf?.globalVariable?.write?.({ variableName: 'currentUser', value: '' })
  } catch (e) {}
}

onShow(async () => {
  // 回到登录页先清一次
  await clearResidualAuth()
})

/* ✅ 登录成功逻辑（放宽判断）：能拿到用户就算成功 */
const handleLoginSuccess = async (payload) => {
  // 1. 尽量多渠道拿 token
  let token =
      payload?.token ||
      payload?.data?.token ||
      payload?.accessToken ||
      payload?.data?.accessToken ||
      ''

  // 有些组件登录成功后直接写本地，不在 payload 里
  if (!token) {
    const localToken = uni.getStorageSync('token') || uni.getStorageSync('h5_token')
    if (localToken) token = localToken
  }
  if (!token) {
    // 再从全局变量兜底
    try {
      const t = await proxy?.$cf?.globalVariable?.read?.({ variableName: 'h5_token' })
      token = t?.data || ''
    } catch (e) {}
  }

  // 2. 尽量拿用户信息
  let me =
      payload?.user ||
      payload?.data?.user ||
      null

  // 不在 payload 里，就调一次后端的“我是谁”
  if (!me) {
    try {
      const resp = await proxy?.$cf?.login?.getLoginUser?.()
      if (resp?.success) {
        me = resp.data
      }
    } catch (e) {}
  }

  // 3. 把 uid 抽出来
  const uid = me?.user_info_id ?? me?.id ?? me?.userId ?? me?.uid ?? null

  // 🚩 改这里：只要拿到了“用户(id)”就算登录成功，
  // token 没拿到也别直接提示“invalid”，先把能存的都存起来
  if (!uid) {
    // 真是没拿到用户，这才当作失败
    await clearResidualAuth()
    proxy?.$cf?.toast?.({
      message: 'Login info invalid, please login again.',
      level: 'error'
    })
    return
  }

  // 4. 能拿到就往本地里写
  if (token) {
    uni.setStorageSync('token', token)
    // 顺手写回全局，方便其他页面用
    try {
      await proxy?.$cf?.globalVariable?.write?.({ variableName: 'h5_token', value: token })
    } catch (e) {}
  }

  uni.setStorageSync('me', me)
  uni.setStorageSync('user', {
    userId: uid,
    username: me.username ?? me.name ?? 'Anonymous',
    avatarUrl:
        me.avatarUrl ??
        me.avatar ??
        'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&h=200&fit=crop'
  })
  uni.setStorageSync('uid', String(uid))

  // 也把 currentUser 写掉，避免你后面页面读不到
  try {
    await proxy?.$cf?.globalVariable?.write?.({ variableName: 'currentUser', value: me })
  } catch (e) {}

  proxy?.$cf?.toast?.({ message: 'Login successful', level: 'success', duration: 1200 })

  // 5. 真正跳转
  setTimeout(() => {
    proxy.$cf.navigate.to({ url: '/pages/chat/index', type: 'page' })
  }, 1200)
}

/* 登录失败 */
const handleLoginFail = () => {
  proxy?.$cf?.toast?.({
    message: 'Login failed. Please check your credentials.',
    level: 'error'
  })
}

/* 注册并自动登录（保持你原来的逻辑） */
const handleRegister = async () => {
  if (!agree.value) {
    proxy.$cf.toast({ message: 'Please agree to Terms & Privacy first.', level: 'error' })
    return
  }

  const { phone_number, username, password } = registerForm.value
  if (!phone_number || !username || !password) {
    proxy.$cf.toast({ message: 'Phone number, username, and password are required.', level: 'error' })
    return
  }

  const payload = { phone_number, username, password }

  try {
    const res = await proxy.$cf.register.register({ table_name: 'user_info', param: payload })
    if (!res || !res.success) {
      proxy.$cf.toast({ message: 'Registration failed', level: 'error' })
      return
    }

    const doLogin = async (account) => {
      return await proxy.$cf.login.loginPasswd({
        phone: account,
        password,
        relevanceTable: 'user_info'
      })
    }

    let loginRes
    try { loginRes = await doLogin(phone_number) } catch {}
    if (!loginRes) { try { loginRes = await doLogin(username) } catch {} }

    if (!loginRes || !loginRes.data) {
      proxy.$cf.toast({ message: 'Auto login failed after registration', level: 'error' })
      return
    }

    await proxy.$cf.globalVariable.write({ variableName: 'h5_token', value: loginRes.data })
    try {
      const me = await proxy.$cf.login.getLoginUser()
      await proxy.$cf.globalVariable.write({ variableName: 'currentUser', value: me?.data })
    } catch {}

    registerPopup.value?.close()
    proxy.$cf.toast({ message: 'Registration successful', level: 'success', duration: 900 })
    setTimeout(() => {
      proxy.$cf.navigate.to({ url: '/pagesA/care/questionare/index', type: 'page' })
    }, 600)
  } catch (e) {
    proxy.$cf.toast({ message: 'Registration failed', level: 'error' })
  }
}
</script>


<style scoped>
.theme-health{
  --brand-sage-700:#3F6D5A;
  --brand-sage-600:#6FA08F;
  --brand-sage-500:#90B2A1;
  --brand-sage-200:#DCE8E2;
  --brand-peach-500:#D9A27A;
  --brand-peach-200:#F1D7C5;
  --card-border:#EEEEEE;
}

.shadow-soft{ box-shadow: 0 8px 28px rgba(0,0,0,0.08); }
.shadow-press{ box-shadow: 0 6px 16px rgba(111,160,143,0.35); }

.modal-card{
  display:flex; flex-direction:column;
  max-height:min(86vh, 720px);
  overflow:hidden;
}
.modal-scroll{ overflow-y:auto; -webkit-overflow-scrolling:touch; }

:deep(.uni-forms){ --label-color:#3F3F46; }
:deep(.uni-forms .uni-forms-item__label){ font-weight:700; font-size:14px; color:var(--label-color); }
:deep(.uni-easyinput__content),
:deep(.uni-easyinput__content-input),
:deep(.uni-easyinput__placeholder-class){
  border-radius:14px !important; background:#FAFAFA !important;
}
:deep(.uni-easyinput__content){ border:1px solid #E7E7E7 !important; padding:2px 10px !important; }
:deep(.uni-easyinput__content-input){ height:44px !important; font-size:16px !important; color:#111827 !important; }
:deep(.uni-easyinput__placeholder-class), :deep(.uni-input-placeholder){ font-size:15px !important; color:#9CA3AF !important; }

:deep(.uni-datetime-picker){
  --bd:#E7E7E7; display:block; border:1px solid var(--bd);
  border-radius:14px; padding:10px 12px; background:#FAFAFA; font-size:15px;
}

.login-shell :deep(button){
  height:48px !important; border-radius:9999px !important;
  font-weight:800 !important; font-size:18px !important;
  background: var(--brand-sage-600) !important; color:#fff !important;
  box-shadow: 0 6px 16px rgba(111,160,143,0.35) !important; border:none !important;
}
.login-shell :deep(button:active){ transform: translateY(0.5px) scale(0.99); }

.agree-wrap{ display:flex; justify-content:center; width:100%; margin:10px 0 4px; }
.agree-label{ display:flex; align-items:center; gap:10px; }
:deep(.agree-checkbox){ transform: scale(1.35); }
:deep(.agree-checkbox .uni-checkbox-input){
  width:22px; height:22px; border-radius:6px;
  border-color: var(--brand-sage-600) !important;
}
:deep(.agree-checkbox .uni-checkbox-input.uni-checkbox-input-checked){
  background: var(--brand-sage-600) !important;
  border-color: var(--brand-sage-600) !important;
}
.agree-text{ font-size:16px; color:#6B7280; line-height:22px; }

button[disabled]{ opacity:.55; filter:grayscale(10%); box-shadow:none; }

.text-neutral-900{ color:#0B0B0C; }
.text-neutral-500{ color:#777; }
</style>
