package org.dromara.health.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.health.domain.HealthEmergencyContact;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 紧急联系人视图对象 health_emergency_contact
 *
 * @author health
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HealthEmergencyContact.class)
public class HealthEmergencyContactVo implements Serializable {

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
     * 联系人姓名
     */
    @ExcelProperty(value = "联系人姓名")
    private String name;

    /**
     * 与老人关系(spouse-配偶,child-子女,parent-父母,sibling-兄弟姐妹,other-其他)
     */
    @ExcelProperty(value = "与老人关系", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_relation_type")
    private String relation;

    /**
     * 联系电话
     */
    @ExcelProperty(value = "联系电话")
    private String phone;

    /**
     * 是否主要联系人(0否 1是)
     */
    @ExcelProperty(value = "是否主要联系人", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_yes_no")
    private String isPrimary;

    /**
     * 是否接收告警(0否 1是)
     */
    @ExcelProperty(value = "是否接收告警", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_yes_no")
    private String notifyAlert;

    /**
     * 通知方式(sms-短信,phone-电话,both-短信+电话)
     */
    @ExcelProperty(value = "通知方式", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_notify_type")
    private String notifyType;

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

}
