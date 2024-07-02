package com.ams.controller;


import com.ams.dto.AuditOvertimeDTO;
import com.ams.dto.OvertimeDTO;
import com.ams.pojo.Audit;
import com.ams.pojo.LeaveTable;
import com.ams.pojo.Overtime;
import com.ams.service.IOvertimeService;
import com.ams.service.impl.OvertimeServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.HashMap;


/**
 * 加班记录表
 */
@RestController
@RequestMapping("/overtime")
public class OvertimeController {

	@Resource
	private IOvertimeService iovertimeService;

	/**
	 * 分页查询加班列表(查询自己的)
	 */
	@GetMapping("/queryOverTime")
	public HashMap<String, Object> queryOvertime(Overtime overtime) {
		return iovertimeService.queryOvertime(overtime);
	}

	/**
	 * 加班申请
	 */
    @PostMapping("/add")
    public HashMap<String, Object> add(@RequestBody OvertimeDTO overtimeDTO) {
	    return iovertimeService.add(overtimeDTO);
    }

	/**
	 * 修改加班申请
	 */
	@PostMapping("/update")
	public HashMap<String, Object> updateOvertime(@RequestBody OvertimeDTO overtimeDTO) {
		return iovertimeService.updateOvertime(overtimeDTO);
	}

	/**
	 * 撤销加班申请
	 */
	@GetMapping("/del")
	public HashMap<String, Object> del(@RequestParam(value = "id") int id) {
		return iovertimeService.del(id);
	}

	// 审核记录模块
	/**
	 * 查询所有的加班记录
	 */
    @GetMapping("/queryAll")
	public HashMap<String, Object> queryAll(Overtime overtime) {
		return iovertimeService.queryAll(overtime);
	}

	/**
	 * 撤销审核
	 */
   @GetMapping("/updateState")
	public HashMap<String, Object> updateState(@RequestParam(value = "id") int id) {
	   return iovertimeService.updateState(id);
   }

	/**
	 * 审核
	 */
	@PostMapping("/updateAndAdd")
	public HashMap<String, Object> updateAndAdd(@RequestBody AuditOvertimeDTO a) {
		return iovertimeService.updateAndAdd(a);
	}
	/**
	 * 统计加班时长(所有部门的)
	 */
	@GetMapping("/selectMonth")
	public HashMap<String, Object> selectMonth() {
		try {
			return iovertimeService.selectMonth();
		} catch (ParseException e) {
			e.printStackTrace();
			return new HashMap<String,Object>(){
				{
					put("info","查询失败");
				}
			};
		}
	}

	@GetMapping("/selectDept")
	public HashMap<String, Object> selectDept(@RequestParam String xuanBumenName) throws ParseException {
		if (xuanBumenName.equals("全部部门")) {
			return  iovertimeService.selectMonth();
		} else {

			return  iovertimeService.selectDept(xuanBumenName);
		}
	}
}


