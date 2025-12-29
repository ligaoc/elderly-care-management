<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="设备编码" prop="deviceCode">
              <el-input v-model="queryParams.deviceCode" placeholder="请输入设备编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="设备名称" prop="deviceName">
              <el-input v-model="queryParams.deviceName" placeholder="请输入设备名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="设备类型" prop="deviceTypeId">
              <el-tree-select
                v-model="queryParams.deviceTypeId"
                :data="deviceTypeOptions"
                :props="{ value: 'id', label: 'typeName', children: 'children' } as any"
                value-key="id"
                placeholder="选择设备类型"
                check-strictly
                clearable
              />
            </el-form-item>
            <el-form-item label="生产厂商" prop="manufacturer">
              <el-input v-model="queryParams.manufacturer" placeholder="请输入生产厂商" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="在线状态" prop="onlineStatus">
              <el-select v-model="queryParams.onlineStatus" placeholder="在线状态" clearable>
                <el-option label="在线" value="1" />
                <el-option label="离线" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item label="绑定用户" prop="bindUserId">
              <el-input v-model="queryParams.bindUserId" placeholder="请输入绑定用户ID" clearable @keyup.enter="handleQuery" />
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
            <el-button v-hasPermi="['health:device:add']" type="primary" plain icon="Plus" @click="handleAdd()">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:device:edit']" type="success" plain :disabled="single" icon="Edit" @click="handleUpdate()">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:device:remove']" type="danger" plain :disabled="multiple" icon="Delete" @click="handleDelete()">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-dropdown class="mt-[1px]">
              <el-button plain type="info">
                更多
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item icon="Download" @click="importTemplate">下载模板</el-dropdown-item>
                  <el-dropdown-item v-if="checkPermi(['health:device:import'])" icon="Top" @click="handleImport">导入数据</el-dropdown-item>
                  <el-dropdown-item v-if="checkPermi(['health:device:export'])" icon="Download" @click="handleExport">导出数据</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" :columns="columns" @query-table="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="deviceList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column v-if="columns[0].visible" key="deviceCode" label="设备编码" align="center" prop="deviceCode" width="120" />
        <el-table-column v-if="columns[1].visible" key="deviceName" label="设备名称" align="center" prop="deviceName" :show-overflow-tooltip="true" />
        <el-table-column v-if="columns[2].visible" key="deviceTypeName" label="设备类型" align="center" prop="deviceTypeName" width="120" />
        <el-table-column v-if="columns[3].visible" key="deviceModel" label="设备型号" align="center" prop="deviceModel" width="120" />
        <el-table-column v-if="columns[4].visible" key="manufacturer" label="生产厂商" align="center" prop="manufacturer" width="120" />
        <el-table-column v-if="columns[5].visible" key="serialNumber" label="序列号" align="center" prop="serialNumber" width="150" />
        <el-table-column v-if="columns[6].visible" key="bindUserName" label="绑定用户" align="center" prop="bindUserName" width="100" />
        <el-table-column v-if="columns[7].visible" key="onlineStatus" label="在线状态" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.onlineStatus === '1'" type="success">在线</el-tag>
            <el-tag v-else type="danger">离线</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[8].visible" key="batteryLevel" label="电量" align="center" width="80">
          <template #default="scope">
            <span v-if="scope.row.batteryLevel">{{ scope.row.batteryLevel }}%</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[9].visible" key="location" label="位置" align="center" prop="location" :show-overflow-tooltip="true" />
        <el-table-column v-if="columns[10].visible" label="创建时间" align="center" prop="createTime" width="160">
          <template #default="scope">
            <span>{{ proxy.parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['health:device:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['health:device:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip v-if="!scope.row.bindUserId" content="绑定" placement="top">
              <el-button v-hasPermi="['health:device:bind']" link type="primary" icon="Link" @click="handleBind(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip v-else content="解绑" placement="top">
              <el-button v-hasPermi="['health:device:unbind']" link type="primary" icon="Unlock" @click="handleUnbind(scope.row)"></el-button>
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

    <!-- 添加或修改设备对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" destroy-on-close append-to-body width="600px">
      <el-form ref="deviceFormRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备名称" prop="deviceName">
              <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备类型" prop="deviceTypeId">
              <el-tree-select
                v-model="form.deviceTypeId"
                :data="deviceTypeOptions"
                :props="{ value: 'id', label: 'typeName', children: 'children' } as any"
                value-key="id"
                placeholder="选择设备类型"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备型号" prop="deviceModel">
              <el-input v-model="form.deviceModel" placeholder="请输入设备型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产厂商" prop="manufacturer">
              <el-input v-model="form.manufacturer" placeholder="请输入生产厂商" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="序列号" prop="serialNumber">
              <el-input v-model="form.serialNumber" placeholder="请输入序列号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电量" prop="batteryLevel">
              <el-input-number v-model="form.batteryLevel" :min="0" :max="100" placeholder="电量百分比" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input-number v-model="form.longitude" :precision="6" placeholder="经度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input-number v-model="form.latitude" :precision="6" placeholder="纬度" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="位置描述" prop="location">
              <el-input v-model="form.location" placeholder="请输入位置描述" />
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

    <!-- 设备绑定对话框 -->
    <el-dialog v-model="bindDialog.visible" title="绑定设备" destroy-on-close append-to-body width="500px">
      <el-form ref="bindFormRef" :model="bindForm" :rules="bindRules" label-width="80px">
        <el-form-item label="设备名称">
          <el-input v-model="currentDevice.deviceName" disabled />
        </el-form-item>
        <el-form-item label="选择老人" prop="elderId">
          <el-select v-model="bindForm.elderId" placeholder="请选择要绑定的老人" filterable>
            <el-option
              v-for="elder in elderOptions"
              :key="elder.id"
              :label="`${elder.name} (${elder.userCode})`"
              :value="elder.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitBindForm">确 定</el-button>
          <el-button @click="bindDialog.visible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 设备导入对话框 -->
    <el-dialog v-model="upload.open" :title="upload.title" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload">
          <i-ep-upload-filled />
        </el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="text-center el-upload__tip">
            <div class="el-upload__tip"><el-checkbox v-model="upload.updateSupport" />是否更新已经存在的设备数据</div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="HealthDevice" lang="ts">
import { 
  listDevice, 
  getDevice, 
  delDevice, 
  addDevice, 
  updateDevice,
  bindDevice,
  unbindDevice,
  exportDevice,
  importDevice
} from '@/api/health/device';
import { 
  HealthDeviceForm, 
  HealthDeviceQuery, 
  HealthDeviceVO,
  HealthDeviceBindForm 
} from '@/api/health/device/types';
import { treeDeviceType } from '@/api/health/deviceType';
import { HealthDeviceTypeTreeVO } from '@/api/health/deviceType/types';
import { listElder } from '@/api/health/elder';
import { HealthElderVO } from '@/api/health/elder/types';
import { globalHeaders } from '@/utils/request';
import { checkPermi } from '@/utils/permission';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const deviceList = ref<HealthDeviceVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<number | string>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const deviceTypeOptions = ref<HealthDeviceTypeTreeVO[]>([]);
const elderOptions = ref<HealthElderVO[]>([]);
const currentDevice = ref<HealthDeviceVO>({} as HealthDeviceVO);

// 列显隐信息
const columns = ref<FieldOption[]>([
  { key: 0, label: `设备编码`, visible: true, children: [] },
  { key: 1, label: `设备名称`, visible: true, children: [] },
  { key: 2, label: `设备类型`, visible: true, children: [] },
  { key: 3, label: `设备型号`, visible: true, children: [] },
  { key: 4, label: `生产厂商`, visible: true, children: [] },
  { key: 5, label: `序列号`, visible: true, children: [] },
  { key: 6, label: `绑定用户`, visible: true, children: [] },
  { key: 7, label: `在线状态`, visible: true, children: [] },
  { key: 8, label: `电量`, visible: true, children: [] },
  { key: 9, label: `位置`, visible: true, children: [] },
  { key: 10, label: `创建时间`, visible: true, children: [] }
]);

/*** 设备导入参数 */
const upload = reactive<ImportOption>({
  open: false,
  title: '',
  isUploading: false,
  updateSupport: 0,
  headers: globalHeaders(),
  url: import.meta.env.VITE_APP_BASE_API + '/health/device/import'
});

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const bindDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const queryFormRef = ref<ElFormInstance>();
const deviceFormRef = ref<ElFormInstance>();
const bindFormRef = ref<ElFormInstance>();
const uploadRef = ref<ElUploadInstance>();

const initFormData: HealthDeviceForm = {
  id: undefined,
  deviceName: '',
  deviceTypeId: undefined,
  deviceModel: '',
  manufacturer: '',
  serialNumber: '',
  batteryLevel: undefined,
  longitude: undefined,
  latitude: undefined,
  location: '',
  status: '0',
  remark: ''
};

const initBindFormData: HealthDeviceBindForm = {
  deviceId: undefined,
  elderId: undefined
};

const initData: PageData<HealthDeviceForm, HealthDeviceQuery> = {
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    deviceCode: undefined,
    deviceName: undefined,
    deviceTypeId: undefined,
    deviceModel: undefined,
    manufacturer: undefined,
    serialNumber: undefined,
    bindUserId: undefined,
    onlineStatus: undefined,
    batteryLevel: undefined
  },
  rules: {
    deviceName: [{ required: true, message: '设备名称不能为空', trigger: 'blur' }],
    deviceTypeId: [{ required: true, message: '设备类型不能为空', trigger: 'change' }]
  }
};

const data = reactive<PageData<HealthDeviceForm, HealthDeviceQuery>>(initData);
const { queryParams, form, rules } = toRefs<PageData<HealthDeviceForm, HealthDeviceQuery>>(data);

const bindForm = ref<HealthDeviceBindForm>({ ...initBindFormData });
const bindRules = ref({
  elderId: [{ required: true, message: '请选择要绑定的老人', trigger: 'change' }]
});

/** 查询设备列表 */
const getList = async () => {
  loading.value = true;
  const res = await listDevice(queryParams.value);
  deviceList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};

/** 查询设备类型树结构 */
const getDeviceTypeTree = async () => {
  const res = await treeDeviceType();
  deviceTypeOptions.value = res.data;
};

/** 查询老人列表 */
const getElderList = async () => {
  const res = await listElder({ pageNum: 1, pageSize: 1000 });
  elderOptions.value = res.rows;
};

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
};

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  deviceFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: HealthDeviceVO[]) => {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '添加设备';
};

