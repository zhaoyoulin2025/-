package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.FranchiseInvestmentForm;
import com.ruoyi.system.service.IFranchiseInvestmentFormService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 加盟投资意向Controller
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/system/form")
public class FranchiseInvestmentFormController extends BaseController
{
    @Autowired
    private IFranchiseInvestmentFormService franchiseInvestmentFormService;

    /**
     * 查询加盟投资意向列表
     */
    @PreAuthorize("@ss.hasPermi('system:form:list')")
    @GetMapping("/list")
    public TableDataInfo list(FranchiseInvestmentForm franchiseInvestmentForm)
    {
        startPage();
        List<FranchiseInvestmentForm> list = franchiseInvestmentFormService.selectFranchiseInvestmentFormList(franchiseInvestmentForm);
        return getDataTable(list);
    }

    /**
     * 导出加盟投资意向列表
     */
    @PreAuthorize("@ss.hasPermi('system:form:export')")
    @Log(title = "加盟投资意向", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FranchiseInvestmentForm franchiseInvestmentForm)
    {
        List<FranchiseInvestmentForm> list = franchiseInvestmentFormService.selectFranchiseInvestmentFormList(franchiseInvestmentForm);
        ExcelUtil<FranchiseInvestmentForm> util = new ExcelUtil<FranchiseInvestmentForm>(FranchiseInvestmentForm.class);
        util.exportExcel(response, list, "加盟投资意向数据");
    }

    /**
     * 获取加盟投资意向详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:form:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(franchiseInvestmentFormService.selectFranchiseInvestmentFormById(id));
    }

    @PreAuthorize("@ss.hasPermi('system:form:query')")
    @GetMapping(value = "/client")
    public AjaxResult getInfobyClient(@RequestParam("clientid") Integer clientid)
    {
        return success(franchiseInvestmentFormService.selectFranchiseInvestmentFormByClientId(clientid));
    }

    /**
     * 新增加盟投资意向
     */
    @PreAuthorize("@ss.hasPermi('system:form:add')")
    @Log(title = "加盟投资意向", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FranchiseInvestmentForm franchiseInvestmentForm)
    {
        return toAjax(franchiseInvestmentFormService.insertFranchiseInvestmentForm(franchiseInvestmentForm));
    }

    /**
     * 修改加盟投资意向
     */
    @PreAuthorize("@ss.hasPermi('system:form:edit')")
    @Log(title = "加盟投资意向", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FranchiseInvestmentForm franchiseInvestmentForm)
    {
        return toAjax(franchiseInvestmentFormService.updateFranchiseInvestmentForm(franchiseInvestmentForm));
    }

    /**
     * 删除加盟投资意向
     */
    @PreAuthorize("@ss.hasPermi('system:form:remove')")
    @Log(title = "加盟投资意向", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(franchiseInvestmentFormService.deleteFranchiseInvestmentFormByIds(ids));
    }
}
