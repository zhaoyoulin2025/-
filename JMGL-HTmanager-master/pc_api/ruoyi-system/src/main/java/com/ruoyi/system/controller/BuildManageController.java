package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.service.IIBuildManageService;
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
import com.ruoyi.system.domain.BuildManage;
import com.ruoyi.system.service.IBuildManageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 监理详情Controller
 * 
 * @author ruoyi
 * @date 2025-08-16
 */
@RestController
@RequestMapping("/system/manage")
public class BuildManageController extends BaseController
{
    @Autowired
    private IIBuildManageService buildManageService;

    /**
     * 查询监理详情列表
     */
    @PreAuthorize("@ss.hasPermi('system:manage:list')")
    @GetMapping("/list")
    public TableDataInfo list(BuildManage buildManage)
    {
        startPage();
        List<BuildManage> list = buildManageService.selectBuildManageList(buildManage);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:manage:list')")
    @GetMapping("/listshop")
    public TableDataInfo listshop(BuildManage buildManage)
    {
        startPage();
        List<BuildManage> list = buildManageService.selectBuildManageListByshop(buildManage);
        return getDataTable(list);
    }

    /**
     * 导出监理详情列表
     */
    @PreAuthorize("@ss.hasPermi('system:manage:export')")
    @Log(title = "监理详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BuildManage buildManage)
    {
        List<BuildManage> list = buildManageService.selectBuildManageList(buildManage);
        ExcelUtil<BuildManage> util = new ExcelUtil<BuildManage>(BuildManage.class);
        util.exportExcel(response, list, "监理详情数据");
    }

    /**
     * 获取监理详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:manage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(buildManageService.selectBuildManageById(id));
    }

    /**
     * 新增监理详情
     */
    @PreAuthorize("@ss.hasPermi('system:manage:add')")
    @Log(title = "监理详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BuildManage buildManage)
    {
        return toAjax(buildManageService.insertBuildManage(buildManage));
    }

    /**
     * 修改监理详情
     */
    @PreAuthorize("@ss.hasPermi('system:manage:edit')")
    @Log(title = "监理详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BuildManage buildManage)
    {
        return toAjax(buildManageService.updateBuildManage(buildManage));
    }

    /**
     * 删除监理详情
     */
    @PreAuthorize("@ss.hasPermi('system:manage:remove')")
    @Log(title = "监理详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(buildManageService.deleteBuildManageByIds(ids));
    }
}
