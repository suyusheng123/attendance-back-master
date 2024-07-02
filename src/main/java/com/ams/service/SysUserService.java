package com.ams.service;

import com.ams.pojo.SysUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * 用户表 服务类
 */
public interface SysUserService {

    //登录
    HashMap<String, Object> login(SysUser a, HttpServletRequest request);

    //新增
    HashMap<String, Object> add(SysUser a);

    //获取邮件验证码
    HashMap<String, String> getSendEmailCode(SysUser a, HttpSession session);

    //修改密码
    boolean updatePassWordByName(String userName, String userPwd, String salt);

    //登录后修改密码
    HashMap<String, Object> updatePwd(SysUser user);

    //查询
    HashMap<String, Object> select(SysUser a);

    //更新
    HashMap<String, Object> update(SysUser a);

    //删除
    HashMap<String,Object> del(SysUser a);
}
