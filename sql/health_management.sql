-- =============================================
-- 健康管理系统数据库脚本
-- 版本: v1.0
-- 创建日期: 2025-12-28
-- =============================================

-- ----------------------------
-- 1. 设备类型表
-- ----------------------------
DROP TABLE IF EXISTS health_device_type;
CREATE TABLE health_device_type (
    id              BIGINT          NOT NULL                    COMMENT '主键ID',
    parent_id       BIGINT          DEFAULT 0                   COMMENT '父级ID',
    type_name       VARCHAR(100)    NOT NULL                    COMMENT '类型名称',
    type_code       VARCHAR(50)     NOT NULL                    COMMENT '类型编码',
    category        VARCHAR(20)     NOT NULL                    COMMENT '设备分类(wearable-智能穿戴,detection-智能检测,iot-智能物联)',
    icon            VARCHAR(200)                                COMMENT '图标',
    sort_order      INT             DEFAULT 0                   COMMENT '排序',
    status          CHAR(1)         DEFAULT '0'                 COMMENT '状态(0正常 1停用)',
    remark          VARCHAR(500)                                COMMENT '备注',
    del_flag        CHAR(1)         DEFAULT '0'                 COMMENT '删除标志(0存在 1删除)',
    create_dept     BIGINT                                      COMMENT '创建部门',
    create_by       BIGINT                                      COMMENT '创建者',
    create_time     DATETIME                                    COMMENT '创建时间',
    update_by       BIGINT                                      COMMENT '更新者',
    update_time     DATETIME                                    COMMENT '更新时间',
    tenant_id       VARCHAR(20)     DEFAULT '000000'            COMMENT '租户编号',
    PRIMARY KEY (id)
) ENGINE=InnoDB COMMENT='设备类型表';

-- ----------------------------
-- 2. 设备信息表
-- ----------------------------
DROP TABLE IF EXISTS health_device;
CREATE TABLE health_device (
    id              BIGINT          NOT NULL                    COMMENT '主键ID',
    device_code     VARCHAR(100)    NOT NULL                    COMMENT '设备编码',
    device_name     VARCHAR(200)    NOT NULL                    COMMENT '设备名称',
    device_type_id  BIGINT          NOT NULL                    COMMENT '设备类型ID',
    device_model    VARCHAR(100)                                COMMENT '设备型号',
    manufacturer    VARCHAR(200)                                COMMENT '生产厂商',
    serial_number   VARCHAR(100)                                COMMENT '序列号',
    mac_address     VARCHAR(50)                                 COMMENT 'MAC地址',
    firmware_version VARCHAR(50)                                COMMENT '固件版本',
    bind_user_id    BIGINT                                      COMMENT '绑定用户ID',
    bind_time       DATETIME                                    COMMENT '绑定时间',
    online_status   CHAR(1)         DEFAULT '0'                 COMMENT '在线状态(0离线 1在线)',
    last_online_time DATETIME                                   COMMENT '最后上线时间',
    last_offline_time DATETIME                                  COMMENT '最后离线时间',
    battery_level   INT             DEFAULT 100                 COMMENT '电量百分比',
    signal_strength INT             DEFAULT 0                   COMMENT '信号强度',
    location        VARCHAR(500)                                COMMENT '设备位置',
    longitude       DECIMAL(10,6)                               COMMENT '经度',
    latitude        DECIMAL(10,6)                               COMMENT '纬度',
    status          CHAR(1)         DEFAULT '0'                 COMMENT '状态(0正常 1停用 2维修)',
    remark          VARCHAR(500)                                COMMENT '备注',
    del_flag        CHAR(1)         DEFAULT '0'                 COMMENT '删除标志(0存在 1删除)',
    create_dept     BIGINT                                      COMMENT '创建部门',
    create_by       BIGINT                                      COMMENT '创建者',
    create_time     DATETIME                                    COMMENT '创建时间',
    update_by       BIGINT                                      COMMENT '更新者',
    update_time     DATETIME                                    COMMENT '更新时间',
    tenant_id       VARCHAR(20)     DEFAULT '000000'            COMMENT '租户编号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_device_code (device_code, tenant_id)
) ENGINE=InnoDB COMMENT='设备信息表';

