# Design Document

## Overview

健康管理后台系统是基于RuoYi-Vue-Plus框架开发的老人健康监护管理平台。系统采用前后端分离架构，后端使用Spring Boot 3.5 + MyBatis-Plus，前端使用Vue3 + TypeScript + Element Plus。系统支持多租户架构，提供设备管理、用户管理、体征监测、告警处理等核心功能。

### 技术架构

```
┌─────────────────────────────────────────────────────────────────┐
│                         前端 (plus-ui)                           │
│  Vue3 + TypeScript + Element Plus + ECharts + 高德地图           │
├─────────────────────────────────────────────────────────────────┤
│                         后端 (ruoyi-modules)                     │
│  Spring Boot 3.5 + MyBatis-Plus + Sa-Token + Redis              │
├─────────────────────────────────────────────────────────────────┤
│                         数据层                                   │
│  MySQL 8.0 + Redis 7.0                                          │
└─────────────────────────────────────────────────────────────────┘
```

### 模块划分

新增业务模块 `ruoyi-health`，包含以下子模块：
- 设备管理 (device)
- 告警管理 (alert)
- 组织管理 (organization)
- 老人管理 (elder)
- 体征管理 (vital)
- 服务人员管理 (staff)

## Architecture

### 后端模块结构

```
ruoyi-modules/ruoyi-health/
├── src/main/java/org/dromara/health/
│   ├── controller/
│   │   ├── HealthDashboardController.java      # 大屏数据接口
│   │   ├── HealthDeviceTypeController.java     # 设备类型管理
│   │   ├── HealthDeviceController.java         # 设备管理
│   │   ├── HealthDeviceAlertController.java    # 告警管理
│   │   ├── HealthOrganizationController.java   # 组织管理
│   │   ├── HealthElderController.java          # 老人管理
│   │   ├── HealthEmergencyContactController.java # 紧急联系人
│   │   ├── HealthVitalSignsController.java     # 体征数据
│   │   └── HealthServiceStaffController.java   # 服务人员
│   ├── service/
│   │   ├── IHealthDashboardService.java
│   │   ├── IHealthDeviceTypeService.java
│   │   ├── IHealthDeviceService.java
│   │   ├── IHealthDeviceAlertService.java
│   │   ├── IHealthOrganizationService.java
│   │   ├── IHealthElderService.java
│   │   ├── IHealthEmergencyContactService.java
│   │   ├── IHealthVitalSignsService.java
│   │   ├── IHealthServiceStaffService.java
│   │   └── impl/
│   ├── mapper/
│   ├── domain/
│   │   ├── HealthDeviceType.java
│   │   ├── HealthDevice.java
│   │   ├── HealthDeviceAlert.java
│   │   ├── HealthOrganization.java
│   │   ├── HealthElder.java
│   │   ├── HealthEmergencyContact.java
│   │   ├── HealthVitalSigns.java
│   │   ├── HealthServiceStaff.java
│   │   ├── bo/
│   │   └── vo/
│   └── config/
└── src/main/resources/
    └── mapper/health/
```

### 前端模块结构

```
plus-ui/src/
├── api/health/
│   ├── dashboard/
│   │   ├── index.ts
│   │   └── types.ts
│   ├── device/
│   │   ├── index.ts
│   │   └── types.ts
│   ├── deviceType/
│   │   ├── index.ts
│   │   └── types.ts
│   ├── alert/
│   │   ├── index.ts
│   │   └── types.ts
│   ├── organization/
│   │   ├── index.ts
│   │   └── types.ts
│   ├── elder/
│   │   ├── index.ts
│   │   └── types.ts
│   ├── vitalSigns/
│   │   ├── index.ts
│   │   └── types.ts
│   └── serviceStaff/
│       ├── index.ts
│       └── types.ts
└── views/health/
    ├── dashboard/
    │   └── index.vue           # 数据大屏
    ├── device/
    │   ├── type/
    │   │   └── index.vue       # 设备类型
    │   └── index.vue           # 设备管理
    ├── alert/
    │   └── index.vue           # 告警管理
    ├── organization/
    │   └── index.vue           # 组织管理
    ├── elder/
    │   └── index.vue           # 老人管理
    ├── vitalSigns/
    │   └── index.vue           # 体征数据
    └── serviceStaff/
        └── index.vue           # 服务人员
```

