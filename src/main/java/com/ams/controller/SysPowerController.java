package com.ams.controller;


import com.ams.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 角色表
 */
@RestController
@RequestMapping("/sys-power")
public class SysPowerController {
	@Autowired(required = false)
	NodeService nodeService;
	//查询权限
	@GetMapping("/selectPower")
	public HashMap<String,Object> selectPower(String userName){

		return nodeService.selectPower(userName);
	}
	//查询所有权限
	@RequestMapping("/selectAllPower")
	public HashMap<String,Object> selectAllPower(){

		return nodeService.selectAllPower();
	}
	//查询已选中的权限
	@RequestMapping("/selectPowerRoleId")
	public HashMap<String,Object> selectPowerRoleId(Integer roleId){
		return nodeService.selectPowerRoleId(roleId);
	}
}

