package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Note;

/**
 * 商圈跟进笔记（含打卡）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-11
 */
public interface NoteMapper 
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
     * 删除商圈跟进笔记（含打卡）
     * 
     * @param id 商圈跟进笔记（含打卡）主键
     * @return 结果
     */
    public int deleteNoteById(Long id);

    /**
     * 批量删除商圈跟进笔记（含打卡）
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNoteByIds(Long[] ids);
}
