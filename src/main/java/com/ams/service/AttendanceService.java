package com.ams.service;

import com.ams.pojo.Attendance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.Map;

/**
 * 考勤表 服务类
 *
 */
public interface AttendanceService {
    Map<String,Object> selectList(Attendance attendance);

    Map<String, Object> signin(Attendance a) throws ParseException;

    Map<String, Object> signout(Attendance a) throws ParseException;
}
