/**
 * 设备类型查询参数
 */
export interface HealthDeviceTypeQuery extends PageQuery {
  typeName?: string;
  typeCode?: string;
  category?: string;
  status?: string;
}

/**
 * 设备类型VO
 */
export interface HealthDeviceTypeVO extends BaseEntity {
  id: number | string;
  parentId: number | string;
  typeName: string;
  typeCode: string;
  category: string;
  icon?: string;
  sortOrder?: number;
  status: string;
  remark?: string;
  children?: HealthDeviceTypeVO[];
}

/**
 * 设备类型树VO
 */
export interface HealthDeviceTypeTreeVO {
  id: number | string;
  parentId: number | string;
  name: string;
  weight: number;
  typeCode: string;
  category: string;
  icon?: string;
  status: string;
  disabled: boolean;
  children?: HealthDeviceTypeTreeVO[];
}

/**
 * 设备类型表单
 */
export interface HealthDeviceTypeForm {
  id?: number | string;
  parentId?: number | string;
  typeName: string;
  typeCode: string;
  category: string;
  icon?: string;
  sortOrder?: number;
  status?: string;
  remark?: string;
}