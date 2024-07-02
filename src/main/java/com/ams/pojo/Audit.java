package com.ams.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 领导审核表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Audit implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 审批人id
     */
    private Integer approverId;

    /**
     * 员工编号
     */
    private String staffId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 类型（请假或者出差）
     */
    private String leaveType;

    /**
     * 审核部门
     */
    private String approverDepartment;

    /**
     * 审核意见或原因
     */
    private String approverSuggestion;

    /**
     * 审核结果
     */
    private String auditResult;


}
