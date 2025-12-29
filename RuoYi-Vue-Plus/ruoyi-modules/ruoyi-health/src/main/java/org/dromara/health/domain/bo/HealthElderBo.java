package org.dromara.health.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.health.domain.HealthElder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 健康用户(老人)业务对象 health_elder
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HealthElder.class, reverseConvertGenerate = false)
public class HealthElderBo extends BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户编码
     */
    @Size(max = 50, message = "用户编码长度不能超过{max}个字符")
    private String userCode;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过{max}个字符")
    private String name;

    /**
     * 性别(0未知 1男 2女)
     */
    private String gender;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 身份证号
     */
    @Size(max = 20, message = "身份证号长度不能超过{max}个字符")
    private String idCard;

    /**
     * 手机号
     */
    @Size(max = 20, message = "手机号长度不能超过{max}个字符")
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
     * 居住地址
     */
    @Size(max = 500, message = "居住地址长度不能超过{max}个字符")
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
    private String bloodType;

    /**
     * 身高(cm)
     */
    private BigDecimal height;

    /**
     * 体重(kg)
     */
    private BigDecimal weight;

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
    private String careLevel;

    /**
     * 风险等级(1低 2中 3高)
     */
    private String riskLevel;

    /**
     * 状态(0正常 1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
