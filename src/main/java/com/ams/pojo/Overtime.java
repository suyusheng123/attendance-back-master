package com.ams.pojo;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 加班实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Overtime extends BasePage implements Serializable {

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
    private String staffDevelopment;

    /**
     * 申请时间

     */
    // todo 加班时间限定在今天加班，不能选择其他时间
    private LocalDateTime applyTime;

    /**
     * 加班开始时间
     */
    private LocalDateTime overBegin;

    /**
     * 加班结束时间
     */
    private LocalDateTime overEnd;

    /**
     * 审核状态
     */
    private Integer auditState;


    /**
     * 审核结果
     */
    private String auditResult;

    /**
     * 加班申请状态
     */
    private Integer applyState;


}
