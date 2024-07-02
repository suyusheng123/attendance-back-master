package com.ams.service;

import java.util.HashMap;



public interface NodeService {
	//查询权限
	HashMap<String,Object> selectPower(String userName);

	//查询所有权限
	HashMap<String,Object> selectAllPower();

	//查询已选中的权限
	HashMap<String,Object> selectPowerRoleId(Integer roleId);
}
