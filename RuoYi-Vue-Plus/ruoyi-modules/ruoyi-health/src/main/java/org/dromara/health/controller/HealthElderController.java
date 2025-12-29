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
import org.dromara.health.domain.bo.HealthElderBo;
import org.dromara.health.domain.vo.HealthElderVo;
import org.dromara.health.service.IHealthElderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 老人管理
 *
 * @author health
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/health/elder")
public class HealthElderController extends BaseController {

    private final IHealthElderService elderService;

    @SaCheckPermission("health:elder:list")
    @GetMapping("/list")
    public TableDataInfo<HealthElderVo> list(HealthElderBo bo, PageQuery pageQuery) {
        return elderService.selectPageList(bo, pageQuery);
    }

    @SaCheckPermission("health:elder:export")
    @Log(title = "老人管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HealthElderBo bo, HttpServletResponse response) {
        List<HealthElderVo> list = elderService.selectList(bo);
        ExcelUtil.exportExcel(list, "老人数据", HealthElderVo.class, response);
    }

    @SaCheckPermission("health:elder:query")
    @GetMapping("/{id}")
    public R<HealthElderVo> getInfo(@PathVariable Long id) {
        return R.ok(elderService.selectById(id));
    }

    @SaCheckPermission("health:elder:add")
    @Log(title = "老人管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody HealthElderBo bo) {
        if (bo.getUserCode() != null && !elderService.checkUserCodeUnique(bo)) {
            return R.fail("新增老人'" + bo.getName() + "'失败，用户编码已存在");
        }
        return toAjax(elderService.insert(bo));
    }

    @SaCheckPermission("health:elder:edit")
    @Log(title = "老人管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody HealthElderBo bo) {
        if (!elderService.checkUserCodeUnique(bo)) {
            return R.fail("修改老人'" + bo.getName() + "'失败，用户编码已存在");
        }
        return toAjax(elderService.update(bo));
    }

    @SaCheckPermission("health:elder:remove")
    @Log(title = "老人管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(elderService.deleteByIds(List.of(ids)));
    }

}
