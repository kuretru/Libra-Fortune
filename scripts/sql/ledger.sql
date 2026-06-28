CREATE TABLE `ledger_v2` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录上一次被动更新的时刻',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上一次被动更新记录的用户',
  `owner` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '账本Owner',
  `name` varchar(16) COLLATE utf8mb4_general_ci NOT NULL COMMENT '账本名称',
  `sequence` int NOT NULL COMMENT '排序标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_owner_name` (`owner`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账本管理·账本表'

CREATE TABLE `ledger_v2_entry` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录上一次被动更新的时刻',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上一次被动更新记录的用户',
  `ledger_id` bigint unsigned NOT NULL COMMENT '外键，账本ID',
  `category_id_l1` bigint unsigned NOT NULL COMMENT '外键，一级分类ID',
  `category_id_l2` bigint unsigned NOT NULL COMMENT '外键，二级分类ID',
  `type` varchar(16) COLLATE utf8mb4_general_ci NOT NULL COMMENT '枚举值，条目类型',
  `date` date NOT NULL COMMENT '交易日期',
  `name` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '条目名称',
  `original_amount` decimal(10,2) NOT NULL COMMENT '原始消费金额',
  `original_currency` char(3) COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始消费货币',
  `settlement_amount` decimal(10,2) NOT NULL COMMENT '结算金额',
  `settlement_currency` char(3) COLLATE utf8mb4_general_ci NOT NULL COMMENT '结算货币',
  `remark` text COLLATE utf8mb4_general_ci COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_main` (`ledger_id`,`category_id_l1`,`category_id_l2`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账本管理·账本条目表'

CREATE TABLE `ledger_v2_entry_detail` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录上一次被动更新的时刻',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上一次被动更新记录的用户',
  `entry_id` bigint unsigned NOT NULL COMMENT '外键，条目ID',
  `username` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '归属用户名',
  `lock_type` varchar(16) COLLATE utf8mb4_general_ci NOT NULL COMMENT '百分比或金额锁定方式',
  `payment_chain` json DEFAULT NULL COMMENT '外键，付款链',
  `funded_ratio` decimal(5,2) unsigned NOT NULL COMMENT '百分比，承担比例',
  `amount` decimal(10,2) NOT NULL COMMENT '承担金额',
  PRIMARY KEY (`id`),
  KEY `idx_entry_id` (`entry_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账本管理·账本条目详情表'

CREATE TABLE `ledger_v2_entry_tag` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `entry_id` bigint unsigned NOT NULL COMMENT '外键，条目ID',
  `tag_id` bigint unsigned NOT NULL COMMENT '外键，标签ID',
  PRIMARY KEY (`id`),
  KEY `idx_entry_id` (`entry_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账本管理·账本条目标签表'

CREATE TABLE `ledger_v2_member` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录上一次被动更新的时刻',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上一次被动更新记录的用户',
  `ledger_id` bigint unsigned NOT NULL COMMENT '外键，账本ID',
  `username` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '成员用户名',
  `default_funded_ratio` decimal(5,2) NOT NULL COMMENT '百分数，默认承担比例',
  PRIMARY KEY (`id`),
  KEY `uk_ledger_id_username` (`ledger_id`,`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账本管理·账本成员表'
