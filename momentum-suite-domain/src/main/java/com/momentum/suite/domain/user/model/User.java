package com.momentum.suite.domain.user.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户领域对象 (Domain Object)
 * <p>
 * 这是用户聚合的聚合根 (Aggregate Root)。
 * 它代表了业务世界中的“用户”这个概念，是业务规则和行为的载体。
 * 它不关心数据如何存储或展示，只关心业务本身。
 *
 * @author itsaxon
 */
@Getter
@Setter // 在严格的DDD中，Setter可能会被更具体的业务方法替代，但为了实用性，我们通常会保留它们。
public class User {

    // --- 核心身份标识 ---
    private Long id;
    private String username;
    private String encryptedPassword;
    private String email;
    private String mobile;

    // --- 业务状态 ---
    private UserStatus status; // 使用枚举来表示状态，比用 String 或 Integer 更安全、更清晰

    // --- 审计信息 ---
    private Long createId;
    private String createName;
    private LocalDateTime createTime;
    private Long updateId;
    private String updateName;
    private LocalDateTime updateTime;

    // --- 构造函数 ---
    // 可以提供一个静态工厂方法来创建一个新用户，封装初始化的业务规则
    public static User createNewUser(String username, String rawPassword, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(username);
        user.setEncryptedPassword(passwordEncoder.encode(rawPassword));
        user.setStatus(UserStatus.ENABLED); // 新用户默认为启用状态
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // 其他初始化逻辑...
        return user;
    }


    // =================================================================
    //                  ↓↓↓ 领域对象的精髓：封装业务行为 ↓↓↓
    // =================================================================

    /**
     * 业务行为：修改密码。
     * <p>
     * 将密码修改的完整逻辑（校验、更新）封装在 User 对象内部。
     * Service 层只需调用 user.changePassword(...)，而无需关心具体实现。
     *
     * @param oldRawPassword  用户输入的旧密码（明文）
     * @param newRawPassword  用户输入的新密码（明文）
     * @param passwordEncoder 密码编码器，通过依赖注入的方式传入，保持领域对象对具体框架的解耦。
     */
    public void changePassword(String oldRawPassword, String newRawPassword, PasswordEncoder passwordEncoder) {
        // 规则1：校验旧密码是否正确
        if (!passwordEncoder.matches(oldRawPassword, this.encryptedPassword)) {
            throw new IllegalArgumentException("旧密码不正确"); // 抛出异常，由上层捕获处理
        }

        // 规则2：新密码不能与旧密码相同
        if (passwordEncoder.matches(newRawPassword, this.encryptedPassword)) {
            throw new IllegalArgumentException("新密码不能与旧密码相同");
        }

        // 规则3：新密码必须符合强度要求（示例）
        if (newRawPassword.length() < 8) {
            throw new IllegalArgumentException("新密码长度不能少于8位");
        }

        // 执行更新
        this.setEncryptedPassword(passwordEncoder.encode(newRawPassword));
        this.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 业务行为：禁用账户。
     */
    public void disable() {
        // 规则：检查账户是否已经是禁用状态，避免无效操作和不必要的数据库更新
        if (this.status == UserStatus.DISABLED) {
            return; // 幂等性保证
        }
        this.setStatus(UserStatus.DISABLED);
        this.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 业务行为：启用账户。
     */
    public void enable() {
        if (this.status == UserStatus.ENABLED) {
            return;
        }
        this.setStatus(UserStatus.ENABLED);
        this.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 业务查询：判断账户是否可用。
     * 将简单的逻辑判断也封装起来，让调用方的代码更具可读性。
     * 例如：if (user.isEnabled()) {...}
     */
    public boolean isEnabled() {
        return this.status == UserStatus.ENABLED;
    }
}
