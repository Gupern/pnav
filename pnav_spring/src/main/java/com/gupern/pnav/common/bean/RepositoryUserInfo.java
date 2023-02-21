package com.gupern.pnav.common.bean;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author: Gupern
 * @date: 2022/3/8 21:43
 * @description: 对DaoXXX的CRUD接口
 * This will be AUTO IMPLEMENTED by Spring into a Bean called RepositoryXXX
 * CRUD refers Create, Read, Update, Delete
 */
public interface RepositoryUserInfo extends CrudRepository<DaoUserInfo, Integer> {
    @Query(value = "select * from user_info where id = ?1", nativeQuery = true)
    DaoUserInfo findById(String packageId);

    @Query(value = "select * from user_info where phone_number = ?1", nativeQuery = true)
    DaoUserInfo findByPhone(String phone);
}
