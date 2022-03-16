package com.gupern.pnav.wechat.bean;

import org.springframework.data.repository.CrudRepository;

/**
 * @author: Gupern
 * @date: 2022/3/8 21:43
 * @description: 对DaoSubscribeMsg的CRUD接口
 * This will be AUTO IMPLEMENTED by Spring into a Bean called RepositorySubscribeMsg
 * CRUD refers Create, Read, Update, Delete
 */
public interface RepositoryUserInfo extends CrudRepository<DaoUserInfo, Integer> {
}
