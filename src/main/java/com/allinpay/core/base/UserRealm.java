package com.allinpay.core.base;

import com.allinpay.core.constant.Constant;
import com.allinpay.entity.TEtcSysMenu;
import com.allinpay.entity.TEtcSysUser;
import com.allinpay.mapper.TEtcMenuMapper;
import com.allinpay.mapper.TEtcUserMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private TEtcUserMapper TEtcUserMapper;


    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        TEtcSysUser user = (TEtcSysUser) principals.getPrimaryPrincipal();
        Integer userId = user.getUserId();
        Set<String> permsList;
        if (userId == Constant.SUPER_ADMIN) {
            permsList = this.TEtcUserMapper.selectAllPerms();
        } else {
            permsList = this.TEtcUserMapper.selectAllPermsByRoleId(userId);
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsList);
        return info;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        TEtcSysUser user = (TEtcSysUser) this.TEtcUserMapper.selectOne((Wrapper) (new QueryWrapper()).eq("username", token.getUsername()));
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        } else if (user.getStatus().equals("0")) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        } else {
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), Util.bytes(user.getSalt()), this.getName());
            return info;
        }
    }

    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName("SHA-256");
        shaCredentialsMatcher.setHashIterations(16);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }
}
