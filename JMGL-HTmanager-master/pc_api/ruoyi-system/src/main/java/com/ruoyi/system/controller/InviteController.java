package com.ruoyi.system.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
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
import com.ruoyi.system.domain.Invite;
import com.ruoyi.system.service.IInviteService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 邀请记录Controller
 * 
 * @author ruoyi
 * @date 2025-06-04
 */
@RestController
@RequestMapping("/system/invite")
public class InviteController extends BaseController
{
    @Autowired
    private IInviteService inviteService;

    /**
     * 查询邀请记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:invite:list')")
    @GetMapping("/list")
    public TableDataInfo list(Invite invite)
    {
        startPage();
        SysUser currentUser = getLoginUser().getUser();
        if (!currentUser.isAdmin()){
            invite.setGuideId(getUserId());
        }
        List<Invite> list = inviteService.selectInviteList(invite);
        return getDataTable(list);
    }

    /**
     * 导出邀请记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:invite:export')")
    @Log(title = "邀请记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Invite invite)
    {
        List<Invite> list = inviteService.selectInviteList(invite);


        for (Invite invite1 : list) {
            String s = Long.valueOf(invite1.getInviteStatus()) == 0 ? "待确认" :
                    Long.valueOf(invite1.getInviteStatus()) == 1?"已确认":"推迟计划";
            invite1.setInviteStatusName(s);
        }
        ExcelUtil<Invite> util = new ExcelUtil<Invite>(Invite.class);
        util.exportExcel(response, list, "邀请记录数据");
    }

    /**
     * 获取邀请记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:invite:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(inviteService.selectInviteById(id));
    }

    @GetMapping(value = "/queryTimeByDate")
    public AjaxResult queryTimeByDate(String dateStr)
    {
        return success(inviteService.inviteQueryByDatePc(dateStr));
    }

    /**
     * 新增邀请记录
     */
    @PreAuthorize("@ss.hasPermi('system:invite:add')")
    @Log(title = "邀请记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Invite invite)
    {
        invite.setGuideId(getUserId());
        invite.setInvitationTime(new Date());
        return toAjax(inviteService.insertInvite(invite));
    }

    /**
     * 修改邀请记录
     */
    @PreAuthorize("@ss.hasPermi('system:invite:edit')")
    @Log(title = "邀请记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Invite invite)
    {
        return toAjax(inviteService.updateInvite(invite));
    }

    /**
     * 删除邀请记录
     */
    @PreAuthorize("@ss.hasPermi('system:invite:remove')")
    @Log(title = "邀请记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(inviteService.deleteInviteByIds(ids));
    }
}
