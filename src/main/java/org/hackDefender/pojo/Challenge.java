package org.hackDefender.pojo;

import java.util.Date;

public class Challenge {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String detail;

    private Integer golden;

    private Boolean status;

    private String memoryLimit;

    private Long cupLimit;

    private String dockerImage;

    private Integer port;

    private Date createTime;

    private Date updateTime;

    public Challenge(Integer id, Integer categoryId, String name, String detail, Integer golden, Boolean status, String memoryLimit, Long cupLimit, String dockerImage, Integer port, Date createTime, Date updateTime) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.detail = detail;
        this.golden = golden;
        this.status = status;
        this.memoryLimit = memoryLimit;
        this.cupLimit = cupLimit;
        this.dockerImage = dockerImage;
        this.port = port;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    public String getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(String memoryLimit) {
        this.memoryLimit = memoryLimit == null ? null : memoryLimit.trim();
    }

    public Long getCupLimit() {
        return cupLimit;
    }

    public void setCupLimit(Long cupLimit) {
        this.cupLimit = cupLimit;
    }

    public String getDockerImage() {
        return dockerImage;
    }

    public void setDockerImage(String dockerImage) {
        this.dockerImage = dockerImage == null ? null : dockerImage.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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