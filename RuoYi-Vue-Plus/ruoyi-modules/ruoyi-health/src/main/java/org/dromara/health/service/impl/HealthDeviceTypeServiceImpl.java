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
import org.dromara.health.domain.HealthDevice;
import org.dromara.health.domain.HealthDeviceType;
import org.dromara.health.domain.bo.HealthDeviceTypeBo;
import org.dromara.health.domain.vo.HealthDeviceTypeVo;
import org.dromara.health.mapper.HealthDeviceMapper;
import org.dromara.health.mapper.HealthDeviceTypeMapper;
import org.dromara.health.service.IHealthDeviceTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备类型Service实现
 *
 * @author health
 */
@RequiredArgsConstructor
@Service
public class HealthDeviceTypeServiceImpl implements IHealthDeviceTypeService {

    private final HealthDeviceTypeMapper baseMapper;
    private final HealthDeviceMapper deviceMapper;

    /**
     * 查询设备类型列表
     */
    @Override
    public List<HealthDeviceTypeVo> selectList(HealthDeviceTypeBo bo) {
        LambdaQueryWrapper<HealthDeviceType> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询设备类型树结构
     */
    @Override
    public List<Tree<Long>> selectTreeList(HealthDeviceTypeBo bo) {
        LambdaQueryWrapper<HealthDeviceType> lqw = buildQueryWrapper(bo);
        List<HealthDeviceTypeVo> list = baseMapper.selectVoList(lqw);
        return buildTree(list);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<HealthDeviceType> buildQueryWrapper(HealthDeviceTypeBo bo) {
        LambdaQueryWrapper<HealthDeviceType> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTypeName()), HealthDeviceType::getTypeName, bo.getTypeName());
        lqw.eq(StringUtils.isNotBlank(bo.getTypeCode()), HealthDeviceType::getTypeCode, bo.getTypeCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), HealthDeviceType::getCategory, bo.getCategory());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HealthDeviceType::getStatus, bo.getStatus());
        lqw.orderByAsc(HealthDeviceType::getParentId);
        lqw.orderByAsc(HealthDeviceType::getSortOrder);
        lqw.orderByAsc(HealthDeviceType::getId);
        return lqw;
    }

    /**
     * 构建树结构
     */
    private List<Tree<Long>> buildTree(List<HealthDeviceTypeVo> list) {
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.build(list, (node, tree) -> {
            tree.setId(node.getId());
            tree.setParentId(node.getParentId());
            tree.setName(node.getTypeName());
            tree.setWeight(node.getSortOrder());
            tree.putExtra("typeCode", node.getTypeCode());
            tree.putExtra("category", node.getCategory());
            tree.putExtra("icon", node.getIcon());
            tree.putExtra("status", node.getStatus());
            tree.putExtra("disabled", SystemConstants.DISABLE.equals(node.getStatus()));
        });
    }

    /**
     * 根据ID查询设备类型
     */
    @Override
    public HealthDeviceTypeVo selectById(Long id) {
        HealthDeviceTypeVo vo = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(vo) && ObjectUtil.isNotNull(vo.getParentId()) && vo.getParentId() > 0) {
            HealthDeviceTypeVo parent = baseMapper.selectVoById(vo.getParentId());
            if (ObjectUtil.isNotNull(parent)) {
                // 可以在VO中添加parentName字段
            }
        }
        return vo;
    }

    /**
     * 校验类型编码是否唯一
     */
    @Override
    public boolean checkTypeCodeUnique(HealthDeviceTypeBo bo) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<HealthDeviceType>()
            .eq(HealthDeviceType::getTypeCode, bo.getTypeCode())
            .ne(ObjectUtil.isNotNull(bo.getId()), HealthDeviceType::getId, bo.getId()));
        return !exist;
    }

    /**
     * 校验类型名称是否唯一
     */
    @Override
    public boolean checkTypeNameUnique(HealthDeviceTypeBo bo) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<HealthDeviceType>()
            .eq(HealthDeviceType::getTypeName, bo.getTypeName())
            .eq(HealthDeviceType::getParentId, bo.getParentId())
            .ne(ObjectUtil.isNotNull(bo.getId()), HealthDeviceType::getId, bo.getId()));
        return !exist;
    }

    /**
     * 是否存在子节点
     */
    @Override
    public boolean hasChildById(Long id) {
        return baseMapper.exists(new LambdaQueryWrapper<HealthDeviceType>()
            .eq(HealthDeviceType::getParentId, id));
    }

    /**
     * 是否存在关联设备
     */
    @Override
    public boolean hasDeviceById(Long id) {
        return deviceMapper.exists(new LambdaQueryWrapper<HealthDevice>()
            .eq(HealthDevice::getDeviceTypeId, id));
    }

    /**
     * 新增设备类型
     */
    @Override
    public int insert(HealthDeviceTypeBo bo) {
        HealthDeviceType entity = MapstructUtils.convert(bo, HealthDeviceType.class);
        if (ObjectUtil.isNull(entity.getParentId())) {
            entity.setParentId(0L);
        }
        return baseMapper.insert(entity);
    }

    /**
     * 修改设备类型
     */
    @Override
    public int update(HealthDeviceTypeBo bo) {
        HealthDeviceType entity = MapstructUtils.convert(bo, HealthDeviceType.class);
        return baseMapper.updateById(entity);
    }

    /**
     * 删除设备类型
     */
    @Override
    public int deleteById(Long id) {
        return baseMapper.deleteById(id);
    }

    /**
     * 批量删除设备类型
     */
    @Override
    public int deleteByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }

}
