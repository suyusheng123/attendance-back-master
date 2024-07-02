package com.ams.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmployeeInformation extends BasePage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id自动递增，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工编号
     */
    private String staffId;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     * 员工性别
     */
    private String staffSex;

    /**
     * 密码
     */
    private String staffPwd;

    /**
     * 部门
     */
    private String department;

    /**
     * 系统角色
     */
    private String sysRole;

    /**
     * 员工电话
     */
    private String staffPhone;

    /**
     * 员工邮箱
     */
    private String staffEmail;

    /**
     * 请假次数
     */
    private Integer leaveTime;

    /**
     * 迟到次数
     */
    private Integer lateTime;

    /**
     * 早退次数
     */
    private Integer earlyTime;

    /**
     * 加班次数
     */

    private int overtimeNumber;


}
