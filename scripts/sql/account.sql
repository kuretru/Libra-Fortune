CREATE TABLE `account` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录上一次被动更新的时刻',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '上一次被动更新记录的用户',
  `owner` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '账户Owner',
  `name` varchar(16) COLLATE utf8mb4_general_ci NOT NULL COMMENT '账户名称',
  `icon` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '图标',
  `can_hold_funds` tinyint(1) NOT NULL COMMENT '是否可以储蓄',
  `sequence` int NOT NULL COMMENT '排序标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_owner_name` (`owner`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账户管理·账户表'

CREATE TABLE `account_balance` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录上一次被动更新的时刻',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '上一次被动更新记录的用户',
  `account_id` bigint unsigned NOT NULL COMMENT '外键，账户ID',
  `date` date NOT NULL COMMENT '日期，表达截止那一天的余额',
  `balance` decimal(9,2) NOT NULL COMMENT '余额',
  PRIMARY KEY (`id`),
  KEY `idx_account_id` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账户管理·账户余额表'
