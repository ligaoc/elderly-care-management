package org.dromara.health.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 紧急联系人表 health_emergency_contact
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_emergency_contact")
public class HealthEmergencyContact extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 联系人姓名
     */
    private String name;

    /**
     * 与老人关系(spouse-配偶,child-子女,parent-父母,sibling-兄弟姐妹,other-其他)
     */
    private String relation;

    /**
     * 联系电话
     */
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

    /**
     * 删除标志(0存在 1删除)
     */
    @TableLogic
    private String delFlag;

}
