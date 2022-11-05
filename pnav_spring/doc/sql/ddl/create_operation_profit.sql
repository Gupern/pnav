create TABLE `operation_profit`
(
    `id`           int  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `openid`             text NOT NULL COMMENT 'openid',
    `fund_name`      text     DEFAULT NULL COMMENT '基金名称',
    `fund_code`      text     DEFAULT NULL COMMENT '基金代号',
    `unv`  float      DEFAULT 0 COMMENT '买入单位净值 unit net value',
    `unv_sold`  float      DEFAULT 0 COMMENT '卖出单位净值',
    `shares`  float      DEFAULT 0 COMMENT '份额',
    `service_charge`  float      DEFAULT 0 COMMENT '手续费',
    `profit`       float      DEFAULT 0 COMMENT '盈亏',
    `operation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `fund_record_id`           int  NOT NULL COMMENT '基金记录表买入份额的主键',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '操作盈亏表';