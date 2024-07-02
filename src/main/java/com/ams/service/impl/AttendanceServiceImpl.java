package com.ams.service.impl;

import com.ams.config.ComputingTime;
import com.ams.dao.*;
import com.ams.pojo.Attendance;
import com.ams.pojo.EmployeeInformation;
import com.ams.pojo.Overtime;
import com.ams.pojo.SysDept;
import com.ams.service.AttendanceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 考勤表 服务实现类
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {
	@Autowired(required = false)
	AttendanceMapper attendanceMapper;
	@Autowired(required = false)
	SysDeptMapper sysDeptMapper;
	@Autowired(required = false)
	private EmployeeInformationMapper employeeInformationMapper;

	@Resource
	private LeaveTableMapper leaveTableMapper;

	@Resource
	private OvertimeMapper overtimeMapper;

	@Override
	public Map<String, Object> selectList(Attendance a) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//创建分页对象
		Page<Attendance> page = new Page<>(a.getPage(), a.getRow());
		QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();

		//条件查询
		queryWrapper.eq(a.getStaffId() != null, "staff_id", a.getStaffId());
		queryWrapper.ge(a.getBeginTime() != null && a.getBeginTime() != "", "clock_date", a.getBeginTime());
		queryWrapper.le(a.getEndTime() != null && a.getEndTime() != "", "clock_date", a.getEndTime());
		queryWrapper.like(a.getName() != null && a.getName() != "", "staff_name", a.getName());
		queryWrapper.eq(a.getDepartmentSelect() != null && !"全部部门".equals(a.getDepartmentSelect()) && a.getDepartmentSelect() != "", "department", a.getDepartmentSelect());
		queryWrapper.orderByDesc("clock_date");
		//获取分页结果集
		Page<Attendance> p = attendanceMapper.selectPage(page, queryWrapper);

		//获取部门列表
		List<SysDept> sysDepts = sysDeptMapper.selectList(null);
		SysDept sysDept = new SysDept();
		sysDept.setDeptName("全部部门");
		try {
			sysDept.setDownTime(null);
			sysDept.setUpTime(null);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sysDept.setDeptName("全部部门");
		sysDepts.add(sysDept);

		map.put("list", p.getRecords());
		map.put("depts", sysDepts);
		//分页总条数
		map.put("total", p.getTotal());
		return map;
	}

	@Override
	public Map<String, Object> signin(Attendance a) throws ParseException {
		HashMap<String, Object> map = new HashMap<String, Object>();


		Date date = new Date();
		//获取当前日期的年月日
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//获取当前时间的时分秒
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
		String nowDate = sdf.format(date);//当前时间的年月日 2024-06-27
		String nowTime = sdf1.format(date);//当前时间的时分秒 20:51

		// 判断用户是否请过假或已经出过差
		int count = leaveTableMapper.selectBeginEnd(a.getStaffId(), nowDate);

		if (count > 0) {
			map.put("info", "已请假或出差,无需签到");
			return map;
		}


		// 设置打卡时间为当前时间
		a.setClockDate(nowDate);
		a.setClockInTime(nowTime);

		// 查询用户是否已经打过卡了
		int num = attendanceMapper.selectIntime(a.getStaffId(), nowDate);

		if (num > 0) {
			map.put("info", "已经打卡过了");
			return map;
		}
		// 没有打过卡
		// 先根据员工id查询员工信息
		EmployeeInformation emp = new EmployeeInformation();
		emp.setStaffId(a.getStaffId());
		EmployeeInformation employeeInformation = employeeInformationMapper.selectByStaffId(emp);
		// 设置考勤表的员工，员工所在的部门
		a.setStaffName(employeeInformation.getStaffName());
		a.setDepartment(employeeInformation.getDepartment());
		// 查询用户所在部门的上班时间
		SysDept sysDept = sysDeptMapper.selectByTime(employeeInformation.getDepartment());
		// 部门上班时间
		String upTime = sysDept.getUpTime();
		// 部门下班时间
		String downTime = sysDept.getDownTime();

		// 计算部门上班时间 + 10min
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		calendar.setTime(sdf2.parse(upTime));
		calendar.add(Calendar.MINUTE, 10);
		String upTimePlus10Min = sdf2.format(calendar.getTime());

		// 部门下班时间 - 10min
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(sdf2.parse(downTime));
		calendar1.add(Calendar.MINUTE, -10);
		String downTimeMinus10Min = sdf2.format(calendar1.getTime());

		// 当前时间
		SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
		String newNowTime = sdf3.format(date);
//		String newNowTime = "09:11";

		// 00:00 ~ 09:00
		if (newNowTime.compareTo(upTime) < 0) {
			map.put("info", "未到签到时间");
			return map;
		}

//		18:50 ~ 23:59 未到签到时间
		if (newNowTime.compareTo(downTimeMinus10Min) > 0) {
			map.put("info", "签到时间已过");
			return map;
		}

		// 09:10 ~ 18:50 迟到
		if (newNowTime.compareTo(upTimePlus10Min) > 0 && newNowTime.compareTo(downTimeMinus10Min) < 0) {
			// 迟到
			a.setRemarks("迟到");
			attendanceMapper.insert(a);
			map.put("info", "迟到");
			return map;
		}


      // 正常签到时间 09:00 - 09:10
		if (newNowTime.compareTo(upTime) >= 0 && newNowTime.compareTo(upTimePlus10Min) <= 0) {
			// 正常签到
			a.setInTimeState(1);
			a.setRemarks("正常出勤");
			int num2 = attendanceMapper.insert(a);
			if (num2 > 0) {
				map.put("info", "签到成功");
			} else {
				map.put("info", "系统问题，请联系管理员");
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> signout(Attendance a) throws ParseException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Date date = new Date();
		//获取当前日期的年月日
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//获取当前时间的时分秒
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
		String nowDate = sdf.format(date);//当前时间的年月日 2024-06-27
		String nowTime = sdf1.format(date);//当前时间的时分秒 20:51

		// 设置打卡时间为当前时间
		a.setClockDate(nowDate);
		a.setClockOutTime(nowTime);

		// 判断用户是否请过假或已经出过差
		int count = leaveTableMapper.selectBeginEnd(a.getStaffId(), nowDate);

		if (count > 0) {
			map.put("info", "已请假或出差,无需签退");
			return map;
		}

		// 查询用户今天是否已经打过卡了
		int num = attendanceMapper.selectOuttime(a.getStaffId(), nowDate);

		if (num > 0) {
			map.put("info", "已经打卡过了");
			return map;
		}

		// 没有打过卡

		// 先根据员工id查询员工信息
		EmployeeInformation emp = new EmployeeInformation();
		emp.setStaffId(a.getStaffId());
		EmployeeInformation employeeInformation = employeeInformationMapper.selectByStaffId(emp);
		// 设置员工的姓名，以及员工所在的部门
		a.setStaffName(employeeInformation.getStaffName());
		a.setDepartment(employeeInformation.getDepartment());

		// 查询用户所在部门的上班时间
		SysDept sysDept = sysDeptMapper.selectByTime(employeeInformation.getDepartment());

// 创建一个DateTimeFormatter对象，用于解析和格式化时间
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
// 将上班时间和下班时间转换为LocalTime对象
		LocalTime down = LocalTime.parse(sysDept.getDownTime(), formatter);
		LocalTime downTimeMinus10Min = down.minusMinutes(10);
// 将nowTime转换为LocalTime对象
		String parse = a.getClockOutTime().substring(0, a.getClockOutTime().lastIndexOf(":"));
		LocalTime now = LocalTime.parse(parse, formatter);
		// 签退时间未到 0:00 ~ 18:50
		if (now.isBefore(downTimeMinus10Min)) {
			map.put("info", "未到签退时间");
			return map;
		}

		// 19:00 ~ 23:59 早退
		if (now.isAfter(down)) {
			map.put("info", "签退时间过了");
			// 查看有没有打卡记录
			Attendance attendance = attendanceMapper.selectByStaffId(a);
			if (attendance != null){
				// 早退
				QueryWrapper<Attendance> queryWrapper2 = new QueryWrapper<>();
				queryWrapper2.eq("staff_id",a.getStaffId());
				queryWrapper2.eq("clock_date",nowDate);
				if (Objects.equals(attendance.getRemarks(), "迟到")){
					a.setRemarks("迟到,早退");
				}else if(Objects.equals(attendance.getRemarks(), "正常出勤")){
					a.setRemarks("早退");
				}
				attendanceMapper.update(a,queryWrapper2);
			}else {
				a.setClockOutTime(null);
				a.setRemarks("缺勤");
				attendanceMapper.insert(a);
			}
			return map;
		}
		// 正常签退时间 18:50 ~ 19:00
		if (!now.isBefore(downTimeMinus10Min) && !now.isAfter(down)) {
			// 正常签退
			map.put("info", "签退成功");
			a.setOutTimeState(1);
			Attendance attendance = attendanceMapper.selectByStaffId(a);
			if (attendance != null) {
				QueryWrapper<Attendance> queryWrapper3 = new QueryWrapper<>();
				queryWrapper3.eq("staff_id", a.getStaffId());
				queryWrapper3.eq("clock_date",nowDate);
				if (Objects.equals(attendance.getRemarks(), "迟到")){
					a.setRemarks("迟到");
				}else if(Objects.equals(attendance.getRemarks(), "正常出勤")){
					a.setRemarks("正常出勤");
				}
				attendanceMapper.update(a, queryWrapper3);
			}else{
				a.setRemarks("缺勤");
				attendanceMapper.insert(a);
			}
		}
		return map;
	}

}
