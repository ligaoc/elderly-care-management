package org.dromara.health.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.health.domain.HealthDevice;
import org.dromara.health.domain.HealthDeviceAlert;
import org.dromara.health.domain.HealthElder;
import org.dromara.health.domain.vo.HealthDeviceAlertVo;
import org.dromara.health.mapper.HealthDeviceAlertMapper;
import org.dromara.health.mapper.HealthDeviceMapper;
import org.dromara.health.mapper.HealthElderMapper;
import org.dromara.health.service.IHealthDashboardService;
import org.dromara.health.service.IHealthDeviceAlertService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 大屏数据Service实现
 *
 * @author health
 */
@RequiredArgsConstructor
@Service
public class HealthDashboardServiceImpl implements IHealthDashboardService {

    private final HealthDeviceMapper deviceMapper;
    private final HealthElderMapper elderMapper;
    private final HealthDeviceAlertMapper alertMapper;
    private final IHealthDeviceAlertService alertService;

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 设备总数
        Long deviceTotal = deviceMapper.selectCount(Wrappers.lambdaQuery());
        result.put("deviceTotal", deviceTotal);
        
        // 在线设备数
        Long deviceOnline = deviceMapper.selectCount(Wrappers.<HealthDevice>lambdaQuery()
            .eq(HealthDevice::getOnlineStatus, "1"));
        result.put("deviceOnline", deviceOnline);
        
        // 老人总数
        Long elderTotal = elderMapper.selectCount(Wrappers.lambdaQuery());
        result.put("elderTotal", elderTotal);
        
        // 今日告警数
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        Date todayStartDate = Date.from(todayStart.atZone(ZoneId.systemDefault()).toInstant());
        Long alertToday = alertMapper.selectCount(Wrappers.<HealthDeviceAlert>lambdaQuery()
            .ge(HealthDeviceAlert::getAlertTime, todayStartDate));
        result.put("alertToday", alertToday);
        
        // 待处理告警数
        Long alertPending = alertMapper.selectCount(Wrappers.<HealthDeviceAlert>lambdaQuery()
            .eq(HealthDeviceAlert::getHandleStatus, "0"));
        result.put("alertPending", alertPending);
        
        return result;
    }

    @Override
    public Map<String, Object> getDeviceOnlineRate() {
        Map<String, Object> result = new HashMap<>();
        
        Long total = deviceMapper.selectCount(Wrappers.lambdaQuery());
        Long online = deviceMapper.selectCount(Wrappers.<HealthDevice>lambdaQuery()
            .eq(HealthDevice::getOnlineStatus, "1"));
        Long offline = total - online;
        
        result.put("total", total);
        result.put("online", online);
        result.put("offline", offline);
        result.put("rate", total > 0 ? Math.round(online * 100.0 / total) : 0);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getAlertTrend() {
        List<Map<String, Object>> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        
        // 获取最近7天的告警数量
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
            
            Date startDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
            
            Long count = alertMapper.selectCount(Wrappers.<HealthDeviceAlert>lambdaQuery()
                .ge(HealthDeviceAlert::getAlertTime, startDate)
                .lt(HealthDeviceAlert::getAlertTime, endDate));
            
            Map<String, Object> item = new HashMap<>();
            item.put("date", date.format(formatter));
            item.put("count", count);
            result.add(item);
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getAlertTypeDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 告警类型映射
        Map<String, String> typeNames = new LinkedHashMap<>();
        typeNames.put("sos", "SOS求助");
        typeNames.put("fall", "摔倒告警");
        typeNames.put("vital", "体征异常");
        typeNames.put("offline", "设备离线");
        typeNames.put("battery", "低电量");
        
        for (Map.Entry<String, String> entry : typeNames.entrySet()) {
            Long count = alertMapper.selectCount(Wrappers.<HealthDeviceAlert>lambdaQuery()
                .eq(HealthDeviceAlert::getAlertType, entry.getKey()));
            
            Map<String, Object> item = new HashMap<>();
            item.put("type", entry.getKey());
            item.put("name", entry.getValue());
            item.put("count", count);
            result.add(item);
        }
        
        return result;
    }

    @Override
    public List<HealthDeviceAlertVo> getLatestAlerts() {
        return alertService.selectLatestAlerts(10);
    }

    @Override
    public List<Map<String, Object>> getDeviceDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 查询有位置信息的设备
        LambdaQueryWrapper<HealthDevice> lqw = Wrappers.lambdaQuery();
        lqw.isNotNull(HealthDevice::getLongitude);
        lqw.isNotNull(HealthDevice::getLatitude);
        lqw.select(HealthDevice::getId, HealthDevice::getDeviceName, 
            HealthDevice::getLongitude, HealthDevice::getLatitude, 
            HealthDevice::getOnlineStatus, HealthDevice::getLocation);
        
        List<HealthDevice> devices = deviceMapper.selectList(lqw);
        
        for (HealthDevice device : devices) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", device.getId());
            item.put("name", device.getDeviceName());
            item.put("lng", device.getLongitude());
            item.put("lat", device.getLatitude());
            item.put("online", "1".equals(device.getOnlineStatus()));
            item.put("location", device.getLocation());
            result.add(item);
        }
        
        return result;
    }

}
