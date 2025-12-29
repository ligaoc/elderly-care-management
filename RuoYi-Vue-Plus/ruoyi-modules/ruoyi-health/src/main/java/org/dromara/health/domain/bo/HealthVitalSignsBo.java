package org.dromara.health.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.health.domain.HealthVitalSigns;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 体征数据业务对象 health_vital_signs
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HealthVitalSigns.class, reverseConvertGenerate = false)
public class HealthVitalSignsBo extends BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 老人ID
     */
    @NotNull(message = "老人不能为空")
    private Long elderId;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 测量时间
     */
    @NotNull(message = "测量时间不能为空")
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
     * 备注
     */
    private String remark;

}
