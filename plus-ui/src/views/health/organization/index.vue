<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="组织名称" prop="orgName">
              <el-input v-model="queryParams.orgName" placeholder="请输入组织名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="组织编码" prop="orgCode">
              <el-input v-model="queryParams.orgCode" placeholder="请输入组织编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="组织类型" prop="orgType">
              <el-select v-model="queryParams.orgType" placeholder="组织类型" clearable>
                <el-option label="养老院" value="nursing_home" />
                <el-option label="社区服务中心" value="community_center" />
                <el-option label="医疗机构" value="medical_institution" />
                <el-option label="家庭服务" value="home_service" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="queryParams.leader" placeholder="请输入负责人" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="queryParams.phone" placeholder="请输入联系电话" clearable @keyup.enter="handleQuery" />
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
            <el-button v-hasPermi="['health:organization:add']" type="primary" plain icon="Plus" @click="handleAdd()">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" plain icon="Sort" @click="handleToggleExpandAll">展开/折叠</el-button>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table
        ref="organizationTableRef"
        v-loading="loading"
        :data="organizationList"
        row-key="id"
        border
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="isExpandAll"
      >
        <el-table-column prop="orgName" label="组织名称" width="200"></el-table-column>
        <el-table-column prop="orgCode" align="center" label="组织编码" width="150"></el-table-column>
        <el-table-column prop="orgType" align="center" label="组织类型" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.orgType === 'nursing_home'" type="primary">养老院</el-tag>
            <el-tag v-else-if="scope.row.orgType === 'community_center'" type="success">社区服务中心</el-tag>
            <el-tag v-else-if="scope.row.orgType === 'medical_institution'" type="warning">医疗机构</el-tag>
            <el-tag v-else-if="scope.row.orgType === 'home_service'" type="info">家庭服务</el-tag>
            <el-tag v-else>其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="leader" align="center" label="负责人" width="100"></el-table-column>
        <el-table-column prop="phone" align="center" label="联系电话" width="120"></el-table-column>
        <el-table-column prop="email" align="center" label="邮箱" width="150" :show-overflow-tooltip="true"></el-table-column>
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
        <el-table-column prop="address" label="地址" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column fixed="right" align="center" label="操作" width="180">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['health:organization:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
            </el-tooltip>
            <el-tooltip content="新增" placement="top">
              <el-button v-hasPermi="['health:organization:add']" link type="primary" icon="Plus" @click="handleAdd(scope.row)" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['health:organization:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改组织对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" destroy-on-close append-to-body width="600px">
      <el-form ref="organizationFormRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col v-if="form.parentId !== 0" :span="24">
            <el-form-item label="上级组织" prop="parentId">
              <el-tree-select
                v-model="form.parentId"
                :data="organizationOptions"
                :props="{ value: 'id', label: 'orgName', children: 'children' } as any"
                value-key="id"
                placeholder="选择上级组织"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组织名称" prop="orgName">
              <el-input v-model="form.orgName" placeholder="请输入组织名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组织编码" prop="orgCode">
              <el-input v-model="form.orgCode" placeholder="请输入组织编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组织类型" prop="orgType">
              <el-select v-model="form.orgType" placeholder="请选择组织类型">
                <el-option label="养老院" value="nursing_home" />
                <el-option label="社区服务中心" value="community_center" />
                <el-option label="医疗机构" value="medical_institution" />
                <el-option label="家庭服务" value="home_service" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sortOrder">
              <el-input-number v-model="form.sortOrder" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入地址" />
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

<script setup name="HealthOrganization" lang="ts">
import { 
  listOrganization, 
  getOrganization, 
  delOrganization, 
  addOrganization, 
  updateOrganization,
  treeOrganization 
} from '@/api/health/organization';
import { 
  HealthOrganizationForm, 
  HealthOrganizationQuery, 
  HealthOrganizationVO,
  HealthOrganizationTreeVO 
} from '@/api/health/organization/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_normal_disable'));

const organizationList = ref<HealthOrganizationVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const organizationOptions = ref<HealthOrganizationTreeVO[]>([]);
const isExpandAll = ref(true);

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const organizationTableRef = ref<ElTableInstance>();
const queryFormRef = ref<ElFormInstance>();
const organizationFormRef = ref<ElFormInstance>();

const initFormData: HealthOrganizationForm = {
  id: undefined,
  parentId: 0,
  orgCode: '',
  orgName: '',
  orgType: '',
  leader: '',
  phone: '',
  email: '',
  address: '',
  sortOrder: 0,
  status: '0',
  remark: ''
};

const initData: PageData<HealthOrganizationForm, HealthOrganizationQuery> = {
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orgName: undefined,
    orgCode: undefined,
    orgType: undefined,
    leader: undefined,
    phone: undefined,
    status: undefined
  },
  rules: {
    orgName: [{ required: true, message: '组织名称不能为空', trigger: 'blur' }],
    orgCode: [{ required: true, message: '组织编码不能为空', trigger: 'blur' }],
    orgType: [{ required: true, message: '组织类型不能为空', trigger: 'change' }],
    sortOrder: [{ required: true, message: '显示排序不能为空', trigger: 'blur' }],
    email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
    phone: [{ pattern: /^1[3456789][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }]
  }
};

const data = reactive<PageData<HealthOrganizationForm, HealthOrganizationQuery>>(initData);
const { queryParams, form, rules } = toRefs<PageData<HealthOrganizationForm, HealthOrganizationQuery>>(data);

/** 查询组织列表 */
const getList = async () => {
  loading.value = true;
  const res = await listOrganization(queryParams.value);
  const data = proxy?.handleTree<HealthOrganizationVO>(res.data, 'id');
  if (data) {
    organizationList.value = data;
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
  organizationFormRef.value?.resetFields();
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
  toggleExpandAll(organizationList.value, isExpandAll.value);
};

/** 展开/折叠所有 */
const toggleExpandAll = (data: HealthOrganizationVO[], status: boolean) => {
  data.forEach((item) => {
    organizationTableRef.value?.toggleRowExpansion(item, status);
    if (item.children && item.children.length > 0) toggleExpandAll(item.children, status);
  });
};

/** 新增按钮操作 */
const handleAdd = async (row?: HealthOrganizationVO) => {
  reset();
  const res = await treeOrganization();
  organizationOptions.value = res.data;
  if (row && row.id) {
    form.value.parentId = row.id;
  }
  dialog.visible = true;
  dialog.title = '添加组织';
};

/** 修改按钮操作 */
const handleUpdate = async (row: HealthOrganizationVO) => {
  reset();
  const res = await getOrganization(row.id);
  form.value = res.data;
  const treeRes = await treeOrganization();
  organizationOptions.value = treeRes.data.filter(item => item.id !== row.id);
  dialog.visible = true;
  dialog.title = '修改组织';
};

/** 提交按钮 */
const submitForm = () => {
  organizationFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      form.value.id ? await updateOrganization(form.value) : await addOrganization(form.value);
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 删除按钮操作 */
const handleDelete = async (row: HealthOrganizationVO) => {
  await proxy?.$modal.confirm('是否确认删除名称为"' + row.orgName + '"的数据项?');
  await delOrganization(row.id);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};

onMounted(() => {
  getList();
});
</script>