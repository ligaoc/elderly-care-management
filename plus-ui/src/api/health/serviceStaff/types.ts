/**
 * 服务人员查询参数
 */
export interface HealthServiceStaffQuery extends PageQuery {
  staffCode?: string;
  name?: string;
  gender?: string;
  phone?: string;
  staffType?: string;
  orgId?: number | string;
  status?: string;
}

/**
 * 服务人员VO
 */
export interface HealthServiceStaffVO extends BaseEntity {
  id: number | string;
  staffCode: string;
  name: string;
  gender: string;
  phone?: string;
  staffType: string;
  orgId?: number | string;
  orgName?: string;
  workTime?: string;
  specialties?: string;
  status: string;
  remark?: string;
}

/**
 * 服务人员表单
 */
export interface HealthServiceStaffForm {
  id?: number | string;
  staffCode?: string;
  name: string;
  gender: string;
  phone?: string;
  staffType: string;
  orgId?: number | string;
  workTime?: string;
  specialties?: string;
  status?: string;
  remark?: string;
}