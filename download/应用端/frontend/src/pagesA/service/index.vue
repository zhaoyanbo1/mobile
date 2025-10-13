<!-- src/pagesA/service/index.vue -->
<template>
  <base-layout>
    <view class="page">
      <!-- Topbar -->
      <view class="topbar">
        <image :src="icons.menu" mode="widthFix" class="icon" />
        <text class="title">Service</text>
        <view class="right-spacer"></view>
      </view>

      <!-- Scrollable content -->
      <view class="content">
        <view class="inner">
          <!-- ===== Section 1: Relaxing activities ===== -->
          <view class="section">
            <view class="sec-head">
              <text class="sec-title">Relaxing activities</text>
              <view class="refresh" @click="refresh('relax')" role="button" aria-label="refresh relax">
                <text class="refresh-glyph">↻</text>
              </view>
            </view>

            <view class="cards">
              <view
                  v-for="it in sections.relax"
                  :key="'relax-'+it.id"
                  class="choice-card"
                  :style="{ background: it.bg }"
                  @click="toggleTaken('relax', it.id)"
              >
                <view class="choice-icon">
                  <image :src="it.icon" mode="aspectFit" class="choice-icon-img" />
                </view>
                <view class="choice-texts">
                  <text class="choice-title">{{ it.title }}</text>
                  <text class="choice-link" :class="{ done: !!it.taken }">
                    {{ it.taken ? 'Taken' : 'Mark as Taken' }}
                  </text>
                </view>
              </view>
            </view>
          </view>

          <!-- ===== Section 2: Dietary Precautions ===== -->
          <view class="section">
            <view class="sec-head">
              <text class="sec-title">Dietary Precautions</text>
              <view class="refresh" @click="refresh('diet')" role="button" aria-label="refresh diet">
                <text class="refresh-glyph">↻</text>
              </view>
            </view>

            <view class="cards">
              <view
                  v-for="it in sections.diet"
                  :key="'diet-'+it.id"
                  class="choice-card"
                  :style="{ background: it.bg }"
                  @click="toggleTaken('diet', it.id)"
              >
                <view class="choice-icon">
                  <image :src="it.icon" mode="aspectFit" class="choice-icon-img" />
                </view>
                <view class="choice-texts">
                  <text class="choice-title">{{ it.title }}</text>
                  <text class="choice-link" :class="{ done: !!it.taken }">
                    {{ it.taken ? 'Taken' : 'Mark as Taken' }}
                  </text>
                </view>
              </view>
            </view>
          </view>

          <!-- ===== Section 3: Today's Medications ===== -->
          <view class="section">
            <view class="sec-head">
              <text class="sec-title">Today's Medications</text>
              <view class="refresh" @click="refresh('meds')" role="button" aria-label="refresh meds">
                <text class="refresh-glyph">↻</text>
              </view>
            </view>

            <view class="cards">
              <view
                  v-for="it in sections.meds"
                  :key="'med-'+it.id"
                  class="choice-card"
                  :style="{ background: it.bg }"
                  @click="toggleTaken('meds', it.id)"
              >
                <view class="choice-icon">
                  <image :src="it.icon" mode="aspectFit" class="choice-icon-img" />
                </view>
                <view class="choice-texts">
                  <text class="choice-title">{{ it.title }}</text>
                  <text class="choice-link" :class="{ done: !!it.taken }">
                    {{ it.taken ? 'Taken' : 'Mark as Taken' }}
                  </text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
/* English comments */
import { reactive } from 'vue'
import menuIcon from '@/static/gg_menu-left-alt.svg'
import massage from '@/static/service/massage.svg'
import apple from '@/static/service/apple.svg'
import golf from '@/static/service/golf.svg'
import pill from '@/static/service/pill.svg'
import coffee from '@/static/service/coffee.svg'
import yoga from '@/static/service/yoga.svg'
import banana from '@/static/service/banana.svg'
import { getCurrentInstance } from 'vue'
// 获取 proxy 实例（用于访问全局 $cf）
const { proxy } = getCurrentInstance()

const icons = { menu: menuIcon }

/* Data pools (replace with API if needed) */
const poolRelax = [
  { id: 1,  title: "An hour's massage",             icon: massage, bg: '#F4E4D7' },
  { id: 2,  title: 'Participate in golf activities', icon: golf,    bg: '#D7EFE2' },
  { id: 3,  title: 'Meditation for 15 minutes',      icon: pill,    bg: '#E6ECFF' },
  { id: 4,  title: 'Light yoga session',             icon: yoga,    bg: '#FFF1E0' },
]
const poolDiet = [
  { id: 11, title: 'A cup of milk', icon: coffee,  bg: '#F7EFE4' },
  { id: 12, title: 'An apple',      icon: apple,   bg: '#E4F1EC' },
  { id: 13, title: 'Banana',        icon: banana,  bg: '#FFF7DF' },
]
const poolMeds = [
  { id: 21, title: 'Ibuprofen tablet',                icon: pill,   bg: '#DF6D5D' },
  { id: 22, title: 'Take vitamin C tablet',           icon: pill,   bg: '#F2C77A' },
  { id: 23, title: 'Aspirin one tablet',              icon: pill,   bg: '#B9C7DA' },
  { id: 24, title: 'Granule for treating cold 200ml', icon: coffee, bg: '#D9C9E9' },
]

function sampleN(pool, n = 2) {
  return [...pool].sort(() => Math.random() - 0.5).slice(0, n).map(it => ({ ...it, taken: false }))
}

