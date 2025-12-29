/**
 * 老人查询参数
 */
export interface HealthElderQuery extends PageQuery {
  userCode?: string;
  name?: string;
  gender?: string;
  phone?: string;
  orgId?: number | string;
  careLevel?: string;
  riskLevel?: string;
  ageStart?: number;
  ageEnd?: number;
}

/**
 * 老人VO
 */
export interface HealthElderVO extends BaseEntity {
  id: number | string;
  userCode: string;
  name: string;
  gender: string;
  birthday: string;
  age: number;
  phone?: string;
  idCard?: string;
  orgId?: number | string;
  orgName?: string;
  height?: number;
  weight?: number;
  bmi?: number;
  medicalHistory?: string;
  careLevel?: string;
  riskLevel?: string;
  address?: string;
  status: string;
  remark?: string;
}

/**
 * 老人表单
 */
export interface HealthElderForm {
  id?: number | string;
  userCode?: string;
  name: string;
  gender: string;
  birthday: string;
  phone?: string;
  idCard?: string;
  orgId?: number | string;
  height?: number;
  weight?: number;
  medicalHistory?: string;
  careLevel?: string;
  riskLevel?: string;
  address?: string;
  status?: string;
  remark?: string;
}