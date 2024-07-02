package com.ams.service;

import com.ams.pojo.EmployeeInformation;
import com.ams.pojo.SysUser;

import java.text.ParseException;
import java.util.HashMap;

/**
 * 员工信息表 服务类
 */
public interface EmployeeInformationService  {
	//新增
	boolean addname(EmployeeInformation a);
	//查询
	HashMap<String,Object> select(EmployeeInformation a) throws ParseException;
	//新增
	HashMap<String,Object> add(EmployeeInformation a, SysUser user);
	//修改
	HashMap<String,Object> update(EmployeeInformation a);
	//删除
	HashMap<String,Object> del(EmployeeInformation a);
	//通过员工编号修改密码
	boolean updatePwdByStaffId(EmployeeInformation a);
	//通过员工编号查询员工
	EmployeeInformation selectByStaffId(EmployeeInformation emp);
}
