-- mysql中，表名区分大小写，列名大小写不敏感，所以要把驼峰改为下划线

-- {
--   "ToUserName": "gh_123456789abc",
--   "FromUserName": "o7esq5OI1Uej6Xixw1lA2H7XDVbc",
--   "CreateTime": "1620973045",
--   "MsgType": "event",
--   "Event": "subscribe_msg_popup_event",
--   "List": [   {
--         "TemplateId": "hD-ixGOhYmUfjOnI8MCzQMPshzGVeux_2vzyvQu7O68",
--         "SubscribeStatusString": "accept",
--         "PopupScene": "0"
--     }],
-- }
--
-- ToUserName	小程序帐号ID
-- FromUserName	用户openid
-- CreateTime	时间戳
-- TemplateId	模板id（一次订阅可能有多个id）
-- SubscribeStatusString	订阅结果（accept接收；reject拒收）
-- PopupScene	弹框场景，0代表在小程序页面内

CREATE TABLE `subscribe_msg`
(
    `id`                      int  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `to_user_name`            text NOT NULL COMMENT '小程序账号ID',
    `create_time`             text NOT NULL COMMENT '时间戳',
    `template_id`             text NOT NULL COMMENT '模板id, 一次订阅可能有多个id',
    `subscribe_status_string` text NOT NULL COMMENT '订阅结果（accept接收；reject拒收）',
    `popup_scene`             text NOT NULL COMMENT '弹窗场景，0代表在小程序页面内',
    `msg_type`                text NOT NULL COMMENT '信息类型，默认为event',
    `from_user_name`          text NOT NULL COMMENT '订阅者openid',
    `event`                   text NOT NULL COMMENT '信息事件',
    `created_time`            datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`            datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '订阅消息表';

alter table subscribe_msg modify `popup_scene` text default null comment '弹窗场景，0代表在小程序页面内';
alter table subscribe_msg modify `msg_type` text default null comment '信息类型，默认为event';
alter table subscribe_msg modify `event` text default null comment '信息事件';
