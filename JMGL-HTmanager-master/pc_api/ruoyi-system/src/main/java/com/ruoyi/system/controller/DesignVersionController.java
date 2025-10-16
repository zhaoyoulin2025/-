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
import com.ruoyi.system.domain.DesignVersion;
import com.ruoyi.system.service.IDesignVersionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设计版本Controller
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@RestController
@RequestMapping("/system/designVersion")
public class DesignVersionController extends BaseController
{
    @Autowired
    private IDesignVersionService designVersionService;

    /**
     * 查询设计版本列表
     */
    @PreAuthorize("@ss.hasPermi('system:designVersion:list')")
    @GetMapping("/list")
    public TableDataInfo list(DesignVersion designVersion)
    {
        startPage();
        List<DesignVersion> list = designVersionService.selectDesignVersionList(designVersion);
        return getDataTable(list);
    }

    /**
     * 导出设计版本列表
     */
    @PreAuthorize("@ss.hasPermi('system:designVersion:export')")
    @Log(title = "设计版本", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DesignVersion designVersion)
    {
        List<DesignVersion> list = designVersionService.selectDesignVersionList(designVersion);
        ExcelUtil<DesignVersion> util = new ExcelUtil<DesignVersion>(DesignVersion.class);
        util.exportExcel(response, list, "设计版本数据");
    }

    /**
     * 获取设计版本详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:designVersion:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(designVersionService.selectDesignVersionById(id));
    }

    /**
     * 新增设计版本
     */
    @PreAuthorize("@ss.hasPermi('system:designVersion:add')")
    @Log(title = "设计版本", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DesignVersion designVersion)
    {
        return toAjax(designVersionService.insertDesignVersion(designVersion));
    }

    /**
     * 修改设计版本
     */
    @PreAuthorize("@ss.hasPermi('system:designVersion:edit')")
    @Log(title = "设计版本", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DesignVersion designVersion)
    {
        return toAjax(designVersionService.updateDesignVersion(designVersion));
    }

    /**
     * 删除设计版本
     */
    @PreAuthorize("@ss.hasPermi('system:designVersion:remove')")
    @Log(title = "设计版本", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(designVersionService.deleteDesignVersionByIds(ids));
    }
}
