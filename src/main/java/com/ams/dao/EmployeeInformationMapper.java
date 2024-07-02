package com.ams.dao;

import com.ams.pojo.EmployeeInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 员工信息表 Mapper 接口
 */
public interface EmployeeInformationMapper extends BaseMapper<EmployeeInformation> {
	//通过员工编号修改密码
	boolean updatePwdByStaffId(EmployeeInformation a);

	void delByStaffId(Integer id);

	//通过员工编号查询员工
	EmployeeInformation selectByStaffId(EmployeeInformation emp);

	boolean updateByStaffId(Integer i, String staffId);
}
