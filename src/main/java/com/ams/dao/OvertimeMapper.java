package com.ams.dao;

import com.ams.pojo.LeaveTable;
import com.ams.pojo.Overtime;
import com.ams.pojo.QingjiaMax;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 加班表
 */
public interface OvertimeMapper extends BaseMapper<Overtime> {

	/**
	 * 查询所有加班列表（未审核在前，根据时间排序）
	 * @param a 查询起止时间
	 * @param b 结束时间
	 * @return 查询的结果条数
	 */
	int queryOverTime(String a, String b);

	// 根据部门查询加班列表（未审核在前，根据时间排序）
	int selectDepartOvertime(String c, String d, String a);

	// 查询所有的考勤信息
	int queryAttendance(String c, String d, String e);


	// 查询加班天数(以月为单位)
	int selectMonth(String c, String d);

	/**
	 * 查询加班天数(以部门为单位)
	 * @param c 起始时间
	 * @param d 结束时间
	 * @param e 部门名称
	 * @return 查询的结果条数
	 */
	int selectDept(String c,String d,String e);

	int selectByStaffIdApplyTime(String staffId, String applyTime);

	// 加班最长时间
//	ArrayList<QingjiaMax> selectOvertimeMax(String a, String b);
//
//	ArrayList<QingjiaMax> selectChidaoMax(String a, String b, String c);
//
//	ArrayList<QingjiaMax> selectAllQingjiaMax();
//
//	ArrayList<QingjiaMax> selectAllChidaoMax(String a);
}