-- ----------------------------
-- 3. 设备告警表
-- ----------------------------
DROP TABLE IF EXISTS health_device_alert;
CREATE TABLE health_device_alert (
    id              BIGINT          NOT NULL                    COMMENT '主键ID',
    alert_code      VARCHAR(50)     NOT NULL                    COMMENT '告警编码',
    device_id       BIGINT          NOT NULL                    COMMENT '设备ID',
    user_id         BIGINT                                      COMMENT '用户ID',
    alert_type      VARCHAR(20)     NOT NULL                    COMMENT '告警类型(sos-求助,fall-摔倒,vital-体征异常,offline-离线,battery-低电量)',
    alert_level     CHAR(1)         DEFAULT '1'                 COMMENT '告警级别(1普通 2重要 3紧急)',
    alert_title     VARCHAR(200)    NOT NULL                    COMMENT '告警标题',
    alert_content   TEXT                                        COMMENT '告警内容',
    alert_time      DATETIME        NOT NULL                    COMMENT '告警时间',
    location        VARCHAR(500)                                COMMENT '告警位置',
    longitude       DECIMAL(10,6)                               COMMENT '经度',
    latitude        DECIMAL(10,6)                               COMMENT '纬度',
    handle_status   CHAR(1)         DEFAULT '0'                 COMMENT '处理状态(0待处理 1处理中 2已处理 3已忽略)',
    handle_type     VARCHAR(20)                                 COMMENT '处理方式(phone-电话,onsite-现场,rescue-救援)',
    handle_user_id  BIGINT                                      COMMENT '处理人ID',
    handle_time     DATETIME                                    COMMENT '处理时间',
    handle_result   TEXT                                        COMMENT '处理结果',
    del_flag        CHAR(1)         DEFAULT '0'                 COMMENT '删除标志(0存在 1删除)',
    create_dept     BIGINT                                      COMMENT '创建部门',
    create_by       BIGINT                                      COMMENT '创建者',
    create_time     DATETIME                                    COMMENT '创建时间',
    update_by       BIGINT                                      COMMENT '更新者',
    update_time     DATETIME                                    COMMENT '更新时间',
    tenant_id       VARCHAR(20)     DEFAULT '000000'            COMMENT '租户编号',
    PRIMARY KEY (id),
    KEY idx_device_id (device_id),
    KEY idx_user_id (user_id),
    KEY idx_alert_time (alert_time)
) ENGINE=InnoDB COMMENT='设备告警表';

-- ----------------------------
-- 4. 组织机构表
-- ----------------------------
DROP TABLE IF EXISTS health_organization;
CREATE TABLE health_organization (
    id              BIGINT          NOT NULL                    COMMENT '主键ID',
    parent_id       BIGINT          DEFAULT 0                   COMMENT '父级ID',
    org_code        VARCHAR(50)     NOT NULL                    COMMENT '组织编码',
    org_name        VARCHAR(200)    NOT NULL                    COMMENT '组织名称',
    org_type        VARCHAR(20)     DEFAULT 'company'           COMMENT '组织类型(company-企业,hospital-医院,community-社区,family-家庭)',
    leader          VARCHAR(50)                                 COMMENT '负责人',
    phone           VARCHAR(20)                                 COMMENT '联系电话',
    email           VARCHAR(100)                                COMMENT '邮箱',
    address         VARCHAR(500)                                COMMENT '地址',
    longitude       DECIMAL(10,6)                               COMMENT '经度',
    latitude        DECIMAL(10,6)                               COMMENT '纬度',
    sort_order      INT             DEFAULT 0                   COMMENT '排序',
    status          CHAR(1)         DEFAULT '0'                 COMMENT '状态(0正常 1停用)',
    remark          VARCHAR(500)                                COMMENT '备注',
    del_flag        CHAR(1)         DEFAULT '0'                 COMMENT '删除标志(0存在 1删除)',
    create_dept     BIGINT                                      COMMENT '创建部门',
    create_by       BIGINT                                      COMMENT '创建者',
    create_time     DATETIME                                    COMMENT '创建时间',
    update_by       BIGINT                                      COMMENT '更新者',
    update_time     DATETIME                                    COMMENT '更新时间',
    tenant_id       VARCHAR(20)     DEFAULT '000000'            COMMENT '租户编号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_org_code (org_code, tenant_id)
) ENGINE=InnoDB COMMENT='组织机构表';

