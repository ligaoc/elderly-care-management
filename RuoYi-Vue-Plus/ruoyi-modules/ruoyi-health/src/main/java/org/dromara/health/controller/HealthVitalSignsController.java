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
import org.dromara.health.domain.bo.HealthVitalSignsBo;
import org.dromara.health.domain.vo.HealthVitalSignsVo;
import org.dromara.health.service.IHealthVitalSignsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 体征数据管理
 *
 * @author health
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/health/vitalSigns")
public class HealthVitalSignsController extends BaseController {

    private final IHealthVitalSignsService vitalSignsService;

    @SaCheckPermission("health:vitalSigns:list")
    @GetMapping("/list")
    public TableDataInfo<HealthVitalSignsVo> list(HealthVitalSignsBo bo, PageQuery pageQuery) {
        return vitalSignsService.selectPageList(bo, pageQuery);
    }

    @SaCheckPermission("health:vitalSigns:export")
    @Log(title = "体征数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HealthVitalSignsBo bo, HttpServletResponse response) {
        List<HealthVitalSignsVo> list = vitalSignsService.selectList(bo);
        ExcelUtil.exportExcel(list, "体征数据", HealthVitalSignsVo.class, response);
    }

    @SaCheckPermission("health:vitalSigns:query")
    @GetMapping("/{id}")
    public R<HealthVitalSignsVo> getInfo(@PathVariable Long id) {
        return R.ok(vitalSignsService.selectById(id));
    }

    /**
     * 查询体征趋势数据
     */
    @SaCheckPermission("health:vitalSigns:query")
    @GetMapping("/trend/{elderId}")
    public R<List<HealthVitalSignsVo>> trend(@PathVariable Long elderId, 
                                              @RequestParam(defaultValue = "7") Integer days) {
        return R.ok(vitalSignsService.selectTrendByElderId(elderId, days));
    }

    @SaCheckPermission("health:vitalSigns:add")
    @Log(title = "体征数据", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody HealthVitalSignsBo bo) {
        return toAjax(vitalSignsService.insert(bo));
    }

}
