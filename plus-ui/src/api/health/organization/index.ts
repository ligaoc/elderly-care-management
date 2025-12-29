import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { 
  HealthOrganizationQuery, 
  HealthOrganizationVO, 
  HealthOrganizationTreeVO, 
  HealthOrganizationForm 
} from './types';

/**
 * 查询组织列表
 */
export const listOrganization = (query?: HealthOrganizationQuery): AxiosPromise<HealthOrganizationVO[]> => {
  return request({
    url: '/health/organization/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询组织树结构
 */
export const treeOrganization = (query?: HealthOrganizationQuery): AxiosPromise<HealthOrganizationTreeVO[]> => {
  return request({
    url: '/health/organization/tree',
    method: 'get',
    params: query
  });
};

/**
 * 获取组织详情
 */
export const getOrganization = (id: string | number): AxiosPromise<HealthOrganizationVO> => {
  return request({
    url: '/health/organization/' + id,
    method: 'get'
  });
};

/**
 * 新增组织
 */
export const addOrganization = (data: HealthOrganizationForm) => {
  return request({
    url: '/health/organization',
    method: 'post',
    data: data
  });
};

/**
 * 修改组织
 */
export const updateOrganization = (data: HealthOrganizationForm) => {
  return request({
    url: '/health/organization',
    method: 'put',
    data: data
  });
};

/**
 * 删除组织
 */
export const delOrganization = (ids: Array<string | number> | string | number) => {
  return request({
    url: '/health/organization/' + ids,
    method: 'delete'
  });
};

export default {
  listOrganization,
  treeOrganization,
  getOrganization,
  addOrganization,
  updateOrganization,
  delOrganization
};