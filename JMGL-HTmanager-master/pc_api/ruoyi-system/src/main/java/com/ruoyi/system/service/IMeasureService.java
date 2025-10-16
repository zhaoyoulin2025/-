package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Measure;

/**
 * 测量信息Service接口
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
public interface IMeasureService 
{
    /**
     * 查询测量信息
     * 
     * @param id 测量信息主键
     * @return 测量信息
     */
    public Measure selectMeasureById(Long id);

    /**
     * 查询测量信息列表
     * 
     * @param measure 测量信息
     * @return 测量信息集合
     */
    public List<Measure> selectMeasureList(Measure measure);

    /**
     * 新增测量信息
     * 
     * @param measure 测量信息
     * @return 结果
     */
    public int insertMeasure(Measure measure);

    /**
     * 修改测量信息
     * 
     * @param measure 测量信息
     * @return 结果
     */
    public int updateMeasure(Measure measure);

    /**
     * 批量删除测量信息
     * 
     * @param ids 需要删除的测量信息主键集合
     * @return 结果
     */
    public int deleteMeasureByIds(Long[] ids);

    /**
     * 删除测量信息信息
     * 
     * @param id 测量信息主键
     * @return 结果
     */
    public int deleteMeasureById(Long id);
}
