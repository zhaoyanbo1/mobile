<!-- src/pagesA/profile/index.vue -->
<template>
  <base-layout>
    <view class="page">
      <!-- User info card -->
      <view class="card user-card">
        <uni-icons type="person-filled" size="36" :color="primaryColor" />
        <view class="ml-4">
          <text class="user-name">{{ userInfo.username || 'User' }}</text>
          <text class="block user-sub">{{ userInfo.phone_number || '' }}</text>
        </view>
      </view>

      <!-- Health data -->
      <view class="card">
        <view class="card-head">
          <text class="card-title">Your Health Risk Level</text>
          <view class="btn primary small" @click="updateQuestionnaire">Update</view>
        </view>

        <!-- === 新：风险等级单卡片（替代原4个指标卡片） === -->
        <view v-if="riskLoading" class="empty">
          <text>Loading...</text>
        </view>
        <view v-else class="risk-card">
          <view class="risk-icon" :style="{ backgroundColor: riskBg }">
            <text class="risk-emoji">{{ riskEmoji }}</text>
          </view>

          <!--          <view class="risk-info">-->
          <!--            <text class="risk-title">Your Health Risk Level</text>-->
          <!--            <text class="risk-level" :class="riskClass">{{ riskText }}</text>-->
          <!--            <text class="risk-desc">{{ riskDesc }}</text> -->
          <!--          </view>-->
        </view>
      </view>

      <!-- Emergency contacts -->
      <view class="card">
        <view class="card-head">
          <text class="card-title">Emergency Contacts</text>
          <uni-icons type="plus" size="20" :color="primaryColor" @click="addEmergencyContact" />
        </view>

        <view v-if="emergencyContacts.length > 0">
          <view
              v-for="contact in emergencyContacts"
              :key="contact.emergency_contact_id"
              class="contact-row"
          >
            <view>
              <text class="contact-name">{{ contact.name }}</text>
              <text class="contact-sub">{{ contact.phone_number }}</text>
            </view>
            <uni-icons
                type="phone-filled"
                size="20"
                :color="primaryColor"
                @click="callContact(contact.phone_number)"
            />
          </view>
        </view>
        <view v-else class="empty">
          <text>No emergency contacts yet</text>
        </view>
      </view>
    </view>

    <!-- Add contact popup -->
    <uni-popup ref="addContactPopup" type="center" class="z-50">
      <view class="popup">
        <text class="popup-title">Add Emergency Contact</text>

        <uni-forms :modelValue="newContact" label-position="top">
          <uni-forms-item name="name" label="Name" required>
            <uni-easyinput
                v-model="newContact.name"
                type="text"
                placeholder="Enter name"
                class="w-full box-border"
            />
          </uni-forms-item>

          <uni-forms-item name="phone_number" label="Phone Number" required>
            <uni-easyinput
                v-model="newContact.phone_number"
                type="text"
                placeholder="Enter phone number"
                class="w-full box-border"
            />
          </uni-forms-item>

          <view class="btn w-full mt-4" @click="saveEmergencyContact">
            <text class="btn-label">Save Contact</text>
          </view>
        </uni-forms>
      </view>
    </uni-popup>
  </base-layout>
</template>

<script setup>
import { ref, computed, getCurrentInstance } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import service from '@/utils/request'          // ✅ 新增：与 Care 页一致的取数方式

const { proxy } = getCurrentInstance()

/** Theme color */
const primaryColor = '#93b2a1' // sage green

/** State */
const userInfo = ref({})
const emergencyContacts = ref([])
const systemSettings = ref({
  reminder_volume: 50,
  font_size: 16,
  questionnaire_exported: false
})
const newContact = ref({ name: '', phone_number: '' })
const addContactPopup = ref(null)

/** ===== 新增：风险相关状态 ===== */
const riskLoading = ref(true)
const riskLevel = ref('low_risk')

/** 风险 -> UI 映射 */
const RISK_MAP = {
  low_risk: {
    emoji: '😀',
    text: 'Low Risk',
    desc: '总体状况良好，继续保持当前生活方式。',
    class: 'risk-low',
    bg: 'rgba(46, 125, 50, .10)'
  },
  moderate_risk: {
    emoji: '😐',
    text: 'Moderate Risk',
    desc: '存在一些风险点，建议适度调整饮食与作息。',
    class: 'risk-mod',
    bg: 'rgba(153, 115, 0, .10)'
  },
  high_risk: {
    emoji: '😟',
    text: 'High Risk',
    desc: '风险较高，请关注睡眠、运动与饮水等关键指标。',
    class: 'risk-high',
    bg: 'rgba(198, 40, 40, .10)'
  },
  extremly_high_risk: {
    emoji: '😱',
    text: 'Extremely High Risk',
    desc: '极高风险，建议尽快咨询医生或专业人员。',
    class: 'risk-xhigh',
    bg: 'rgba(183, 28, 28, .10)'
  }
}

const ui = computed(() => RISK_MAP[riskLevel.value] || RISK_MAP.low_risk)
const riskEmoji = computed(() => ui.value.emoji)
const riskText  = computed(() => ui.value.text)
const riskDesc  = computed(() => ui.value.desc)
const riskClass = computed(() => ui.value.class)
const riskBg    = computed(() => ui.value.bg)

