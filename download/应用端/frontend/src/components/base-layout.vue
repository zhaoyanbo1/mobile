<template>
  <view class="layout-container">
    <!-- Content area: reserve bottom space (tab height + safe area) so content isn't covered -->
    <view
        class="content-container"
        :style="{ paddingBottom: isTab ? 'calc(128rpx + env(safe-area-inset-bottom))' : '0' }"
    >
      <slot></slot>
    </view>

    <!-- Custom TabBar (already fixed to the bottom) -->
    <base-tabBar v-if="isTab" :tabList="tabList" />

    <!-- Floating entry to AI Coach -->
    <view
        v-if="showCoachButton"
        class="ai-coach-button"
        :style="coachButtonStyle"
        role="button"
        tabindex="0"
        @click="goAiChat"
        @keyup.enter="goAiChat"
    >
      ask AI Coach
    </view>
  </view>
</template>

<script setup>
import {onLoad, onShow} from '@dcloudio/uni-app'
import {ref, onMounted, getCurrentInstance, computed} from 'vue'
import {dynamicTabBars} from '../tabbar/tabbar'

const tabList = ref(dynamicTabBars)
const isTab = ref(false)
const showCoachButton = ref(false)
const proxy = getCurrentInstance()?.proxy
const aiChatRoute = 'pagesA/chat/ai_chat/index'

const checkIsTab = () => {
  const pages = getCurrentPages()
  if (!pages.length) return
  const curPage = pages[pages.length - 1]
  const route = curPage.route
  // Prefix-match against tab config; also handle routes ending with /index
  isTab.value = tabList.value.some(item =>
      route.includes(item.url.replace(/^\//, '').replace('/index', ''))
  )

  showCoachButton.value = !!route && !route.includes(aiChatRoute)
}

const goAiChat = () => {
  const target = '/pagesA/chat/ai_chat/index'
  //const normalizedTarget = target.replace(/^\//, '')

  if (proxy?.$cf?.navigate?.to) {
    proxy.$cf.navigate.to({
      url: target, // 保留完整路径
      type: 'page',
      mode: 'navigate'
    })
    return
  }

  uni.navigateTo({url: target})
}

const coachButtonStyle = computed(() => {
  const bottomOffset = isTab.value ? 160 : 96
  return {
    bottom: `calc(${bottomOffset}rpx + env(safe-area-inset-bottom))`
  }
})

// Run on initial load, when page becomes visible again, and after mount
onLoad(checkIsTab)
onShow(checkIsTab)
onMounted(checkIsTab)
</script>

<style scoped>
/* Outer container fills viewport to allow flexible content */
.layout-container {
  position: relative;
  min-height: 100vh;
  width: 100%;
}

/* Main content wrapper; bottom padding keeps it clear of the custom TabBar */
.content-container {
  width: 100%;
  box-sizing: border-box;
}

.ai-coach-button {
  position: fixed;
  right: 32rpx;
  z-index: 9999;
  background: linear-gradient(135deg, #6a5cff, #8f7bff);
  color: #fff;
  font-size: 32rpx;
  padding: 20rpx 32rpx;
  border-radius: 999rpx;
  box-shadow: 0 12rpx 24rpx rgba(0, 0, 0, 0.18);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.ai-coach-button:focus-visible {
  outline: 4rpx solid rgba(255, 255, 255, 0.8);
  outline-offset: 4rpx;
}

.ai-coach-button:active {
  transform: translateY(4rpx);
  box-shadow: 0 8rpx 16rpx rgba(0, 0, 0, 0.18);
  opacity: 0.85;
}


/* If you need to force the custom TabBar above everything else, enable this:
::v-deep .tabbar-wrap{ z-index: 999999; }
*/
</style>
