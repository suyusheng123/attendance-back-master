package com.ams.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 考勤表
 */
@Data
@EqualsAndHashCode(callSuper = false)

public class Attendance extends BasePage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 考勤表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工id
     */
    private String staffId;


    /**
     * 员工姓名
     */

    private String staffName;
    /**
     * 员工部门
     */
    private String department;

    /**
     * 上班打卡时间
     */
    private String clockInTime;

    /**
     * 下班打卡时间
     */
    private String clockOutTime;

    /**
     * 打卡日期
     */
    private String clockDate;

    /**
     * 备注（迟到、早退）
     */
    private String remarks;


    /**
     * 是否迟到(1表示迟到。0表示未迟到)
     */
    private int inTimeState;
    /**
     * 是否早退(1表示早退。0表示未早退)
     */
    private int outTimeState;


}
