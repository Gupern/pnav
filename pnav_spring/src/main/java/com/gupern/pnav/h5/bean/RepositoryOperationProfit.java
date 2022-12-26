package com.gupern.pnav.h5.bean;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author: Gupern
 * @date: 2022/11/5 21:43
 * @description: 对DaoOperationProfit的CRUD接口
 * This will be AUTO IMPLEMENTED by Spring into a Bean called RepositoryXXX
 * CRUD refers Create, Read, Update, Delete
 */
public interface RepositoryOperationProfit extends CrudRepository<DaoOperationProfit, Integer> {
    @Query(value = "select * from operation_profit where openid = ?1", nativeQuery = true)
    List<DaoOperationProfit> findAllByOpenid(String openid);

    @Query(value = "select * from operation_profit where user_id = ?1", nativeQuery = true)
    List<DaoOperationProfit> findAllByUserId(String userId);

    @Query(value = "select * from operation_profit where id = ?1", nativeQuery = true)
   DaoOperationProfit findById(int operationProfitId);
}
