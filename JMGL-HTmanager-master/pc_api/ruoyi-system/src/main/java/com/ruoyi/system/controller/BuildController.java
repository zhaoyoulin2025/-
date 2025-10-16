package com.ruoyi.system.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.Shop;
import com.ruoyi.system.domain.vo.SuperviserAddMeasure;
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
import com.ruoyi.system.domain.Build;
import com.ruoyi.system.service.IBuildService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 建设管理Controller
 *
 * @author ruoyi
 * @date 2025-06-25
 */
@RestController
@RequestMapping("/system/build")
public class BuildController extends BaseController
{
    @Autowired
    private IBuildService buildService;

    /**
     * 查询建设管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:build:list')")
    @GetMapping("/list")
    public TableDataInfo list(Build build)
    {
        startPage();
        List<Build> list = buildService.selectBuildList(build);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:build:list')")
    @GetMapping("/shoplist")
    public TableDataInfo shoplist(Shop shop)
    {
        startPage();
        List<Shop> list = buildService.selectShopBuildList(shop);
        return getDataTable(list);
    }

    /**
     * 导出建设管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:build:export')")
    @Log(title = "建设管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Build build)
    {
        List<Build> list = buildService.selectBuildList(build);
        ExcelUtil<Build> util = new ExcelUtil<Build>(Build.class);
        util.exportExcel(response, list, "建设管理数据");
    }

    /**
     * 获取建设管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:build:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(buildService.selectBuildById(id));
    }

    /**
     * 新增建设管理
     */
    @PreAuthorize("@ss.hasPermi('system:build:add')")
    @Log(title = "建设管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Build build)
    {
        return toAjax(buildService.insertBuild(build));
    }


    @PreAuthorize("@ss.hasPermi('system:build:add')")
    @Log(title = "批量添加监理", businessType = BusinessType.INSERT)
    @PostMapping("/addSuperviser")
    public AjaxResult addSuperviser(@RequestBody SuperviserAddMeasure superviserAddMeasure)
    {

        return toAjax(buildService.addSuperviser(superviserAddMeasure));
    }



    @PreAuthorize("@ss.hasPermi('system:build:add')")
    @Log(title = "批量添加监理", businessType = BusinessType.INSERT)
    @PostMapping("/addFollower")
    public AjaxResult addFollower(@RequestBody SuperviserAddMeasure superviserAddMeasure)
    {

        return toAjax(buildService.addFollower(superviserAddMeasure));
    }

    /**
     * 修改建设管理
     */
    @PreAuthorize("@ss.hasPermi('system:build:edit')")
    @Log(title = "建设管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Build build)
    {
        return toAjax(buildService.updateBuild(build));
    }

    /**
     * 删除建设管理
     */
    @PreAuthorize("@ss.hasPermi('system:build:remove')")
    @Log(title = "建设管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(buildService.deleteBuildByIds(ids));
    }


    @PreAuthorize("@ss.hasPermi('system:build:remove')")
    @Log(title = "建设管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/toBuild/{ids}")
    public AjaxResult toBuild(@PathVariable Long[] ids)
    {
        return toAjax(buildService.toBuild(ids));
    }
}
