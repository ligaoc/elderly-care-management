package org.dromara.health.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.constant.SystemConstants;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.TreeBuildUtils;
import org.dromara.health.domain.HealthElder;
import org.dromara.health.domain.HealthOrganization;
import org.dromara.health.domain.HealthServiceStaff;
import org.dromara.health.domain.bo.HealthOrganizationBo;
import org.dromara.health.domain.vo.HealthOrganizationVo;
import org.dromara.health.mapper.HealthElderMapper;
import org.dromara.health.mapper.HealthOrganizationMapper;
import org.dromara.health.mapper.HealthServiceStaffMapper;
import org.dromara.health.service.IHealthOrganizationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组织机构Service实现
 *
 * @author health
 */
@RequiredArgsConstructor
@Service
public class HealthOrganizationServiceImpl implements IHealthOrganizationService {

    private final HealthOrganizationMapper baseMapper;
    private final HealthElderMapper elderMapper;
    private final HealthServiceStaffMapper staffMapper;

    @Override
    public List<HealthOrganizationVo> selectList(HealthOrganizationBo bo) {
        LambdaQueryWrapper<HealthOrganization> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public List<Tree<Long>> selectTreeList(HealthOrganizationBo bo) {
        List<HealthOrganizationVo> list = selectList(bo);
        return buildTree(list);
    }

    private LambdaQueryWrapper<HealthOrganization> buildQueryWrapper(HealthOrganizationBo bo) {
        LambdaQueryWrapper<HealthOrganization> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getOrgName()), HealthOrganization::getOrgName, bo.getOrgName());
        lqw.eq(StringUtils.isNotBlank(bo.getOrgCode()), HealthOrganization::getOrgCode, bo.getOrgCode());
        lqw.eq(StringUtils.isNotBlank(bo.getOrgType()), HealthOrganization::getOrgType, bo.getOrgType());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HealthOrganization::getStatus, bo.getStatus());
        lqw.orderByAsc(HealthOrganization::getParentId);
        lqw.orderByAsc(HealthOrganization::getSortOrder);
        return lqw;
    }

    private List<Tree<Long>> buildTree(List<HealthOrganizationVo> list) {
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.build(list, (node, tree) -> {
            tree.setId(node.getId());
            tree.setParentId(node.getParentId());
            tree.setName(node.getOrgName());
            tree.setWeight(node.getSortOrder());
            tree.putExtra("orgCode", node.getOrgCode());
            tree.putExtra("orgType", node.getOrgType());
            tree.putExtra("leader", node.getLeader());
            tree.putExtra("phone", node.getPhone());
            tree.putExtra("status", node.getStatus());
            tree.putExtra("disabled", SystemConstants.DISABLE.equals(node.getStatus()));
        });
    }

    @Override
    public HealthOrganizationVo selectById(Long id) {
        HealthOrganizationVo vo = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(vo) && ObjectUtil.isNotNull(vo.getParentId()) && vo.getParentId() > 0) {
            HealthOrganizationVo parent = baseMapper.selectVoById(vo.getParentId());
            if (ObjectUtil.isNotNull(parent)) {
                vo.setParentName(parent.getOrgName());
            }
        }
        return vo;
    }

    @Override
    public boolean checkOrgCodeUnique(HealthOrganizationBo bo) {
        return !baseMapper.exists(new LambdaQueryWrapper<HealthOrganization>()
            .eq(HealthOrganization::getOrgCode, bo.getOrgCode())
            .ne(ObjectUtil.isNotNull(bo.getId()), HealthOrganization::getId, bo.getId()));
    }

    @Override
    public boolean checkOrgNameUnique(HealthOrganizationBo bo) {
        return !baseMapper.exists(new LambdaQueryWrapper<HealthOrganization>()
            .eq(HealthOrganization::getOrgName, bo.getOrgName())
            .eq(HealthOrganization::getParentId, bo.getParentId())
            .ne(ObjectUtil.isNotNull(bo.getId()), HealthOrganization::getId, bo.getId()));
    }

    @Override
    public boolean hasChildById(Long id) {
        return baseMapper.exists(new LambdaQueryWrapper<HealthOrganization>()
            .eq(HealthOrganization::getParentId, id));
    }

    @Override
    public boolean hasElderById(Long id) {
        return elderMapper.exists(new LambdaQueryWrapper<HealthElder>()
            .eq(HealthElder::getOrgId, id));
    }

    @Override
    public boolean hasStaffById(Long id) {
        return staffMapper.exists(new LambdaQueryWrapper<HealthServiceStaff>()
            .eq(HealthServiceStaff::getOrgId, id));
    }

    @Override
    public int insert(HealthOrganizationBo bo) {
        HealthOrganization entity = MapstructUtils.convert(bo, HealthOrganization.class);
        if (ObjectUtil.isNull(entity.getParentId())) {
            entity.setParentId(0L);
        }
        return baseMapper.insert(entity);
    }

    @Override
    public int update(HealthOrganizationBo bo) {
        HealthOrganization entity = MapstructUtils.convert(bo, HealthOrganization.class);
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

}
