package com.baizhi.shiro;

import com.baizhi.dao.AdminDao;
import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import com.baizhi.util.MyUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
@Component("myShiRo")
public class MyShiRo extends AuthorizingRealm{
    @Autowired
    private AdminService adminService;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取身份信息
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        //查询用户所有的角色
        List<String> list = adminService.selectAllRole(primaryPrincipal);
        //用于去重的set集合
        HashSet<String > set = new HashSet<>();
        //遍历查询用户所有的权限
        for (String s : list) {
            List<String> premission = adminService.selectAllPremission(s);
            for (String s1 : premission) {
                set.add(s1);
            }
        }
        //添加所有角色和权限
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(list);
        authorizationInfo.addStringPermissions(set);
        return authorizationInfo;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        AdminDao adminDao= (AdminDao) MyUtils.getBean(AdminDao.class);
        Admin admin = adminDao.selectAdmin(principal);
        if (admin==null){
            return null;
        }
        AuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(admin.getUsername(),admin.getPassword(), ByteSource.Util.bytes(admin.getSalt()),this.getName());
        return authenticationInfo;
    }
}
