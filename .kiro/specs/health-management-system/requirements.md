# Requirements Document

## Introduction

健康管理后台系统是一个基于若依框架(RuoYi-Vue-Plus)开发的老人健康监护管理平台。系统支持多租户架构，主要面向养老机构、社区、医院等组织，提供设备管理、用户管理、体征监测、告警处理等核心功能，并配备炫酷的数据可视化大屏用于演示展示。

## Glossary

- **Health_Management_System**: 健康管理后台系统，整体应用平台
- **Dashboard**: 首页数据大屏，展示关键指标和实时数据的可视化页面
- **Device_Manager**: 设备管理模块，管理智能穿戴、检测、物联设备
- **Elder_Manager**: 老人管理模块，管理被监护老人的基本信息和健康档案
- **Alert_Manager**: 告警管理模块，处理设备告警和紧急事件
- **Vital_Signs_Manager**: 体征管理模块，记录和分析健康数据
- **Organization_Manager**: 组织管理模块，管理机构和组织架构
- **Service_Staff**: 服务人员，包括陪诊人员、医务人员、救援人员

## Requirements

### Requirement 1: 首页数据大屏

**User Story:** As a 系统管理员, I want 查看首页数据大屏, so that 我可以快速了解系统整体运行状态和关键健康指标。

#### Acceptance Criteria

1. WHEN 用户访问首页大屏 THEN Dashboard SHALL 展示用户总数、设备总数、在线设备数、今日告警数等核心统计卡片
2. WHEN 用户访问首页大屏 THEN Dashboard SHALL 展示设备在线率的实时环形图表
3. WHEN 用户访问首页大屏 THEN Dashboard SHALL 展示最近7天告警趋势折线图
4. WHEN 用户访问首页大屏 THEN Dashboard SHALL 展示告警类型分布饼图
5. WHEN 用户访问首页大屏 THEN Dashboard SHALL 展示最新告警列表(前10条)
6. WHEN 用户访问首页大屏 THEN Dashboard SHALL 展示设备分布地图(基于经纬度)
7. WHEN 有新告警产生 THEN Dashboard SHALL 实时更新告警数据(每30秒刷新)
8. WHEN 用户点击统计卡片 THEN Dashboard SHALL 跳转到对应的详情页面

### Requirement 2: 设备类型管理

**User Story:** As a 系统管理员, I want 管理设备类型, so that 我可以对不同类型的智能设备进行分类管理。

#### Acceptance Criteria

1. WHEN 用户访问设备类型列表 THEN Device_Manager SHALL 以树形结构展示所有设备类型
2. WHEN 用户新增设备类型 THEN Device_Manager SHALL 创建新的设备类型记录并支持选择父级类型
3. WHEN 用户修改设备类型 THEN Device_Manager SHALL 更新设备类型信息
4. WHEN 用户删除设备类型 THEN Device_Manager SHALL 检查是否有关联设备，有则阻止删除
5. THE Device_Manager SHALL 支持按设备分类(智能穿戴/智能检测/智能物联)筛选
6. THE Device_Manager SHALL 支持设备类型的启用/停用状态切换

### Requirement 3: 设备信息管理

**User Story:** As a 系统管理员, I want 管理设备信息, so that 我可以对所有智能设备进行统一管理和监控。

#### Acceptance Criteria

1. WHEN 用户访问设备列表 THEN Device_Manager SHALL 展示设备编码、名称、类型、绑定用户、在线状态、电量等信息
2. WHEN 用户新增设备 THEN Device_Manager SHALL 创建设备记录并生成唯一设备编码
3. WHEN 用户修改设备 THEN Device_Manager SHALL 更新设备信息
4. WHEN 用户删除设备 THEN Device_Manager SHALL 逻辑删除设备记录
5. WHEN 用户绑定设备 THEN Device_Manager SHALL 将设备与指定老人关联
6. WHEN 用户解绑设备 THEN Device_Manager SHALL 解除设备与老人的关联
7. THE Device_Manager SHALL 支持按设备类型、在线状态、绑定状态筛选
8. THE Device_Manager SHALL 支持设备信息的导入导出功能
9. WHEN 设备状态变化 THEN Device_Manager SHALL 记录设备上线/离线时间

### Requirement 4: 设备告警管理

**User Story:** As a 系统管理员, I want 管理设备告警, so that 我可以及时发现和处理老人的紧急情况。

#### Acceptance Criteria

1. WHEN 用户访问告警列表 THEN Alert_Manager SHALL 展示告警编码、设备、用户、类型、级别、状态、时间等信息
2. WHEN 用户查看告警详情 THEN Alert_Manager SHALL 展示告警的完整信息包括位置地图
3. WHEN 用户处理告警 THEN Alert_Manager SHALL 记录处理方式、处理人、处理结果
4. WHEN 用户忽略告警 THEN Alert_Manager SHALL 将告警状态更新为已忽略
5. THE Alert_Manager SHALL 支持按告警类型、级别、处理状态、时间范围筛选
6. THE Alert_Manager SHALL 支持告警数据的导出功能
7. WHEN 新告警产生 THEN Alert_Manager SHALL 根据告警级别显示不同颜色标识
8. IF 告警级别为紧急 THEN Alert_Manager SHALL 在页面顶部显示醒目提示

