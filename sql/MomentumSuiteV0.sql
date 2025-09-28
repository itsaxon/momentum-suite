DROP DATABASE IF EXISTS `momentum-suite`;

CREATE DATABASE  `momentum-suite` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `momentum-suite`;

-- 用户信息

CREATE TABLE `t_app_user_info` (
  `id` bigint NOT NULL COMMENT '主键',
  `chat_user_id` varchar(100) NOT NULL DEFAULT '' COMMENT '聊天服务用户id',
  `mobile` varchar(100) NOT NULL DEFAULT '' COMMENT '手机号',
  `nickname` varchar(100) NOT NULL DEFAULT '' COMMENT '昵称',
  `avatar` varchar(100) NOT NULL DEFAULT '' COMMENT '头像',
  `intro` varchar(100) NOT NULL DEFAULT '' COMMENT '简介',
  `enable` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '启用状态: 1-启用, 0-禁用',
  `del_flag` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除标记: 0-未删除, 1-已删除',
  `create_id` bigint NOT NULL COMMENT '创建人ID',
  `create_name` varchar(15) NOT NULL DEFAULT '' COMMENT '创建人姓名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` bigint NOT NULL COMMENT '更新人ID',
  `update_name` varchar(15) NOT NULL DEFAULT '' COMMENT '更新人姓名',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='app用户信息表';





