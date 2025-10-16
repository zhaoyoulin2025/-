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
import com.ruoyi.system.domain.Districtfollower;
import com.ruoyi.system.service.IDistrictfollowerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商圈跟进人员Controller
 * 
 * @author ruoyi
 * @date 2025-07-11
 */
@RestController
@RequestMapping("/system/districtfollower")
public class DistrictfollowerController extends BaseController
{
    @Autowired
    private IDistrictfollowerService districtfollowerService;

    /**
     * 查询商圈跟进人员列表
     */
    @PreAuthorize("@ss.hasPermi('system:districtfollower:list')")
    @GetMapping("/list")
    public TableDataInfo list(Districtfollower districtfollower)
    {
        startPage();
        List<Districtfollower> list = districtfollowerService.selectDistrictfollowerList(districtfollower);
        return getDataTable(list);
    }

    /**
     * 导出商圈跟进人员列表
     */
    @PreAuthorize("@ss.hasPermi('system:districtfollower:export')")
    @Log(title = "商圈跟进人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Districtfollower districtfollower)
    {
        List<Districtfollower> list = districtfollowerService.selectDistrictfollowerList(districtfollower);
        ExcelUtil<Districtfollower> util = new ExcelUtil<Districtfollower>(Districtfollower.class);
        util.exportExcel(response, list, "商圈跟进人员数据");
    }

    /**
     * 获取商圈跟进人员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:districtfollower:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(districtfollowerService.selectDistrictfollowerById(id));
    }

    /**
     * 新增商圈跟进人员
     */
    @PreAuthorize("@ss.hasPermi('system:districtfollower:add')")
    @Log(title = "商圈跟进人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Districtfollower districtfollower)
    {
        return toAjax(districtfollowerService.insertDistrictfollower(districtfollower));
    }

    /**
     * 修改商圈跟进人员
     */
    @PreAuthorize("@ss.hasPermi('system:districtfollower:edit')")
    @Log(title = "商圈跟进人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Districtfollower districtfollower)
    {
        return toAjax(districtfollowerService.updateDistrictfollower(districtfollower));
    }

    /**
     * 删除商圈跟进人员
     */
    @PreAuthorize("@ss.hasPermi('system:districtfollower:remove')")
    @Log(title = "商圈跟进人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(districtfollowerService.deleteDistrictfollowerByIds(ids));
    }
}
