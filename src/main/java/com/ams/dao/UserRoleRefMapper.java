package com.ams.dao;

import com.ams.pojo.UserRoleRef;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * 用户角色中间表 Mapper 接口
 */
public interface UserRoleRefMapper extends BaseMapper<UserRoleRef> {
    @Delete("delete from user_role_ref where user_id=#{userId}")
    int delectByUserId(Integer userId);

}
