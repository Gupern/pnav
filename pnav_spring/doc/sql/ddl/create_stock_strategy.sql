create TABLE `stock_strategy`
(
    `id`           int  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `strategy_name`      text     DEFAULT NULL COMMENT '策略名称',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '推荐策略信息表';