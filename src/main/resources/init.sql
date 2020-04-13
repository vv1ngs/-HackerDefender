use hackerdefender;

CREATE TABLE `hackerdefender_user`
(
    `id`           int(11)     NOT NULL AUTO_INCREMENT COMMENT '用户表id',
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
    `id`          int(11)     NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `name`        varchar(50) NOT NULL COMMENT '挑战名称',
    `detail`      text COMMENT '挑战具体描述',
    `golden`      int(4)      NOT NULL COMMENT '该题值多少金币',
    `status`      tinyint(1) DEFAULT '1' COMMENT '题目状态 1-正常 0-下线',
    `create_time` datetime    NOT NULL COMMENT '创建时间',
    `update_time` datetime    NOT NULL COMMENT '最后一次更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `challenge_category`
(
    `id`           int(11)  NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `category_id`  int(11)  NOT NULL COMMENT '类别Id',
    `challenge_id` int(11)  NOT NULL COMMENT '题目Id',
    `create_time`  datetime NOT NULL COMMENT '创建时间',
    `update_time`  datetime NOT NULL COMMENT '最后一次更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;