package org.dromara.health.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.health.domain.vo.HealthDeviceAlertVo;
import org.dromara.health.service.IHealthDashboardService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 数据大屏
 *
 * @author health
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/health/dashboard")
public class HealthDashboardController {

    private final IHealthDashboardService dashboardService;

    /**
     * 获取统计数据
     */
    @SaCheckPermission("health:dashboard:list")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        return R.ok(dashboardService.getStatistics());
    }

    /**
     * 获取设备在线率
     */
    @SaCheckPermission("health:dashboard:list")
    @GetMapping("/deviceOnlineRate")
    public R<Map<String, Object>> deviceOnlineRate() {
        return R.ok(dashboardService.getDeviceOnlineRate());
    }

    /**
     * 获取告警趋势(7天)
     */
    @SaCheckPermission("health:dashboard:list")
    @GetMapping("/alertTrend")
    public R<List<Map<String, Object>>> alertTrend() {
        return R.ok(dashboardService.getAlertTrend());
    }

    /**
     * 获取告警类型分布
     */
    @SaCheckPermission("health:dashboard:list")
    @GetMapping("/alertTypeDistribution")
    public R<List<Map<String, Object>>> alertTypeDistribution() {
        return R.ok(dashboardService.getAlertTypeDistribution());
    }

    /**
     * 获取最新告警列表
     */
    @SaCheckPermission("health:dashboard:list")
    @GetMapping("/latestAlerts")
    public R<List<HealthDeviceAlertVo>> latestAlerts() {
        return R.ok(dashboardService.getLatestAlerts());
    }

    /**
     * 获取设备分布数据
     */
    @SaCheckPermission("health:dashboard:list")
    @GetMapping("/deviceDistribution")
    public R<List<Map<String, Object>>> deviceDistribution() {
        return R.ok(dashboardService.getDeviceDistribution());
    }

}
