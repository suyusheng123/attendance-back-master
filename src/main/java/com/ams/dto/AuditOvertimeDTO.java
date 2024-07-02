package com.ams.dto;


import lombok.Data;
import org.apache.poi.util.Internal;

@Data
public class AuditOvertimeDTO {
	private Integer id;
	private Integer auditState;
	private String auditResult;
	private Integer approverId;
	private String approverName;
	private String approverDepartment;
	private String staffId;
	private String leaveType;
}
