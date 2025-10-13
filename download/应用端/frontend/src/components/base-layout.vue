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
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { onLoad, onShow } from '@dcloudio/uni-app'
import { dynamicTabBars } from '../tabbar/tabbar'

const tabList = ref(dynamicTabBars)
const isTab = ref(false)

const checkIsTab = () => {
  const pages = getCurrentPages()
  if (!pages.length) return
  const curPage = pages[pages.length - 1]
  const route = curPage.route
  // Prefix-match against tab config; also handle routes ending with /index
  isTab.value = tabList.value.some(item =>
      route.includes(item.url.replace(/^\//, '').replace('/index', ''))
  )
}

// Run on initial load, when page becomes visible again, and after mount
onLoad(checkIsTab)
onShow(checkIsTab)
onMounted(checkIsTab)
</script>

<style scoped>
/* Outer container fills viewport to allow flexible content */
.layout-container{
  position: relative;
  min-height: 100vh;
  width: 100%;
}

/* Main content wrapper; bottom padding keeps it clear of the custom TabBar */
.content-container{
  width: 100%;
  box-sizing: border-box;
}

/* If you need to force the custom TabBar above everything else, enable this:
::v-deep .tabbar-wrap{ z-index: 999999; }
*/
</style>
