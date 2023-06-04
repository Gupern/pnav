package com.gupern.pnav.wechat.bean;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author: Gupern
 * @date: 2022/3/8 21:43
 * @description: 对DaoTaskInfo的CRUD接口
 * This will be AUTO IMPLEMENTED by Spring into a Bean called RepositoryXXX
 * CRUD refers Create, Read, Update, Delete
 */
public interface RepositoryStockRecommendDetail extends CrudRepository<DaoStockRecommendDetail, Integer> {
    @Query(value = "select * from stock_recommend_detail " +
            "where strategy_id = ?1 and recommend_time = ?2", nativeQuery = true)
    List<JSONObject> findListByStrategyId(int id, String recommend_time);

    @Query(value = "select distinct(date_format(recommend_time,'%Y-%m-%d')) from pnavdb.stock_recommend_detail where " +
            "strategy_id = ?1 order by recommend_time desc;", nativeQuery = true)
    List<String> findRecHisListByStrategyId(int strategyId);

    @Query(value = "select * from stock_recommend_detail " +
            "where recommend_operation = 0 and strategy_id = ?1 and recommend_time = ?2", nativeQuery = true)
    List<JSONObject> findBuyListByStrategyIdAndDate(int strategyId, String recommendDate);
//    @Query(value = "select * from task_info where openid = ?1 and id = ?2", nativeQuery = true)
//    List<JSONObject> findAllTasksByOpenidAndTaskId(String openid, String taskId);
//
//    @Query(value = "select * from task_info where openid = ?1", nativeQuery = true)
//    List<JSONObject> findAllTasksByOpenid(String openid);
}
