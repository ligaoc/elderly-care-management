package org.dromara.health.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.health.domain.bo.HealthDeviceBo;
import org.dromara.health.domain.vo.HealthDeviceVo;
import org.dromara.health.service.IHealthDeviceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备管理
 *
 * @author health
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/health/device")
public class HealthDeviceController extends BaseController {

    private final IHealthDeviceService deviceService;

    /**
     * 查询设备列表
     */
    @SaCheckPermission("health:device:list")
    @GetMapping("/list")
    public TableDataInfo<HealthDeviceVo> list(HealthDeviceBo bo, PageQuery pageQuery) {
        return deviceService.selectPageList(bo, pageQuery);
    }

    /**
     * 导出设备列表
     */
    @SaCheckPermission("health:device:export")
    @Log(title = "设备管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HealthDeviceBo bo, HttpServletResponse response) {
        List<HealthDeviceVo> list = deviceService.selectList(bo);
        ExcelUtil.exportExcel(list, "设备数据", HealthDeviceVo.class, response);
    }

    /**
     * 获取设备详情
     *
     * @param id 主键ID
     */
    @SaCheckPermission("health:device:query")
    @GetMapping("/{id}")
    public R<HealthDeviceVo> getInfo(@PathVariable Long id) {
        return R.ok(deviceService.selectById(id));
    }

    /**
     * 新增设备
     */
    @SaCheckPermission("health:device:add")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody HealthDeviceBo bo) {
        if (bo.getDeviceCode() != null && !deviceService.checkDeviceCodeUnique(bo)) {
            return R.fail("新增设备'" + bo.getDeviceName() + "'失败，设备编码已存在");
        }
        return toAjax(deviceService.insert(bo));
    }

    /**
     * 修改设备
     */
    @SaCheckPermission("health:device:edit")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody HealthDeviceBo bo) {
        if (!deviceService.checkDeviceCodeUnique(bo)) {
            return R.fail("修改设备'" + bo.getDeviceName() + "'失败，设备编码已存在");
        }
        return toAjax(deviceService.update(bo));
    }

    /**
     * 删除设备
     *
     * @param ids 主键ID串
     */
    @SaCheckPermission("health:device:remove")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(deviceService.deleteByIds(List.of(ids)));
    }

    /**
     * 绑定设备
     *
     * @param id     设备ID
     * @param userId 用户ID
     */
    @SaCheckPermission("health:device:bind")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping("/bind/{id}/{userId}")
    public R<Void> bind(@PathVariable Long id, @PathVariable Long userId) {
        return toAjax(deviceService.bindDevice(id, userId));
    }

    /**
     * 解绑设备
     *
     * @param id 设备ID
     */
    @SaCheckPermission("health:device:bind")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping("/unbind/{id}")
    public R<Void> unbind(@PathVariable Long id) {
        return toAjax(deviceService.unbindDevice(id));
    }

}
