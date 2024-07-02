package com.ams.service;

import com.ams.dto.AuditOvertimeDTO;
import com.ams.dto.OvertimeDTO;
import com.ams.pojo.Overtime;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.HashMap;


public interface IOvertimeService extends IService<Overtime> {

	// 查询加班列表
	HashMap<String, Object> queryOvertime(Overtime overtime);

	// 加班申请
	HashMap<String, Object> add(OvertimeDTO overtimeDTO);

	// 修改加班申请
	HashMap<String, Object> updateOvertime(OvertimeDTO overtimeDTO);

	HashMap<String, Object> del(int id);

	// 查询所有的加班记录
	HashMap<String, Object> queryAll(Overtime overtime);

	// 撤销审核
	HashMap<String, Object> updateState(int id);

	// 审核
	HashMap<String, Object> updateAndAdd(AuditOvertimeDTO a);

	// 统计加班时长(按月)
	HashMap<String, Object> selectMonth() throws ParseException;

	// 统计加班时长(按部门)
	HashMap<String, Object> selectDept(String a);
}
