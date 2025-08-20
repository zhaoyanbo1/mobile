<script setup>
setTimeout(() => {
  const fontSize = window.innerWidth / 100; // 根据视口宽度动态计算
document.documentElement.style.fontSize = `${fontSize*3.5}px`;
}, 10);
</script>
<style>
/*每个页面公共css */
::v-deep .uni-popup__wrapper{
  background-color: white !important;
  border-radius: 10px;
  overflow-y: auto;
}
/* 禁止透传 */
::v-deep uni-popup{
  z-index: 99999 !important;
}
html, body {
  touch-action: manipulation; /* 禁用双击放大 */
  font-size: 12px;
}
page{
  height: 100% !important;
}
::v-deep .uni-card__cover-image {
  width: 100% !important;
}
*,html,body,view,div {
  box-sizing: border-box;
}
</style>
