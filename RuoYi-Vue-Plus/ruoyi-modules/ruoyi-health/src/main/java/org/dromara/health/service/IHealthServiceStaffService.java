package org.dromara.health.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.health.domain.bo.HealthServiceStaffBo;
import org.dromara.health.domain.vo.HealthServiceStaffVo;

import java.util.List;

/**
 * 服务人员Service接口
 *
 * @author health
 */
public interface IHealthServiceStaffService {

    TableDataInfo<HealthServiceStaffVo> selectPageList(HealthServiceStaffBo bo, PageQuery pageQuery);

    List<HealthServiceStaffVo> selectList(HealthServiceStaffBo bo);

    HealthServiceStaffVo selectById(Long id);

    boolean checkStaffCodeUnique(HealthServiceStaffBo bo);

    int insert(HealthServiceStaffBo bo);

    int update(HealthServiceStaffBo bo);

    int deleteById(Long id);

    int deleteByIds(List<Long> ids);

}
