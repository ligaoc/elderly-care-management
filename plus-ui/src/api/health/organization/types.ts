/**
 * 组织查询参数
 */
export interface HealthOrganizationQuery extends PageQuery {
  orgName?: string;
  orgCode?: string;
  orgType?: string;
  leader?: string;
  phone?: string;
  status?: string;
}

/**
 * 组织VO
 */
export interface HealthOrganizationVO extends BaseEntity {
  id: number | string;
  parentId: number | string;
  orgCode: string;
  orgName: string;
  orgType: string;
  leader?: string;
  phone?: string;
  email?: string;
  address?: string;
  sortOrder?: number;
  status: string;
  remark?: string;
  children?: HealthOrganizationVO[];
}

/**
 * 组织树VO
 */
export interface HealthOrganizationTreeVO {
  id: number | string;
  parentId: number | string;
  name: string;
  weight: number;
  orgCode: string;
  orgType: string;
  leader?: string;
  status: string;
  disabled: boolean;
  children?: HealthOrganizationTreeVO[];
}

/**
 * 组织表单
 */
export interface HealthOrganizationForm {
  id?: number | string;
  parentId?: number | string;
  orgCode?: string;
  orgName: string;
  orgType: string;
  leader?: string;
  phone?: string;
  email?: string;
  address?: string;
  sortOrder?: number;
  status?: string;
  remark?: string;
}