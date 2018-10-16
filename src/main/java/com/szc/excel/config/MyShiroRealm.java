package com.szc.excel.config;


import com.szc.excel.domain.SysPermission;
import com.szc.excel.domain.SysRole;
import com.szc.excel.domain.UserInfo;
import com.szc.excel.mapper.SysPermissionMapper;
import com.szc.excel.mapper.SysRoleMapper;
import com.szc.excel.mapper.SysRolePermissionMapper;
import com.szc.excel.mapper.UserInfoMapper;
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

import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    /**
     * 授权用户身份
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        try{

            UserInfo userInfo  = (UserInfo)principals.getPrimaryPrincipal();
            List<SysRole> sysRoles = sysRoleMapper.getRoleList(userInfo);
            for(SysRole role:sysRoles){
                System.out.println("设置角色:"+role);
                authorizationInfo.addRole(role.getRole());
                List<SysPermission> permissions = sysPermissionMapper.getPermissions(role);
                for(SysPermission p:permissions){
                    System.out.println("设置权限:"+p);
                    authorizationInfo.addStringPermission(p.getPermission());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return authorizationInfo;
    }

    /**
     * 验证用户身份，也就是说验证用户输入的账号和密码是否正确。
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证权限->MyShiroRealm.doGetAuthenticationInfo()");
        //获取用户的输入的账号.
        String username = (String)token.getPrincipal();
        System.out.println(token.getCredentials());
        System.out.println(username);
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        UserInfo userInfo = null;
        try{
            userInfo  = userInfoMapper.findByUsername(username);
       }catch (Exception e){
           e.printStackTrace();
       }
        System.out.println("----->>userInfo="+userInfo);
        if(userInfo == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户名
                userInfo.getPassword(), //密码
//                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }
}
