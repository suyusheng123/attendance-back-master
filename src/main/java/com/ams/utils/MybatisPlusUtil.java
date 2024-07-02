package com.ams.utils;



import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * 代码生成器
 */
public class MybatisPlusUtil {
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        System.out.println("userDir=" + userDir);
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //获取当前的项目的路径
        String projectPath = System.getProperty("user.dir");
        System.out.println(projectPath);

        //配置生成路径
        gc.setOutputDir(projectPath + "/src/main/java");
        // 作者名称
        gc.setAuthor("LK");
        // 配置日期类型，此处为 ONLY_DATE（可选）
        gc.setDateType(DateType.ONLY_DATE);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://127.0.0.1:13306/attendance?useUnicode=true&characterEncoding=UTF-8" +
                "&zeroDateTimeBehavior=convertToNull");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.ams");
        pc.setController("controller");
        pc.setEntity("pojo");
        pc.setMapper("dao");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略:下划线转驼峰命
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略:下划线转驼峰命
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //实体策略配置
        strategy.setEntityLombokModel(true);
        // 配置 rest 风格的控制器（@RestController）
        strategy.setRestControllerStyle(true);
        // 指定表名（可以同时操作多个表，使用 , 隔开）
        strategy.setInclude("overtime");
        //配置驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        // 如果表名有下划线,生成实体去除下划线
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);

        //模板生成器
        TemplateConfig tc = new TemplateConfig();
        //设置sql映射配置文件的模板
        tc.setXml("/templates/mapper.xml");
        mpg.setTemplate(tc);

        mpg.execute();
    }




}