### Requirement 5: 组织机构管理

**User Story:** As a 系统管理员, I want 管理组织机构, so that 我可以对养老机构、社区、医院等进行层级管理。

#### Acceptance Criteria

1. WHEN 用户访问组织列表 THEN Organization_Manager SHALL 以树形结构展示所有组织
2. WHEN 用户新增组织 THEN Organization_Manager SHALL 创建组织记录并支持选择上级组织
3. WHEN 用户修改组织 THEN Organization_Manager SHALL 更新组织信息
4. WHEN 用户删除组织 THEN Organization_Manager SHALL 检查是否有下级组织或关联用户，有则阻止删除
5. THE Organization_Manager SHALL 支持按组织类型(企业/医院/社区/家庭)筛选
6. THE Organization_Manager SHALL 支持组织的启用/停用状态切换

### Requirement 6: 老人信息管理

**User Story:** As a 系统管理员, I want 管理老人信息, so that 我可以建立完整的老人健康档案。

#### Acceptance Criteria

1. WHEN 用户访问老人列表 THEN Elder_Manager SHALL 展示老人编码、姓名、性别、年龄、所属组织、护理等级、风险等级等信息
2. WHEN 用户新增老人 THEN Elder_Manager SHALL 创建老人记录并生成唯一用户编码
3. WHEN 用户修改老人信息 THEN Elder_Manager SHALL 更新老人基本信息和健康档案
4. WHEN 用户删除老人 THEN Elder_Manager SHALL 逻辑删除老人记录
5. WHEN 用户查看老人详情 THEN Elder_Manager SHALL 展示完整的健康档案包括既往病史、过敏史、家族病史
6. THE Elder_Manager SHALL 支持按组织、护理等级、风险等级筛选
7. THE Elder_Manager SHALL 支持老人信息的导入导出功能
8. WHEN 老人身高体重变化 THEN Elder_Manager SHALL 自动计算BMI指数

### Requirement 7: 紧急联系人管理

**User Story:** As a 系统管理员, I want 管理老人的紧急联系人, so that 发生紧急情况时可以及时通知家属。

#### Acceptance Criteria

1. WHEN 用户查看老人详情 THEN Elder_Manager SHALL 展示该老人的所有紧急联系人
2. WHEN 用户新增紧急联系人 THEN Elder_Manager SHALL 创建联系人记录并关联到老人
3. WHEN 用户修改紧急联系人 THEN Elder_Manager SHALL 更新联系人信息
4. WHEN 用户删除紧急联系人 THEN Elder_Manager SHALL 删除联系人记录
5. THE Elder_Manager SHALL 支持设置主要联系人(每个老人只能有一个)
6. THE Elder_Manager SHALL 支持配置联系人的通知方式(短信/电话/短信+电话)

### Requirement 8: 体征数据管理

**User Story:** As a 系统管理员, I want 查看老人的体征数据, so that 我可以监控老人的健康状况。

#### Acceptance Criteria

1. WHEN 用户访问体征数据列表 THEN Vital_Signs_Manager SHALL 展示老人的各项体征数据记录
2. WHEN 用户查看体征详情 THEN Vital_Signs_Manager SHALL 展示体征数据的历史趋势图表
3. THE Vital_Signs_Manager SHALL 支持记录体温、心率、血压、血氧、血糖等体征数据
4. THE Vital_Signs_Manager SHALL 支持按老人、体征类型、时间范围筛选
5. THE Vital_Signs_Manager SHALL 支持体征数据的导出功能
6. WHEN 体征数据异常 THEN Vital_Signs_Manager SHALL 用红色标识异常值
7. IF 体征数据超出正常范围 THEN Vital_Signs_Manager SHALL 自动生成告警记录

### Requirement 9: 服务人员管理

**User Story:** As a 系统管理员, I want 管理服务人员, so that 我可以对陪诊、医务、救援人员进行统一管理。

#### Acceptance Criteria

1. WHEN 用户访问服务人员列表 THEN Service_Staff SHALL 展示人员姓名、类型、联系方式、所属组织、状态等信息
2. WHEN 用户新增服务人员 THEN Service_Staff SHALL 创建人员记录
3. WHEN 用户修改服务人员 THEN Service_Staff SHALL 更新人员信息
4. WHEN 用户删除服务人员 THEN Service_Staff SHALL 逻辑删除人员记录
5. THE Service_Staff SHALL 支持按人员类型(陪诊/医务/救援)筛选
6. THE Service_Staff SHALL 支持人员的启用/停用状态切换

### Requirement 10: 通知配置管理

**User Story:** As a 系统管理员, I want 配置通知规则, so that 系统可以按照配置自动发送告警通知。

#### Acceptance Criteria

1. WHEN 用户访问通知配置 THEN Health_Management_System SHALL 展示各类通知的配置项
2. WHEN 用户修改告警通知配置 THEN Health_Management_System SHALL 更新告警通知规则
3. THE Health_Management_System SHALL 支持配置告警通知的触发条件
4. THE Health_Management_System SHALL 支持配置通知方式(短信/电话/短信+电话)
5. THE Health_Management_System SHALL 支持配置通知时间段(避免夜间打扰)
6. THE Health_Management_System SHALL 支持启用/停用各类通知

