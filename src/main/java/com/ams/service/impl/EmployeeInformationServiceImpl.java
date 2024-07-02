package com.ams.service.impl;

import com.ams.dao.*;
import com.ams.pojo.*;
import com.ams.service.EmployeeInformationService;
import com.ams.utils.MdFive;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QuerydslRepositoryInvokerAdapter;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.*;

/**
 * <p>
 * 员工信息表 服务实现类
 */
@Service
public class EmployeeInformationServiceImpl implements EmployeeInformationService {
	@Autowired(required = false)
	private EmployeeInformationMapper employeeInformationMapper;
	@Autowired(required = false)
	SysDeptMapper sysDeptMapper;
	@Autowired(required = false)
	SysRoleMapper sysRoleMapper;
	@Autowired(required = false)
	SysUserMapper sysUserMapper;
	@Autowired(required = false)
	private MdFive mdFive;
	@Autowired(required = false)
	UserRoleRefMapper userRoleRefMapper;

	static String username = "user";

	@Override
	public boolean addname(EmployeeInformation a) {

		return employeeInformationMapper.insert(a) > 0;
	}


	@Override
	public HashMap<String, Object> select(EmployeeInformation a) throws ParseException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//定义分页对象
		Page<EmployeeInformation> page = new Page<>(a.getPage(), a.getRow());

		//条件构造器
		QueryWrapper<EmployeeInformation> queryWrapper = new QueryWrapper<>();

		queryWrapper.like(a.getStaffId() != null && !a.getStaffId().equals(""), "staff_id", a.getStaffId());
		queryWrapper.like(a.getStaffName() != null && !a.getStaffName().equals(""), "staff_name", a.getStaffName());
		queryWrapper.eq(a.getDepartment() != null && !a.getDepartment().equals(""), "department", a.getDepartment());
		queryWrapper.orderByDesc("id");
		//查询数据库
		Page<EmployeeInformation> aList = employeeInformationMapper.selectPage(page, queryWrapper);
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

		//得到部门列表
		List<SysDept> sysDepts = sysDeptMapper.selectList(null);
		//创建一个集合对象
		SysDept sysDept1 = new SysDept();
		sysDept1.setDeptName("");
		sysDept1.setDownTime("17:10");
		sysDept1.setUpTime("08:20");
		sysDept1.setDeptId(1);
		sysDepts.add(0, sysDept1);

		map.put("deptNames", sysDepts);

		for (SysDept sysDept : sysDepts) {
			System.out.println(sysDept);
		}

		//得到角色列表
		List<SysRole> sysRoles = sysRoleMapper.selectList(null);
		map.put("sysRoles", sysRoles);

