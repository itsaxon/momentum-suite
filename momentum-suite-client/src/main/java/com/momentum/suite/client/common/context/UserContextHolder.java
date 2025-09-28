package com.momentum.suite.client.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 用户上下文持有器
 * <p>
 * 用于在单次请求的线程生命周期内安全地存储和获取用户信息。
 * 使用 TransmittableThreadLocal 确保在异步场景下（如 @Async 方法）上下文能够正确传递。
 * </p>
 *
 * @author itsaxon
 * @version v1.0 2025/09/27
 */
public final class UserContextHolder {

    private static final ThreadLocal<Long> USER_ID = new TransmittableThreadLocal<>();

    private static final ThreadLocal<String> USERNAME = new TransmittableThreadLocal<>();

    // 私有构造函数，防止被实例化
    private UserContextHolder() {
    }

    /**
     * 设置当前用户的ID
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * 获取当前用户的ID
     * @return 用户ID，如果未设置则返回 null
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 设置当前用户的用户名
     * @param username 用户名
     */
    public static void setUsername(String username) {
        USERNAME.set(username);
    }

    /**
     * 获取当前用户的用户名
     * @return 用户名，如果未设置则返回 null
     */
    public static String getUsername() {
        return USERNAME.get();
    }

    /**
     * 清理当前线程的用户上下文信息。
     * 必须在请求处理完成后（如在拦截器的 afterCompletion 中）调用，以防内存泄漏。
     */
    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
    }
}