-- ----------------------------
-- 5. 健康用户表(老人/被监护人)
-- ----------------------------
DROP TABLE IF EXISTS health_elder;
CREATE TABLE health_elder (
    id              BIGINT          NOT NULL                    COMMENT '主键ID',
    user_code       VARCHAR(50)     NOT NULL                    COMMENT '用户编码',
    name            VARCHAR(50)     NOT NULL                    COMMENT '姓名',
    gender          CHAR(1)         DEFAULT '0'                 COMMENT '性别(0未知 1男 2女)',
    birthday        DATE                                        COMMENT '出生日期',
    age             INT                                         COMMENT '年龄',
    id_card         VARCHAR(20)                                 COMMENT '身份证号',
    phone           VARCHAR(20)                                 COMMENT '手机号',
    avatar          VARCHAR(500)                                COMMENT '头像',
    org_id          BIGINT                                      COMMENT '所属组织ID',
    address         VARCHAR(500)                                COMMENT '居住地址',
    longitude       DECIMAL(10,6)                               COMMENT '经度',
    latitude        DECIMAL(10,6)                               COMMENT '纬度',
    blood_type      VARCHAR(10)                                 COMMENT '血型(A,B,AB,O)',
    height          DECIMAL(5,2)                                COMMENT '身高(cm)',
    weight          DECIMAL(5,2)                                COMMENT '体重(kg)',
    bmi             DECIMAL(4,2)                                COMMENT 'BMI指数',
    medical_history TEXT                                        COMMENT '既往病史(JSON)',
    allergy_history TEXT                                        COMMENT '过敏史(JSON)',
    family_history  TEXT                                        COMMENT '家族病史(JSON)',
    care_level      CHAR(1)         DEFAULT '1'                 COMMENT '护理等级(1自理 2半自理 3不能自理)',
    risk_level      CHAR(1)         DEFAULT '1'                 COMMENT '风险等级(1低 2中 3高)',
    status          CHAR(1)         DEFAULT '0'                 COMMENT '状态(0正常 1停用)',
    remark          VARCHAR(500)                                COMMENT '备注',
    del_flag        CHAR(1)         DEFAULT '0'                 COMMENT '删除标志(0存在 1删除)',
    create_dept     BIGINT                                      COMMENT '创建部门',
    create_by       BIGINT                                      COMMENT '创建者',
    create_time     DATETIME                                    COMMENT '创建时间',
    update_by       BIGINT                                      COMMENT '更新者',
    update_time     DATETIME                                    COMMENT '更新时间',
    tenant_id       VARCHAR(20)     DEFAULT '000000'            COMMENT '租户编号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_code (user_code, tenant_id)
) ENGINE=InnoDB COMMENT='健康用户表';

-- ----------------------------
-- 6. 紧急联系人表
-- ----------------------------
DROP TABLE IF EXISTS health_emergency_contact;
CREATE TABLE health_emergency_contact (
    id              BIGINT          NOT NULL                    COMMENT '主键ID',
    elder_id        BIGINT          NOT NULL                    COMMENT '老人ID',
    name            VARCHAR(50)     NOT NULL                    COMMENT '联系人姓名',
    relation        VARCHAR(20)     NOT NULL                    COMMENT '与老人关系(spouse-配偶,child-子女,parent-父母,sibling-兄弟姐妹,other-其他)',
    phone           VARCHAR(20)     NOT NULL                    COMMENT '联系电话',
    is_primary      CHAR(1)         DEFAULT '0'                 COMMENT '是否主要联系人(0否 1是)',
    notify_alert    CHAR(1)         DEFAULT '1'                 COMMENT '是否接收告警(0否 1是)',
    notify_type     VARCHAR(50)     DEFAULT 'sms'               COMMENT '通知方式(sms-短信,phone-电话,both-短信+电话)',
    sort_order      INT             DEFAULT 0                   COMMENT '排序',
    status          CHAR(1)         DEFAULT '0'                 COMMENT '状态(0正常 1停用)',
    remark          VARCHAR(500)                                COMMENT '备注',
    del_flag        CHAR(1)         DEFAULT '0'                 COMMENT '删除标志(0存在 1删除)',
    create_dept     BIGINT                                      COMMENT '创建部门',
    create_by       BIGINT                                      COMMENT '创建者',
    create_time     DATETIME                                    COMMENT '创建时间',
    update_by       BIGINT                                      COMMENT '更新者',
    update_time     DATETIME                                    COMMENT '更新时间',
    tenant_id       VARCHAR(20)     DEFAULT '000000'            COMMENT '租户编号',
    PRIMARY KEY (id),
    KEY idx_elder_id (elder_id)
) ENGINE=InnoDB COMMENT='紧急联系人表';

