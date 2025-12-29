package org.dromara.health.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 服务人员表 health_service_staff
 *
 * @author health
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_service_staff")
public class HealthServiceStaff extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 人员编码
     */
    private String staffCode;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别(0未知 1男 2女)
     */
    private String gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 人员类型(escort-陪诊,medical-医务,rescue-救援)
     */
    private String staffType;

    /**
     * 所属组织ID
     */
    private Long orgId;

    /**
     * 职称
     */
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

    /**
     * 删除标志(0存在 1删除)
     */
    @TableLogic
    private String delFlag;

}
