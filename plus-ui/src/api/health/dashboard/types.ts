/**
 * 大屏统计数据
 */
export interface DashboardStatistics {
  deviceTotal: number;
  deviceOnline: number;
  elderTotal: number;
  alertToday: number;
  alertPending: number;
}

/**
 * 设备在线率
 */
export interface DeviceOnlineRate {
  total: number;
  online: number;
  offline: number;
  rate: number;
}

/**
 * 告警趋势数据
 */
export interface AlertTrendItem {
  date: string;
  count: number;
}

/**
 * 告警类型分布
 */
export interface AlertTypeDistribution {
  type: string;
  name: string;
  count: number;
}

/**
 * 设备分布数据
 */
export interface DeviceDistribution {
  id: number;
  name: string;
  lng: number;
  lat: number;
  online: boolean;
  location: string;
}