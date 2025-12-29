package org.dromara.health.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.health.domain.HealthDevice;

import java.math.BigDecimal;

/**
 * 设备信息业务对象 health_device
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HealthDevice.class, reverseConvertGenerate = false)
public class HealthDeviceBo extends BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 设备编码
     */
    @Size(max = 100, message = "设备编码长度不能超过{max}个字符")
    private String deviceCode;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    @Size(max = 200, message = "设备名称长度不能超过{max}个字符")
    private String deviceName;

    /**
     * 设备类型ID
     */
    @NotNull(message = "设备类型不能为空")
    private Long deviceTypeId;

    /**
     * 设备型号
     */
    @Size(max = 100, message = "设备型号长度不能超过{max}个字符")
    private String deviceModel;

    /**
     * 生产厂商
     */
    @Size(max = 200, message = "生产厂商长度不能超过{max}个字符")
    private String manufacturer;

    /**
     * 序列号
     */
    @Size(max = 100, message = "序列号长度不能超过{max}个字符")
    private String serialNumber;

    /**
     * MAC地址
     */
    @Size(max = 50, message = "MAC地址长度不能超过{max}个字符")
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

}