		return map;

	}

	@Override
	public HashMap<String, Object> add(EmployeeInformation a,SysUser b) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//默认次数赋值
		a.setLeaveTime(10);
		a.setEarlyTime(10);
		a.setLateTime(10);

		// 先去员工表里查询电话号码是否重复
		QueryWrapper<EmployeeInformation> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("staff_phone", a.getStaffPhone());
		List<EmployeeInformation> list = employeeInformationMapper.selectList(queryWrapper);
		if (list.size() > 1) {
			map.put("info", "电话号码重复，请退出重新新增一个！");
			return map;
		}
		//int staffId = Integer.parseInt(a.getStaffId());
		//创建sysusr对象
		SysUser user = new SysUser();
		if (a.getStaffId().equals("")) {
			//生成随机员工编号
			String randomNum = getRandomNum();
			//判重
			List<EmployeeInformation> emps = employeeInformationMapper.selectList(null);
			for (EmployeeInformation emp : emps) {

				if (emp.getStaffId().equals(randomNum)) {
					map.put("info", "员工编号重复，请退出重新新增一个！");
				} else {
					//新增员工编号
					a.setStaffId(randomNum);
				}
			}

			//获取盐值
			String salt = UUID.randomUUID() + "";
			user.setSalt(salt);
			//加密密码
			String p = mdFive.encrypt(a.getStaffPwd(), user.getSalt());
			user.setUserPwd(p);
			a.setStaffPwd(p);

				//添加员工信息到sysusr
				int ss = new Random().nextInt(100);
				user.setUserName(username + ss);

			user.setEmail(a.getStaffEmail());
			//获得员工编号
			int staffId = Integer.parseInt(a.getStaffId());
			user.setStaffId(staffId);
			//向sysuser表添加一条数据
		} else {
			user.setStaffId(Integer.parseInt(a.getStaffId()));
			user.setUserName(b.getUserName());
			user.setUserPwd(b.getUserPwd());
			user.setEmail(b.getEmail());
			user.setSalt(b.getSalt());
			//向sysuser表添加一条数据
		}

		user.setState("0");
		sysUserMapper.insert(user);


		//给新增用户赋予角色
		//得到角色列表
		List<SysRole> sysRoles = sysRoleMapper.selectList(null);
		//通过员工编号拿到user表的ID；
		SysUser sysUser = sysUserMapper.selectIdByStaffId(user.getStaffId());
		int userId = sysUser.getUserId();
		for (SysRole sysRole : sysRoles) {
			//循环判断，如果系统角色相等，则添加到用户权限表中
			if (sysRole.getSysRoleName().equals(a.getSysRole())) {
				SysRole sysRole1 = sysRoleMapper.selectIdBySysRoleName(a.getSysRole());
				int roleId = sysRole1.getRoleId();
				UserRoleRef userRoleRef = new UserRoleRef();
				userRoleRef.setUserId(userId);
				userRoleRef.setRoleId(roleId);
				userRoleRefMapper.insert(userRoleRef);
			}
		}

		//新增
		int num = employeeInformationMapper.insert(a);
		if (num > 0) {
			map.put("info", "保存成功");
		} else {
			map.put("info", "保存错误");
		}
		return map;
	}

	@Override
	public HashMap<String, Object> update(EmployeeInformation a) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		// 先去员工表里查询电话号码是否重复
		QueryWrapper<EmployeeInformation> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("staff_phone", a.getStaffPhone());
		// 查询排除自己
		queryWrapper.ne("staff_id",a.getStaffId());
		List<EmployeeInformation> list = employeeInformationMapper.selectList(queryWrapper);
		if (!list.isEmpty()) {
			map.put("info", "电话号码重复，请退出重新新增一个！");
			return map;
		}
		//先通过查询的员工编号，找到对应uesrID
		int sid = Integer.parseInt(a.getStaffId());
		SysUser sysUser = sysUserMapper.selectIdByStaffId(sid);
		int userId = sysUser.getUserId();
		//删除已有的这条记录。
		userRoleRefMapper.delectByUserId(userId);
		//得到角色列表
		List<SysRole> sysRoles = sysRoleMapper.selectList(null);
		for (SysRole sysRole : sysRoles) {
			//循环判断，如果系统角色相等，则添加到用户权限表中
			if (sysRole.getSysRoleName().equals(a.getSysRole())) {
				SysRole sysRole1 = sysRoleMapper.selectIdBySysRoleName(a.getSysRole());
				int roleId = sysRole1.getRoleId();
				UserRoleRef userRoleRef = new UserRoleRef();
				userRoleRef.setUserId(userId);
				userRoleRef.setRoleId(roleId);
				userRoleRefMapper.insert(userRoleRef);
			}
		}


		//修改
		int num = employeeInformationMapper.updateById(a);
		if (num > 0) {
			map.put("info", "保存成功");
		} else {
			map.put("info", "保存错误");
		}
		return map;
	}

	@Override
	public HashMap<String, Object> del(EmployeeInformation a) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		int staffId = Integer.parseInt(a.getStaffId());
		//级联删除,删除user_role_ref表中的数据
		SysUser sysUser = sysUserMapper.selectIdByStaffId(staffId);
		int userId = sysUser.getUserId();
		//删除已有的这条记录。
		userRoleRefMapper.delectByUserId(userId);
		//联级删除，把sysuser表中对应的数据也删除
		sysUserMapper.delectByStaffId(staffId);


		//删除
		int num = employeeInformationMapper.deleteById(a);
		if (num > 0) {
			map.put("info", "保存成功");
		} else {
			map.put("info", "保存错误");
		}
		return map;
	}

	//生成随机数（员工编号）
	private static String getRandomNum() {
		SecureRandom random = new SecureRandom();
		StringBuilder str = new StringBuilder();//定义可变字符串
		//随机生成数字，并添加到字符串
		for (int i = 0; i < 7; i++) {
			str.append(random.nextInt(10));
		}
		//将字符串转换为数字并输出
		return String.valueOf(str);
	}

	@Override
	public boolean updatePwdByStaffId(EmployeeInformation a) {
		return employeeInformationMapper.updatePwdByStaffId(a);
	}

	@Override
	public EmployeeInformation selectByStaffId(EmployeeInformation emp) {
		return employeeInformationMapper.selectByStaffId(emp);
	}
}
