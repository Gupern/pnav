package com.gupern.pnav.h5.bean;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author: Gupern
 * @date: 2022/11/5 21:43
 * @description: 对DaoSharesRunning的CRUD接口
 * This will be AUTO IMPLEMENTED by Spring into a Bean called RepositoryXXX
 * CRUD refers Create, Read, Update, Delete
 */
public interface RepositorySharesRunning extends CrudRepository<DaoSharesRunning, Integer> {
    @Query(value = "select * from shares_running where openid = ?1", nativeQuery = true)
    List<DaoSharesRunning> findAllByOpenid(String openid);

    @Query(value = "select * from shares_running where user_id = ?1", nativeQuery = true)
    List<DaoSharesRunning> findAllByUserId(String userId);

    @Query(value = "select * from shares_running where id = ?1", nativeQuery = true)
    DaoSharesRunning findById(int sharesRunningId);

    @Query(value = "select * from shares_running where fund_record_id = ?1", nativeQuery = true)
    DaoSharesRunning findByFundRecordId(int fundRecordId);
}
