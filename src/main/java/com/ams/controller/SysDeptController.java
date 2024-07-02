package com.ams.controller;


import com.ams.dto.SysDeptDTO;
import com.ams.pojo.SysDept;
import com.ams.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 员工部门表
 */
@RestController
@RequestMapping("/sys-dept")
public class SysDeptController {
	@Autowired
	SysDeptService sysDeptService;

	@RequestMapping("/list")
	public HashMap<String, Object> list(SysDept a) {
		return sysDeptService.select(a);
	}

	@RequestMapping("/listAll")
	public HashMap<String, Object> listAll( ) {
		return sysDeptService.selectAll();
	}
	//添加
	@RequestMapping("/add")
	public HashMap<String, Object> add(SysDept a) {
		return sysDeptService.add(a);
	}

	//更新
	@RequestMapping("/update")
	public HashMap<String, Object> update(SysDept a) {
		return sysDeptService.update(a);
	}

	//删除
	@RequestMapping("/del")
	public HashMap<String, Object> del(SysDept a) {
		return sysDeptService.del(a);
	}

	// 查询部门的上下班时间
	@GetMapping("/queryDept")
	public HashMap<String,Object> queryDept(@RequestParam(value = "deptName") String deptName){
		return sysDeptService.queryDept(deptName);
	}
}

