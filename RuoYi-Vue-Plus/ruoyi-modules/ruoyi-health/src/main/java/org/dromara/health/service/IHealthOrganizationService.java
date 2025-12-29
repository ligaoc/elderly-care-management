package org.dromara.health.service;

import cn.hutool.core.lang.tree.Tree;
import org.dromara.health.domain.bo.HealthOrganizationBo;
import org.dromara.health.domain.vo.HealthOrganizationVo;

import java.util.List;

/**
 * 组织机构Service接口
 *
 * @author health
 */
public interface IHealthOrganizationService {

    List<HealthOrganizationVo> selectList(HealthOrganizationBo bo);

    List<Tree<Long>> selectTreeList(HealthOrganizationBo bo);

    HealthOrganizationVo selectById(Long id);

    boolean checkOrgCodeUnique(HealthOrganizationBo bo);

    boolean checkOrgNameUnique(HealthOrganizationBo bo);

    boolean hasChildById(Long id);

    boolean hasElderById(Long id);

    boolean hasStaffById(Long id);

    int insert(HealthOrganizationBo bo);

    int update(HealthOrganizationBo bo);

    int deleteById(Long id);

    int deleteByIds(List<Long> ids);

}
