<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="老人姓名" prop="elderId">
              <el-select v-model="queryParams.elderId" placeholder="选择老人" clearable filterable>
                <el-option
                  v-for="elder in elderOptions"
                  :key="elder.id"
                  :label="`${elder.name} (${elder.userCode})`"
                  :value="elder.id"
                />
              </el-select>
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
            <el-form-item label="是否异常" prop="isAbnormal">
              <el-select v-model="queryParams.isAbnormal" placeholder="是否异常" clearable>
                <el-option label="正常" value="0" />
                <el-option label="异常" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item label="测量时间" style="width: 308px">
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
            <el-button v-hasPermi="['health:vitalSigns:add']" type="primary" plain icon="Plus" @click="handleAdd()">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-hasPermi="['health:vitalSigns:export']" type="info" plain icon="Download" @click="handleExport">导出</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="TrendCharts" @click="handleTrend" :disabled="!selectedElder">趋势分析</el-button>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" :columns="columns" @query-table="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="vitalSignsList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column v-if="columns[0].visible" key="elderName" label="老人姓名" align="center" prop="elderName" width="100" />
        <el-table-column v-if="columns[1].visible" key="deviceName" label="设备名称" align="center" prop="deviceName" width="120" />
        <el-table-column v-if="columns[2].visible" key="measureTime" label="测量时间" align="center" prop="measureTime" width="160">
          <template #default="scope">
            <span>{{ proxy.parseTime(scope.row.measureTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[3].visible" key="temperature" label="体温(°C)" align="center" width="100">
          <template #default="scope">
            <span v-if="scope.row.temperature" :class="{ 'text-red-500': isTemperatureAbnormal(scope.row.temperature) }">
              {{ scope.row.temperature }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[4].visible" key="heartRate" label="心率(次/分)" align="center" width="100">
          <template #default="scope">
            <span v-if="scope.row.heartRate" :class="{ 'text-red-500': isHeartRateAbnormal(scope.row.heartRate) }">
              {{ scope.row.heartRate }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[5].visible" key="bloodPressure" label="血压(mmHg)" align="center" width="120">
          <template #default="scope">
            <span v-if="scope.row.systolicPressure && scope.row.diastolicPressure" 
                  :class="{ 'text-red-500': isBloodPressureAbnormal(scope.row.systolicPressure, scope.row.diastolicPressure) }">
              {{ scope.row.systolicPressure }}/{{ scope.row.diastolicPressure }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[6].visible" key="bloodOxygen" label="血氧(%)" align="center" width="100">
          <template #default="scope">
            <span v-if="scope.row.bloodOxygen" :class="{ 'text-red-500': isBloodOxygenAbnormal(scope.row.bloodOxygen) }">
              {{ scope.row.bloodOxygen }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[7].visible" key="bloodSugar" label="血糖(mmol/L)" align="center" width="120">
          <template #default="scope">
            <span v-if="scope.row.bloodSugar">{{ scope.row.bloodSugar }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[8].visible" key="isAbnormal" label="是否异常" align="center" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.isAbnormal === '1'" type="danger">异常</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[9].visible" key="abnormalItems" label="异常项目" align="center" prop="abnormalItems" :show-overflow-tooltip="true" />
        <el-table-column label="操作" fixed="right" width="120" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="详情" placement="top">
              <el-button link type="primary" icon="View" @click="handleDetail(scope.row)"></el-button>
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

    <!-- 添加体征数据对话框 -->
    <el-dialog v-model="dialog.visible" title="新增体征数据" destroy-on-close append-to-body width="600px">
      <el-form ref="vitalSignsFormRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="老人" prop="elderId">
              <el-select v-model="form.elderId" placeholder="请选择老人" filterable>
                <el-option
                  v-for="elder in elderOptions"
                  :key="elder.id"
                  :label="`${elder.name} (${elder.userCode})`"
                  :value="elder.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备" prop="deviceId">
              <el-select v-model="form.deviceId" placeholder="请选择设备" filterable>
                <el-option
                  v-for="device in deviceOptions"
                  :key="device.id"
                  :label="device.deviceName"
                  :value="device.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="测量时间" prop="measureTime">
              <el-date-picker
                v-model="form.measureTime"
                type="datetime"
                placeholder="选择测量时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体温(°C)" prop="temperature">
              <el-input-number v-model="form.temperature" :min="30" :max="45" :precision="1" placeholder="体温" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="心率(次/分)" prop="heartRate">
              <el-input-number v-model="form.heartRate" :min="30" :max="200" placeholder="心率" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收缩压(mmHg)" prop="systolicPressure">
              <el-input-number v-model="form.systolicPressure" :min="60" :max="250" placeholder="收缩压" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="舒张压(mmHg)" prop="diastolicPressure">
              <el-input-number v-model="form.diastolicPressure" :min="40" :max="150" placeholder="舒张压" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血氧(%)" prop="bloodOxygen">
              <el-input-number v-model="form.bloodOxygen" :min="70" :max="100" placeholder="血氧" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血糖(mmol/L)" prop="bloodSugar">
              <el-input-number v-model="form.bloodSugar" :min="2" :max="30" :precision="1" placeholder="血糖" />
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

    <!-- 体征详情对话框 -->
    <el-dialog v-model="detailDialog.visible" title="体征详情" destroy-on-close append-to-body width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="老人姓名">{{ currentVitalSigns.elderName }}</el-descriptions-item>
        <el-descriptions-item label="设备名称">{{ currentVitalSigns.deviceName }}</el-descriptions-item>
        <el-descriptions-item label="测量时间">{{ proxy.parseTime(currentVitalSigns.measureTime) }}</el-descriptions-item>
        <el-descriptions-item label="是否异常">
          <el-tag v-if="currentVitalSigns.isAbnormal === '1'" type="danger">异常</el-tag>
          <el-tag v-else type="success">正常</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="体温(°C)">
          <span :class="{ 'text-red-500': isTemperatureAbnormal(currentVitalSigns.temperature) }">
            {{ currentVitalSigns.temperature || '-' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="心率(次/分)">
          <span :class="{ 'text-red-500': isHeartRateAbnormal(currentVitalSigns.heartRate) }">
            {{ currentVitalSigns.heartRate || '-' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="收缩压(mmHg)">
          <span :class="{ 'text-red-500': isBloodPressureAbnormal(currentVitalSigns.systolicPressure, currentVitalSigns.diastolicPressure) }">
            {{ currentVitalSigns.systolicPressure || '-' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="舒张压(mmHg)">
          <span :class="{ 'text-red-500': isBloodPressureAbnormal(currentVitalSigns.systolicPressure, currentVitalSigns.diastolicPressure) }">
            {{ currentVitalSigns.diastolicPressure || '-' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="血氧(%)">
          <span :class="{ 'text-red-500': isBloodOxygenAbnormal(currentVitalSigns.bloodOxygen) }">
            {{ currentVitalSigns.bloodOxygen || '-' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="血糖(mmol/L)">{{ currentVitalSigns.bloodSugar || '-' }}</el-descriptions-item>
        <el-descriptions-item label="异常项目" :span="2">{{ currentVitalSigns.abnormalItems || '无' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          <div style="max-height: 100px; overflow-y: auto;">{{ currentVitalSigns.remark || '无' }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 趋势分析对话框 -->
    <el-dialog v-model="trendDialog.visible" title="体征趋势分析" destroy-on-close append-to-body width="1000px">
      <div class="mb-4">
        <el-form :inline="true">
          <el-form-item label="老人">
            <el-select v-model="trendQuery.elderId" placeholder="请选择老人" @change="handleElderChange">
              <el-option
                v-for="elder in elderOptions"
                :key="elder.id"
                :label="`${elder.name} (${elder.userCode})`"
                :value="elder.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="数据类型">
            <el-select v-model="trendQuery.dataType" placeholder="请选择数据类型" @change="getTrendData">
              <el-option label="体温" value="temperature" />
              <el-option label="心率" value="heartRate" />
              <el-option label="血压" value="bloodPressure" />
              <el-option label="血氧" value="bloodOxygen" />
              <el-option label="血糖" value="bloodSugar" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="trendDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              @change="getTrendData"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <div id="trendChart" style="height: 400px; width: 100%;"></div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="trendDialog.visible = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="HealthVitalSigns" lang="ts">
import { 
  listVitalSigns, 
  getVitalSigns, 
  addVitalSigns,
  exportVitalSigns,
  getVitalSignsTrend
} from '@/api/health/vitalSigns';
import { 
  HealthVitalSignsQuery, 
  HealthVitalSignsVO, 
  HealthVitalSignsForm,
  HealthVitalSignsTrendQuery,
  HealthVitalSignsTrendVO
} from '@/api/health/vitalSigns/types';
import { listElder } from '@/api/health/elder';
import { HealthElderVO } from '@/api/health/elder/types';
import { listDevice } from '@/api/health/device';
import { HealthDeviceVO } from '@/api/health/device/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const vitalSignsList = ref<HealthVitalSignsVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<number | string>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRange = ref<[DateModelType, DateModelType]>(['', '']);
const elderOptions = ref<HealthElderVO[]>([]);
const deviceOptions = ref<HealthDeviceVO[]>([]);
const currentVitalSigns = ref<HealthVitalSignsVO>({} as HealthVitalSignsVO);
const selectedElder = ref<string | number>('');

// 趋势分析相关
const trendDateRange = ref<[string, string]>(['', '']);

// 列显隐信息
const columns = ref<FieldOption[]>([
  { key: 0, label: `老人姓名`, visible: true, children: [] },
  { key: 1, label: `设备名称`, visible: true, children: [] },
  { key: 2, label: `测量时间`, visible: true, children: [] },
  { key: 3, label: `体温`, visible: true, children: [] },
  { key: 4, label: `心率`, visible: true, children: [] },
  { key: 5, label: `血压`, visible: true, children: [] },
  { key: 6, label: `血氧`, visible: true, children: [] },
  { key: 7, label: `血糖`, visible: true, children: [] },
  { key: 8, label: `是否异常`, visible: true, children: [] },
  { key: 9, label: `异常项目`, visible: true, children: [] }
]);

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const detailDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const trendDialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const queryFormRef = ref<ElFormInstance>();
const vitalSignsFormRef = ref<ElFormInstance>();

const initFormData: HealthVitalSignsForm = {
  id: undefined,
  elderId: undefined,
  deviceId: undefined,
  measureTime: '',
  temperature: undefined,
  heartRate: undefined,
  systolicPressure: undefined,
  diastolicPressure: undefined,
  bloodOxygen: undefined,
  bloodSugar: undefined,
  remark: ''
};

const initTrendQueryData: HealthVitalSignsTrendQuery = {
  elderId: undefined,
  startDate: '',
  endDate: '',
  dataType: 'temperature'
};

const initData: PageData<HealthVitalSignsForm, HealthVitalSignsQuery> = {
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderId: undefined,
    deviceId: undefined,
    measureTimeStart: undefined,
    measureTimeEnd: undefined,
    isAbnormal: undefined,
    temperatureMin: undefined,
    temperatureMax: undefined,
    heartRateMin: undefined,
    heartRateMax: undefined,
    systolicPressureMin: undefined,
    systolicPressureMax: undefined,
    diastolicPressureMin: undefined,
    diastolicPressureMax: undefined,
    bloodOxygenMin: undefined,
    bloodOxygenMax: undefined,
    bloodSugarMin: undefined,
    bloodSugarMax: undefined
  },
  rules: {
    elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
    measureTime: [{ required: true, message: '请选择测量时间', trigger: 'change' }]
  }
};

const data = reactive<PageData<HealthVitalSignsForm, HealthVitalSignsQuery>>(initData);
const { queryParams, form, rules } = toRefs<PageData<HealthVitalSignsForm, HealthVitalSignsQuery>>(data);

const trendQuery = ref<HealthVitalSignsTrendQuery>({ ...initTrendQueryData });

/** 查询体征数据列表 */
const getList = async () => {
  loading.value = true;
  const params = proxy?.addDateRange(queryParams.value, dateRange.value, 'measureTime');
  const res = await listVitalSigns(params);
  vitalSignsList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};

/** 查询老人列表 */
const getElderList = async () => {
  const res = await listElder({ pageNum: 1, pageSize: 1000 });
  elderOptions.value = res.rows;
};

/** 查询设备列表 */
const getDeviceList = async () => {
  const res = await listDevice({ pageNum: 1, pageSize: 1000 });
  deviceOptions.value = res.rows;
};

/** 体温异常判断 */
const isTemperatureAbnormal = (temperature: number) => {
  return temperature && (temperature > 37.5 || temperature < 35);
};

/** 心率异常判断 */
const isHeartRateAbnormal = (heartRate: number) => {
  return heartRate && (heartRate > 100 || heartRate < 60);
};

/** 血压异常判断 */
const isBloodPressureAbnormal = (systolic: number, diastolic: number) => {
  return (systolic && (systolic > 140 || systolic < 90)) || (diastolic && (diastolic > 90 || diastolic < 60));
};

/** 血氧异常判断 */
const isBloodOxygenAbnormal = (bloodOxygen: number) => {
  return bloodOxygen && bloodOxygen < 95;
};

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
};

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  vitalSignsFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: HealthVitalSignsVO[]) => {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
  
  // 设置选中的老人，用于趋势分析
  if (selection.length === 1) {
    selectedElder.value = selection[0].elderId;
  } else {
    selectedElder.value = '';
  }
};

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  form.value.measureTime = proxy.parseTime(new Date());
  dialog.visible = true;
};

/** 详情按钮操作 */
const handleDetail = async (row: HealthVitalSignsVO) => {
  const res = await getVitalSigns(row.id);
  currentVitalSigns.value = res.data;
  detailDialog.visible = true;
};

/** 提交按钮 */
const submitForm = () => {
  vitalSignsFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      await addVitalSigns(form.value);
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 趋势分析 */
const handleTrend = () => {
  if (selectedElder.value) {
    trendQuery.value.elderId = selectedElder.value;
  }
  
  // 设置默认时间范围为最近30天
  const endDate = new Date();
  const startDate = new Date();
  startDate.setDate(endDate.getDate() - 30);
  
  trendDateRange.value = [
    startDate.toISOString().split('T')[0],
    endDate.toISOString().split('T')[0]
  ];
  
  trendQuery.value.startDate = trendDateRange.value[0];
  trendQuery.value.endDate = trendDateRange.value[1];
  
  trendDialog.visible = true;
  
  nextTick(() => {
    if (trendQuery.value.elderId && trendQuery.value.dataType) {
      getTrendData();
    }
  });
};

/** 老人变更 */
const handleElderChange = () => {
  if (trendQuery.value.elderId && trendQuery.value.dataType) {
    getTrendData();
  }
};

/** 获取趋势数据 */
const getTrendData = async () => {
  if (!trendQuery.value.elderId || !trendQuery.value.dataType || !trendDateRange.value[0] || !trendDateRange.value[1]) {
    return;
  }
  
  trendQuery.value.startDate = trendDateRange.value[0];
  trendQuery.value.endDate = trendDateRange.value[1];
  
  try {
    const res = await getVitalSignsTrend(trendQuery.value);
    renderTrendChart(res.data);
  } catch (error) {
    console.error('获取趋势数据失败:', error);
  }
};

/** 渲染趋势图表 */
const renderTrendChart = (data: HealthVitalSignsTrendVO[]) => {
  const chartContainer = document.getElementById('trendChart');
  if (!chartContainer) return;
  
  // 这里可以集成ECharts或其他图表库
  // 示例代码，实际需要根据具体图表库实现
  const dates = data.map(item => item.date);
  const values = data.map(item => item.value);
  const abnormalDates = data.filter(item => item.isAbnormal).map(item => item.date);
  
  chartContainer.innerHTML = `
    <div style="display: flex; align-items: center; justify-content: center; height: 100%; background: #f5f5f5; border: 1px dashed #ccc;">
      <div style="text-align: center;">
        <p style="margin: 0; font-size: 16px; color: #666;">体征趋势图表</p>
        <p style="margin: 10px 0 0 0; font-size: 14px; color: #999;">
          数据类型: ${getDataTypeName(trendQuery.value.dataType)}<br>
          时间范围: ${trendQuery.value.startDate} 至 ${trendQuery.value.endDate}<br>
          数据点数: ${data.length}<br>
          异常数据: ${abnormalDates.length}
        </p>
        <p style="margin: 10px 0 0 0; font-size: 12px; color: #ccc;">
          (此处可集成ECharts等图表库)
        </p>
      </div>
    </div>
  `;
};

/** 获取数据类型名称 */
const getDataTypeName = (dataType: string) => {
  const typeMap: Record<string, string> = {
    temperature: '体温',
    heartRate: '心率',
    bloodPressure: '血压',
    bloodOxygen: '血氧',
    bloodSugar: '血糖'
  };
  return typeMap[dataType] || dataType;
};

/** 导出按钮操作 */
const handleExport = () => {
  const params = proxy?.addDateRange(queryParams.value, dateRange.value, 'measureTime');
  proxy?.download(
    'health/vitalSigns/export',
    params,
    `vital_signs_${new Date().getTime()}.xlsx`
  );
};

onMounted(() => {
  getList();
  getElderList();
  getDeviceList();
});
</script>

<style scoped>
.text-red-500 {
  color: #ef4444;
  font-weight: bold;
}

.el-tag {
  margin: 2px;
}
</style>