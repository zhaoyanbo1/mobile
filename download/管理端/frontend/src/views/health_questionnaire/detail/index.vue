<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">健康问卷</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、用户ID、姓名、年龄、居住情况、慢性病、过敏史、常用药、饮食偏好、运动频率、创建时间、更新时间、版本号。</span>
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
:to="{ path: '/health_questionnaire' }"
>
健康问卷管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
健康问卷详情
</el-breadcrumb-item>
</el-breadcrumb>
<base-wrapper
class="p-10"
>
<el-tabs
class="demo-tabs"
>
<el-tab-pane
label="健康问卷详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"name":[{"trigger":"blur","message":"姓名不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"用户ID不能为空","required":true}],"age":[{"trigger":"blur","message":"年龄不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item   label="用户ID" prop="userInfoUserInfoId1">
        <base-select clearable v-model="form.userInfoUserInfoId1" api="health_questionnaire_all.get_user_info_list" show_name=""></base-select>
    </el-form-item>

    <el-form-item  label="姓名" prop="name">
        <el-input clearable placeholder="请输入姓名" v-model="form.name" />
    </el-form-item>

    <el-form-item  label="年龄" prop="age">
        <el-input clearable placeholder="请输入年龄" v-model="form.age" />
    </el-form-item>

    <el-form-item  label="居住情况" prop="residence">
        <el-input clearable placeholder="请输入居住情况" v-model="form.residence" />
    </el-form-item>

    <el-form-item  label="慢性病" prop="chronicDisease">
        <el-input clearable placeholder="请输入慢性病" v-model="form.chronicDisease" />
    </el-form-item>

    <el-form-item  label="过敏史" prop="allergyHistory">
        <el-input clearable placeholder="请输入过敏史" v-model="form.allergyHistory" />
    </el-form-item>

    <el-form-item  label="常用药" prop="commonMedication">
        <el-input clearable placeholder="请输入常用药" v-model="form.commonMedication" />
    </el-form-item>

    <el-form-item  label="饮食偏好" prop="dietPreference">
        <el-input clearable placeholder="请输入饮食偏好" v-model="form.dietPreference" />
    </el-form-item>

    <el-form-item  label="运动频率" prop="exerciseFrequency">
        <el-input clearable placeholder="请输入运动频率" v-model="form.exerciseFrequency" />
    </el-form-item>

    <el-form-item   label="创建时间" prop="creationTime">
        <el-date-picker placeholder="请输入创建时间" v-model="form.creationTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="更新时间" prop="updateTime">
        <el-date-picker placeholder="请输入更新时间" v-model="form.updateTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="版本号" prop="version">
        <el-input clearable placeholder="请输入版本号" v-model="form.version" />
    </el-form-item>

</el-form>
<base-cell
header="所属用户信息"
label-width="180px"
class="m-b-10"
>
<base-cell-item
label="手机号"
>
{{ formAll.phoneNumber }}
</base-cell-item>
<base-cell-item
label="用户名"
>
{{ formAll.username }}
</base-cell-item>
<base-cell-item
label="密码"
>
{{ formAll.password }}
</base-cell-item>
<base-cell-item
label="注册时间"
>
{{ formAll.registrationDate }}
</base-cell-item>
<base-cell-item
label="最后登录时间"
>
{{ formAll.lastLoginDate }}
</base-cell-item>
</base-cell>
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
let detail = ref('all')

function submitForm() {
  proxy.$refs.dataFormRef.validate(async (valid) => {
    if (valid) {

      if(import.meta.env.VITE_APP_MODEL === 'PREVIEW'){
          let res = await proxy.$api.table['update']({
              table_name: 'health_questionnaire',
              param: form.value
          });

          proxy.$modal.msgSuccess(res.message);

          if(res.code == 0){
              refresh();
          }
      }
      else{
          let res = await proxy.$api.health_questionnaire['update'](form.value);
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
            table_name: 'health_questionnaire',
            param: {
                healthQuestionnaireId: id
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
        let res = await proxy.$api.health_questionnaire['get'](id);
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
            table_name: 'health_questionnaire',
            param: {
                healthQuestionnaireId: id
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
        let res = await proxy.$api.health_questionnaire_all['get'](id);
        if(res.code == 0){
            formAll.value = Object.assign({}, res.data);
        }
        else{
            proxy.$modal.msgError(res.message);
        }
    }
}

function refresh(){
    if(routerQuery.healthQuestionnaireId){
        getDetail(routerQuery.healthQuestionnaireId);
    }
    if(detail.value == 'all'){
        if(routerQuery.healthQuestionnaireId){
            getDetailAll(routerQuery.healthQuestionnaireId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>