package org.dromara.health.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.health.domain.bo.HealthServiceStaffBo;
import org.dromara.health.domain.vo.HealthServiceStaffVo;
import org.dromara.health.service.IHealthServiceStaffService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务人员管理
 *
 * @author health
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/health/serviceStaff")
public class HealthServiceStaffController extends BaseController {

    private final IHealthServiceStaffService staffService;

    @SaCheckPermission("health:serviceStaff:list")
    @GetMapping("/list")
    public TableDataInfo<HealthServiceStaffVo> list(HealthServiceStaffBo bo, PageQuery pageQuery) {
        return staffService.selectPageList(bo, pageQuery);
    }

    @SaCheckPermission("health:serviceStaff:query")
    @GetMapping("/{id}")
    public R<HealthServiceStaffVo> getInfo(@PathVariable Long id) {
        return R.ok(staffService.selectById(id));
    }

    @SaCheckPermission("health:serviceStaff:add")
    @Log(title = "服务人员", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody HealthServiceStaffBo bo) {
        if (bo.getStaffCode() != null && !staffService.checkStaffCodeUnique(bo)) {
            return R.fail("新增服务人员'" + bo.getName() + "'失败，人员编码已存在");
        }
        return toAjax(staffService.insert(bo));
    }

    @SaCheckPermission("health:serviceStaff:edit")
    @Log(title = "服务人员", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody HealthServiceStaffBo bo) {
        if (!staffService.checkStaffCodeUnique(bo)) {
            return R.fail("修改服务人员'" + bo.getName() + "'失败，人员编码已存在");
        }
        return toAjax(staffService.update(bo));
    }

    @SaCheckPermission("health:serviceStaff:remove")
    @Log(title = "服务人员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(staffService.deleteByIds(List.of(ids)));
    }

}
