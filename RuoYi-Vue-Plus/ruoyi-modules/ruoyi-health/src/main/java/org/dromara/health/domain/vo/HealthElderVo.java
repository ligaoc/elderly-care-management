package org.dromara.health.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.health.domain.HealthElder;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 健康用户(老人)视图对象 health_elder
 *
 * @author health
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HealthElder.class)
public class HealthElderVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 用户编码
     */
    @ExcelProperty(value = "用户编码")
    private String userCode;

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
     * 出生日期
     */
    @ExcelProperty(value = "出生日期")
    private Date birthday;

    /**
     * 年龄
     */
    @ExcelProperty(value = "年龄")
    private Integer age;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    private String idCard;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    private String phone;

    /**
     * 头像
     */
    private String avatar;

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
     * 居住地址
     */
    @ExcelProperty(value = "居住地址")
    private String address;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 血型(A,B,AB,O)
     */
    @ExcelProperty(value = "血型")
    private String bloodType;

    /**
     * 身高(cm)
     */
    @ExcelProperty(value = "身高(cm)")
    private BigDecimal height;

    /**
     * 体重(kg)
     */
    @ExcelProperty(value = "体重(kg)")
    private BigDecimal weight;

    /**
     * BMI指数
     */
    @ExcelProperty(value = "BMI指数")
    private BigDecimal bmi;

    /**
     * 既往病史(JSON)
     */
    private String medicalHistory;

    /**
     * 过敏史(JSON)
     */
    private String allergyHistory;

    /**
     * 家族病史(JSON)
     */
    private String familyHistory;

    /**
     * 护理等级(1自理 2半自理 3不能自理)
     */
    @ExcelProperty(value = "护理等级", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_care_level")
    private String careLevel;

    /**
     * 风险等级(1低 2中 3高)
     */
    @ExcelProperty(value = "风险等级", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "health_risk_level")
    private String riskLevel;

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
