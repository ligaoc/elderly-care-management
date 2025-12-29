import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { 
  DashboardStatistics, 
  DeviceOnlineRate, 
  AlertTrendItem, 
  AlertTypeDistribution, 
  DeviceDistribution 
} from './types';
import { HealthDeviceAlertVO } from '../alert/types';

/**
 * 获取统计数据
 */
export const getStatistics = (): AxiosPromise<DashboardStatistics> => {
  return request({
    url: '/health/dashboard/statistics',
    method: 'get'
  });
};

/**
 * 获取设备在线率
 */
export const getDeviceOnlineRate = (): AxiosPromise<DeviceOnlineRate> => {
  return request({
    url: '/health/dashboard/deviceOnlineRate',
    method: 'get'
  });
};

/**
 * 获取告警趋势(7天)
 */
export const getAlertTrend = (): AxiosPromise<AlertTrendItem[]> => {
  return request({
    url: '/health/dashboard/alertTrend',
    method: 'get'
  });
};

/**
 * 获取告警类型分布
 */
export const getAlertTypeDistribution = (): AxiosPromise<AlertTypeDistribution[]> => {
  return request({
    url: '/health/dashboard/alertTypeDistribution',
    method: 'get'
  });
};

/**
 * 获取最新告警列表
 */
export const getLatestAlerts = (): AxiosPromise<HealthDeviceAlertVO[]> => {
  return request({
    url: '/health/dashboard/latestAlerts',
    method: 'get'
  });
};

/**
 * 获取设备分布数据
 */
export const getDeviceDistribution = (): AxiosPromise<DeviceDistribution[]> => {
  return request({
    url: '/health/dashboard/deviceDistribution',
    method: 'get'
  });
};

export default {
  getStatistics,
  getDeviceOnlineRate,
  getAlertTrend,
  getAlertTypeDistribution,
  getLatestAlerts,
  getDeviceDistribution
};