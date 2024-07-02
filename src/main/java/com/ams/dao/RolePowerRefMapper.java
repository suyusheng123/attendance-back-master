package com.ams.dao;

import com.ams.pojo.RolePowerRef;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 角色权限表
 */
public interface RolePowerRefMapper extends BaseMapper<RolePowerRef> {

	int delByRoleId(Integer roleId);

	int selectPower(Integer roleId, String powerIndex);
}
