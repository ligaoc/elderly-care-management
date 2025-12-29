package org.dromara.health.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.health.domain.HealthDevice;
import org.dromara.health.domain.HealthDeviceType;
import org.dromara.health.domain.HealthElder;
import org.dromara.health.domain.bo.HealthDeviceBo;
import org.dromara.health.domain.vo.HealthDeviceVo;
import org.dromara.health.mapper.HealthDeviceMapper;
import org.dromara.health.mapper.HealthDeviceTypeMapper;
import org.dromara.health.mapper.HealthElderMapper;
import org.dromara.health.service.IHealthDeviceService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备管理Service实现
 *
 * @author health
 */
@RequiredArgsConstructor
@Service
public class HealthDeviceServiceImpl implements IHealthDeviceService {

    private final HealthDeviceMapper baseMapper;
    private final HealthDeviceTypeMapper deviceTypeMapper;
    private final HealthElderMapper elderMapper;

    /**
     * 分页查询设备列表
     */
    @Override
    public TableDataInfo<HealthDeviceVo> selectPageList(HealthDeviceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HealthDevice> lqw = buildQueryWrapper(bo);
        Page<HealthDeviceVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        // 填充关联信息
        fillRelationInfo(page.getRecords());
        return TableDataInfo.build(page);
    }

    /**
     * 查询设备列表
     */
    @Override
    public List<HealthDeviceVo> selectList(HealthDeviceBo bo) {
        LambdaQueryWrapper<HealthDevice> lqw = buildQueryWrapper(bo);
        List<HealthDeviceVo> list = baseMapper.selectVoList(lqw);
        fillRelationInfo(list);
        return list;
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<HealthDevice> buildQueryWrapper(HealthDeviceBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HealthDevice> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getDeviceCode()), HealthDevice::getDeviceCode, bo.getDeviceCode());
        lqw.like(StringUtils.isNotBlank(bo.getDeviceName()), HealthDevice::getDeviceName, bo.getDeviceName());
        lqw.eq(ObjectUtil.isNotNull(bo.getDeviceTypeId()), HealthDevice::getDeviceTypeId, bo.getDeviceTypeId());
        lqw.like(StringUtils.isNotBlank(bo.getDeviceModel()), HealthDevice::getDeviceModel, bo.getDeviceModel());
        lqw.like(StringUtils.isNotBlank(bo.getManufacturer()), HealthDevice::getManufacturer, bo.getManufacturer());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HealthDevice::getStatus, bo.getStatus());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            HealthDevice::getCreateTime, params.get("beginTime"), params.get("endTime"));
        lqw.orderByDesc(HealthDevice::getCreateTime);
        return lqw;
    }

    /**
     * 填充关联信息
     */
    private void fillRelationInfo(List<HealthDeviceVo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (HealthDeviceVo vo : list) {
            // 填充设备类型名称
            if (ObjectUtil.isNotNull(vo.getDeviceTypeId())) {
                HealthDeviceType deviceType = deviceTypeMapper.selectById(vo.getDeviceTypeId());
                if (ObjectUtil.isNotNull(deviceType)) {
                    vo.setDeviceTypeName(deviceType.getTypeName());
                }
            }
            // 填充绑定用户名称
            if (ObjectUtil.isNotNull(vo.getBindUserId())) {
                HealthElder elder = elderMapper.selectById(vo.getBindUserId());
                if (ObjectUtil.isNotNull(elder)) {
                    vo.setBindUserName(elder.getName());
                }
            }
        }
    }

    /**
     * 根据ID查询设备
     */
    @Override
    public HealthDeviceVo selectById(Long id) {
        HealthDeviceVo vo = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(vo)) {
            fillRelationInfo(List.of(vo));
        }
        return vo;
    }

    /**
     * 校验设备编码是否唯一
     */
    @Override
    public boolean checkDeviceCodeUnique(HealthDeviceBo bo) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<HealthDevice>()
            .eq(HealthDevice::getDeviceCode, bo.getDeviceCode())
            .ne(ObjectUtil.isNotNull(bo.getId()), HealthDevice::getId, bo.getId()));
        return !exist;
    }

    /**
     * 新增设备
     */
    @Override
    public int insert(HealthDeviceBo bo) {
        HealthDevice entity = MapstructUtils.convert(bo, HealthDevice.class);
        // 如果没有设备编码，自动生成
        if (StringUtils.isBlank(entity.getDeviceCode())) {
            entity.setDeviceCode(generateDeviceCode());
        }
        return baseMapper.insert(entity);
    }

    /**
     * 修改设备
     */
    @Override
    public int update(HealthDeviceBo bo) {
        HealthDevice entity = MapstructUtils.convert(bo, HealthDevice.class);
        return baseMapper.updateById(entity);
    }

    /**
     * 删除设备
     */
    @Override
    public int deleteById(Long id) {
        return baseMapper.deleteById(id);
    }

    /**
     * 批量删除设备
     */
    @Override
    public int deleteByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }

    /**
     * 绑定设备
     */
    @Override
    public int bindDevice(Long id, Long userId) {
        HealthDevice device = new HealthDevice();
        device.setId(id);
        device.setBindUserId(userId);
        device.setBindTime(new Date());
        return baseMapper.updateById(device);
    }

    /**
     * 解绑设备
     */
    @Override
    public int unbindDevice(Long id) {
        HealthDevice device = baseMapper.selectById(id);
        if (ObjectUtil.isNull(device)) {
            return 0;
        }
        device.setBindUserId(null);
        // 保留绑定时间记录
        return baseMapper.updateById(device);
    }

    /**
     * 生成设备编码
     * 格式: DEV + 年月日 + 4位序号
     */
    @Override
    public String generateDeviceCode() {
        String prefix = "DEV" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 查询当天最大编码
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<HealthDevice>()
            .likeRight(HealthDevice::getDeviceCode, prefix));
        return prefix + String.format("%04d", count + 1);
    }

}
