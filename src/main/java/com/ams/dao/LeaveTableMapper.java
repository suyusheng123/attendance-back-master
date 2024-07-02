package com.ams.dao;

import com.ams.pojo.LeaveTable;
import com.ams.pojo.QingjiaMax;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 差假表 Mapper 接口
 */
public interface LeaveTableMapper extends BaseMapper<LeaveTable> {

	int selectbyAllQingjia(String a, String b);

	int selectbyAllBumenQingjia(String c, String d, String a);

	int selectbyAllChuchai(String s, String s1);

	int selectbyAllBumenChuchai(String s, String s1, String a);


	int selectbyAllKaoqing(String c, String d, String e);

	int selectbyAllKaoqingBumen(String c, String d, String e, String a);

	List<LeaveTable> selectToDay();


	ArrayList<QingjiaMax> selectQingjiaMax(String a, String b);

	ArrayList<QingjiaMax> selectChidaoMax(String a, String b, String c);

	ArrayList<QingjiaMax> selectAllQingjiaMax();

	ArrayList<QingjiaMax> selectAllChidaoMax(String a);


	int selectBeginEnd(String staffId, String nowDate);
}
