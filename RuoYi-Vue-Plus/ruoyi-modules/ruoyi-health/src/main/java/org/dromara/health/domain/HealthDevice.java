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
 * 设备信息表 health_device
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_device")
public class HealthDevice extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型ID
     */
    private Long deviceTypeId;

    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 生产厂商
     */
    private String manufacturer;

    /**
     * 序列号
     */
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
     * 绑定时间
     */
    private Date bindTime;

    /**
     * 在线状态(0离线 1在线)
     */
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
    private String status;

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
