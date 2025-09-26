package com.momentum.suite.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class UserPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    @TableField("password")
    private String encryptedPassword;

    private String email;

    private String mobile;

    /**
     * 启用状态: 1-启用, 0-禁用
     */
    @TableField("enable_flag")
    private Boolean enableFlag;

    /**
     * 创建人ID
     */
    @TableField(value = "create_id", fill = FieldFill.INSERT) // 插入时自动填充
    private Long createId;

    /**
     * 创建人姓名
     */
    @TableField(value = "create_name", fill = FieldFill.INSERT) // 插入时自动填充
    private String createName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT) // 插入时自动填充
    private LocalDateTime createTime;

    /**
     * 最后更新人ID
     */
    @TableField(value = "update_id", fill = FieldFill.INSERT_UPDATE) // 插入和更新时都填充
    private Long updateId;

    /**
     * 最后更新人姓名
     */
    @TableField(value = "update_name", fill = FieldFill.INSERT_UPDATE) // 插入和更新时都填充
    private String updateName;

    /**
     * 最后更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE) // 插入和更新时都填充
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标记: 0-未删除, 1-已删除
     */
    @TableLogic // 标记为逻辑删除字段
    @TableField("del_flag")
    private Boolean delFlag;
}
