<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="姓名"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="health_questionnaire.import"
table_name="health_questionnaire"
firstSearchData="name"
>
<template #collapse>
    <el-form-item v-show="!props.params.userInfoUserInfoId1"  label="用户ID" prop="userInfoUserInfoId1">
        <base-select clearable v-model="listQuery.userInfoUserInfoId1" api="health_questionnaire_all.get_user_info_list" show_name=""></base-select>
    </el-form-item>

    <el-form-item  label="姓名" prop="name">
        <el-input clearable placeholder="请输入姓名" v-model="listQuery.name" />
    </el-form-item>

    <el-form-item  label="年龄" prop="age">
        <el-input clearable placeholder="请输入年龄" v-model="listQuery.age" />
    </el-form-item>

    <el-form-item  label="居住情况" prop="residence">
        <el-input clearable placeholder="请输入居住情况" v-model="listQuery.residence" />
    </el-form-item>

    <el-form-item  label="慢性病" prop="chronicDisease">
        <el-input clearable placeholder="请输入慢性病" v-model="listQuery.chronicDisease" />
    </el-form-item>

    <el-form-item  label="过敏史" prop="allergyHistory">
        <el-input clearable placeholder="请输入过敏史" v-model="listQuery.allergyHistory" />
    </el-form-item>

    <el-form-item  label="常用药" prop="commonMedication">
        <el-input clearable placeholder="请输入常用药" v-model="listQuery.commonMedication" />
    </el-form-item>

    <el-form-item  label="饮食偏好" prop="dietPreference">
        <el-input clearable placeholder="请输入饮食偏好" v-model="listQuery.dietPreference" />
    </el-form-item>

    <el-form-item  label="运动频率" prop="exerciseFrequency">
        <el-input clearable placeholder="请输入运动频率" v-model="listQuery.exerciseFrequency" />
    </el-form-item>

    <el-form-item   label="创建时间" prop="creationTime">
        <el-date-picker placeholder="请输入创建时间" v-model="listQuery.creationTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="更新时间" prop="updateTime">
        <el-date-picker placeholder="请输入更新时间" v-model="listQuery.updateTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="版本号" prop="version">
        <el-input clearable placeholder="请输入版本号" v-model="listQuery.version" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="health_questionnaire_all.page"
>
<el-table-column
prop="healthQuestionnaireId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="name"
label="姓名"
align="center"
>

</el-table-column>
<el-table-column
prop="age"
label="年龄"
align="center"
>

</el-table-column>
<el-table-column
prop="residence"
label="居住情况"
align="center"
>

</el-table-column>
<el-table-column
prop="chronicDisease"
label="慢性病"
align="center"
>

</el-table-column>
<el-table-column
prop="allergyHistory"
label="过敏史"
align="center"
>

</el-table-column>
<el-table-column
prop="commonMedication"
label="常用药"
align="center"
>

</el-table-column>
<el-table-column
prop="dietPreference"
label="饮食偏好"
align="center"
>

</el-table-column>
<el-table-column
prop="exerciseFrequency"
label="运动频率"
align="center"
>

</el-table-column>
<el-table-column
prop="creationTime"
label="创建时间"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.creationTime,'{y}-{m}-{d} {h}:{i}:{s}') }}
</template>
</el-table-column>
<el-table-column
prop="updateTime"
label="更新时间"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.updateTime,'{y}-{m}-{d} {h}:{i}:{s}') }}
</template>
</el-table-column>
<el-table-column
prop="version"
label="版本号"
align="center"
>

</el-table-column>
<el-table-column
prop="phoneNumber"
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
path="/health_questionnaire/detail"
:query="{healthQuestionnaireId: scope.row.healthQuestionnaireId}"
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
:rules='{"name":[{"trigger":"blur","message":"姓名不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"用户ID不能为空","required":true}],"age":[{"trigger":"blur","message":"年龄不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item v-show="!props.params.userInfoUserInfoId1"  label="用户ID" prop="userInfoUserInfoId1">
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

    <el-form-item  label="版本号" prop="version">
        <el-input clearable placeholder="请输入版本号" v-model="form.version" />
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.healthQuestionnaireId }}
</base-cell-item>
<base-cell-item
label="用户ID"
>
{{ form.userInfoUserInfoId1 }}
</base-cell-item>
<base-cell-item
label="姓名"
>
{{ form.name }}
</base-cell-item>
<base-cell-item
label="年龄"
>
{{ form.age }}
</base-cell-item>
<base-cell-item
label="居住情况"
>
{{ form.residence }}
</base-cell-item>
<base-cell-item
label="慢性病"
>
{{ form.chronicDisease }}
</base-cell-item>
<base-cell-item
label="过敏史"
>
{{ form.allergyHistory }}
</base-cell-item>
<base-cell-item
label="常用药"
>
{{ form.commonMedication }}
</base-cell-item>
<base-cell-item
label="饮食偏好"
>
{{ form.dietPreference }}
</base-cell-item>
<base-cell-item
label="运动频率"
>
{{ form.exerciseFrequency }}
</base-cell-item>
<base-cell-item
label="创建时间"
>
{{ form.creationTime }}
</base-cell-item>
<base-cell-item
label="版本号"
>
{{ form.version }}
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
        table_name: 'health_questionnaire',
        param: {
            healthQuestionnaireId: row.healthQuestionnaireId
        }
    });
    proxy.$modal.msgSuccess(res.message);
  }
  else{
    let res = await proxy.$api.health_questionnaire.delete(row.healthQuestionnaireId);
    proxy.$modal.msgSuccess(res.message);
  }
  refreshTableData();
}
async function handleExport() {
  try {
    await proxy.$api.health_questionnaire_all.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.health_questionnaire.downloadExcel({
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
                table_name: 'health_questionnaire',
                param: form.value
            });
            proxy.$modal.msgSuccess(res.message);
          }
          else{
            let res = await proxy.$api.health_questionnaire[dialogStatus.value](form.value);
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