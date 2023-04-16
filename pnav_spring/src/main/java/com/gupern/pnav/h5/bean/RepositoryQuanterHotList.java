package com.gupern.pnav.h5.bean;

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
public interface RepositoryQuanterHotList extends CrudRepository<DaoQuanterHotList, Integer> {
    @Query(value = "select * from quanter_hot_list where created_time > ?1 and created_time < ?2", nativeQuery = true)
    List<DaoQuanterHotList> findHotListByDate(String startDate, String endDate);
}
