package com.ams.controller;


import com.ams.dao.AuditMapper;
import com.ams.pojo.Audit;
import com.ams.pojo.LeaveTable;
import com.ams.service.AuditService;
import com.ams.service.LeaveTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 领导审核表
 */
@RestController
@RequestMapping("/audit")
public class AuditController {
    @Autowired
    AuditService auditService;
    @Autowired
    LeaveTableService leaveTableService;

    @RequestMapping("/add")
    public HashMap<String, Object> add(Audit audit){
        return auditService.add(audit);
    }

}

