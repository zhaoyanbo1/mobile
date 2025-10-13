<!-- src/pages/reminder_edit/index.vue -->
<template>
  <base-layout>
    <view class="page">
      <!-- Header -->
      <view class="page-head">
        <text class="page-title">{{ formData.reminder_item_id ? 'Edit Task' : 'Add Task' }}</text>
        <uni-icons type="close" size="24" color="#6b7280" @click="goBack" />
      </view>

      <!-- Form -->
      <uni-forms :modelValue="formData" label-position="top" class="card">
        <!-- Type -->
        <uni-forms-item required label="Type" name="reminder_type_enum_id" class="form-item">
          <uni-data-select
              v-model="formData.reminder_type_enum_id"
              :localdata="reminderTypes"
              placeholder="Please select"
              class="w-full"
          />
        </uni-forms-item>

        <!-- Title -->
        <uni-forms-item required label="Title" name="title" class="form-item">
          <uni-easyinput
              v-model="formData.title"
              type="text"
              placeholder="Enter title"
              class="w-full box-border"
          />
        </uni-forms-item>

        <!-- Description -->
        <uni-forms-item label="Description" name="description" class="form-item">
          <uni-easyinput
              v-model="formData.description"
              type="textarea"
              placeholder="Enter details (optional)"
              class="w-full box-border"
          />
        </uni-forms-item>

        <!-- Reminder time -->
        <uni-forms-item required label="Reminder Time" name="reminder_time" class="form-item">
          <uni-datetime-picker
              type="datetime"
              v-model="formData.reminder_time"
              placeholder="Pick date & time"
              class="w-full"
          />
        </uni-forms-item>

        <!-- Medication-only fields -->
<!--        <uni-forms-item-->
<!--            v-if="formData.reminder_type_enum_id == 1"-->
<!--            label="Medicine Photo"-->
<!--            name="medicine_photo"-->
<!--            class="form-item"-->
<!--        >-->
<!--          <base-upload-->
<!--              :limit="1"-->
<!--              mode="grid"-->
<!--              @success="handleUploadSuccess"-->
<!--              @delete="handleDeletePhoto"-->
<!--              :initialFiles="initialFiles"-->
<!--              class="w-full mb-4"-->
<!--          />-->
<!--        </uni-forms-item>-->

        <uni-forms-item
            v-if="formData.reminder_type_enum_id == 1"
            label="Dosage"
            name="medicine_dosage"
            class="form-item"
        >
          <uni-easyinput
              v-model="formData.medicine_dosage"
              type="text"
              placeholder="e.g. 1 tablet"
              class="w-full box-border"
          />
        </uni-forms-item>

<!--        &lt;!&ndash; Activity-only fields &ndash;&gt;-->
<!--        <uni-forms-item-->
<!--            v-if="formData.reminder_type_enum_id == 2"-->
<!--            label="Location"-->
<!--            name="location"-->
<!--            class="form-item"-->
<!--        >-->
<!--          <view class="mb-4">-->
<!--            <view class="btn primary" @click="showLocationPicker">-->
<!--              <uni-icons type="location-filled" color="#fff" size="18" />-->
<!--              <text class="btn-text">Choose Location</text>-->
<!--            </view>-->
<!--          </view>-->

<!--          <view v-if="formData.location_address" class="addr">-->
<!--            {{ formData.location_address }}-->
<!--          </view>-->

<!--          <base-map-view-->
<!--              v-if="formData.location_latitude && formData.location_longitude"-->
<!--              :latitude="formData.location_latitude"-->
<!--              :longitude="formData.location_longitude"-->
<!--              :markers="locationMarkers"-->
<!--              class="map"-->
<!--          />-->
<!--        </uni-forms-item>-->

        <!-- Status -->
        <uni-forms-item label="Status" name="is_completed" class="form-item">
          <view class="row">
            <switch
                :checked="formData.is_completed"
                @change="(e) => (formData.is_completed = e.detail.value)"
                :color="primaryColor"
            />
            <text class="ml-2 text-neutral-700">
              {{ formData.is_completed ? 'Completed' : 'Pending' }}
            </text>
          </view>
        </uni-forms-item>

        <!-- Save -->
        <view class="btn" @click="saveReminder" :class="{ disabled: loading }">
          <text class="btn-text">{{ loading ? 'Saving…' : 'Save' }}</text>
        </view>
      </uni-forms>

      <!-- Location picker -->
      <uni-popup ref="locationPopup" type="bottom" class="z-50">
        <view class="popup">
          <view class="popup-head">
            <text class="popup-title">Pick a Place</text>
            <uni-icons type="close" size="20" @click="closeLocationPicker" />
          </view>

          <base-map-view
              :latitude="currentLocation.latitude"
              :longitude="currentLocation.longitude"
              :markers="locationMarkers"
              @mapTap="handleMapTap"
              class="popup-map"
          />

          <view class="btn primary w-full mt-12" @click="confirmLocation">
            <text class="btn-text">Confirm Location</text>
          </view>
        </view>
      </uni-popup>
    </view>
  </base-layout>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
