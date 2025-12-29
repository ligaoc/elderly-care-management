-- =============================================
-- Tenant 461397 Demo Test Data
-- Created: 2025-12-29
-- Description: Complete demo data for tenant 461397
-- =============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Set tenant ID variable
SET @tenant_id = '461397';
SET @now = NOW();

-- =============================================
-- 1. Device Type Data
-- =============================================
INSERT INTO health_device_type (id, parent_id, type_name, type_code, category, icon, sort_order, status, create_time, tenant_id) VALUES
-- Smart Wearable Devices
(461001, 0, '智能穿戴', 'wearable', 'wearable', 'icon-watch', 1, '0', @now, @tenant_id),
(461011, 461001, '智能手表', 'smart_watch', 'wearable', 'icon-watch', 1, '0', @now, @tenant_id),
(461012, 461001, '智能手环', 'smart_band', 'wearable', 'icon-band', 2, '0', @now, @tenant_id),
(461013, 461001, '定位器', 'gps_tracker', 'wearable', 'icon-location', 3, '0', @now, @tenant_id),
(461014, 461001, 'SOS报警器', 'sos_alarm', 'wearable', 'icon-sos', 4, '0', @now, @tenant_id),
-- Smart Detection Devices
(461002, 0, '智能检测', 'detection', 'detection', 'icon-detection', 2, '0', @now, @tenant_id),
(461021, 461002, '血压计', 'blood_pressure', 'detection', 'icon-bp', 1, '0', @now, @tenant_id),
(461022, 461002, '血糖仪', 'blood_glucose', 'detection', 'icon-glucose', 2, '0', @now, @tenant_id),
(461023, 461002, '体温计', 'thermometer', 'detection', 'icon-temp', 3, '0', @now, @tenant_id),
(461024, 461002, '血氧仪', 'oximeter', 'detection', 'icon-oxygen', 4, '0', @now, @tenant_id),
-- Smart IoT Devices
(461003, 0, '智能物联', 'iot', 'iot', 'icon-iot', 3, '0', @now, @tenant_id),
(461031, 461003, '智能床垫', 'smart_mattress', 'iot', 'icon-bed', 1, '0', @now, @tenant_id),
(461032, 461003, '跌倒检测器', 'fall_detector', 'iot', 'icon-fall', 2, '0', @now, @tenant_id),
(461033, 461003, '烟雾报警器', 'smoke_alarm', 'iot', 'icon-smoke', 3, '0', @now, @tenant_id);

-- =============================================
-- 2. Organization Data
-- =============================================
INSERT INTO health_organization (id, parent_id, org_code, org_name, org_type, leader, phone, email, address, sort_order, status, create_time, tenant_id) VALUES
(461100, 0, 'ORG-461-001', '阳光健康养老集团', 'company', '张明华', '13912345678', 'zhangmh@yangguang.com', '上海市浦东新区张江高科技园区', 1, '0', @now, @tenant_id),
(461101, 461100, 'ORG-461-002', '浦东康乐养老院', 'hospital', '李秀英', '13912345679', 'lxy@yangguang.com', '上海市浦东新区康桥路188号', 1, '0', @now, @tenant_id),
(461102, 461100, 'ORG-461-003', '徐汇区日间照料中心', 'community', '王建国', '13912345680', 'wjg@yangguang.com', '上海市徐汇区漕溪北路200号', 2, '0', @now, @tenant_id),
(461103, 461100, 'ORG-461-004', '静安区居家养老服务站', 'community', '陈丽萍', '13912345681', 'clp@yangguang.com', '上海市静安区南京西路1000号', 3, '0', @now, @tenant_id),
(461104, 461101, 'ORG-461-005', '康乐养老院一区', 'hospital', '赵志强', '13912345682', 'zzq@yangguang.com', '上海市浦东新区康桥路188号A栋', 1, '0', @now, @tenant_id),
(461105, 461101, 'ORG-461-006', '康乐养老院二区', 'hospital', '刘美玲', '13912345683', 'lml@yangguang.com', '上海市浦东新区康桥路188号B栋', 2, '0', @now, @tenant_id);


