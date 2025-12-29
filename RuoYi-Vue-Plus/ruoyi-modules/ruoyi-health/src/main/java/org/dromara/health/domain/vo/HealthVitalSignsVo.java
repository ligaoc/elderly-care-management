package org.dromara.health.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.health.domain.HealthVitalSigns;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 体征数据视图对象 health_vital_signs
 *
 * @author health
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HealthVitalSigns.class)
public class HealthVitalSignsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 老人姓名
     */
    @ExcelProperty(value = "老人姓名")
    private String elderName;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称")
    private String deviceName;

    /**
     * 测量时间
     */
    @ExcelProperty(value = "测量时间")
    private Date measureTime;

    /**
     * 体温(℃)
     */
    @ExcelProperty(value = "体温(℃)")
    private BigDecimal temperature;

    /**
     * 心率(次/分)
     */
    @ExcelProperty(value = "心率(次/分)")
    private Integer heartRate;

    /**
     * 收缩压(mmHg)
     */
    @ExcelProperty(value = "收缩压(mmHg)")
    private Integer systolicPressure;

    /**
     * 舒张压(mmHg)
     */
    @ExcelProperty(value = "舒张压(mmHg)")
    private Integer diastolicPressure;

    /**
     * 血氧饱和度(%)
     */
    @ExcelProperty(value = "血氧(%)")
    private Integer bloodOxygen;

    /**
     * 血糖(mmol/L)
     */
    @ExcelProperty(value = "血糖(mmol/L)")
    private BigDecimal bloodSugar;

    /**
     * 呼吸频率(次/分)
     */
    @ExcelProperty(value = "呼吸频率(次/分)")
    private Integer respiratoryRate;

    /**
     * 步数
     */
    @ExcelProperty(value = "步数")
    private Integer steps;

    /**
     * 睡眠时长(分钟)
     */
    @ExcelProperty(value = "睡眠时长(分钟)")
    private Integer sleepDuration;

    /**
     * 是否异常(0正常 1异常)
     */
    @ExcelProperty(value = "是否异常", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_abnormal_status")
    private String isAbnormal;

    /**
     * 异常项目(逗号分隔)
     */
    @ExcelProperty(value = "异常项目")
    private String abnormalItems;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
