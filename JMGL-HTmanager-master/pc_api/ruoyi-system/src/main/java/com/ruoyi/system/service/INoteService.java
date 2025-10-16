package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Note;
import com.ruoyi.system.domain.vo.BatchAuditNoteDTO;

/**
 * 商圈跟进笔记（含打卡）Service接口
 * 
 * @author ruoyi
 * @date 2025-07-11
 */
public interface INoteService 
{
    /**
     * 查询商圈跟进笔记（含打卡）
     * 
     * @param id 商圈跟进笔记（含打卡）主键
     * @return 商圈跟进笔记（含打卡）
     */
    public Note selectNoteById(Long id);
    public Note selectWXNoteById(Long id);

    /**
     * 查询商圈跟进笔记（含打卡）列表
     * 
     * @param note 商圈跟进笔记（含打卡）
     * @return 商圈跟进笔记（含打卡）集合
     */
    public List<Note> selectNoteList(Note note);

    /**
     * 新增商圈跟进笔记（含打卡）
     * 
     * @param note 商圈跟进笔记（含打卡）
     * @return 结果
     */
    public int insertNote(Note note);

    /**
     * 修改商圈跟进笔记（含打卡）
     * 
     * @param note 商圈跟进笔记（含打卡）
     * @return 结果
     */
    public int updateNote(Note note);

    /**
     * 批量删除商圈跟进笔记（含打卡）
     * 
     * @param ids 需要删除的商圈跟进笔记（含打卡）主键集合
     * @return 结果
     */
    public int deleteNoteByIds(Long[] ids);

    /**
     * 删除商圈跟进笔记（含打卡）信息
     * 
     * @param id 商圈跟进笔记（含打卡）主键
     * @return 结果
     */
    public int deleteNoteById(Long id);

    int batchAuditNote(BatchAuditNoteDTO batchAuditNoteDTO);
}
