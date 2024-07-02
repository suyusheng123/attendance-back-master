package com.ams.dao;

import com.ams.pojo.SysRole;
import com.ams.pojo.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * 角色表 Mapper 接口
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("select role_id from sys_role where sys_role_name=#{sysRoleName}")
    SysRole selectIdBySysRoleName(String sysRoleName);


    // 判断角色名是否存在
    int selectRoleName(String sysRoleName);

    // 判断角色编码是否存在
    int selectRoleCode(String sysRoleCode);
}
