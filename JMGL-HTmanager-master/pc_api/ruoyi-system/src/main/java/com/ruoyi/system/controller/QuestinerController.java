package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ruoyi.system.domain.Questiner;
import com.ruoyi.system.service.IQuestinerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 提问者Controller
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@RestController
@RequestMapping("/system/questiner")
public class QuestinerController extends BaseController
{
    @Autowired
    private IQuestinerService questinerService;

    /**
     * 查询提问者列表
     */
    @PreAuthorize("@ss.hasPermi('system:questiner:list')")
    @GetMapping("/list")
    public TableDataInfo list(Questiner questiner)
    {
        startPage();
        List<Questiner> list = questinerService.selectQuestinerList(questiner);
        return getDataTable(list);
    }

    /**
     * 导出提问者列表
     */
    @PreAuthorize("@ss.hasPermi('system:questiner:export')")
    @Log(title = "提问者", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Questiner questiner)
    {
        List<Questiner> list = questinerService.selectQuestinerList(questiner);
        ExcelUtil<Questiner> util = new ExcelUtil<Questiner>(Questiner.class);
        util.exportExcel(response, list, "提问者数据");
    }

    /**
     * 获取提问者详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:questiner:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(questinerService.selectQuestinerById(id));
    }

    /**
     * 新增提问者
     */
    @PreAuthorize("@ss.hasPermi('system:questiner:add')")
    @Log(title = "提问者", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Questiner questiner)
    {
        return toAjax(questinerService.insertQuestiner(questiner));
    }

    /**
     * 修改提问者
     */
    @PreAuthorize("@ss.hasPermi('system:questiner:edit')")
    @Log(title = "提问者", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Questiner questiner)
    {
        return toAjax(questinerService.updateQuestiner(questiner));
    }

    /**
     * 删除提问者
     */
    @PreAuthorize("@ss.hasPermi('system:questiner:remove')")
    @Log(title = "提问者", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(questinerService.deleteQuestinerByIds(ids));
    }
}
