package com.ams.dao;

import com.ams.pojo.Attendance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 考勤表 Mapper 接口
 */
public interface AttendanceMapper extends BaseMapper<Attendance> {


    Attendance selectByStaffId(Attendance a);

    List selectByStaffIdList(Attendance a);

    int selectIntime(String staffId,String nowDate);

    int selectOuttime(String staffId, String nowDate);

    boolean updateState(int i, String deptName);
}
