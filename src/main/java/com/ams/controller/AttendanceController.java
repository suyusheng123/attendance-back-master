package com.ams.controller;


import com.ams.pojo.Attendance;
import com.ams.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

/**
 * 考勤表
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;

    @RequestMapping("/attendceanList")
    public Map<String,Object> attendceList(Attendance a){
        return attendanceService.selectList(a);
    }

    @RequestMapping("/signin")
    public Map<String,Object> signin(Attendance a) throws ParseException {
        return attendanceService.signin(a);
    }

    @RequestMapping("/signout")
    public Map<String,Object> signout(Attendance a) throws ParseException {
        return attendanceService.signout(a);
    }

}

