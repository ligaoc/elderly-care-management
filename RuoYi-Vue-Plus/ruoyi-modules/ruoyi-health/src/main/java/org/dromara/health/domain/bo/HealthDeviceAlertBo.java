package org.dromara.health.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.health.domain.HealthDeviceAlert;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备告警业务对象 health_device_alert
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HealthDeviceAlert.class, reverseConvertGenerate = false)
public class HealthDeviceAlertBo extends BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 告警编码
     */
    private String alertCode;

    /**
     * 设备ID
     */
    @NotNull(message = "设备不能为空")
    private Long deviceId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 告警类型(sos-求助,fall-摔倒,vital-体征异常,offline-离线,battery-低电量)
     */
    @NotBlank(message = "告警类型不能为空")
    private String alertType;

    /**
     * 告警级别(1普通 2重要 3紧急)
     */
    private String alertLevel;

    /**
     * 告警标题
     */
    @NotBlank(message = "告警标题不能为空")
    private String alertTitle;

    /**
     * 告警内容
     */
    private String alertContent;

    /**
     * 告警时间
     */
    @NotNull(message = "告警时间不能为空")
    private Date alertTime;

    /**
     * 告警位置
     */
    private String location;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 处理状态(0待处理 1处理中 2已处理 3已忽略)
     */
    private String handleStatus;

    /**
     * 处理方式(phone-电话,onsite-现场,rescue-救援)
     */
    private String handleType;

    /**
     * 处理结果
     */
    private String handleResult;

}
