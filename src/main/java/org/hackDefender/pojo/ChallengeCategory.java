package org.hackDefender.pojo;

import java.util.Date;

public class ChallengeCategory {
    private Integer id;

    private Integer categoryId;

    private Integer challengeId;

    private Date createTime;

    private Date updateTime;

    public ChallengeCategory(Integer id, Integer categoryId, Integer challengeId, Date createTime, Date updateTime) {
        this.id = id;
        this.categoryId = categoryId;
        this.challengeId = challengeId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public ChallengeCategory() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}