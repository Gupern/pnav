package com.gupern.pnav.wechat.util;

/**
 * @author: Gupern
 * @date: 2022/3/9 23:27
 * @description: 定时任务，基于官网代码改写，用于定时推送给微信消息
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.gupern.pnav.wechat.util.WechatUtil.getMiniProgramAccessToken;

@Component
public class ScheduledTasks {

    // 公众号appid 从properties文件中获取
    @Value("${wechat.miniprogram.appid}")
    private String appId = "appId";
    // 公众号appsecert
    @Value("${wechat.miniprogram.appsecret.encoded}")
    private String appSecretEncoded = "asdfasdfasdf";

    // 与接口配置信息中的Token要一致
    @Value("${wechat.miniprogram.token}")
    private String token = "pnav007";

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // 固定频率，每60*1000ms = 60000ms = 1min/次
    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() throws IOException {
        log.info("The time is now {}", dateFormat.format(new Date()));
        // TODO 每分钟一次
        // 1. 检查是否为推送时间
        if (isPushTime()) {
            String accessToken = getMiniProgramAccessToken(appId, CryptoUtil.getInstance(null).Base64Decode(appSecretEncoded));
            JSONObject requestJson = JSONObject.parseObject("{\n" +
                    "  \"touser\": \"oqfv65BT-v9nULW5WMERMnzsMxHg\",\n" +
                    "  \"template_id\": \"Y7kzQsgyfzu-K1WkY5nAFbB6avGFNyK1voD9vx3_fCU\",\n" +
                    "  \"page\": \"pages/index/index\",\n" +
                    "  \"miniprogram_state\":\"developer\",\n" +
                    "  \"lang\":\"zh_CN\",\n" +
                    "  \"data\": {\n" +
                    "      \"time1\": {\n" +
                    "          \"value\": \"2015年01月05日\"\n" +
                    "      },\n" +
                    "      \"thing2\": {\n" +
                    "          \"value\": \"测试推送\"\n" +
                    "      }\n" +
                    "  }\n" +
                    "}");
            log.info(requestJson.toString());
            WechatUtil.sendSubscribeMsg(accessToken, requestJson);
        }
        // 4. 加强版： mysql 获取用户id进行推送
        // 5. 加强版： 拼装推送信息
        // 6. 加强版： 前端自定义编辑随机事项
    }

    /*
     * @author: Gupern
     * @date: 2022/3/12 23:22
     * @description:  判断是否为推送时间，目前规则写死，后续可从数据库中获取用户配置的时间
     */
    private boolean isPushTime() {
        // TODO 增加推送时间配置
        if (true) {
            return true;
        }
        return false;
    }
}