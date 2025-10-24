package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.DesignFlowDetail;
import com.ruoyi.system.domain.vo.AuditDTO;
import com.ruoyi.system.domain.vo.DesignTaskAssignDTO;
import com.ruoyi.system.service.SendMessage;
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
import com.ruoyi.system.domain.Design;
import com.ruoyi.system.service.IDesignService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设计管理Controller
 *
 * @author ruoyi
 * @date 2025-05-28
 */
@RestController
@RequestMapping("/system/design")
public class DesignController extends BaseController
{
    @Autowired
    private IDesignService designService;

    @Autowired
    private SendMessage sendMessage;



    @PreAuthorize("@ss.hasPermi('system:design:list')")
    @GetMapping("/shoplist")
    public TableDataInfo shoplist(Design design)
    {
        startPage();
        List<Design> list = designService.selectDesignList(design);
        Map<String, List<Design>> collect = list.stream().collect(Collectors.groupingBy(Design::getShopName));

        return getDataTable(list);
    }




    /**
     * 查询设计管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:design:list')")
    @GetMapping("/list")
    public TableDataInfo list(Design design)
    {
        SysUser currentUser = getLoginUser().getUser();
        if (!currentUser.isAdmin()){
            design.setFoller(getUserId());
        }
        startPage();
        List<Design> list = designService.selectDesignList(design);
        return getDataTable(list);
    }

    /**
     * 导出设计管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:design:export')")
    @Log(title = "设计管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Design design)
    {
        List<Design> list = designService.selectDesignList(design);
        ExcelUtil<Design> util = new ExcelUtil<Design>(Design.class);
        util.exportExcel(response, list, "设计管理数据");
    }

    /**
     * 获取设计管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:design:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {

        return success(designService.selectDesignById(id));
    }

    /**
     * 新增设计管理
     */
    @PreAuthorize("@ss.hasPermi('system:design:add')")
    @Log(title = "设计管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Design design)
    {
        return toAjax(designService.insertDesign(design));
    }



    @PreAuthorize("@ss.hasPermi('system:design:add')")
    @Log(title = "设计管理", businessType = BusinessType.INSERT)
    @PostMapping("/assignDesignTask")
    public AjaxResult assignDesignTask(@RequestBody DesignTaskAssignDTO designTaskAssignDTO)
    {
        return toAjax(designService.assignDesignTask(designTaskAssignDTO));
    }

    /**
     * 修改设计管理
     */
    @PreAuthorize("@ss.hasPermi('system:design:edit')")
    @Log(title = "设计管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Design design)
    {
        return toAjax(designService.updateDesign(design));
    }

    /**
     * 删除设计管理
     */
    @PreAuthorize("@ss.hasPermi('system:design:remove')")
    @Log(title = "设计管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(designService.deleteDesignByIds(ids));
    }



    @GetMapping("/readMessage/{id}")
    public AjaxResult readMessage(@PathVariable Long id)
    {
        return toAjax(sendMessage.readMessage(id));
    }


    @PostMapping("/addRecord")
    public AjaxResult add(@RequestBody DesignFlowDetail detail)
    {
        return toAjax(designService.add(detail));
    }

    @PostMapping("/submitAudit")
    public AjaxResult auditDesignRecord(@RequestBody AuditDTO auditDTO) {
        try {
            // 调用Service层处理审核逻辑
            designService.auditDesignRecord(auditDTO);
            return AjaxResult.success("审核操作成功");
        } catch (Exception e) {
            return AjaxResult.error("审核失败：" + e.getMessage());
        }
    }

}
