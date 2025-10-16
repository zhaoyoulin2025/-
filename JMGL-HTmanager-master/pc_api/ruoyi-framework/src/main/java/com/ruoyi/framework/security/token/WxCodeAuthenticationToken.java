package com.ruoyi.framework.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

public class WxCodeAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    /**
     * 在 UsernamePasswordAuthenticationToken 中该字段代表登录的用户名，
     * 在这里就代表登录的手机号码
     */
    private final Object principal;

    private final Object wxClient;

    /**
     * 构建一个没有鉴权的 SmsCodeAuthenticationToken
     */
    public WxCodeAuthenticationToken(Object principal,Object wxClient) {
        super(null);
        this.principal = principal;
        this.wxClient =wxClient;
        setAuthenticated(false);
    }

    /**
     * 构建拥有鉴权的 SmsCodeAuthenticationToken
     */
    public WxCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities,Object wxClient) {
        super(authorities);
        this.principal = principal;
        this.wxClient = wxClient;
        super.setAuthenticated(true); // must use super, as we override
    }



    // ~ Methods
    // 剩下的方法不用动就行了 就是从 UsernamePasswordAuthenticationToken 里粘贴出来的
    // ========================================================================================================

    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public Object getWxClient() {
        return this.wxClient;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
