package com.ams.dao;

import com.ams.AmsApp;
import com.ams.pojo.RolePowerRef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by lk on $(DATE)
 */
@RunWith(SpringRunner.class)//spring测试的运行器注解
@SpringBootTest(classes = AmsApp.class)//springboot测试类在注解
public class RoleDeptRefMapperTest {

	@Autowired(required = false)
	private RolePowerRefMapper roleDeptRefMapper;
	@Test
	public void test() {
		List<RolePowerRef> roleDeptRefs = roleDeptRefMapper.selectList(null);
		for (RolePowerRef roleDeptRef : roleDeptRefs) {
			System.out.println(roleDeptRef);
		}

	}
}