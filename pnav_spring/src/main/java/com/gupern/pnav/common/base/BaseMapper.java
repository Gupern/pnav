package com.gupern.pnav.common.base;

import java.io.Serializable;
/**
 * DAO公共基类
 * @param <Model> 这里是泛型，不是Model类
 * @param <PK> 如果是无主键，则可以用Model来跳过，如果是多主键则是Key类
 *
 * */
public interface BaseMapper<Model,PK extends Serializable> {
    int deleteByPrimaryKey(PK id);

    int insert(Model record);

    int insertSelective(Model record);

    Model selectByPrimaryKey(PK id);

    int updateByPrimaryKeySelective(Model record);

    int updateByPrimaryKey(Model record);
}