-- =============================================
-- 3. Elder Data (15 elders)
-- =============================================
INSERT INTO health_elder (id, user_code, name, gender, birthday, age, id_card, phone, org_id, address, blood_type, height, weight, bmi, medical_history, care_level, risk_level, status, create_time, tenant_id) VALUES
(461201, 'ELD-461-001', '张大爷', '1', '1945-03-15', 79, '310101194503150011', '13800001001', 461101, '上海市浦东新区康桥路188号101室', 'A', 168.00, 65.00, 23.03, '["高血压","糖尿病"]', '1', '2', '0', @now, @tenant_id),
(461202, 'ELD-461-002', '李奶奶', '2', '1948-07-22', 76, '310101194807220022', '13800001002', 461101, '上海市浦东新区康桥路188号102室', 'B', 155.00, 52.00, 21.64, '["冠心病"]', '1', '2', '0', @now, @tenant_id),
(461203, 'ELD-461-003', '王大伯', '1', '1942-11-08', 82, '310101194211080033', '13800001003', 461101, '上海市浦东新区康桥路188号103室', 'O', 172.00, 70.00, 23.66, '["高血压","关节炎"]', '2', '2', '0', @now, @tenant_id),
(461204, 'ELD-461-004', '陈阿姨', '2', '1950-05-18', 74, '310101195005180044', '13800001004', 461101, '上海市浦东新区康桥路188号104室', 'AB', 158.00, 55.00, 22.03, '["骨质疏松"]', '1', '1', '0', @now, @tenant_id),
(461205, 'ELD-461-005', '刘爷爷', '1', '1940-09-25', 84, '310101194009250055', '13800001005', 461104, '上海市浦东新区康桥路188号A栋201室', 'A', 165.00, 58.00, 21.30, '["帕金森","高血压"]', '2', '3', '0', @now, @tenant_id),
(461206, 'ELD-461-006', '赵奶奶', '2', '1946-12-03', 78, '310101194612030066', '13800001006', 461104, '上海市浦东新区康桥路188号A栋202室', 'B', 152.00, 48.00, 20.78, '["糖尿病","白内障"]', '1', '2', '0', @now, @tenant_id),
(461207, 'ELD-461-007', '孙大爷', '1', '1944-04-12', 80, '310101194404120077', '13800001007', 461104, '上海市浦东新区康桥路188号A栋203室', 'O', 170.00, 68.00, 23.53, '["心律不齐"]', '1', '2', '0', @now, @tenant_id),
(461208, 'ELD-461-008', '周阿姨', '2', '1952-08-30', 72, '310101195208300088', '13800001008', 461105, '上海市浦东新区康桥路188号B栋101室', 'A', 160.00, 58.00, 22.66, '[]', '1', '1', '0', @now, @tenant_id),
(461209, 'ELD-461-009', '吴爷爷', '1', '1938-01-20', 86, '310101193801200099', '13800001009', 461105, '上海市浦东新区康桥路188号B栋102室', 'AB', 168.00, 62.00, 21.97, '["脑梗后遗症","高血压"]', '3', '3', '0', @now, @tenant_id),
(461210, 'ELD-461-010', '郑奶奶', '2', '1949-06-15', 75, '310101194906150100', '13800001010', 461102, '上海市徐汇区漕溪北路200号', 'B', 156.00, 50.00, 20.55, '["高血脂"]', '1', '1', '0', @now, @tenant_id),
(461211, 'ELD-461-011', '黄大伯', '1', '1943-10-28', 81, '310101194310280111', '13800001011', 461102, '上海市徐汇区漕溪北路200号', 'O', 175.00, 72.00, 23.51, '["前列腺增生"]', '1', '1', '0', @now, @tenant_id),
(461212, 'ELD-461-012', '林阿姨', '2', '1951-02-14', 73, '310101195102140122', '13800001012', 461103, '上海市静安区南京西路1000号', 'A', 162.00, 56.00, 21.34, '["甲状腺结节"]', '1', '1', '0', @now, @tenant_id),
(461213, 'ELD-461-013', '何爷爷', '1', '1947-07-07', 77, '310101194707070133', '13800001013', 461103, '上海市静安区南京西路1000号', 'B', 166.00, 64.00, 23.23, '["高血压","痛风"]', '1', '2', '0', @now, @tenant_id),
(461214, 'ELD-461-014', '马奶奶', '2', '1945-11-22', 79, '310101194511220144', '13800001014', 461103, '上海市静安区南京西路1000号', 'O', 154.00, 46.00, 19.40, '["骨质疏松","贫血"]', '2', '2', '0', @now, @tenant_id),
(461215, 'ELD-461-015', '高大爷', '1', '1941-03-05', 83, '310101194103050155', '13800001015', 461101, '上海市浦东新区康桥路188号105室', 'A', 169.00, 66.00, 23.11, '["慢阻肺","高血压"]', '2', '3', '0', @now, @tenant_id);


-- =============================================
-- 4. Emergency Contact Data
-- =============================================
INSERT INTO health_emergency_contact (id, elder_id, name, relation, phone, is_primary, notify_alert, notify_type, sort_order, status, create_time, tenant_id) VALUES
-- Zhang Daye contacts
(461301, 461201, '张小明', 'child', '13900001001', '1', '1', 'both', 1, '0', @now, @tenant_id),
(461302, 461201, '张小红', 'child', '13900001002', '0', '1', 'sms', 2, '0', @now, @tenant_id),
-- Li Nainai contacts
(461303, 461202, '李建华', 'child', '13900001003', '1', '1', 'both', 1, '0', @now, @tenant_id),
-- Wang Dabo contacts
(461304, 461203, '王志刚', 'child', '13900001004', '1', '1', 'phone', 1, '0', @now, @tenant_id),
(461305, 461203, '王秀芳', 'spouse', '13900001005', '0', '1', 'sms', 2, '0', @now, @tenant_id),
-- Chen Ayi contacts
(461306, 461204, '陈伟', 'child', '13900001006', '1', '1', 'both', 1, '0', @now, @tenant_id),
-- Liu Yeye contacts
(461307, 461205, '刘强', 'child', '13900001007', '1', '1', 'both', 1, '0', @now, @tenant_id),
(461308, 461205, '刘芳', 'child', '13900001008', '0', '1', 'sms', 2, '0', @now, @tenant_id),
-- Zhao Nainai contacts
(461309, 461206, '赵军', 'child', '13900001009', '1', '1', 'phone', 1, '0', @now, @tenant_id),
-- Sun Daye contacts
(461310, 461207, '孙磊', 'child', '13900001010', '1', '1', 'both', 1, '0', @now, @tenant_id),
-- Zhou Ayi contacts
(461311, 461208, '周杰', 'child', '13900001011', '1', '1', 'sms', 1, '0', @now, @tenant_id),
-- Wu Yeye contacts
(461312, 461209, '吴涛', 'child', '13900001012', '1', '1', 'both', 1, '0', @now, @tenant_id),
(461313, 461209, '吴娟', 'child', '13900001013', '0', '1', 'phone', 2, '0', @now, @tenant_id),
-- Zheng Nainai contacts
(461314, 461210, '郑浩', 'child', '13900001014', '1', '1', 'both', 1, '0', @now, @tenant_id),
-- Huang Dabo contacts
(461315, 461211, '黄勇', 'child', '13900001015', '1', '1', 'sms', 1, '0', @now, @tenant_id),
-- Lin Ayi contacts
(461316, 461212, '林峰', 'child', '13900001016', '1', '1', 'both', 1, '0', @now, @tenant_id),
-- He Yeye contacts
(461317, 461213, '何伟', 'child', '13900001017', '1', '1', 'phone', 1, '0', @now, @tenant_id),
-- Ma Nainai contacts
(461318, 461214, '马超', 'child', '13900001018', '1', '1', 'both', 1, '0', @now, @tenant_id),
-- Gao Daye contacts
(461319, 461215, '高明', 'child', '13900001019', '1', '1', 'both', 1, '0', @now, @tenant_id),
(461320, 461215, '高丽', 'child', '13900001020', '0', '1', 'sms', 2, '0', @now, @tenant_id);


