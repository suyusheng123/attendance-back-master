package com.ams.service;

import com.ams.pojo.SysRole;


import java.util.HashMap;

/**
 * 角色表 服务类
 */
public interface SysRoleService  {
	//查询
	HashMap<String, Object> select(SysRole a);
	//添加
	HashMap<String, Object> add(SysRole a,int[] permissions);
	//删除
	HashMap<String, Object> del(SysRole a);
	//更新
	HashMap<String, Object> update(SysRole a,int[] permissions);

}
