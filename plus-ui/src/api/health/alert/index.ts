import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { 
  HealthDeviceAlertQuery, 
  HealthDeviceAlertVO,
  HealthDeviceAlertHandleForm 
} from './types';

/**
 * 查询告警列表
 */
export const listAlert = (query?: HealthDeviceAlertQuery): AxiosPromise<HealthDeviceAlertVO[]> => {
  return request({
    url: '/health/alert/list',
    method: 'get',
    params: query
  });
};

/**
 * 获取告警详情
 */
export const getAlert = (id: string | number): AxiosPromise<HealthDeviceAlertVO> => {
  return request({
    url: '/health/alert/' + id,
    method: 'get'
  });
};

/**
 * 处理告警
 */
export const handleAlert = (data: HealthDeviceAlertHandleForm) => {
  return request({
    url: '/health/alert/handle',
    method: 'put',
    data: data
  });
};

/**
 * 忽略告警
 */
export const ignoreAlert = (id: string | number) => {
  return request({
    url: '/health/alert/ignore/' + id,
    method: 'put'
  });
};

/**
 * 导出告警
 */
export const exportAlert = (query?: HealthDeviceAlertQuery) => {
  return request({
    url: '/health/alert/export',
    method: 'post',
    params: query
  });
};

export default {
  listAlert,
  getAlert,
  handleAlert,
  ignoreAlert,
  exportAlert
};