package com.gupern.pnav.wechat.util;

/**
 * @author: Gupern
 * @date: 2022/3/9 23:27
 * @description: 定时任务，基于官网代码改写，用于定时推送给微信消息
 * Copyright 2012-2015 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.util.CryptoUtil;
import com.gupern.pnav.wechat.bean.RepositorySubscribeMsg;
import com.gupern.pnav.wechat.bean.RepositoryTaskInfoMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    // 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
    @Value("${wechat.miniprogram.state}")
    private String miniprogramState = "developer";

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // 推送时间的pattern，30即每小时30分进行推送
    @Value("${wechat.miniprogram.push.pattern}")
    private String pattern = "30";

    @Autowired
    private RepositorySubscribeMsg repositorySubscribeMsg;

    @Autowired
    private RepositoryTaskInfoMsg repositoryTaskInfoMsg;

    /*
     * @author: Gupern
     * @date: 2022/3/13 11:20
     * @description: 定时任务，每分钟触发一次，然后查看是否满足用户订阅的时候，若满足，则进行推送
     * cron 定时， 每周一到周五，9点5分推送
     * from_user_name == openid == touser
     */
    @Scheduled(cron = "0 5 9 ? * MON-FRI")
//    @Scheduled(cron = "0 40 9 * * *")  // 调试用
    public void reportCurrentTime() throws IOException {
        log.info("The time is now {}", dateFormat.format(new Date()));
        // 从数据库中获取订阅消息
        List<JSONObject> allFromUserName = repositorySubscribeMsg.findAllFromUserName();
        log.info("allFromUserName:{}", allFromUserName.toString());

        // 循环遍历，为所有订阅者进行推送
        for (JSONObject item : allFromUserName) {
            String openid = item.getString("from_user_name");
            String templateId = item.getString("template_id");

            List<JSONObject> allTasksList = repositoryTaskInfoMsg.findAllTasksByOpenid(openid);
            log.info("openid:{}, allTasksList:{}", openid, allTasksList);

            JSONObject taskInfo = WechatUtil.getRandomTask(allTasksList);
            // 如果用户没设置任务，则跳过
            if (taskInfo == null) {
                continue;
            }
            String thing = taskInfo.getString("task");

            JSONObject tmpThing2 = new JSONObject();
            tmpThing2.put("value", thing);

            // 获取当前时间
            JSONObject tmpTime1 = new JSONObject();
            String nowadate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date());
            tmpTime1.put("value", nowadate);

            JSONObject tmpData = new JSONObject();
            tmpData.put("time1", tmpTime1);
            tmpData.put("thing2", tmpThing2);

            JSONObject requestJson = new JSONObject();
            requestJson.put("data", tmpData);
            requestJson.put("touser", openid);
            requestJson.put("template_id", templateId);
            String page = String.format("pages/index/index?openid=%s&taskId=%s", openid, taskInfo.getString("id"));
            requestJson.put("page", page);
            requestJson.put("miniprogram_state", miniprogramState);
            requestJson.put("lang", "zh_CN");
            log.info("requestJson:{}", requestJson);

            // 获取token
            String accessToken = getMiniProgramAccessToken(appId, CryptoUtil.getInstance(null).Base64Decode(appSecretEncoded));

            // 进行推送
            WechatUtil.sendSubscribeMsg(accessToken, requestJson);
        }
    }
}