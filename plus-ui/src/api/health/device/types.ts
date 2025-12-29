/**
 * 设备查询参数
 */
export interface HealthDeviceQuery extends PageQuery {
  deviceCode?: string;
  deviceName?: string;
  deviceTypeId?: number | string;
  deviceModel?: string;
  manufacturer?: string;
  serialNumber?: string;
  bindUserId?: number | string;
  onlineStatus?: string;
  batteryLevel?: number;
}

/**
 * 设备VO
 */
export interface HealthDeviceVO extends BaseEntity {
  id: number | string;
  deviceCode: string;
  deviceName: string;
  deviceTypeId: number | string;
  deviceTypeName?: string;
  deviceModel?: string;
  manufacturer?: string;
  serialNumber?: string;
  bindUserId?: number | string;
  bindUserName?: string;
  bindTime?: string;
  onlineStatus: string;
  batteryLevel?: number;
  longitude?: number;
  latitude?: number;
  location?: string;
  status: string;
  remark?: string;
}

/**
 * 设备表单
 */
export interface HealthDeviceForm {
  id?: number | string;
  deviceCode?: string;
  deviceName: string;
  deviceTypeId: number | string;
  deviceModel?: string;
  manufacturer?: string;
  serialNumber?: string;
  onlineStatus?: string;
  batteryLevel?: number;
  longitude?: number;
  latitude?: number;
  location?: string;
  status?: string;
  remark?: string;
}

/**
 * 设备绑定表单
 */
export interface HealthDeviceBindForm {
  deviceId: number | string;
  elderId: number | string;
}