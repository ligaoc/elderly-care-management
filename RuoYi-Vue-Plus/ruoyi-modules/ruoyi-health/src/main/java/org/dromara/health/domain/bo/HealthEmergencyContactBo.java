package org.dromara.health.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.health.domain.HealthEmergencyContact;

/**
 * 紧急联系人业务对象 health_emergency_contact
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HealthEmergencyContact.class, reverseConvertGenerate = false)
public class HealthEmergencyContactBo extends BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 老人ID
     */
    @NotNull(message = "老人ID不能为空")
    private Long elderId;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空")
    @Size(max = 50, message = "联系人姓名长度不能超过{max}个字符")
    private String name;

    /**
     * 与老人关系(spouse-配偶,child-子女,parent-父母,sibling-兄弟姐妹,other-其他)
     */
    @NotBlank(message = "与老人关系不能为空")
    private String relation;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Size(max = 20, message = "联系电话长度不能超过{max}个字符")
    private String phone;

    /**
     * 是否主要联系人(0否 1是)
     */
    private String isPrimary;

    /**
     * 是否接收告警(0否 1是)
     */
    private String notifyAlert;

    /**
     * 通知方式(sms-短信,phone-电话,both-短信+电话)
     */
    private String notifyType;

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
