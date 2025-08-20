<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="联系人姓名"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="emergency_contact.import"
table_name="emergency_contact"
firstSearchData="name"
>
<template #collapse>
    <el-form-item v-show="!props.params.userInfoUserInfoId1"  label="用户ID" prop="userInfoUserInfoId1">
        <base-select clearable v-model="listQuery.userInfoUserInfoId1" api="emergency_contact_all.get_user_info_list" show_name=""></base-select>
    </el-form-item>

    <el-form-item  label="联系人姓名" prop="name">
        <el-input clearable placeholder="请输入联系人姓名" v-model="listQuery.name" />
    </el-form-item>

    <el-form-item  label="联系电话" prop="phoneNumber">
        <el-input clearable placeholder="请输入联系电话" v-model="listQuery.phoneNumber" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="emergency_contact_all.page"
>
<el-table-column
prop="emergencyContactId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="name"
label="联系人姓名"
align="center"
>

</el-table-column>
<el-table-column
prop="phoneNumber"
label="联系电话"
align="center"
>

</el-table-column>
<el-table-column
prop="phoneNumberUi"
label="用户ID"
align="center"
>

</el-table-column>
<el-table-column
width="220"
fixed="right"
label="操作"
align="center"
>
<template #default="scope">
<base-info-btn
path="/emergency_contact/detail"
:query="{emergencyContactId: scope.row.emergencyContactId}"
text="详情"
>
</base-info-btn>
<base-edit-btn
@ok="handleUpdate(scope.row)"
>
</base-edit-btn>
<base-delete-btn
@ok="handleDelete(scope.row)"
></base-delete-btn>
</template>
</el-table-column>
</base-table-p>
<base-dialog
v-if="dialogVisible"
:title="dialogTitleObj[dialogStatus]"
width="50%"
style="max-width: 600px;"
v-model="dialogVisible"
>
<el-form
ref='dataFormRef'
v-if='dialogStatus !== "detail"'
:model='form'
label-position='top'
:rules='{"phoneNumber":[{"trigger":"blur","message":"联系电话不能为空","required":true}],"name":[{"trigger":"blur","message":"联系人姓名不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"用户ID不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item v-show="!props.params.userInfoUserInfoId1"  label="用户ID" prop="userInfoUserInfoId1">
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
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.emergencyContactId }}
</base-cell-item>
<base-cell-item
label="用户ID"
>
{{ form.userInfoUserInfoId1 }}
</base-cell-item>
<base-cell-item
label="联系人姓名"
>
{{ form.name }}
</base-cell-item>
<base-cell-item
label="联系电话"
>
{{ form.phoneNumber }}
</base-cell-item>
</base-cell>
<template #footer>
<el-button
@click="dialogVisible = false"
round
>取 消</el-button>
<el-button
@click="submitForm"
round
color="#5D5FEF"
type="primary"
>确 定</el-button>
</template>
</base-dialog>
</base-wrapper>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const props = defineProps({
    params: { type: Object, default: () => ({}) },
})

// 计算属性，用于判断 params 的长度
const paramsLength = computed(() =>  Object.keys(props.params).length)

// 计算属性，判断 params 是否为空
const isParamsEmpty = computed(() => paramsLength.value === 0)

let listQuery = ref({});
let form = ref({});
let dialogVisible = ref(false);
let dialogStatus = ref('');
let dialogTitleObj = ref({ update: '编辑', add: '添加', detail: '详情'});
let rules = ref({});
if(isParamsEmpty.value){
  let routerQuery = proxy.$route.query;
  Object.assign(listQuery.value, routerQuery);
}
else{
  Object.assign(listQuery.value, props.params);
}


function refreshTableData() {
  proxy.$refs.baseTableRef.refresh();
}
function handleDetail(row) {
  form.value = Object.assign({}, row);
  dialogStatus.value = 'detail';
  dialogVisible.value = true;
}
function handleAdd() {
  form.value = { ...props.params };
  dialogStatus.value = 'add';
  dialogVisible.value = true;
}
function handleUpdate(row) {
  form.value = Object.assign({}, row);
  dialogStatus.value = 'update';
  dialogVisible.value = true;
}
async function handleDelete(row) {
  if(import.meta.env.VITE_APP_MODEL === 'PREVIEW'){
    let res = await proxy.$api.table.delete({
        table_name: 'emergency_contact',
        param: {
            emergencyContactId: row.emergencyContactId
        }
    });
    proxy.$modal.msgSuccess(res.message);
  }
  else{
    let res = await proxy.$api.emergency_contact.delete(row.emergencyContactId);
    proxy.$modal.msgSuccess(res.message);
  }
  refreshTableData();
}
async function handleExport() {
  try {
    await proxy.$api.emergency_contact_all.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.emergency_contact.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "downloadTemplate", "get")
  }catch (error) {
    console.error("导出失败", error)
  }
}
function submitForm() {
  if(dialogStatus.value == 'detail'){
      dialogVisible.value = false;
  }
  else {
      proxy.$refs.dataFormRef.validate(async (valid) => {
        if (valid) {
          if(import.meta.env.VITE_APP_MODEL === 'PREVIEW'){
            let res = await proxy.$api.table[dialogStatus.value]({
                table_name: 'emergency_contact',
                param: form.value
            });
            proxy.$modal.msgSuccess(res.message);
          }
          else{
            let res = await proxy.$api.emergency_contact[dialogStatus.value](form.value);
            proxy.$modal.msgSuccess(res.message);
          }
          refreshTableData();
          dialogVisible.value = false;
        }
      });
  }
}

function choose(item){
    form.value = Object.assign({}, item);
    dialogVisible.value = true;
    dialogStatus.value = 'update';
}
</script>

<style lang="scss" scoped>
</style>