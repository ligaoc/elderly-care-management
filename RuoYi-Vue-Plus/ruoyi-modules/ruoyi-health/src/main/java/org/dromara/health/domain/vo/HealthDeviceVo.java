package org.dromara.health.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.health.domain.HealthDevice;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备信息视图对象 health_device
 *
 * @author health
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HealthDevice.class)
public class HealthDeviceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 设备编码
     */
    @ExcelProperty(value = "设备编码")
    private String deviceCode;

    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称")
    private String deviceName;

    /**
     * 设备类型ID
     */
    private Long deviceTypeId;

    /**
     * 设备类型名称
     */
    @ExcelProperty(value = "设备类型")
    private String deviceTypeName;

    /**
     * 设备型号
     */
    @ExcelProperty(value = "设备型号")
    private String deviceModel;

    /**
     * 生产厂商
     */
    @ExcelProperty(value = "生产厂商")
    private String manufacturer;

    /**
     * 序列号
     */
    @ExcelProperty(value = "序列号")
    private String serialNumber;

    /**
     * MAC地址
     */
    private String macAddress;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * 绑定用户ID
     */
    private Long bindUserId;

    /**
     * 绑定用户名称
     */
    @ExcelProperty(value = "绑定用户")
    private String bindUserName;

    /**
     * 绑定时间
     */
    private Date bindTime;

    /**
     * 在线状态(0离线 1在线)
     */
    @ExcelProperty(value = "在线状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_online_status")
    private String onlineStatus;

    /**
     * 最后上线时间
     */
    private Date lastOnlineTime;

    /**
     * 最后离线时间
     */
    private Date lastOfflineTime;

    /**
     * 电量百分比
     */
    @ExcelProperty(value = "电量(%)")
    private Integer batteryLevel;

    /**
     * 信号强度
     */
    private Integer signalStrength;

    /**
     * 设备位置
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
     * 状态(0正常 1停用 2维修)
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_device_status")
    private String status;

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
