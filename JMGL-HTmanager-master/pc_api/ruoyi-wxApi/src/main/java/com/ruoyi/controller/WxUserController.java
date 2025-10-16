package com.ruoyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.WxRequestUtils;
import com.ruoyi.domain.BindUser;
import com.ruoyi.domain.UpdateUser;
import com.ruoyi.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WxUserController {

    @Autowired
    private WxRequestUtils wxRequestUtils;

    @Autowired
    private WxUserService wxUserService;

    @PostMapping("/wxGetPhone")
    public AjaxResult wxGetPhone(@RequestBody JSONObject object) {
        return AjaxResult.success("获取成功",wxRequestUtils.getWxPhone(String.valueOf(object.get("code"))));
    }

    @PostMapping("/wxUpdateUserInfo")
    @ResponseBody
    public AjaxResult wxUpdateUserInfo(@RequestBody UpdateUser sysUser){
        if (null ==sysUser.getUserId()){
         return  AjaxResult.error("用户id不能为空");
        }
        return wxUserService.wxUpdateUserInfo(sysUser);
    }

    @PostMapping("/wxUserBind")
    public AjaxResult wxUserBind(@Validated @RequestBody BindUser bindUser) {
        return wxUserService.wxUserBind(bindUser);
    }

    @GetMapping("/cancelBind")
    public AjaxResult cancelBind(Long userId){
        wxUserService.cancelBind(userId);
        return AjaxResult.success();
    }

}
