package com.ams.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class EmailUtil {
    //注入java邮件发送服务列
    @Autowired
    JavaMailSender javaMailSender;

    //发送者邮箱
    @Value("${spring.mail.username}")
    private String fromEmail;
    //邮件发送

    /**
     *
     * @param toEmail 接收者邮箱
     * @param title    邮件标题
     * @param content   邮件正文
     * @return
     */
    public  String sendEmail(String toEmail,String title,String content){
        try {
            //创建一个普通邮件
            SimpleMailMessage message=new SimpleMailMessage();
            //发送者邮箱
            message.setFrom(fromEmail);
            //接收者邮箱
            message.setTo(toEmail);
            //邮件标题
            message.setSubject(title);
            //邮件正文
            message.setText(content);

            //发送邮件
            javaMailSender.send(message);
            System.out.println("邮件发送成功");
            return "success";
        } catch (MailException e) {
            e.printStackTrace();
            System.out.println("邮件发送失败");
        }

        return "fail";
    }

    public String RandEmailCode(){
        //创建随机对象
        Random random = new Random();
        //定义随机的字符的全集
        String string="QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789";
        //保存验证码
        StringBuffer sb=new StringBuffer();
        for (int i=0;i<5;i++) {
            //生成一个随机的数，最大为string的长度
            int randomIndex = random.nextInt(string.length());
            char randomChar = string.charAt(randomIndex);
            sb.append(randomChar);
        }
        String checkCode = sb.toString();
        return checkCode;
    }

}
