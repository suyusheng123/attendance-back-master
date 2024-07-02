package com.ams.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Chuchai {
    private int value;
    private String name;
}
