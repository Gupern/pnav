package com.gupern.pnav.common.middleware;

/**
 * author: Gupern
 * date: 2022-11-30 22:56
 * description: use for singleton architecture cache
 *              if evolve to distribute architecture, use Redis please.
 */

import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Component
public class CodeCache {
    public static Map<String, String> stringCache = new HashMap<String, String>();
    public static Map<String, Object> objectCache = new HashMap<String, Object>();
    public static Map<String, JSONObject> jsonObjectCache = new HashMap<String, JSONObject>();

    @PostConstruct
    public void init() {
        //系统启动中。。。加载xxxCache

    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
    }

    // 每2小时执行一次缓存
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void refreshCache() {
//        init();
    }

}