/**
 * 告警查询参数
 */
export interface HealthDeviceAlertQuery extends PageQuery {
  alertCode?: string;
  deviceId?: number | string;
  userId?: number | string;
  alertType?: string;
  alertLevel?: string;
  handleStatus?: string;
  alertTimeStart?: string;
  alertTimeEnd?: string;
}

/**
 * 告警VO
 */
export interface HealthDeviceAlertVO extends BaseEntity {
  id: number | string;
  alertCode: string;
  deviceId: number | string;
  deviceName?: string;
  userId?: number | string;
  userName?: string;
  alertType: string;
  alertLevel: string;
  alertTitle: string;
  alertContent?: string;
  alertTime: string;
  handleStatus: string;
  handleUserId?: number | string;
  handleUserName?: string;
  handleTime?: string;
  handleResult?: string;
  longitude?: number;
  latitude?: number;
  location?: string;
}

/**
 * 告警处理表单
 */
export interface HealthDeviceAlertHandleForm {
  id: number | string;
  handleResult: string;
}