package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Task;
import com.ruoyi.system.domain.TbWxBanner;
import com.ruoyi.system.service.TbWxBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/tbWxBanner")
public class TbWxBannerController extends BaseController {

    @Autowired
    private TbWxBannerService tbWxBannerService;

    @GetMapping("/list")
    public TableDataInfo list(TbWxBanner tbWxBanner)
    {
        startPage();
        List<TbWxBanner> list = tbWxBannerService.selectList(tbWxBanner);
        return getDataTable(list);
    }

    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody TbWxBanner tbWxBanner)
    {
        tbWxBannerService.addOrUpdate(tbWxBanner);
        return AjaxResult.success();
    }

    /**
     * 删除任务管理
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        tbWxBannerService.deleteBannerByIds(ids);
        return AjaxResult.success();
    }

    @GetMapping(value = "/detail/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbWxBannerService.selectBannerById(id));
    }


}
