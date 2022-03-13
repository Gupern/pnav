package com.gupern.pnav.wechat.bean;

import com.alibaba.fastjson.JSONObject;
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
public interface RepositorySubscribeMsg extends CrudRepository<DaoSubscribeMsg, Integer> {
    /**
     * 在自定义方法查询中，
     * 我们要使用new一个对象来封装实例
     * 然后用对应的实体类来替代表
     * 所以我们的实体类需要去绑定表
     * 注意：在new的使用，实体类中要有对应的构造器
     * nativeQuery = true  代表sql写法，否则默认为hql写法
     */
    @Query(value = "select distinct from_user_name, template_id from subscribe_msg", nativeQuery = true)
    List<JSONObject> findAllFromUserName();

}
