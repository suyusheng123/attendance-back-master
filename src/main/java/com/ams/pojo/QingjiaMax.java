package com.ams.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请假最大天数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QingjiaMax {
    private String name;
    private int num;
}
