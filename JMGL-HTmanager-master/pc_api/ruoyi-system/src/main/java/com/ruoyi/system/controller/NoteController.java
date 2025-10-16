package com.ruoyi.system.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.vo.BatchAuditNoteDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Note;
import com.ruoyi.system.service.INoteService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商圈跟进笔记（含打卡）Controller
 *
 * @author ruoyi
 * @date 2025-07-11
 */
@RestController
@RequestMapping("/system/note")
public class NoteController extends BaseController
{
    @Autowired
    private INoteService noteService;

    /**
     * 查询商圈跟进笔记（含打卡）列表
     */
    @PreAuthorize("@ss.hasPermi('system:note:list')")
    @GetMapping("/list")
    public TableDataInfo list(Note note)
    {

        SysUser currentUser = getLoginUser().getUser();
        if (!currentUser.isAdmin()){
            note.setFollowerId(getUserId());
        }
        startPage();
        List<Note> list = noteService.selectNoteList(note);
        return getDataTable(list);
    }

    /**
     * 导出商圈跟进笔记（含打卡）列表
     */
    @PreAuthorize("@ss.hasPermi('system:note:export')")
    @Log(title = "商圈跟进笔记（含打卡）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Note note)
    {
        List<Note> list = noteService.selectNoteList(note);
        ExcelUtil<Note> util = new ExcelUtil<Note>(Note.class);
        util.exportExcel(response, list, "商圈跟进笔记（含打卡）数据");
    }

    /**
     * 获取商圈跟进笔记（含打卡）详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:note:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(noteService.selectNoteById(id));
    }

    /**
     * 新增商圈跟进笔记（含打卡）
     */
    @PreAuthorize("@ss.hasPermi('system:note:add')")
    @Log(title = "商圈跟进笔记（含打卡）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Note note)
    {
        List<String> imageUrls = note.getImageUrls();
        if (CollectionUtils.isNotEmpty(imageUrls)) {
            note.setImageUrl(String.join(",",imageUrls));
        }

        note.setUpdatedAt(new Date());
        note.setStatusss("0");
        note.setFollowerId(getUserId());
        return toAjax(noteService.insertNote(note));
    }

    /**
     * 修改商圈跟进笔记（含打卡）
     */
    @PreAuthorize("@ss.hasPermi('system:note:edit')")
    @Log(title = "商圈跟进笔记（含打卡）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Note note)
    {
        note.setFollowerId(getUserId());
        List<String> imageUrls = note.getImageUrls();
        if (CollectionUtils.isNotEmpty(imageUrls)) {
            note.setImageUrl(String.join(",",imageUrls));
        }
        return toAjax(noteService.updateNote(note));
    }

    /**
     * 删除商圈跟进笔记（含打卡）
     */
    @PreAuthorize("@ss.hasPermi('system:note:remove')")
    @Log(title = "商圈跟进笔记（含打卡）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(noteService.deleteNoteByIds(ids));
    }


    @PreAuthorize("@ss.hasPermi('system:note:remove')")
    @Log(title = "商圈跟进笔记（含打卡）", businessType = BusinessType.DELETE)
    @PostMapping("/batchAudit") // 接口路径，与前端调用地址一致
    public AjaxResult batchAuditNote(@Validated @RequestBody BatchAuditNoteDTO batchAuditNoteDTO) {
        // 调用Service层批量审核逻辑，返回结果
        return toAjax(noteService.batchAuditNote(batchAuditNoteDTO));
    }
}