-- =============================================
-- 5. Device Data (20 devices, some online)
-- =============================================
INSERT INTO health_device (id, device_code, device_name, device_type_id, device_model, manufacturer, serial_number, mac_address, firmware_version, bind_user_id, bind_time, online_status, last_online_time, battery_level, signal_strength, location, status, create_time, tenant_id) VALUES
-- Smart Watch
(461401, 'DEV-461-001', '智能健康手表A1', 461011, 'HW-A1', '华为', 'SN20241201001', 'AA:BB:CC:DD:EE:01', 'v2.1.0', 461201, @now, '1', @now, 85, 75, '上海市浦东新区康桥路188号101室', '0', @now, @tenant_id),
(461402, 'DEV-461-002', '智能健康手表A1', 461011, 'HW-A1', '华为', 'SN20241201002', 'AA:BB:CC:DD:EE:02', 'v2.1.0', 461202, @now, '1', @now, 92, 80, '上海市浦东新区康桥路188号102室', '0', @now, @tenant_id),
(461403, 'DEV-461-003', '智能健康手表B2', 461011, 'XM-B2', '小米', 'SN20241201003', 'AA:BB:CC:DD:EE:03', 'v3.0.1', 461203, @now, '1', @now, 68, 70, '上海市浦东新区康桥路188号103室', '0', @now, @tenant_id),
(461404, 'DEV-461-004', '智能健康手表B2', 461011, 'XM-B2', '小米', 'SN20241201004', 'AA:BB:CC:DD:EE:04', 'v3.0.1', 461204, @now, '0', DATE_SUB(@now, INTERVAL 2 HOUR), 45, 60, '上海市浦东新区康桥路188号104室', '0', @now, @tenant_id),
(461405, 'DEV-461-005', '智能健康手表A1', 461011, 'HW-A1', '华为', 'SN20241201005', 'AA:BB:CC:DD:EE:05', 'v2.1.0', 461205, @now, '1', @now, 78, 85, '上海市浦东新区康桥路188号A栋201室', '0', @now, @tenant_id),
-- Smart Band
(461406, 'DEV-461-006', '智能健康手环C1', 461012, 'OPPO-C1', 'OPPO', 'SN20241201006', 'AA:BB:CC:DD:EE:06', 'v1.5.0', 461206, @now, '1', @now, 95, 90, '上海市浦东新区康桥路188号A栋202室', '0', @now, @tenant_id),
(461407, 'DEV-461-007', '智能健康手环C1', 461012, 'OPPO-C1', 'OPPO', 'SN20241201007', 'AA:BB:CC:DD:EE:07', 'v1.5.0', 461207, @now, '1', @now, 88, 75, '上海市浦东新区康桥路188号A栋203室', '0', @now, @tenant_id),
(461408, 'DEV-461-008', '智能健康手环D2', 461012, 'VIVO-D2', 'VIVO', 'SN20241201008', 'AA:BB:CC:DD:EE:08', 'v2.0.0', 461208, @now, '0', DATE_SUB(@now, INTERVAL 5 HOUR), 32, 55, '上海市浦东新区康桥路188号B栋101室', '0', @now, @tenant_id),
-- GPS Tracker
(461409, 'DEV-461-009', 'GPS定位器E1', 461013, 'GPS-E1', '中兴', 'SN20241201009', 'AA:BB:CC:DD:EE:09', 'v1.2.0', 461209, @now, '1', @now, 72, 80, '上海市浦东新区康桥路188号B栋102室', '0', @now, @tenant_id),
(461410, 'DEV-461-010', 'GPS定位器E1', 461013, 'GPS-E1', '中兴', 'SN20241201010', 'AA:BB:CC:DD:EE:10', 'v1.2.0', 461210, @now, '1', @now, 65, 70, '上海市徐汇区漕溪北路200号', '0', @now, @tenant_id),
-- Blood Pressure Monitor
(461411, 'DEV-461-011', '智能血压计F1', 461021, 'OMRON-F1', '欧姆龙', 'SN20241201011', 'AA:BB:CC:DD:EE:11', 'v1.0.0', 461201, @now, '1', @now, 100, 95, '上海市浦东新区康桥路188号101室', '0', @now, @tenant_id),
(461412, 'DEV-461-012', '智能血压计F1', 461021, 'OMRON-F1', '欧姆龙', 'SN20241201012', 'AA:BB:CC:DD:EE:12', 'v1.0.0', 461203, @now, '1', @now, 100, 90, '上海市浦东新区康桥路188号103室', '0', @now, @tenant_id),
-- Blood Glucose Meter
(461413, 'DEV-461-013', '智能血糖仪G1', 461022, 'ROCHE-G1', '罗氏', 'SN20241201013', 'AA:BB:CC:DD:EE:13', 'v2.0.0', 461201, @now, '1', @now, 100, 85, '上海市浦东新区康桥路188号101室', '0', @now, @tenant_id),
(461414, 'DEV-461-014', '智能血糖仪G1', 461022, 'ROCHE-G1', '罗氏', 'SN20241201014', 'AA:BB:CC:DD:EE:14', 'v2.0.0', 461206, @now, '0', DATE_SUB(@now, INTERVAL 1 DAY), 100, 80, '上海市浦东新区康桥路188号A栋202室', '0', @now, @tenant_id),
-- Oximeter
(461415, 'DEV-461-015', '智能血氧仪H1', 461024, 'YUWELL-H1', '鱼跃', 'SN20241201015', 'AA:BB:CC:DD:EE:15', 'v1.1.0', 461205, @now, '1', @now, 100, 88, '上海市浦东新区康桥路188号A栋201室', '0', @now, @tenant_id),
(461416, 'DEV-461-016', '智能血氧仪H1', 461024, 'YUWELL-H1', '鱼跃', 'SN20241201016', 'AA:BB:CC:DD:EE:16', 'v1.1.0', 461209, @now, '1', @now, 100, 82, '上海市浦东新区康桥路188号B栋102室', '0', @now, @tenant_id),
-- Smart Mattress
(461417, 'DEV-461-017', '智能监测床垫I1', 461031, 'SLEEPACE-I1', '享睡', 'SN20241201017', 'AA:BB:CC:DD:EE:17', 'v3.0.0', 461209, @now, '1', @now, 100, 95, '上海市浦东新区康桥路188号B栋102室', '0', @now, @tenant_id),
(461418, 'DEV-461-018', '智能监测床垫I1', 461031, 'SLEEPACE-I1', '享睡', 'SN20241201018', 'AA:BB:CC:DD:EE:18', 'v3.0.0', 461215, @now, '1', @now, 100, 92, '上海市浦东新区康桥路188号105室', '0', @now, @tenant_id),
-- Fall Detector
(461419, 'DEV-461-019', '跌倒检测器J1', 461032, 'FALL-J1', '海康威视', 'SN20241201019', 'AA:BB:CC:DD:EE:19', 'v2.0.0', 461205, @now, '1', @now, 100, 90, '上海市浦东新区康桥路188号A栋201室', '0', @now, @tenant_id),
(461420, 'DEV-461-020', '跌倒检测器J1', 461032, 'FALL-J1', '海康威视', 'SN20241201020', 'AA:BB:CC:DD:EE:20', 'v2.0.0', 461214, @now, '0', DATE_SUB(@now, INTERVAL 3 HOUR), 100, 85, '上海市静安区南京西路1000号', '0', @now, @tenant_id);