## Components and Interfaces

### 后端API接口设计

#### 1. 大屏数据接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /health/dashboard/statistics | 获取统计数据 |
| GET | /health/dashboard/deviceOnlineRate | 获取设备在线率 |
| GET | /health/dashboard/alertTrend | 获取告警趋势(7天) |
| GET | /health/dashboard/alertTypeDistribution | 获取告警类型分布 |
| GET | /health/dashboard/latestAlerts | 获取最新告警列表 |
| GET | /health/dashboard/deviceDistribution | 获取设备分布数据 |

#### 2. 设备类型接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /health/deviceType/list | 查询设备类型列表 |
| GET | /health/deviceType/tree | 查询设备类型树 |
| GET | /health/deviceType/{id} | 获取设备类型详情 |
| POST | /health/deviceType | 新增设备类型 |
| PUT | /health/deviceType | 修改设备类型 |
| DELETE | /health/deviceType/{ids} | 删除设备类型 |

#### 3. 设备管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /health/device/list | 查询设备列表 |
| GET | /health/device/{id} | 获取设备详情 |
| POST | /health/device | 新增设备 |
| PUT | /health/device | 修改设备 |
| DELETE | /health/device/{ids} | 删除设备 |
| PUT | /health/device/bind | 绑定设备 |
| PUT | /health/device/unbind/{id} | 解绑设备 |
| POST | /health/device/export | 导出设备 |
| POST | /health/device/import | 导入设备 |

#### 4. 告警管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /health/alert/list | 查询告警列表 |
| GET | /health/alert/{id} | 获取告警详情 |
| PUT | /health/alert/handle | 处理告警 |
| PUT | /health/alert/ignore/{id} | 忽略告警 |
| POST | /health/alert/export | 导出告警 |

#### 5. 组织管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /health/organization/list | 查询组织列表 |
| GET | /health/organization/tree | 查询组织树 |
| GET | /health/organization/{id} | 获取组织详情 |
| POST | /health/organization | 新增组织 |
| PUT | /health/organization | 修改组织 |
| DELETE | /health/organization/{ids} | 删除组织 |

#### 6. 老人管理接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /health/elder/list | 查询老人列表 |
| GET | /health/elder/{id} | 获取老人详情 |
| POST | /health/elder | 新增老人 |
| PUT | /health/elder | 修改老人 |
| DELETE | /health/elder/{ids} | 删除老人 |
| POST | /health/elder/export | 导出老人 |
| POST | /health/elder/import | 导入老人 |

#### 7. 紧急联系人接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /health/emergencyContact/list/{elderId} | 查询联系人列表 |
| POST | /health/emergencyContact | 新增联系人 |
| PUT | /health/emergencyContact | 修改联系人 |
| DELETE | /health/emergencyContact/{ids} | 删除联系人 |

#### 8. 体征数据接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /health/vitalSigns/list | 查询体征数据列表 |
| GET | /health/vitalSigns/{id} | 获取体征详情 |
| GET | /health/vitalSigns/trend | 获取体征趋势 |
| POST | /health/vitalSigns | 新增体征数据 |
| POST | /health/vitalSigns/export | 导出体征数据 |

#### 9. 服务人员接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /health/serviceStaff/list | 查询服务人员列表 |
| GET | /health/serviceStaff/{id} | 获取人员详情 |
| POST | /health/serviceStaff | 新增服务人员 |
| PUT | /health/serviceStaff | 修改服务人员 |
| DELETE | /health/serviceStaff/{ids} | 删除服务人员 |

## Data Models

### 数据库表设计

#### 1. health_device_type (设备类型表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| parent_id | BIGINT | 父级ID |
| type_name | VARCHAR(100) | 类型名称 |
| type_code | VARCHAR(50) | 类型编码 |
| category | VARCHAR(20) | 设备分类 |
| icon | VARCHAR(200) | 图标 |
| sort_order | INT | 排序 |
| status | CHAR(1) | 状态 |
| tenant_id | VARCHAR(20) | 租户编号 |

