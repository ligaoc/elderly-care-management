package org.dromara.health.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.health.domain.bo.HealthDeviceTypeBo;
import org.dromara.health.domain.vo.HealthDeviceTypeVo;
import org.dromara.health.service.IHealthDeviceTypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备类型管理
 *
 * @author health
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/health/deviceType")
public class HealthDeviceTypeController extends BaseController {

    private final IHealthDeviceTypeService deviceTypeService;

    /**
     * 查询设备类型列表
     */
    @SaCheckPermission("health:deviceType:list")
    @GetMapping("/list")
    public R<List<HealthDeviceTypeVo>> list(HealthDeviceTypeBo bo) {
        List<HealthDeviceTypeVo> list = deviceTypeService.selectList(bo);
        return R.ok(list);
    }

    /**
     * 查询设备类型树结构
     */
    @SaCheckPermission("health:deviceType:list")
    @GetMapping("/tree")
    public R<List<Tree<Long>>> tree(HealthDeviceTypeBo bo) {
        List<Tree<Long>> tree = deviceTypeService.selectTreeList(bo);
        return R.ok(tree);
    }

    /**
     * 获取设备类型详情
     *
     * @param id 主键ID
     */
    @SaCheckPermission("health:deviceType:query")
    @GetMapping("/{id}")
    public R<HealthDeviceTypeVo> getInfo(@PathVariable Long id) {
        return R.ok(deviceTypeService.selectById(id));
    }

    /**
     * 新增设备类型
     */
    @SaCheckPermission("health:deviceType:add")
    @Log(title = "设备类型", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody HealthDeviceTypeBo bo) {
        if (!deviceTypeService.checkTypeCodeUnique(bo)) {
            return R.fail("新增设备类型'" + bo.getTypeName() + "'失败，类型编码已存在");
        }
        if (!deviceTypeService.checkTypeNameUnique(bo)) {
            return R.fail("新增设备类型'" + bo.getTypeName() + "'失败，类型名称已存在");
        }
        return toAjax(deviceTypeService.insert(bo));
    }

    /**
     * 修改设备类型
     */
    @SaCheckPermission("health:deviceType:edit")
    @Log(title = "设备类型", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody HealthDeviceTypeBo bo) {
        if (!deviceTypeService.checkTypeCodeUnique(bo)) {
            return R.fail("修改设备类型'" + bo.getTypeName() + "'失败，类型编码已存在");
        }
        if (!deviceTypeService.checkTypeNameUnique(bo)) {
            return R.fail("修改设备类型'" + bo.getTypeName() + "'失败，类型名称已存在");
        }
        if (bo.getParentId() != null && bo.getParentId().equals(bo.getId())) {
            return R.fail("修改设备类型'" + bo.getTypeName() + "'失败，上级类型不能是自己");
        }
        return toAjax(deviceTypeService.update(bo));
    }

    /**
     * 删除设备类型
     *
     * @param ids 主键ID串
     */
    @SaCheckPermission("health:deviceType:remove")
    @Log(title = "设备类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        for (Long id : ids) {
            if (deviceTypeService.hasChildById(id)) {
                return R.warn("存在下级类型，不允许删除");
            }
            if (deviceTypeService.hasDeviceById(id)) {
                return R.warn("类型下存在设备，不允许删除");
            }
        }
        return toAjax(deviceTypeService.deleteByIds(List.of(ids)));
    }

}
