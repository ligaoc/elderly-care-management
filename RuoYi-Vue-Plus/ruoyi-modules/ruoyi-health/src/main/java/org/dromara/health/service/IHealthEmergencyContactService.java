package org.dromara.health.service;

import org.dromara.health.domain.bo.HealthEmergencyContactBo;
import org.dromara.health.domain.vo.HealthEmergencyContactVo;

import java.util.List;

/**
 * 紧急联系人Service接口
 *
 * @author health
 */
public interface IHealthEmergencyContactService {

    List<HealthEmergencyContactVo> selectListByElderId(Long elderId);

    HealthEmergencyContactVo selectById(Long id);

    int insert(HealthEmergencyContactBo bo);

    int update(HealthEmergencyContactBo bo);

    int deleteById(Long id);

    int deleteByIds(List<Long> ids);

}
