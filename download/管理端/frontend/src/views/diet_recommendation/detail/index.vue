<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">饮食推荐</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、食谱标题、烹饪难度、所需时间、创建时间。</span>
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
:to="{ path: '/diet_recommendation' }"
>
饮食推荐管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
饮食推荐详情
</el-breadcrumb-item>
</el-breadcrumb>
<base-wrapper
class="p-10"
>
<el-tabs
class="demo-tabs"
>
<el-tab-pane
label="饮食推荐详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"title":[{"trigger":"blur","message":"食谱标题不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="食谱标题" prop="title">
        <el-input clearable placeholder="请输入食谱标题" v-model="form.title" />
    </el-form-item>

    <el-form-item  label="烹饪难度" prop="difficulty">
        <el-input clearable placeholder="请输入烹饪难度" v-model="form.difficulty" />
    </el-form-item>

    <el-form-item  label="所需时间" prop="requiredTime">
        <el-input clearable placeholder="请输入所需时间" v-model="form.requiredTime" />
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
              table_name: 'diet_recommendation',
              param: form.value
          });

          proxy.$modal.msgSuccess(res.message);

          if(res.code == 0){
              refresh();
          }
      }
      else{
          let res = await proxy.$api.diet_recommendation['update'](form.value);
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
            table_name: 'diet_recommendation',
            param: {
                dietRecommendationId: id
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
        let res = await proxy.$api.diet_recommendation['get'](id);
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
            table_name: 'diet_recommendation',
            param: {
                dietRecommendationId: id
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
        let res = await proxy.$api.diet_recommendation['get'](id);
        if(res.code == 0){
            formAll.value = Object.assign({}, res.data);
        }
        else{
            proxy.$modal.msgError(res.message);
        }
    }
}

function refresh(){
    if(routerQuery.dietRecommendationId){
        getDetail(routerQuery.dietRecommendationId);
    }
    if(detail.value == 'all'){
        if(routerQuery.dietRecommendationId){
            getDetailAll(routerQuery.dietRecommendationId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>