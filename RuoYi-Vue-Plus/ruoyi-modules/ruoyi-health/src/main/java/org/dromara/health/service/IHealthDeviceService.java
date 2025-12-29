package org.dromara.health.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.health.domain.bo.HealthDeviceBo;
import org.dromara.health.domain.vo.HealthDeviceVo;

import java.util.List;

/**
 * 设备管理Service接口
 *
 * @author health
 */
public interface IHealthDeviceService {

    /**
     * 分页查询设备列表
     *
     * @param bo        设备信息
     * @param pageQuery 分页参数
     * @return 设备分页列表
     */
    TableDataInfo<HealthDeviceVo> selectPageList(HealthDeviceBo bo, PageQuery pageQuery);

    /**
     * 查询设备列表
     *
     * @param bo 设备信息
     * @return 设备列表
     */
    List<HealthDeviceVo> selectList(HealthDeviceBo bo);

    /**
     * 根据ID查询设备
     *
     * @param id 主键ID
     * @return 设备信息
     */
    HealthDeviceVo selectById(Long id);

    /**
     * 校验设备编码是否唯一
     *
     * @param bo 设备信息
     * @return 结果
     */
    boolean checkDeviceCodeUnique(HealthDeviceBo bo);

    /**
     * 新增设备
     *
     * @param bo 设备信息
     * @return 结果
     */
    int insert(HealthDeviceBo bo);

    /**
     * 修改设备
     *
     * @param bo 设备信息
     * @return 结果
     */
    int update(HealthDeviceBo bo);

    /**
     * 删除设备
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteById(Long id);

    /**
     * 批量删除设备
     *
     * @param ids 主键ID集合
     * @return 结果
     */
    int deleteByIds(List<Long> ids);

    /**
     * 绑定设备
     *
     * @param id     设备ID
     * @param userId 用户ID
     * @return 结果
     */
    int bindDevice(Long id, Long userId);

    /**
     * 解绑设备
     *
     * @param id 设备ID
     * @return 结果
     */
    int unbindDevice(Long id);

    /**
     * 生成设备编码
     *
     * @return 设备编码
     */
    String generateDeviceCode();

}
