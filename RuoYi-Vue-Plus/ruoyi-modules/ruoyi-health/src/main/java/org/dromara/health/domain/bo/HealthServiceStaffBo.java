package org.dromara.health.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.health.domain.HealthServiceStaff;

/**
 * 服务人员业务对象 health_service_staff
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HealthServiceStaff.class, reverseConvertGenerate = false)
public class HealthServiceStaffBo extends BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 人员编码
     */
    @Size(max = 50, message = "人员编码长度不能超过{max}个字符")
    private String staffCode;

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
     * 手机号
     */
    @Size(max = 20, message = "手机号长度不能超过{max}个字符")
    private String phone;

    /**
     * 身份证号
     */
    @Size(max = 20, message = "身份证号长度不能超过{max}个字符")
    private String idCard;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 人员类型(escort-陪诊,medical-医务,rescue-救援)
     */
    @NotBlank(message = "人员类型不能为空")
    private String staffType;

    /**
     * 所属组织ID
     */
    private Long orgId;

    /**
     * 职称
     */
    @Size(max = 50, message = "职称长度不能超过{max}个字符")
    private String jobTitle;

    /**
     * 资质证书
     */
    private String certificate;

    /**
     * 工作年限
     */
    private Integer workYears;

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
