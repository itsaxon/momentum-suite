package com.momentum.suite.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * app用户信息表
 * </p>
 *
 * @author itsaxon
 * @since 2025-09-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_app_user_info")
public class AppUserInfoPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 聊天服务用户id
     */
    @TableField("chat_user_id")
    private String chatUserId;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 简介
     */
    @TableField("intro")
    private String intro;

    /**
     * 启用状态: 1-启用, 0-禁用
     */
    @TableField("enable")
    private Boolean enable;

    /**
     * 逻辑删除标记: 0-未删除, 1-已删除
     */
    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;

    /**
     * 创建人ID
     */
    @TableField(value = "create_id", fill = FieldFill.INSERT)
    private Long createId;

    /**
     * 创建人姓名
     */
    @TableField(value = "create_name", fill = FieldFill.INSERT)
    private String createName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "update_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateId;

    /**
     * 更新人姓名
     */
    @TableField(value = "update_name", fill = FieldFill.INSERT_UPDATE)
    private String updateName;

    /**
     * 最后更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
