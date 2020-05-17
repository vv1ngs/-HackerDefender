package org.hackDefender.dao;

import org.apache.ibatis.annotations.Param;
import org.hackDefender.pojo.Container;

import java.util.List;

public interface ContainerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Container record);

    int insertSelective(Container record);

    Container selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Container record);

    int updateByPrimaryKey(Container record);

    Container selectByUid(Integer userId);

    List<Container> selectByTime(@Param("date") String date);

    Container selectByUidAndCId(@Param("userId") Integer userId);

    int checkUid(Integer userId);

    List<Container> selectAll();

    String selectContainerID(Integer userId);

    int selectRenewCountById(Integer userId);
}