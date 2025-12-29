<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="告警编码" prop="alertCode">
              <el-input v-model="queryParams.alertCode" placeholder="请输入告警编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="设备名称" prop="deviceId">
              <el-select v-model="queryParams.deviceId" placeholder="选择设备" clearable filterable>
                <el-option
                  v-for="device in deviceOptions"
                  :key="device.id"
                  :label="device.deviceName"
                  :value="device.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="用户姓名" prop="userId">
              <el-select v-model="queryParams.userId" placeholder="选择用户" clearable filterable>
                <el-option
                  v-for="elder in elderOptions"
                  :key="elder.id"
                  :label="elder.name"
                  :value="elder.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="告警类型" prop="alertType">
              <el-select v-model="queryParams.alertType" placeholder="告警类型" clearable>
                <el-option label="设备离线" value="device_offline" />
                <el-option label="低电量" value="low_battery" />
                <el-option label="SOS求救" value="sos_alert" />
                <el-option label="跌倒检测" value="fall_detection" />
                <el-option label="越界告警" value="geo_fence" />
                <el-option label="体征异常" value="vital_abnormal" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
            <el-form-item label="告警级别" prop="alertLevel">
              <el-select v-model="queryParams.alertLevel" placeholder="告警级别" clearable>
                <el-option label="紧急" value="1" />
                <el-option label="重要" value="2" />
                <el-option label="一般" value="3" />
                <el-option label="提示" value="4" />
              </el-select>
            </el-form-item>
            <el-form-item label="处理状态" prop="handleStatus">
              <el-select v-model="queryParams.handleStatus" placeholder="处理状态" clearable>
                <el-option label="待处理" value="1" />
                <el-option label="已处理" value="2" />
                <el-option label="已忽略" value="3" />
              </el-select>
            </el-form-item>
            <el-form-item label="告警时间" style="width: 308px">
              <el-date-picker
                v-model="dateRange"
                value-format="YYYY-MM-DD HH:mm:ss"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
              ></el-date-picker>
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
            <el-button v-hasPermi="['health:alert:handle']" type="success" plain :disabled="single" icon="Check" @click="handleProcess()">处理</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:alert:ignore']" type="warning" plain :disabled="single" icon="Close" @click="handleIgnore()">忽略</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:alert:export']" type="info" plain icon="Download" @click="handleExport">导出</el-button>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" :columns="columns" @query-table="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="alertList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column v-if="columns[0].visible" key="alertCode" label="告警编码" align="center" prop="alertCode" width="120" />
        <el-table-column v-if="columns[1].visible" key="alertTitle" label="告警标题" align="center" prop="alertTitle" :show-overflow-tooltip="true" />
        <el-table-column v-if="columns[2].visible" key="deviceName" label="设备名称" align="center" prop="deviceName" width="120" />
        <el-table-column v-if="columns[3].visible" key="userName" label="用户姓名" align="center" prop="userName" width="100" />
        <el-table-column v-if="columns[4].visible" key="alertType" label="告警类型" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.alertType === 'device_offline'" type="info">设备离线</el-tag>
            <el-tag v-else-if="scope.row.alertType === 'low_battery'" type="warning">低电量</el-tag>
            <el-tag v-else-if="scope.row.alertType === 'sos_alert'" type="danger">SOS求救</el-tag>
            <el-tag v-else-if="scope.row.alertType === 'fall_detection'" type="danger">跌倒检测</el-tag>
            <el-tag v-else-if="scope.row.alertType === 'geo_fence'" type="warning">越界告警</el-tag>
            <el-tag v-else-if="scope.row.alertType === 'vital_abnormal'" type="danger">体征异常</el-tag>
            <el-tag v-else>其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[5].visible" key="alertLevel" label="告警级别" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.alertLevel === '1'" type="danger" effect="dark">紧急</el-tag>
            <el-tag v-else-if="scope.row.alertLevel === '2'" type="warning" effect="dark">重要</el-tag>
            <el-tag v-else-if="scope.row.alertLevel === '3'" type="primary">一般</el-tag>
            <el-tag v-else type="info">提示</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[6].visible" key="alertTime" label="告警时间" align="center" prop="alertTime" width="160">
          <template #default="scope">
            <span>{{ proxy.parseTime(scope.row.alertTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[7].visible" key="handleStatus" label="处理状态" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.handleStatus === '1'" type="danger">待处理</el-tag>
            <el-tag v-else-if="scope.row.handleStatus === '2'" type="success">已处理</el-tag>
            <el-tag v-else type="info">已忽略</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[8].visible" key="handleUserName" label="处理人" align="center" prop="handleUserName" width="100" />
        <el-table-column v-if="columns[9].visible" key="handleTime" label="处理时间" align="center" prop="handleTime" width="160">
          <template #default="scope">
            <span>{{ scope.row.handleTime ? proxy.parseTime(scope.row.handleTime) : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="180" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="详情" placement="top">
              <el-button link type="primary" icon="View" @click="handleDetail(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip v-if="scope.row.handleStatus === '1'" content="处理" placement="top">
              <el-button v-hasPermi="['health:alert:handle']" link type="primary" icon="Check" @click="handleProcess(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip v-if="scope.row.handleStatus === '1'" content="忽略" placement="top">
              <el-button v-hasPermi="['health:alert:ignore']" link type="primary" icon="Close" @click="handleIgnore(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip v-if="scope.row.longitude && scope.row.latitude" content="地图" placement="top">
              <el-button link type="primary" icon="Location" @click="handleShowMap(scope.row)"></el-button>
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

    <!-- 告警详情对话框 -->
    <el-dialog v-model="detailDialog.visible" title="告警详情" destroy-on-close append-to-body width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="告警编码">{{ currentAlert.alertCode }}</el-descriptions-item>
        <el-descriptions-item label="告警标题">{{ currentAlert.alertTitle }}</el-descriptions-item>
        <el-descriptions-item label="设备名称">{{ currentAlert.deviceName }}</el-descriptions-item>
        <el-descriptions-item label="用户姓名">{{ currentAlert.userName }}</el-descriptions-item>
        <el-descriptions-item label="告警类型">
          <el-tag v-if="currentAlert.alertType === 'device_offline'" type="info">设备离线</el-tag>
          <el-tag v-else-if="currentAlert.alertType === 'low_battery'" type="warning">低电量</el-tag>
          <el-tag v-else-if="currentAlert.alertType === 'sos_alert'" type="danger">SOS求救</el-tag>
          <el-tag v-else-if="currentAlert.alertType === 'fall_detection'" type="danger">跌倒检测</el-tag>
          <el-tag v-else-if="currentAlert.alertType === 'geo_fence'" type="warning">越界告警</el-tag>
          <el-tag v-else-if="currentAlert.alertType === 'vital_abnormal'" type="danger">体征异常</el-tag>
          <el-tag v-else>其他</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="告警级别">
          <el-tag v-if="currentAlert.alertLevel === '1'" type="danger" effect="dark">紧急</el-tag>
          <el-tag v-else-if="currentAlert.alertLevel === '2'" type="warning" effect="dark">重要</el-tag>
          <el-tag v-else-if="currentAlert.alertLevel === '3'" type="primary">一般</el-tag>
          <el-tag v-else type="info">提示</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="告警时间">{{ proxy.parseTime(currentAlert.alertTime) }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag v-if="currentAlert.handleStatus === '1'" type="danger">待处理</el-tag>
          <el-tag v-else-if="currentAlert.handleStatus === '2'" type="success">已处理</el-tag>
          <el-tag v-else type="info">已忽略</el-tag>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentAlert.handleUserName" label="处理人">{{ currentAlert.handleUserName }}</el-descriptions-item>
        <el-descriptions-item v-if="currentAlert.handleTime" label="处理时间">{{ proxy.parseTime(currentAlert.handleTime) }}</el-descriptions-item>
        <el-descriptions-item v-if="currentAlert.location" label="位置信息">{{ currentAlert.location }}</el-descriptions-item>
        <el-descriptions-item v-if="currentAlert.longitude && currentAlert.latitude" label="坐标">
          {{ currentAlert.longitude }}, {{ currentAlert.latitude }}
        </el-descriptions-item>
        <el-descriptions-item label="告警内容" :span="2">
          <div style="max-height: 200px; overflow-y: auto;">{{ currentAlert.alertContent }}</div>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentAlert.handleResult" label="处理结果" :span="2">
          <div style="max-height: 200px; overflow-y: auto;">{{ currentAlert.handleResult }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 告警处理对话框 -->
    <el-dialog v-model="processDialog.visible" title="处理告警" destroy-on-close append-to-body width="500px">
      <el-form ref="processFormRef" :model="processForm" :rules="processRules" label-width="80px">
        <el-form-item label="告警标题">
          <el-input v-model="currentAlert.alertTitle" disabled />
        </el-form-item>
        <el-form-item label="处理结果" prop="handleResult">
          <el-input v-model="processForm.handleResult" type="textarea" :rows="4" placeholder="请输入处理结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitProcessForm">确 定</el-button>
          <el-button @click="processDialog.visible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 地图显示对话框 -->
    <el-dialog v-model="mapDialog.visible" title="告警位置" destroy-on-close append-to-body width="800px">
      <div id="alertMap" style="height: 400px; width: 100%;"></div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="mapDialog.visible = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="HealthAlert" lang="ts">
import { 
  listAlert, 
  getAlert, 
  handleAlert, 
  ignoreAlert,
  exportAlert
} from '@/api/health/alert';
import { 
  HealthDeviceAlertQuery, 
  HealthDeviceAlertVO,
  HealthDeviceAlertHandleForm 
} from '@/api/health/alert/types';
import { listDevice } from '@/api/health/device';
import { HealthDeviceVO } from '@/api/health/device/types';
import { listElder } from '@/api/health/elder';
import { HealthElderVO } from '@/api/health/elder/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const alertList = ref<HealthDeviceAlertVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<number | string>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRange = ref<[DateModelType, DateModelType]>(['', '']);
const deviceOptions = ref<HealthDeviceVO[]>([]);
const elderOptions = ref<HealthElderVO[]>([]);
const currentAlert = ref<HealthDeviceAlertVO>({} as HealthDeviceAlertVO);

// 列显隐信息
const columns = ref<FieldOption[]>([
  { key: 0, label: `告警编码`, visible: true, children: [] },
  { key: 1, label: `告警标题`, visible: true, children: [] },
  { key: 2, label: `设备名称`, visible: true, children: [] },
  { key: 3, label: `用户姓名`, visible: true, children: [] },
  { key: 4, label: `告警类型`, visible: true, children: [] },
  { key: 5, label: `告警级别`, visible: true, children: [] },
  { key: 6, label: `告警时间`, visible: true, children: [] },
  { key: 7, label: `处理状态`, visible: true, children: [] },
  { key: 8, label: `处理人`, visible: true, children: [] },
  { key: 9, label: `处理时间`, visible: true, children: [] }
]);

const detailDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const processDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const mapDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const queryFormRef = ref<ElFormInstance>();
const processFormRef = ref<ElFormInstance>();

const initQueryData: HealthDeviceAlertQuery = {
  pageNum: 1,
  pageSize: 10,
  alertCode: undefined,
  deviceId: undefined,
  userId: undefined,
  alertType: undefined,
  alertLevel: undefined,
  handleStatus: undefined,
  alertTimeStart: undefined,
  alertTimeEnd: undefined
};

const queryParams = ref<HealthDeviceAlertQuery>({ ...initQueryData });

const processForm = ref<HealthDeviceAlertHandleForm>({
  id: undefined,
  handleResult: ''
});

const processRules = ref({
  handleResult: [{ required: true, message: '处理结果不能为空', trigger: 'blur' }]
});

/** 查询告警列表 */
const getList = async () => {
  loading.value = true;
  const params = proxy?.addDateRange(queryParams.value, dateRange.value, 'alertTime');
  const res = await listAlert(params);
  alertList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};

/** 查询设备列表 */
const getDeviceList = async () => {
  const res = await listDevice({ pageNum: 1, pageSize: 1000 });
  deviceOptions.value = res.rows;
};

/** 查询老人列表 */
const getElderList = async () => {
  const res = await listElder({ pageNum: 1, pageSize: 1000 });
  elderOptions.value = res.rows;
};

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};

/** 重置按钮操作 */
const resetQuery = () => {
  dateRange.value = ['', ''];
  queryFormRef.value?.resetFields();
  handleQuery();
};

/** 多选框选中数据 */
const handleSelectionChange = (selection: HealthDeviceAlertVO[]) => {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 详情按钮操作 */
const handleDetail = async (row: HealthDeviceAlertVO) => {
  const res = await getAlert(row.id);
  currentAlert.value = res.data;
  detailDialog.visible = true;
};

/** 处理按钮操作 */
const handleProcess = (row?: HealthDeviceAlertVO) => {
  if (row) {
    currentAlert.value = row;
    processForm.value = { id: row.id, handleResult: '' };
  } else {
    const alertId = ids.value[0];
    const alert = alertList.value.find(item => item.id === alertId);
    if (alert) {
      currentAlert.value = alert;
      processForm.value = { id: alertId, handleResult: '' };
    }
  }
  processDialog.visible = true;
};

/** 提交处理表单 */
const submitProcessForm = () => {
  processFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      await handleAlert(processForm.value);
      proxy?.$modal.msgSuccess('处理成功');
      processDialog.visible = false;
      await getList();
    }
  });
};

/** 忽略按钮操作 */
const handleIgnore = async (row?: HealthDeviceAlertVO) => {
  const alertIds = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认忽略选中的告警？');
  
  if (Array.isArray(alertIds)) {
    for (const id of alertIds) {
      await ignoreAlert(id);
    }
  } else {
    await ignoreAlert(alertIds);
  }
  
  await getList();
  proxy?.$modal.msgSuccess('忽略成功');
};

/** 显示地图 */
const handleShowMap = (row: HealthDeviceAlertVO) => {
  currentAlert.value = row;
  mapDialog.visible = true;
  
  // 延迟初始化地图，确保DOM已渲染
  nextTick(() => {
    initMap();
  });
};

/** 初始化地图 */
const initMap = () => {
  // 这里可以集成高德地图或其他地图服务
  // 示例代码，实际需要根据具体地图API实现
  const mapContainer = document.getElementById('alertMap');
  if (mapContainer && currentAlert.value.longitude && currentAlert.value.latitude) {
    mapContainer.innerHTML = `
      <div style="display: flex; align-items: center; justify-content: center; height: 100%; background: #f5f5f5; border: 1px dashed #ccc;">
        <div style="text-align: center;">
          <p style="margin: 0; font-size: 16px; color: #666;">地图位置</p>
          <p style="margin: 10px 0 0 0; font-size: 14px; color: #999;">
            经度: ${currentAlert.value.longitude}<br>
            纬度: ${currentAlert.value.latitude}<br>
            位置: ${currentAlert.value.location || '未知'}
          </p>
          <p style="margin: 10px 0 0 0; font-size: 12px; color: #ccc;">
            (此处可集成高德地图等地图服务)
          </p>
        </div>
      </div>
    `;
  }
};

/** 导出按钮操作 */
const handleExport = () => {
  const params = proxy?.addDateRange(queryParams.value, dateRange.value, 'alertTime');
  proxy?.download(
    'health/alert/export',
    params,
    `alert_${new Date().getTime()}.xlsx`
  );
};

onMounted(() => {
  getList();
  getDeviceList();
  getElderList();
});
</script>

<style scoped>
.el-tag {
  margin: 2px;
}
</style>