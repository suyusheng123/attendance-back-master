//package com.ams.config;
//
//import com.ams.dao.AttendanceMapper;
//import com.ams.dao.EmployeeInformationMapper;
//import com.ams.dao.LeaveTableMapper;
//import com.ams.dao.OvertimeMapper;
//import com.ams.pojo.Attendance;
//import com.ams.pojo.EmployeeInformation;
//import com.ams.pojo.LeaveTable;
//import com.ams.pojo.Overtime;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import javax.annotation.Resource;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
//@Slf4j
//public class SaticScheduleTask {
//	@Autowired(required = false)
//	AttendanceMapper attendanceMapper;
//	@Autowired(required = false)
//	EmployeeInformationMapper employeeInformationMapper;
//	@Autowired(required = false)
//	LeaveTableMapper leaveTableMapper;
//
//
//	//3.添加定时任务
//	//每天定时插入考勤人员数据
////	@Scheduled(fixedRate = 60000)//(秒  分  时  日  月  星期  年)
////	private void everyInsert() {
////		log.info("已开启请假出差定时任务");
////		List<EmployeeInformation> emp = employeeInformationMapper.selectList(null);
////		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
////		//查询今天有哪些人请假，出差
////		List<LeaveTable> leaveTables = leaveTableMapper.selectToDay();
////		for (EmployeeInformation e : emp) {
////			// 判断考勤表里有没有这条记录
////			QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
////			queryWrapper.eq("staff_id", e.getStaffId());
////			queryWrapper.eq("clock_date", simp.format(new Date()));
////			Attendance attendance = attendanceMapper.selectOne(queryWrapper);
////			if (attendance == null) {
////				Attendance a = new Attendance();
////				a.setStaffId(e.getStaffId());
////				a.setDepartment(e.getDepartment());
////				a.setStaffName(e.getStaffName());
////				a.setClockDate(simp.format(new Date()));
////				//判断请假，出差名单中是否有此人，有就加上备注
////				for (LeaveTable l : leaveTables) {
////					if (a.getStaffId().equals(l.getStaffId())) {
////						a.setRemarks(l.getLeaveType());
////					}
////				}
////				attendanceMapper.insert(a);
////			}
////		}
////	}
//
//
////	// 早勤打卡
////	@Scheduled(cron = "0 55 20 * * ?")//(秒  分  时  日  月  星期  年)
////	private void everyUppdate() {
////		log.info("已开启早勤定时任务");
////		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
////		QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
////		//查询今天的考勤信息
////		queryWrapper.eq("clock_date", simp.format(new Date()));
////		List<Attendance> attendances = attendanceMapper.selectList(queryWrapper);
////		List<EmployeeInformation> emp = employeeInformationMapper.selectList(null);
////		for (EmployeeInformation e : emp) {
////			for (Attendance a : attendances) {
////				if (e.getStaffId().equals(a.getStaffId())) {
////					if (a.getRemarks() == null) {
////						// 正常出勤
////						if (a.getInTimeState() == 1) {
////							a.setRemarks("正常出勤");
////						}
////						// 迟到
////						if (a.getInTimeState() == 0) {
////							a.setRemarks("迟到");
////						}
////						attendanceMapper.updateById(a);
////					}
////				} else {
////					Attendance attendance = new Attendance();
////					attendance.setStaffId(e.getStaffId());
////					attendance.setStaffName(e.getStaffName());
////					attendance.setDepartment(e.getDepartment());
////					attendance.setClockDate(simp.format(new Date()));
////					attendance.setRemarks("缺勤");
////					attendanceMapper.insert(attendance);
////				}
////			}
////		}
////	}
//
//	// 晚勤打卡
////	@Scheduled(fixedRate = 60000)//(秒  分  时  日  月  星期  年)
////	private void everyDowntime() {
////		log.info("已开启晚勤定时任务");
////		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
////		QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
////		//查询今天的考勤信息
////		queryWrapper.eq("clock_date", simp.format(new Date()));
////		List<Attendance> attendances = attendanceMapper.selectList(queryWrapper);
////		List<EmployeeInformation> emp = employeeInformationMapper.selectList(null);
////		for (EmployeeInformation e : emp) {
////			boolean flag = false;
////			for (Attendance a : attendances) {
////				if (e.getStaffId().equals(a.getStaffId())) {
////					flag = true;
////					if (a.getRemarks() == null) {
////						// 正常出勤
////						if (a.getInTimeState() == 1 && a.getOutTimeState() == 1) {
////							a.setRemarks("正常出勤");
////						}
////						// 迟到
////						if (a.getInTimeState() == 0 && a.getOutTimeState() == 1) {
////							a.setRemarks("迟到");
////						}
////						// 早退
////						if (a.getInTimeState() == 1 && a.getOutTimeState() == 0) {
////							a.setRemarks("早退");
////						}
////						// 迟到，早退
////						if (a.getInTimeState() == 0 && a.getOutTimeState() == 0) {
////							a.setRemarks("迟到,早退");
////						}
////						attendanceMapper.updateById(a);
////						break;
////					}
////				}
////			}
////			if (!flag){
////				Attendance attendance = new Attendance();
////				attendance.setStaffId(e.getStaffId());
////				attendance.setStaffName(e.getStaffName());
////				attendance.setDepartment(e.getDepartment());
////				attendance.setClockDate(simp.format(new Date()));
////				attendance.setRemarks("缺勤");
////				attendanceMapper.insert(attendance);
////			}
////		}
////	}
//}
//
//
//
