package com.momentum.suite.domain.user.model;

/**
 * 密码编码器接口，用于将领域模型与具体的技术实现（如 Spring Security）解耦。
 * <p>
 * Domain 层定义接口，Infrastructure 层提供实现。
 * 这是一种典型的“依赖倒置原则”的应用。
 */
public interface PasswordEncoder {

    String encode(String rawPassword);

    /**
     * 默认方法，方便在接口中直接提供匹配逻辑。
     * @param rawPassword 明文密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean matches(String rawPassword, String encodedPassword);
}
