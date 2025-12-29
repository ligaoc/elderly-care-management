<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="类型名称" prop="typeName">
              <el-input v-model="queryParams.typeName" placeholder="请输入类型名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="类型编码" prop="typeCode">
              <el-input v-model="queryParams.typeCode" placeholder="请输入类型编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="设备分类" prop="category">
              <el-select v-model="queryParams.category" placeholder="设备分类" clearable>
                <el-option label="监测设备" value="monitor" />
                <el-option label="定位设备" value="location" />
                <el-option label="报警设备" value="alarm" />
                <el-option label="其他设备" value="other" />
              </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="状态" clearable>
                <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="hover">
      <template #header>
        <el-row :gutter="10">
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:deviceType:add']" type="primary" plain icon="Plus" @click="handleAdd()">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" plain icon="Sort" @click="handleToggleExpandAll">展开/折叠</el-button>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table
        ref="deviceTypeTableRef"
        v-loading="loading"
        :data="deviceTypeList"
        row-key="id"
        border
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="isExpandAll"
      >
        <el-table-column prop="typeName" label="类型名称" width="200"></el-table-column>
        <el-table-column prop="typeCode" align="center" label="类型编码" width="150"></el-table-column>
        <el-table-column prop="category" align="center" label="设备分类" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.category === 'monitor'" type="primary">监测设备</el-tag>
            <el-tag v-else-if="scope.row.category === 'location'" type="success">定位设备</el-tag>
            <el-tag v-else-if="scope.row.category === 'alarm'" type="warning">报警设备</el-tag>
            <el-tag v-else type="info">其他设备</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="icon" align="center" label="图标" width="80">
          <template #default="scope">
            <i v-if="scope.row.icon" :class="scope.row.icon" style="font-size: 18px;"></i>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" align="center" label="排序" width="80"></el-table-column>
        <el-table-column prop="status" align="center" label="状态" width="80">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ proxy.parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column fixed="right" align="center" label="操作" width="180">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['health:deviceType:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
            </el-tooltip>
            <el-tooltip content="新增" placement="top">
              <el-button v-hasPermi="['health:deviceType:add']" link type="primary" icon="Plus" @click="handleAdd(scope.row)" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['health:deviceType:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改设备类型对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" destroy-on-close append-to-body width="600px">
      <el-form ref="deviceTypeFormRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col v-if="form.parentId !== 0" :span="24">
            <el-form-item label="上级类型" prop="parentId">
              <el-tree-select
                v-model="form.parentId"
                :data="deviceTypeOptions"
                :props="{ value: 'id', label: 'typeName', children: 'children' } as any"
                value-key="id"
                placeholder="选择上级类型"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型名称" prop="typeName">
              <el-input v-model="form.typeName" placeholder="请输入类型名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型编码" prop="typeCode">
              <el-input v-model="form.typeCode" placeholder="请输入类型编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备分类" prop="category">
              <el-select v-model="form.category" placeholder="请选择设备分类">
                <el-option label="监测设备" value="monitor" />
                <el-option label="定位设备" value="location" />
                <el-option label="报警设备" value="alarm" />
                <el-option label="其他设备" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sortOrder">
              <el-input-number v-model="form.sortOrder" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="图标" prop="icon">
              <el-input v-model="form.icon" placeholder="请输入图标类名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="HealthDeviceType" lang="ts">
import { 
  listDeviceType, 
  getDeviceType, 
  delDeviceType, 
  addDeviceType, 
  updateDeviceType,
  treeDeviceType 
} from '@/api/health/deviceType';
import { 
  HealthDeviceTypeForm, 
  HealthDeviceTypeQuery, 
  HealthDeviceTypeVO,
  HealthDeviceTypeTreeVO 
} from '@/api/health/deviceType/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_normal_disable'));

const deviceTypeList = ref<HealthDeviceTypeVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const deviceTypeOptions = ref<HealthDeviceTypeTreeVO[]>([]);
const isExpandAll = ref(true);

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const deviceTypeTableRef = ref<ElTableInstance>();
const queryFormRef = ref<ElFormInstance>();
const deviceTypeFormRef = ref<ElFormInstance>();

const initFormData: HealthDeviceTypeForm = {
  id: undefined,
  parentId: 0,
  typeName: '',
  typeCode: '',
  category: '',
  icon: '',
  sortOrder: 0,
  status: '0',
  remark: ''
};

const initData: PageData<HealthDeviceTypeForm, HealthDeviceTypeQuery> = {
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    typeName: undefined,
    typeCode: undefined,
    category: undefined,
    status: undefined
  },
  rules: {
    typeName: [{ required: true, message: '类型名称不能为空', trigger: 'blur' }],
    typeCode: [{ required: true, message: '类型编码不能为空', trigger: 'blur' }],
    category: [{ required: true, message: '设备分类不能为空', trigger: 'change' }],
    sortOrder: [{ required: true, message: '显示排序不能为空', trigger: 'blur' }]
  }
};

const data = reactive<PageData<HealthDeviceTypeForm, HealthDeviceTypeQuery>>(initData);
const { queryParams, form, rules } = toRefs<PageData<HealthDeviceTypeForm, HealthDeviceTypeQuery>>(data);

/** 查询设备类型列表 */
const getList = async () => {
  loading.value = true;
  const res = await listDeviceType(queryParams.value);
  const data = proxy?.handleTree<HealthDeviceTypeVO>(res.data, 'id');
  if (data) {
    deviceTypeList.value = data;
  }
  loading.value = false;
};

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
};

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  deviceTypeFormRef.value?.resetFields();
};

/** 搜索按钮操作 */
const handleQuery = () => {
  getList();
};

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
};

/** 展开/折叠操作 */
const handleToggleExpandAll = () => {
  isExpandAll.value = !isExpandAll.value;
  toggleExpandAll(deviceTypeList.value, isExpandAll.value);
};

/** 展开/折叠所有 */
const toggleExpandAll = (data: HealthDeviceTypeVO[], status: boolean) => {
  data.forEach((item) => {
    deviceTypeTableRef.value?.toggleRowExpansion(item, status);
    if (item.children && item.children.length > 0) toggleExpandAll(item.children, status);
  });
};

/** 新增按钮操作 */
const handleAdd = async (row?: HealthDeviceTypeVO) => {
  reset();
  const res = await treeDeviceType();
  deviceTypeOptions.value = res.data;
  if (row && row.id) {
    form.value.parentId = row.id;
  }
  dialog.visible = true;
  dialog.title = '添加设备类型';
};

/** 修改按钮操作 */
const handleUpdate = async (row: HealthDeviceTypeVO) => {
  reset();
  const res = await getDeviceType(row.id);
  form.value = res.data;
  const treeRes = await treeDeviceType();
  deviceTypeOptions.value = treeRes.data.filter(item => item.id !== row.id);
  dialog.visible = true;
  dialog.title = '修改设备类型';
};

/** 提交按钮 */
const submitForm = () => {
  deviceTypeFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      form.value.id ? await updateDeviceType(form.value) : await addDeviceType(form.value);
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 删除按钮操作 */
const handleDelete = async (row: HealthDeviceTypeVO) => {
  await proxy?.$modal.confirm('是否确认删除名称为"' + row.typeName + '"的数据项?');
  await delDeviceType(row.id);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};

onMounted(() => {
  getList();
});
</script>