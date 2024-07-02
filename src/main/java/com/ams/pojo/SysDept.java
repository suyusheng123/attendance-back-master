package com.ams.pojo;

import com.baomidou.mybatisplus.annotation.IdType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工部门表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDept extends BasePage implements Serializable {
	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	private static final long serialVersionUID = 1L;

	/**
	 * 部门id
	 */
	@TableId(value = "dept_id", type = IdType.AUTO)
	private Integer deptId;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 上班时间
	 */
	private Date upTime;

	/**
	 * 下班时间
	 */
	private Date downTime;

	public String getUpTime() {
		return sdf.format(upTime);
	}

	public void setUpTime(String upTime) throws ParseException {
        if (upTime != null) {
            this.upTime = sdf.parse(upTime);
        } else {
            this.upTime = new Date();
        }

	}

	public String getDownTime() {
        return sdf.format(downTime);
	}

	public void setDownTime(String downTime) throws ParseException {
        if (downTime != null) {
            this.downTime = sdf.parse(downTime);
        } else {
            this.downTime = new Date();
        }

    }
}
