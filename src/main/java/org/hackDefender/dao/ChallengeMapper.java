package org.hackDefender.dao;

import org.hackDefender.pojo.Challenge;

public interface ChallengeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Challenge record);

    int insertSelective(Challenge record);

    Challenge selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Challenge record);

    int updateByPrimaryKey(Challenge record);

    int checkId(Integer challengeId);
}