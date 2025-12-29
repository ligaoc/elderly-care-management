import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { 
  HealthVitalSignsQuery, 
  HealthVitalSignsVO, 
  HealthVitalSignsForm,
  HealthVitalSignsTrendQuery,
  HealthVitalSignsTrendVO 
} from './types';

/**
 * 查询体征数据列表
 */
export const listVitalSigns = (query?: HealthVitalSignsQuery): AxiosPromise<HealthVitalSignsVO[]> => {
  return request({
    url: '/health/vitalSigns/list',
    method: 'get',
    params: query
  });
};

/**
 * 获取体征数据详情
 */
export const getVitalSigns = (id: string | number): AxiosPromise<HealthVitalSignsVO> => {
  return request({
    url: '/health/vitalSigns/' + id,
    method: 'get'
  });
};

/**
 * 获取体征趋势数据
 */
export const getVitalSignsTrend = (query: HealthVitalSignsTrendQuery): AxiosPromise<HealthVitalSignsTrendVO[]> => {
  return request({
    url: '/health/vitalSigns/trend',
    method: 'get',
    params: query
  });
};

/**
 * 新增体征数据
 */
export const addVitalSigns = (data: HealthVitalSignsForm) => {
  return request({
    url: '/health/vitalSigns',
    method: 'post',
    data: data
  });
};

/**
 * 导出体征数据
 */
export const exportVitalSigns = (query?: HealthVitalSignsQuery) => {
  return request({
    url: '/health/vitalSigns/export',
    method: 'post',
    params: query
  });
};

export default {
  listVitalSigns,
  getVitalSigns,
  getVitalSignsTrend,
  addVitalSigns,
  exportVitalSigns
};