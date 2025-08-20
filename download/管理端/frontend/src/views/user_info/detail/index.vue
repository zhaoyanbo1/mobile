<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">用户信息</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、手机号、用户名、密码、注册时间、最后登录时间。</span>
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
:to="{ path: '/user_info' }"
>
用户信息管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
用户信息详情
</el-breadcrumb-item>
</el-breadcrumb>
<base-wrapper
class="p-10"
>
<el-tabs
class="demo-tabs"
>
<el-tab-pane
label="用户信息详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"password":[{"trigger":"blur","message":"密码不能为空","required":true}],"phoneNumber":[{"trigger":"blur","message":"手机号不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item  label="手机号" prop="phoneNumber">
        <el-input clearable placeholder="请输入手机号" v-model="form.phoneNumber" />
    </el-form-item>

    <el-form-item  label="用户名" prop="username">
        <el-input clearable placeholder="请输入用户名" v-model="form.username" />
    </el-form-item>

    <el-form-item  label="密码" prop="password">
        <el-input clearable placeholder="请输入密码" v-model="form.password" />
    </el-form-item>

    <el-form-item   label="注册时间" prop="registrationDate">
        <el-date-picker placeholder="请输入注册时间" v-model="form.registrationDate" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="最后登录时间" prop="lastLoginDate">
        <el-date-picker placeholder="请输入最后登录时间" v-model="form.lastLoginDate" type="datetime"
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
<el-tab-pane
label="查看健康问卷"
>
<health_questionnaire_component
 :params="{userInfoUserInfoId1:routerQuery.userInfoId}"
 />
</el-tab-pane>
<el-tab-pane
label="查看提醒事项"
>
<reminder_item_component
 :params="{userInfoUserInfoId1:routerQuery.userInfoId}"
 />
</el-tab-pane>
<el-tab-pane
label="查看紧急联系人"
>
<emergency_contact_component
 :params="{userInfoUserInfoId1:routerQuery.userInfoId}"
 />
</el-tab-pane>
<el-tab-pane
label="查看系统设置"
>
<system_settings_component
 :params="{userInfoUserInfoId1:routerQuery.userInfoId}"
 />
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
              table_name: 'user_info',
              param: form.value
          });

          proxy.$modal.msgSuccess(res.message);

          if(res.code == 0){
              refresh();
          }
      }
      else{
          let res = await proxy.$api.user_info['update'](form.value);
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
            table_name: 'user_info',
            param: {
                userInfoId: id
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
        let res = await proxy.$api.user_info['get'](id);
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
            table_name: 'user_info',
            param: {
                userInfoId: id
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
        let res = await proxy.$api.user_info['get'](id);
        if(res.code == 0){
            formAll.value = Object.assign({}, res.data);
        }
        else{
            proxy.$modal.msgError(res.message);
        }
    }
}

function refresh(){
    if(routerQuery.userInfoId){
        getDetail(routerQuery.userInfoId);
    }
    if(detail.value == 'all'){
        if(routerQuery.userInfoId){
            getDetailAll(routerQuery.userInfoId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>