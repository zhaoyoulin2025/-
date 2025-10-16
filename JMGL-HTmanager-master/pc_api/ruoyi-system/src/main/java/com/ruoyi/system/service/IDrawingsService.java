package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Drawings;

/**
 * 图纸信息Service接口
 *
 * @author ruoyi
 * @date 2025-06-18
 */
public interface IDrawingsService
{
    /**
     * 查询图纸信息
     *
     * @param id 图纸信息主键
     * @return 图纸信息
     */
    public Drawings selectDrawingsById(Long id);

    /**
     * 查询图纸信息列表
     *
     * @param drawings 图纸信息
     * @return 图纸信息集合
     */
    public List<Drawings> selectDrawingsList(Drawings drawings);

    public List<Drawings> selectDrawingsVersionList(Drawings drawings);

    /**
     * 新增图纸信息
     *
     * @param drawings 图纸信息
     * @return 结果
     */
    public int insertDrawings(Drawings drawings);

    /**
     * 修改图纸信息
     *
     * @param drawings 图纸信息
     * @return 结果
     */
    public int updateDrawings(Drawings drawings);

    /**
     * 批量删除图纸信息
     *
     * @param ids 需要删除的图纸信息主键集合
     * @return 结果
     */
    public int deleteDrawingsByIds(Long[] ids);

    /**
     * 删除图纸信息信息
     *
     * @param id 图纸信息主键
     * @return 结果
     */
    public int deleteDrawingsById(Long id);

    List<Drawings> selectDrawings(Long id);
}
