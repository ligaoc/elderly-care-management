package org.dromara.health.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.health.domain.HealthDeviceType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设备类型视图对象 health_device_type
 *
 * @author health
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HealthDeviceType.class)
public class HealthDeviceTypeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 类型名称
     */
    @ExcelProperty(value = "类型名称")
    private String typeName;

    /**
     * 类型编码
     */
    @ExcelProperty(value = "类型编码")
    private String typeCode;

    /**
     * 设备分类(wearable-智能穿戴,detection-智能检测,iot-智能物联)
     */
    @ExcelProperty(value = "设备分类", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_device_category")
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
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
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

    /**
     * 子类型
     */
    private List<HealthDeviceTypeVo> children = new ArrayList<>();

}
