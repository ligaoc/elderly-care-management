import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { 
  HealthDeviceTypeQuery, 
  HealthDeviceTypeVO, 
  HealthDeviceTypeTreeVO, 
  HealthDeviceTypeForm 
} from './types';

/**
 * 查询设备类型列表
 */
export const listDeviceType = (query?: HealthDeviceTypeQuery): AxiosPromise<HealthDeviceTypeVO[]> => {
  return request({
    url: '/health/deviceType/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询设备类型树结构
 */
export const treeDeviceType = (query?: HealthDeviceTypeQuery): AxiosPromise<HealthDeviceTypeTreeVO[]> => {
  return request({
    url: '/health/deviceType/tree',
    method: 'get',
    params: query
  });
};

/**
 * 获取设备类型详情
 */
export const getDeviceType = (id: string | number): AxiosPromise<HealthDeviceTypeVO> => {
  return request({
    url: '/health/deviceType/' + id,
    method: 'get'
  });
};

/**
 * 新增设备类型
 */
export const addDeviceType = (data: HealthDeviceTypeForm) => {
  return request({
    url: '/health/deviceType',
    method: 'post',
    data: data
  });
};

/**
 * 修改设备类型
 */
export const updateDeviceType = (data: HealthDeviceTypeForm) => {
  return request({
    url: '/health/deviceType',
    method: 'put',
    data: data
  });
};

/**
 * 删除设备类型
 */
export const delDeviceType = (ids: Array<string | number> | string | number) => {
  return request({
    url: '/health/deviceType/' + ids,
    method: 'delete'
  });
};

export default {
  listDeviceType,
  treeDeviceType,
  getDeviceType,
  addDeviceType,
  updateDeviceType,
  delDeviceType
};