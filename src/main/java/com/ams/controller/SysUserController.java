package com.ams.controller;


import com.ams.dao.EmployeeInformationMapper;
import com.ams.dao.SysUserMapper;
import com.ams.pojo.EmployeeInformation;
import com.ams.pojo.SysUser;
import com.ams.service.EmployeeInformationService;
import com.ams.service.SysUserService;
import com.ams.utils.MdFive;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 用户表
 */
@RestController
@RequestMapping("/sys-user")
public class SysUserController {
	@Autowired(required = false)
	private SysUserService sysUserService;

	@Autowired(required = false)
	private SysUserMapper sysUserMapper;

	@Autowired(required = false)
	private EmployeeInformationService employeeInformationService;

	@Resource
	private Producer captchaProducer;

	//注入md5工具类
	@Autowired
	MdFive mdFive;

	@RequestMapping("/login")
	public HashMap<String, Object> login(SysUser a,HttpServletRequest request){
		System.out.println(a);
		return sysUserService.login(a,request);
	}
	//用户列表
	@RequestMapping("/list")
	public HashMap<String, Object> List(SysUser a) {

		return sysUserService.select(a);
	}

	@RequestMapping("/getKaptchaImage")
	public void getKaptchaImage(HttpServletResponse response, HttpSession session) throws Exception {
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
		response.setContentType("image/jpeg");
		// create the text for the image
		String capText = captchaProducer.createText();
		// store the text in the session
		//request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		//将验证码存到session
		session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		System.out.println("验证码："+capText);

		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		// write the data out
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
	}

	//添加
	@RequestMapping("/add")
	public HashMap<String, Object> add(SysUser a) {

		return sysUserService.add(a);
	}
	//更新
	@RequestMapping("/update")
	public HashMap<String, Object> update(SysUser a) {

		return sysUserService.update(a);
	}
	//删除
	@RequestMapping("/del")
	public HashMap<String, Object> del(SysUser a) {
		return sysUserService.del(a);
	}


	@RequestMapping("/emailCode")
	public HashMap<String, String> emailCode(SysUser a, HttpSession session) {

		return sysUserService.getSendEmailCode(a, session);
	}

	@CrossOrigin(allowCredentials = "true")
	@RequestMapping("/register")
	public HashMap<String, Object> register(SysUser a, HttpSession session) {
		System.out.println(a);
		HashMap<String, Object> map = new HashMap<>();
		SecureRandom random = new SecureRandom();
		//获取前端输入的验证码
		//String inputEmailCode = a.getEmailCode();
		//?怎么获取已经发送的验证码？使用session

		//String sendEmailCode = (String) session.getAttribute("emailCode");
		//System.out.println("前端输入的验证码" + inputEmailCode + "    后端生成的验证码" + sendEmailCode);
		//生成staffId
		StringBuilder str = new StringBuilder();//定义变长字符串
		//随机生成数字，并添加到字符串
		for (int i = 0; i < 7; i++) {
			str.append(random.nextInt(10));
		}
		//将字符串转换为数字并输出
		int password = Integer.parseInt(str.toString());
		a.setStaffId(password);
		EmployeeInformation emp = new EmployeeInformation();
		emp.setStaffName(a.getStaffName());
		emp.setStaffEmail(a.getEmail());
		emp.setStaffSex(a.getSex());
		emp.setStaffPhone(a.getPhone());
		emp.setSysRole("员工");
		//获取盐值
		String salt = UUID.randomUUID()+"";
		a.setSalt(salt);
		//加密密码
		String p = mdFive.encrypt(a.getUserPwd(), a.getSalt());
		a.setUserPwd(p);
		emp.setStaffPwd(p);
		emp.setStaffId(String.valueOf(a.getStaffId()));
		HashMap<String, Object> res = employeeInformationService.add(emp,a);

		if (res.get("info") == "保存成功") {
			map.put("info", "注册成功");
		} else {
			map.put("info", "注册失败");
		}

		return map;
	}

	/**
	 * 登录界面忘了密码
	 *
	 * @param a
	 * @param request
	 * @return HashMap
	 */
	@RequestMapping("/updatePassword")
	public HashMap<String, Object> updatePassword(SysUser a, HttpServletRequest request) {
		HashMap<String, Object> map = new HashMap<>();
		//通过用户名查询邮箱
		List<SysUser> list = sysUserMapper.selectByName(a.getUserName());
		if (list.size() != 0) {
			String email = list.get(0).getEmail();
			//和前端输入的邮件作比较 发送验证码
			if (a.getEmail().equalsIgnoreCase(email)) {
				//加密密码
				String encrypt = mdFive.encrypt(a.getSurePassword(), list.get(0).getSalt());
				//前端两次输入的确认密码比较
				if (!encrypt.equals(list.get(0).getUserPwd())) {
					//获取发送的验证码
					//HttpSession session = request.getSession();
					//String sendEmailCode = (String) session.getAttribute("emailCode");
					//if (a.getEmailCode().equalsIgnoreCase(sendEmailCode)) {
					boolean flag = sysUserService.updatePassWordByName(a.getUserName(), a.getUserPwd(), list.get(0).getSalt());
					EmployeeInformation emp = new EmployeeInformation();
					emp.setStaffId(String.valueOf(list.get(0).getStaffId()));
					String newPwd = mdFive.encrypt(a.getSurePassword(), list.get(0).getSalt());
					emp.setStaffPwd(newPwd);
					employeeInformationService.updatePwdByStaffId(emp);
					if (flag) {
						map.put("info", "修改成功");
					} else {
						map.put("info", "修改失败");
					}
					//} else {
					//	map.put("info", "验证码输入错误");
					//}

				} else {
					map.put("info", "不能与原密码相同！");
				}
			} else {
				map.put("info", "邮箱错误!非注册邮箱,请重新输入。");
			}
		} else {
			map.put("info", "无" + a.getUserName() + "用户");
		}

		return map;
	}

	/**
	 * 登录之后修改密码
	 *
	 * @param user
	 * @return HashMap
	 */
	@RequestMapping("/updatePwd")
	public HashMap<String, Object> updatePwd(SysUser user) {

		return sysUserService.updatePwd(user);
	}
}

