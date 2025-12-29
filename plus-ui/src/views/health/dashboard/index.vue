<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb-4">
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-item">
            <div class="stat-icon device-icon">
              <el-icon><Monitor /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.deviceTotal || 0 }}</div>
              <div class="stat-label">设备总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-item">
            <div class="stat-icon online-icon">
              <el-icon><Connection /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.deviceOnline || 0 }}</div>
              <div class="stat-label">在线设备</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-item">
            <div class="stat-icon elder-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.elderTotal || 0 }}</div>
              <div class="stat-label">老人总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-item">
            <div class="stat-icon alert-icon">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.alertToday || 0 }}</div>
              <div class="stat-label">今日告警</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="mb-4">
      <!-- 设备在线率 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>设备在线率</span>
              <el-button link type="primary" @click="refreshOnlineRate">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
          <div ref="onlineRateChartRef" class="chart-container"></div>
          <div class="chart-info">
            <div class="info-item">
              <span class="info-label">在线率:</span>
              <span class="info-value">{{ onlineRate.rate || 0 }}%</span>
            </div>
            <div class="info-item">
              <span class="info-label">在线:</span>
              <span class="info-value online">{{ onlineRate.online || 0 }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">离线:</span>
              <span class="info-value offline">{{ onlineRate.offline || 0 }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 告警趋势 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>告警趋势(7天)</span>
              <el-button link type="primary" @click="refreshAlertTrend">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
          <div ref="alertTrendChartRef" class="chart-container-full"></div>
        </el-card>
      </el-col>

      <!-- 告警类型分布 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>告警类型分布</span>
              <el-button link type="primary" @click="refreshAlertDistribution">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
          <div ref="alertDistributionChartRef" class="chart-container-full"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部区域 -->
    <el-row :gutter="20">
      <!-- 最新告警 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" class="list-card">
          <template #header>
            <div class="card-header">
              <span>最新告警</span>
              <el-button link type="primary" @click="refreshLatestAlerts">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
          <div class="alert-list">
            <div v-if="latestAlerts.length === 0" class="empty-data">
              <el-empty description="暂无告警数据" />
            </div>
            <div v-else>
              <div v-for="alert in latestAlerts" :key="alert.id" class="alert-item">
                <div class="alert-level">
                  <el-tag v-if="alert.alertLevel === '3'" type="danger" size="small">紧急</el-tag>
                  <el-tag v-else-if="alert.alertLevel === '2'" type="warning" size="small">重要</el-tag>
                  <el-tag v-else type="info" size="small">一般</el-tag>
                </div>
                <div class="alert-content">
                  <div class="alert-title">{{ alert.alertTitle }}</div>
                  <div class="alert-info">
                    <span class="alert-time">{{ formatTime(alert.alertTime) }}</span>
                  </div>
                </div>
                <div class="alert-status">
                  <el-tag v-if="alert.handleStatus === '0'" type="danger" size="small">待处理</el-tag>
                  <el-tag v-else-if="alert.handleStatus === '1'" type="warning" size="small">处理中</el-tag>
                  <el-tag v-else-if="alert.handleStatus === '2'" type="success" size="small">已处理</el-tag>
                  <el-tag v-else type="info" size="small">已忽略</el-tag>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 设备分布 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" class="map-card">
          <template #header>
            <div class="card-header">
              <span>设备分布</span>
              <el-button link type="primary" @click="refreshDeviceDistribution">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
          <div ref="deviceDistributionChartRef" class="map-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 自动刷新控制 -->
    <div class="refresh-control">
      <el-switch
        v-model="autoRefresh"
        active-text="自动刷新"
        inactive-text="手动刷新"
        @change="handleAutoRefreshChange"
      />
      <span v-if="autoRefresh" class="refresh-countdown">{{ countdown }}s后刷新</span>
    </div>
  </div>
</template>

<script setup name="HealthDashboard" lang="ts">
import * as echarts from 'echarts';
import { 
  getStatistics,
  getDeviceOnlineRate,
  getAlertTrend,
  getAlertTypeDistribution,
  getLatestAlerts,
  getDeviceDistribution
} from '@/api/health/dashboard';
import { 
  DashboardStatistics,
  DeviceOnlineRate,
  AlertTrendItem,
  AlertTypeDistribution,
  DeviceDistribution
} from '@/api/health/dashboard/types';
import { HealthDeviceAlertVO } from '@/api/health/alert/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

// Chart DOM refs
const onlineRateChartRef = ref<HTMLElement>();
const alertTrendChartRef = ref<HTMLElement>();
const alertDistributionChartRef = ref<HTMLElement>();
const deviceDistributionChartRef = ref<HTMLElement>();

// Chart instances
let onlineRateChart: echarts.ECharts | null = null;
let alertTrendChart: echarts.ECharts | null = null;
let alertDistributionChart: echarts.ECharts | null = null;
let deviceDistributionChart: echarts.ECharts | null = null;

// Data state
const statistics = ref<DashboardStatistics>({
  deviceTotal: 0,
  deviceOnline: 0,
  elderTotal: 0,
  alertToday: 0,
  alertPending: 0
});

const onlineRate = ref<DeviceOnlineRate>({
  total: 0,
  online: 0,
  offline: 0,
  rate: 0
});

const alertTrend = ref<AlertTrendItem[]>([]);
const alertDistribution = ref<AlertTypeDistribution[]>([]);
const latestAlerts = ref<HealthDeviceAlertVO[]>([]);
const deviceDistribution = ref<DeviceDistribution[]>([]);

// Auto refresh control
const autoRefresh = ref(true);
const countdown = ref(30);
let refreshTimer: ReturnType<typeof setInterval> | null = null;
let countdownTimer: ReturnType<typeof setInterval> | null = null;

const getStatisticsData = async () => {
  try {
    const res = await getStatistics();
    statistics.value = res.data;
  } catch (error) {
    console.error('Failed to get statistics:', error);
  }
};

const getOnlineRateData = async () => {
  try {
    const res = await getDeviceOnlineRate();
    onlineRate.value = res.data;
    renderOnlineRateChart();
  } catch (error) {
    console.error('Failed to get online rate:', error);
  }
};

const getAlertTrendData = async () => {
  try {
    const res = await getAlertTrend();
    alertTrend.value = res.data;
    renderAlertTrendChart();
  } catch (error) {
    console.error('Failed to get alert trend:', error);
  }
};

const getAlertDistributionData = async () => {
  try {
    const res = await getAlertTypeDistribution();
    alertDistribution.value = res.data;
    renderAlertDistributionChart();
  } catch (error) {
    console.error('Failed to get alert distribution:', error);
  }
};

const getLatestAlertsData = async () => {
  try {
    const res = await getLatestAlerts();
    latestAlerts.value = res.data;
  } catch (error) {
    console.error('Failed to get latest alerts:', error);
  }
};

const getDeviceDistributionData = async () => {
  try {
    const res = await getDeviceDistribution();
    deviceDistribution.value = res.data;
    renderDeviceDistributionChart();
  } catch (error) {
    console.error('Failed to get device distribution:', error);
  }
};

const renderOnlineRateChart = () => {
  if (!onlineRateChartRef.value) return;
  if (!onlineRateChart) {
    onlineRateChart = echarts.init(onlineRateChartRef.value);
  }
  const option: echarts.EChartsOption = {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    series: [{
      name: '设备状态',
      type: 'pie',
      radius: ['50%', '70%'],
      center: ['50%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      label: {
        show: true,
        position: 'center',
        formatter: () => `${onlineRate.value.rate}%\n在线率`,
        fontSize: 16,
        fontWeight: 'bold',
        color: '#409EFF'
      },
      data: [
        { value: onlineRate.value.online, name: '在线', itemStyle: { color: '#67C23A' } },
        { value: onlineRate.value.offline, name: '离线', itemStyle: { color: '#F56C6C' } }
      ]
    }]
  };
  onlineRateChart.setOption(option);
};

const renderAlertTrendChart = () => {
  if (!alertTrendChartRef.value) return;
  if (!alertTrendChart) {
    alertTrendChart = echarts.init(alertTrendChartRef.value);
  }
  const dates = alertTrend.value.map(item => item.date);
  const counts = alertTrend.value.map(item => item.count);
  const option: echarts.EChartsOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross', label: { backgroundColor: '#6a7985' } }
    },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLine: { lineStyle: { color: '#E4E7ED' } },
      axisLabel: { color: '#606266' }
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: { lineStyle: { color: '#E4E7ED', type: 'dashed' } },
      axisLabel: { color: '#606266' }
    },
    series: [{
      name: '告警数',
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      lineStyle: { color: '#F56C6C', width: 3 },
      itemStyle: { color: '#F56C6C' },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(245, 108, 108, 0.3)' },
          { offset: 1, color: 'rgba(245, 108, 108, 0.05)' }
        ])
      },
      data: counts
    }]
  };
  alertTrendChart.setOption(option);
};

