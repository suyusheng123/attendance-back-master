package com.ams.service.impl;

import com.ams.dao.*;
import com.ams.dto.AuditOvertimeDTO;
import com.ams.dto.OvertimeDTO;
import com.ams.pojo.*;
import com.ams.service.IOvertimeService;
import com.ams.vo.OverTimeList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 加班实现类
 */
@Service
public class OvertimeServiceImpl extends ServiceImpl<OvertimeMapper, Overtime> implements IOvertimeService {


	@Resource
	private SysDeptMapper sysDeptMapper;
	@Resource
	private OvertimeMapper overtimeMapper;

	@Resource
	private EmployeeInformationMapper employeeMapper;

	@Resource
	private AuditMapper auditMapper;

	@Resource
	private LeaveTableMapper leaveTableMapper;
	/**
	 * 查询加班列表(查询自己的)
	 * @return
	 */
	@Override
	public HashMap<String, Object> queryOvertime(Overtime overtime) {
		HashMap<String,Object> map = new HashMap<>();
		Page<Overtime> page = new Page<>(overtime.getPage(), overtime.getRow());

		// 条件构造器
		QueryWrapper<Overtime> queryWrapper = new QueryWrapper<>();
		// 查询自己的加班记录
		queryWrapper.eq("staff_id", overtime.getStaffId());
		// 查询未撤销的加班申请
		queryWrapper.eq("apply_state", 0);
		// 未审核的在前
		queryWrapper.orderByAsc("audit_state");
		// 按照申请时间降序排序
		queryWrapper.orderByDesc("apply_time");

		page(page, queryWrapper);

		map.put("total", page.getTotal());
		map.put("list", page.getRecords());
		System.out.println(map);
		return map;
	}

	/**
	 * 加班申请
	 * @param overtimeDTO 前端传入的参数
	 * @return
	 */
	@Override
	public HashMap<String, Object> add(OvertimeDTO overtimeDTO) {
		HashMap<String,Object> map = new HashMap<>();
		String staffId = overtimeDTO.getStaffId();
		String applyTime = overtimeDTO.getApplyTime().toLocalDate().toString();

		// 查询用户今天有没有申请过请假或者出差了,有的话，就不让申请加班
		QueryWrapper<LeaveTable> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("staff_id", staffId);
		queryWrapper.eq("leave_time",applyTime);
		LeaveTable leaveTable = leaveTableMapper.selectOne(queryWrapper);
		if (leaveTable != null){
			map.put("info","你今天已经申请过" + leaveTable.getLeaveType() + "了");
			return map;
		}


		// 查询用户今天有没有申请过加班了
		int i = overtimeMapper.selectByStaffIdApplyTime(staffId, applyTime);
		if (i > 0){
			map.put("info","你今天已经申请过加班了");
			return map;
		}

		Overtime overtime = new Overtime();
		// 设置审核状态未审核
		overtime.setAuditState(0);
		overtime.setApplyState(0);
		// 创建一个员工信息
		EmployeeInformation emp = new EmployeeInformation();
		emp.setStaffId(overtimeDTO.getStaffId());
		EmployeeInformation employee = employeeMapper.selectByStaffId(emp);
		// 加班次数 + 1
        // employee.setOvertimeNumber(employee.getOvertimeNumber() + 1);
		// 更新员工信息(加班次数 + 1)
		int res = employeeMapper.updateById(employee);
		// 加班记录添加到加班表中
		overtime.setStaffId(overtimeDTO.getStaffId());
		overtime.setStaffDevelopment(overtimeDTO.getStaffDevelopment());
		overtime.setStaffName(employee.getStaffName());
		overtime.setApplyTime(overtimeDTO.getApplyTime());
		overtime.setOverBegin(overtimeDTO.getOverBegin());
		overtime.setOverEnd(overtimeDTO.getOverEnd());
		int num = overtimeMapper.insert(overtime);
		if (num > 0 && res > 0) {
			map.put("info", "新增成功");
		} else {
			map.put("info", "新增失败");
		}
		return map;
	}

	/**
	 * 修改加班申请
	 * @param overtimeDTO 前端传入的参数
	 * @return
	 */
	@Override
	public HashMap<String, Object> updateOvertime(OvertimeDTO overtimeDTO) {
		HashMap<String,Object> map = new HashMap<>();
		EmployeeInformation emp = new EmployeeInformation();
		emp.setStaffId(overtimeDTO.getStaffId());
		EmployeeInformation employee = employeeMapper.selectByStaffId(emp);
		if (employee == null){
			map.put("info", "员工不存在");
			return map;
		}
		Overtime overtime = new Overtime();
		// 设置默认的状态未审核，审核的不可能修改
		overtime.setAuditState(0);
		BeanUtils.copyProperties(overtimeDTO, overtime);
		boolean isSuccess = updateById(overtime);
		if (!isSuccess) map.put("info","修改失败");
		map.put("info","修改成功");
		return map;
	}

