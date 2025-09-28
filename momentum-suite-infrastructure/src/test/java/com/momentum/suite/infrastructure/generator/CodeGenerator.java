package com.momentum.suite.infrastructure.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成器
 *
 * @author itsaxon
 * @version v2.0 2025/09/26
 */
public class CodeGenerator {

    // ================= ↓↓↓↓↓↓ 自定义配置区域 ↓↓↓↓↓↓ ==================

    // 数据库连接配置
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/momentum-suite?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "password";

    // 生成的根包名
    private static final String ROOT_PACKAGE = "com.momentum.suite";

    // 要生成的表名 (例如: "t_user", "t_role")
    private static final String[] TABLE_NAMES = {"t_admin_user_info"};

    // 需要移除的表前缀
    private static final String[] TABLE_PREFIX = {"t_"};

    // 作者
    private static final String AUTHOR = "itsaxon";

    // ================= ↑↑↑↑↑↑ 自定义配置区域 ↑↑↑↑↑↑ ==================

    public static void main(String[] args) {
        String projectRoot = System.getProperty("user.dir");

        FastAutoGenerator.create(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)
                // 1. 全局配置
                .globalConfig(builder -> {
                    builder.author(AUTHOR)
                            .outputDir(projectRoot + "/momentum-suite-infrastructure/src/main/java")
                            .dateType(DateType.TIME_PACK) // 使用 java.time 包下的日期类型
                            .commentDate("yyyy-MM-dd")
                            .disableOpenDir();
                })

                // 2. 数据源配置 (可选，用于自定义类型转换)
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    // 将数据库的 tinyint(1) 转换为 Boolean 类型
                    if (metaInfo.getJdbcType().TYPE_CODE == java.sql.Types.TINYINT) {
                        return DbColumnType.BOOLEAN;
                    }
                    return typeRegistry.getColumnType(metaInfo);
                }))

                // 3. 包配置 (核心，定义多模块输出路径)
                .packageConfig(builder -> {

                    // 为每个模块单独设置完整的包路径
                    builder.entity("infrastructure.persistent.po")
                            .mapper("infrastructure.persistent.mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("app.controller");

                    Map<OutputFile, String> pathInfo = new HashMap<>();
                    // Infrastructure 模块
                    pathInfo.put(OutputFile.entity, projectRoot + "/momentum-suite-infrastructure/src/main/java/com/momentum/suite/infrastructure/persistent/po");
                    pathInfo.put(OutputFile.mapper, projectRoot + "/momentum-suite-infrastructure/src/main/java/com/momentum/suite/infrastructure/persistent/mapper");
                    pathInfo.put(OutputFile.xml, projectRoot + "/momentum-suite-infrastructure/src/main/resources/mapper");
                    // Client 模块
                    pathInfo.put(OutputFile.service, projectRoot + "/momentum-suite-service/src/main/java/com/momentum/suite/service");
                    // Service 模块
                    pathInfo.put(OutputFile.serviceImpl, projectRoot + "/momentum-suite-service/src/main/java/com/momentum/suite/service/impl");
                    builder.parent(ROOT_PACKAGE).pathInfo(pathInfo);
                })

                // 4. 策略配置 (核心，定义生成规则)
                .strategyConfig(builder -> {
                    builder.addInclude(TABLE_NAMES)
                            .addTablePrefix(TABLE_PREFIX)

                            // Controller 策略
                            .controllerBuilder()
                            .disable()

                            // Service (Facade) 策略
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")

                            // Entity (PO) 策略
                            .entityBuilder()
                            .enableLombok()
                            .enableChainModel()
                            .enableTableFieldAnnotation() // 生成字段注解
                            .logicDeleteColumnName("del_flag") // 逻辑删除字段
                            .idType(IdType.ASSIGN_ID) // 全局主键策略: 雪花算法
                            .addTableFills( // 【重要】自动填充配置
                                    new Column("create_time", FieldFill.INSERT),
                                    new Column("create_id", FieldFill.INSERT),
                                    new Column("create_name", FieldFill.INSERT),
                                    new Column("update_time", FieldFill.INSERT_UPDATE),
                                    new Column("update_id", FieldFill.INSERT_UPDATE),
                                    new Column("update_name", FieldFill.INSERT_UPDATE)
                            )
                            .formatFileName("%sPO")

                            // Mapper 策略
                            .mapperBuilder()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper");
                })

                // 5. 模板引擎
                .templateEngine(new FreemarkerTemplateEngine())

                // 6. 执行
                .execute();

        System.out.println("✅ 代码生成成功！");
        System.out.println("请检查以下模块的代码是否已生成或更新：");
        System.out.println("  - momentum-suite-app (Controller)");
        System.out.println("  - momentum-suite-service (Service, ServiceImpl)");
        System.out.println("  - momentum-suite-infrastructure (PO, Mapper, XML)");
    }
}
