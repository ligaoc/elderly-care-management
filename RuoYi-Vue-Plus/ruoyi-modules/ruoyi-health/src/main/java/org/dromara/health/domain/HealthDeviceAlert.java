package org.dromara.health.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备告警表 health_device_alert
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_device_alert")
public class HealthDeviceAlert extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 告警编码
     */
    private String alertCode;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 告警类型(sos-求助,fall-摔倒,vital-体征异常,offline-离线,battery-低电量)
     */
    private String alertType;

    /**
     * 告警级别(1普通 2重要 3紧急)
     */
    private String alertLevel;

    /**
     * 告警标题
     */
    private String alertTitle;

    /**
     * 告警内容
     */
    private String alertContent;

    /**
     * 告警时间
     */
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
     * 处理人ID
     */
    private Long handleUserId;

    /**
     * 处理时间
     */
    private Date handleTime;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 删除标志(0存在 1删除)
     */
    @TableLogic
    private String delFlag;

}
