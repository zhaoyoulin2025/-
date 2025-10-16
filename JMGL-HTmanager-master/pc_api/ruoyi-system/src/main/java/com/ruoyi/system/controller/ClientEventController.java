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
import com.ruoyi.system.domain.ClientEvent;
import com.ruoyi.system.service.IClientEventService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客户事件Controller
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@RestController
@RequestMapping("/system/clientEvent")
public class ClientEventController extends BaseController
{
    @Autowired
    private IClientEventService clientEventService;

    /**
     * 查询客户事件列表
     */
    @PreAuthorize("@ss.hasPermi('system:clientEvent:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClientEvent clientEvent)
    {
        startPage();
        List<ClientEvent> list = clientEventService.selectClientEventList(clientEvent);
        return getDataTable(list);
    }

    /**
     * 导出客户事件列表
     */
    @PreAuthorize("@ss.hasPermi('system:clientEvent:export')")
    @Log(title = "客户事件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ClientEvent clientEvent)
    {
        List<ClientEvent> list = clientEventService.selectClientEventList(clientEvent);
        ExcelUtil<ClientEvent> util = new ExcelUtil<ClientEvent>(ClientEvent.class);
        util.exportExcel(response, list, "客户事件数据");
    }

    /**
     * 获取客户事件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:clientEvent:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(clientEventService.selectClientEventById(id));
    }

    /**
     * 新增客户事件
     */
    @PreAuthorize("@ss.hasPermi('system:clientEvent:add')")
    @Log(title = "客户事件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClientEvent clientEvent)
    {
        return toAjax(clientEventService.insertClientEvent(clientEvent));
    }

    /**
     * 修改客户事件
     */
    @PreAuthorize("@ss.hasPermi('system:clientEvent:edit')")
    @Log(title = "客户事件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClientEvent clientEvent)
    {
        return toAjax(clientEventService.updateClientEvent(clientEvent));
    }

    /**
     * 删除客户事件
     */
    @PreAuthorize("@ss.hasPermi('system:clientEvent:remove')")
    @Log(title = "客户事件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(clientEventService.deleteClientEventByIds(ids));
    }
}
