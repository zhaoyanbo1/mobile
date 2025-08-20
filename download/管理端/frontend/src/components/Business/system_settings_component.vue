<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="system_settings.import"
table_name="system_settings"
firstSearchData
>
<template #collapse>
    <el-form-item v-show="!props.params.userInfoUserInfoId1"  label="用户ID" prop="userInfoUserInfoId1">
        <base-select clearable v-model="listQuery.userInfoUserInfoId1" api="system_settings_all.get_user_info_list" show_name=""></base-select>
    </el-form-item>

    <el-form-item  label="提醒音量" prop="reminderVolume">
        <el-input clearable placeholder="请输入提醒音量" v-model="listQuery.reminderVolume" />
    </el-form-item>

    <el-form-item  label="字体大小" prop="fontSize">
        <el-input clearable placeholder="请输入字体大小" v-model="listQuery.fontSize" />
    </el-form-item>

    <el-form-item  label="问卷数据是否导出" prop="questionnaireExported">
        <el-select v-model="listQuery.questionnaireExported" placeholder="请选择">
            <el-option label="是" :value="true"></el-option>
            <el-option label="否" :value="false"></el-option>
        </el-select>
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="system_settings_all.page"
>
<el-table-column
prop="systemSettingsId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="reminderVolume"
label="提醒音量"
align="center"
>

</el-table-column>
<el-table-column
prop="fontSize"
label="字体大小"
align="center"
>

</el-table-column>
<el-table-column
prop="questionnaireExported"
label="问卷数据是否导出"
align="center"
>
<template #default="scope">
{{  scope.row.questionnaireExported? '是':'否' }}
</template>
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
path="/system_settings/detail"
:query="{systemSettingsId: scope.row.systemSettingsId}"
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
:rules='{"userInfoUserInfoId1":[{"trigger":"blur","message":"用户ID不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item v-show="!props.params.userInfoUserInfoId1"  label="用户ID" prop="userInfoUserInfoId1">
        <base-select clearable v-model="form.userInfoUserInfoId1" api="system_settings_all.get_user_info_list" show_name=""></base-select>
    </el-form-item>

    <el-form-item  label="提醒音量" prop="reminderVolume">
        <el-input clearable placeholder="请输入提醒音量" v-model="form.reminderVolume" />
    </el-form-item>

    <el-form-item  label="字体大小" prop="fontSize">
        <el-input clearable placeholder="请输入字体大小" v-model="form.fontSize" />
    </el-form-item>

    <el-form-item  label="问卷数据是否导出" prop="questionnaireExported">
        <el-select v-model="form.questionnaireExported" placeholder="请选择">
            <el-option label="是" :value="true"></el-option>
            <el-option label="否" :value="false"></el-option>
        </el-select>
    </el-form-item>

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.systemSettingsId }}
</base-cell-item>
<base-cell-item
label="用户ID"
>
{{ form.userInfoUserInfoId1 }}
</base-cell-item>
<base-cell-item
label="提醒音量"
>
{{ form.reminderVolume }}
</base-cell-item>
<base-cell-item
label="字体大小"
>
{{ form.fontSize }}
</base-cell-item>
<base-cell-item
label="问卷数据是否导出"
>
{{ form.questionnaireExported }}
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
        table_name: 'system_settings',
        param: {
            systemSettingsId: row.systemSettingsId
        }
    });
    proxy.$modal.msgSuccess(res.message);
  }
  else{
    let res = await proxy.$api.system_settings.delete(row.systemSettingsId);
    proxy.$modal.msgSuccess(res.message);
  }
  refreshTableData();
}
async function handleExport() {
  try {
    await proxy.$api.system_settings_all.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.system_settings.downloadExcel({
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
                table_name: 'system_settings',
                param: form.value
            });
            proxy.$modal.msgSuccess(res.message);
          }
          else{
            let res = await proxy.$api.system_settings[dialogStatus.value](form.value);
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