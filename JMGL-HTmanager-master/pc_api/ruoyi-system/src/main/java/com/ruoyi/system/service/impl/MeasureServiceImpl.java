package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.MeasureMapper;
import com.ruoyi.system.domain.Measure;
import com.ruoyi.system.service.IMeasureService;

/**
 * 测量信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
@Service
public class MeasureServiceImpl implements IMeasureService 
{
    @Autowired
    private MeasureMapper measureMapper;

    /**
     * 查询测量信息
     * 
     * @param id 测量信息主键
     * @return 测量信息
     */
    @Override
    public Measure selectMeasureById(Long id)
    {
        return measureMapper.selectMeasureById(id);
    }

    /**
     * 查询测量信息列表
     * 
     * @param measure 测量信息
     * @return 测量信息
     */
    @Override
    public List<Measure> selectMeasureList(Measure measure)
    {
        return measureMapper.selectMeasureList(measure);
    }

    /**
     * 新增测量信息
     * 
     * @param measure 测量信息
     * @return 结果
     */
    @Override
    public int insertMeasure(Measure measure)
    {
        measure.setCreateTime(DateUtils.getNowDate());
        return measureMapper.insertMeasure(measure);
    }

    /**
     * 修改测量信息
     * 
     * @param measure 测量信息
     * @return 结果
     */
    @Override
    public int updateMeasure(Measure measure)
    {
        return measureMapper.updateMeasure(measure);
    }

    /**
     * 批量删除测量信息
     * 
     * @param ids 需要删除的测量信息主键
     * @return 结果
     */
    @Override
    public int deleteMeasureByIds(Long[] ids)
    {
        return measureMapper.deleteMeasureByIds(ids);
    }

    /**
     * 删除测量信息信息
     * 
     * @param id 测量信息主键
     * @return 结果
     */
    @Override
    public int deleteMeasureById(Long id)
    {
        return measureMapper.deleteMeasureById(id);
    }
}