-- =============================================
-- 6. Vital Signs Data (Last 7 days)
-- =============================================
-- Zhang Daye vital signs
INSERT INTO health_vital_signs (id, elder_id, device_id, measure_time, temperature, heart_rate, systolic_pressure, diastolic_pressure, blood_oxygen, blood_sugar, respiratory_rate, steps, is_abnormal, abnormal_items, create_time, tenant_id) VALUES
(461501, 461201, 461401, DATE_SUB(@now, INTERVAL 6 DAY), 36.5, 72, 138, 88, 97, 6.8, 18, 3200, '0', NULL, DATE_SUB(@now, INTERVAL 6 DAY), @tenant_id),
(461502, 461201, 461401, DATE_SUB(@now, INTERVAL 5 DAY), 36.6, 75, 142, 90, 96, 7.2, 17, 2800, '1', '血压偏高', DATE_SUB(@now, INTERVAL 5 DAY), @tenant_id),
(461503, 461201, 461401, DATE_SUB(@now, INTERVAL 4 DAY), 36.4, 70, 135, 85, 98, 6.5, 18, 3500, '0', NULL, DATE_SUB(@now, INTERVAL 4 DAY), @tenant_id),
(461504, 461201, 461401, DATE_SUB(@now, INTERVAL 3 DAY), 36.7, 78, 140, 88, 97, 7.0, 19, 2600, '0', NULL, DATE_SUB(@now, INTERVAL 3 DAY), @tenant_id),
(461505, 461201, 461401, DATE_SUB(@now, INTERVAL 2 DAY), 36.5, 73, 136, 86, 98, 6.6, 18, 3100, '0', NULL, DATE_SUB(@now, INTERVAL 2 DAY), @tenant_id),
(461506, 461201, 461401, DATE_SUB(@now, INTERVAL 1 DAY), 36.8, 76, 145, 92, 96, 7.5, 17, 2400, '1', '血压偏高,血糖偏高', DATE_SUB(@now, INTERVAL 1 DAY), @tenant_id),
(461507, 461201, 461401, @now, 36.6, 74, 138, 87, 97, 6.9, 18, 1800, '0', NULL, @now, @tenant_id),

