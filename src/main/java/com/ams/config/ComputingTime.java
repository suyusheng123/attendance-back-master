package com.ams.config;

import com.ams.dao.AttendanceMapper;
import com.ams.dao.SysDeptMapper;
import com.ams.pojo.Attendance;
import com.ams.pojo.SysDept;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComputingTime {

    public String ctime(String up, String clock) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        /*计算迟到时间*/
        Date upTime = sdf.parse(up);
        Date clockTime = sdf.parse(clock);

        long upTimeSeconds = upTime.getTime();
        long clockTimeSeconds = clockTime.getTime();

        long timeDiffer = upTimeSeconds - clockTimeSeconds;

        int hours = (int) (timeDiffer / 1000) % (3600 * 24) / 3600;
        int hours_remains = (int) (timeDiffer / 1000) % (3600 * 24) % 3600;

        int minutes = hours_remains / 60;
        String outTime = ((hours < 10) ? "0" : "") + hours + "小时" +
                ((minutes < 10) ? "0" : "") + minutes + "分";
        return outTime;
    }
}
