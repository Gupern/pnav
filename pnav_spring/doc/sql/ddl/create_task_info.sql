CREATE TABLE `task_info`
(
    `id`           int  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `openid`       text NOT NULL COMMENT '接收者（用户）的openid',
    `project`      text     DEFAULT NULL COMMENT '项目，指平时完不成的项目，可为空',
    `task`         text NOT NULL COMMENT '项目分解后的任务，至少进行2分钟，有热情则随意持续进行',
    `comment`      text NOT NULL COMMENT '任务执行心得',
    `status`       int      DEFAULT 0 COMMENT '任务是否已完成，0未完成，1已完成',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '任务表';
alter table task_info modify `comment` text default null comment '任务执行心得';
alter table task_info add `count` bigint DEFAULT 0 comment '该任务执行完成的次数' after `comment`;
alter table task_info add `change_count` bigint DEFAULT 0 comment '该任务换一个的次数' after `comment`;
