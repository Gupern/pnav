package com.gupern.pnav.wechat.bean;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author: Gupern
 * @date: 2022/3/8 21:43
 * @description: 对DaoFundRecord的CRUD接口
 * This will be AUTO IMPLEMENTED by Spring into a Bean called RepositoryXXX
 * CRUD refers Create, Read, Update, Delete
 */
public interface RepositoryFundRecord extends CrudRepository<DaoFundRecord, Integer> {
    @Transactional
    @Modifying
    @Query(value = "update fund_record set status = 1 where id = ?1 and openid = ?2", nativeQuery = true)
    int deleteLogically(int id, String openid);

    @Query(value = "select * from fund_record where openid = ?1", nativeQuery = true)
    List<DaoFundRecord> findAllByOpenid(String openid);
}
