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
import org.dromara.health.domain.HealthDevice;
import org.dromara.health.domain.HealthElder;
import org.dromara.health.domain.HealthVitalSigns;
import org.dromara.health.domain.bo.HealthDeviceAlertBo;
import org.dromara.health.domain.bo.HealthVitalSignsBo;
import org.dromara.health.domain.vo.HealthVitalSignsVo;
import org.dromara.health.mapper.HealthDeviceMapper;
import org.dromara.health.mapper.HealthElderMapper;
import org.dromara.health.mapper.HealthVitalSignsMapper;
import org.dromara.health.service.IHealthDeviceAlertService;
import org.dromara.health.service.IHealthVitalSignsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体征数据Service实现
 *
 * @author health
 */
@RequiredArgsConstructor
@Service
public class HealthVitalSignsServiceImpl implements IHealthVitalSignsService {

    private final HealthVitalSignsMapper baseMapper;
    private final HealthElderMapper elderMapper;
    private final HealthDeviceMapper deviceMapper;
    private final IHealthDeviceAlertService alertService;

    @Override
    public TableDataInfo<HealthVitalSignsVo> selectPageList(HealthVitalSignsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HealthVitalSigns> lqw = buildQueryWrapper(bo);
        Page<HealthVitalSignsVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        fillRelationInfo(page.getRecords());
        return TableDataInfo.build(page);
    }

    @Override
    public List<HealthVitalSignsVo> selectList(HealthVitalSignsBo bo) {
        LambdaQueryWrapper<HealthVitalSigns> lqw = buildQueryWrapper(bo);
        List<HealthVitalSignsVo> list = baseMapper.selectVoList(lqw);
        fillRelationInfo(list);
        return list;
    }

    private LambdaQueryWrapper<HealthVitalSigns> buildQueryWrapper(HealthVitalSignsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HealthVitalSigns> lqw = Wrappers.lambdaQuery();
        lqw.eq(ObjectUtil.isNotNull(bo.getElderId()), HealthVitalSigns::getElderId, bo.getElderId());
        lqw.eq(ObjectUtil.isNotNull(bo.getDeviceId()), HealthVitalSigns::getDeviceId, bo.getDeviceId());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            HealthVitalSigns::getMeasureTime, params.get("beginTime"), params.get("endTime"));
        lqw.orderByDesc(HealthVitalSigns::getMeasureTime);
        return lqw;
    }

    private void fillRelationInfo(List<HealthVitalSignsVo> list) {
        if (list == null || list.isEmpty()) return;
        for (HealthVitalSignsVo vo : list) {
            if (ObjectUtil.isNotNull(vo.getElderId())) {
                HealthElder elder = elderMapper.selectById(vo.getElderId());
                if (ObjectUtil.isNotNull(elder)) {
                    vo.setElderName(elder.getName());
                }
            }
            if (ObjectUtil.isNotNull(vo.getDeviceId())) {
                HealthDevice device = deviceMapper.selectById(vo.getDeviceId());
                if (ObjectUtil.isNotNull(device)) {
                    vo.setDeviceName(device.getDeviceName());
                }
            }
        }
    }

    @Override
    public HealthVitalSignsVo selectById(Long id) {
        HealthVitalSignsVo vo = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(vo)) {
            fillRelationInfo(List.of(vo));
        }
        return vo;
    }

    @Override
    public List<HealthVitalSignsVo> selectTrendByElderId(Long elderId, Integer days) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        Date startDate = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
        
        LambdaQueryWrapper<HealthVitalSigns> lqw = Wrappers.lambdaQuery();
        lqw.eq(HealthVitalSigns::getElderId, elderId);
        lqw.ge(HealthVitalSigns::getMeasureTime, startDate);
        lqw.orderByAsc(HealthVitalSigns::getMeasureTime);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public int insert(HealthVitalSignsBo bo) {
        HealthVitalSigns entity = MapstructUtils.convert(bo, HealthVitalSigns.class);
        if (entity.getMeasureTime() == null) {
            entity.setMeasureTime(new Date());
        }
        
        // 检测异常并设置标记
        List<String> abnormalItems = checkAbnormal(entity);
        if (!abnormalItems.isEmpty()) {
            entity.setIsAbnormal("1");
            entity.setAbnormalItems(String.join(",", abnormalItems));
            // 创建告警
            createAlert(entity, abnormalItems);
        } else {
            entity.setIsAbnormal("0");
        }
        
        return baseMapper.insert(entity);
    }

    /**
     * 检测体征异常
     */
    private List<String> checkAbnormal(HealthVitalSigns entity) {
        List<String> abnormalItems = new ArrayList<>();
        
        // 体温异常: >37.5 或 <35
        if (ObjectUtil.isNotNull(entity.getTemperature())) {
            if (entity.getTemperature().compareTo(new BigDecimal("37.5")) > 0 
                || entity.getTemperature().compareTo(new BigDecimal("35")) < 0) {
                abnormalItems.add("体温");
            }
        }
        
        // 心率异常: >100 或 <60
        if (ObjectUtil.isNotNull(entity.getHeartRate())) {
            if (entity.getHeartRate() > 100 || entity.getHeartRate() < 60) {
                abnormalItems.add("心率");
            }
        }
        
        // 收缩压异常: >140 或 <90
        if (ObjectUtil.isNotNull(entity.getSystolicPressure())) {
            if (entity.getSystolicPressure() > 140 || entity.getSystolicPressure() < 90) {
                abnormalItems.add("收缩压");
            }
        }
        
        // 舒张压异常: >90 或 <60
        if (ObjectUtil.isNotNull(entity.getDiastolicPressure())) {
            if (entity.getDiastolicPressure() > 90 || entity.getDiastolicPressure() < 60) {
                abnormalItems.add("舒张压");
            }
        }
        
        // 血氧异常: <95
        if (ObjectUtil.isNotNull(entity.getBloodOxygen())) {
            if (entity.getBloodOxygen() < 95) {
                abnormalItems.add("血氧");
            }
        }
        
        return abnormalItems;
    }

    /**
     * 创建体征异常告警
     */
    private void createAlert(HealthVitalSigns entity, List<String> abnormalItems) {
        HealthDeviceAlertBo alertBo = new HealthDeviceAlertBo();
        alertBo.setDeviceId(entity.getDeviceId());
        alertBo.setUserId(entity.getElderId());
        alertBo.setAlertType("vital");
        alertBo.setAlertLevel("2"); // 重要
        alertBo.setAlertTitle("体征数据异常");
        alertBo.setAlertContent("检测到以下体征指标异常：" + String.join("、", abnormalItems));
        alertBo.setAlertTime(entity.getMeasureTime());
        alertService.insert(alertBo);
    }

}