/** Fetch user data */
const fetchUserData = async () => {
  const userRes = await proxy.$cf.login.getLoginUser()
  if (userRes.success && userRes.data) {
    userInfo.value = userRes.data
    const uid = userRes.data.user_info_id
    await fetchEmergencyContacts(uid)
    await fetchSystemSettings(uid)
    await fetchRiskLevel(uid)   // ✅ 改为走 /care/risk
  } else {
    riskLoading.value = false
  }
}

/** ✅ 用 service.get('/care/risk') 获取最新风险等级 */
// const fetchRiskLevel = async (userId) => {
//   try {
//     // ✅ 和 Care 页一致，走封装的 service，相对路径 '/care/risk'
//     const r = await service.get('/care/risk', { params: { userId } })
//
//     // 兼容返回结构：直接对象 / R 包装
//     const lvl = r?.risk_level ?? r?.data?.risk_level ?? r?.data?.data?.risk_level
//     riskLevel.value = (lvl || 'low_risk').toString().trim()
//   } catch (e) {
//     console.warn('fetchRiskLevel error:', e?.response?.status, e?.response?.data || e)
//     riskLevel.value = 'low_risk'
//   } finally {
//     riskLoading.value = false
//   }
// }
const fetchRiskLevel = async (userId) => {
  try {
    const r = await service.get('/care/risk', { params: { userId } })

    // 1) 取原始返回
    const raw =
        r?.risk_level ??
        r?.data?.risk_level ??
        r?.data?.data?.risk_level ??
        'low_risk'

    // 2) 规范化：去两端空白、转小写、空格/连字符 => 下划线
    let key = String(raw).trim().toLowerCase()
        .replace(/[\s-]+/g, '_')

    // 3) 同义/拼写兼容（比如 extremely -> extremly，high risk 写法等）
    const alias = {
      'low risk': 'low_risk',
      'moderate risk': 'moderate_risk',
      'high risk': 'high_risk',
      'extremely_high_risk': 'extremly_high_risk', // 若后端返回 extremely
      'extremely risk': 'extremly_high_risk',
      'extremely_high': 'extremly_high_risk'
    }
    if (alias[key]) key = alias[key]

    // 4) 不在映射表则回退
    riskLevel.value = (key in RISK_MAP) ? key : 'low_risk'
  } catch (e) {
    console.warn('fetchRiskLevel error:', e?.response?.status, e?.response?.data || e)
    riskLevel.value = 'low_risk'
  } finally {
    riskLoading.value = false
  }
}


const fetchEmergencyContacts = async (userId) => {
  const res = await proxy.$cf.table.list({
    table_name: 'emergency_contact',
    param: { user_info_user_info_id_1: userId }
  })
  if (res.success) emergencyContacts.value = res.data
}

const fetchSystemSettings = async (userId) => {
  const res = await proxy.$cf.table.list({
    table_name: 'system_settings',
    param: { user_info_user_info_id_1: userId }
  })
  if (res.success && res.data.length > 0) systemSettings.value = res.data[0]
}

/** Actions */
const updateQuestionnaire = () => {
  proxy.$cf.navigate.to({ url: '/pages/health_questionnaire_update/index', type: 'page' })
}

const addEmergencyContact = () => {
  newContact.value = { name: '', phone_number: '' }
  addContactPopup.value.open()
}

const saveEmergencyContact = async () => {
  if (!newContact.value.name || !newContact.value.phone_number) {
    proxy.$cf.toast({ message: 'Please fill in all required fields', level: 'error' })
    return
  }
  const userRes = await proxy.$cf.login.getLoginUser()
  if (!userRes.success) return

  const res = await proxy.$cf.table.add({
    table_name: 'emergency_contact',
    param: {
      user_info_user_info_id_1: userRes.data.user_info_id,
      name: newContact.value.name,
      phone_number: newContact.value.phone_number
    }
  })
  if (res.success) {
    proxy.$cf.toast({ message: 'Contact saved successfully', level: 'success' })
    addContactPopup.value.close()
    await fetchEmergencyContacts(userRes.data.user_info_id)
  }
}

const callContact = (phoneNumber) => {
  uni.makePhoneCall({ phoneNumber })
}

/** System settings (optional) */
const saveSystemSettings = async () => {
  const userRes = await proxy.$cf.login.getLoginUser()
  if (!userRes.success) return
  const checkRes = await proxy.$cf.table.list({
    table_name: 'system_settings',
    param: { user_info_user_info_id_1: userRes.data.user_info_id }
  })
  if (checkRes.success && checkRes.data.length > 0) {
    await proxy.$cf.table.update({
      table_name: 'system_settings',
      param: { system_settings_id: checkRes.data[0].system_settings_id, ...systemSettings.value }
    })
  } else {
    await proxy.$cf.table.add({
      table_name: 'system_settings',
      param: { user_info_user_info_id_1: userRes.data.user_info_id, ...systemSettings.value }
    })
  }
}

onLoad(() => { fetchUserData() })
</script>

