package org.hackDefender.dao;

import org.hackDefender.pojo.Container;

public interface ContainerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Container record);

    int insertSelective(Container record);

    Container selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Container record);

    int updateByPrimaryKey(Container record);
}