-- ----------------------------
-- 7. 体征数据表
-- ----------------------------
DROP TABLE IF EXISTS health_vital_signs;
CREATE TABLE health_vital_signs (
    id                  BIGINT          NOT NULL                    COMMENT '主键ID',
    elder_id            BIGINT          NOT NULL                    COMMENT '老人ID',
    device_id           BIGINT                                      COMMENT '设备ID',
    measure_time        DATETIME        NOT NULL                    COMMENT '测量时间',
    temperature         DECIMAL(4,1)                                COMMENT '体温(℃)',
    heart_rate          INT                                         COMMENT '心率(次/分)',
    systolic_pressure   INT                                         COMMENT '收缩压(mmHg)',
    diastolic_pressure  INT                                         COMMENT '舒张压(mmHg)',
    blood_oxygen        INT                                         COMMENT '血氧饱和度(%)',
    blood_sugar         DECIMAL(4,1)                                COMMENT '血糖(mmol/L)',
    respiratory_rate    INT                                         COMMENT '呼吸频率(次/分)',
    steps               INT                                         COMMENT '步数',
    sleep_duration      INT                                         COMMENT '睡眠时长(分钟)',
    is_abnormal         CHAR(1)         DEFAULT '0'                 COMMENT '是否异常(0正常 1异常)',
    abnormal_items      VARCHAR(200)                                COMMENT '异常项目(逗号分隔)',
    remark              VARCHAR(500)                                COMMENT '备注',
    del_flag            CHAR(1)         DEFAULT '0'                 COMMENT '删除标志(0存在 1删除)',
    create_dept         BIGINT                                      COMMENT '创建部门',
    create_by           BIGINT                                      COMMENT '创建者',
    create_time         DATETIME                                    COMMENT '创建时间',
    update_by           BIGINT                                      COMMENT '更新者',
    update_time         DATETIME                                    COMMENT '更新时间',
    tenant_id           VARCHAR(20)     DEFAULT '000000'            COMMENT '租户编号',
    PRIMARY KEY (id),
    KEY idx_elder_id (elder_id),
    KEY idx_device_id (device_id),
    KEY idx_measure_time (measure_time)
) ENGINE=InnoDB COMMENT='体征数据表';

-- ----------------------------
-- 8. 服务人员表
-- ----------------------------
DROP TABLE IF EXISTS health_service_staff;
CREATE TABLE health_service_staff (
    id              BIGINT          NOT NULL                    COMMENT '主键ID',
    staff_code      VARCHAR(50)     NOT NULL                    COMMENT '人员编码',
    name            VARCHAR(50)     NOT NULL                    COMMENT '姓名',
    gender          CHAR(1)         DEFAULT '0'                 COMMENT '性别(0未知 1男 2女)',
    phone           VARCHAR(20)                                 COMMENT '手机号',
    id_card         VARCHAR(20)                                 COMMENT '身份证号',
    avatar          VARCHAR(500)                                COMMENT '头像',
    staff_type      VARCHAR(20)     NOT NULL                    COMMENT '人员类型(escort-陪诊,medical-医务,rescue-救援)',
    org_id          BIGINT                                      COMMENT '所属组织ID',
    job_title       VARCHAR(50)                                 COMMENT '职称',
    certificate     VARCHAR(200)                                COMMENT '资质证书',
    work_years      INT                                         COMMENT '工作年限',
    sort_order      INT             DEFAULT 0                   COMMENT '排序',
    status          CHAR(1)         DEFAULT '0'                 COMMENT '状态(0正常 1停用)',
    remark          VARCHAR(500)                                COMMENT '备注',
    del_flag        CHAR(1)         DEFAULT '0'                 COMMENT '删除标志(0存在 1删除)',
    create_dept     BIGINT                                      COMMENT '创建部门',
    create_by       BIGINT                                      COMMENT '创建者',
    create_time     DATETIME                                    COMMENT '创建时间',
    update_by       BIGINT                                      COMMENT '更新者',
    update_time     DATETIME                                    COMMENT '更新时间',
    tenant_id       VARCHAR(20)     DEFAULT '000000'            COMMENT '租户编号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_staff_code (staff_code, tenant_id),
    KEY idx_org_id (org_id)
) ENGINE=InnoDB COMMENT='服务人员表';

