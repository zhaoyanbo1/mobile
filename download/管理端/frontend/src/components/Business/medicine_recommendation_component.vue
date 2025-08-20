<template>
    <base-wrapper
>
<base-header>
<search-operation
@downloadEvent="downloadTemplate"
@exportEvent="handleExport"
firstSearchComment="药品名称"
:searchData="listQuery"
@refreshTableData="refreshTableData"
@addEvent="handleAdd"
uploadExcelAPI="medicine_recommendation.import"
table_name="medicine_recommendation"
firstSearchData="title"
>
<template #collapse>
    <el-form-item  label="药品名称" prop="title">
        <el-input clearable placeholder="请输入药品名称" v-model="listQuery.title" />
    </el-form-item>

    <el-form-item  label="使用指南" prop="usageGuide">
        <el-input clearable placeholder="请输入使用指南" v-model="listQuery.usageGuide" />
    </el-form-item>

    <el-form-item  label="附近药店信息" prop="nearbyPharmacyInfo">
        <el-input clearable placeholder="请输入附近药店信息" v-model="listQuery.nearbyPharmacyInfo" />
    </el-form-item>

    <el-form-item   label="创建时间" prop="creationTime">
        <el-date-picker placeholder="请输入创建时间" v-model="listQuery.creationTime" type="datetime"
         format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </el-form-item>

</template>
</search-operation>
</base-header>
<base-table-p
ref="baseTableRef"
:params="listQuery"
api="medicine_recommendation.page"
>
<el-table-column
prop="medicineRecommendationId"
label="主键"
align="center"
>

</el-table-column>
<el-table-column
prop="title"
label="药品名称"
align="center"
>

</el-table-column>
<el-table-column
prop="usageGuide"
label="使用指南"
align="center"
>

</el-table-column>
<el-table-column
prop="nearbyPharmacyInfo"
label="附近药店信息"
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
width="220"
fixed="right"
label="操作"
align="center"
>
<template #default="scope">
<base-info-btn
path="/medicine_recommendation/detail"
:query="{medicineRecommendationId: scope.row.medicineRecommendationId}"
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
:rules='{"title":[{"trigger":"blur","message":"药品名称不能为空","required":true}]}'
label-width='100px'
>
    <el-form-item  label="药品名称" prop="title">
        <el-input clearable placeholder="请输入药品名称" v-model="form.title" />
    </el-form-item>

    <el-form-item  label="使用指南" prop="usageGuide">
        <el-input clearable placeholder="请输入使用指南" v-model="form.usageGuide" />
    </el-form-item>

    <el-form-item  label="附近药店信息" prop="nearbyPharmacyInfo">
        <el-input clearable placeholder="请输入附近药店信息" v-model="form.nearbyPharmacyInfo" />
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
{{ form.medicineRecommendationId }}
</base-cell-item>
<base-cell-item
label="药品名称"
>
{{ form.title }}
</base-cell-item>
<base-cell-item
label="使用指南"
>
{{ form.usageGuide }}
</base-cell-item>
<base-cell-item
label="附近药店信息"
>
{{ form.nearbyPharmacyInfo }}
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
        table_name: 'medicine_recommendation',
        param: {
            medicineRecommendationId: row.medicineRecommendationId
        }
    });
    proxy.$modal.msgSuccess(res.message);
  }
  else{
    let res = await proxy.$api.medicine_recommendation.delete(row.medicineRecommendationId);
    proxy.$modal.msgSuccess(res.message);
  }
  refreshTableData();
}
async function handleExport() {
  try {
    await proxy.$api.medicine_recommendation.downloadExcel({
      ...listQuery.value
    }, `${new Date().getTime()}.xlsx`, "export", "post")
  }catch (error) {
    console.error("导出失败", error)
  }
}
async function downloadTemplate() {
  try {
    await proxy.$api.medicine_recommendation.downloadExcel({
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
                table_name: 'medicine_recommendation',
                param: form.value
            });
            proxy.$modal.msgSuccess(res.message);
          }
          else{
            let res = await proxy.$api.medicine_recommendation[dialogStatus.value](form.value);
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