package com.ruoyi.system.controller;

import java.util.Date;
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
import com.ruoyi.system.domain.CustomerFollowUp;
import com.ruoyi.system.service.ICustomerFollowUpService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户跟进记录Controller
 * 
 * @author ruoyi
 * @date 2025-06-06
 */
@RestController
@RequestMapping("/system/up")
public class CustomerFollowUpController extends BaseController
{
    @Autowired
    private ICustomerFollowUpService customerFollowUpService;

    /**
     * 查询客户跟进记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:up:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerFollowUp customerFollowUp)
    {
        startPage();
        List<CustomerFollowUp> list = customerFollowUpService.selectCustomerFollowUpList(customerFollowUp);
        return getDataTable(list);
    }

    /**
     * 导出客户跟进记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:up:export')")
    @Log(title = "客户跟进记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerFollowUp customerFollowUp)
    {
        List<CustomerFollowUp> list = customerFollowUpService.selectCustomerFollowUpList(customerFollowUp);
        ExcelUtil<CustomerFollowUp> util = new ExcelUtil<CustomerFollowUp>(CustomerFollowUp.class);
        util.exportExcel(response, list, "客户跟进记录数据");
    }

    /**
     * 获取客户跟进记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:up:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(customerFollowUpService.selectCustomerFollowUpById(id));
    }

    /**
     * 新增客户跟进记录
     */
    @PreAuthorize("@ss.hasPermi('system:up:add')")
    @Log(title = "客户跟进记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerFollowUp customerFollowUp)
    {
        customerFollowUp.setUserId(String.valueOf(getUserId()));
        customerFollowUp.setFollowUpTime(customerFollowUp.getFollowUpTime());
        customerFollowUpService.insertCustomerFollowUp(customerFollowUp);
        return success(customerFollowUp);
    }

    /**
     * 修改客户跟进记录
     */
    @PreAuthorize("@ss.hasPermi('system:up:edit')")
    @Log(title = "客户跟进记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerFollowUp customerFollowUp)
    {
        return toAjax(customerFollowUpService.updateCustomerFollowUp(customerFollowUp));
    }

    /**
     * 删除客户跟进记录
     */
    @PreAuthorize("@ss.hasPermi('system:up:remove')")
    @Log(title = "客户跟进记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(customerFollowUpService.deleteCustomerFollowUpByIds(ids));
    }
}
