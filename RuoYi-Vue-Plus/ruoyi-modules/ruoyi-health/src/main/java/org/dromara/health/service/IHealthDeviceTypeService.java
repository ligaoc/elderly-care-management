package org.dromara.health.service;

import cn.hutool.core.lang.tree.Tree;
import org.dromara.health.domain.bo.HealthDeviceTypeBo;
import org.dromara.health.domain.vo.HealthDeviceTypeVo;

import java.util.List;

/**
 * 设备类型Service接口
 *
 * @author health
 */
public interface IHealthDeviceTypeService {

    /**
     * 查询设备类型列表
     *
     * @param bo 设备类型
     * @return 设备类型集合
     */
    List<HealthDeviceTypeVo> selectList(HealthDeviceTypeBo bo);

    /**
     * 查询设备类型树结构
     *
     * @param bo 设备类型
     * @return 设备类型树
     */
    List<Tree<Long>> selectTreeList(HealthDeviceTypeBo bo);

    /**
     * 根据ID查询设备类型
     *
     * @param id 主键ID
     * @return 设备类型
     */
    HealthDeviceTypeVo selectById(Long id);

    /**
     * 校验类型编码是否唯一
     *
     * @param bo 设备类型
     * @return 结果
     */
    boolean checkTypeCodeUnique(HealthDeviceTypeBo bo);

    /**
     * 校验类型名称是否唯一
     *
     * @param bo 设备类型
     * @return 结果
     */
    boolean checkTypeNameUnique(HealthDeviceTypeBo bo);

    /**
     * 是否存在子节点
     *
     * @param id 主键ID
     * @return 结果
     */
    boolean hasChildById(Long id);

    /**
     * 是否存在关联设备
     *
     * @param id 主键ID
     * @return 结果
     */
    boolean hasDeviceById(Long id);

    /**
     * 新增设备类型
     *
     * @param bo 设备类型
     * @return 结果
     */
    int insert(HealthDeviceTypeBo bo);

    /**
     * 修改设备类型
     *
     * @param bo 设备类型
     * @return 结果
     */
    int update(HealthDeviceTypeBo bo);

    /**
     * 删除设备类型
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteById(Long id);

    /**
     * 批量删除设备类型
     *
     * @param ids 主键ID集合
     * @return 结果
     */
    int deleteByIds(List<Long> ids);

}
