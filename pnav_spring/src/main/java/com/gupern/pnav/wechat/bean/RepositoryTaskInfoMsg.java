package com.gupern.pnav.wechat.bean;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: Gupern
 * @date: 2022/3/8 21:43
 * @description: 对DaoXXX的CRUD接口
 * This will be AUTO IMPLEMENTED by Spring into a Bean called RepositoryXXX
 * CRUD refers Create, Read, Update, Delete
 */
public interface RepositoryTaskInfoMsg extends CrudRepository<DaoTaskInfo, Integer> {
    @Query(value = "select * from task_info where openid = ?1 and id = ?2", nativeQuery = true)
    List<JSONObject> findAllTasksByOpenidAndTaskId(String openid, String taskId);

    @Query(value = "select * from task_info where openid = ?1", nativeQuery = true)
    List<JSONObject> findAllTasksByOpenid(String openid);
}
