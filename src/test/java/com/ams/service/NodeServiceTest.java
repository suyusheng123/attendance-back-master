package com.ams.service;

import com.ams.AmsApp;
import com.ams.dao.NodeMapper;
import com.ams.pojo.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by lk on $(DATE)
 */
@RunWith(SpringRunner.class)//spring测试的运行器注解
@SpringBootTest(classes = AmsApp.class)//springboot测试类在注解
public class NodeServiceTest {


	@Autowired(required = false)
	NodeService nodeService;

	@Test
	public void selectPower() {

		HashMap<String, Object> map = nodeService.selectPower("root");
		for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
			System.out.println(stringObjectEntry.getKey()+" "+stringObjectEntry.getValue());
		}
	}
}