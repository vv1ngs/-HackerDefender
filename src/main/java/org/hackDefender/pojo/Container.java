package org.hackDefender.pojo;

import java.util.Date;

public class Container {
    private Integer id;

    private Integer userId;

    private Integer challengeId;

    private Integer renewCount;

    private String uuid;

    private Integer port;

    private Boolean status;

    private Date createTime;

    private Date updateTime;

    public Container(Integer id, Integer userId, Integer challengeId, Integer renewCount, String uuid, Integer port, Boolean status, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.challengeId = challengeId;
        this.renewCount = renewCount;
        this.uuid = uuid;
        this.port = port;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Container() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    public Integer getRenewCount() {
        return renewCount;
    }

    public void setRenewCount(Integer renewCount) {
        this.renewCount = renewCount;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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