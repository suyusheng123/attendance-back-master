package com.ams.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色中间表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserRoleRef implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "user_role_id", type = IdType.AUTO)
    private Integer userRoleId;

    private Integer userId;

    private Integer roleId;


}
