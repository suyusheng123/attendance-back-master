package com.ams.service.impl;

import com.ams.dao.NodeMapper;
import com.ams.pojo.Node;
import com.ams.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class NodeServiceImpl implements NodeService {
	@Autowired(required = false)
	NodeMapper mapper;

	@Override
	public HashMap<String, Object> selectPower(String userName) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Node> list = mapper.selectPower(userName);
		List<Node> list1 = new ArrayList<>();
		if (list != null) {
			//构建权限菜单的数据结构
			for (Node n : list) {
				if (n.getParentId() == 0) {
					Node nn = new Node();
					nn.setUrl(n.getUrl());
					nn.setIndex(n.getIndex());
					nn.setName(n.getName());
					//创建一个集合
					List<Node> listChilder = new ArrayList<>();
					//查询子菜单
					for (Node m : list) {
						if (Objects.equals(m.getParentId(), n.getNodeId())) {
							listChilder.add(m);
						}
					}
					//判断节点是否存在子节点
					if (listChilder.size()!=0 ) {
						nn.setChildren(listChilder);
						list1.add(nn);
					}


				}
			}
		}

		map.put("info", list1);
		return map;
	}

	@Override
	public HashMap<String, Object> selectAllPower() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Node> list = mapper.selectAllPower();
		List<Node> res = new ArrayList<>();
		List<String> permissions = new ArrayList<>();

		if (list != null) {
			//构建权限菜单的数据结构
			for (Node n : list) {
				if (n.getParentId() == 0) {
					Node nn = new Node();
					nn.setUrl(n.getUrl());
					nn.setIndex(n.getIndex());
					nn.setName(n.getName());
					//创建一个集合
					List<Node> listChilder = new ArrayList<>();
					//查询子菜单
					for (Node m : list) {
						if (m.getParentId() == n.getNodeId()) {
							listChilder.add(m);
							permissions.add(m.getIndex());
						}
					}
					nn.setChildren(listChilder);
					res.add(nn);
				}
			}
		}

		map.put("info",res);
		map.put("permissions",permissions);
		return map;

	}

	@Override
	public HashMap<String, Object> selectPowerRoleId(Integer roleId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Node> list = mapper.selectPowerRoleId(roleId);
		List<Node> res = new ArrayList<>();
		List<String> permissions = new ArrayList<>();

		if (list != null) {
			//构建权限菜单的数据结构
			for (Node n : list) {
				if (n.getParentId() == 0) {
					Node nn = new Node();
					nn.setUrl(n.getUrl());
					nn.setIndex(n.getIndex());

					nn.setName(n.getName());
					//创建一个集合
					List<Node> listChilder = new ArrayList<>();
					//查询子菜单
					for (Node m : list) {
						if (m.getParentId() == n.getNodeId()) {
							listChilder.add(m);
							permissions.add(m.getIndex());
						}
					}
					nn.setChildren(listChilder);
					permissions.add(nn.getIndex());
					res.add(nn);
				}
			}
		}

		map.put("info",res);
		map.put("permissions",permissions);
		return map;

	}
}