-- Li Nainai vital signs
(461508, 461202, 461402, DATE_SUB(@now, INTERVAL 6 DAY), 36.3, 68, 125, 78, 98, 5.2, 16, 2100, '0', NULL, DATE_SUB(@now, INTERVAL 6 DAY), @tenant_id),
(461509, 461202, 461402, DATE_SUB(@now, INTERVAL 5 DAY), 36.4, 70, 128, 80, 97, 5.4, 17, 1900, '0', NULL, DATE_SUB(@now, INTERVAL 5 DAY), @tenant_id),
(461510, 461202, 461402, DATE_SUB(@now, INTERVAL 4 DAY), 36.5, 72, 130, 82, 96, 5.6, 16, 2200, '0', NULL, DATE_SUB(@now, INTERVAL 4 DAY), @tenant_id),
(461511, 461202, 461402, DATE_SUB(@now, INTERVAL 3 DAY), 36.4, 69, 126, 79, 98, 5.3, 17, 2000, '0', NULL, DATE_SUB(@now, INTERVAL 3 DAY), @tenant_id),
(461512, 461202, 461402, DATE_SUB(@now, INTERVAL 2 DAY), 36.6, 71, 132, 84, 97, 5.5, 16, 1800, '0', NULL, DATE_SUB(@now, INTERVAL 2 DAY), @tenant_id),
(461513, 461202, 461402, DATE_SUB(@now, INTERVAL 1 DAY), 36.5, 68, 124, 77, 98, 5.1, 17, 2300, '0', NULL, DATE_SUB(@now, INTERVAL 1 DAY), @tenant_id),
(461514, 461202, 461402, @now, 36.4, 70, 127, 80, 97, 5.4, 16, 1500, '0', NULL, @now, @tenant_id),

-- Wang Dabo vital signs
(461515, 461203, 461403, DATE_SUB(@now, INTERVAL 6 DAY), 36.6, 80, 148, 95, 95, 5.8, 20, 1800, '1', '血压偏高', DATE_SUB(@now, INTERVAL 6 DAY), @tenant_id),
(461516, 461203, 461403, DATE_SUB(@now, INTERVAL 5 DAY), 36.5, 78, 145, 92, 96, 5.6, 19, 2000, '1', '血压偏高', DATE_SUB(@now, INTERVAL 5 DAY), @tenant_id),
(461517, 461203, 461403, DATE_SUB(@now, INTERVAL 4 DAY), 36.7, 82, 150, 96, 94, 5.9, 21, 1600, '1', '血压偏高,血氧偏低', DATE_SUB(@now, INTERVAL 4 DAY), @tenant_id),
(461518, 461203, 461403, DATE_SUB(@now, INTERVAL 3 DAY), 36.4, 76, 142, 90, 96, 5.5, 19, 2200, '0', NULL, DATE_SUB(@now, INTERVAL 3 DAY), @tenant_id),
(461519, 461203, 461403, DATE_SUB(@now, INTERVAL 2 DAY), 36.6, 79, 146, 93, 95, 5.7, 20, 1900, '1', '血压偏高', DATE_SUB(@now, INTERVAL 2 DAY), @tenant_id),
(461520, 461203, 461403, DATE_SUB(@now, INTERVAL 1 DAY), 36.5, 77, 143, 91, 96, 5.6, 19, 2100, '0', NULL, DATE_SUB(@now, INTERVAL 1 DAY), @tenant_id),
(461521, 461203, 461403, @now, 36.6, 78, 144, 92, 96, 5.7, 19, 1200, '0', NULL, @now, @tenant_id),

-- Liu Yeye vital signs (High risk elder)
(461522, 461205, 461405, DATE_SUB(@now, INTERVAL 6 DAY), 36.8, 85, 155, 98, 93, 6.2, 22, 800, '1', '血压偏高,血氧偏低', DATE_SUB(@now, INTERVAL 6 DAY), @tenant_id),
(461523, 461205, 461405, DATE_SUB(@now, INTERVAL 5 DAY), 36.7, 82, 152, 95, 94, 6.0, 21, 900, '1', '血压偏高', DATE_SUB(@now, INTERVAL 5 DAY), @tenant_id),
(461524, 461205, 461405, DATE_SUB(@now, INTERVAL 4 DAY), 36.9, 88, 158, 100, 92, 6.5, 23, 600, '1', '血压偏高,血氧偏低', DATE_SUB(@now, INTERVAL 4 DAY), @tenant_id),
(461525, 461205, 461405, DATE_SUB(@now, INTERVAL 3 DAY), 36.6, 80, 148, 93, 95, 5.8, 20, 1000, '1', '血压偏高', DATE_SUB(@now, INTERVAL 3 DAY), @tenant_id),
(461526, 461205, 461405, DATE_SUB(@now, INTERVAL 2 DAY), 36.8, 84, 154, 97, 93, 6.3, 22, 700, '1', '血压偏高,血氧偏低', DATE_SUB(@now, INTERVAL 2 DAY), @tenant_id),
(461527, 461205, 461405, DATE_SUB(@now, INTERVAL 1 DAY), 36.7, 81, 150, 94, 94, 6.1, 21, 850, '1', '血压偏高', DATE_SUB(@now, INTERVAL 1 DAY), @tenant_id),
(461528, 461205, 461405, @now, 36.8, 83, 152, 96, 93, 6.2, 21, 500, '1', '血压偏高,血氧偏低', @now, @tenant_id),

