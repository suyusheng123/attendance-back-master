package com.ams.service.impl;

import com.ams.dao.*;
import com.ams.pojo.*;
import com.ams.service.LeaveTableService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections4.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import java.text.ParseException;
import java.util.HashMap;

/**
 * <p>
 * 差假表 服务实现类
 */
@Service
public class LeaveTableServiceImpl extends ServiceImpl<LeaveTableMapper, LeaveTable> implements LeaveTableService {
	@Autowired(required = false)
	LeaveTableMapper leaveTableMapper;
	@Autowired(required = false)
	AuditMapper auditMapper;
	@Autowired(required = false)
	private SysDeptMapper sysDeptMapper;
	@Autowired(required = false)
	private EmployeeInformationMapper employeeInformationMapper;


	@Resource
	private OvertimeMapper overtimeMapper;


	@Resource
	private AttendanceMapper attendanceMapper;
	@Override
	//查询请假列表（未审核在前，根据时间排序）
	public HashMap<String, Object> selectByTypeLeave(LeaveTable leave) {
		HashMap<String, Object> map = new HashMap<>();
		Page<LeaveTable> page = new Page<>(leave.getPage(), leave.getRow());
		QueryWrapper<LeaveTable> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(leave.getLeaveType().equals("请假"), "leave_type", leave.getLeaveType());
		queryWrapper.orderByAsc("audit_state", "leave_time");

		Page<LeaveTable> aList = leaveTableMapper.selectPage(page, queryWrapper);
		//当前页集合
		map.put("list", aList.getRecords());
		map.put("total", aList.getTotal());
		System.out.println("=======================" + map);
		return map;
	}


	//请假查询
	@Override
	public HashMap<String, Object> selectMonth() throws ParseException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Integer> integerList = new ArrayList<>();

		for (int i = 1; i < 12; i++) {
			String c = "2024-" + i + "-1";
			String d = "2024-" + (i + 1) + "-1";
			int n = leaveTableMapper.selectbyAllQingjia(c, d);
			integerList.add(n);
		}
		int n = leaveTableMapper.selectbyAllQingjia("2024-12-1", "2025-1-1");
		integerList.add(n);
		map.put("integerList", integerList);
		List<SysDept> selectBumenName = sysDeptMapper.selectList(null);
		SysDept sysDept = new SysDept();
		sysDept.setDeptId(10);
		sysDept.setUpTime("18:20");
		sysDept.setDownTime("18:20");
		sysDept.setDeptName("全部部门");
		selectBumenName.add(sysDept);
		map.put("selectBumenName", selectBumenName);

