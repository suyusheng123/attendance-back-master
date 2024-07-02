package com.ams.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色权限中间表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RolePowerRef implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 角色权限id
     */
    @TableId(value = "role_power_id", type = IdType.AUTO)
    private Integer rolePowerId;

    /**
     * 权限id
     */
    private Integer powerIndex;

    /**
     * 角色id
     */
    private Integer roleId;


}
