CREATE TABLE `reminder_info`
(
    `id`           int  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `openid`       text NOT NULL COMMENT '接收者（用户）的openid',
    `type`         text NOT NULL COMMENT '提醒的方式：once, repeat',
    `interval`     text NOT NULL COMMENT '提醒的间隔，即每隔多久提醒一次，reminder_type的值非once时设置',
    `working_hour` text NOT NULL COMMENT '提醒小助手的上班时间段，reminder_type的值非once时设置',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '用户对提醒小助手的设置表';