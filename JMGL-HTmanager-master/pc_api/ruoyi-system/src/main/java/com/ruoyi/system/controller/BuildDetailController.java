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
import com.ruoyi.system.domain.BuildDetail;
import com.ruoyi.system.service.IBuildDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 建设详情Controller
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@RestController
@RequestMapping("/system/buildDetail")
public class BuildDetailController extends BaseController
{
    @Autowired
    private IBuildDetailService buildDetailService;

    /**
     * 查询建设详情列表
     */
    @PreAuthorize("@ss.hasPermi('system:buildDetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(BuildDetail buildDetail)
    {
        startPage();
        List<BuildDetail> list = buildDetailService.selectBuildDetailList(buildDetail);
        return getDataTable(list);
    }

    /**
     * 导出建设详情列表
     */
    @PreAuthorize("@ss.hasPermi('system:buildDetail:export')")
    @Log(title = "建设详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BuildDetail buildDetail)
    {
        List<BuildDetail> list = buildDetailService.selectBuildDetailList(buildDetail);
        ExcelUtil<BuildDetail> util = new ExcelUtil<BuildDetail>(BuildDetail.class);
        util.exportExcel(response, list, "建设详情数据");
    }

    /**
     * 获取建设详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:buildDetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(buildDetailService.selectBuildDetailById(id));
    }

    /**
     * 新增建设详情
     */
    @PreAuthorize("@ss.hasPermi('system:buildDetail:add')")
    @Log(title = "建设详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BuildDetail buildDetail)
    {
        return toAjax(buildDetailService.insertBuildDetail(buildDetail));
    }

    /**
     * 修改建设详情
     */
    @PreAuthorize("@ss.hasPermi('system:buildDetail:edit')")
    @Log(title = "建设详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BuildDetail buildDetail)
    {
        return toAjax(buildDetailService.updateBuildDetail(buildDetail));
    }

    /**
     * 删除建设详情
     */
    @PreAuthorize("@ss.hasPermi('system:buildDetail:remove')")
    @Log(title = "建设详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(buildDetailService.deleteBuildDetailByIds(ids));
    }
}
