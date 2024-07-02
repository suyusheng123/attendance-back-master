package com.ams.realm;

import com.ams.dao.SysUserMapper;
import com.ams.pojo.SysUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 用户登录校验
 */
public class MyRealm extends AuthorizingRealm {
    @Autowired(required = false)
    SysUserMapper sysUserMapper;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        //获取认证通过的用户名
//        Object username = principals.fromRealm(this.getName()).iterator().next();
//        //根据用户名查询用户拥有的角色参数
//        List<Admin> list =adminMapper.selectRolesByUserName(username+"");
//
//        //创建授权对象
//        SimpleAuthorizationInfo smp=new SimpleAuthorizationInfo();
//
//        if (list.size()>0) {
//            //添加角色
//            for(Role r:list.get(0).getRoleList()){
//                //给授权对象添加角色
//                smp.addRole(r.getRoleParam());
//                System.out.println("给"+username+"赋予了角色"+r.getRoleName());
//            }
//        }
        return null;
    }

    //认证（登录）
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //取出用户名
        UsernamePasswordToken t=(UsernamePasswordToken) token;
        String username=t.getPrincipal()+"";
        //验证用户名输入是否正确
        List<SysUser> list = sysUserMapper.selectByName(username);

        if(list.size()==0){
            throw new UnknownAccountException();
        }

        //创建凭证  验证密码
        AuthenticationInfo au=new SimpleAuthenticationInfo(
                list.get(0).getUserName(),//用户名
                list.get(0).getUserPwd(),//密码
                new Md5Hash(list.get(0).getSalt()),//加密算法和盐值
                this.getName()//myrealm对象
                );
        return au;
    }

}