		return map;
	}

	@Override
	public HashMap<String, Object> selectBumen(String a) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Integer> BumenList = new ArrayList<>();

		for (int i = 1; i < 12; i++) {
			String c = "2024-" + i + "-1";
			String d = "2024-" + (i + 1) + "-1";
			int n = leaveTableMapper.selectbyAllBumenQingjia(c, d, a);
			BumenList.add(n);
		}
		int n = leaveTableMapper.selectbyAllBumenQingjia("2024-12-1", "2024-1-1", a);
		BumenList.add(n);
		map.put("integerList", BumenList);

		return map;
	}

	//出差查询
	@Override
	public HashMap<String, Object> selectChuchaiMonth() throws ParseException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Chuchai> chuchaiList = new ArrayList<>();

		for (int i = 1; i < 12; i++) {
			String c = "2024-" + i + "-1";
			String d = "2024-" + (i + 1) + "-1";
			int n = leaveTableMapper.selectbyAllChuchai(c, d);
			Chuchai chuchai = new Chuchai();
			chuchai.setValue(n);
			chuchai.setName(i + "月");
			chuchaiList.add(chuchai);
		}
		int n = leaveTableMapper.selectbyAllChuchai("2024-12-1", "2024-1-1");
		Chuchai chuchai = new Chuchai();
		chuchai.setValue(n);
		chuchai.setName(12 + "月");
		chuchaiList.add(chuchai);

		map.put("chuchaiList", chuchaiList);
		List<SysDept> selectBumenName = sysDeptMapper.selectList(null);
		SysDept sysDept = new SysDept();
		sysDept.setDeptId(10);
		sysDept.setUpTime("18:20");
		sysDept.setDownTime("18:20");
		sysDept.setDeptName("全部部门");
		selectBumenName.add(sysDept);
		map.put("selectBumenName", selectBumenName);

		return map;
	}

	@Override
	public HashMap<String, Object> selectChuchaiBumen(String a) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Chuchai> chuchaiList = new ArrayList<>();

		for (int i = 1; i < 12; i++) {
			String c = "2024-" + i + "-1";
			String d = "2024-" + (i + 1) + "-1";
			int n = leaveTableMapper.selectbyAllBumenChuchai(c, d, a);
			Chuchai chuchai = new Chuchai();
			chuchai.setValue(n);
			chuchai.setName(i + "月");
			chuchaiList.add(chuchai);
		}
		int n = leaveTableMapper.selectbyAllBumenChuchai("2024-12-1", "2024-1-1", a);
		Chuchai chuchai = new Chuchai();
		chuchai.setValue(n);
		chuchai.setName("12月");
		chuchaiList.add(chuchai);

		map.put("chuchaiList", chuchaiList);

		return map;
	}

	//考勤查询
	@Override
	public HashMap<String, Object> selectKaoqingMonth() throws ParseException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//每月迟到
		ArrayList<Integer> AllKaoqingChidaoList = new ArrayList<>();
		String e = "迟到";
		AllKaoqingChidaoList = kaoqingchaxun(AllKaoqingChidaoList, e, leaveTableMapper);
		map.put("AllKaoqingChidaoList", AllKaoqingChidaoList);
		//每月早退
		ArrayList<Integer> AllKaoqingZaotuiList = new ArrayList<>();
		String f = "早退";
		AllKaoqingZaotuiList = kaoqingchaxun(AllKaoqingZaotuiList, f, leaveTableMapper);
		map.put("AllKaoqingZaotuiList", AllKaoqingZaotuiList);
		//每月迟到，早退
		ArrayList<Integer> AllKaoqingChidaoZaotuiList = new ArrayList<>();
		String h = "迟到,早退";
		AllKaoqingChidaoZaotuiList = kaoqingchaxun(AllKaoqingChidaoZaotuiList, h, leaveTableMapper);
		map.put("AllKaoqingChidaoZaotuiList", AllKaoqingChidaoZaotuiList);
		//每月缺勤
		ArrayList<Integer> AllKaoqingQueqingList = new ArrayList<>();
		String k = "缺勤";
		AllKaoqingQueqingList = kaoqingchaxun(AllKaoqingQueqingList, k, leaveTableMapper);
		map.put("AllKaoqingQueqingList", AllKaoqingQueqingList);

		//部门
		List<SysDept> selectBumenName = sysDeptMapper.selectList(null);
		SysDept sysDept = new SysDept();

		sysDept.setDeptId(10);
		sysDept.setUpTime("18:20");
		sysDept.setDownTime("18:20");
		sysDept.setDeptName("全部部门");

		selectBumenName.add(sysDept);
		map.put("selectBumenName", selectBumenName);

		return map;
	}

	@Override
	public HashMap<String, Object> selectKaoqingBumen(String a) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//每月迟到
		ArrayList<Integer> AllKaoqingChidaoList = new ArrayList<>();
		String e = "迟到";
		AllKaoqingChidaoList = kaoqingBumenChaxun(AllKaoqingChidaoList, e, leaveTableMapper, a);
		map.put("AllKaoqingChidaoList", AllKaoqingChidaoList);
		//每月早退
		ArrayList<Integer> AllKaoqingZaotuiList = new ArrayList<>();
		String f = "早退";
		AllKaoqingZaotuiList = kaoqingBumenChaxun(AllKaoqingZaotuiList, f, leaveTableMapper, a);
		map.put("AllKaoqingZaotuiList", AllKaoqingZaotuiList);
		//每月迟到，早退
		ArrayList<Integer> AllKaoqingChidaoZaotuiList = new ArrayList<>();
		String h = "迟到,早退";
		AllKaoqingChidaoZaotuiList = kaoqingBumenChaxun(AllKaoqingChidaoZaotuiList, h, leaveTableMapper, a);
		map.put("AllKaoqingChidaoZaotuiList", AllKaoqingChidaoZaotuiList);
		//每月缺勤
		ArrayList<Integer> AllKaoqingQueqingList = new ArrayList<>();
		String k = "缺勤";
		AllKaoqingQueqingList = kaoqingBumenChaxun(AllKaoqingQueqingList, k, leaveTableMapper, a);
		map.put("AllKaoqingQueqingList", AllKaoqingQueqingList);

		return map;
	}

	//考勤查询的静态方法
	public static ArrayList<Integer> kaoqingchaxun(ArrayList arrayList, String a, LeaveTableMapper leaveTableMapper) {
		for (int i = 1; i < 12; i++) {
			String c = "2024-" + i + "-1";
			String d = "2024-" + (i + 1) + "-1";
			int l = leaveTableMapper.selectbyAllKaoqing(c, d, a);
			arrayList.add(l);
		}
		int l = leaveTableMapper.selectbyAllKaoqing("2024-12-1", "2024-1-1", a);
		arrayList.add(l);
		return arrayList;
	}

	public static ArrayList<Integer> kaoqingBumenChaxun(ArrayList arrayList, String a, LeaveTableMapper leaveTableMapper, String b) {
		for (int i = 1; i < 12; i++) {
			String c = "2024-" + i + "-1";
			String d = "2024-" + (i + 1) + "-1";
			int n = leaveTableMapper.selectbyAllKaoqingBumen(c, d, a, b);
			arrayList.add(n);
		}
		int n = leaveTableMapper.selectbyAllKaoqingBumen("2024-12-1", "2024-1-1", a, b);
		arrayList.add(n);
		return arrayList;
	}


	@Override
	//查询出差列表（未审核在前，根据时间排序）
	public HashMap<String, Object> selectByTypeBusiness(LeaveTable leave) {
		HashMap<String, Object> map = new HashMap<>();
		Page<LeaveTable> page = new Page<>(leave.getPage(), leave.getRow());
		QueryWrapper<LeaveTable> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(leave.getLeaveType().equals("出差"), "leave_type", leave.getLeaveType());
		queryWrapper.orderByAsc("audit_state");
		queryWrapper.orderByDesc("leave_time");

		Page<LeaveTable> aList = leaveTableMapper.selectPage(page, queryWrapper);
		//当前页集合
		map.put("list", aList.getRecords());
		map.put("total", aList.getTotal());
		System.out.println("=======================" + map);
		return map;
	}

	@Override
	//通过id查询请假列表
	public HashMap<String, Object> selectByIdLeave(LeaveTable leave) {
		HashMap<String, Object> map = new HashMap<>();
		Page<LeaveTable> page = new Page<>(leave.getPage(), leave.getRow());
		QueryWrapper<LeaveTable> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(leave.getStaffId() != null, "staff_id", leave.getStaffId());
		queryWrapper.eq(leave.getLeaveType().equals("请假"), "leave_type", leave.getLeaveType());
		queryWrapper.orderByAsc("audit_state");
		queryWrapper.orderByDesc("leave_time");
		Page<LeaveTable> aList = leaveTableMapper.selectPage(page, queryWrapper);
		map.put("list", aList.getRecords());
		map.put("total", aList.getTotal());
		System.out.println("=======================" + map);
		return map;
	}

	@Override
	//通过id查询出差列表
	public HashMap<String, Object> selectByIdBusiness(LeaveTable leave) {
		HashMap<String, Object> map = new HashMap<>();
		Page<LeaveTable> page = new Page<>(leave.getPage(), leave.getRow());
		QueryWrapper<LeaveTable> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(leave.getStaffId() != null, "staff_id", leave.getStaffId());
		queryWrapper.eq(leave.getLeaveType().equals("出差"), "leave_type", leave.getLeaveType());
		queryWrapper.orderByAsc("audit_state", "leave_time");
		Page<LeaveTable> aList = leaveTableMapper.selectPage(page, queryWrapper);
		map.put("list", aList.getRecords());
		map.put("total", aList.getTotal());
		System.out.println("=======================" + map);
		return map;
	}

	@Override
	//新增操作
	public HashMap<String, Object> add(LeaveTable leave) {
		HashMap<String, Object> map = new HashMap<>();
		leave.setAuditState(0);

		// 请假和出差在同一天不能同时选择
		// 查询用户今天有没有申请过
		QueryWrapper<LeaveTable> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("staff_id", leave.getStaffId());
		queryWrapper.eq("leave_time",leave.getLeaveTime());
		LeaveTable leaveTable = leaveTableMapper.selectOne(queryWrapper);
		if (leaveTable != null){
			// 请假类型相同，提醒用户你已经请过假了
			// 请假类型不同，提醒用户你已经请假或出差了
			if (leaveTable.getLeaveType().equals(leave.getLeaveType())) {
				map.put("info", "你已经" + leaveTable.getLeaveType() + "了");
				return map;
			} else {
				map.put("info", "你已经" + leaveTable.getLeaveType() + "了");
				return map;
			}
		}

		// 查询用户有没有申请过加班，有的话不让请假或者出差
		int i = overtimeMapper.selectByStaffIdApplyTime(leave.getStaffId(), leave.getLeaveTime());
		if (i > 0){
			map.put("info","你已经申请过加班了");
			return map;
		}


		int num = leaveTableMapper.insert(leave);
		if (num > 0) {
			map.put("info", "新增成功");
		} else {
			map.put("info", "新增失败");
		}
		return map;
	}

	@Override
	//删除操作(根据id删除而不是员工id)
	public HashMap<String, Object> del(LeaveTable a) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//修改
		int num = leaveTableMapper.deleteById(a);
		if (num > 0) {
			map.put("info", "撤销成功");
		} else {
			map.put("info", "撤销失败");
		}
		return map;
	}

	@Override
	//修改操作(根据id修改而不是员工id)
	public HashMap<String, Object> update(LeaveTable leave) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//修改
		int num = leaveTableMapper.updateById(leave);
		if (num > 0) {
			map.put("info", "修改成功");
		} else {
			map.put("info", "修改失败");
		}
		return map;
	}

	@Override
	//查询所有并排序（未审核在前，根据时间排序)
	public HashMap<String, Object> selectAllLeave(LeaveTable leave) {
		HashMap<String, Object> map = new HashMap<>();
		Page<LeaveTable> page = new Page<>(leave.getPage(), leave.getRow());
		QueryWrapper<LeaveTable> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(leave.getStaffName() != null && !leave.getStaffName().equals(""), "staff_name",
				leave.getStaffName());
		queryWrapper.like(leave.getStaffDepartment() != null && !leave.getStaffDepartment().equals(""), "staff_department", leave.getStaffDepartment());
		queryWrapper.eq("请假".equals(leave.getLeaveType()), "leave_type", leave.getLeaveType());
		queryWrapper.orderByAsc("audit_state", "leave_time");
		Page<LeaveTable> aList = leaveTableMapper.selectPage(page, queryWrapper);
		//当前页集合
		map.put("list", aList.getRecords());
		map.put("total", aList.getTotal());
		System.out.println("=======================" + map);
		return map;
	}

	@Override
	public HashMap<String, Object> selectAllBusiness(LeaveTable leave) {
		HashMap<String, Object> map = new HashMap<>();
		Page<LeaveTable> page = new Page<>(leave.getPage(), leave.getRow());
		QueryWrapper<LeaveTable> queryWrapper = new QueryWrapper<>();


		queryWrapper.like(leave.getStaffName() != null && !leave.getStaffName().equals(""), "staff_name", leave.getStaffName());
		queryWrapper.like(leave.getStaffDepartment() != null && !leave.getStaffDepartment().equals(""), "staff_department", leave.getStaffDepartment());
		queryWrapper.eq("出差".equals(leave.getLeaveType()), "leave_type", leave.getLeaveType());
		queryWrapper.orderByAsc("audit_state", "leave_time");
		Page<LeaveTable> aList = leaveTableMapper.selectPage(page, queryWrapper);
		//当前页集合
		map.put("list", aList.getRecords());
		map.put("total", aList.getTotal());
		System.out.println("=======================" + map);
		return map;
	}
	/*==============================================================*/


	@Override
	@Transactional
	public HashMap<String, Object> updateAndAdd(Audit a) {
		LeaveTable leaveTable = new LeaveTable();

		Integer id = a.getId();
		leaveTable.setId(a.getId());
		leaveTable.setAuditState(1);
		leaveTable.setAuditResult(a.getAuditResult());
		a.setId(null);
		leaveTableMapper.updateById(leaveTable);
		int num = auditMapper.insert(a);
		HashMap<String, Object> map = new HashMap<>();
		if (num > 0 && Objects.equals(a.getAuditResult(), "通过")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String nowDate = sdf.format(new Date());
			EmployeeInformation emp = new EmployeeInformation();
			emp.setStaffId(a.getStaffId());
			EmployeeInformation employee = employeeInformationMapper.selectByStaffId(emp);

			// 判断用户的请假开始时间和结束时间
			QueryWrapper<LeaveTable> queryWrapper = new QueryWrapper<>();
			LeaveTable leaveTable1 = leaveTableMapper.selectById(id);
			if (nowDate.compareTo(leaveTable1.getLeaveBeginTime()) >= 0 && nowDate.compareTo(leaveTable1.getLeaveEndTime()) <= 0) {
				Attendance attendance = new Attendance();
				attendance.setRemarks(a.getLeaveType());
				attendance.setStaffId(a.getStaffId());
				attendance.setStaffName(employee.getStaffName());
				attendance.setDepartment(employee.getDepartment());
				attendance.setClockDate(sdf.format(new Date()));
				attendanceMapper.insert(attendance);
			}
		}
		map.put("info", "审核成功");
		return map;
	}

	// 撤销审核
	@Override
	public HashMap<String, Object> updatesState(LeaveTable leaveTable) {
		System.out.println("=============================" + leaveTable);
		leaveTable.setAuditState(0);
		leaveTable.setAuditResult("已撤销");

		// 删除考勤表里已请假或出差的记录
		QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("staff_id",leaveTable.getStaffId());
		queryWrapper.eq("clock_date",leaveTable.getLeaveTime());
		queryWrapper.eq("remarks",leaveTable.getLeaveType());
		attendanceMapper.delete(queryWrapper);


		int num = leaveTableMapper.updateById(leaveTable);
		HashMap<String, Object> map = new HashMap<>();
		if (num > 0) {
			map.put("info", "撤销审核成功");
		} else {
			map.put("info", "撤销审核失败");
		}
		System.out.println(map);
		return map;
	}

	/////++++++++++++++++++++++++每月最多的
	@Override
	public HashMap<String, Object> selectAllMonth() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Chuchai> qingjiaMaxArrayList = new ArrayList<>();

		ArrayList<QingjiaMax> List = new ArrayList<>();
		List = leaveTableMapper.selectAllQingjiaMax();
		//获取请假最大数
		if (!List.isEmpty()) {
			int maxNum = List.stream().max(Comparator.comparing(QingjiaMax::getNum)).get().getNum();
			//将最大数的名字存入
			String name = "请假最多的有:";
			for (int j = 0; j < List.size(); j++) {
				if (List.get(j).getNum() == maxNum) {
					name = name + List.get(j).getName() + ",";
				}
			}
			Chuchai chuchai = new Chuchai();
			chuchai.setName(name);
			chuchai.setValue(maxNum);
			qingjiaMaxArrayList.add(chuchai);
		} else {
			Chuchai chuchai = new Chuchai();
			chuchai.setName("请假最多的有:无");
			chuchai.setValue(0);
			qingjiaMaxArrayList.add(chuchai);
		}

		ArrayList<QingjiaMax> List2 = new ArrayList<>();
		List2 = leaveTableMapper.selectAllChidaoMax("迟到");
		//获取迟到最大数
		if (!List2.isEmpty()) {
			int maxNum = List2.stream().max(Comparator.comparing(QingjiaMax::getNum)).get().getNum();
			//将最大数的名字存入
			String name = "迟到最多的有:";
			for (int j = 0; j < List2.size(); j++) {
				if (List2.get(j).getNum() == maxNum) {
					name = name + List2.get(j).getName() + ",";
				}
			}
			Chuchai chuchai = new Chuchai();
			chuchai.setName(name);
			chuchai.setValue(maxNum);
			qingjiaMaxArrayList.add(chuchai);
		} else {
			Chuchai chuchai = new Chuchai();
			chuchai.setName("迟到最多的有:无");
			chuchai.setValue(0);
			qingjiaMaxArrayList.add(chuchai);
		}
		ArrayList<QingjiaMax> List3 = new ArrayList<>();
		List3 = leaveTableMapper.selectAllChidaoMax("早退");
		//获取早退最大数
		if (!List3.isEmpty()) {
			int maxNum = List3.stream().max(Comparator.comparing(QingjiaMax::getNum)).get().getNum();
			//将最大数的名字存入
			String name = "早退最多的有:";
			for (int j = 0; j < List3.size(); j++) {
				if (List3.get(j).getNum() == maxNum) {
					name = name + List3.get(j).getName() + ",";
				}
			}
			Chuchai chuchai = new Chuchai();
			chuchai.setName(name);
			chuchai.setValue(maxNum);
			qingjiaMaxArrayList.add(chuchai);
		} else {
			Chuchai chuchai = new Chuchai();
			chuchai.setName("早退最多的有:无");
			chuchai.setValue(0);
			qingjiaMaxArrayList.add(chuchai);
		}
		ArrayList<String> yuefen = new ArrayList<>();
		for (int i = 1; i < 13; i++) {
			yuefen.add(i + "月");
		}
		yuefen.add("整年");

		map.put("yuefen", yuefen);
		map.put("qingjiaMaxArrayList", qingjiaMaxArrayList);


		return map;
	}

	@Override
	public HashMap<String, Object> selectAllaaaMonth(String a) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Chuchai> qingjiaMaxArrayList = new ArrayList<>();

		ArrayList<String> yuefen = new ArrayList<>();
		for (int i = 1; i < 13; i++) {
			yuefen.add(i + "月");
		}
		int b = yuefen.indexOf(a) + 1;
		if (b != 12) {

			ArrayList<QingjiaMax> List = new ArrayList<>();
			List = leaveTableMapper.selectQingjiaMax("2024-" + b + "-1", "2024-" + (b + 1) + "-1");
			//获取请假最大数
			if (!List.isEmpty()) {
				int maxNum = List.stream().max(Comparator.comparing(QingjiaMax::getNum)).get().getNum();
				//将最大数的名字存入
				String name = "请假最多的有:";
				for (int j = 0; j < List.size(); j++) {
					if (List.get(j).getNum() == maxNum) {
						name = name + List.get(j).getName() + ",";
					}
				}
				Chuchai chuchai = new Chuchai();
				chuchai.setName(name);
				chuchai.setValue(maxNum);
				qingjiaMaxArrayList.add(chuchai);
			} else {
				Chuchai chuchai = new Chuchai();
				chuchai.setName("请假最多的有:无");
				chuchai.setValue(0);
				qingjiaMaxArrayList.add(chuchai);
			}

			ArrayList<QingjiaMax> List2 = new ArrayList<>();
			List2 = leaveTableMapper.selectChidaoMax("2024-" + b + "-1", "2024-" + (b + 1) + "-1", "迟到");
			//获取迟到最大数
			if (!List2.isEmpty()) {
				int maxNum = List2.stream().max(Comparator.comparing(QingjiaMax::getNum)).get().getNum();
				//将最大数的名字存入
				String name = "迟到最多的有:";
				for (int j = 0; j < List2.size(); j++) {
					if (List2.get(j).getNum() == maxNum) {
						name = name + List2.get(j).getName() + ",";
					}
				}
				Chuchai chuchai = new Chuchai();
				chuchai.setName(name);
				chuchai.setValue(maxNum);
				qingjiaMaxArrayList.add(chuchai);
			} else {
				Chuchai chuchai = new Chuchai();
				chuchai.setName("迟到最多的有:无");
				chuchai.setValue(0);
				qingjiaMaxArrayList.add(chuchai);
			}
			ArrayList<QingjiaMax> List3 = new ArrayList<>();
			List3 = leaveTableMapper.selectChidaoMax("2024-" + b + "-1", "2024-" + (b + 1) + "-1", "早退");
			//获取早退最大数
			if (!List3.isEmpty()) {
				int maxNum = List3.stream().max(Comparator.comparing(QingjiaMax::getNum)).get().getNum();
				//将最大数的名字存入
				String name = "早退最多的有:";
				for (int j = 0; j < List3.size(); j++) {
					if (List3.get(j).getNum() == maxNum) {
						name = name + List3.get(j).getName() + ",";
					}
				}
				Chuchai chuchai = new Chuchai();
				chuchai.setName(name);
				chuchai.setValue(maxNum);
				qingjiaMaxArrayList.add(chuchai);
			} else {
				Chuchai chuchai = new Chuchai();
				chuchai.setName("早退最多的有:无");
				chuchai.setValue(0);
				qingjiaMaxArrayList.add(chuchai);
			}
			map.put("qingjiaMaxArrayList", qingjiaMaxArrayList);
		} else {

			ArrayList<QingjiaMax> List = new ArrayList<>();
			List = leaveTableMapper.selectQingjiaMax("2024-" + b + "-1", "2024-1-1");
			//获取请假最大数
			if (!List.isEmpty()) {
				int maxNum = List.stream().max(Comparator.comparing(QingjiaMax::getNum)).get().getNum();
				//将最大数的名字存入
				String name = "请假最多的有:";
				for (int j = 0; j < List.size(); j++) {
					if (List.get(j).getNum() == maxNum) {
						name = name + List.get(j).getName() + ",";
					}
				}
				Chuchai chuchai = new Chuchai();
				chuchai.setName(name);
				chuchai.setValue(maxNum);
				qingjiaMaxArrayList.add(chuchai);
			} else {
				Chuchai chuchai = new Chuchai();
				chuchai.setName("请假最多的有:无");
				chuchai.setValue(0);
				qingjiaMaxArrayList.add(chuchai);
			}

			ArrayList<QingjiaMax> List2 = new ArrayList<>();
			List2 = leaveTableMapper.selectChidaoMax("2024-" + b + "-1", "2024-1-1", "迟到");
			//获取迟到最大数
			if (!List2.isEmpty()) {
				int maxNum = List2.stream().max(Comparator.comparing(QingjiaMax::getNum)).get().getNum();
				//将最大数的名字存入
				String name = "迟到最多的有:";
				for (int j = 0; j < List2.size(); j++) {
					if (List2.get(j).getNum() == maxNum) {
						name = name + List2.get(j).getName() + ",";
					}
				}
				Chuchai chuchai = new Chuchai();
				chuchai.setName(name);
				chuchai.setValue(maxNum);
				qingjiaMaxArrayList.add(chuchai);
			} else {
				Chuchai chuchai = new Chuchai();
				chuchai.setName("迟到最多的有:无");
				chuchai.setValue(0);
				qingjiaMaxArrayList.add(chuchai);
			}
			ArrayList<QingjiaMax> List3 = new ArrayList<>();
			List3 = leaveTableMapper.selectChidaoMax("2024-" + b + "-1", "2024-1-1", "早退");
			//获取早退最大数
			if (!List3.isEmpty()) {
				int maxNum = List3.stream().max(Comparator.comparing(QingjiaMax::getNum)).get().getNum();
				//将最大数的名字存入
				String name = "早退最多的有:";
				for (int j = 0; j < List3.size(); j++) {
					if (List3.get(j).getNum() == maxNum) {
						name = name + List3.get(j).getName() + ",";
					}
				}
				Chuchai chuchai = new Chuchai();
				chuchai.setName(name);
				chuchai.setValue(maxNum);
				qingjiaMaxArrayList.add(chuchai);
			} else {
				Chuchai chuchai = new Chuchai();
				chuchai.setName("早退最多的有:无");
				chuchai.setValue(0);
				qingjiaMaxArrayList.add(chuchai);
			}
			map.put("qingjiaMaxArrayList", qingjiaMaxArrayList);
		}


		return map;
	}
//+++++++++++++++++++++++++++++++++
}
