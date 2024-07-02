package com.ams.dao;

import com.ams.pojo.SysDept;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 员工部门表 Mapper 接口
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

    SysDept selectByTime(String department);

}
