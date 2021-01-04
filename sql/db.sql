CREATE TABLE `sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `log_type` int(2) DEFAULT NULL COMMENT '日志类型',
  `log_content` varchar(1000) DEFAULT NULL COMMENT '日志内容',
  `operate_type` int(2) DEFAULT NULL COMMENT '操作类型',
  `user_id` varchar(32) DEFAULT NULL COMMENT '操作用户账号',
  `user_name` varchar(100) DEFAULT NULL COMMENT '操作用户名称',
  `ip` varchar(100) DEFAULT NULL COMMENT 'IP',
  `method` varchar(500) DEFAULT NULL COMMENT '请求java方法',
  `request_url` varchar(255) DEFAULT NULL COMMENT '请求路径',
  `request_param` longtext COMMENT '请求参数',
  `request_type` varchar(10) DEFAULT NULL COMMENT '请求类型',
  `cost_time` bigint(20) DEFAULT NULL COMMENT '耗时',
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` int(11) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_user_id` (`user_id`) USING BTREE,
  KEY `index_log_type` (`log_type`) USING BTREE,
  KEY `index_operate_type` (`operate_type`) USING BTREE
) ENGINE=InnoDB COMMENT='系统日志表';

CREATE TABLE `sys_tenant` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '租户编码',
  `name` varchar(100) DEFAULT NULL COMMENT '租户名称',
  `begin_date` datetime DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  COMMENT='多租户信息表';

CREATE TABLE `sys_third` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `third_id` varchar(100) DEFAULT NULL COMMENT '第三方ID',
  `third_type` varchar(100) DEFAULT NULL COMMENT '第三方类型',
  `third_user` varchar(100) DEFAULT NULL COMMENT '用户账号',
  `third_name` varchar(100) DEFAULT NULL COMMENT '用户名称',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` int(11) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  COMMENT='第三方用户表';

CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_name` varchar(100) DEFAULT NULL COMMENT '登录账号',
  `real_name` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255)  DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) DEFAULT NULL COMMENT 'md5密码盐',
  `avatar` varchar(800) DEFAULT NULL COMMENT '头像',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `work_no` varchar(100)  DEFAULT NULL COMMENT '工号',
  `pos` int(11) DEFAULT NULL COMMENT '职务',
  `telephone` varchar(45)  DEFAULT NULL COMMENT '座机号',
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` int(11) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_user_name` (`user_name`) USING BTREE,
  UNIQUE KEY `uniq_sys_user_work_no` (`work_no`) USING BTREE,
  UNIQUE KEY `uniq_sys_user_phone` (`phone`) USING BTREE
) ENGINE=InnoDB COMMENT='用户表';

CREATE TABLE `sys_dict` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `dict_code` varchar(100) NOT NULL COMMENT '字典编码',
  `dict_name` varchar(100) NOT NULL COMMENT '字典名称',
  `description` varchar(255)  DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `type` tinyint(1) unsigned DEFAULT '0' COMMENT '字典类型0:string,1:int',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32)  DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `indextable_dict_code` (`dict_code`) USING BTREE
) ENGINE=InnoDB comment '字典表';

CREATE TABLE `sys_dict_item` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `dict_id` int(11) DEFAULT NULL COMMENT '字典id',
  `item_text` varchar(100) NOT NULL COMMENT '字典项文本',
  `item_value` varchar(100) NOT NULL COMMENT '字典项值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32)  DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB comment '字典项表';

CREATE TABLE `sys_depart` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '父部门ID',
  `depart_name` varchar(100) NOT NULL COMMENT '部门名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `org_code` varchar(64) NULL COMMENT '机构编码',
  `type` tinyint(1) unsigned DEFAULT '0' COMMENT '字典类型0:部门,1:公司',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `fax` varchar(32) DEFAULT NULL COMMENT '传真',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32)  DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_depart_org_code` (`org_code`) USING BTREE
) ENGINE=InnoDB COMMENT='部门表';

CREATE TABLE `sys_position` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `code` varchar(100) DEFAULT NULL COMMENT '职务编码',
  `name` varchar(100) DEFAULT NULL COMMENT '职务名称',
  `post_rank` varchar(2) DEFAULT NULL COMMENT '职级',
  `depart_id` int(11) DEFAULT NULL COMMENT '公司id',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32)  DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_code` (`code`) USING BTREE
) ENGINE=InnoDB COMMENT '职务表';