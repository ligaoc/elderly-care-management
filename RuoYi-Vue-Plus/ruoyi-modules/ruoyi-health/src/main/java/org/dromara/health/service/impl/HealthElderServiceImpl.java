package org.dromara.health.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.health.domain.HealthElder;
import org.dromara.health.domain.HealthOrganization;
import org.dromara.health.domain.bo.HealthElderBo;
import org.dromara.health.domain.vo.HealthElderVo;
import org.dromara.health.mapper.HealthElderMapper;
import org.dromara.health.mapper.HealthOrganizationMapper;
import org.dromara.health.service.IHealthElderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 老人管理Service实现
 *
 * @author health
 */
@RequiredArgsConstructor
@Service
public class HealthElderServiceImpl implements IHealthElderService {

    private final HealthElderMapper baseMapper;
    private final HealthOrganizationMapper orgMapper;

    @Override
    public TableDataInfo<HealthElderVo> selectPageList(HealthElderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HealthElder> lqw = buildQueryWrapper(bo);
        Page<HealthElderVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        fillRelationInfo(page.getRecords());
        return TableDataInfo.build(page);
    }

    @Override
    public List<HealthElderVo> selectList(HealthElderBo bo) {
        LambdaQueryWrapper<HealthElder> lqw = buildQueryWrapper(bo);
        List<HealthElderVo> list = baseMapper.selectVoList(lqw);
        fillRelationInfo(list);
        return list;
    }

    private LambdaQueryWrapper<HealthElder> buildQueryWrapper(HealthElderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HealthElder> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getUserCode()), HealthElder::getUserCode, bo.getUserCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HealthElder::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getGender()), HealthElder::getGender, bo.getGender());
        lqw.like(StringUtils.isNotBlank(bo.getPhone()), HealthElder::getPhone, bo.getPhone());
        lqw.eq(ObjectUtil.isNotNull(bo.getOrgId()), HealthElder::getOrgId, bo.getOrgId());
        lqw.eq(StringUtils.isNotBlank(bo.getCareLevel()), HealthElder::getCareLevel, bo.getCareLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getRiskLevel()), HealthElder::getRiskLevel, bo.getRiskLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HealthElder::getStatus, bo.getStatus());
        lqw.orderByDesc(HealthElder::getCreateTime);
        return lqw;
    }

    private void fillRelationInfo(List<HealthElderVo> list) {
        if (list == null || list.isEmpty()) return;
        for (HealthElderVo vo : list) {
            if (ObjectUtil.isNotNull(vo.getOrgId())) {
                HealthOrganization org = orgMapper.selectById(vo.getOrgId());
                if (ObjectUtil.isNotNull(org)) {
                    vo.setOrgName(org.getOrgName());
                }
            }
        }
    }

    @Override
    public HealthElderVo selectById(Long id) {
        HealthElderVo vo = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(vo)) {
            fillRelationInfo(List.of(vo));
        }
        return vo;
    }

    @Override
    public boolean checkUserCodeUnique(HealthElderBo bo) {
        return !baseMapper.exists(new LambdaQueryWrapper<HealthElder>()
            .eq(HealthElder::getUserCode, bo.getUserCode())
            .ne(ObjectUtil.isNotNull(bo.getId()), HealthElder::getId, bo.getId()));
    }

    @Override
    public int insert(HealthElderBo bo) {
        HealthElder entity = MapstructUtils.convert(bo, HealthElder.class);
        if (StringUtils.isBlank(entity.getUserCode())) {
            entity.setUserCode(generateUserCode());
        }
        // 计算BMI
        calculateBmi(entity);
        return baseMapper.insert(entity);
    }

    @Override
    public int update(HealthElderBo bo) {
        HealthElder entity = MapstructUtils.convert(bo, HealthElder.class);
        // 计算BMI
        calculateBmi(entity);
        return baseMapper.updateById(entity);
    }

    /**
     * 计算BMI指数
     * BMI = 体重(kg) / 身高(m)^2
     */
    private void calculateBmi(HealthElder entity) {
        if (ObjectUtil.isNotNull(entity.getHeight()) && ObjectUtil.isNotNull(entity.getWeight())
            && entity.getHeight().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal heightM = entity.getHeight().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            BigDecimal bmi = entity.getWeight().divide(heightM.multiply(heightM), 2, RoundingMode.HALF_UP);
            entity.setBmi(bmi);
        }
    }

    @Override
    public int deleteById(Long id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }

    @Override
    public String generateUserCode() {
        String prefix = "ELD" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<HealthElder>()
            .likeRight(HealthElder::getUserCode, prefix));
        return prefix + String.format("%04d", count + 1);
    }

}
