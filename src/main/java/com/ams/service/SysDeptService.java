package com.ams.service;

import com.ams.pojo.SysDept;

import java.util.HashMap;

/**
 * 员工部门表 服务类
 */
public interface SysDeptService  {

	//查询
	HashMap<String, Object> select(SysDept a);
	//查询所有
	HashMap<String, Object> selectAll();
	//添加
	HashMap<String, Object> add(SysDept a);
	//删除
	HashMap<String, Object> del(SysDept a);
	//更新
	HashMap<String, Object> update(SysDept a);

	// 查询部门信息
	HashMap<String, Object> queryDept(String deptName);
}
