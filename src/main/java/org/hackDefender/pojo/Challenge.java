package org.hackDefender.pojo;

import java.util.Date;

public class Challenge {
    private Integer id;

    private String name;

    private String detail;

    private Integer golden;

    private Boolean status;

    private Date createTime;

    private Date updateTime;

    public Challenge(Integer id, String name, String detail, Integer golden, Boolean status, Date createTime, Date updateTime) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.golden = golden;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Challenge() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getGolden() {
        return golden;
    }

    public void setGolden(Integer golden) {
        this.golden = golden;
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