-- =============================================
-- 初始化数据
-- =============================================

-- ----------------------------
-- 设备类型初始化数据
-- ----------------------------
INSERT INTO health_device_type (id, parent_id, type_name, type_code, category, icon, sort_order, status, create_time, tenant_id) VALUES
-- 智能穿戴设备
(1001, 0, '智能穿戴', 'wearable', 'wearable', 'icon-watch', 1, '0', NOW(), '000000'),
(1011, 1001, '智能手表', 'smart_watch', 'wearable', 'icon-watch', 1, '0', NOW(), '000000'),
(1012, 1001, '智能手环', 'smart_band', 'wearable', 'icon-band', 2, '0', NOW(), '000000'),
(1013, 1001, '定位器', 'gps_tracker', 'wearable', 'icon-location', 3, '0', NOW(), '000000'),
(1014, 1001, 'SOS报警器', 'sos_alarm', 'wearable', 'icon-sos', 4, '0', NOW(), '000000'),
-- 智能检测设备
(1002, 0, '智能检测', 'detection', 'detection', 'icon-detection', 2, '0', NOW(), '000000'),
(1021, 1002, '血压计', 'blood_pressure', 'detection', 'icon-bp', 1, '0', NOW(), '000000'),
(1022, 1002, '血糖仪', 'blood_glucose', 'detection', 'icon-glucose', 2, '0', NOW(), '000000'),
(1023, 1002, '体温计', 'thermometer', 'detection', 'icon-temp', 3, '0', NOW(), '000000'),
(1024, 1002, '血氧仪', 'oximeter', 'detection', 'icon-oxygen', 4, '0', NOW(), '000000'),
(1025, 1002, '心电仪', 'ecg_monitor', 'detection', 'icon-ecg', 5, '0', NOW(), '000000'),
-- 智能物联设备
(1003, 0, '智能物联', 'iot', 'iot', 'icon-iot', 3, '0', NOW(), '000000'),
(1031, 1003, '智能床垫', 'smart_mattress', 'iot', 'icon-bed', 1, '0', NOW(), '000000'),
(1032, 1003, '跌倒检测器', 'fall_detector', 'iot', 'icon-fall', 2, '0', NOW(), '000000'),
(1033, 1003, '烟雾报警器', 'smoke_alarm', 'iot', 'icon-smoke', 3, '0', NOW(), '000000'),
(1034, 1003, '门磁传感器', 'door_sensor', 'iot', 'icon-door', 4, '0', NOW(), '000000'),
(1035, 1003, '红外感应器', 'infrared_sensor', 'iot', 'icon-infrared', 5, '0', NOW(), '000000');

-- ----------------------------
-- 组织机构初始化数据
-- ----------------------------
INSERT INTO health_organization (id, parent_id, org_code, org_name, org_type, leader, phone, address, sort_order, status, create_time, tenant_id) VALUES
(2001, 0, 'ORG001', '健康养老集团', 'company', '张总', '13800138000', '北京市朝阳区', 1, '0', NOW(), '000000'),
(2011, 2001, 'ORG001-01', '朝阳养老院', 'hospital', '李院长', '13800138001', '北京市朝阳区XX路', 1, '0', NOW(), '000000'),
(2012, 2001, 'ORG001-02', '海淀社区服务中心', 'community', '王主任', '13800138002', '北京市海淀区XX街道', 2, '0', NOW(), '000000'),
(2013, 2001, 'ORG001-03', '西城居家养老站', 'community', '赵站长', '13800138003', '北京市西城区XX社区', 3, '0', NOW(), '000000');



-- =============================================
-- 菜单数据
-- =============================================

-- ----------------------------
-- 健康管理一级菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5000, '健康管理', 0, 5, 'health', NULL, 1, 0, 'M', '0', '0', '', 'health', 103, 1, NOW(), NULL, NULL, '健康管理目录');

-- ----------------------------
-- 数据大屏菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5001, '数据大屏', 5000, 1, 'dashboard', 'health/dashboard/index', 1, 0, 'C', '0', '0', 'health:dashboard:list', 'dashboard', 103, 1, NOW(), NULL, NULL, '数据大屏菜单');