/* State */
const sections = reactive({
  relax : sampleN(poolRelax, 2),
  diet  : sampleN(poolDiet,  2),
  meds  : sampleN(poolMeds,  2),
})

/* Actions */
function refresh(which) {
  const map = { relax: poolRelax, diet: poolDiet, meds: poolMeds }
  sections[which] = sampleN(map[which], 2)
}
async function toggleTaken(which, id) {
  const list = sections[which]
  const idx = list.findIndex(x => x.id === id)
  if (idx < 0) return

  list[idx].taken = !list[idx].taken

  try {
    const userRes = await proxy.$cf.login.getLoginUser()
    if (!userRes.success) return

    const now = new Date()
    const plus10_5Hours = new Date(now.getTime() + 10.5 * 60 * 60 * 1000)
    const formattedTime = plus10_5Hours.toISOString().slice(0, 19).replace('T', ' ')
    const item = list[idx]
    const saveData = {
      reminder_item_id: id, // 真实的主键 ID
      is_completed: item.taken,
      reminder_time: formattedTime,
      reminder_type_enum_id:
          which === 'meds' ? 1 :
              which === 'relax' ? 2 :
                  which === 'diet' ? 3 : null,
      title: item.title,
      user_info_user_info_id_1: userRes.data.user_info_id
    }

    const updateRes = await proxy.$cf.table.update({
      table_name: 'reminder_item',
      param: saveData
    })

  } catch (e) {
    console.error('toggleTaken save failed:', e)
  }
}
</script>

<style scoped>
/* ===== Tokens (card 3× height; big title-card gap; 1/3|2/3 layout; centered texts with 3× spacing) ===== */
.page{
  --bg:#f7f7f7;
  --text:#222;
  --muted:#666;
  --white:#fff;
  --shadow:0 6px 14px rgba(0,0,0,.08);

  /* card size & spacing */
  --radius:18rpx;
  --card-min-h:312rpx;     /* 3× base height */
  --card-gap:24rpx;        /* gap between cards */
  --head-gap:40rpx;        /* gap between section title and cards */
  --section-gap:36rpx;     /* gap between sections */

  /* paddings */
  --card-pad-y:24rpx;
  --card-pad-x:24rpx;

  /* icon size (auto scales with card height; override if needed) */
  --icon-size: calc(var(--card-min-h) * 0.32);

  /* typography (already enlarged) */
  --section-title-size:48rpx;
  --title-size:40rpx;
  --link-size:34rpx;

  /* texts spacing inside card (3× the previous 14rpx) */
  --text-gap:42rpx;
}

.content{
  padding:24rpx 0 80rpx;
}
.inner{
  width:690rpx;
  margin:0 auto;
}

/* Topbar */
.topbar{
  display:grid;
  grid-template-columns:112rpx 1fr 112rpx;
  align-items:center;
  height:112rpx;
  background:var(--white);
  padding:0 24rpx;
}
.icon{
  width:64rpx;
  height:48rpx;
}
.title{
  text-align:center;
  font-size:44rpx;
  font-weight:800;
  color:#111;
}
.right-spacer{
  width:112rpx;
}

/* Sections */
.section{
  margin-top: var(--section-gap);
}
.sec-head{
  display:grid;
  grid-template-columns:1fr auto;
  align-items:center;
  margin:10rpx 0 var(--head-gap);
}
.sec-title{
  font-size:var(--section-title-size);
  font-weight:900;
  color:#111;
}

/* ⟳ Bigger/bolder refresh button */
.refresh{
  width:68rpx;
  height:68rpx;
  display:flex;
  align-items:center;
  justify-content:center;
  border-radius:50%;
  background:#eef3ef;
  border:2rpx solid #dfe7e2;
  box-shadow:0 3px 10px rgba(0,0,0,.08);
  transition:transform .12s ease;
}
.refresh:active{ transform: scale(.95) rotate(-12deg); }

.refresh-glyph{
  font-size:44rpx;      /* bigger */
  font-weight:900;      /* bolder */
  line-height:1;
  color:#1f2937;        /* darker for clarity */
  transform: translateY(-1rpx); /* optical tweak */
}

/* Cards list */
.cards{
  display:grid;
  gap:var(--card-gap);
}

/* Card: grid 1/3 | 2/3 */
.choice-card{
  min-height: var(--card-min-h);
  border-radius: var(--radius);
  display: grid;
  grid-template-columns: 1fr 2fr;   /* left 1/3 for icon, right 2/3 for texts */
  align-items: stretch;             /* let right column fill height for vertical centering */
  column-gap: 18rpx;
  padding: var(--card-pad-y) var(--card-pad-x);
  box-shadow: var(--shadow);
  color:#111;
}

/* Icon: enlarged */
.choice-icon{
  justify-self: center;
  align-self: center;
  background: transparent;
  box-shadow: none;
  border-radius: 0;
  width: auto;
  height: auto;
}
.choice-icon-img{
  width: var(--icon-size);
  height: var(--icon-size);
  display: block;
}

/* Texts: centered in right 2/3 with large vertical gap */
.choice-texts{
  align-self: stretch;
  height: 100%;
  display:flex; flex-direction:column;
  justify-content:center;     /* vertical center */
  align-items:center;         /* horizontal center */
  text-align:center;
  gap: var(--text-gap);       /* bigger spacing between lines */
}
.choice-title{
  font-size:var(--title-size);
  font-weight:800;
  line-height:1.35;
}
.choice-link{
  font-size:var(--link-size);
  color:#6b7280;
}
.choice-link.done{
  color:#1e293b;
  text-decoration:line-through;
}
</style>
