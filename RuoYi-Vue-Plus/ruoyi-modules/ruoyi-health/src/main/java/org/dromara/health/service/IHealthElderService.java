package org.dromara.health.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.health.domain.bo.HealthElderBo;
import org.dromara.health.domain.vo.HealthElderVo;

import java.util.List;

/**
 * 老人管理Service接口
 *
 * @author health
 */
public interface IHealthElderService {

    TableDataInfo<HealthElderVo> selectPageList(HealthElderBo bo, PageQuery pageQuery);

    List<HealthElderVo> selectList(HealthElderBo bo);

    HealthElderVo selectById(Long id);

    boolean checkUserCodeUnique(HealthElderBo bo);

    int insert(HealthElderBo bo);

    int update(HealthElderBo bo);

    int deleteById(Long id);

    int deleteByIds(List<Long> ids);

    String generateUserCode();

}
