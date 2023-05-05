package com.gupern.pnav.h5.bean;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

/**
 * @author: Gupern
 * @date: 2022/3/8 21:43
 * @description: 对DaoFundRecord的CRUD接口
 * This will be AUTO IMPLEMENTED by Spring into a Bean called RepositoryXXX
 * CRUD refers Create, Read, Update, Delete
 */
public interface RepositoryQuanterStockBasic extends CrudRepository<DaoQuanterStockBasic, Integer> {
    @Query(value = "select name from quanter_stock_basic where symbol in ?1",
            nativeQuery = true)
    List<String> findNameListBySymbolList(Set<String> symbolList);
}
