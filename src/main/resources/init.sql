create database hackerdefender default character set utf8;
use hackerdefender;

CREATE TABLE `hackerdefender_user`
(
    `id`           int(11)     NOT NULL AUTO_INCREMENT COMMENT '用户表id',
    `uuid`         varchar(50) NOT NULL COMMENT 'uuid标识符',
    `username`     varchar(50) NOT NULL COMMENT '用户名',
    `password`     varchar(50) NOT NULL COMMENT '用户密码，MD5加密',
    `email`        varchar(50) DEFAULT NULL,
    `phone`        varchar(20) DEFAULT NULL,
    `role`         int(4)      NOT NULL COMMENT '角色0-管理员,1-普通用户',
    `golden_count` int(20)     NOT NULL COMMENT '金币数量',
    `create_time`  datetime    NOT NULL COMMENT '创建时间',
    `update_time`  datetime    NOT NULL COMMENT '最后一次更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_name_unique` (`username`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `hackerdefender_category`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT '类别Id',
    `parent_id`   int(11)     DEFAULT NULL COMMENT '父类别id当id=0时说明是根节点,一级类别',
    `name`        varchar(50) DEFAULT NULL COMMENT '类别名称',
    `sort_order`  int(4)      DEFAULT NULL COMMENT '排序编号,同类展示顺序,数值相等则自然排序',
    `create_time` datetime    DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime    DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;



CREATE TABLE `hackerdefender_challenge`
(
    `id`           int(11)                   NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `category_id`  int(11)                   NOT NULL COMMENT '分类id',
    `name`         varchar(50)               NOT NULL COMMENT '挑战名称',
    `detail`       text COMMENT '挑战具体描述',
    `answer`       text COMMENT '题目的答案',
    `golden`       int(4)                    NOT NULL COMMENT '该题值多少金币',
    `status`       tinyint(1)  DEFAULT '1' COMMENT '题目状态 1-正常 0-下线',
    `memory_limit` varchar(50) default '128m' COMMENT '内存限制',
    `cup_limit`    float(50)   default 0.5 COMMENT 'cpu限制',
    `docker_image` varchar(255) NOT NULL COMMENT '题目的docker镜像',
    `script_url`   varchar(255) COMMENT '该题的攻击脚本地址',
    `check_url`   varchar(255) COMMENT '该题的验证脚本地址',
    `user_upload_path` varchar (255)  COMMENT '用户上传脚本地址',
    `log_path` varchar (255)    COMMENT '该挑战的log地址',
    `port`        int(11) NOT NULL COMMENT '题目转发到的port',
    `create_time`  datetime    default now() NOT NULL COMMENT '创建时间',
    `update_time`  datetime    default now() NOT NULL COMMENT '最后一次更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;



CREATE TABLE `challenge_container`
(
    `id`           int(11)     NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `user_id`      int(11)     NOT NULL COMMENT '用户ID',
    `challenge_id` int(11)     NOT NULL COMMENT '题目Id',
    `renew_count`  int(11)     NOT NULL COMMENT '用户重启次数',
    `container_id` varchar(50) NOT NULL COMMENT 'docker中的containerId',
    `uuid`         varchar(50) NOT NULL COMMENT 'uuid标识符',
    `port`         int(11)     NOT NULL COMMENT '映射端口',
    `status`       tinyint(1) DEFAULT '1' COMMENT '容器状态 1-正常 0-下线',
    `create_time`  datetime    NOT NULL COMMENT '创建时间',
    `update_time`  datetime    NOT NULL COMMENT '最后一次更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
