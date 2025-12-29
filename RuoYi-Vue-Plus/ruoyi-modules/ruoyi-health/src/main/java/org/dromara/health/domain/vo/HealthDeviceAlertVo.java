package org.dromara.health.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.health.domain.HealthDeviceAlert;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备告警视图对象 health_device_alert
 *
 * @author health
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HealthDeviceAlert.class)
public class HealthDeviceAlertVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 告警编码
     */
    @ExcelProperty(value = "告警编码")
    private String alertCode;

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
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    @ExcelProperty(value = "用户名称")
    private String userName;

    /**
     * 告警类型(sos-求助,fall-摔倒,vital-体征异常,offline-离线,battery-低电量)
     */
    @ExcelProperty(value = "告警类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_alert_type")
    private String alertType;

    /**
     * 告警级别(1普通 2重要 3紧急)
     */
    @ExcelProperty(value = "告警级别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_alert_level")
    private String alertLevel;

    /**
     * 告警标题
     */
    @ExcelProperty(value = "告警标题")
    private String alertTitle;

    /**
     * 告警内容
     */
    private String alertContent;

    /**
     * 告警时间
     */
    @ExcelProperty(value = "告警时间")
    private Date alertTime;

    /**
     * 告警位置
     */
    @ExcelProperty(value = "告警位置")
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
    @ExcelProperty(value = "处理状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_handle_status")
    private String handleStatus;

    /**
     * 处理方式(phone-电话,onsite-现场,rescue-救援)
     */
    @ExcelProperty(value = "处理方式", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_handle_type")
    private String handleType;

    /**
     * 处理人ID
     */
    private Long handleUserId;

    /**
     * 处理人名称
     */
    @ExcelProperty(value = "处理人")
    private String handleUserName;

    /**
     * 处理时间
     */
    @ExcelProperty(value = "处理时间")
    private Date handleTime;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 创建时间
     */
    private Date createTime;

}
