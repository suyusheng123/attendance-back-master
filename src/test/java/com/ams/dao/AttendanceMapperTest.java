package com.ams.dao;

import com.ams.AmsApp;
import com.ams.pojo.Attendance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)//spring测试的运行器注解
@SpringBootTest(classes = AmsApp.class)//springboot测试类在注解
public class AttendanceMapperTest {
    @Autowired(required = false)
    AttendanceMapper attendanceMapper;

    @Test
    public void selectByStaffId() {
        Attendance a = new Attendance();
        a.setStaffId("6333622");
        a.setClockDate("2021-11-16");
        Attendance attendance = attendanceMapper.selectByStaffId(a);
        System.out.println(attendance);
    }
}