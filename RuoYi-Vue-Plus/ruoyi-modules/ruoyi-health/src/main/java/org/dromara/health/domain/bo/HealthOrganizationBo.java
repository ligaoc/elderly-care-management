package org.dromara.health.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.health.domain.HealthOrganization;

import java.math.BigDecimal;

/**
 * 组织机构业务对象 health_organization
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HealthOrganization.class, reverseConvertGenerate = false)
public class HealthOrganizationBo extends BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 组织编码
     */
    @NotBlank(message = "组织编码不能为空")
    @Size(max = 50, message = "组织编码长度不能超过{max}个字符")
    private String orgCode;

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空")
    @Size(max = 200, message = "组织名称长度不能超过{max}个字符")
    private String orgName;

    /**
     * 组织类型(company-企业,hospital-医院,community-社区,family-家庭)
     */
    private String orgType;

    /**
     * 负责人
     */
    @Size(max = 50, message = "负责人长度不能超过{max}个字符")
    private String leader;

    /**
     * 联系电话
     */
    @Size(max = 20, message = "联系电话长度不能超过{max}个字符")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过{max}个字符")
    private String email;

    /**
     * 地址
     */
    @Size(max = 500, message = "地址长度不能超过{max}个字符")
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
