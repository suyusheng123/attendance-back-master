package com.ams.pojo;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * 返回json类
 */
@Data
public class ResultJson {
	private  String name;
	private T value;
}
