package com.momentum.suite.infrastructure.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器
 * <p>
 * 负责自动填充 create_time, update_time, create_by, update_by 等字段。
 */
@Slf4j
@Component // 必须将其注册为 Spring Bean
public class MybatisPlusFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始进行 insert 自动填充...");
        // 填充创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        // 填充更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);

        // 填充创建人和更新人信息 (这里先用模拟数据，后续应从用户上下文中获取)
        // Long currentUserId = AuthUtil.getUserId();
        // String currentUsername = AuthUtil.getUsername();
        Long currentUserId = 1L; // 模拟系统管理员ID
        String currentUsername = "system"; // 模拟系统

        this.strictInsertFill(metaObject, "createId", () -> currentUserId, Long.class);
        this.strictInsertFill(metaObject, "createName", () -> currentUsername, String.class);
        this.strictInsertFill(metaObject, "updateId", () -> currentUserId, Long.class);
        this.strictInsertFill(metaObject, "updateName", () -> currentUsername, String.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始进行 update 自动填充...");
        // 填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);

        // 填充更新人信息 (这里先用模拟数据)
        // Long currentUserId = AuthUtil.getUserId();
        // String currentUsername = AuthUtil.getUsername();
        Long currentUserId = 1L; // 模拟系统管理员ID
        String currentUsername = "system"; // 模拟系统

        this.strictUpdateFill(metaObject, "updateId", () -> currentUserId, Long.class);
        this.strictUpdateFill(metaObject, "updateName", () -> currentUsername, String.class);
    }
}