-- Zhao Nainai vital signs
(461529, 461206, 461406, DATE_SUB(@now, INTERVAL 6 DAY), 36.4, 72, 130, 82, 97, 8.2, 17, 1500, '1', '血糖偏高', DATE_SUB(@now, INTERVAL 6 DAY), @tenant_id),
(461530, 461206, 461406, DATE_SUB(@now, INTERVAL 5 DAY), 36.5, 70, 128, 80, 98, 7.8, 16, 1700, '1', '血糖偏高', DATE_SUB(@now, INTERVAL 5 DAY), @tenant_id),
(461531, 461206, 461406, DATE_SUB(@now, INTERVAL 4 DAY), 36.3, 68, 125, 78, 97, 7.5, 17, 1600, '0', NULL, DATE_SUB(@now, INTERVAL 4 DAY), @tenant_id),
(461532, 461206, 461406, DATE_SUB(@now, INTERVAL 3 DAY), 36.4, 71, 129, 81, 98, 8.0, 16, 1400, '1', '血糖偏高', DATE_SUB(@now, INTERVAL 3 DAY), @tenant_id),
(461533, 461206, 461406, DATE_SUB(@now, INTERVAL 2 DAY), 36.5, 69, 126, 79, 97, 7.6, 17, 1800, '0', NULL, DATE_SUB(@now, INTERVAL 2 DAY), @tenant_id),
(461534, 461206, 461406, DATE_SUB(@now, INTERVAL 1 DAY), 36.4, 70, 127, 80, 98, 7.9, 16, 1550, '1', '血糖偏高', DATE_SUB(@now, INTERVAL 1 DAY), @tenant_id),
(461535, 461206, 461406, @now, 36.5, 71, 128, 81, 97, 7.7, 17, 1000, '0', NULL, @now, @tenant_id),

-- Wu Yeye vital signs (High risk elder)
(461536, 461209, 461409, DATE_SUB(@now, INTERVAL 6 DAY), 36.9, 90, 162, 102, 91, 5.5, 24, 300, '1', '血压偏高,血氧偏低,心率偏高', DATE_SUB(@now, INTERVAL 6 DAY), @tenant_id),
(461537, 461209, 461409, DATE_SUB(@now, INTERVAL 5 DAY), 36.7, 86, 158, 98, 92, 5.3, 22, 400, '1', '血压偏高,血氧偏低', DATE_SUB(@now, INTERVAL 5 DAY), @tenant_id),
(461538, 461209, 461409, DATE_SUB(@now, INTERVAL 4 DAY), 37.0, 92, 165, 105, 90, 5.6, 25, 250, '1', '血压偏高,血氧偏低,心率偏高', DATE_SUB(@now, INTERVAL 4 DAY), @tenant_id),
(461539, 461209, 461409, DATE_SUB(@now, INTERVAL 3 DAY), 36.8, 88, 160, 100, 91, 5.4, 23, 350, '1', '血压偏高,血氧偏低', DATE_SUB(@now, INTERVAL 3 DAY), @tenant_id),
(461540, 461209, 461409, DATE_SUB(@now, INTERVAL 2 DAY), 36.6, 84, 155, 96, 93, 5.2, 21, 450, '1', '血压偏高', DATE_SUB(@now, INTERVAL 2 DAY), @tenant_id),
(461541, 461209, 461409, DATE_SUB(@now, INTERVAL 1 DAY), 36.9, 89, 161, 101, 91, 5.5, 24, 280, '1', '血压偏高,血氧偏低', DATE_SUB(@now, INTERVAL 1 DAY), @tenant_id),
(461542, 461209, 461409, @now, 36.8, 87, 159, 99, 92, 5.4, 23, 200, '1', '血压偏高,血氧偏低', @now, @tenant_id);


-- =============================================
-- 7. Alert Data (Last 7 days)
-- =============================================
INSERT INTO health_device_alert (id, alert_code, device_id, user_id, alert_type, alert_level, alert_title, alert_content, alert_time, location, handle_status, handle_type, handle_user_id, handle_time, handle_result, create_time, tenant_id) VALUES
-- Today alerts (Pending)
(461601, 'ALT-461-001', 461405, 461205, 'vital', '3', '刘爷爷血压异常告警', '检测到血压152/96mmHg，超出正常范围，请及时关注', @now, '上海市浦东新区康桥路188号A栋201室', '0', NULL, NULL, NULL, NULL, @now, @tenant_id),
(461602, 'ALT-461-002', 461409, 461209, 'vital', '3', '吴爷爷血氧异常告警', '检测到血氧饱和度92%，低于正常值，请及时关注', @now, '上海市浦东新区康桥路188号B栋102室', '0', NULL, NULL, NULL, NULL, @now, @tenant_id),
(461603, 'ALT-461-003', 461408, 461208, 'battery', '1', '周阿姨设备低电量告警', '智能手环电量仅剩32%，请及时充电', DATE_SUB(@now, INTERVAL 2 HOUR), '上海市浦东新区康桥路188号B栋101室', '0', NULL, NULL, NULL, NULL, DATE_SUB(@now, INTERVAL 2 HOUR), @tenant_id),
(461604, 'ALT-461-004', 461420, 461214, 'offline', '2', '马奶奶跌倒检测器离线', '设备已离线超过3小时，请检查设备状态', DATE_SUB(@now, INTERVAL 3 HOUR), '上海市静安区南京西路1000号', '0', NULL, NULL, NULL, NULL, DATE_SUB(@now, INTERVAL 3 HOUR), @tenant_id),

