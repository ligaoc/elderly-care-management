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
 * 健康用户表(老人/被监护人) health_elder
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_elder")
public class HealthElder extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 姓名
     */
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
    private String idCard;

    /**
     * 手机号
     */
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
     * BMI指数
     */
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

    /**
     * 删除标志(0存在 1删除)
     */
    @TableLogic
    private String delFlag;

}
