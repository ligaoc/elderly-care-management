import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { 
  HealthDeviceQuery, 
  HealthDeviceVO, 
  HealthDeviceForm,
  HealthDeviceBindForm 
} from './types';

/**
 * 查询设备列表
 */
export const listDevice = (query?: HealthDeviceQuery): AxiosPromise<HealthDeviceVO[]> => {
  return request({
    url: '/health/device/list',
    method: 'get',
    params: query
  });
};

/**
 * 获取设备详情
 */
export const getDevice = (id: string | number): AxiosPromise<HealthDeviceVO> => {
  return request({
    url: '/health/device/' + id,
    method: 'get'
  });
};

/**
 * 新增设备
 */
export const addDevice = (data: HealthDeviceForm) => {
  return request({
    url: '/health/device',
    method: 'post',
    data: data
  });
};

/**
 * 修改设备
 */
export const updateDevice = (data: HealthDeviceForm) => {
  return request({
    url: '/health/device',
    method: 'put',
    data: data
  });
};

/**
 * 删除设备
 */
export const delDevice = (ids: Array<string | number> | string | number) => {
  return request({
    url: '/health/device/' + ids,
    method: 'delete'
  });
};

/**
 * 绑定设备
 */
export const bindDevice = (data: HealthDeviceBindForm) => {
  return request({
    url: '/health/device/bind',
    method: 'put',
    data: data
  });
};

/**
 * 解绑设备
 */
export const unbindDevice = (id: string | number) => {
  return request({
    url: '/health/device/unbind/' + id,
    method: 'put'
  });
};

/**
 * 导出设备
 */
export const exportDevice = (query?: HealthDeviceQuery) => {
  return request({
    url: '/health/device/export',
    method: 'post',
    params: query
  });
};

/**
 * 导入设备
 */
export const importDevice = (data: FormData) => {
  return request({
    url: '/health/device/import',
    method: 'post',
    data: data
  });
};

export default {
  listDevice,
  getDevice,
  addDevice,
  updateDevice,
  delDevice,
  bindDevice,
  unbindDevice,
  exportDevice,
  importDevice
};