package com.ams.service.impl;

import com.ams.dao.AttendanceMapper;
import com.ams.pojo.Attendance;
import com.ams.pojo.SysDept;
import com.ams.dao.SysDeptMapper;
import com.ams.service.SysDeptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 员工部门表 服务实现类
 */
@Service
public class SysDeptServiceImpl  implements SysDeptService {
	@Autowired(required = false)
	SysDeptMapper sysDeptMapper;

	// 考勤表
	@Resource
	private AttendanceMapper attendanceMapper;

	@Override
	public HashMap<String, Object> select(SysDept a) {
		HashMap<String, Object> map = new HashMap<>();

		//定义分页对象
		Page<SysDept> page = new Page<>(a.getPage(), a.getRow());
		QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
		//编号查询
		queryWrapper.eq(a.getDeptId() != null, "dept_id", a.getDeptId());
		//Page<AttenManage> aList = attenManageMapper.selectByCon(page, a);

		//用户名的模糊查询
		queryWrapper.like(!StringUtils.isEmpty(a.getDeptName()) && a.getDeptName() != null, "dept_name",
				a.getDeptName());
		//查询数据库
		Page<SysDept> aList = sysDeptMapper.selectPage(page, queryWrapper);
		//当前页集合
		map.put("list", aList.getRecords());
		//总页数
		map.put("totalPage", aList.getPages());
		//总条数
		map.put("total", aList.getTotal());
		//当前页
		map.put("curPage", a.getPage());
		if (a.getPage() == 1) {
			//上一页
			map.put("prePage", 1);

		} else {
			map.put("prePage", a.getPage() - 1);

		}
		if (a.getPage() == aList.getPages()) {
			//下一页
			map.put("nextPage", aList.getPages());

		} else {
			map.put("nextPage", a.getPage() + 1);

		}
		//每页显示条数
		map.put("row", a.getRow());
		return map;
	}

	@Override
	public HashMap<String, Object> add(SysDept a) {
		HashMap<String, Object> map = new HashMap<>();

		String upTime = a.getUpTime();
		String downTime = a.getDownTime();
		// 这里限定部门的上班时间和下班时间，时间间隔为至少20min，否则不允许插入

		// 转换为 LocalTime 类型
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime upTimeLocal = LocalTime.parse(upTime, formatter);
		LocalTime downTimeLocal = LocalTime.parse(downTime, formatter);

		// 计算时间差
		long minutesBetween = ChronoUnit.MINUTES.between(upTimeLocal, downTimeLocal);

		// 检查时间差是否小于20分钟
		if (minutesBetween < 20) {
			map.put("info", "上下班时间至少相差20min，时间设置不对");
			return map;
		}

		int num = sysDeptMapper.insert(a);
		if (num > 0) {
			map.put("info", "保存成功");
		} else {
			map.put("info", "保存失败");
		}
		return map;
	}

	@Override
	public HashMap<String, Object> del(SysDept a) {
		HashMap<String, Object> map = new HashMap<>();

		int num = sysDeptMapper.deleteById(a);
		if (num > 0) {
			map.put("info", "保存成功");
		} else {
			map.put("info", "保存失败");
		}
		return map;
	}

	@Override
	public HashMap<String, Object> update(SysDept a) {
		HashMap<String, Object> map = new HashMap<>();

		String upTime = a.getUpTime();
		String downTime = a.getDownTime();
		// 转换为 LocalTime 类型
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime upTimeLocal = LocalTime.parse(upTime, formatter);
		LocalTime downTimeLocal = LocalTime.parse(downTime, formatter);

		// 计算时间差
		long minutesBetween = ChronoUnit.MINUTES.between(upTimeLocal, downTimeLocal);

		// 检查时间差是否小于20分钟
		if (minutesBetween < 20) {
			map.put("info", "上下班时间至少相差20min，时间设置不对");
			return map;
		}


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 更新考勤表
		QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("department", a.getDeptName());
		queryWrapper.eq("clock_date", sdf.format(new Date()));
		attendanceMapper.delete(queryWrapper);

		int num = sysDeptMapper.updateById(a);

		if (num > 0) {
			map.put("info", "保存成功");
		} else {
			map.put("info", "保存失败");
		}
		return map;
	}

	// 查询部门信息
	@Override
	public HashMap<String, Object> queryDept(String deptName) {
		HashMap<String, Object> map = new HashMap<>();
		QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dept_name", deptName);
		SysDept dept = sysDeptMapper.selectOne(queryWrapper);
		if (dept == null) {
			map.put("info", "部门信息不存在");
		}
		map.put("dept", dept);
		map.put("info", "查询成功");
		return map;
	}

	@Override
	public HashMap<String, Object> selectAll() {

		HashMap<String, Object> map = new HashMap<>();

		List<SysDept> sysDepts = sysDeptMapper.selectList(null);
		if (sysDepts.size() > 0) {
			map.put("info", "保存成功");
			map.put("list", sysDepts);
		} else {
			map.put("info", "保存失败");
		}
		return map;
	}
}
