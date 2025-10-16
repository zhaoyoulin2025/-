//package com.ruoyi.system.controller;
//
//import java.util.List;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import com.ruoyi.common.annotation.Log;
//import com.ruoyi.common.core.controller.BaseController;
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.common.enums.BusinessType;
//import com.ruoyi.system.domain.Measure;
//import com.ruoyi.system.service.IMeasureService;
//import com.ruoyi.common.utils.poi.ExcelUtil;
//import com.ruoyi.common.core.page.TableDataInfo;
//
///**
// * 测量信息Controller
// *
// * @author ruoyi
// * @date 2025-06-18
// */
//@RestController
//@RequestMapping("/system/measure")
//public class MeasureController extends BaseController
//{
//    @Autowired
//    private IMeasureService measureService;
//
//    /**
//     * 查询测量信息列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:measure:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(Measure measure)
//    {
//        startPage();
//        List<Measure> list = measureService.selectMeasureList(measure);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出测量信息列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:measure:export')")
//    @Log(title = "测量信息", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, Measure measure)
//    {
//        List<Measure> list = measureService.selectMeasureList(measure);
//        ExcelUtil<Measure> util = new ExcelUtil<Measure>(Measure.class);
//        util.exportExcel(response, list, "测量信息数据");
//    }
//
//    /**
//     * 获取测量信息详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:measure:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return success(measureService.selectMeasureById(id));
//    }
//
//    /**
//     * 新增测量信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:measure:add')")
//    @Log(title = "测量信息", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody Measure measure)
//    {
//        return toAjax(measureService.insertMeasure(measure));
//    }
//
//    /**
//     * 修改测量信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:measure:edit')")
//    @Log(title = "测量信息", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody Measure measure)
//    {
//        return toAjax(measureService.updateMeasure(measure));
//    }
//
//    /**
//     * 删除测量信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:measure:remove')")
//    @Log(title = "测量信息", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(measureService.deleteMeasureByIds(ids));
//    }
//}
