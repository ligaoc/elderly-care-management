import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { 
  HealthServiceStaffQuery, 
  HealthServiceStaffVO, 
  HealthServiceStaffForm 
} from './types';

/**
 * 查询服务人员列表
 */
export const listServiceStaff = (query?: HealthServiceStaffQuery): AxiosPromise<HealthServiceStaffVO[]> => {
  return request({
    url: '/health/serviceStaff/list',
    method: 'get',
    params: query
  });
};

/**
 * 获取服务人员详情
 */
export const getServiceStaff = (id: string | number): AxiosPromise<HealthServiceStaffVO> => {
  return request({
    url: '/health/serviceStaff/' + id,
    method: 'get'
  });
};

/**
 * 新增服务人员
 */
export const addServiceStaff = (data: HealthServiceStaffForm) => {
  return request({
    url: '/health/serviceStaff',
    method: 'post',
    data: data
  });
};

/**
 * 修改服务人员
 */
export const updateServiceStaff = (data: HealthServiceStaffForm) => {
  return request({
    url: '/health/serviceStaff',
    method: 'put',
    data: data
  });
};

/**
 * 删除服务人员
 */
export const delServiceStaff = (ids: Array<string | number> | string | number) => {
  return request({
    url: '/health/serviceStaff/' + ids,
    method: 'delete'
  });
};

export default {
  listServiceStaff,
  getServiceStaff,
  addServiceStaff,
  updateServiceStaff,
  delServiceStaff
};