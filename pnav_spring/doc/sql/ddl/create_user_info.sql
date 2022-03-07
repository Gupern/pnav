-- mysql中，表名区分大小写，列名大小写不敏感，所以要把驼峰改为下划线

-- openid	用户唯一标识
-- session_key	会话密钥
-- unionid	用户在开放平台的唯一标识符。本字段在满足一定条件的情况下才返回。具体参看UnionID机制说明
-- scope	对应接口	描述
-- scope.userInfo	wx.getUserInfo	用户信息
-- scope.userLocation	wx.getLocation, wx.chooseLocation	地理位置
-- scope.address	wx.chooseAddress	通讯地址
-- scope.invoiceTitle	wx.chooseInvoiceTitle	发票抬头
-- scope.werun	wx.getWeRunData	微信运动步数
-- scope.record	wx.startRecord	录音功能
-- scope.writePhotosAlbum	wx.saveImageToPhotosAlbum, wx.saveVideoToPhotosAlbum	保存到相册
-- scope.camera	 	摄像头
-- nickName	String	用户昵称
-- avatarUrl	String	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为 空。若用户更换头像，原有头像URL将失效。
-- gender	String	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
-- city	String	用户所在城市
-- province	String	用户所在省份
-- country	String	用户所在国家
-- language	String	用户的语言，简体中文为zh_CN
-- phoneNumber	String	用户绑定的手机号（国外手机号会有区号）
-- purePhoneNumber	String	没有区号的手机号
-- countryCode	String	区号

CREATE TABLE `user_info`
(
    `id`                 int  NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `openid`             text NOT NULL COMMENT 'openid',
    `session_key`        text NOT NULL COMMENT '会话密钥',
    `unionid`            text NOT NULL COMMENT '唯一标识',
    `user_info`          text NOT NULL COMMENT '用户信息',
    `user_location`      text NOT NULL COMMENT '地理位置',
    `address`            text NOT NULL COMMENT '通讯地址',
    `invoice_title`      text NOT NULL COMMENT '发票抬头',
    `werun`              text NOT NULL COMMENT '微信运动步数',
    `record`             text NOT NULL COMMENT '录音功能',
    `write_photos_album` text NOT NULL COMMENT '保存到相册',
    `camera`             text NOT NULL COMMENT '摄像头',
    `nick_name`          text NOT NULL COMMENT '用户昵称',
    `avatar_url`         text NOT NULL COMMENT '用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132，0代表640*640正方形）',
    `gender`             text NOT NULL COMMENT '用户性别，1男性，2女性，0未知',
    `city`               text NOT NULL COMMENT '用户所在城市',
    `province`           text NOT NULL COMMENT '用户所在省份',
    `country`            text NOT NULL COMMENT '用户所在国家',
    `language`           text NOT NULL COMMENT '用户使用的语言',
    `phone_number`       text NOT NULL COMMENT '用户绑定的手机号，国外手机号会有区号',
    `pure_phone_number`  text NOT NULL COMMENT '没有区号的手机号',
    `country_code`       text NOT NULL COMMENT '区号，国家号',
    `created_time`       datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`       datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '用户信息表';

