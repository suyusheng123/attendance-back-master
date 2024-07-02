package com.ams.service.impl;

import com.ams.dao.EmployeeInformationMapper;
import com.ams.pojo.EmployeeInformation;
import com.ams.pojo.SysUser;
import com.ams.dao.SysUserMapper;
import com.ams.service.EmployeeInformationService;
import com.ams.service.SysUserService;
import com.ams.utils.EmailUtil;
import com.ams.utils.MdFive;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;


/**
 * <p>
 * 用户表 服务实现类
 */
@Service
public class SysUserServiceImpl  implements SysUserService {
    @Autowired(required = false)
    SysUserMapper sysUserMapper;

    @Autowired(required = false)
    EmployeeInformationMapper employeeInformationMapper;

    @Autowired
    EmailUtil emailUtil;

    //注入md5工具类
    @Autowired
    MdFive mdFive;

    @Override
    public HashMap<String, Object> login(SysUser a, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
//        HttpSession session = request.getSession();
        //获取session中的验证码
//        String code =(String) session.getAttribute(KAPTCHA_SESSION_KEY);
//        String code =(String)req.getAttribute(KAPTCHA_SESSION_KEY);
        //删除session中的验证码
//        request.getSession().removeAttribute(KAPTCHA_SESSION_KEY);
//        req.removeAttribute(KAPTCHA_SESSION_KEY);
//        System.out.println(code);
        List<SysUser> sysUsers = sysUserMapper.selectByName(a.getUserName());
        if ("1".equals(sysUsers.get(0).getState())) {
            map.put("info","该用户已经停用");
        }
//        else if(!(a.getCode()!=null&&a.getCode().equalsIgnoreCase(code))){
//            map.put("info","验证码错误");
//        }
        else{

            try {
                //1.用户令牌（用户和密码对象）
                UsernamePasswordToken token = new UsernamePasswordToken(a.getUserName(), a.getUserPwd());
                //2 创建shiro的用户对对象
                Subject su = SecurityUtils.getSubject();
                //3 登录
                su.login(token);

                EmployeeInformation employeeInformation=new EmployeeInformation();
                employeeInformation.setStaffId(String.valueOf(sysUsers.get(0).getStaffId()));
                EmployeeInformation emp = employeeInformationMapper.selectByStaffId(employeeInformation);
                String role="";
                if (!emp.getSysRole().equals("")) {
                    role = emp.getSysRole();
                    map.put("role",role);
                }

                map.put("staffId",sysUsers.get(0).getStaffId());
                map.put("userName",sysUsers.get(0).getUserName());
                map.put("userId",sysUsers.get(0).getUserId());
                map.put("department",emp.getDepartment());
                map.put("info","登录成功");
            } catch (UnknownAccountException e) {
                e.printStackTrace();
                map.put("info","用户名不正确");
                System.out.println("用户名不正确");
            }catch (IncorrectCredentialsException e){
                e.printStackTrace();
                map.put("info","密码不正确");
                System.out.println("密码不正确");
            }
        }

        return map;
    }


