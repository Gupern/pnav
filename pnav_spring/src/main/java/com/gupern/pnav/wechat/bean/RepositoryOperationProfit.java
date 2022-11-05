package com.gupern.pnav.wechat.bean;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * @author: Gupern
 * @date: 2022/11/5 21:43
 * @description: 对DaoOperationProfit的CRUD接口
 * This will be AUTO IMPLEMENTED by Spring into a Bean called RepositoryXXX
 * CRUD refers Create, Read, Update, Delete
 */
public interface RepositoryOperationProfit extends CrudRepository<DaoOperationProfit, Integer> {
}
