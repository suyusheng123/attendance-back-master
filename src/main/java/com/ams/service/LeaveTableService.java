package com.ams.service;

import com.ams.pojo.Audit;
import com.ams.pojo.LeaveTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.HashMap;

import java.util.HashMap;

/**
 * <p>
 * 差假表 服务类
 */
public interface LeaveTableService extends IService<LeaveTable> {
	//查询请假列表（未审核在前，根据时间排序）
	HashMap<String, Object> selectByTypeLeave(LeaveTable leave);

	//查询出差列表（未审核在前，根据时间排序
	HashMap<String, Object> selectByTypeBusiness(LeaveTable leave);

	//通过id查询请假列表
	HashMap<String, Object> selectByIdLeave(LeaveTable leave);

	//通过id查询出差列表
	HashMap<String, Object> selectByIdBusiness(LeaveTable leave);

	//新增操作
	HashMap<String, Object> add(LeaveTable leave);

	//删除操作
	HashMap<String, Object> del(LeaveTable a);

	//修改操作
	HashMap<String, Object> update(LeaveTable leave);

	//查询所有并排序（未审核在前，根据时间排序)
	HashMap<String, Object> selectAllLeave(LeaveTable leave);

	HashMap<String, Object> selectAllBusiness(LeaveTable leave);


	//模糊查询（根据部门或者姓名进行查询，未审核在前，根据时间排序）
	HashMap<String, Object> updateAndAdd(Audit a);

	HashMap<String, Object> updatesState(LeaveTable leaveTable);


	HashMap<String, Object> selectMonth() throws ParseException;

	HashMap<String, Object> selectBumen(String a);

	HashMap<String, Object> selectChuchaiMonth() throws ParseException;

	HashMap<String, Object> selectChuchaiBumen(String a);

	HashMap<String, Object> selectKaoqingMonth() throws ParseException;

	HashMap<String, Object> selectKaoqingBumen(String a);


	//最多请假、早退、迟到
	HashMap<String,Object> selectAllMonth();
	HashMap<String,Object> selectAllaaaMonth(String a);



}
