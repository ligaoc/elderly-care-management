/**
 * 体征数据查询参数
 */
export interface HealthVitalSignsQuery extends PageQuery {
  elderId?: number | string;
  deviceId?: number | string;
  measureTimeStart?: string;
  measureTimeEnd?: string;
  isAbnormal?: string;
  temperatureMin?: number;
  temperatureMax?: number;
  heartRateMin?: number;
  heartRateMax?: number;
  systolicPressureMin?: number;
  systolicPressureMax?: number;
  diastolicPressureMin?: number;
  diastolicPressureMax?: number;
  bloodOxygenMin?: number;
  bloodOxygenMax?: number;
  bloodSugarMin?: number;
  bloodSugarMax?: number;
}

/**
 * 体征数据VO
 */
export interface HealthVitalSignsVO extends BaseEntity {
  id: number | string;
  elderId: number | string;
  elderName?: string;
  deviceId?: number | string;
  deviceName?: string;
  measureTime: string;
  temperature?: number;
  heartRate?: number;
  systolicPressure?: number;
  diastolicPressure?: number;
  bloodOxygen?: number;
  bloodSugar?: number;
  isAbnormal: string;
  abnormalItems?: string;
  remark?: string;
}

/**
 * 体征数据表单
 */
export interface HealthVitalSignsForm {
  id?: number | string;
  elderId: number | string;
  deviceId?: number | string;
  measureTime?: string;
  temperature?: number;
  heartRate?: number;
  systolicPressure?: number;
  diastolicPressure?: number;
  bloodOxygen?: number;
  bloodSugar?: number;
  remark?: string;
}

/**
 * 体征趋势查询参数
 */
export interface HealthVitalSignsTrendQuery {
  elderId: number | string;
  startDate: string;
  endDate: string;
  dataType: string; // temperature, heartRate, bloodPressure, bloodOxygen, bloodSugar
}

/**
 * 体征趋势数据
 */
export interface HealthVitalSignsTrendVO {
  date: string;
  value: number;
  isAbnormal: boolean;
}