package com.ams.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser extends BasePage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户编号
     */
    private Integer staffId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 状态
     */
    private String state;

    /**
     * 邮箱
     */
    private String email;

    @TableField(exist = false)
    private String staffName;

    @TableField(exist = false)
    private String sex;

    @TableField(exist = false)
    private  String phone;

    //定义注册/修改时输入邮箱验证码
    @TableField(exist = false)
    private String emailCode;

    @TableField(exist = false)
    private String code;

}
