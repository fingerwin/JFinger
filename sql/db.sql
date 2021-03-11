CREATE TABLE QRTZ_JOB_DETAILS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(200) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CRON_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(200) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CALENDARS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    CALENDAR_NAME  VARCHAR(200) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    SCHED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(200) NULL,
    JOB_GROUP VARCHAR(200) NULL,
    IS_NONCONCURRENT VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);

CREATE TABLE QRTZ_SCHEDULER_STATE
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40) NOT NULL,
    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

CREATE TABLE `sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `log_type` int(2) DEFAULT NULL COMMENT '日志类型',
  `log_content` varchar(1000) DEFAULT NULL COMMENT '日志内容',
  `operate_type` int(2) DEFAULT NULL COMMENT '操作类型',
  `user_id` varchar(32) DEFAULT NULL COMMENT '操作用户账号',
  `user_name` varchar(100) DEFAULT NULL COMMENT '操作用户名称',
  `ip` varchar(100) DEFAULT NULL COMMENT 'IP',
  `function_name` varchar(500) DEFAULT NULL COMMENT '调用函数',
  `request_url` varchar(255) DEFAULT NULL COMMENT '请求路径',
  `request_param` longtext COMMENT '请求参数',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `cost_time` bigint(20) DEFAULT NULL COMMENT '耗时',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
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
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  COMMENT='多租户信息表';

CREATE TABLE `sys_third` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `third_id` varchar(100) DEFAULT NULL COMMENT '第三方ID',
  `third_type` varchar(100) DEFAULT NULL COMMENT '第三方类型',
  `third_user` varchar(100) DEFAULT NULL COMMENT '用户账号',
  `third_name` varchar(100) DEFAULT NULL COMMENT '用户名称',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,
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
  `org_code` varchar(64) DEFAULT NULL COMMENT '机构编码',
  `work_no` varchar(100)  DEFAULT NULL COMMENT '工号',
  `pos` int(11) DEFAULT NULL COMMENT '职务',
  `telephone` varchar(45)  DEFAULT NULL COMMENT '座机号',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,
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
  `type` tinyint(1) unsigned DEFAULT '0' COMMENT '字典类型0:string,1:int',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,
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
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,
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
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_depart_org_code` (`org_code`) USING BTREE
) ENGINE=InnoDB COMMENT='部门表';

CREATE TABLE `sys_position` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `code` varchar(100) DEFAULT NULL COMMENT '职务编码',
  `name` varchar(100) DEFAULT NULL COMMENT '职务名称',
  `post_rank` varchar(2) DEFAULT NULL COMMENT '职级',
  `depart_id` int(11) DEFAULT NULL COMMENT '公司id',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_code` (`code`) USING BTREE
) ENGINE=InnoDB COMMENT '职务表';

CREATE TABLE `sys_role` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `role_code` varchar(100) NOT NULL COMMENT '角色编码',
  `role_name` varchar(200) DEFAULT NULL COMMENT '角色名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_sys_role_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB COMMENT='角色表';

CREATE TABLE `sys_permission` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '父id',
  `name` varchar(100) DEFAULT NULL COMMENT '菜单标题',
  `url` varchar(255) DEFAULT NULL COMMENT '路径',
  `component` varchar(255) DEFAULT NULL COMMENT '组件',
  `component_name` varchar(100) DEFAULT NULL COMMENT '组件名字',
  `redirect` varchar(255) DEFAULT NULL COMMENT '一级菜单跳转地址',
  `menu_type` int DEFAULT NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
  `perms` varchar(255) DEFAULT NULL COMMENT '菜单权限编码',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `image` varchar(255) DEFAULT NULL COMMENT '宫格图片',
  `route_menu` tinyint(1) DEFAULT '1' COMMENT '是否路由菜单',
  `leaf` tinyint(1) DEFAULT '0' COMMENT '是否叶子节点',
  `keep_alive` tinyint(1) DEFAULT '1' COMMENT '是否缓存该页',
  `hidden` tinyint(1) DEFAULT '0' COMMENT '是否隐藏路由',
  `always_show` tinyint(1) DEFAULT NULL COMMENT '聚合子路由: 1是0否',
  `target` tinyint(1) DEFAULT NULL COMMENT '外链菜单打开方式 0/内部打开 1/外部打开',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) not null DEFAULT '1' COMMENT '状态',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='菜单权限表';

CREATE TABLE `sys_user_role` (
  `id` int(11)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='用户角色表';

CREATE TABLE `sys_user_depart` (
  `id` int(11)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11)  DEFAULT NULL COMMENT '用户id',
  `dept_id` int(11)  DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB COMMENT='用户部门表';

CREATE TABLE `sys_role_permission` (
  `id` int(11)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='角色权限表';

CREATE TABLE `sys_area` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_id` varchar(20) NOT NULL COMMENT '父级编码',
  `area_id` varchar(20) NOT NULL COMMENT '区划编码',
  `area_name` char(64) NOT NULL COMMENT '区划名称',
  `latitude` double DEFAULT NULL COMMENT '纬度',
  `longitude` double DEFAULT NULL COMMENT '经度',
  PRIMARY KEY (`id`),
  KEY `area_id` (`area_id`)
) ENGINE=InnoDB COMMENT='镇数据库';