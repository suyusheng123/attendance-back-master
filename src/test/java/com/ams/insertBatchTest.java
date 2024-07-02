package com.ams;

import com.ams.dao.OvertimeMapper;
import com.ams.pojo.Overtime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * ClassName:insertBatchTest
 * Package:com.ams
 * Description:
 *
 * @Author 卜翔威
 * @Create 2024/6/27 0:55
 * @Version 1.0
 */

@SpringBootTest(classes = AmsApp.class)
@RunWith(SpringRunner.class)//spring测试的运行器注解
// 测试数据生成器
public class insertBatchTest {
	@Resource
	public OvertimeMapper overtimeMapper;


	@Test
	public void insertData() {
		Overtime overtime = new Overtime();
		String[] staffId = new String[]{
				"6333633", "6333622", "6546454", "4260053", "7737427", "6787678", "7155646", "8082441",
				"1669246", "9955332"
		};
		String[] staffName = new String[]{
				"admin", "小白", "翠花", "曾琪淋", "刘云峰", "蔡徐坤", "骚平安", "猪骚被",
				"骚男尼古斯丁", "猪男男"
		};
		String[] department = new String[]{
				"人事部", "人事部", "人事部", "董事会", "董事会", "销售部", "人事部", "研发部",
				"就业部", "就业部"
		};
		for (int month = 1; month <= 12; month++) {
			for (int day = 1; day <= 28; day++) {
				// 创造出0-9的随机数，用于随机选择staffId
				int index = (int) (Math.random() * 10);
				overtime.setStaffId(staffId[index]);
				overtime.setStaffName(staffName[index]);
				overtime.setStaffDevelopment(department[index]);
				LocalDateTime overBegin = LocalDateTime.of(LocalDateTime.now().getYear(), month, day, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), LocalDateTime.now().getSecond());
				LocalDateTime overEnd = LocalDateTime.of(LocalDateTime.now().getYear(), month, day, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute() + 10, LocalDateTime.now().getSecond());
				overtime.setApplyTime(overBegin);
				overtime.setOverBegin(overBegin);
				overtime.setOverEnd(overEnd); // Assuming overEnd is the same as overBegin
				overtime.setAuditState(1);
				overtime.setAuditResult("通过");
				overtime.setApplyState(0);
				overtimeMapper.insert(overtime);
			}
		}
	}

}

