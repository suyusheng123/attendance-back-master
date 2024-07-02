package com.ams.controller;


import com.ams.pojo.Audit;
import com.ams.pojo.LeaveTable;
import com.ams.service.LeaveTableService;
import org.springframework.beans.factory.annotation.Autowired;
import com.ams.pojo.EmployeeInformation;
import com.ams.pojo.LeaveTable;
import com.ams.pojo.SysDept;
import com.ams.service.LeaveTableService;
import com.ams.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import java.text.ParseException;
import java.util.HashMap;

/**
 * 差假表
 */
@RestController
@RequestMapping("/leaveTable")
public class LeaveTableController {
	@Autowired
	LeaveTableService leaveTableService;
    //查询请假列表（未审核在前，根据时间排序）
	//todo 暂时不知道这两个接口是干什么用的
	@RequestMapping("/selectByTypeLeave")
    public HashMap<String, Object> selectByTypeLeave(LeaveTable leave) {
		return leaveTableService.selectByTypeLeave(leave);
	}

    //查询出差列表（未审核在前，根据时间排序
	@RequestMapping("/selectByTypeBusiness")
    public HashMap<String, Object> selectByTypeBusiness(LeaveTable leave) {
		return leaveTableService.selectByTypeBusiness(leave);
	}


    //通过id查询请假列表
	@RequestMapping("/selectByIdLeave")
    public HashMap<String, Object> selectByIdLeave(LeaveTable leave) {
		return leaveTableService.selectByIdLeave(leave);
	}


    //通过id查询出差列表
	@RequestMapping("/selectByIdBusiness")
    public HashMap<String, Object> selectByIdBusiness(LeaveTable leave) {
		return leaveTableService.selectByIdBusiness(leave);
	}


    //新增操作
	@RequestMapping("/add")
    public HashMap<String, Object> add(LeaveTable leave) {
		return leaveTableService.add(leave);
	}

    //删除操作
	@RequestMapping("/del")
    public HashMap<String, Object> del(LeaveTable leave) {
		return leaveTableService.del(leave);
	}

    //修改操作
	@RequestMapping("/update")
    public HashMap<String, Object> update(LeaveTable leave) {
		return leaveTableService.update(leave);
	}


	// 审核记录
    //查询所有并排序（未审核在前，根据时间排序)
	@RequestMapping("/selectAllLeave")
    public HashMap<String, Object> selectAllLeave(LeaveTable leave) {
		return leaveTableService.selectAllLeave(leave);
	}


    //查询所有并排序（未审核在前，根据时间排序)
	@RequestMapping("/selectAllBusiness")
    public HashMap<String, Object> selectAllBusiness(LeaveTable leave) {
		return leaveTableService.selectAllBusiness(leave);
	}


    //模糊查询（根据部门或者姓名进行查询，未审核在前，根据时间排序）
	@RequestMapping("/updateAndAdd")
    public HashMap<String, Object> updateAndAdd(Audit a) {
		System.out.println("========================" + a);
		return leaveTableService.updateAndAdd(a);
	}

	@RequestMapping("/updatesState")
    public HashMap<String, Object> updatesState(LeaveTable leaveTable) {
		return leaveTableService.updatesState(leaveTable);
	}


	//加载表格数据
	@RequestMapping("/listAllQing")
	public HashMap<String, Object> listAllQing() throws ParseException {

		return leaveTableService.selectMonth();
	}

	@RequestMapping("/listBumen")
	public HashMap<String, Object> listBumen(@RequestBody String xuanBumenName) throws ParseException {
		if (xuanBumenName.equals("全部部门")) {
			return leaveTableService.selectMonth();
		} else {

			return leaveTableService.selectBumen(xuanBumenName);
		}
	}

	//++++++++++++++++++++++++++++
	@RequestMapping("/listAllchu")
	public HashMap<String, Object> listAllchu() throws ParseException {

		return leaveTableService.selectChuchaiMonth();
	}

	@RequestMapping("/listBumenChu")
	public HashMap<String, Object> listBumenChu(@RequestBody String xuanBumenName) throws ParseException {
		if (xuanBumenName.equals("全部部门")) {
			return leaveTableService.selectChuchaiMonth();
		} else {

			return leaveTableService.selectChuchaiBumen(xuanBumenName);
		}
	}

	//    考勤============================================
	@RequestMapping("/listAllKaoqing")
	public HashMap<String, Object> listAllKaoqing() throws ParseException {

		return leaveTableService.selectKaoqingMonth();
	}

	@RequestMapping("/listBumenKaoqing")
	public HashMap<String, Object> listBumenKaoqing(@RequestBody String xuanBumenName) throws ParseException {
		if (xuanBumenName.equals("全部部门")) {
			return leaveTableService.selectKaoqingMonth();
		} else {

			return leaveTableService.selectKaoqingBumen(xuanBumenName);
		}
	}
//++++++++++++每月最大的

	//最大请假，早退，迟到+++++++++++++++++++

	@RequestMapping("/listMaxqingjia")
	public HashMap<String,Object> listMaxqingjia() {

		return leaveTableService.selectAllMonth();
	}
	//最大请假，早退，迟到+++++++++++++++++++按月
	@RequestMapping("/listMaxaaaqingjia")
	public HashMap<String,Object> listaaaMax(@RequestBody String xuanBumenName) {
		if (xuanBumenName.equals("整年")){
			return leaveTableService.selectAllMonth();
		}else {

			return leaveTableService.selectAllaaaMonth(xuanBumenName);
		}
	}



	//+++++++++++++++++++++++++每月最大的结束
}

