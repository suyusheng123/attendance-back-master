package com.ams.controller;


import com.ams.pojo.EmployeeInformation;
import com.ams.pojo.SysUser;
import com.ams.service.EmployeeInformationService;
import org.apache.xmlbeans.impl.jam.mutable.MPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;

/**
 * 员工信息表
 */
@RestController
@RequestMapping("/employee")
public class EmployeeInformationController {

    @Autowired
    EmployeeInformationService employeeInformationService;

    //加载表格数据
    @RequestMapping("/list")
    public HashMap<String,Object> listPage(EmployeeInformation e) throws ParseException {
        return employeeInformationService.select(e);
    }
    //修改
    @RequestMapping("/update")
    public HashMap<String,Object>  update(EmployeeInformation e){
        return employeeInformationService.update(e);
    }
    //新增
    @RequestMapping("/add")
    public HashMap<String,Object> add(EmployeeInformation e){
        return employeeInformationService.add(e,new SysUser());
    }

    //删除
    @RequestMapping("/del")
    public HashMap<String,Object> del(EmployeeInformation e){
        return employeeInformationService.del(e);
    }
    //查询单个用户信息
    @RequestMapping("/queryOne")
    public HashMap<String,Object> queryOne(EmployeeInformation e){
        HashMap<String, Object> map = new HashMap<>();

        EmployeeInformation res = employeeInformationService.selectByStaffId(e);
        if (res != null) {
            map.put("info", "保存成功");
            map.put("data", res);
        } else {
            map.put("info", "保存失败");
        }
        return map;
    }
}


