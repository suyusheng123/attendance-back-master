package com.ams.service.impl;

import com.ams.dao.LeaveTableMapper;
import com.ams.pojo.Audit;
import com.ams.dao.AuditMapper;
import com.ams.pojo.LeaveTable;
import com.ams.service.AuditService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * <p>
 * 领导审核表 服务实现类
 */
@Service
public class AuditServiceImpl extends ServiceImpl<AuditMapper, Audit> implements AuditService {
@Autowired(required = false)
AuditMapper auditMapper;

@Autowired(required = false)
LeaveTableMapper leaveTableMapper;
    @Override
    public HashMap<String, Object> add(Audit audit) {
        HashMap<String,Object> map=new HashMap<>();


        int num=auditMapper.insert(audit);
        if (num>0){
            map.put("info","新增成功");
        }else {
            map.put("info","新增失败");
        }
        return map;
    }


}
