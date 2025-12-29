import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { 
  HealthElderQuery, 
  HealthElderVO, 
  HealthElderForm 
} from './types';

/**
 * 查询老人列表
 */
export const listElder = (query?: HealthElderQuery): AxiosPromise<HealthElderVO[]> => {
  return request({
    url: '/health/elder/list',
    method: 'get',
    params: query
  });
};

/**
 * 获取老人详情
 */
export const getElder = (id: string | number): AxiosPromise<HealthElderVO> => {
  return request({
    url: '/health/elder/' + id,
    method: 'get'
  });
};

/**
 * 新增老人
 */
export const addElder = (data: HealthElderForm) => {
  return request({
    url: '/health/elder',
    method: 'post',
    data: data
  });
};

/**
 * 修改老人
 */
export const updateElder = (data: HealthElderForm) => {
  return request({
    url: '/health/elder',
    method: 'put',
    data: data
  });
};

/**
 * 删除老人
 */
export const delElder = (ids: Array<string | number> | string | number) => {
  return request({
    url: '/health/elder/' + ids,
    method: 'delete'
  });
};

/**
 * 导出老人
 */
export const exportElder = (query?: HealthElderQuery) => {
  return request({
    url: '/health/elder/export',
    method: 'post',
    params: query
  });
};

/**
 * 导入老人
 */
export const importElder = (data: FormData) => {
  return request({
    url: '/health/elder/import',
    method: 'post',
    data: data
  });
};

export default {
  listElder,
  getElder,
  addElder,
  updateElder,
  delElder,
  exportElder,
  importElder
};