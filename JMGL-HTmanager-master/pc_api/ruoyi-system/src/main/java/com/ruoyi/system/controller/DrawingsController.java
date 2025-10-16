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
import com.ruoyi.system.domain.Drawings;
import com.ruoyi.system.service.IDrawingsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图纸信息Controller
 *
 * @author ruoyi
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/system/drawings")
public class DrawingsController extends BaseController
{
    @Autowired
    private IDrawingsService drawingsService;

    /**
     * 查询图纸信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:drawings:list')")
    @GetMapping("/list")
    public TableDataInfo list(Drawings drawings)
    {
        startPage();
        List<Drawings> list = drawingsService.selectDrawingsList(drawings);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:drawings:list')")
    @GetMapping("/versionlist")
    public TableDataInfo selectDrawingsVersionList(Drawings drawings)
    {
        startPage();
        List<Drawings> list = drawingsService.selectDrawingsVersionList(drawings);
        return getDataTable(list);
    }

    /**
     * 导出图纸信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:drawings:export')")
    @Log(title = "图纸信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Drawings drawings)
    {



        List<Drawings> list = drawingsService.selectDrawingsList(drawings);
        ExcelUtil<Drawings> util = new ExcelUtil<Drawings>(Drawings.class);
        util.exportExcel(response, list, "图纸信息数据");
    }

    /**
     * 获取图纸信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:drawings:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(drawingsService.selectDrawingsById(id));
    }

    /**
     * 新增图纸信息
     */
    @PreAuthorize("@ss.hasPermi('system:drawings:add')")
    @Log(title = "图纸信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Drawings drawings)
    {
        return toAjax(drawingsService.insertDrawings(drawings));
    }

    /**
     * 修改图纸信息
     */
    @PreAuthorize("@ss.hasPermi('system:drawings:edit')")
    @Log(title = "图纸信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Drawings drawings)
    {
        return toAjax(drawingsService.updateDrawings(drawings));
    }

    /**
     * 删除图纸信息
     */
    @PreAuthorize("@ss.hasPermi('system:drawings:remove')")
    @Log(title = "图纸信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(drawingsService.deleteDrawingsByIds(ids));
    }
}
