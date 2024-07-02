# AMS

#### 介绍
考勤信息管理系统

#### 软件架构
软件架构说明

![输入图片说明](https://images.gitee.com/uploads/images/2021/1123/194637_45bb7b52_8672045.png "屏幕截图.png")

前后端分离 后端：springboot+shiro+mybatiplus  前端：vue脚手架 +elementUI

1. 安全管理  密码修改   权限管理 （前端做权限控制）

![输入图片说明](https://images.gitee.com/uploads/images/2021/1123/194516_89bc2991_8672045.png "屏幕截图.png")

![输入图片说明](https://images.gitee.com/uploads/images/2021/1123/194551_96fab791_8672045.png "屏幕截图.png")

2. 考情管理

3. 差假管理模块

4. 考情查询模块

5. 人事管理模块

6. 系统设置

7. 提醒管理模块 报表统计 （一般是echarts）

#### 安装教程

1.  数据库
    ![输入图片说明](https://images.gitee.com/uploads/images/2021/1123/194607_9f4f8365_8672045.png "屏幕截图.png")
2.  前端
    https://gitee.com/Liuk9805/ams-front-end
3.  后端
    https://gitee.com/Liuk9805/ams

#### 使用说明

1. 本项目仅仅只是一个demo，为了学习而作，没有上线也没有维护。

2. 后端还有很多问题，例如本来该用redis做的由于时间和电脑所以没用，如果你有兴趣可以去下面了解：https://note.youdao.com/s/36qdhpvJ

3. 此外微服务也没实现有兴趣可以去了解：https://note.youdao.com/s/3bWIYhH6

4. 如果对其业务功能有问题请移步：https://note.youdao.com/s/XuiV8U5E

3. 至于数据库云端数据库地址:(不保证数据一直存在，请不要搞破坏)

   云服务器数据库（阿里云）
   47.106.135.97 默认端口：3306
   用户名：attendance
   密码：123456

   默认用户名：root
   密码：123

   建议使用本地数据库：[attendance.sql](https://gitee.com/Liuk9805/ams/blob/master/attendance.sql)

