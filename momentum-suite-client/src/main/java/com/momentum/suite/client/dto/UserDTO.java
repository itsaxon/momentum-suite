package com.momentum.suite.client.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户数据传输对象 (DTO)
 * <p>
 * 用于在 Controller, Service, Facade 之间以及跨服务调用时传输用户信息。
 *
 * {@code @Data}: Lombok 注解，自动生成 Getter, Setter, toString, equals, hashCode 方法。
 * {@code @Accessors(chain = true)}: Lombok 注解，开启链式调用，例如: new UserDTO().setUsername("test").setEmail("...");
 */
@Data
@Accessors(chain = true)
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

}
