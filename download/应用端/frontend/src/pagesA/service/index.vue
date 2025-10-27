<template>
  <base-layout>
    <view class="page">
      <!-- Topbar -->
      <view class="topbar">
        <image :src="icons.menu" mode="widthFix" class="icon" />
        <text class="title">Service</text>
        <view class="right-spacer"></view>
      </view>

      <!-- Content -->
      <view class="container">
        <!-- Top Health Icon -->
        <view class="center mb-8 animate-bounce-slow">
          <view class="pill-wrap">
            <text class="pill-emoji">💊</text>
            <view class="pill-dot" />
          </view>
        </view>

        <text class="h2 center mb-8">Health Management</text>

        <!-- Supplement Reminder -->
        <view class="section">
          <view class="row gap-2 mb-4">
            <text class="section-icon">💊</text>
            <text class="h3">Supplement Reminder</text>
          </view>

          <view class="card-outer green-grad">
            <view
                v-for="s in supplements"
                :key="s.id"
                class="card-white animate-fade-card"
            >
              <view class="row gap-4">
                <view class="icon-box green-soft">
                  <text class="icon-emoji">{{ s.icon }}</text>
                </view>

                <view class="flex-1">
                  <text class="h4" :class="s.taken ? 'dim' : ''">{{ s.name }}</text>
                  <text class="muted">{{ s.description }}</text>
                </view>

                <button
                    class="btn-check"
                    :class="s.taken ? 'btn-check-on' : 'btn-check-off'"
                    @click="toggleSupplement(s)"
                >
                  <text v-if="s.taken" class="btn-check-tick">✓</text>
                </button>
              </view>
            </view>
          </view>
        </view>

        <!-- Vaccine Reminder -->
        <view class="section">
          <view class="row gap-2 mb-4">
            <text class="section-icon">💉</text>
            <text class="h3">Vaccine Reminder</text>
          </view>

          <view class="card-outer sand-grad">
            <view
                v-for="v in vaccines"
                :key="v.id"
                class="card-white animate-fade-card"
            >
              <view class="row gap-4 align-start">
                <view class="icon-box sand-soft">
                  <text class="icon-emoji">💉</text>
                </view>

                <view class="flex-1">
                  <view class="row gap-2 mb-1">
                    <text class="h4" :class="v.completed ? 'dim' : ''">{{ v.name }}</text>
                    <text v-if="!v.completed" class="bell">🔔</text>
                  </view>
                  <text class="muted mb-2">{{ v.description }}</text>
                  <text class="due">Due Date: {{ v.dueDate }}</text>
                </view>
              </view>

              <view class="row gap-2 mt-3">
                <button
                    class="btn-wide"
                    :class="v.completed ? 'btn-done' : 'btn-primary'"
                    @click="toggleVaccine(v)"
                >
                  <text class="btn-text">
                    {{ v.completed ? 'Completed ✓' : 'Book Appointment' }}
                  </text>
                </button>
              </view>
            </view>
          </view>
        </view>

        <view class="center mt-8 animate-fade-in">
          <text class="muted">💚 Take care of your health and enjoy life</text>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
const { proxy } = getCurrentInstance()

/** ======================
 * Mocked data
 ====================== */
const icons = { menu: '/static/gg_menu-left-alt.svg' }

const supplements = ref([
  { id: 1, name: 'Calcium Tablets', description: 'Daily calcium supplement for stronger bones', icon: '💊', taken: false },
  { id: 2, name: 'Vitamin D', description: 'Helps calcium absorption', icon: '☀️', taken: false },
  { id: 3, name: 'Fish Oil', description: 'Supports cardiovascular health', icon: '🐟', taken: false }
])

const vaccines = ref([
  { id: 1, name: 'Flu Vaccine', description: 'Flu vaccine not completed this month', dueDate: 'Before Oct 31', completed: false },
  { id: 2, name: 'Pneumonia Vaccine', description: 'Recommended once a year', dueDate: 'Before Nov 15', completed: false }
])

/** ======================
 * Core function — Add reminder (same as reminder_edit.vue)
 ====================== */
async function addReminder({ reminder_type_enum_id, title, description }) {
  try {
    // 获取用户信息
    const userRes = await proxy.$cf.login.getLoginUser()
    if (!userRes.success) {
      proxy.$cf.toast({ message: 'Please sign in first', level: 'error' })
      return
    }

    // 组装数据（10分钟后提醒）
    const saveData = {
      reminder_type_enum_id,
      title,
      description,
      reminder_time: new Date(Date.now() + 10 * 60 * 1000)
          .toLocaleString('sv-SE', { hour12: false }) // 输出类似 2025-10-20 19:05:02
          .replace('T', ' '),
      is_completed: false,
      user_info_user_info_id_1: userRes.data.user_info_id
    }

    // ✅ 使用与 reminder_edit.vue 相同的 update()
    const res = await proxy.$cf.table.update({
      table_name: 'reminder_item',
      param: saveData
    })

    if (res.success) {
      proxy.$cf.toast({ message: 'Task added successfully', level: 'success' })
    } else {
      proxy.$cf.toast({ message: 'Failed to add task', level: 'error' })
    }
  } catch (err) {
    console.error('Add reminder failed:', err)
    proxy.$cf.toast({ message: 'Save failed', level: 'error' })
  }
}

