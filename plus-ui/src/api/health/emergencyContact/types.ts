/**
 * 紧急联系人查询参数
 */
export interface HealthEmergencyContactQuery extends PageQuery {
  elderId?: number | string;
  name?: string;
  relation?: string;
  phone?: string;
  isPrimary?: string;
}

/**
 * 紧急联系人VO
 */
export interface HealthEmergencyContactVO extends BaseEntity {
  id: number | string;
  elderId: number | string;
  elderName?: string;
  name: string;
  relation: string;
  phone: string;
  isPrimary: string;
  notifyType?: string;
  address?: string;
  remark?: string;
}

/**
 * 紧急联系人表单
 */
export interface HealthEmergencyContactForm {
  id?: number | string;
  elderId: number | string;
  name: string;
  relation: string;
  phone: string;
  isPrimary?: string;
  notifyType?: string;
  address?: string;
  remark?: string;
}