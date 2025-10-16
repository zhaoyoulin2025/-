package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.system.domain.vo.ClientAddFollwer;
import com.ruoyi.system.service.IShopService;
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
import com.ruoyi.system.domain.Shop;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 标准作业流程/店铺信息Controller
 *
 * @author ruoyi
 * @date 2025-06-15
 */
@RestController
@RequestMapping("/system/shop")
public class ShopController extends BaseController
{
    @Autowired
    private IShopService shopService;

    /**
     * 查询标准作业流程/店铺信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:shop:list')")
    @GetMapping("/list")
    public TableDataInfo list(Shop shop)
    {
        startPage();
        List<Shop> list = shopService.selectShopList(shop);
        return getDataTable(list);
    }

    /**
     * 导出标准作业流程/店铺信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:shop:export')")
    @Log(title = "标准作业流程/店铺信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Shop shop)
    {
        List<Shop> list = shopService.selectShopList(shop);
        ExcelUtil<Shop> util = new ExcelUtil<Shop>(Shop.class);
        util.exportExcel(response, list, "标准作业流程/店铺信息数据");
    }

    /**
     * 获取标准作业流程/店铺信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:shop:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(shopService.selectShopById(id));
    }

    /**
     * 新增标准作业流程/店铺信息
     */
    @PreAuthorize("@ss.hasPermi('system:shop:add')")
    @Log(title = "标准作业流程/店铺信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Shop shop)
    {
        return toAjax(shopService.insertShop(shop));
    }

    /**
     * 修改标准作业流程/店铺信息
     */
    @PreAuthorize("@ss.hasPermi('system:shop:edit')")
    @Log(title = "标准作业流程/店铺信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Shop shop)
    {
        return toAjax(shopService.updateShop(shop));
    }

    /**
     * 删除标准作业流程/店铺信息
     */
    @PreAuthorize("@ss.hasPermi('system:shop:remove')")
    @Log(title = "标准作业流程/店铺信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shopService.deleteShopByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('system:shop:edit')")
    @Log(title = "标准作业流程/店铺信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/removeFollower/{ids}")
    public AjaxResult removeFollower(@PathVariable Long[] ids)
    {
        return toAjax(shopService.removeFollower(ids));
    }

    @PreAuthorize("@ss.hasPermi('system:shop:edit')")
    @Log(title = "标准作业流程/审核", businessType = BusinessType.DELETE)
    @DeleteMapping("/audit/{ids}")
    public AjaxResult audit(@PathVariable Long[] ids)
    {
        //驳回审核
        return toAjax(shopService.audit(ids));
    }

    @PreAuthorize("@ss.hasPermi('system:shop:edit')")
    @Log(title = "标准作业流程/审核", businessType = BusinessType.DELETE)
    @DeleteMapping("/auditAcc/{ids}")
    public AjaxResult auditAcc(@PathVariable Long[] ids)
    {
        //审核通过
        return toAjax(shopService.auditAcc(ids));
    }

    @PreAuthorize("@ss.hasPermi('system:shop:edit')")
    @Log(title = "标准作业流程/审核", businessType = BusinessType.DELETE)
    @DeleteMapping("/toDesgin/{ids}")
    public AjaxResult toDesgin(@PathVariable Long[] ids)
    {
        //审核通过
        int desgin = shopService.toDesgin(ids);
        if(desgin!=0){
            return new AjaxResult(HttpStatus.SUCCESS, "成功", null);
        }
        return new AjaxResult(HttpStatus.ERROR, "该店铺已经在设计中", null);
    }







}
