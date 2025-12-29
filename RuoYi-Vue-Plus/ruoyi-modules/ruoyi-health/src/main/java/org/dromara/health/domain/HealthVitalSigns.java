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
 * 体征数据表 health_vital_signs
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_vital_signs")
public class HealthVitalSigns extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 测量时间
     */
    private Date measureTime;

    /**
     * 体温(℃)
     */
    private BigDecimal temperature;

    /**
     * 心率(次/分)
     */
    private Integer heartRate;

    /**
     * 收缩压(mmHg)
     */
    private Integer systolicPressure;

    /**
     * 舒张压(mmHg)
     */
    private Integer diastolicPressure;

    /**
     * 血氧饱和度(%)
     */
    private Integer bloodOxygen;

    /**
     * 血糖(mmol/L)
     */
    private BigDecimal bloodSugar;

    /**
     * 呼吸频率(次/分)
     */
    private Integer respiratoryRate;

    /**
     * 步数
     */
    private Integer steps;

    /**
     * 睡眠时长(分钟)
     */
    private Integer sleepDuration;

    /**
     * 是否异常(0正常 1异常)
     */
    private String isAbnormal;

    /**
     * 异常项目(逗号分隔)
     */
    private String abnormalItems;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志(0存在 1删除)
     */
    @TableLogic
    private String delFlag;

}
