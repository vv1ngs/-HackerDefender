package org.hackDefender.dao;

import org.hackDefender.pojo.ChallengeCategory;

public interface ChallengeCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ChallengeCategory record);

    int insertSelective(ChallengeCategory record);

    ChallengeCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChallengeCategory record);

    int updateByPrimaryKey(ChallengeCategory record);
}