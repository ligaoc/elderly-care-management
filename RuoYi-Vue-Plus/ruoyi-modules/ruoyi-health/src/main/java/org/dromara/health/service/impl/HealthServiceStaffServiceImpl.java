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
import org.dromara.health.domain.HealthOrganization;
import org.dromara.health.domain.HealthServiceStaff;
import org.dromara.health.domain.bo.HealthServiceStaffBo;
import org.dromara.health.domain.vo.HealthServiceStaffVo;
import org.dromara.health.mapper.HealthOrganizationMapper;
import org.dromara.health.mapper.HealthServiceStaffMapper;
import org.dromara.health.service.IHealthServiceStaffService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务人员Service实现
 *
 * @author health
 */
@RequiredArgsConstructor
@Service
public class HealthServiceStaffServiceImpl implements IHealthServiceStaffService {

    private final HealthServiceStaffMapper baseMapper;
    private final HealthOrganizationMapper orgMapper;

    @Override
    public TableDataInfo<HealthServiceStaffVo> selectPageList(HealthServiceStaffBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HealthServiceStaff> lqw = buildQueryWrapper(bo);
        Page<HealthServiceStaffVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        fillRelationInfo(page.getRecords());
        return TableDataInfo.build(page);
    }

    @Override
    public List<HealthServiceStaffVo> selectList(HealthServiceStaffBo bo) {
        LambdaQueryWrapper<HealthServiceStaff> lqw = buildQueryWrapper(bo);
        List<HealthServiceStaffVo> list = baseMapper.selectVoList(lqw);
        fillRelationInfo(list);
        return list;
    }

    private LambdaQueryWrapper<HealthServiceStaff> buildQueryWrapper(HealthServiceStaffBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HealthServiceStaff> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getStaffCode()), HealthServiceStaff::getStaffCode, bo.getStaffCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HealthServiceStaff::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getGender()), HealthServiceStaff::getGender, bo.getGender());
        lqw.like(StringUtils.isNotBlank(bo.getPhone()), HealthServiceStaff::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getStaffType()), HealthServiceStaff::getStaffType, bo.getStaffType());
        lqw.eq(ObjectUtil.isNotNull(bo.getOrgId()), HealthServiceStaff::getOrgId, bo.getOrgId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HealthServiceStaff::getStatus, bo.getStatus());
        lqw.orderByAsc(HealthServiceStaff::getSortOrder);
        lqw.orderByDesc(HealthServiceStaff::getCreateTime);
        return lqw;
    }

    private void fillRelationInfo(List<HealthServiceStaffVo> list) {
        if (list == null || list.isEmpty()) return;
        for (HealthServiceStaffVo vo : list) {
            if (ObjectUtil.isNotNull(vo.getOrgId())) {
                HealthOrganization org = orgMapper.selectById(vo.getOrgId());
                if (ObjectUtil.isNotNull(org)) {
                    vo.setOrgName(org.getOrgName());
                }
            }
        }
    }

    @Override
    public HealthServiceStaffVo selectById(Long id) {
        HealthServiceStaffVo vo = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(vo)) {
            fillRelationInfo(List.of(vo));
        }
        return vo;
    }

    @Override
    public boolean checkStaffCodeUnique(HealthServiceStaffBo bo) {
        return !baseMapper.exists(new LambdaQueryWrapper<HealthServiceStaff>()
            .eq(HealthServiceStaff::getStaffCode, bo.getStaffCode())
            .ne(ObjectUtil.isNotNull(bo.getId()), HealthServiceStaff::getId, bo.getId()));
    }

    @Override
    public int insert(HealthServiceStaffBo bo) {
        HealthServiceStaff entity = MapstructUtils.convert(bo, HealthServiceStaff.class);
        if (StringUtils.isBlank(entity.getStaffCode())) {
            entity.setStaffCode(generateStaffCode());
        }
        return baseMapper.insert(entity);
    }

    @Override
    public int update(HealthServiceStaffBo bo) {
        HealthServiceStaff entity = MapstructUtils.convert(bo, HealthServiceStaff.class);
        return baseMapper.updateById(entity);
    }

    @Override
    public int deleteById(Long id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }

    private String generateStaffCode() {
        String prefix = "STF" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<HealthServiceStaff>()
            .likeRight(HealthServiceStaff::getStaffCode, prefix));
        return prefix + String.format("%04d", count + 1);
    }

}
