package com.ruoyi.system.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.vo.BatchSetDistrictClientDTO;
import com.ruoyi.system.domain.vo.ClientAddFollwer;
import com.ruoyi.system.domain.vo.DistrctStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.ruoyi.system.domain.BusinessDistrict;
import com.ruoyi.system.service.IBusinessDistrictService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商圈信息Controller
 *
 * @author ruoyi
 * @date 2025-07-11
 */
@RestController
@RequestMapping("/system/district")
public class BusinessDistrictController extends BaseController
{
    @Autowired
    private IBusinessDistrictService businessDistrictService;

    /**
     * 查询商圈信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:district:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusinessDistrict businessDistrict)
    {
        startPage();
        List<BusinessDistrict> list = businessDistrictService.selectBusinessDistrictList(businessDistrict);
        return getDataTable(list);
    }

    /**
     * 查询商圈信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:district:list')")
    @GetMapping("/listout")
    public TableDataInfo listout(BusinessDistrict businessDistrict)
    {
        startPage();
        businessDistrict.setStatues("4");
        List<BusinessDistrict> list = businessDistrictService.selectBusinessDistrictList(businessDistrict);
        return getDataTable(list);
    }


    /**
     * 导出商圈信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:district:export')")
    @Log(title = "商圈信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BusinessDistrict businessDistrict)
    {
        List<BusinessDistrict> list = businessDistrictService.selectBusinessDistrictList(businessDistrict);
        ExcelUtil<BusinessDistrict> util = new ExcelUtil<BusinessDistrict>(BusinessDistrict.class);
        util.exportExcel(response, list, "商圈信息数据");
    }

    /**
     * 获取商圈信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:district:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(businessDistrictService.selectBusinessDistrictById(id));
    }

    /**
     * 新增商圈信息
     */
    @PreAuthorize("@ss.hasPermi('system:district:add')")
    @Log(title = "商圈信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusinessDistrict businessDistrict)
    {

        businessDistrict.setCreatorId(getUserId());
        businessDistrict.setCreatedAt(new Date());
        businessDistrict.setStatues("0");
        return toAjax(businessDistrictService.insertBusinessDistrict(businessDistrict));
    }

    /**
     * 修改商圈信息
     */
    @PreAuthorize("@ss.hasPermi('system:district:edit')")
    @Log(title = "商圈信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusinessDistrict businessDistrict)
    {
        return toAjax(businessDistrictService.updateBusinessDistrict(businessDistrict));
    }

    /**
     * 删除商圈信息
     */
    @PreAuthorize("@ss.hasPermi('system:district:remove')")
    @Log(title = "商圈信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(businessDistrictService.deleteBusinessDistrictByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('system:district:shop:attract:edit')")
    @Log(title = "商圈信息", businessType = BusinessType.UPDATE)
    @PostMapping("/addFollowers")
    public AjaxResult addFoller(@RequestBody ClientAddFollwer clientAddFollwer)
    {
        return toAjax(businessDistrictService.addFoller(clientAddFollwer));
    }

    @PreAuthorize("@ss.hasPermi('system:district:edit')")
    @Log(title = "标准作业流程/审核", businessType = BusinessType.DELETE)
    @PostMapping("/audit")
    public AjaxResult auditAcc(@RequestBody DistrctStatus distrctStatus)
    {
        //审核通过
        return toAjax(businessDistrictService.audit(distrctStatus));
    }


    @PreAuthorize("@ss.hasPermi('system:district:edit')")
    @Log(title = "标准作业流程/审核", businessType = BusinessType.DELETE)
    @PostMapping("/batchSetClient")
    public AjaxResult batchSetClient(@Validated @RequestBody BatchSetDistrictClientDTO batchSetDistrictClientDTO) {
        // 调用业务层处理逻辑（例如：循环更新选中的商圈的customerId字段）

        return toAjax(businessDistrictService.batchSetClient(batchSetDistrictClientDTO));
    }
}
