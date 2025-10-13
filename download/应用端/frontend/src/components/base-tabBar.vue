<template>
  <view class="tabbar" v-if="tabLists.length">
    <view
        v-for="(item, index) in tabLists"
        :key="index"
        :class="['tab_box', currentRoute == item.url ? 'on' : '']"
        :style="{ width: 100 / tabLists.length + '%' }"
    >
      <view class="tab_top" @click="tabFn(item.url)">
        <image
            class="tab_icon"
            :src="currentRoute == item.url ? item.onicon : item.icon"
            mode="aspectFit"
        />
      </view>
      <view class="tab_bot">
        {{ item.title || item.text }}
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()
const props = defineProps({
  tabList: { type: Array, default: () => [] }
})

const tabLists = ref(props.tabList)
const currentRoute = ref('')

/** Get current page route for active state */
const getCurrentRoute = () => {
  const pages = getCurrentPages()
  if (pages.length) {
    const currentPage = pages[pages.length - 1]
    currentRoute.value = `/${currentPage.route}`
  }
  return currentRoute.value
}
getCurrentRoute()

/** Navigate when a tab is clicked */
function tabFn(url) {
  proxy.$cf.navigate.to({ url, type: 'page', mode: 'redirect' })
}
</script>

<style scoped>
/* ========== Theme tokens (aligned with global style) ========== */
.tabbar{
  --brand-sage-700:#3F6D5A;   /* active text accent */
  --brand-sage-600:#6FA08F;
  --ink:#111827;              /* black */
  --muted:#8A8F98;            /* inactive text */
  --bar-bg-start:#FFFFFF;
  --bar-bg-end:#F8FAF9;
  --bar-border:#E5E7EB;
  --icon-size:56rpx;          /* default icon size */
  --icon-size-active:64rpx;   /* active icon size (slightly larger) */

  width:100%;
  height: calc(140rpx + env(safe-area-inset-bottom)); /* taller bar for more whitespace */
  padding-bottom: env(safe-area-inset-bottom);
  position:fixed; left:0; right:0; bottom:0; z-index:9;

  display:flex;
  background: linear-gradient(180deg, var(--bar-bg-start) 0%, var(--bar-bg-end) 100%);
  border-top:1rpx solid var(--bar-border);
  box-shadow: 0 -8rpx 24rpx rgba(0,0,0,0.05);
}

.tab_box{
  display:flex; flex-direction:column; align-items:center; justify-content:center;
  gap: 6rpx;
  color: var(--muted);
  transition: transform .16s ease;
}

/* Icon area */
.tab_top{ width:100%; text-align:center; line-height:1; }
.tab_icon{
  width: var(--icon-size);
  height: var(--icon-size);
}

/* Label text */
.tab_bot{
  width:100%; text-align:center;
  font-size: 28rpx;           /* 24rpx -> 28rpx */
  font-weight: 700;
  line-height: 1;
}

/* Active state: black text, slightly larger icon and a tiny lift */
.on{ color: var(--brand-sage-700); }
.on .tab_bot{ color: var(--ink); }
.on .tab_icon{
  width: var(--icon-size-active);
  height: var(--icon-size-active);
  transform: translateY(-2rpx);
}
</style>