<style scoped>
.btn-label{ color: inherit; font-size: inherit; font-weight: inherit; line-height: 1; display: inline-block; }

/* ===== Theme ===== */
:root{
  --sage:#93b2a1;
  --ink:#111827;
  --muted:#6b7280;
  --bg:#f8f8f8;
  --surface:#ffffff;
  --success:#1BAA5F;
  --shadow:0 10px 22px rgba(0,0,0,.10);
  --hairline:rgba(17,24,39,.06);
}

/* Page */
.page{ padding:16px; background:var(--bg); min-height:100vh; }

/* Cards */
.card{ background:var(--surface); border:1px solid var(--hairline); border-radius:20px; padding:18px; box-shadow:var(--shadow); margin-bottom:18px; }
.user-card{ display:flex; align-items:center; }

/* Titles */
.user-name{ font-size:18px; font-weight:800; color:var(--ink); }
.user-sub{ font-size:13px; color:var(--muted); }
.card-head{ display:flex; justify-content:space-between; align-items:center; margin-bottom:12px; }
.card-title{ font-size:20px; font-weight:800; color:var(--ink); }

/* —— 旧指标卡样式（保留，不再使用） —— */
.ind-card{
  background: rgba(255,255,255,.8);
  backdrop-filter: blur(6px);
  border-radius: 20px;
  padding: 16px;
  box-shadow: var(--shadow);
}
.ind-head{ display:flex; align-items:center; gap:8px; margin-bottom:12px; }
.ind-icon{ width: 44px; height: 44px; border-radius: 12px; display:flex; align-items:center; justify-content:center; }
.ind-emoji{ font-size: 22px; }
.ind-title{ display:block; font-weight:700; color:var(--ink); margin-bottom:6px; }
.ind-desc{ display:block; color:var(--muted); font-size: 12px; margin-bottom:10px; }
.ind-bar{ width:100%; height:8px; background:#e5e7eb; border-radius:9999px; overflow:hidden; }
.ind-fill{ height:100%; border-radius:9999px; transition: width .6s ease; }

/* === 新：风险卡片样式 === */
/*.risk-card{ display:flex; align-items:center; gap:14px; }
.risk-icon{
  width:56px; height:56px; border-radius:14px;
  display:flex; align-items:center; justify-content:center;
}
.risk-emoji{ font-size:32px; line-height:1; }*/

/* === 放大表情并让卡片居中显示 === */
.risk-card {
  display: flex;
  align-items: center;      /* 水平居中 */
  justify-content: center;  /* 垂直居中 */
  min-height: 180px;        /* 卡片高度（可根据整体风格调整） */
  padding: 40px 0;
}

.risk-icon {
  width: 160px;             /* 放大背景容器 */
  height: 160px;
  border-radius: 40px;      /* 圆角更柔和 */
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 6px 16px rgba(0,0,0,0.08); /* 增强视觉层次，可选 */
}

.risk-emoji {
  font-size: 120px;         /* 放大表情 */
  line-height: 1;
}

.risk-info{ display:flex; flex-direction:column; }
.risk-title{ font-size:14px; color:var(--muted); }
.risk-level{ font-size:18px; font-weight:800; margin-top:2px; }
.risk-desc{ font-size:12px; color:#6b7280; margin-top:4px; }

/* 颜色语义 */
.risk-low{ color:#2E7D32; }
.risk-mod{ color:#997300; }
.risk-high{ color:#C62828; }
.risk-xhigh{ color:#B71C1C; }

/* Empty state */
.empty{ padding:32px 0; text-align:center; color:#9ca3af; }

/* Contacts */
.contact-row{ display:flex; align-items:center; justify-content:space-between; padding:12px 0; border-bottom:1px solid #f0f0f0; }
.contact-row:last-child{ border-bottom:none; }
.contact-name{ font-weight:600; color:#111; }
.contact-sub{ font-size:13px; color:#6b7280; }

/* Buttons */
.btn{ display:inline-flex; align-items:center; justify-content:center; height:40px; padding:0 16px; border-radius:9999px;
  font-weight:800; font-size:14px; box-shadow:0 6px 14px rgba(0,0,0,.08); background: var(--sage, #93b2a1); color: #fff; }
.btn.small{ height:34px; padding:0 12px; font-size:13px; box-shadow:none; }
.btn.primary{ background:var(--sage); color:#fff; }
.btn.success{ background:var(--success); color:#fff; }
.w-full{ width:100%; }
.mt-4{ margin-top:1rem; }

/* Popup */
.popup{ background:#fff; border-radius:18px; padding:24px; width:20rem; box-shadow:var(--shadow); }
.popup-title{ font-size:18px; font-weight:800; color:var(--ink); margin-bottom:12px; }

/* Form */
:deep(.uni-forms-item__label){ font-size:14px; color:#4b5563; font-weight:700; }
:deep(.uni-easyinput__content){ border-radius:14px !important; border-color:#e5e7eb !important; min-height:44px; background:#fff; }
:deep(.uni-easyinput__content-input){ font-size:15px; color:#111827; }
:deep(.uni-easyinput__placeholder-class){ color:#9ca3af !important; }
</style>
