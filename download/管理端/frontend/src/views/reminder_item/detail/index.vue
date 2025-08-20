<template>
     
  <base-wrapper
>
<div class="flex-between-start" style="background-color: #5D5FEF; height: 140px;">
    <div class="flex-c-center-start h-full">
      <div class="m-x-20">
          <span style="font-weight: bold; font-size: 20px; color: #FFFFFF;">提醒事项</span>
      </div>
      <div class="m-x-20 m-t-4">
        <span style="font-size: 14px; color: #FFFFFF;">主键、用户ID、提醒类型枚举ID、提醒标题、提醒内容、提醒时间、是否完成、药品照片、用药量、地点纬度、地点经度、地点详细地址、关联食谱ID、创建时间、更新时间。</span>
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
:to="{ path: '/reminder_item' }"
>
提醒事项管理
</el-breadcrumb-item>
<el-breadcrumb-item
>
提醒事项详情
</el-breadcrumb-item>
</el-breadcrumb>
<base-wrapper
class="p-10"
>
<el-tabs
class="demo-tabs"
>
<el-tab-pane
label="提醒事项详情"
>
<el-form
:model='form'
ref='dataFormRef'
label-position='top'
:rules='{"reminderTypeEnumId":[{"trigger":"blur","message":"提醒类型枚举ID不能为空","required":true}],"reminderTime":[{"trigger":"blur","message":"提醒时间不能为空","required":true}],"title":[{"trigger":"blur","message":"提醒标题不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"用户ID不能为空","required":true}],"isCompleted":[{"trigger":"blur","message":"是否完成不能为空","required":true}]}'
label-width='150px'
>
    <el-form-item   label="用户ID" prop="userInfoUserInfoId1">
        <base-select clearable v-model="form.userInfoUserInfoId1" api="reminder_item_all.get_user_info_list" show_name=""></base-select>
    </el-form-item>

    <el-form-item  label="提醒类型枚举ID" prop="reminderTypeEnumId">
        <el-input clearable placeholder="请输入提醒类型枚举ID" v-model="form.reminderTypeEnumId" />
    </el-form-item>

    <el-form-item  label="提醒标题" prop="title">
        <el-input clearable placeholder="请输入提醒标题" v-model="form.title" />
    </el-form-item>

    <el-form-item  label="提醒内容" prop="description">
        <el-input clearable placeholder="请输入提醒内容" v-model="form.description" />
    </el-form-item>

    <el-form-item   label="提醒时间" prop="reminderTime">
        <el-date-picker placeholder="请输入提醒时间" v-model="form.reminderTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="是否完成" prop="isCompleted">
        <el-select v-model="form.isCompleted" placeholder="请选择">
            <el-option label="是" :value="true"></el-option>
            <el-option label="否" :value="false"></el-option>
        </el-select>
    </el-form-item>

    <el-form-item  label="药品照片" prop="medicinePhotoResourceKey">
        <image-upload v-model="form.medicinePhoto" :type="true" />
    </el-form-item>

    <el-form-item  label="用药量" prop="medicineDosage">
        <el-input clearable placeholder="请输入用药量" v-model="form.medicineDosage" />
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

    <el-form-item  label="关联食谱ID" prop="dietRecipeId">
        <el-input clearable placeholder="请输入关联食谱ID" v-model="form.dietRecipeId" />
    </el-form-item>

    <el-form-item   label="创建时间" prop="creationTime">
        <el-date-picker placeholder="请输入创建时间" v-model="form.creationTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="更新时间" prop="updateTime">
        <el-date-picker placeholder="请输入更新时间" v-model="form.updateTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
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
              table_name: 'reminder_item',
              param: form.value
          });

          proxy.$modal.msgSuccess(res.message);

          if(res.code == 0){
              refresh();
          }
      }
      else{
          let res = await proxy.$api.reminder_item['update'](form.value);
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
            table_name: 'reminder_item',
            param: {
                reminderItemId: id
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
        let res = await proxy.$api.reminder_item['get'](id);
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
            table_name: 'reminder_item',
            param: {
                reminderItemId: id
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
        let res = await proxy.$api.reminder_item_all['get'](id);
        if(res.code == 0){
            formAll.value = Object.assign({}, res.data);
        }
        else{
            proxy.$modal.msgError(res.message);
        }
    }
}

function refresh(){
    if(routerQuery.reminderItemId){
        getDetail(routerQuery.reminderItemId);
    }
    if(detail.value == 'all'){
        if(routerQuery.reminderItemId){
            getDetailAll(routerQuery.reminderItemId);
        }
    }
}

refresh();

</script>

<style lang="scss" scoped>
</style>