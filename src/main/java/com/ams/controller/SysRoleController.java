package com.ams.controller;


import com.ams.pojo.SysRole;
import com.ams.service.RolePowerRefService;
import com.ams.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author LK
 * @since 2021-11-11
 */
@RestController
@RequestMapping("/sys-role")
public class SysRoleController {

	@Autowired
	SysRoleService sysRoleService;

	@Autowired
	RolePowerRefService rolePowerRefService;

	@RequestMapping("/list")
	public HashMap<String, Object> list(SysRole a) {
		return sysRoleService.select(a);
	}
	//添加
	@RequestMapping("/add")
	public HashMap<String, Object> add(SysRole a,@RequestParam(value = "checkedOption[]",
			                                                   required = false) int[] permissions) {

		return sysRoleService.add(a, permissions);
	}
	//更新
	@RequestMapping("/update")
	public HashMap<String, Object> update(SysRole a,@RequestParam(value = "checkedOption[]",
	                                                              required = false) int[] permissions) {
		System.out.println(a);
		System.out.println(Arrays.toString(permissions));

		return sysRoleService.update(a,permissions);
	}
	//删除
	@RequestMapping("/del")
	public HashMap<String, Object> del(SysRole a) {
		return sysRoleService.del(a);
	}
}

