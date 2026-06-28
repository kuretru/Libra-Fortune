CREATE TABLE `metadata_category` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录上一次被动更新的时刻',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '上一次被动更新记录的用户',
  `parent_id` bigint unsigned DEFAULT NULL COMMENT '父分类ID',
  `name` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `sequence` int NOT NULL COMMENT '排序标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_parent_id_name` (`parent_id`,`name`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='元数据·账本分类表'

CREATE TABLE `metadata_currency` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录上一次被动更新的时刻',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '上一次被动更新记录的用户',
  `code` char(3) COLLATE utf8mb4_general_ci NOT NULL COMMENT '货币代码',
  `symbol` char(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '货币符号',
  `name` varchar(16) COLLATE utf8mb4_general_ci NOT NULL COMMENT '货币名称',
  `sequence` int NOT NULL COMMENT '排序标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='元数据·货币单位表'

CREATE TABLE `metadata_tag_set` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录上一次被动更新的时刻',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '上一次被动更新记录的用户',
  `name` varchar(16) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签组名称',
  `required` tinyint(1) NOT NULL COMMENT '该标签组是否必选',
  `allow_multiple` tinyint(1) NOT NULL COMMENT '是否允许多选',
  `sequence` int NOT NULL COMMENT '排序标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='元数据·标签组表'

CREATE TABLE `metadata_tag_set_item` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键，自增',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录主动创建的时刻',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主动创建记录的用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录上一次被动更新的时刻',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '上一次被动更新记录的用户',
  `set_id` bigint unsigned NOT NULL COMMENT '关联标签组ID',
  `name` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `sequence` int NOT NULL COMMENT '排序标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_set_id_name` (`set_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='元数据·标签组元素表'
