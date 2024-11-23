package com.qiming;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;
import java.sql.Types;
import java.util.Collections;

public class CodeGenerator {
    static String url = "jdbc:mysql://192.168.16.128:3306/db01?remarks=true&useInformationSchema=true";
    static String username = "qm";
    static String password = "wangqiming";

    static String outputPath = Paths.get(System.getProperty("user.dir")) + "/src/test/java";

    public static void main(String[] args) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("QM") // 设置作者
                            .dateType(DateType.ONLY_DATE) // 设置时间类型
                            .outputDir(outputPath); // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.INTEGER) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("com.qiming") // 设置父包名
                                .moduleName("mapper") // 设置父包模块名
                                .xml("com.qiming.mapper") // 设置mapper.xml生成路径
                                .pathInfo(Collections.singletonMap(OutputFile.xml, outputPath)) // 设置mapperXml生成路径
                )
                .strategyConfig(builder -> {
                            builder.entityBuilder()
                                    .enableLombok()
                                    .enableChainModel() // 开启链式调用
                                    .enableTableFieldAnnotation() // 添加@TableField 注解
                                    .enableColumnConstant(); // 添加常量字段
                            builder.controllerBuilder()
                                    .enableRestStyle(); // 开启生成@RestController 控制器
                        }
                )
                .injectionConfig(builder -> {
                    builder.beforeOutputFile((tableInfo, objectMap) -> { // 查看要生成文件关联的配置信息
                        System.out.println("table:" + tableInfo.getEntityName() + " to file" + objectMap.size());
                        objectMap.forEach((key, value) -> System.out.println("\tkey:" + key + ", value:" + value));
                    });
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
