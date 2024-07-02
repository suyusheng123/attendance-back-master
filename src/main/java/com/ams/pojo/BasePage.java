package com.ams.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 前端入参校验
 */
@Data
public class BasePage {
    //页码
    //exist = false 表示不配置这个属性和表的映射关系
    @TableField(exist = false)
    private Integer page = 1;
    //每页显示条数
    @TableField(exist = false)
    private Integer row =10;
    //查询名字
    @TableField(exist = false)
    private String name ="";
    //查询部门
    @TableField(exist = false)
    private String departmentSelect ="";
    //查询开始时间
    @TableField(exist = false)
    private String beginTime ="";
    //查询结束时间
    @TableField(exist = false)
    private String endTime ="";
    /*
    封装前端输入的验证码
    * */
    @TableField(exist = false)
    private String code;


    //修改密码时的确认密码
    @TableField(exist = false)
    private String surePassword;
}