-- Yesterday alerts (Processing)
(461605, 'ALT-461-005', 461401, 461201, 'vital', '2', '张大爷血糖偏高告警', '检测到血糖7.5mmol/L，略高于正常范围', DATE_SUB(@now, INTERVAL 1 DAY), '上海市浦东新区康桥路188号101室', '1', 'phone', NULL, NULL, NULL, DATE_SUB(@now, INTERVAL 1 DAY), @tenant_id),
(461606, 'ALT-461-006', 461405, 461205, 'vital', '3', '刘爷爷血氧异常告警', '检测到血氧饱和度93%，低于正常值', DATE_SUB(@now, INTERVAL 1 DAY), '上海市浦东新区康桥路188号A栋201室', '1', 'onsite', NULL, NULL, NULL, DATE_SUB(@now, INTERVAL 1 DAY), @tenant_id),

-- 2 days ago alerts (Handled)
(461607, 'ALT-461-007', 461403, 461203, 'vital', '2', '王大伯血压偏高告警', '检测到血压146/93mmHg，超出正常范围', DATE_SUB(@now, INTERVAL 2 DAY), '上海市浦东新区康桥路188号103室', '2', 'phone', 1, DATE_SUB(@now, INTERVAL 2 DAY) + INTERVAL 30 MINUTE, '已电话联系家属，老人表示身体无明显不适，建议继续观察', DATE_SUB(@now, INTERVAL 2 DAY), @tenant_id),
(461608, 'ALT-461-008', 461409, 461209, 'vital', '3', '吴爷爷心率异常告警', '检测到心率89次/分，偏高', DATE_SUB(@now, INTERVAL 2 DAY), '上海市浦东新区康桥路188号B栋102室', '2', 'onsite', 1, DATE_SUB(@now, INTERVAL 2 DAY) + INTERVAL 45 MINUTE, '护理人员已到现场查看，老人情绪激动导致心率升高，已安抚', DATE_SUB(@now, INTERVAL 2 DAY), @tenant_id),

-- 3 days ago alerts (Handled)
(461609, 'ALT-461-009', 461406, 461206, 'vital', '2', '赵奶奶血糖偏高告警', '检测到血糖8.0mmol/L，高于正常范围', DATE_SUB(@now, INTERVAL 3 DAY), '上海市浦东新区康桥路188号A栋202室', '2', 'phone', 1, DATE_SUB(@now, INTERVAL 3 DAY) + INTERVAL 20 MINUTE, '已通知家属，建议调整饮食，减少糖分摄入', DATE_SUB(@now, INTERVAL 3 DAY), @tenant_id),
(461610, 'ALT-461-010', 461405, 461205, 'vital', '3', '刘爷爷血压异常告警', '检测到血压154/97mmHg，严重超标', DATE_SUB(@now, INTERVAL 3 DAY), '上海市浦东新区康桥路188号A栋201室', '2', 'rescue', 1, DATE_SUB(@now, INTERVAL 3 DAY) + INTERVAL 15 MINUTE, '已安排医护人员上门检查，给予降压药物，血压已恢复正常', DATE_SUB(@now, INTERVAL 3 DAY), @tenant_id),

-- 4 days ago alerts (Handled)
(461611, 'ALT-461-011', 461403, 461203, 'vital', '2', '王大伯血氧偏低告警', '检测到血氧饱和度94%，略低于正常值', DATE_SUB(@now, INTERVAL 4 DAY), '上海市浦东新区康桥路188号103室', '2', 'phone', 1, DATE_SUB(@now, INTERVAL 4 DAY) + INTERVAL 25 MINUTE, '电话确认老人状态良好，可能是测量时手指位置不当', DATE_SUB(@now, INTERVAL 4 DAY), @tenant_id),
(461612, 'ALT-461-012', 461409, 461209, 'vital', '3', '吴爷爷多项指标异常', '血压165/105mmHg，血氧90%，心率92次/分，多项指标异常', DATE_SUB(@now, INTERVAL 4 DAY), '上海市浦东新区康桥路188号B栋102室', '2', 'rescue', 1, DATE_SUB(@now, INTERVAL 4 DAY) + INTERVAL 10 MINUTE, '紧急派遣医护人员，已送医检查，目前情况稳定', DATE_SUB(@now, INTERVAL 4 DAY), @tenant_id),

