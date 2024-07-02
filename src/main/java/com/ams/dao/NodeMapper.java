package com.ams.dao;

import com.ams.pojo.Node;

import java.util.List;



public interface NodeMapper {
	List<Node> selectPower(String userName);
	List<Node> selectPowerRoleId(Integer roleId);

	List<Node> selectAllPower();
}
