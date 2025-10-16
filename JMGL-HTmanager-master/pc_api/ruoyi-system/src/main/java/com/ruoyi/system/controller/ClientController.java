//package com.ruoyi.system.controller;
//
//import java.util.List;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.ruoyi.common.constant.Constants;
//import com.ruoyi.common.core.domain.model.LoginUser;
//import com.ruoyi.common.utils.StringUtils;
//import com.ruoyi.system.domain.vo.ClientAddFollwer;
//
//import io.jsonwebtoken.Claims;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
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
//import com.ruoyi.system.domain.Client;
//import com.ruoyi.system.service.IClientService;
//import com.ruoyi.common.utils.poi.ExcelUtil;
//import com.ruoyi.common.core.page.TableDataInfo;
//
///**
// * 客户信息Controller
// *
// * @author ruoyi
// * @date 2025-06-15
// */
//@RestController
//@RequestMapping("/system/client")
//public class ClientController extends BaseController
//{
//    @Autowired
//    private IClientService clientService;
//
//
//
//
//
//    /**
//     * 查询客户信息列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:client:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(Client client, HttpServletRequest request)
//    {
//        startPage();
//        request.getCookies();
//        List<Client> list = clientService.selectClientList(client);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出客户信息列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:client:export')")
//    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, Client client)
//    {
//        List<Client> list = clientService.selectClientList(client);
//        ExcelUtil<Client> util = new ExcelUtil<Client>(Client.class);
//        util.exportExcel(response, list, "客户信息数据");
//    }
//
//    /**
//     * 获取客户信息详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:client:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return success(clientService.selectClientById(id));
//    }
//
//    /**
//     * 新增客户信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:client:add')")
//    @Log(title = "客户信息", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody Client client)
//    {
//        return toAjax(clientService.insertClient(client));
//    }
//
//    /**
//     * 修改客户信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:client:edit')")
//    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody Client client)
//    {
//        return toAjax(clientService.updateClient(client));
//    }
//
//
//    @PreAuthorize("@ss.hasPermi('system:client:edit')")
//    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
//    @PostMapping("/addFollowers")
//    public AjaxResult addFoller(@RequestBody ClientAddFollwer clientAddFollwer)
//    {
//        return toAjax(clientService.addFoller(clientAddFollwer));
//    }
//
//    /**
//     * 删除客户信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:client:remove')")
//    @Log(title = "客户信息", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(clientService.deleteClientByIds(ids));
//    }
//}