    public HashMap<String, Object> add(SysUser a) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //创建条件构造器
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        //定义根据用户名查询的条件
        // SELECT su.*
        // FROM sys_user su
        // WHERE email = "3208747550@qq.com" OR user_name = "root";
        queryWrapper.eq("email", a.getEmail()).or().eq("user_name", a.getUserName());
        //查询用户名是否重名
        List<SysUser> list = sysUserMapper.selectList(queryWrapper);
        SecureRandom random = new SecureRandom();
        if(list.size() > 0){
            map.put("info","用户名或者邮箱重名");
        }else {
                EmployeeInformation emp = new EmployeeInformation();
                emp.setStaffId(a.getStaffId().toString());
                //获取盐值
                String salt = UUID.randomUUID()+"";
                a.setSalt(salt);
                a.setState("0");
                //加密密码
                String p = mdFive.encrypt(a.getUserPwd(), a.getSalt());
                a.setUserPwd(p);
                emp.setStaffPwd(p);
                // 添加员工的邮箱
                emp.setStaffEmail(a.getEmail());
                employeeInformationMapper.insert(emp);
                //新增
                int  num = sysUserMapper.insert(a);
                if(num>0){
                    map.put("info","保存成功");
                }else{
                    map.put("info","保存错误");
                }
            }
        return map;
    }
    @Override
    public HashMap<String, String> getSendEmailCode(SysUser a,HttpSession session) {
        HashMap<String, String> map = new HashMap<>();
        String  checkCode= emailUtil.RandEmailCode();
        System.out.println(checkCode);
        String content = "你好:你申请绑定邮箱验证码的邮箱验证码为:"+checkCode+",该验证码1分钟内有效。";
        //发送验证码
        if(a.getEmail().endsWith("@qq.com")){
            emailUtil.sendEmail(a.getEmail(),"验证码",content);

            session.setAttribute("emailCode",checkCode);
            //返回验证码
            map.put("info",checkCode);
        }else {
            map.put("info","邮件格式错误！请以@qq.com格式结尾。");
        }
        return map;
    }

    @Override
    public boolean updatePassWordByName(String userName, String userPwd, String salt) {
        //加密密码
        String p = mdFive.encrypt(userPwd, salt);
        int n=sysUserMapper.updatePassWordByName(userName,p);
        if(n==1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public HashMap<String, Object> updatePwd(SysUser user) {
        HashMap<String, Object> map = new HashMap<>();
        SysUser sysUser = sysUserMapper.selectById(user);
        String newPwd = mdFive.encrypt(user.getUserPwd(), sysUser.getSalt());
        user.setUserPwd(newPwd);
        int num = sysUserMapper.updateById(user);
        if (num>0){
            map.put("info","修改密码成功");
        }else {
            map.put("info","修改密码失败");
        }
        return map;
    }

    public HashMap<String, Object> select(SysUser a) {
        HashMap<String, Object> map = new HashMap<>();

        //定义分页对象
        Page<SysUser> page = new Page<>(a.getPage(), a.getRow());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        //编号查询
        queryWrapper.eq(a.getStaffId() != null, "staff_id", a.getStaffId());
        //Page<AttenManage> aList = attenManageMapper.selectByCon(page, a);

        //用户名的模糊查询
        queryWrapper.like(!StringUtils.isEmpty(a.getUserName()) && a.getUserName() != null, "user_name",
                a.getUserName());

        // 根据id降序排序
        queryWrapper.orderByDesc("user_id");
        //查询数据库
        Page<SysUser> aList = sysUserMapper.selectPage(page, queryWrapper);
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
    public HashMap<String, Object> update(SysUser a) {
        HashMap<String, Object> map = new HashMap<>();

        //创建条件构造器
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        //定义根据用户名查询的条件
        queryWrapper.eq("user_name", a.getUserName()).or().eq("email", a.getEmail());
        queryWrapper.ne("staff_id", a.getStaffId());

        //查询用户名和邮箱是否重名
        List<SysUser> list = sysUserMapper.selectList(queryWrapper);
        int num = 0;
        EmployeeInformation emp = new EmployeeInformation();
        String salt = UUID.randomUUID() + "";
        //加密密码
        String md5Pass = mdFive.encrypt(a.getUserPwd(), salt);
        if (list.size() > 0) {
            map.put("info", "用户名或邮箱重复");
        }
        //修改
            num = sysUserMapper.updateById(a);
            a.setSalt(salt);
            a.setUserPwd(md5Pass);
            emp.setStaffId(String.valueOf(a.getStaffId()));
            emp.setStaffPwd(md5Pass);
            // 修改员工的邮箱
            emp.setStaffEmail(a.getEmail());
            QueryWrapper<EmployeeInformation> query = new QueryWrapper<>();
            query.eq("staff_id", a.getStaffId());
            employeeInformationMapper.update(emp, query);
        if (num > 0) {
            map.put("info", "保存成功");
        } else {
            map.put("info", "保存失败");
        }
        return map;
    }


    @Override
    public HashMap<String, Object> del(SysUser a) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        employeeInformationMapper.delByStaffId(a.getStaffId());
        //删除
        int num = sysUserMapper.deleteById(a);
        if (num > 0) {
            map.put("info", "保存成功");
        } else {
            map.put("info", "保存错误");
        }
        return map;
    }
}
