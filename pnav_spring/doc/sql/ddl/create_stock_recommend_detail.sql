create TABLE `stock_recommend_detail`
(
    `id`           int  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `recommend_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '推荐时间',
    `stock_name`      text     DEFAULT NULL COMMENT '股票名称',
    `stock_code`      text     DEFAULT NULL COMMENT '股票代号',
    `strategy_id`       int      DEFAULT 0 COMMENT '所属策略id',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '推荐详情表';
ALTER TABLE `pnavdb`.`stock_recommend_detail`
ADD COLUMN `recommend_operation` INT NULL DEFAULT '0' COMMENT '0买入，1卖出，2持有' AFTER `strategy_id`;