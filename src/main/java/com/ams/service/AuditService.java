package com.ams.service;

import com.ams.pojo.Audit;
import com.ams.pojo.LeaveTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 * 领导审核表 服务类
 */
public interface AuditService extends IService<Audit> {
    //新增数据
    HashMap<String,Object> add(Audit audit);
    //修改数据

}