#### 2. health_device (设备信息表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| device_code | VARCHAR(100) | 设备编码 |
| device_name | VARCHAR(200) | 设备名称 |
| device_type_id | BIGINT | 设备类型ID |
| device_model | VARCHAR(100) | 设备型号 |
| manufacturer | VARCHAR(200) | 生产厂商 |
| serial_number | VARCHAR(100) | 序列号 |
| bind_user_id | BIGINT | 绑定用户ID |
| online_status | CHAR(1) | 在线状态 |
| battery_level | INT | 电量 |
| longitude | DECIMAL(10,6) | 经度 |
| latitude | DECIMAL(10,6) | 纬度 |
| tenant_id | VARCHAR(20) | 租户编号 |

#### 3. health_device_alert (设备告警表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| alert_code | VARCHAR(50) | 告警编码 |
| device_id | BIGINT | 设备ID |
| user_id | BIGINT | 用户ID |
| alert_type | VARCHAR(20) | 告警类型 |
| alert_level | CHAR(1) | 告警级别 |
| alert_title | VARCHAR(200) | 告警标题 |
| alert_content | TEXT | 告警内容 |
| alert_time | DATETIME | 告警时间 |
| handle_status | CHAR(1) | 处理状态 |
| handle_user_id | BIGINT | 处理人ID |
| handle_result | TEXT | 处理结果 |
| tenant_id | VARCHAR(20) | 租户编号 |

#### 4. health_organization (组织机构表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| parent_id | BIGINT | 父级ID |
| org_code | VARCHAR(50) | 组织编码 |
| org_name | VARCHAR(200) | 组织名称 |
| org_type | VARCHAR(20) | 组织类型 |
| leader | VARCHAR(50) | 负责人 |
| phone | VARCHAR(20) | 联系电话 |
| address | VARCHAR(500) | 地址 |
| tenant_id | VARCHAR(20) | 租户编号 |

#### 5. health_elder (老人信息表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| user_code | VARCHAR(50) | 用户编码 |
| name | VARCHAR(50) | 姓名 |
| gender | CHAR(1) | 性别 |
| birthday | DATE | 出生日期 |
| age | INT | 年龄 |
| phone | VARCHAR(20) | 手机号 |
| org_id | BIGINT | 所属组织ID |
| height | DECIMAL(5,2) | 身高 |
| weight | DECIMAL(5,2) | 体重 |
| bmi | DECIMAL(4,2) | BMI指数 |
| medical_history | TEXT | 既往病史(JSON) |
| care_level | CHAR(1) | 护理等级 |
| risk_level | CHAR(1) | 风险等级 |
| tenant_id | VARCHAR(20) | 租户编号 |

#### 6. health_emergency_contact (紧急联系人表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| elder_id | BIGINT | 老人ID |
| name | VARCHAR(50) | 联系人姓名 |
| relation | VARCHAR(20) | 与老人关系 |
| phone | VARCHAR(20) | 联系电话 |
| is_primary | CHAR(1) | 是否主要联系人 |
| notify_type | VARCHAR(50) | 通知方式 |
| tenant_id | VARCHAR(20) | 租户编号 |

#### 7. health_vital_signs (体征数据表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| elder_id | BIGINT | 老人ID |
| device_id | BIGINT | 设备ID |
| measure_time | DATETIME | 测量时间 |
| temperature | DECIMAL(4,1) | 体温 |
| heart_rate | INT | 心率 |
| systolic_pressure | INT | 收缩压 |
| diastolic_pressure | INT | 舒张压 |
| blood_oxygen | INT | 血氧 |
| blood_sugar | DECIMAL(4,1) | 血糖 |
| is_abnormal | CHAR(1) | 是否异常 |
| tenant_id | VARCHAR(20) | 租户编号 |

#### 8. health_service_staff (服务人员表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| staff_code | VARCHAR(50) | 人员编码 |
| name | VARCHAR(50) | 姓名 |
| gender | CHAR(1) | 性别 |
| phone | VARCHAR(20) | 手机号 |
| staff_type | VARCHAR(20) | 人员类型 |
| org_id | BIGINT | 所属组织ID |
| status | CHAR(1) | 状态 |
| tenant_id | VARCHAR(20) | 租户编号 |



## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system-essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

### Property 1: 数据持久化一致性

*For any* 实体对象（设备类型、设备、告警、组织、老人、联系人、体征数据、服务人员），新增后查询应返回相同数据，修改后查询应返回更新后的数据，删除后查询应返回空或标记为已删除。