-- 5 days ago alerts (Ignored)
(461613, 'ALT-461-013', 461404, 461204, 'offline', '1', '陈阿姨设备离线告警', '智能手表已离线超过2小时', DATE_SUB(@now, INTERVAL 5 DAY), '上海市浦东新区康桥路188号104室', '3', NULL, 1, DATE_SUB(@now, INTERVAL 5 DAY) + INTERVAL 3 HOUR, '确认为设备充电导致离线，非异常情况', DATE_SUB(@now, INTERVAL 5 DAY), @tenant_id),

-- 6 days ago alerts (Handled)
(461614, 'ALT-461-014', 461401, 461201, 'vital', '2', '张大爷血压偏高告警', '检测到血压142/90mmHg，超出正常范围', DATE_SUB(@now, INTERVAL 6 DAY), '上海市浦东新区康桥路188号101室', '2', 'phone', 1, DATE_SUB(@now, INTERVAL 6 DAY) + INTERVAL 35 MINUTE, '已联系家属，老人表示刚运动完，休息后血压恢复正常', DATE_SUB(@now, INTERVAL 6 DAY), @tenant_id),
(461615, 'ALT-461-015', 461405, 461205, 'vital', '3', '刘爷爷血氧异常告警', '检测到血氧饱和度93%，低于正常值', DATE_SUB(@now, INTERVAL 6 DAY), '上海市浦东新区康桥路188号A栋201室', '2', 'onsite', 1, DATE_SUB(@now, INTERVAL 6 DAY) + INTERVAL 20 MINUTE, '护理人员已到场，给予吸氧处理，血氧恢复正常', DATE_SUB(@now, INTERVAL 6 DAY), @tenant_id);


-- =============================================
-- 8. Service Staff Data
-- =============================================
INSERT INTO health_service_staff (id, staff_code, name, gender, phone, id_card, staff_type, org_id, job_title, certificate, work_years, sort_order, status, create_time, tenant_id) VALUES
-- Escort Staff
(461701, 'STF-461-001', '张护士', '2', '13811111001', '310101199001010011', 'escort', 461101, '护士', '护士执业证书', 8, 1, '0', @now, @tenant_id),
(461702, 'STF-461-002', '李护士', '2', '13811111002', '310101199202020022', 'escort', 461101, '护士', '护士执业证书', 6, 2, '0', @now, @tenant_id),
(461703, 'STF-461-003', '王护工', '1', '13811111003', '310101198503030033', 'escort', 461104, '护工', '养老护理员证书', 10, 3, '0', @now, @tenant_id),
(461704, 'STF-461-004', '陈护工', '2', '13811111004', '310101198804040044', 'escort', 461105, '护工', '养老护理员证书', 7, 4, '0', @now, @tenant_id),
-- Medical Staff
(461705, 'STF-461-005', '刘医生', '1', '13811111005', '310101198005050055', 'medical', 461101, '主治医师', '医师执业证书', 15, 1, '0', @now, @tenant_id),
(461706, 'STF-461-006', '赵医生', '2', '13811111006', '310101198206060066', 'medical', 461101, '住院医师', '医师执业证书', 12, 2, '0', @now, @tenant_id),
(461707, 'STF-461-007', '孙医生', '1', '13811111007', '310101198507070077', 'medical', 461102, '全科医生', '医师执业证书', 10, 3, '0', @now, @tenant_id),
-- Rescue Staff
(461708, 'STF-461-008', '周队长', '1', '13811111008', '310101198008080088', 'rescue', 461100, '救援队长', '急救员证书', 12, 1, '0', @now, @tenant_id),
(461709, 'STF-461-009', '吴队员', '1', '13811111009', '310101199009090099', 'rescue', 461100, '救援队员', '急救员证书', 5, 2, '0', @now, @tenant_id),
(461710, 'STF-461-010', '郑队员', '1', '13811111010', '310101199110100100', 'rescue', 461100, '救援队员', '急救员证书', 4, 3, '0', @now, @tenant_id);

-- =============================================
-- Done
-- =============================================
SET FOREIGN_KEY_CHECKS = 1;

SELECT 'Tenant 461397 demo data import completed!' AS message;


-- =============================================
-- Update device coordinates for map display
-- =============================================
UPDATE health_device SET longitude = 121.5501, latitude = 31.2201 WHERE id = 461401 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5502, latitude = 31.2202 WHERE id = 461402 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5503, latitude = 31.2203 WHERE id = 461403 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5504, latitude = 31.2204 WHERE id = 461404 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5510, latitude = 31.2210 WHERE id = 461405 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5511, latitude = 31.2211 WHERE id = 461406 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5512, latitude = 31.2212 WHERE id = 461407 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5520, latitude = 31.2220 WHERE id = 461408 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5521, latitude = 31.2221 WHERE id = 461409 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.4350, latitude = 31.1850 WHERE id = 461410 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5501, latitude = 31.2201 WHERE id = 461411 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5503, latitude = 31.2203 WHERE id = 461412 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5501, latitude = 31.2201 WHERE id = 461413 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5511, latitude = 31.2211 WHERE id = 461414 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5510, latitude = 31.2210 WHERE id = 461415 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5521, latitude = 31.2221 WHERE id = 461416 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5521, latitude = 31.2221 WHERE id = 461417 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5505, latitude = 31.2205 WHERE id = 461418 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.5510, latitude = 31.2210 WHERE id = 461419 AND tenant_id = '461397';
UPDATE health_device SET longitude = 121.4680, latitude = 31.2280 WHERE id = 461420 AND tenant_id = '461397';
