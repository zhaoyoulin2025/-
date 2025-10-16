package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.vo.BatchAuditNoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.NoteMapper;
import com.ruoyi.system.domain.Note;
import com.ruoyi.system.service.INoteService;

/**
 * 商圈跟进笔记（含打卡）Service业务层处理
 *
 * @author ruoyi
 * @date 2025-07-11
 */
@Service
public class NoteServiceImpl implements INoteService
{
    @Autowired
    private NoteMapper noteMapper;

    /**
     * 查询商圈跟进笔记（含打卡）
     *
     * @param id 商圈跟进笔记（含打卡）主键
     * @return 商圈跟进笔记（含打卡）
     */
    @Override
    public Note selectNoteById(Long id)
    {
        return noteMapper.selectNoteById(id);
    }

    @Override
    public Note selectWXNoteById(Long id) {
        return noteMapper.selectWXNoteById(id);
    }

    /**
     * 查询商圈跟进笔记（含打卡）列表
     *
     * @param note 商圈跟进笔记（含打卡）
     * @return 商圈跟进笔记（含打卡）
     */
    @Override
    public List<Note> selectNoteList(Note note)
    {
        return noteMapper.selectNoteList(note);
    }

    /**
     * 新增商圈跟进笔记（含打卡）
     *
     * @param note 商圈跟进笔记（含打卡）
     * @return 结果
     */
    @Override
    public int insertNote(Note note)
    {

        return noteMapper.insertNote(note);
    }

    /**
     * 修改商圈跟进笔记（含打卡）
     *
     * @param note 商圈跟进笔记（含打卡）
     * @return 结果
     */
    @Override
    public int updateNote(Note note)
    {
        return noteMapper.updateNote(note);
    }

    /**
     * 批量删除商圈跟进笔记（含打卡）
     *
     * @param ids 需要删除的商圈跟进笔记（含打卡）主键
     * @return 结果
     */
    @Override
    public int deleteNoteByIds(Long[] ids)
    {
        return noteMapper.deleteNoteByIds(ids);
    }

    /**
     * 删除商圈跟进笔记（含打卡）信息
     *
     * @param id 商圈跟进笔记（含打卡）主键
     * @return 结果
     */
    @Override
    public int deleteNoteById(Long id)
    {
        return noteMapper.deleteNoteById(id);
    }

    @Override
    public int batchAuditNote(BatchAuditNoteDTO batchAuditNoteDTO) {

        for (Long noteId : batchAuditNoteDTO.getNoteIds()) {
            Note note = noteMapper.selectNoteById(noteId);
            note.setStatusss(batchAuditNoteDTO.getStatusss()+"");
            noteMapper.updateNote(note);


        }



        return batchAuditNoteDTO.getNoteIds().size();
    }
}
