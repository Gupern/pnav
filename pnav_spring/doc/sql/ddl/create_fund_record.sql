create TABLE `fund_record`
(
    `id`           int  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `openid`             text NOT NULL COMMENT 'openid',
    `fund_name`      text     DEFAULT NULL COMMENT '基金名称',
    `fund_code`      text     DEFAULT NULL COMMENT '基金代号',
    `operation`       int      DEFAULT 0 COMMENT '操作，0-买，1-卖，默认是买入',
    `amount`  float      DEFAULT 0 COMMENT '金额',
    `shares`  float      DEFAULT 0 COMMENT '份额',
    `net_value`  float      DEFAULT 0 COMMENT '单位净值',
    `status`       int      DEFAULT 0 COMMENT '0有效，1已逻辑删除',
    `operation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '基金记录表';