/** 修改按钮操作 */
const handleUpdate = async (row?: HealthDeviceVO) => {
  reset();
  const deviceId = row?.id || ids.value[0];
  const res = await getDevice(deviceId);
  form.value = res.data;
  dialog.visible = true;
  dialog.title = '修改设备';
};

/** 提交按钮 */
const submitForm = () => {
  deviceFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      form.value.id ? await updateDevice(form.value) : await addDevice(form.value);
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 删除按钮操作 */
const handleDelete = async (row?: HealthDeviceVO) => {
  const deviceIds = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除设备编号为"' + deviceIds + '"的数据项？');
  await delDevice(deviceIds);
  await getList();
  proxy?.$modal.msgSuccess('删除成功');
};

/** 绑定按钮操作 */
const handleBind = (row: HealthDeviceVO) => {
  currentDevice.value = row;
  bindForm.value = { deviceId: row.id, elderId: undefined };
  bindDialog.visible = true;
};

/** 解绑按钮操作 */
const handleUnbind = async (row: HealthDeviceVO) => {
  await proxy?.$modal.confirm('是否确认解绑设备"' + row.deviceName + '"？');
  await unbindDevice(row.id);
  await getList();
  proxy?.$modal.msgSuccess('解绑成功');
};

/** 提交绑定表单 */
const submitBindForm = () => {
  bindFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      await bindDevice(bindForm.value);
      proxy?.$modal.msgSuccess('绑定成功');
      bindDialog.visible = false;
      await getList();
    }
  });
};

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'health/device/export',
    {
      ...queryParams.value
    },
    `device_${new Date().getTime()}.xlsx`
  );
};

/** 导入按钮操作 */
const handleImport = () => {
  upload.title = '设备导入';
  upload.open = true;
};

/** 下载模板操作 */
const importTemplate = () => {
  proxy?.download('health/device/importTemplate', {}, `device_template_${new Date().getTime()}.xlsx`);
};

/**文件上传中处理 */
const handleFileUploadProgress = () => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response: any, file: UploadFile) => {
  upload.open = false;
  upload.isUploading = false;
  uploadRef.value?.handleRemove(file);
  ElMessageBox.alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + '</div>', '导入结果', {
    dangerouslyUseHTMLString: true
  });
  getList();
};

/** 提交上传文件 */
function submitFileForm() {
  uploadRef.value?.submit();
}

onMounted(() => {
  getList();
  getDeviceTypeTree();
  getElderList();
});
</script>