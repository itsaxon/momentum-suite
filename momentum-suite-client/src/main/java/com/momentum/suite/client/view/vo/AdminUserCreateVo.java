package com.momentum.suite.client.view.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AdminUserCreateVo {

    /**
     * 新创建用户的ID
     */
    private String userId;
}