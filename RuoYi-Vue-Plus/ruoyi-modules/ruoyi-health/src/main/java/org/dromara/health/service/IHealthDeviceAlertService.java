package org.dromara.health.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.health.domain.bo.HealthDeviceAlertBo;
import org.dromara.health.domain.vo.HealthDeviceAlertVo;

import java.util.List;

/**
 * 设备告警Service接口
 *
 * @author health
 */
public interface IHealthDeviceAlertService {

    /**
     * 分页查询告警列表
     */
    TableDataInfo<HealthDeviceAlertVo> selectPageList(HealthDeviceAlertBo bo, PageQuery pageQuery);

    /**
     * 查询告警列表
     */
    List<HealthDeviceAlertVo> selectList(HealthDeviceAlertBo bo);

    /**
     * 根据ID查询告警
     */
    HealthDeviceAlertVo selectById(Long id);

    /**
     * 新增告警
     */
    int insert(HealthDeviceAlertBo bo);

    /**
     * 处理告警
     */
    int handleAlert(Long id, String handleType, String handleResult);

    /**
     * 忽略告警
     */
    int ignoreAlert(Long id);

    /**
     * 获取最新告警列表
     */
    List<HealthDeviceAlertVo> selectLatestAlerts(int limit);

}
