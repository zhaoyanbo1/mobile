<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">紧急联系人</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、用户ID、联系人姓名、联系电话。</span>
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
:to="{ path: '/emergency_contact' }"
>
紧急联系人管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
紧急联系人详情
</el-breadcrumb-item>
</el-breadcrumb>
<base-wrapper
class="p-10"
>
<el-tabs
class="demo-tabs"
>
<el-tab-pane
label="紧急联系人详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"phoneNumber":[{"trigger":"blur","message":"联系电话不能为空","required":true}],"name":[{"trigger":"blur","message":"联系人姓名不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"用户ID不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item   label="用户ID" prop="userInfoUserInfoId1">
        <base-select clearable v-model="form.userInfoUserInfoId1" api="emergency_contact_all.get_user_info_list" show_name=""></base-select>
    </el-form-item>

    <el-form-item  label="联系人姓名" prop="name">
        <el-input clearable placeholder="请输入联系人姓名" v-model="form.name" />
    </el-form-item>

    <el-form-item  label="联系电话" prop="phoneNumber">
        <el-input clearable placeholder="请输入联系电话" v-model="form.phoneNumber" />
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
{{ formAll.phoneNumberUi }}
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
              table_name: 'emergency_contact',
              param: form.value
          });

          proxy.$modal.msgSuccess(res.message);

          if(res.code == 0){
              refresh();
          }
      }
      else{
          let res = await proxy.$api.emergency_contact['update'](form.value);
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
            table_name: 'emergency_contact',
            param: {
                emergencyContactId: id
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
        let res = await proxy.$api.emergency_contact['get'](id);
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
            table_name: 'emergency_contact',
            param: {
                emergencyContactId: id
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
        let res = await proxy.$api.emergency_contact_all['get'](id);
        if(res.code == 0){
            formAll.value = Object.assign({}, res.data);
        }
        else{
            proxy.$modal.msgError(res.message);
        }
    }
}

function refresh(){
    if(routerQuery.emergencyContactId){
        getDetail(routerQuery.emergencyContactId);
    }
    if(detail.value == 'all'){
        if(routerQuery.emergencyContactId){
            getDetailAll(routerQuery.emergencyContactId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>