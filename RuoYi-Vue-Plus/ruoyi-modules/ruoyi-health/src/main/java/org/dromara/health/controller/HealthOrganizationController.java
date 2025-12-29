package org.dromara.health.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.health.domain.bo.HealthOrganizationBo;
import org.dromara.health.domain.vo.HealthOrganizationVo;
import org.dromara.health.service.IHealthOrganizationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织管理
 *
 * @author health
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/health/organization")
public class HealthOrganizationController extends BaseController {

    private final IHealthOrganizationService organizationService;

    @SaCheckPermission("health:organization:list")
    @GetMapping("/list")
    public R<List<HealthOrganizationVo>> list(HealthOrganizationBo bo) {
        return R.ok(organizationService.selectList(bo));
    }

    @SaCheckPermission("health:organization:list")
    @GetMapping("/tree")
    public R<List<Tree<Long>>> tree(HealthOrganizationBo bo) {
        return R.ok(organizationService.selectTreeList(bo));
    }

    @SaCheckPermission("health:organization:query")
    @GetMapping("/{id}")
    public R<HealthOrganizationVo> getInfo(@PathVariable Long id) {
        return R.ok(organizationService.selectById(id));
    }

    @SaCheckPermission("health:organization:add")
    @Log(title = "组织管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody HealthOrganizationBo bo) {
        if (!organizationService.checkOrgCodeUnique(bo)) {
            return R.fail("新增组织'" + bo.getOrgName() + "'失败，组织编码已存在");
        }
        if (!organizationService.checkOrgNameUnique(bo)) {
            return R.fail("新增组织'" + bo.getOrgName() + "'失败，组织名称已存在");
        }
        return toAjax(organizationService.insert(bo));
    }

    @SaCheckPermission("health:organization:edit")
    @Log(title = "组织管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody HealthOrganizationBo bo) {
        if (!organizationService.checkOrgCodeUnique(bo)) {
            return R.fail("修改组织'" + bo.getOrgName() + "'失败，组织编码已存在");
        }
        if (bo.getParentId() != null && bo.getParentId().equals(bo.getId())) {
            return R.fail("修改组织'" + bo.getOrgName() + "'失败，上级组织不能是自己");
        }
        return toAjax(organizationService.update(bo));
    }

    @SaCheckPermission("health:organization:remove")
    @Log(title = "组织管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        for (Long id : ids) {
            if (organizationService.hasChildById(id)) {
                return R.warn("存在下级组织，不允许删除");
            }
            if (organizationService.hasElderById(id)) {
                return R.warn("组织下存在老人，不允许删除");
            }
            if (organizationService.hasStaffById(id)) {
                return R.warn("组织下存在服务人员，不允许删除");
            }
        }
        return toAjax(organizationService.deleteByIds(List.of(ids)));
    }

}
