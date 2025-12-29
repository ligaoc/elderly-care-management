package org.dromara.health.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.health.domain.bo.HealthDeviceAlertBo;
import org.dromara.health.domain.vo.HealthDeviceAlertVo;
import org.dromara.health.service.IHealthDeviceAlertService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 告警管理
 *
 * @author health
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/health/alert")
public class HealthDeviceAlertController extends BaseController {

    private final IHealthDeviceAlertService alertService;

    /**
     * 查询告警列表
     */
    @SaCheckPermission("health:alert:list")
    @GetMapping("/list")
    public TableDataInfo<HealthDeviceAlertVo> list(HealthDeviceAlertBo bo, PageQuery pageQuery) {
        return alertService.selectPageList(bo, pageQuery);
    }

    /**
     * 导出告警列表
     */
    @SaCheckPermission("health:alert:export")
    @Log(title = "告警管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HealthDeviceAlertBo bo, HttpServletResponse response) {
        List<HealthDeviceAlertVo> list = alertService.selectList(bo);
        ExcelUtil.exportExcel(list, "告警数据", HealthDeviceAlertVo.class, response);
    }

    /**
     * 获取告警详情
     */
    @SaCheckPermission("health:alert:query")
    @GetMapping("/{id}")
    public R<HealthDeviceAlertVo> getInfo(@PathVariable Long id) {
        return R.ok(alertService.selectById(id));
    }

    /**
     * 处理告警
     */
    @SaCheckPermission("health:alert:handle")
    @Log(title = "告警管理", businessType = BusinessType.UPDATE)
    @PutMapping("/handle")
    public R<Void> handle(@RequestParam Long id, @RequestParam String handleType, @RequestParam String handleResult) {
        return toAjax(alertService.handleAlert(id, handleType, handleResult));
    }

    /**
     * 忽略告警
     */
    @SaCheckPermission("health:alert:ignore")
    @Log(title = "告警管理", businessType = BusinessType.UPDATE)
    @PutMapping("/ignore/{id}")
    public R<Void> ignore(@PathVariable Long id) {
        return toAjax(alertService.ignoreAlert(id));
    }

}