/** ======================
 * User interaction logic
 ====================== */
async function toggleSupplement(s) {
  s.taken = !s.taken
  if (s.taken) {
    await addReminder({
      reminder_type_enum_id: 1, // Medication
      title: s.name,
      description: s.description
    })
  }
}

async function toggleVaccine(v) {
  v.completed = !v.completed
  if (v.completed) {
    await addReminder({
      reminder_type_enum_id: 2, // Activity
      title: v.name,
      description: v.description + ' ' + (v.dueDate || '')
    })
  }
}
</script>

<style scoped>
.page{ min-height:100vh; background:#F8F9F8; }
.container{ padding:48rpx 32rpx 96rpx; }
.topbar{ display:grid; grid-template-columns:112rpx 1fr 112rpx; align-items:center; height:112rpx; background:#fff; padding:0 24rpx; }
.icon{ width:64rpx; height:48rpx; }
.title{ text-align:center; font-size:44rpx; font-weight:800; color:#111; }
.right-spacer{ width:112rpx; }

.center{ display:flex; align-items:center; justify-content:center; }
.mb-8{ margin-bottom:32rpx; }
.mt-8{ margin-top:32rpx; }
.h2{ font-size:44rpx; font-weight:800; color:#1D1D1D; }
.h3{ font-size:38rpx; font-weight:700; color:#1D1D1D; }
.h4{ font-size:34rpx; font-weight:700; color:#1D1D1D; }
.dim{ opacity:.6; }
.muted{ color:#6b7280; font-size:28rpx; }

.section{ margin-bottom:40rpx; }
.section-icon{ font-size:36rpx; }

.card-outer{ border-radius:36rpx; padding:24rpx; box-shadow:0 3px 15px rgba(0,0,0,.08); }
.green-grad{ background:linear-gradient(135deg, rgba(163,177,138,.10), rgba(126,140,119,.05)); }
.sand-grad{  background:linear-gradient(135deg, rgba(221,184,146,.10), rgba(201,168,124,.05)); }

.card-white{
  background:rgba(255,255,255,.8);
  backdrop-filter:saturate(1.2) blur(6px);
  border-radius:28rpx;
  padding:24rpx;
  box-shadow:0 2px 8px rgba(0,0,0,.06);
  margin-bottom:18rpx;
}

.row{ display:flex; align-items:center; }
.align-start{ align-items:flex-start; }
.gap-2{ gap:12rpx; }
.gap-4{ gap:20rpx; }
.flex-1{ flex:1; }
.mb-4{ margin-bottom:16rpx; }
.mb-2{ margin-bottom:8rpx; }
.mt-3{ margin-top:12rpx; }

.icon-box{
  width:96rpx; height:96rpx; border-radius:24rpx;
  display:flex; align-items:center; justify-content:center;
}
.green-soft{ background:linear-gradient(135deg, rgba(163,177,138,.20), rgba(126,140,119,.10)); }
.sand-soft{  background:linear-gradient(135deg, rgba(221,184,146,.20), rgba(201,168,124,.10)); }
.icon-emoji{ font-size:48rpx; }

.btn-check{
  width:80rpx; height:80rpx; border-radius:20rpx; display:flex; align-items:center; justify-content:center;
  transition: all .2s ease;
}
.btn-check-on{ background:#A3B18A; color:#fff; box-shadow:0 4px 12px rgba(0,0,0,.12); }
.btn-check-off{ background:#e5e7eb; }
.btn-check-tick{ font-size:36rpx; font-weight:800; }

.btn-wide{
  flex:1; padding:16rpx 0; border-radius:20rpx; display:flex; align-items:center; justify-content:center;
}
.btn-primary{ background:#DDB892; color:#fff; box-shadow:0 4px 12px rgba(0,0,0,.12); }
.btn-done{ background:#A3B18A; color:#fff; box-shadow:0 4px 12px rgba(0,0,0,.12); }
.btn-text{ font-size:30rpx; font-weight:700; }

.bell{ font-size:28rpx; color:#DDB892; }
.due{ color:#DDB892; }

.animate-bounce-slow{ animation: float 2s ease-in-out infinite; }
.animate-fade-in{ animation: fade-in .3s ease-out .4s both; }
.animate-fade-card{ animation: fade-card .3s ease-out both; }

@keyframes float{
  0%,100%{ transform:translateY(0); opacity:1; }
  50%{ transform:translateY(-10px); }
}
@keyframes fade-in{ from{ opacity:0; } to{ opacity:1; } }
@keyframes fade-card{ from{ opacity:0; transform:translateY(10px); } to{ opacity:1; transform:translateY(0); } }
</style>
