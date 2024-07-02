package com.ams.dao;

import com.ams.pojo.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户表 Mapper 接口
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    //用户名查询
    @Select("SELECT user_id,staff_id,user_name,user_pwd,salt,email,state FROM sys_user WHERE user_name=#{username}")
    List<SysUser> selectByName(String username);

    @Update("update sys_user set user_pwd=#{p}" +
            " where user_name=#{userName}")
    int updatePassWordByName(String userName, String p);

    @Delete("delete from sys_user where staff_id=#{staffId}")
    int delectByStaffId(Integer staffId);

    @Select("select user_id from sys_user where staff_id=#{staffId}")
    SysUser selectIdByStaffId(Integer staffId);
}
