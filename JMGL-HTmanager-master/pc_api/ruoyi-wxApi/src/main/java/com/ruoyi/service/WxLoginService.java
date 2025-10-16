package com.ruoyi.service;

import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.framework.security.token.WxCodeAuthenticationToken;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WxLoginService {

    @Resource
    private AuthenticationManager authenticationManager;


    @Autowired
    private TokenService tokenService;

    public String login(LoginBody loginBody){
        // 手动触发认证流程
        Authentication authentication = authenticationManager.authenticate(
                new WxCodeAuthenticationToken(loginBody.getWxCode(),loginBody.getWxClient())
        );

        // 生成Token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return tokenService.createToken(loginUser);
    }

}
