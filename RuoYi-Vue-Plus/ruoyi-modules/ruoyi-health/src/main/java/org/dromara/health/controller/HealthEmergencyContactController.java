package org.dromara.health.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.health.domain.bo.HealthEmergencyContactBo;
import org.dromara.health.domain.vo.HealthEmergencyContactVo;
import org.dromara.health.service.IHealthEmergencyContactService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 紧急联系人管理
 *
 * @author health
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/health/emergencyContact")
public class HealthEmergencyContactController extends BaseController {

    private final IHealthEmergencyContactService contactService;

    @SaCheckPermission("health:elder:query")
    @GetMapping("/list/{elderId}")
    public R<List<HealthEmergencyContactVo>> list(@PathVariable Long elderId) {
        return R.ok(contactService.selectListByElderId(elderId));
    }

    @SaCheckPermission("health:elder:query")
    @GetMapping("/{id}")
    public R<HealthEmergencyContactVo> getInfo(@PathVariable Long id) {
        return R.ok(contactService.selectById(id));
    }

    @SaCheckPermission("health:elder:edit")
    @Log(title = "紧急联系人", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody HealthEmergencyContactBo bo) {
        return toAjax(contactService.insert(bo));
    }

    @SaCheckPermission("health:elder:edit")
    @Log(title = "紧急联系人", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody HealthEmergencyContactBo bo) {
        return toAjax(contactService.update(bo));
    }

    @SaCheckPermission("health:elder:edit")
    @Log(title = "紧急联系人", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(contactService.deleteByIds(List.of(ids)));
    }

}