-- ----------------------------
-- 设备类型菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5010, '设备类型', 5000, 2, 'deviceType', 'health/device/type/index', 1, 0, 'C', '0', '0', 'health:deviceType:list', 'tree', 103, 1, NOW(), NULL, NULL, '设备类型菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5011, '设备类型查询', 5010, 1, '', '', 1, 0, 'F', '0', '0', 'health:deviceType:query', '#', 103, 1, NOW(), NULL, NULL, ''),
(5012, '设备类型新增', 5010, 2, '', '', 1, 0, 'F', '0', '0', 'health:deviceType:add', '#', 103, 1, NOW(), NULL, NULL, ''),
(5013, '设备类型修改', 5010, 3, '', '', 1, 0, 'F', '0', '0', 'health:deviceType:edit', '#', 103, 1, NOW(), NULL, NULL, ''),
(5014, '设备类型删除', 5010, 4, '', '', 1, 0, 'F', '0', '0', 'health:deviceType:remove', '#', 103, 1, NOW(), NULL, NULL, '');

-- ----------------------------
-- 设备管理菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5020, '设备管理', 5000, 3, 'device', 'health/device/index', 1, 0, 'C', '0', '0', 'health:device:list', 'server', 103, 1, NOW(), NULL, NULL, '设备管理菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5021, '设备查询', 5020, 1, '', '', 1, 0, 'F', '0', '0', 'health:device:query', '#', 103, 1, NOW(), NULL, NULL, ''),
(5022, '设备新增', 5020, 2, '', '', 1, 0, 'F', '0', '0', 'health:device:add', '#', 103, 1, NOW(), NULL, NULL, ''),
(5023, '设备修改', 5020, 3, '', '', 1, 0, 'F', '0', '0', 'health:device:edit', '#', 103, 1, NOW(), NULL, NULL, ''),
(5024, '设备删除', 5020, 4, '', '', 1, 0, 'F', '0', '0', 'health:device:remove', '#', 103, 1, NOW(), NULL, NULL, ''),
(5025, '设备导出', 5020, 5, '', '', 1, 0, 'F', '0', '0', 'health:device:export', '#', 103, 1, NOW(), NULL, NULL, ''),
(5026, '设备导入', 5020, 6, '', '', 1, 0, 'F', '0', '0', 'health:device:import', '#', 103, 1, NOW(), NULL, NULL, ''),
(5027, '设备绑定', 5020, 7, '', '', 1, 0, 'F', '0', '0', 'health:device:bind', '#', 103, 1, NOW(), NULL, NULL, '');

-- ----------------------------
-- 告警管理菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5030, '告警管理', 5000, 4, 'alert', 'health/alert/index', 1, 0, 'C', '0', '0', 'health:alert:list', 'alarm', 103, 1, NOW(), NULL, NULL, '告警管理菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5031, '告警查询', 5030, 1, '', '', 1, 0, 'F', '0', '0', 'health:alert:query', '#', 103, 1, NOW(), NULL, NULL, ''),
(5032, '告警处理', 5030, 2, '', '', 1, 0, 'F', '0', '0', 'health:alert:handle', '#', 103, 1, NOW(), NULL, NULL, ''),
(5033, '告警忽略', 5030, 3, '', '', 1, 0, 'F', '0', '0', 'health:alert:ignore', '#', 103, 1, NOW(), NULL, NULL, ''),
(5034, '告警导出', 5030, 4, '', '', 1, 0, 'F', '0', '0', 'health:alert:export', '#', 103, 1, NOW(), NULL, NULL, '');

-- ----------------------------
-- 组织管理菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5040, '组织管理', 5000, 5, 'organization', 'health/organization/index', 1, 0, 'C', '0', '0', 'health:organization:list', 'tree-table', 103, 1, NOW(), NULL, NULL, '组织管理菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5041, '组织查询', 5040, 1, '', '', 1, 0, 'F', '0', '0', 'health:organization:query', '#', 103, 1, NOW(), NULL, NULL, ''),
(5042, '组织新增', 5040, 2, '', '', 1, 0, 'F', '0', '0', 'health:organization:add', '#', 103, 1, NOW(), NULL, NULL, ''),
(5043, '组织修改', 5040, 3, '', '', 1, 0, 'F', '0', '0', 'health:organization:edit', '#', 103, 1, NOW(), NULL, NULL, ''),
(5044, '组织删除', 5040, 4, '', '', 1, 0, 'F', '0', '0', 'health:organization:remove', '#', 103, 1, NOW(), NULL, NULL, '');