const renderAlertDistributionChart = () => {
  if (!alertDistributionChartRef.value) return;
  if (!alertDistributionChart) {
    alertDistributionChart = echarts.init(alertDistributionChartRef.value);
  }
  const colors = ['#F56C6C', '#E6A23C', '#409EFF', '#909399', '#67C23A'];
  const data = alertDistribution.value.map((item, index) => ({
    value: item.count,
    name: item.name,
    itemStyle: { color: colors[index % colors.length] }
  }));
  const option: echarts.EChartsOption = {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: {
      orient: 'vertical',
      right: '5%',
      top: 'center',
      itemWidth: 10,
      itemHeight: 10,
      textStyle: { fontSize: 12, color: '#606266' }
    },
    series: [{
      name: '告警类型',
      type: 'pie',
      radius: ['40%', '65%'],
      center: ['35%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
      label: { show: false },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      data: data
    }]
  };
  alertDistributionChart.setOption(option);
};

const renderDeviceDistributionChart = () => {
  if (!deviceDistributionChartRef.value) return;
  if (!deviceDistributionChart) {
    deviceDistributionChart = echarts.init(deviceDistributionChartRef.value);
  }
  const onlineDevices = deviceDistribution.value.filter(d => d.online);
  const offlineDevices = deviceDistribution.value.filter(d => !d.online);
  const option: echarts.EChartsOption = {
    tooltip: {
      trigger: 'item',
      formatter: (params: any) => `${params.data.name}<br/>状态: ${params.seriesName}<br/>位置: ${params.data.location || '未知'}`
    },
    legend: { data: ['在线设备', '离线设备'], bottom: 10, itemWidth: 10, itemHeight: 10 },
    grid: { left: '3%', right: '3%', bottom: '15%', top: '5%', containLabel: true },
    xAxis: {
      type: 'value',
      name: '经度',
      nameLocation: 'middle',
      nameGap: 25,
      min: 121.0,
      max: 122.0,
      axisLine: { lineStyle: { color: '#E4E7ED' } },
      splitLine: { lineStyle: { color: '#E4E7ED', type: 'dashed' } },
      axisLabel: { color: '#909399', fontSize: 10 }
    },
    yAxis: {
      type: 'value',
      name: '纬度',
      nameLocation: 'middle',
      nameGap: 35,
      min: 31.0,
      max: 31.5,
      axisLine: { lineStyle: { color: '#E4E7ED' } },
      splitLine: { lineStyle: { color: '#E4E7ED', type: 'dashed' } },
      axisLabel: { color: '#909399', fontSize: 10 }
    },
    series: [
      {
        name: '在线设备',
        type: 'scatter',
        symbolSize: 15,
        itemStyle: { color: '#67C23A', shadowBlur: 10, shadowColor: 'rgba(103, 194, 58, 0.5)' },
        data: onlineDevices.map(d => ({ value: [d.lng || 121.5, d.lat || 31.2], name: d.name, location: d.location }))
      },
      {
        name: '离线设备',
        type: 'scatter',
        symbolSize: 15,
        itemStyle: { color: '#F56C6C', shadowBlur: 10, shadowColor: 'rgba(245, 108, 108, 0.5)' },
        data: offlineDevices.map(d => ({ value: [d.lng || 121.5, d.lat || 31.2], name: d.name, location: d.location }))
      }
    ]
  };
  deviceDistributionChart.setOption(option);
};

const formatTime = (time: string) => {
  if (!time) return '';
  const date = new Date(time);
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  if (diff < 60000) return '刚刚';
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前';
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前';
  return proxy?.parseTime(time, '{m}-{d} {h}:{i}') || time;
};

const refreshAllData = async () => {
  await Promise.all([
    getStatisticsData(),
    getOnlineRateData(),
    getAlertTrendData(),
    getAlertDistributionData(),
    getLatestAlertsData(),
    getDeviceDistributionData()
  ]);
};

const refreshOnlineRate = () => getOnlineRateData();
const refreshAlertTrend = () => getAlertTrendData();
const refreshAlertDistribution = () => getAlertDistributionData();
const refreshLatestAlerts = () => getLatestAlertsData();
const refreshDeviceDistribution = () => getDeviceDistributionData();

const handleAutoRefreshChange = (value: boolean) => {
  value ? startAutoRefresh() : stopAutoRefresh();
};

const startAutoRefresh = () => {
  countdown.value = 30;
  countdownTimer = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) countdown.value = 30;
  }, 1000);
  refreshTimer = setInterval(() => refreshAllData(), 30000);
};

