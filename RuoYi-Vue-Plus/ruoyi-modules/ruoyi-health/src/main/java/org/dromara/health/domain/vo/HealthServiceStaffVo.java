package org.dromara.health.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.health.domain.HealthServiceStaff;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 服务人员视图对象 health_service_staff
 *
 * @author health
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HealthServiceStaff.class)
public class HealthServiceStaffVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 人员编码
     */
    @ExcelProperty(value = "人员编码")
    private String staffCode;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名")
    private String name;

    /**
     * 性别(0未知 1男 2女)
     */
    @ExcelProperty(value = "性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_user_sex")
    private String gender;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    private String phone;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    private String idCard;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 人员类型(escort-陪诊,medical-医务,rescue-救援)
     */
    @ExcelProperty(value = "人员类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_staff_type")
    private String staffType;

    /**
     * 所属组织ID
     */
    private Long orgId;

    /**
     * 所属组织名称
     */
    @ExcelProperty(value = "所属组织")
    private String orgName;

    /**
     * 职称
     */
    @ExcelProperty(value = "职称")
    private String jobTitle;

    /**
     * 资质证书
     */
    private String certificate;

    /**
     * 工作年限
     */
    @ExcelProperty(value = "工作年限")
    private Integer workYears;

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
