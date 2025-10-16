package com.ruoyi.controller;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.domain.ReviceCheckImageVO;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.service.WxLoginService;
import com.ruoyi.service.WxUserService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.TbWxBannerService;
import com.ruoyi.utils.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
public class WxLoginController {

    @Autowired
    private WxLoginService wxLoginService;

    @Autowired
    private WxUserService wxUserService;


    @Autowired
    private TbWxBannerService tbWxBannerService;


    /**
     * 登录时获取的 code（微信官方提供的临时凭证）
     *
     * @param
     * @return
     */
    @PostMapping("/wxloginByCode")
    public AjaxResult wxLogin(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        String token = wxLoginService.login(loginBody);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @GetMapping("/wxGetInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        return wxUserService.getWxUserInfo(user);
    }

    @GetMapping("/wxGetBanner")
    public AjaxResult getWxBanner(HttpServletRequest request) {
        String imgUrl = tbWxBannerService.getSJWxBanner();
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
        return AjaxResult.success("", url + imgUrl);
    }

    @RequestMapping("/reviceWxCheckImage")
    public String reviceCheckImage(HttpServletRequest request, String signature, String timestamp, String nonce, String echostr) {
        if (request.getMethod().equalsIgnoreCase("get")) {
            System.out.println("=============");
            System.out.println(signature);
            System.out.println(timestamp);
            System.out.println(nonce);
            System.out.println("=============");
            if (StringUtils.isNotEmpty(signature)) {
                if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                    return echostr;
                }
            }
        }else {//接收用户的相关行为事件结果
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder requestContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestContent.append(line);
                }
                reader.close();
                //接收：{"ToUserName":"gh_ea84a199bf81","FromUserName":"oG0NJ5Oi_3Dd1HlZJ14xfnA0sJ6s","CreateTime":1686131943,"MsgType":"event","Event":"subscribe_msg_popup_event","List":{"PopupScene":"0","SubscribeStatusString":"accept","TemplateId":"4ondVRxk4L20ihrJ3iI15BDK72XatGPxE0MeCVwHasQ"}}
                System.out.println(requestContent.toString());
                //
                return "";
            } catch (IOException e) {
                // 处理异常情况
                e.printStackTrace();
                return e.toString();
            }
        }

        return "";
    }
}
