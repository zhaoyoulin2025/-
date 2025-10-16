package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.DrawingEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DrawingsMapper;
import com.ruoyi.system.domain.Drawings;
import com.ruoyi.system.service.IDrawingsService;
import org.springframework.util.ObjectUtils;

/**
 * 图纸信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-18
 */
@Service
public class DrawingsServiceImpl implements IDrawingsService
{
    @Autowired
    private DrawingsMapper drawingsMapper;

    /**
     * 查询图纸信息
     *
     * @param id 图纸信息主键
     * @return 图纸信息
     */
    @Override
    public Drawings selectDrawingsById(Long id)
    {
        return drawingsMapper.selectDrawingsById(id);
    }

    /**
     * 查询图纸信息列表
     *
     * @param drawings 图纸信息
     * @return 图纸信息
     */
    @Override
    public List<Drawings> selectDrawingsList(Drawings drawings)
    {
        drawings.setIsActive(1);
        return drawingsMapper.selectDrawingsList(drawings);
    }


    @Override
    public List<Drawings> selectDrawingsVersionList(Drawings drawings)
    {
        drawings.setIsActive(1);
        return drawingsMapper.selectDrawingsList(drawings);
    }

    /**
     * 新增图纸信息
     *
     * @param drawings 图纸信息
     * @return 结果
     */
    @Override
    public int insertDrawings(Drawings drawings)
    {
        drawings.setCreateTime(DateUtils.getNowDate());
        drawings.setIsActive(1);
//        drawings = setDrawName(drawings);
        return drawingsMapper.insertDrawings(drawings);
    }


    private Drawings setDrawName(Drawings drawings){
        Long drawingType = drawings.getDrawingType();
        DrawingEnum drawName = DrawingEnum.getDrawName(drawingType);
        if(drawingType>4){
            drawings.setDrawingName(drawName.getDrawNm()+"01");
        }else{
            drawings.setDrawingName(drawName.getDrawNm()+drawings.getDrawingNo());
        }
        return drawings;
    }

    /**
     * 修改图纸信息
     *
     * @param drawings 图纸信息
     * @return 结果
     */
    @Override
    public int updateDrawings(Drawings drawings)
    {
        drawings = setDrawName(drawings);
        //做版本数据，先查，然后在进行版本保留
        Drawings drawings1 = drawingsMapper.selectDrawingsById(drawings.getId());
        if(!ObjectUtils.isEmpty(drawings1)
                &&!StringUtils.isEmpty(drawings1.getFileUrl())
                &&!StringUtils.isEmpty(drawings.getFileUrl())
                &&drawings1.getFileUrl().equals(drawings.getFileUrl())){
                // 版本数据
                drawings1.setIsActive(0);
                drawingsMapper.updateDrawings(drawings1);
                drawings.setIsActive(1);
                drawingsMapper.insertDrawings(drawings);
        }
        return drawingsMapper.updateDrawings(drawings);
    }

    /**
     * 批量删除图纸信息
     *
     * @param ids 需要删除的图纸信息主键
     * @return 结果
     */
    @Override
    public int deleteDrawingsByIds(Long[] ids)
    {
        return drawingsMapper.deleteDrawingsByIds(ids);
    }

    /**
     * 删除图纸信息信息
     *
     * @param id 图纸信息主键
     * @return 结果
     */
    @Override
    public int deleteDrawingsById(Long id)
    {
        return drawingsMapper.deleteDrawingsById(id);
    }

    @Override
    public List<Drawings> selectDrawings(Long id) {
        return drawingsMapper.selectDrawings(id);
    }
}