const stopAutoRefresh = () => {
  if (refreshTimer) { clearInterval(refreshTimer); refreshTimer = null; }
  if (countdownTimer) { clearInterval(countdownTimer); countdownTimer = null; }
};

const handleResize = () => {
  onlineRateChart?.resize();
  alertTrendChart?.resize();
  alertDistributionChart?.resize();
  deviceDistributionChart?.resize();
};

onMounted(() => {
  refreshAllData();
  if (autoRefresh.value) startAutoRefresh();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  stopAutoRefresh();
  window.removeEventListener('resize', handleResize);
  onlineRateChart?.dispose();
  alertTrendChart?.dispose();
  alertDistributionChart?.dispose();
  deviceDistributionChart?.dispose();
});
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
}

.stat-card { height: 120px; margin-bottom: 20px; }
.stat-item { display: flex; align-items: center; height: 100%; }
.stat-icon {
  width: 60px; height: 60px; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 24px; color: white; margin-right: 20px;
}
.device-icon { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.online-icon { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
.elder-icon { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.alert-icon { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
.stat-content { flex: 1; }
.stat-value { font-size: 32px; font-weight: bold; color: #303133; line-height: 1; margin-bottom: 8px; }
.stat-label { font-size: 14px; color: #909399; }

.chart-card { height: 350px; margin-bottom: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; font-weight: bold; }
.chart-container { height: 180px; width: 100%; }
.chart-container-full { height: 260px; width: 100%; }
.chart-info { display: flex; justify-content: space-around; margin-top: 10px; padding-top: 10px; border-top: 1px solid #ebeef5; }
.info-item { text-align: center; }
.info-label { font-size: 12px; color: #909399; display: block; margin-bottom: 5px; }
.info-value { font-size: 18px; font-weight: bold; color: #303133; }
.info-value.online { color: #67c23a; }
.info-value.offline { color: #f56c6c; }

.list-card { height: 400px; margin-bottom: 20px; }
.alert-list { height: 320px; overflow-y: auto; }
.empty-data { height: 100%; display: flex; align-items: center; justify-content: center; }
.alert-item { display: flex; align-items: center; padding: 12px 0; border-bottom: 1px solid #ebeef5; }
.alert-item:last-child { border-bottom: none; }
.alert-level { width: 60px; flex-shrink: 0; }
.alert-content { flex: 1; margin: 0 15px; }
.alert-title { font-size: 14px; color: #303133; margin-bottom: 5px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.alert-info { font-size: 12px; color: #909399; }
.alert-status { width: 70px; flex-shrink: 0; text-align: right; }

.map-card { height: 400px; margin-bottom: 20px; }
.map-container { height: 320px; width: 100%; }

.refresh-control {
  position: fixed; bottom: 20px; right: 20px;
  background: white; padding: 10px 15px; border-radius: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex; align-items: center; gap: 10px;
}
.refresh-countdown { font-size: 12px; color: #909399; }

@media (max-width: 768px) {
  .dashboard-container { padding: 10px; }
  .stat-value { font-size: 24px; }
  .stat-icon { width: 50px; height: 50px; font-size: 20px; margin-right: 15px; }
  .chart-card, .list-card, .map-card { height: auto; min-height: 300px; }
}
</style>
