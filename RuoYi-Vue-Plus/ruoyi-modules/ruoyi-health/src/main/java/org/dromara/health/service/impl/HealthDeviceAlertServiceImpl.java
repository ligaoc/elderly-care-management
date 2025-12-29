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
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.health.domain.HealthDevice;
import org.dromara.health.domain.HealthDeviceAlert;
import org.dromara.health.domain.HealthElder;
import org.dromara.health.domain.bo.HealthDeviceAlertBo;
import org.dromara.health.domain.vo.HealthDeviceAlertVo;
import org.dromara.health.mapper.HealthDeviceAlertMapper;
import org.dromara.health.mapper.HealthDeviceMapper;
import org.dromara.health.mapper.HealthElderMapper;
import org.dromara.health.service.IHealthDeviceAlertService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备告警Service实现
 *
 * @author health
 */
@RequiredArgsConstructor
@Service
public class HealthDeviceAlertServiceImpl implements IHealthDeviceAlertService {

    private final HealthDeviceAlertMapper baseMapper;
    private final HealthDeviceMapper deviceMapper;
    private final HealthElderMapper elderMapper;

    @Override
    public TableDataInfo<HealthDeviceAlertVo> selectPageList(HealthDeviceAlertBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HealthDeviceAlert> lqw = buildQueryWrapper(bo);
        Page<HealthDeviceAlertVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        fillRelationInfo(page.getRecords());
        return TableDataInfo.build(page);
    }

    @Override
    public List<HealthDeviceAlertVo> selectList(HealthDeviceAlertBo bo) {
        LambdaQueryWrapper<HealthDeviceAlert> lqw = buildQueryWrapper(bo);
        List<HealthDeviceAlertVo> list = baseMapper.selectVoList(lqw);
        fillRelationInfo(list);
        return list;
    }

    private LambdaQueryWrapper<HealthDeviceAlert> buildQueryWrapper(HealthDeviceAlertBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HealthDeviceAlert> lqw = Wrappers.lambdaQuery();
        lqw.eq(ObjectUtil.isNotNull(bo.getDeviceId()), HealthDeviceAlert::getDeviceId, bo.getDeviceId());
        lqw.eq(ObjectUtil.isNotNull(bo.getUserId()), HealthDeviceAlert::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getAlertType()), HealthDeviceAlert::getAlertType, bo.getAlertType());
        lqw.eq(StringUtils.isNotBlank(bo.getAlertLevel()), HealthDeviceAlert::getAlertLevel, bo.getAlertLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getHandleStatus()), HealthDeviceAlert::getHandleStatus, bo.getHandleStatus());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            HealthDeviceAlert::getAlertTime, params.get("beginTime"), params.get("endTime"));
        lqw.orderByDesc(HealthDeviceAlert::getAlertTime);
        return lqw;
    }

    private void fillRelationInfo(List<HealthDeviceAlertVo> list) {
        if (list == null || list.isEmpty()) return;
        for (HealthDeviceAlertVo vo : list) {
            if (ObjectUtil.isNotNull(vo.getDeviceId())) {
                HealthDevice device = deviceMapper.selectById(vo.getDeviceId());
                if (ObjectUtil.isNotNull(device)) {
                    vo.setDeviceName(device.getDeviceName());
                }
            }
            if (ObjectUtil.isNotNull(vo.getUserId())) {
                HealthElder elder = elderMapper.selectById(vo.getUserId());
                if (ObjectUtil.isNotNull(elder)) {
                    vo.setUserName(elder.getName());
                }
            }
        }
    }

    @Override
    public HealthDeviceAlertVo selectById(Long id) {
        HealthDeviceAlertVo vo = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(vo)) {
            fillRelationInfo(List.of(vo));
        }
        return vo;
    }

    @Override
    public int insert(HealthDeviceAlertBo bo) {
        HealthDeviceAlert entity = MapstructUtils.convert(bo, HealthDeviceAlert.class);
        if (StringUtils.isBlank(entity.getAlertCode())) {
            entity.setAlertCode(generateAlertCode());
        }
        if (entity.getAlertTime() == null) {
            entity.setAlertTime(new Date());
        }
        if (StringUtils.isBlank(entity.getHandleStatus())) {
            entity.setHandleStatus("0"); // 待处理
        }
        return baseMapper.insert(entity);
    }

    @Override
    public int handleAlert(Long id, String handleType, String handleResult) {
        HealthDeviceAlert alert = new HealthDeviceAlert();
        alert.setId(id);
        alert.setHandleStatus("2"); // 已处理
        alert.setHandleType(handleType);
        alert.setHandleResult(handleResult);
        alert.setHandleTime(new Date());
        alert.setHandleUserId(LoginHelper.getUserId());
        return baseMapper.updateById(alert);
    }

    @Override
    public int ignoreAlert(Long id) {
        HealthDeviceAlert alert = new HealthDeviceAlert();
        alert.setId(id);
        alert.setHandleStatus("3"); // 已忽略
        alert.setHandleTime(new Date());
        alert.setHandleUserId(LoginHelper.getUserId());
        return baseMapper.updateById(alert);
    }

    @Override
    public List<HealthDeviceAlertVo> selectLatestAlerts(int limit) {
        LambdaQueryWrapper<HealthDeviceAlert> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(HealthDeviceAlert::getAlertTime);
        lqw.last("LIMIT " + limit);
        List<HealthDeviceAlertVo> list = baseMapper.selectVoList(lqw);
        fillRelationInfo(list);
        return list;
    }

    private String generateAlertCode() {
        String prefix = "ALT" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<HealthDeviceAlert>()
            .likeRight(HealthDeviceAlert::getAlertCode, prefix));
        return prefix + String.format("%04d", count + 1);
    }

}
