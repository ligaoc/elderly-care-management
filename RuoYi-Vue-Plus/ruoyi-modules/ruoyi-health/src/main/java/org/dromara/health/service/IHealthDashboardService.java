package org.dromara.health.service;

import org.dromara.health.domain.vo.HealthDeviceAlertVo;

import java.util.List;
import java.util.Map;

/**
 * 大屏数据Service接口
 *
 * @author health
 */
public interface IHealthDashboardService {

    /**
     * 获取统计数据
     */
    Map<String, Object> getStatistics();

    /**
     * 获取设备在线率
     */
    Map<String, Object> getDeviceOnlineRate();

    /**
     * 获取告警趋势(7天)
     */
    List<Map<String, Object>> getAlertTrend();

    /**
     * 获取告警类型分布
     */
    List<Map<String, Object>> getAlertTypeDistribution();

    /**
     * 获取最新告警列表
     */
    List<HealthDeviceAlertVo> getLatestAlerts();

    /**
     * 获取设备分布数据
     */
    List<Map<String, Object>> getDeviceDistribution();

}