	/*
	   撤销加班申请
	 */
	@Override
	public HashMap<String, Object> del(int id) {
		HashMap<String,Object> map = new HashMap<>();
		Overtime overtime = getById(id);
		if (overtime == null) {
			map.put("info", "加班记录不存在");
			return map;
		}
		// 删除加班记录
		overtime.setApplyState(1);
		boolean isSuccess = updateById(overtime);
		if (!isSuccess) map.put("info","撤销失败");
		map.put("info","撤销成功");
		return map;
	}

	// 查询所有的加班记录
	@Override
	public HashMap<String, Object> queryAll(Overtime overtime) {
		HashMap<String, Object> map = new HashMap<>();
		Page<Overtime> page = new Page<>(overtime.getPage(), overtime.getRow());
		QueryWrapper<Overtime> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(overtime.getStaffName() != null && !overtime.getStaffName().equals(""), "staff_name",
				overtime.getStaffName());
		queryWrapper.like(overtime.getStaffDevelopment() != null && !overtime.getStaffDevelopment().equals(""), "staff_development", overtime.getStaffDevelopment());
		// 添加一个判断条件，撤销申请的不显示
		queryWrapper.eq("apply_state",0);
		queryWrapper.orderByAsc("audit_state");
		queryWrapper.orderByDesc("apply_time");
		Page<Overtime> aList = overtimeMapper.selectPage(page, queryWrapper);
		//当前页集合
		map.put("list", aList.getRecords());
		map.put("total", aList.getTotal());
		return map;
	}

	/**
	 * 撤销审核
	 * @param id 申请id
	 * @return
	 */
	@Override
	public HashMap<String, Object> updateState(int id) {
		HashMap<String,Object> map = new HashMap<>();
		// 将已审核改为未审核
		Overtime overtime = getById(id);
		if (overtime == null) {
			map.put("info", "加班记录不存在");
			return map;
		}
		overtime.setAuditState(0);
//		overtime .setAuditResult("已撤销");
		int num = overtimeMapper.updateById(overtime);
		EmployeeInformation employee = new EmployeeInformation();
		employee.setStaffId(overtime.getStaffId());
		EmployeeInformation emp = employeeMapper.selectByStaffId(employee);
		if (emp == null) {
			map.put("info", "员工不存在");
			return map;
		}
		if (emp.getOvertimeNumber() > 0)
		 emp.setOvertimeNumber(emp.getOvertimeNumber() - 1);
		if (num > 0) {
			map.put("info", "撤销审核成功");
		} else {
			map.put("info", "撤销审核失败");
		}
		return map;
	}

	/**
	 * 审核
	 * @param a 审核信息
	 * @return
	 */
	@Override
	public HashMap<String, Object> updateAndAdd(AuditOvertimeDTO a) {
		HashMap<String,Object> map = new HashMap<>();
		Overtime overtime = new Overtime();
		overtime.setId(a.getId());
		overtime.setAuditState(1);
		overtime.setAuditResult(a.getAuditResult());
		a.setId(null);
		int num = overtimeMapper.updateById(overtime);
		Audit audit = new Audit();
		BeanUtils.copyProperties(a, audit);
		int num2 = auditMapper.insert(audit);
		if (num > 0 && num2 > 0) map.put("info", "审核成功");
		else map.put("info", "审核失败");
		return map;
	}

	// 统计加班天数(所有的部门)
	@Override
	public HashMap<String, Object> selectMonth() throws ParseException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Integer> overtimeList = new ArrayList<>();

		for (int i = 1; i < 12; i++) {
			String c = (i <= 9) ? "2024-" + "0" + i + "-01" : "2024-" + i + "-01";
			String d = (i <= 8) ? "2024-" + "0" + (i + 1) + "-01" : "2024-" + (i + 1) + "-01";
			int n = overtimeMapper.selectMonth(c, d);
			overtimeList.add(n);
		}
		int n = overtimeMapper.selectMonth("2024-12-01", "2025-01-01");
		overtimeList.add(n);

		map.put("overtimeList", overtimeList);
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

	// 统计部门加班时长(按部门算)
	@Override
	public HashMap<String, Object> selectDept(String a) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Integer> DeptList = new ArrayList<>();

		for (int i = 1; i < 12; i++) {
			String c = (i <= 9) ? "2024-" + "0" + i + "-01" : "2024-" + i + "-01";
			String d = (i <= 8) ? "2024-" + "0" + (i + 1) + "-01" : "2024-" + (i + 1) + "-01";
			int n = overtimeMapper.selectDept(c, d, a);
			DeptList.add(n);
		}
		int n = overtimeMapper.selectDept("2024-12-01", "2025-01-01", a);
		DeptList.add(n);
		map.put("integerList", DeptList);
		return map;
	}


}