**Validates: Requirements 2.2, 2.3, 3.2, 3.3, 3.4, 5.2, 5.3, 6.2, 6.3, 6.4, 7.2, 7.3, 7.4, 9.2, 9.3, 9.4**

### Property 2: 树形结构完整性

*For any* 设备类型或组织机构的树形查询，返回的数据结构应满足：每个节点的parent_id指向有效的父节点或为0（根节点），且不存在循环引用。

**Validates: Requirements 2.1, 5.1**

### Property 3: 删除约束验证

*For any* 设备类型删除操作，如果存在关联的设备记录，则删除应失败并返回错误信息。*For any* 组织删除操作，如果存在下级组织或关联的老人/服务人员，则删除应失败。

**Validates: Requirements 2.4, 5.4**

### Property 4: 唯一编码生成

*For any* 新增的设备或老人，系统生成的编码（device_code/user_code）在同一租户下应唯一，不与已存在的编码重复。

**Validates: Requirements 3.2, 6.2**

### Property 5: BMI自动计算

*For any* 老人信息的身高(height)和体重(weight)更新，BMI值应自动计算为 weight / (height/100)^2，结果保留两位小数。

**Validates: Requirements 6.8**

### Property 6: 主要联系人唯一性

*For any* 老人的紧急联系人列表，设置某个联系人为主要联系人时，该老人的其他联系人的is_primary字段应自动设置为'0'。

**Validates: Requirements 7.5**

### Property 7: 体征异常告警触发

*For any* 新增的体征数据，如果体温>37.5或<35、心率>100或<60、收缩压>140或<90、血氧<95，则is_abnormal应设置为'1'，并自动创建对应的告警记录。

**Validates: Requirements 8.7**

### Property 8: 设备绑定状态一致性

*For any* 设备绑定操作，绑定后设备的bind_user_id应等于指定的老人ID，bind_time应更新为当前时间。*For any* 解绑操作，bind_user_id应设置为null，bind_time保持不变。

**Validates: Requirements 3.5, 3.6**

### Property 9: 查询筛选正确性

*For any* 带筛选条件的列表查询，返回的所有记录应满足筛选条件。例如：按设备分类筛选时，返回的设备类型的category字段应等于筛选值。

**Validates: Requirements 2.5, 3.7, 4.5, 5.5, 6.6, 8.4, 9.5**

### Property 10: 告警处理状态流转

*For any* 告警处理操作，处理后handle_status应更新为'2'(已处理)，handle_user_id应设置为当前操作用户ID，handle_time应更新为当前时间。*For any* 忽略操作，handle_status应更新为'3'(已忽略)。

**Validates: Requirements 4.3, 4.4**

## Error Handling

### 后端错误处理

1. **参数校验错误**: 使用 `@Validated` 注解进行参数校验，校验失败返回400错误
2. **业务逻辑错误**: 使用 `ServiceException` 抛出业务异常，返回对应错误码和消息
3. **数据不存在**: 查询不到数据时返回404错误
4. **权限不足**: 使用 `@SaCheckPermission` 注解校验权限，无权限返回403错误
5. **重复提交**: 使用 `@RepeatSubmit` 注解防止重复提交

### 前端错误处理

1. **网络错误**: 显示网络异常提示，支持重试
2. **业务错误**: 根据后端返回的错误码显示对应提示信息
3. **表单验证**: 使用 Element Plus 的表单验证，实时提示错误
4. **超时处理**: 请求超时显示提示，支持重试

## Testing Strategy

### 单元测试

1. **Service层测试**: 测试业务逻辑的正确性
2. **工具类测试**: 测试BMI计算、编码生成等工具方法

### 集成测试

1. **API接口测试**: 测试Controller层的接口响应
2. **数据库操作测试**: 测试Mapper层的CRUD操作

### 属性测试

使用JUnit 5 + jqwik进行属性测试：

1. **Property 1**: 生成随机实体数据，验证CRUD操作的一致性
2. **Property 4**: 生成大量设备/老人，验证编码唯一性
3. **Property 5**: 生成随机身高体重，验证BMI计算正确性
4. **Property 7**: 生成随机体征数据，验证异常判断和告警触发

### 前端测试

1. **组件测试**: 使用Vitest测试Vue组件
2. **E2E测试**: 使用Playwright测试关键业务流程

