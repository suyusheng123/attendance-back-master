package com.ams.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 差假表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LeaveTable extends BasePage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
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
     * 员工部门
     */
    private String staffDepartment;

    /**
     * 类型（请假或者出差）
     */
    private String leaveType;

    /**
     * 交通工具
     */
    private String travelVehicle;

    /**
     * 预支费用
     */
    private String prepaidExpenses;

    /**
     * 请假时间
     */
    private String leaveTime;

    /**
     * 请假起始时间
     */
    private String leaveBeginTime;

    /**
     * 请假结束时间
     */
    private String leaveEndTime;

    /**
     * 请假事由
     */
    private String leaveReason;

    /**
     * 审核状态
     */
    private Integer auditState;


    /**
     * 审核意见或原因
     */
    private String approverSuggestion;

    /**
     * 审核结果
     */
    private String auditResult;


}
