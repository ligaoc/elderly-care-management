package org.dromara.health.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.health.domain.bo.HealthVitalSignsBo;
import org.dromara.health.domain.vo.HealthVitalSignsVo;

import java.util.List;

/**
 * 体征数据Service接口
 *
 * @author health
 */
public interface IHealthVitalSignsService {

    TableDataInfo<HealthVitalSignsVo> selectPageList(HealthVitalSignsBo bo, PageQuery pageQuery);

    List<HealthVitalSignsVo> selectList(HealthVitalSignsBo bo);

    HealthVitalSignsVo selectById(Long id);

    /**
     * 查询体征趋势数据
     */
    List<HealthVitalSignsVo> selectTrendByElderId(Long elderId, Integer days);

    int insert(HealthVitalSignsBo bo);

}
