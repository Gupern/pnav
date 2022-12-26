create TABLE `shares_running`
(
    `id`           int  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `openid`             text NOT NULL COMMENT 'openid',
    `fund_name`      text     DEFAULT NULL COMMENT '基金名称',
    `fund_code`      text     DEFAULT NULL COMMENT '基金代号',
    `purchase_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `unv`  float      DEFAULT 0 COMMENT '买入单位净值 unit net value',
    `shares`  float      DEFAULT 0 COMMENT '买入份额',
    `shares_remaining`  float      DEFAULT 0 COMMENT '剩余份额',
    `zero_cost`  int      DEFAULT 0 COMMENT '是否零成本 0否 1是',
    `fund_record_id`           int  NOT NULL COMMENT '基金记录表买入份额的主键',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '份额运营表';