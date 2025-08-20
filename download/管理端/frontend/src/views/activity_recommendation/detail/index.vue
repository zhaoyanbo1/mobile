<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">活动推荐</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、活动标题、活动时间、地点纬度、地点经度、地点详细地址、适宜人群、创建时间。</span>
      </div>
    </div>
    <div class="flex-start-end h-full m-r-10">
        <img src="@/assets/logo/document.png" height="130px" />
    </div>
</div>
<el-breadcrumb
separator="/"
class="m-20"
>
<el-breadcrumb-item
:to="{ path: '/' }"
>
首页
</el-breadcrumb-item>
<el-breadcrumb-item
:to="{ path: '/activity_recommendation' }"
>
活动推荐管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
活动推荐详情
</el-breadcrumb-item>
</el-breadcrumb>
<base-wrapper
class="p-10"
>
<el-tabs
class="demo-tabs"
>
<el-tab-pane
label="活动推荐详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"locationLatitude":[{"trigger":"blur","message":"地点纬度不能为空","required":true}],"locationLongitude":[{"trigger":"blur","message":"地点经度不能为空","required":true}],"activityTime":[{"trigger":"blur","message":"活动时间不能为空","required":true}],"locationAddress":[{"trigger":"blur","message":"地点详细地址不能为空","required":true}],"title":[{"trigger":"blur","message":"活动标题不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="活动标题" prop="title">
        <el-input clearable placeholder="请输入活动标题" v-model="form.title" />
    </el-form-item>

    <el-form-item   label="活动时间" prop="activityTime">
        <el-date-picker placeholder="请输入活动时间" v-model="form.activityTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="地点纬度" prop="locationLatitude">
        <el-input clearable placeholder="请输入地点纬度" v-model="form.locationLatitude" />
    </el-form-item>

    <el-form-item  label="地点经度" prop="locationLongitude">
        <el-input clearable placeholder="请输入地点经度" v-model="form.locationLongitude" />
    </el-form-item>

    <el-form-item  label="地点详细地址" prop="locationAddress">
        <el-input clearable placeholder="请输入地点详细地址" v-model="form.locationAddress" />
    </el-form-item>

    <el-form-item  label="适宜人群" prop="suitablePeople">
        <el-input clearable placeholder="请输入适宜人群" v-model="form.suitablePeople" />
    </el-form-item>

    <el-form-item   label="创建时间" prop="creationTime">
        <el-date-picker placeholder="请输入创建时间" v-model="form.creationTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

</el-form>
<base-layout
style="text-align: center;"
w_full="true"
>
<el-button
@click="submitForm"
round
color="#5D5FEF"
style="width: 200px;"
type="primary"
>保 存</el-button>
</base-layout>
</el-tab-pane>
</el-tabs>
</base-wrapper>
</base-wrapper>
   
</template>

<script setup>

const { proxy } = getCurrentInstance();
let form = ref({});
let formAll = ref({});
let rules = ref({});
let routerQuery = proxy.$route.query;
let detail = ref('normally')

function submitForm() {
  proxy.$refs.dataFormRef.validate(async (valid) => {
    if (valid) {

      if(import.meta.env.VITE_APP_MODEL === 'PREVIEW'){
          let res = await proxy.$api.table['update']({
              table_name: 'activity_recommendation',
              param: form.value
          });

          proxy.$modal.msgSuccess(res.message);

          if(res.code == 0){
              refresh();
          }
      }
      else{
          let res = await proxy.$api.activity_recommendation['update'](form.value);
          proxy.$modal.msgSuccess(res.message);
          if(res.code == 0){
            refresh();
          }
      }
    }
  });
}

async function getDetail(id) {
    if(import.meta.env.VITE_APP_MODEL === 'PREVIEW'){
        let res = await proxy.$api.table.get({
            table_name: 'activity_recommendation',
            param: {
                activityRecommendationId: id
            }
        });

        if(res.code == 0){
            form.value = Object.assign({}, res.data);
        }
        else{
            proxy.$modal.msgError(res.message);
        }
    }
    else{
        let res = await proxy.$api.activity_recommendation['get'](id);
        if(res.code == 0){
            form.value = Object.assign({}, res.data);
        }
        else{
            proxy.$modal.msgError(res.message);
        }
    }
}

async function getDetailAll(id){
    if(import.meta.env.VITE_APP_MODEL === 'PREVIEW'){
        let res = await proxy.$api.table.get({
            table_name: 'activity_recommendation',
            param: {
                activityRecommendationId: id
            }
        });

        if(res.code == 0){
            formAll.value = Object.assign({}, res.data);
        }
        else{
            proxy.$modal.msgError(res.message);
        }
    }
    else{
        let res = await proxy.$api.activity_recommendation['get'](id);
        if(res.code == 0){
            formAll.value = Object.assign({}, res.data);
        }
        else{
            proxy.$modal.msgError(res.message);
        }
    }
}

function refresh(){
    if(routerQuery.activityRecommendationId){
        getDetail(routerQuery.activityRecommendationId);
    }
    if(detail.value == 'all'){
        if(routerQuery.activityRecommendationId){
            getDetailAll(routerQuery.activityRecommendationId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>