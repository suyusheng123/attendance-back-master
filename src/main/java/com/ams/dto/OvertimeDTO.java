package com.ams.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OvertimeDTO implements Serializable {
	private static final long serialVersionUID=1L;

	private String staffId;
	private String staffName;
	// json格式化

	private LocalDateTime applyTime;

	private LocalDateTime overBegin;

	private LocalDateTime overEnd;
	// 部门
	private String staffDevelopment;

	// id 修改查询
	private Integer id;

	private String auditState;

	private String auditResult;


}
