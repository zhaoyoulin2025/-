package com.ruoyi.web.controller.common;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.Client;
import com.ruoyi.system.domain.vo.ClientAddFollwer;
import com.ruoyi.system.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 客户信息Controller
 *
 * @author ruoyi
 * @date 2025-06-15
 */
@RestController
@RequestMapping("/system/client")
public class ClientController extends BaseController
{
    @Autowired
    private IClientService clientService;


    @Autowired
    private TokenService tokenService;


    /**
     * 查询客户信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:client:shop:attract:list')")
    @GetMapping("/list")
    public TableDataInfo list(Client client, HttpServletRequest request)
    {
        startPage();
        SysUser currentUser = getLoginUser().getUser();
        if (!currentUser.isAdmin()){
            client.setGuideId(getUserId());
        }
//        LoginUser loginUser = tokenService.getLoginUser(request);
//        client.setGuideId(loginUser.getUserId());
        List<Client> list = clientService.selectClientList(client);
        return getDataTable(list);
    }

    /**
     * 导出客户信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:client:shop:attract:list')")
    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Client client)
    {
        List<Client> list = clientService.selectClientList(client);

        for (Client client1 : list) {
            client1.setClientStatusName(getCSName(client1.getClientStatus()));
            client1.setClientLevelName(getlvname(client1.getClientLevel()));


            client1.setIsTransforName(getTransname(String.valueOf(client1.getIsTransfor())));
            client1.setIsmoreName(getIsmore(String.valueOf(client1.getIsmore())));
            client1.setShopResourceName(getResourceName(client1.getShopResource()));
            client1.setSingleName(getSingName(String.valueOf(client1.getSingle())));
        }

        ExcelUtil<Client> util = new ExcelUtil<Client>(Client.class);
        util.exportExcel(response, list, "客户信息数据");
    }

    private String getResourceName(Integer shopResource) {
        return 1==shopResource?"有权限":0==shopResource?"无权限":"未设置";
    }

    private String getIsmore(String valueOf) {
        if("1".equals(valueOf)){
            return "是";
        }else if("2".equals(valueOf)){
            return "否";
        }
        return "";
    }

    private String getSingName(String valueOf) {
        if("1".equals(valueOf)){
            return "独资";
        }else if("2".equals(valueOf)){
            return "合伙";
        }
        return "";

    }

    private String getlvname(String clientLevel) {
       // "1==普通，2=VIP,,默=认=0
        if("1".equals(clientLevel)){
            return "A";
        }else if("2".equals(clientLevel)){
            return "B";
        }else if("3".equals(clientLevel)){
            return "C";
        }else if("4".equals(clientLevel)){
            return "D";
        }
        return "";
    }

    private String getTransname(String clientLevel) {
        // "1==普通，2=VIP,,默=认=0
        if("1".equals(clientLevel)){
            return "是";
        }else if("0".equals(clientLevel)){
            return "否";
        }
        return "未设置";
    }

    private String getCSName(Integer clientStatus) {

//        0: '潜在',
//                2: '已邀约',
//                3: '已面谈',
//                4: '已签约',
//                5:'已流失'
       // 0==待跟进，1=意向中,2==已签约,3==,已放弃,0=,默=认值
        if(clientStatus ==0){
            return "潜在";
        }else if(clientStatus ==1){
            return "意向中";
        }else if(clientStatus ==2){
            return "已签约";
        }else if(clientStatus ==3){
            return "已面谈";
        }else if(clientStatus ==4){
            return "已签约";
        }else if(clientStatus ==5){
            return "已流失";
        }
        return "";

    }

    /**
     * 获取客户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:client:shop:attract:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(clientService.selectClientById(id));
    }

    /**
     * 新增客户信息
     */
    @PreAuthorize("@ss.hasPermi('system:client:shop:attract:add')")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Client client)
    {
        client.setsTime(new Date());
        client.setUpTime(new Date());
        return toAjax(clientService.insertClient(client));
    }

    /**
     * 修改客户信息
     */
    @PreAuthorize("@ss.hasPermi('system:client:shop:attract:edit')")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Client client)
    {
        client.setUpTime(new Date());
        return
                toAjax(clientService.updateClient(client));
    }


    @PreAuthorize("@ss.hasPermi('system:client:shop:attract:edit')")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PostMapping("/addFollowers")
    public AjaxResult addFoller(@RequestBody ClientAddFollwer clientAddFollwer)
    {
        return toAjax(clientService.addFoller(clientAddFollwer));
    }

    /**
     * 删除客户信息
     */
    @PreAuthorize("@ss.hasPermi('system:client:shop:attract:remove')")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(clientService.deleteClientByIds(ids));
    }
}