import { onLoad } from '@dcloudio/uni-app'

const { proxy } = getCurrentInstance()

/** Theme */
const primaryColor = '#ffffff' // sage
const successColor = '#ffffff'

/** Form state */
const formData = ref({
  reminder_item_id: null,
  reminder_type_enum_id: null,
  title: '',
  description: '',
  reminder_time: '',
  is_completed: false,
  medicine_photo: [],
  medicine_dosage: '',
  location_latitude: null,
  location_longitude: null,
  location_address: ''
})

/** Upload */
const initialFiles = ref([])

/** Select options */
const reminderTypes = ref([
  { value: 1, text: 'Medication' },
  { value: 2, text: 'Activity' },
  { value: 3, text: 'Diet' }
])

/** Map state */
const locationMarkers = ref([])
const selectedLocation = ref(null)
const currentLocation = ref({ latitude: 0, longitude: 0 })

/** UI */
const loading = ref(false)
const locationPopup = ref(null)

/* Get current location */
async function getCurrentPosition () {
  try {
    const res = await proxy.$cf.location.getCurrentLocation()
    if (res.success) {
      currentLocation.value = { latitude: res.latitude, longitude: res.longitude }
      if (!formData.value.location_latitude) {
        formData.value.location_latitude = res.latitude
        formData.value.location_longitude = res.longitude
        updateLocationMarkers()
      }
    }
  } catch (e) {
    console.error('Failed to get current location:', e)
  }
}

/* Update marker */
function updateLocationMarkers () {
  if (formData.value.location_latitude && formData.value.location_longitude) {
    locationMarkers.value = [{
      id: 1,
      latitude: formData.value.location_latitude,
      longitude: formData.value.location_longitude,
      iconPath: '/static/mark_icon.png',
      width: 40,
      height: 40
    }]
  }
}

/* Popup helpers */
function showLocationPicker () { locationPopup.value.open() }
function closeLocationPicker () { locationPopup.value.close() }

function handleMapTap (e) {
  selectedLocation.value = { latitude: e.latitude, longitude: e.longitude }
  locationMarkers.value = [{
    id: 1,
    latitude: e.latitude,
    longitude: e.longitude,
    iconPath: '/static/mark_icon.png',
    width: 40,
    height: 40
  }]
}

async function confirmLocation () {
  if (!selectedLocation.value) return
  formData.value.location_latitude = selectedLocation.value.latitude
  formData.value.location_longitude = selectedLocation.value.longitude

  try {
    const res = await proxy.$cf.location.getAddressByLocation({
      latitude: selectedLocation.value.latitude,
      longitude: selectedLocation.value.longitude
    })
    if (res.success) formData.value.location_address = res.result.address
  } catch (e) {
    console.error('Reverse geocoding failed:', e)
  }

  closeLocationPicker()
}

/* Upload handlers */
function handleUploadSuccess (res) {
  if (res.success) {
    formData.value.medicine_photo = [{ url: res.data.url }]
  }
}
function handleDeletePhoto () {
  formData.value.medicine_photo = []
}

/* Load details */
async function loadReminderDetail (reminderId) {
  try {
    loading.value = true
    const res = await proxy.$cf.table.get({
      table_name: 'reminder_item',
      param: { reminder_item_id: reminderId }
    })

    if (res.success && res.data) {
      const d = res.data
      formData.value = {
        reminder_item_id: d.reminder_item_id,
        reminder_type_enum_id: d.reminder_type_enum_id,
        title: d.title,
        description: d.description,
        reminder_time: d.reminder_time,
        is_completed: d.is_completed,
        medicine_photo: d.medicine_photo || [],
        medicine_dosage: d.medicine_dosage || '',
        location_latitude: d.location_latitude,
        location_longitude: d.location_longitude,
        location_address: d.location_address || ''
      }

      if (d.medicine_photo && d.medicine_photo.length > 0) {
        initialFiles.value = d.medicine_photo.map(it => ({
          ...it,
          url: proxy.get_resource_url(it.url)
        }))
      }

      updateLocationMarkers()
    }
  } catch (e) {
    console.error('Load reminder failed:', e)
    proxy.$cf.toast({ message: 'Failed to load task', level: 'error' })
  } finally {
    loading.value = false
  }
}

