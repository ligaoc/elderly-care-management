package org.dromara.health.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.health.domain.HealthDeviceType;

/**
 * 设备类型业务对象 health_device_type
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HealthDeviceType.class, reverseConvertGenerate = false)
public class HealthDeviceTypeBo extends BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 类型名称
     */
    @NotBlank(message = "类型名称不能为空")
    @Size(max = 100, message = "类型名称长度不能超过{max}个字符")
    private String typeName;

    /**
     * 类型编码
     */
    @NotBlank(message = "类型编码不能为空")
    @Size(max = 50, message = "类型编码长度不能超过{max}个字符")
    private String typeCode;

    /**
     * 设备分类(wearable-智能穿戴,detection-智能检测,iot-智能物联)
     */
    @NotBlank(message = "设备分类不能为空")
    private String category;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态(0正常 1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
