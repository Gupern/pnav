CREATE TABLE `task_info`
(
    `id`           int  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `openid`       text NOT NULL COMMENT '接收者（用户）的openid',
    `project`      text     DEFAULT NULL COMMENT '项目，指平时完不成的项目，可为空',
    `task`         text NOT NULL COMMENT '项目分解后的任务，至少进行2分钟，有热情则随意持续进行',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '任务表';