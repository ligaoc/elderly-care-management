<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="人员编码" prop="staffCode">
              <el-input v-model="queryParams.staffCode" placeholder="请输入人员编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-select v-model="queryParams.gender" placeholder="性别" clearable>
                <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="人员类型" prop="staffType">
              <el-select v-model="queryParams.staffType" placeholder="人员类型" clearable>
                <el-option label="护理员" value="nurse" />
                <el-option label="医生" value="doctor" />
                <el-option label="康复师" value="therapist" />
                <el-option label="营养师" value="nutritionist" />
                <el-option label="心理咨询师" value="psychologist" />
                <el-option label="社工" value="social_worker" />
                <el-option label="管理员" value="administrator" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
            <el-form-item label="所属组织" prop="orgId">
              <el-tree-select
                v-model="queryParams.orgId"
                :data="organizationOptions"
                :props="{ value: 'id', label: 'orgName', children: 'children' } as any"
                value-key="id"
                placeholder="选择所属组织"
                check-strictly
                clearable
              />
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
            <el-button v-hasPermi="['health:serviceStaff:add']" type="primary" plain icon="Plus" @click="handleAdd()">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:serviceStaff:edit']" type="success" plain :disabled="single" icon="Edit" @click="handleUpdate()">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:serviceStaff:remove']" type="danger" plain :disabled="multiple" icon="Delete" @click="handleDelete()">删除</el-button>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" :columns="columns" @query-table="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="serviceStaffList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column v-if="columns[0].visible" key="staffCode" label="人员编码" align="center" prop="staffCode" width="120" />
        <el-table-column v-if="columns[1].visible" key="name" label="姓名" align="center" prop="name" width="100" />
        <el-table-column v-if="columns[2].visible" key="gender" label="性别" align="center" width="80">
          <template #default="scope">
            <dict-tag :options="sys_user_sex" :value="scope.row.gender" />
          </template>
        </el-table-column>
        <el-table-column v-if="columns[3].visible" key="phone" label="手机号" align="center" prop="phone" width="120" />
        <el-table-column v-if="columns[4].visible" key="staffType" label="人员类型" align="center" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.staffType === 'nurse'" type="primary">护理员</el-tag>
            <el-tag v-else-if="scope.row.staffType === 'doctor'" type="success">医生</el-tag>
            <el-tag v-else-if="scope.row.staffType === 'therapist'" type="warning">康复师</el-tag>
            <el-tag v-else-if="scope.row.staffType === 'nutritionist'" type="info">营养师</el-tag>
            <el-tag v-else-if="scope.row.staffType === 'psychologist'" type="danger">心理咨询师</el-tag>
            <el-tag v-else-if="scope.row.staffType === 'social_worker'" type="primary">社工</el-tag>
            <el-tag v-else-if="scope.row.staffType === 'administrator'" type="success">管理员</el-tag>
            <el-tag v-else>其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[5].visible" key="orgName" label="所属组织" align="center" prop="orgName" width="150" :show-overflow-tooltip="true" />
        <el-table-column v-if="columns[6].visible" key="workTime" label="工作时间" align="center" prop="workTime" width="120" />
        <el-table-column v-if="columns[7].visible" key="specialties" label="专业特长" align="center" prop="specialties" :show-overflow-tooltip="true" />
        <el-table-column v-if="columns[8].visible" key="status" label="状态" align="center" width="80">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column v-if="columns[9].visible" label="创建时间" align="center" prop="createTime" width="160">
          <template #default="scope">
            <span>{{ proxy.parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="150" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="详情" placement="top">
              <el-button link type="primary" icon="View" @click="handleDetail(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['health:serviceStaff:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['health:serviceStaff:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        :total="total"
        @pagination="getList"
      />
    </el-card>

    <!-- 添加或修改服务人员对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" destroy-on-close append-to-body width="600px">
      <el-form ref="serviceStaffFormRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择性别">
                <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="人员类型" prop="staffType">
              <el-select v-model="form.staffType" placeholder="请选择人员类型">
                <el-option label="护理员" value="nurse" />
                <el-option label="医生" value="doctor" />
                <el-option label="康复师" value="therapist" />
                <el-option label="营养师" value="nutritionist" />
                <el-option label="心理咨询师" value="psychologist" />
                <el-option label="社工" value="social_worker" />
                <el-option label="管理员" value="administrator" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="所属组织" prop="orgId">
              <el-tree-select
                v-model="form.orgId"
                :data="organizationOptions"
                :props="{ value: 'id', label: 'orgName', children: 'children' } as any"
                value-key="id"
                placeholder="选择所属组织"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="工作时间" prop="workTime">
              <el-input v-model="form.workTime" placeholder="请输入工作时间，如：周一至周五 9:00-17:00" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="专业特长" prop="specialties">
              <el-input v-model="form.specialties" type="textarea" :rows="3" placeholder="请输入专业特长，多个特长用逗号分隔" />
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

    <!-- 服务人员详情对话框 -->
    <el-dialog v-model="detailDialog.visible" title="服务人员详情" destroy-on-close append-to-body width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="人员编码">{{ currentServiceStaff.staffCode }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ currentServiceStaff.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <dict-tag :options="sys_user_sex" :value="currentServiceStaff.gender" />
        </el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentServiceStaff.phone }}</el-descriptions-item>
        <el-descriptions-item label="人员类型">
          <el-tag v-if="currentServiceStaff.staffType === 'nurse'" type="primary">护理员</el-tag>
          <el-tag v-else-if="currentServiceStaff.staffType === 'doctor'" type="success">医生</el-tag>
          <el-tag v-else-if="currentServiceStaff.staffType === 'therapist'" type="warning">康复师</el-tag>
          <el-tag v-else-if="currentServiceStaff.staffType === 'nutritionist'" type="info">营养师</el-tag>
          <el-tag v-else-if="currentServiceStaff.staffType === 'psychologist'" type="danger">心理咨询师</el-tag>
          <el-tag v-else-if="currentServiceStaff.staffType === 'social_worker'" type="primary">社工</el-tag>
          <el-tag v-else-if="currentServiceStaff.staffType === 'administrator'" type="success">管理员</el-tag>
          <el-tag v-else>其他</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="所属组织">{{ currentServiceStaff.orgName }}</el-descriptions-item>
        <el-descriptions-item label="工作时间">{{ currentServiceStaff.workTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="sys_normal_disable" :value="currentServiceStaff.status" />
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ proxy.parseTime(currentServiceStaff.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="专业特长" :span="2">
          <div style="max-height: 100px; overflow-y: auto;">{{ currentServiceStaff.specialties || '无' }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          <div style="max-height: 100px; overflow-y: auto;">{{ currentServiceStaff.remark || '无' }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup name="HealthServiceStaff" lang="ts">
import { 
  listServiceStaff, 
  getServiceStaff, 
  delServiceStaff, 
  addServiceStaff, 
  updateServiceStaff
} from '@/api/health/serviceStaff';
import { 
  HealthServiceStaffForm, 
  HealthServiceStaffQuery, 
  HealthServiceStaffVO 
} from '@/api/health/serviceStaff/types';
import { treeOrganization } from '@/api/health/organization';
import { HealthOrganizationTreeVO } from '@/api/health/organization/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable, sys_user_sex } = toRefs<any>(proxy?.useDict('sys_normal_disable', 'sys_user_sex'));

const serviceStaffList = ref<HealthServiceStaffVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<number | string>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const organizationOptions = ref<HealthOrganizationTreeVO[]>([]);
const currentServiceStaff = ref<HealthServiceStaffVO>({} as HealthServiceStaffVO);

// 列显隐信息
const columns = ref<FieldOption[]>([
  { key: 0, label: `人员编码`, visible: true, children: [] },
  { key: 1, label: `姓名`, visible: true, children: [] },
  { key: 2, label: `性别`, visible: true, children: [] },
  { key: 3, label: `手机号`, visible: true, children: [] },
  { key: 4, label: `人员类型`, visible: true, children: [] },
  { key: 5, label: `所属组织`, visible: true, children: [] },
  { key: 6, label: `工作时间`, visible: true, children: [] },
  { key: 7, label: `专业特长`, visible: true, children: [] },
  { key: 8, label: `状态`, visible: true, children: [] },
  { key: 9, label: `创建时间`, visible: true, children: [] }
]);

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const detailDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const queryFormRef = ref<ElFormInstance>();
const serviceStaffFormRef = ref<ElFormInstance>();

const initFormData: HealthServiceStaffForm = {
  id: undefined,
  name: '',
  gender: '',
  phone: '',
  staffType: '',
  orgId: undefined,
  workTime: '',
  specialties: '',
  status: '0',
  remark: ''
};

const initData: PageData<HealthServiceStaffForm, HealthServiceStaffQuery> = {
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    staffCode: undefined,
    name: undefined,
    gender: undefined,
    phone: undefined,
    staffType: undefined,
    orgId: undefined,
    status: undefined
  },
  rules: {
    name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
    gender: [{ required: true, message: '性别不能为空', trigger: 'change' }],
    staffType: [{ required: true, message: '人员类型不能为空', trigger: 'change' }],
    phone: [{ pattern: /^1[3456789][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }]
  }
};

const data = reactive<PageData<HealthServiceStaffForm, HealthServiceStaffQuery>>(initData);
const { queryParams, form, rules } = toRefs<PageData<HealthServiceStaffForm, HealthServiceStaffQuery>>(data);

/** 查询服务人员列表 */
const getList = async () => {
  loading.value = true;
  const res = await listServiceStaff(queryParams.value);
  serviceStaffList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};

/** 查询组织树结构 */
const getOrganizationTree = async () => {
  const res = await treeOrganization();
  organizationOptions.value = res.data;
};

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
};

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  serviceStaffFormRef.value?.resetFields();
};

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
};

/** 多选框选中数据 */
const handleSelectionChange = (selection: HealthServiceStaffVO[]) => {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '添加服务人员';
};

/** 修改按钮操作 */
const handleUpdate = async (row?: HealthServiceStaffVO) => {
  reset();
  const serviceStaffId = row?.id || ids.value[0];
  const res = await getServiceStaff(serviceStaffId);
  form.value = res.data;
  dialog.visible = true;
  dialog.title = '修改服务人员';
};

/** 详情按钮操作 */
const handleDetail = async (row: HealthServiceStaffVO) => {
  const res = await getServiceStaff(row.id);
  currentServiceStaff.value = res.data;
  detailDialog.visible = true;
};

/** 提交按钮 */
const submitForm = () => {
  serviceStaffFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      form.value.id ? await updateServiceStaff(form.value) : await addServiceStaff(form.value);
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 删除按钮操作 */
const handleDelete = async (row?: HealthServiceStaffVO) => {
  const serviceStaffIds = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除服务人员编号为"' + serviceStaffIds + '"的数据项？');
  await delServiceStaff(serviceStaffIds);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};

onMounted(() => {
  getList();
  getOrganizationTree();
});
</script>

<style scoped>
.el-tag {
  margin: 2px;
}
</style>