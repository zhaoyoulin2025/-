package com.ruoyi.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.domain.BindUser;
import com.ruoyi.domain.TbQuestion;
import com.ruoyi.domain.UpdateUser;
import com.ruoyi.framework.security.token.WxCodeAuthenticationToken;
import com.ruoyi.framework.web.service.SysPasswordService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class WxUserService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private IFranchiseInvestmentFormService franchiseInvestmentFormService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private IInviteService inviteService;

    @Autowired
    private IShopSignService shopSignService;

    @Autowired
    private IShopService shopService;

    public AjaxResult wxUpdateUserInfo(UpdateUser user) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(user.getUserId());
        if(StringUtils.isNotEmpty(user.getNickName())){
            sysUser.setNickName(user.getNickName());
        }
        if(StringUtils.isNotEmpty(user.getAvatar())){
            sysUser.setAvatar(user.getAvatar());
        }
        sysUser.setPhonenumber(user.getPhonenumber());
        userService.updateWxUser(sysUser);
        UpdateWrapper<Client> clientUpdateWrapper = new UpdateWrapper<>();
        clientUpdateWrapper.eq("userId", user.getUserId());
        if(StringUtils.isNotEmpty(user.getNickName())){
            clientUpdateWrapper.set("name", user.getNickName());
        }
        clientUpdateWrapper.set("phone", user.getPhonenumber());
        clientUpdateWrapper.set("upTime", new Date());
        clientService.update(clientUpdateWrapper);
        return getWxUserInfo(sysUser);
    }

    public AjaxResult getWxUserInfo(SysUser user) {
        SysUser sysUser1 = userService.selectUserById(user.getUserId());
        //查客户表id
        LambdaQueryWrapper<Client> clientQueryWrapper = new LambdaQueryWrapper<>();
        clientQueryWrapper.eq(Client::getUserId, user.getUserId());
        Client client = clientService.getOne(clientQueryWrapper);
        boolean custemFlag = true; //客户标记
        int customerType = 4; // 没有提交问卷=0;  提交问卷没有预约=1;  预约了=2;  签约了的=3 不是客户 = 4
        List<SysMenu> menus = new ArrayList<>();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        for (String role : roles) {
            if ("common".equals(role)) {
                break;
            } else {
                custemFlag = false;
            }
        }
        if (custemFlag) {
            if(null != client){
                //就看是否签约过
                LambdaQueryWrapper<Shop> shopLambdaQueryWrapper = new LambdaQueryWrapper<>();
                shopLambdaQueryWrapper.eq(Shop::getClientId, client.getId());
                long shopSignCount = shopService.count(shopLambdaQueryWrapper);
                if (shopSignCount > 0) {
                    customerType = 3;
                } else {
                    //如果没有 就看是否填过问卷
                    if (null != client) {
                        sysUser1.setClientId(client.getId());
                        if (StringUtils.isNotEmpty(client.getImprovementSuggestion())) {
                            customerType = 1;
                        } else {
                            customerType = 0;
                        }
                    } else {
                        customerType = 0;
                    }
                }
            }else {
                customerType = 0;
            }
        }
        sysUser1.setCustomerType(customerType);
        sysUser1.setInviteFlag(0);
        if (null != client) {
            sysUser1.setClientId(client.getId());
            sysUser1.setShopResource(client.getShopResource());
            // 查询该用户的预约记录
            // 如果没签约就看是否预约过，
            LambdaQueryWrapper<Invite> inviteLambdaQueryWrapper = new LambdaQueryWrapper<>();
            inviteLambdaQueryWrapper.eq(Invite::getClientId, client.getId());
            long count = inviteService.count(inviteLambdaQueryWrapper);
            if (count > 0) {
                sysUser1.setInviteFlag(1);
            }
            sysUser1.setPhonenumber(client.getPhone());
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", sysUser1);
        return ajax;
    }

    public void updateWxUserSession(Long userId, String wxClient) {
        SysUser sysUser = userService.selectUserById(userId);
        // 2. 生成新的 UserDetails
        UserDetails userDetails = new LoginUser(userId, sysUser.getDeptId(), sysUser, null);
        ;

        // 3. 创建新的认证令牌
        WxCodeAuthenticationToken newAuth = new WxCodeAuthenticationToken(
                userDetails,
                userDetails.getAuthorities(),
                wxClient
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

    }

    public void cancelBind(Long userId) {
        userService.cancelBind(userId);
    }

    public AjaxResult wxUserBind(BindUser bindUser) {
        if (null == bindUser.getUserId()) {
            return AjaxResult.error("用户ID不能为空");
        }
        SysUser user = userService.selectUserByUserName(bindUser.getUsername());
        if (StringUtils.isNull(user)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }
        if (StringUtils.isNotEmpty(user.getOpenId())) {
            return AjaxResult.error("该账号已绑定，请解绑后再重试！");
        }
        if (!passwordService.matches(user, bindUser.getPassword())) {
            return AjaxResult.error("密码错误");
        }
        SysUser userOld = userService.selectUserById(bindUser.getUserId());
        user.setOpenId(userOld.getOpenId());
        userService.updateWxUser(user);
        userService.deleteUserById(userOld.getUserId());
        return AjaxResult.success();
    }

}