/* Save */
async function saveReminder () {
  try {
    loading.value = true

    if (!formData.value.reminder_type_enum_id) {
      proxy.$cf.toast({ message: 'Please choose a type', level: 'error' })
      return
    }
    if (!formData.value.title) {
      proxy.$cf.toast({ message: 'Please enter a title', level: 'error' })
      return
    }
    console.log(formData.value.reminder_time)
    if (!formData.value.reminder_time) {
      proxy.$cf.toast({ message: 'Please pick date & time', level: 'error' })
      return
    }

    const userRes = await proxy.$cf.login.getLoginUser()
    if (!userRes.success) {
      proxy.$cf.toast({ message: 'Please sign in first', level: 'error' })
      return
    }

    const saveData = { ...formData.value, user_info_user_info_id_1: userRes.data.user_info_id }

    const updateRes = await proxy.$cf.table.update({
      table_name: 'reminder_item',
      param: saveData
    })

    if (updateRes.success) {
      proxy.$cf.toast({ message: 'Task updated', level: 'success' })
      proxy.$cf.navigate.to({ url: '/pagesA/todo/index', type: 'page' })
    } else {
      proxy.$cf.toast({ message: 'Update failed', level: 'error' })
    }
  } catch (e) {
    console.error('Save reminder failed:', e)
    proxy.$cf.toast({ message: 'Save failed', level: 'error' })
  } finally {
    loading.value = false
  }
}

/* Back */
function goBack () {
  proxy.$cf.navigate.to({ url: '/pagesA/todo/index', type: 'page' })
}

/* Init */
onLoad((options) => {
  if (options.reminder_item_id) loadReminderDetail(options.reminder_item_id)
  getCurrentPosition()
})
</script>

<style scoped>
/* ==== Design tokens (match questionnaire page) ==== */
:root{
  --sage:#93b2a1;        /* primary */
  --ink:#111827;         /* text */
  --muted:#6b7280;       /* secondary text */
  --bg:#f8f8f8;          /* page bg */
  --surface:#ffffff;     /* card bg */
  --success:#1BAA5F;     /* success */
  --shadow:0 10px 22px rgba(0,0,0,.10);
  --hairline:rgba(17,24,39,.06);
}

.page{ padding:16px; background:var(--bg); min-height:100vh; }

.page-head{
  display:flex; align-items:center; justify-content:space-between;
  margin-bottom:12px;
}
.page-title{ font-size:20px; font-weight:800; color:var(--ink); }

/* Card container – same as questionnaire */
.card{
  background:var(--surface);
  border:1px solid var(--hairline);
  border-radius:20px;
  padding:18px;
  box-shadow:var(--shadow);
}

/* Form polish (keep labels on one line) */
.form-item :deep(.uni-forms-item__label){
  white-space:nowrap;
  font-size:14px; color:#4b5563; font-weight:700;
}
:deep(.uni-select), :deep(.uni-easyinput__content){
  border-radius:16px !important; border-color:#e5e7eb !important;
  min-height:48px; background:#fff;
}
:deep(.uni-select__input-text), :deep(.uni-easyinput__content-input){
  font-size:16px; color:var(--ink);
}
:deep(.uni-select__input-placeholder), :deep(.uni-easyinput__placeholder-class){
  color:#9ca3af !important;
}

/* Buttons (view buttons to keep theme) */
.btn{
  height:48px; border-radius:9999px;
  display:flex; align-items:center; justify-content:center; gap:8px;
  padding:0 16px; font-weight:800; font-size:16px;
  box-shadow:var(--shadow); background-color: #839f90;
}
.btn .btn-text{ font-size:16px; }
.btn.primary{ background:var(--sage); color:#fff; }
.btn.success{ background:var(--success); color:#fff; }
.w-full{ width:100%; }
.mt-12{ margin-top:12px; }
.mt-16{ margin-top:16px; }
.disabled{ opacity:.6; pointer-events:none; }

/* Map & address */
.map{ height:192px; border:1px solid #e5e7eb; border-radius:14px; }
.addr{ font-size:13px; color:var(--muted); margin-bottom:12px; }

/* Popup */
.popup{
  background:#fff; border-radius:16px 16px 0 0; padding:16px; min-height:60vh;
}
.popup-head{
  display:flex; align-items:center; justify-content:space-between; margin-bottom:8px;
}
.popup-title{ font-size:16px; font-weight:800; color:var(--ink); }
.popup-map{ height:48vh; border:1px solid #e5e7eb; border-radius:14px; }
.row{ display:flex; align-items:center; }

.ml-2{ margin-left:.5rem; }
.mb-4{ margin-bottom:1rem; }
.z-50{ z-index:50; }
</style>
