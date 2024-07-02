package com.ams.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 员工部门表
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysPower implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 权限id
     */
    @TableId(value = "power_id", type = IdType.AUTO)
    private Integer powerId;

    /**
     * 菜单名称
     */
    private String powerName;

    /**
     * 菜单索引
     */
    private String powerIndex;

    /**
     * 菜单url
     */
    private String powerUrl;

    /**
     * 父节点id
     */
    private Integer parentId;

    /**
     * 子节点id
     */
    private Integer nodeId;


}
