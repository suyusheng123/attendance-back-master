package com.ams.service.impl;

import com.ams.dao.RolePowerRefMapper;
import com.ams.dao.SysPowerMapper;
import com.ams.pojo.RolePowerRef;
import com.ams.pojo.SysPower;
import com.ams.pojo.SysRole;
import com.ams.dao.SysRoleMapper;
import com.ams.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired(required = false)
	SysRoleMapper sysRoleMapper;
	@Autowired(required = false)
	RolePowerRefMapper rolePowerRefMapper;

	@Resource
	private SysPowerMapper sysPowerMapper;

	@Override
	public HashMap<String, Object> select(SysRole a) {
		HashMap<String, Object> map = new HashMap<>();

		//定义分页对象
		Page<SysRole> page = new Page<>(a.getPage(), a.getRow());
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
		//编号查询
		queryWrapper.eq(a.getRoleId() != null, "role_id", a.getRoleId());
		//Page<AttenManage> aList = attenManageMapper.selectByCon(page, a);

		//用户名的模糊查询
		queryWrapper.like(!StringUtils.isEmpty(a.getSysRoleName()) && a.getSysRoleName() != null, "sys_role_name",
				a.getSysRoleName());
		//查询数据库
		Page<SysRole> aList = sysRoleMapper.selectPage(page, queryWrapper);
		//当前页集合
		map.put("list", aList.getRecords());
		//总页数
		map.put("totalPage", aList.getPages());
		//总条数
		map.put("total", aList.getTotal());
		//当前页
		map.put("curPage", a.getPage());
		if (a.getPage() == 1) {
			//上一页
			map.put("prePage", 1);

		} else {
			map.put("prePage", a.getPage() - 1);

		}
		if (a.getPage() == aList.getPages()) {
			//下一页
			map.put("nextPage", aList.getPages());

		} else {
			map.put("nextPage", a.getPage() + 1);

		}
		//每页显示条数
		map.put("row", a.getRow());
		return map;
	}

	@Override
	public HashMap<String, Object> add(SysRole a, int[] permissions) {
		HashMap<String, Object> map = new HashMap<>();

		// 判断该角色是否已添加
	    int count1 = sysRoleMapper.selectRoleName(a.getSysRoleName());
		if (count1 > 0){
			map.put("info", "该角色已存在");
			return map;
		}

		int count2 = sysRoleMapper.selectRoleCode(a.getSysRoleCode());
		if (count2 > 0){
			map.put("info", "该角色已存在");
			return map;
		}

		int num = sysRoleMapper.insert(a);
		if (num > 0) {
			int roleId = 0;
			List<SysRole> sysRoles = sysRoleMapper.selectList(null);
			for (SysRole sysRole : sysRoles) {
				if (sysRole.getSysRoleName().equals(a.getSysRoleName())) {
					roleId = sysRole.getRoleId();
					break;
				}
			}
			rolePowerRefMapper.delByRoleId(a.getRoleId());
			if (permissions == null){
				map.put("info", "你没有分配权限,请重新分配权限");
				return map;
			}
			RolePowerRef item = new RolePowerRef();
			for (int permission : permissions) {
				item.setRoleId(roleId);
				// 查询菜单有没有父菜单，如果有的话，把父菜单也加进去
				QueryWrapper<SysPower> queryWrapper1 = new QueryWrapper<>();
				queryWrapper1.eq("power_index", permission);
				SysPower sysPower = sysPowerMapper.selectOne(queryWrapper1);

				// 查询当前这个角色有没有插入过这个菜单了

				int i = rolePowerRefMapper.selectPower(roleId, sysPower.getPowerIndex());
				if (i == 0) {
					if (sysPower.getParentId() != 0) {
						// 查询父节点有没有添加
						int j = rolePowerRefMapper.selectPower(roleId, String.valueOf(sysPower.getParentId()));
						if (j == 0){
							item.setPowerIndex(sysPower.getParentId());
							rolePowerRefMapper.insert(item);
						}
					}
					item.setPowerIndex(permission);
					int insertRes = rolePowerRefMapper.insert(item);
					if (insertRes < 0) {
						map.put("info", "插入权限错误");
						break;
					}
				}
			}
			map.put("info", "保存成功");
		} else {
			map.put("info", "保存失败");
		}
		return map;
	}

	@Override
	public HashMap<String, Object> del(SysRole a) {
		HashMap<String, Object> map = new HashMap<>();

		int num = sysRoleMapper.deleteById(a);
		rolePowerRefMapper.delByRoleId(a.getRoleId());
		if (num > 0) {
			map.put("info", "保存成功");
		} else {
			map.put("info", "保存失败");
		}
		return map;
	}

	@Override
	public HashMap<String, Object> update(SysRole a, int[] permissions) {
		HashMap<String, Object> map = new HashMap<>();

		// 判断该角色是否已添加
		int count1 = sysRoleMapper.selectRoleName(a.getSysRoleName());

		if (count1 > 1){
			map.put("info", "该角色已存在");
			return map;
		}

		int count2 = sysRoleMapper.selectRoleCode(a.getSysRoleCode());

		if (count2 > 1){
			map.put("info", "该角色已存在");
			return map;
		}

		int num = sysRoleMapper.updateById(a);
		if (num > 0) {
			rolePowerRefMapper.delByRoleId(a.getRoleId());
			if (permissions == null) {
				map.put("info", "不允许权限滞空,请重新分配权限");
				return map;
			}
			RolePowerRef item = new RolePowerRef();
			for (int permission : permissions) {
				item.setRoleId(a.getRoleId());
				// 查询菜单有没有父菜单，如果有的话，把父菜单也加进去
				QueryWrapper<SysPower> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("power_index", permission);
				SysPower sysPower = sysPowerMapper.selectOne(queryWrapper);
				int i = rolePowerRefMapper.selectPower(a.getRoleId(), sysPower.getPowerIndex());
				// 防止插入重复的菜单項
				if (i == 0) {
					if (sysPower.getParentId() != 0) {
						int j = rolePowerRefMapper.selectPower(a.getRoleId(), String.valueOf(sysPower.getParentId()));
						if (j == 0){
							item.setPowerIndex(sysPower.getParentId());
							rolePowerRefMapper.insert(item);
						}
					}

					item.setPowerIndex(permission);
					int insertRes = rolePowerRefMapper.insert(item);
					if (insertRes < 0) {
						map.put("info", "插入权限错误");
						break;
					}
				}
			}
			map.put("info", "保存成功");
		} else {
			map.put("info", "保存失败");
		}
		return map;
	}
}
