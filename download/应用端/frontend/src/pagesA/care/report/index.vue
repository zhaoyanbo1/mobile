<template>
  <base-layout>
    <view class="page">
      <!-- User header -->
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
          <text class="card-title">Health Data</text>
          <view class="btn primary small" @click="updateQuestionnaire">Update</view>
        </view>

        <!-- Four fixed cards: 😊 Low risk (title left, content centered below) -->
        <view class="grid grid-cols-2 gap-4">
          <view class="stat stat-sage">
            <text class="stat-label">Functional Independence</text>
            <view class="stat-body">
              <text class="stat-emoji">😊</text>
              <text class="stat-value-strong">Low risk</text>
            </view>
          </view>

          <view class="stat stat-warn">
            <text class="stat-label">Fall </text>
            <text class="stat-label">Risk</text>
            <view class="stat-body">
              <text class="stat-emoji">😊</text>
              <text class="stat-value-strong">Low risk</text>
            </view>
          </view>

          <view class="stat stat-success">
            <text class="stat-label">Physical Activity</text>
            <view class="stat-body">
              <text class="stat-emoji">😊</text>
              <text class="stat-value-strong">Low risk</text>
            </view>
          </view>

          <view class="stat stat-sage">
            <text class="stat-label">Nutrition Status</text>
            <view class="stat-body">
              <text class="stat-emoji">😊</text>
              <text class="stat-value-strong">Low risk</text>
            </view>
          </view>
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
import { ref, getCurrentInstance } from 'vue'
import { onLoad } from '@dcloudio/uni-app'

const { proxy } = getCurrentInstance()

/** Palette */
const primaryColor = '#93b2a1' // sage

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

/** Fetchers */
const fetchUserData = async () => {
  const userRes = await proxy.$cf.login.getLoginUser()
  if (userRes.success && userRes.data) {
    userInfo.value = userRes.data
    await fetchEmergencyContacts(userRes.data.user_info_id)
    await fetchSystemSettings(userRes.data.user_info_id)
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
    proxy.$cf.toast({ message: 'Please fill all required fields', level: 'error' })
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
    proxy.$cf.toast({ message: 'Contact saved', level: 'success' })
    addContactPopup.value.close()
    await fetchEmergencyContacts(userRes.data.user_info_id)
  }
}

const callContact = (phoneNumber) => {
  uni.makePhoneCall({ phoneNumber })
}

/** Optional settings */
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

/* make sure label renders and inherits color/size */
.btn-label{
  color: inherit;
  font-size: inherit;
  font-weight: inherit;
  line-height: 1;
  display: inline-block;
}

/* ===== Shared theme ===== */
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
.card{
  background:var(--surface);
  border:1px solid var(--hairline);
  border-radius:20px;
  padding:18px;
  box-shadow:var(--shadow);
  margin-bottom:18px;
}
.user-card{ display:flex; align-items:center; }

/* Titles */
.user-name{ font-size:18px; font-weight:800; color:var(--ink); }
.user-sub{ font-size:13px; color:var(--muted); }
.card-head{ display:flex; justify-content:space-between; align-items:center; margin-bottom:12px; }
.card-title{ font-size:20px; font-weight:800; color:var(--ink); }

/* Stat cards: split into top (title) + bottom (centered content) */
.stat{
  border-radius:14px;
  padding:12px 14px;
  background:#f5f7f5;
  border:1px solid var(--hairline);
  min-height:240rpx;                 /* consistent height */
  display:flex;
  flex-direction:column;             /* title on top, body below */
}
.stat-sage{ background:#eef3ef;  }
.stat-success{ background:#eef8f2; }
.stat-warn{ background:#f3f6ee; }
.stat-danger{ background:#f6eeee; }

.stat-label{
  font-size:32rpx;
  font-weight:900;
  color:#50605a;
  text-align:left;
  margin-bottom:6rpx;
}

/* Lower half: emoji + text centered as a group */
.stat-body{
  flex:1;                            /* take remaining space */
  display:flex;
  flex-direction:column;
  align-items:center;
  justify-content:center;
  gap:8rpx;
  text-align:center;
}
.stat-emoji{ font-size:64rpx; line-height:1; }
.stat-value-strong{ font-size:30rpx; font-weight:800; color:#111827; }

/* Empty state */
.empty{ padding:32px 0; text-align:center; color:#9ca3af; }

/* Contacts */
.contact-row{
  display:flex; align-items:center; justify-content:space-between;
  padding:12px 0; border-bottom:1px solid #f0f0f0;
}
.contact-row:last-child{ border-bottom:none; }
.contact-name{ font-weight:600; color:#111; }
.contact-sub{ font-size:13px; color:#6b7280; }

/* Buttons */
.btn{
  display:inline-flex; align-items:center; justify-content:center;
  height:40px; padding:0 16px; border-radius:9999px;
  font-weight:800; font-size:14px;
  box-shadow:0 6px 14px rgba(0,0,0,.08);
  background: var(--sage, #93b2a1);
  color: #fff;
}
.btn.small{ height:34px; padding:0 12px; font-size:13px; box-shadow:none; }
.btn.primary{ background:var(--sage); color:#fff; }
.btn.success{ background:var(--success); color:#fff; }
.w-full{ width:100%; }
.mt-4{ margin-top:1rem; }

/* Popup */
.popup{
  background:#fff; border-radius:18px; padding:24px; width:20rem;
  box-shadow:var(--shadow);
}
.popup-title{ font-size:18px; font-weight:800; color:var(--ink); margin-bottom:12px; }

/* Form tweaks */
:deep(.uni-forms-item__label){ font-size:14px; color:#4b5563; font-weight:700; }
:deep(.uni-easyinput__content){
  border-radius:14px !important;
  border-color:#e5e7eb !important;
  min-height:44px; background:#fff;
}
:deep(.uni-easyinput__content-input){ font-size:15px; color:#111827; }
:deep(.uni-easyinput__placeholder-class){ color:#9ca3af !important; }
</style>
