import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { 
  HealthEmergencyContactQuery, 
  HealthEmergencyContactVO, 
  HealthEmergencyContactForm 
} from './types';

/**
 * 查询紧急联系人列表
 */
export const listEmergencyContact = (elderId: string | number): AxiosPromise<HealthEmergencyContactVO[]> => {
  return request({
    url: '/health/emergencyContact/list/' + elderId,
    method: 'get'
  });
};

/**
 * 获取紧急联系人详情
 */
export const getEmergencyContact = (id: string | number): AxiosPromise<HealthEmergencyContactVO> => {
  return request({
    url: '/health/emergencyContact/' + id,
    method: 'get'
  });
};

/**
 * 新增紧急联系人
 */
export const addEmergencyContact = (data: HealthEmergencyContactForm) => {
  return request({
    url: '/health/emergencyContact',
    method: 'post',
    data: data
  });
};

/**
 * 修改紧急联系人
 */
export const updateEmergencyContact = (data: HealthEmergencyContactForm) => {
  return request({
    url: '/health/emergencyContact',
    method: 'put',
    data: data
  });
};

/**
 * 删除紧急联系人
 */
export const delEmergencyContact = (ids: Array<string | number> | string | number) => {
  return request({
    url: '/health/emergencyContact/' + ids,
    method: 'delete'
  });
};

export default {
  listEmergencyContact,
  getEmergencyContact,
  addEmergencyContact,
  updateEmergencyContact,
  delEmergencyContact
};