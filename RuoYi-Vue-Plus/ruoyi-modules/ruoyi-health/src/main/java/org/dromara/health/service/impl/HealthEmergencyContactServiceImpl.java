package org.dromara.health.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.health.domain.HealthElder;
import org.dromara.health.domain.HealthEmergencyContact;
import org.dromara.health.domain.bo.HealthEmergencyContactBo;
import org.dromara.health.domain.vo.HealthEmergencyContactVo;
import org.dromara.health.mapper.HealthElderMapper;
import org.dromara.health.mapper.HealthEmergencyContactMapper;
import org.dromara.health.service.IHealthEmergencyContactService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 紧急联系人Service实现
 *
 * @author health
 */
@RequiredArgsConstructor
@Service
public class HealthEmergencyContactServiceImpl implements IHealthEmergencyContactService {

    private final HealthEmergencyContactMapper baseMapper;
    private final HealthElderMapper elderMapper;

    @Override
    public List<HealthEmergencyContactVo> selectListByElderId(Long elderId) {
        LambdaQueryWrapper<HealthEmergencyContact> lqw = Wrappers.lambdaQuery();
        lqw.eq(HealthEmergencyContact::getElderId, elderId);
        lqw.orderByDesc(HealthEmergencyContact::getIsPrimary);
        lqw.orderByAsc(HealthEmergencyContact::getSortOrder);
        List<HealthEmergencyContactVo> list = baseMapper.selectVoList(lqw);
        fillElderName(list);
        return list;
    }

    private void fillElderName(List<HealthEmergencyContactVo> list) {
        if (list == null || list.isEmpty()) return;
        for (HealthEmergencyContactVo vo : list) {
            if (ObjectUtil.isNotNull(vo.getElderId())) {
                HealthElder elder = elderMapper.selectById(vo.getElderId());
                if (ObjectUtil.isNotNull(elder)) {
                    vo.setElderName(elder.getName());
                }
            }
        }
    }

    @Override
    public HealthEmergencyContactVo selectById(Long id) {
        HealthEmergencyContactVo vo = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(vo)) {
            fillElderName(List.of(vo));
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(HealthEmergencyContactBo bo) {
        HealthEmergencyContact entity = MapstructUtils.convert(bo, HealthEmergencyContact.class);
        // 如果设置为主要联系人，则将该老人的其他联系人设为非主要
        if ("1".equals(entity.getIsPrimary())) {
            clearPrimaryContact(entity.getElderId());
        }
        return baseMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(HealthEmergencyContactBo bo) {
        HealthEmergencyContact entity = MapstructUtils.convert(bo, HealthEmergencyContact.class);
        // 如果设置为主要联系人，则将该老人的其他联系人设为非主要
        if ("1".equals(entity.getIsPrimary())) {
            clearPrimaryContact(entity.getElderId());
        }
        return baseMapper.updateById(entity);
    }

    /**
     * 清除该老人的主要联系人标记
     */
    private void clearPrimaryContact(Long elderId) {
        baseMapper.update(null, Wrappers.<HealthEmergencyContact>lambdaUpdate()
            .set(HealthEmergencyContact::getIsPrimary, "0")
            .eq(HealthEmergencyContact::getElderId, elderId)
            .eq(HealthEmergencyContact::getIsPrimary, "1"));
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