-- ----------------------------
-- 老人管理菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5050, '老人管理', 5000, 6, 'elder', 'health/elder/index', 1, 0, 'C', '0', '0', 'health:elder:list', 'peoples', 103, 1, NOW(), NULL, NULL, '老人管理菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5051, '老人查询', 5050, 1, '', '', 1, 0, 'F', '0', '0', 'health:elder:query', '#', 103, 1, NOW(), NULL, NULL, ''),
(5052, '老人新增', 5050, 2, '', '', 1, 0, 'F', '0', '0', 'health:elder:add', '#', 103, 1, NOW(), NULL, NULL, ''),
(5053, '老人修改', 5050, 3, '', '', 1, 0, 'F', '0', '0', 'health:elder:edit', '#', 103, 1, NOW(), NULL, NULL, ''),
(5054, '老人删除', 5050, 4, '', '', 1, 0, 'F', '0', '0', 'health:elder:remove', '#', 103, 1, NOW(), NULL, NULL, ''),
(5055, '老人导出', 5050, 5, '', '', 1, 0, 'F', '0', '0', 'health:elder:export', '#', 103, 1, NOW(), NULL, NULL, ''),
(5056, '老人导入', 5050, 6, '', '', 1, 0, 'F', '0', '0', 'health:elder:import', '#', 103, 1, NOW(), NULL, NULL, '');

-- ----------------------------
-- 体征数据菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5060, '体征数据', 5000, 7, 'vitalSigns', 'health/vitalSigns/index', 1, 0, 'C', '0', '0', 'health:vitalSigns:list', 'chart', 103, 1, NOW(), NULL, NULL, '体征数据菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5061, '体征查询', 5060, 1, '', '', 1, 0, 'F', '0', '0', 'health:vitalSigns:query', '#', 103, 1, NOW(), NULL, NULL, ''),
(5062, '体征新增', 5060, 2, '', '', 1, 0, 'F', '0', '0', 'health:vitalSigns:add', '#', 103, 1, NOW(), NULL, NULL, ''),
(5063, '体征导出', 5060, 3, '', '', 1, 0, 'F', '0', '0', 'health:vitalSigns:export', '#', 103, 1, NOW(), NULL, NULL, '');

-- ----------------------------
-- 服务人员菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5070, '服务人员', 5000, 8, 'serviceStaff', 'health/serviceStaff/index', 1, 0, 'C', '0', '0', 'health:serviceStaff:list', 'user', 103, 1, NOW(), NULL, NULL, '服务人员菜单');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5071, '人员查询', 5070, 1, '', '', 1, 0, 'F', '0', '0', 'health:serviceStaff:query', '#', 103, 1, NOW(), NULL, NULL, ''),
(5072, '人员新增', 5070, 2, '', '', 1, 0, 'F', '0', '0', 'health:serviceStaff:add', '#', 103, 1, NOW(), NULL, NULL, ''),
(5073, '人员修改', 5070, 3, '', '', 1, 0, 'F', '0', '0', 'health:serviceStaff:edit', '#', 103, 1, NOW(), NULL, NULL, ''),
(5074, '人员删除', 5070, 4, '', '', 1, 0, 'F', '0', '0', 'health:serviceStaff:remove', '#', 103, 1, NOW(), NULL, NULL, '');

-- ----------------------------
-- 紧急联系人权限(挂在老人管理下)
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark) VALUES
(5080, '联系人查询', 5050, 7, '', '', 1, 0, 'F', '0', '0', 'health:emergencyContact:query', '#', 103, 1, NOW(), NULL, NULL, ''),
(5081, '联系人新增', 5050, 8, '', '', 1, 0, 'F', '0', '0', 'health:emergencyContact:add', '#', 103, 1, NOW(), NULL, NULL, ''),
(5082, '联系人修改', 5050, 9, '', '', 1, 0, 'F', '0', '0', 'health:emergencyContact:edit', '#', 103, 1, NOW(), NULL, NULL, ''),
(5083, '联系人删除', 5050, 10, '', '', 1, 0, 'F', '0', '0', 'health:emergencyContact:remove', '#', 103, 1, NOW(), NULL, NULL, '');
