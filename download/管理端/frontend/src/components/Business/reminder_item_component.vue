<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="提醒标题"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="reminder_item.import"
table_name="reminder_item"
firstSearchData="title"
>
<template #collapse>
    <el-form-item v-show="!props.params.userInfoUserInfoId1"  label="用户ID" prop="userInfoUserInfoId1">
        <base-select clearable v-model="listQuery.userInfoUserInfoId1" api="reminder_item_all.get_user_info_list" show_name=""></base-select>
    </el-form-item>

    <el-form-item  label="提醒类型枚举ID" prop="reminderTypeEnumId">
        <el-input clearable placeholder="请输入提醒类型枚举ID" v-model="listQuery.reminderTypeEnumId" />
    </el-form-item>

    <el-form-item  label="提醒标题" prop="title">
        <el-input clearable placeholder="请输入提醒标题" v-model="listQuery.title" />
    </el-form-item>

    <el-form-item  label="提醒内容" prop="description">
        <el-input clearable placeholder="请输入提醒内容" v-model="listQuery.description" />
    </el-form-item>

    <el-form-item   label="提醒时间" prop="reminderTime">
        <el-date-picker placeholder="请输入提醒时间" v-model="listQuery.reminderTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item  label="是否完成" prop="isCompleted">
        <el-select v-model="listQuery.isCompleted" placeholder="请选择">
            <el-option label="是" :value="true"></el-option>
            <el-option label="否" :value="false"></el-option>
        </el-select>
    </el-form-item>

    <el-form-item  label="用药量" prop="medicineDosage">
        <el-input clearable placeholder="请输入用药量" v-model="listQuery.medicineDosage" />
    </el-form-item>

    <el-form-item  label="地点纬度" prop="locationLatitude">
        <el-input clearable placeholder="请输入地点纬度" v-model="listQuery.locationLatitude" />
    </el-form-item>

    <el-form-item  label="地点经度" prop="locationLongitude">
        <el-input clearable placeholder="请输入地点经度" v-model="listQuery.locationLongitude" />
    </el-form-item>

    <el-form-item  label="地点详细地址" prop="locationAddress">
        <el-input clearable placeholder="请输入地点详细地址" v-model="listQuery.locationAddress" />
    </el-form-item>

    <el-form-item  label="关联食谱ID" prop="dietRecipeId">
        <el-input clearable placeholder="请输入关联食谱ID" v-model="listQuery.dietRecipeId" />
    </el-form-item>

    <el-form-item   label="创建时间" prop="creationTime">
        <el-date-picker placeholder="请输入创建时间" v-model="listQuery.creationTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

    <el-form-item   label="更新时间" prop="updateTime">
        <el-date-picker placeholder="请输入更新时间" v-model="listQuery.updateTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="reminder_item_all.page"
>
<el-table-column
prop="reminderItemId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="reminderTypeEnumId"
label="提醒类型枚举ID"
align="center"
>

</el-table-column>
<el-table-column
prop="title"
label="提醒标题"
align="center"
>

</el-table-column>
<el-table-column
prop="description"
label="提醒内容"
align="center"
>

</el-table-column>
<el-table-column
prop="reminderTime"
label="提醒时间"
align="center"
>
<template #default="scope">
{{ parseTime(scope.row.reminderTime,'{y}-{m}-{d} {h}:{i}:{s}') }}
</template>
</el-table-column>
<el-table-column
prop="isCompleted"
label="是否完成"
align="center"
>
<template #default="scope">
{{  scope.row.isCompleted? '是':'否' }}
</template>
</el-table-column>
<el-table-column
prop="medicinePhotoResourceKey"
label="药品照片"
align="center"
>
<template #default="scope">
<image-preview
:height="50"
:src="scope.row.medicinePhoto.map(item => item.url).join(',')"
:width="50"
/>
</template>
</el-table-column>
<el-table-column
prop="medicineDosage"
label="用药量"
align="center"
>

</el-table-column>
<el-table-column
prop="locationLatitude"
label="地点纬度"
align="center"
>

</el-table-column>
<el-table-column
prop="locationLongitude"
label="地点经度"
align="center"
>

</el-table-column>
<el-table-column
prop="locationAddress"
label="地点详细地址"
align="center"
>

</el-table-column>
<el-table-column
prop="dietRecipeId"
label="关联食谱ID"
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
path="/reminder_item/detail"
:query="{reminderItemId: scope.row.reminderItemId}"
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
:rules='{"reminderTypeEnumId":[{"trigger":"blur","message":"提醒类型枚举ID不能为空","required":true}],"reminderTime":[{"trigger":"blur","message":"提醒时间不能为空","required":true}],"title":[{"trigger":"blur","message":"提醒标题不能为空","required":true}],"userInfoUserInfoId1":[{"trigger":"blur","message":"用户ID不能为空","required":true}],"isCompleted":[{"trigger":"blur","message":"是否完成不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item v-show="!props.params.userInfoUserInfoId1"  label="用户ID" prop="userInfoUserInfoId1">
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

</el-form>
<base-cell
v-else
label-width="100px"
>
<base-cell-item
label="主键"
>
{{ form.reminderItemId }}
</base-cell-item>
<base-cell-item
label="用户ID"
>
{{ form.userInfoUserInfoId1 }}
</base-cell-item>
<base-cell-item
label="提醒类型枚举ID"
>
{{ form.reminderTypeEnumId }}
</base-cell-item>
<base-cell-item
label="提醒标题"
>
{{ form.title }}
</base-cell-item>
<base-cell-item
label="提醒内容"
>
{{ form.description }}
</base-cell-item>
<base-cell-item
label="提醒时间"
>
{{ form.reminderTime }}
</base-cell-item>
<base-cell-item
label="是否完成"
>
{{ form.isCompleted }}
</base-cell-item>
<base-cell-item
label="药品照片"
>
{{ form.medicinePhotoResourceKey }}
</base-cell-item>
<base-cell-item
label="用药量"
>
{{ form.medicineDosage }}
</base-cell-item>
<base-cell-item
label="地点纬度"
>
{{ form.locationLatitude }}
</base-cell-item>
<base-cell-item
label="地点经度"
>
{{ form.locationLongitude }}
</base-cell-item>
<base-cell-item
label="地点详细地址"
>
{{ form.locationAddress }}
</base-cell-item>
<base-cell-item
label="关联食谱ID"
>
{{ form.dietRecipeId }}
</base-cell-item>
<base-cell-item
label="创建时间"
>
{{ form.creationTime }}
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
        table_name: 'reminder_item',
        param: {
            reminderItemId: row.reminderItemId
        }
    });
    proxy.$modal.msgSuccess(res.message);
  }
  else{
    let res = await proxy.$api.reminder_item.delete(row.reminderItemId);
    proxy.$modal.msgSuccess(res.message);
  }
  refreshTableData();
}
async function handleExport() {
  try {
    await proxy.$api.reminder_item_all.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.reminder_item.downloadExcel({
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
                table_name: 'reminder_item',
                param: form.value
            });
            proxy.$modal.msgSuccess(res.message);
          }
          else{
            let res = await proxy.$api.reminder_item[dialogStatus.value](form.value);
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