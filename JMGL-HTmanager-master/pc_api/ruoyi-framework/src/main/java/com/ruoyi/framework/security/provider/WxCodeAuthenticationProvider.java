package com.ruoyi.framework.security.provider;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.security.token.WxCodeAuthenticationToken;
import com.ruoyi.system.domain.Client;
import com.ruoyi.system.service.IClientService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Configuration
public class WxCodeAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ISysUserService userService;

    @Value("${wxApi.AppID}")
    private String AppID ;

    @Value("${wxApi.AppSecret}")
    private String AppSecret;

    @Autowired
    private IClientService clientService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WxCodeAuthenticationToken authenticationToken = (WxCodeAuthenticationToken) authentication;

        String wxCode = (String) authenticationToken.getPrincipal();

        String wxClient = (String) authenticationToken.getWxClient();

        String openId = getUserWxOpenId(wxCode);
        if (StringUtils.isEmpty(openId)){
            throw new InternalAuthenticationServiceException("微信获取用户信息失败");
        }
        SysUser sysUser = userService.loadUserByUserOpenId(openId);
        if (null == sysUser){
            sysUser = new SysUser();
            sysUser.setOpenId(openId);
            sysUser.setUserName(openId);
            sysUser.setNickName("暂无昵称");
            if ("0".equals(wxClient)){
                Long[] ids = new Long[]{2L};
                sysUser.setRoleIds(ids);
            }
            userService.insertWxUser(sysUser);

        }
        if ("0".equals(wxClient)){
            LambdaQueryWrapper<Client> clientQueryWrapper = new LambdaQueryWrapper<>();
            clientQueryWrapper.eq(Client::getUserId,sysUser.getUserId());
            Client one = clientService.getOne(clientQueryWrapper);
            if (null == one){
                Client client = new Client();
                client.setUserId(sysUser.getUserId());
                client.setName(openId);
                client.setsTime(new Date());
                clientService.save(client);
            }
        }
        UserDetails userDetails = createLoginUser(sysUser);
        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        WxCodeAuthenticationToken authenticationResult = new WxCodeAuthenticationToken(userDetails, userDetails.getAuthorities(),wxClient);

        authenticationResult.setDetails(authenticationToken.getDetails());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationResult);
        SecurityContextHolder.setContext(context);

        // 手动保存到 HttpSession（关键步骤）
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", context);

        return authenticationResult;
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
        return WxCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public String getUserWxOpenId(String wxCode){
        String url  = "https://api.weixin.qq.com/sns/jscode2session";
        /**
         * 拼接需要的参数
         * appid = AppID 你自己的微信小程序APPID
         * js_code = AppSecret 你自己的微信APP密钥
         * grant_type=authorization_code = code 微信官方提供的临时凭证
         */
        String params = StrUtil.format("appid={}&secret={}&js_code={}&grant_type=authorization_code", AppID, AppSecret, wxCode);
        //开始发起网络请求,若依管理系统自带网络请求工具，直接使用即可
        String res = HttpUtils.sendGet(url,params);
        JSONObject jsonObject = JSON.parseObject(res);
        return (String) jsonObject.get("openid");
    }

}
