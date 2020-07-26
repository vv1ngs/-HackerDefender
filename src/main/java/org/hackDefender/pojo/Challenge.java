package org.hackDefender.pojo;

import java.util.Date;

public class Challenge {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String detail;

    private String answer;

    private Integer golden;

    private Boolean status;

    private String memoryLimit;

    private Double cupLimit;

    private String dockerImage;

    private String scriptUrl;

    private String checkUrl;

    private String userUploadPath;

    private String logPath;

    private Integer port;

    private Date createTime;

    private Date updateTime;

    public Challenge(Integer id, Integer categoryId, String name, String detail, String answer, Integer golden, Boolean status, String memoryLimit, Double cupLimit, String dockerImage, String scriptUrl, String checkUrl, String userUploadPath, String logPath, Integer port, Date createTime, Date updateTime) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.detail = detail;
        this.answer = answer;
        this.golden = golden;
        this.status = status;
        this.memoryLimit = memoryLimit;
        this.cupLimit = cupLimit;
        this.dockerImage = dockerImage;
        this.scriptUrl = scriptUrl;
        this.checkUrl = checkUrl;
        this.userUploadPath = userUploadPath;
        this.logPath = logPath;
        this.port = port;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
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

    public Double getCupLimit() {
        return cupLimit;
    }

    public void setCupLimit(Double cupLimit) {
        this.cupLimit = cupLimit;
    }

    public String getDockerImage() {
        return dockerImage;
    }

    public void setDockerImage(String dockerImage) {
        this.dockerImage = dockerImage == null ? null : dockerImage.trim();
    }

    public String getScriptUrl() {
        return scriptUrl;
    }

    public void setScriptUrl(String scriptUrl) {
        this.scriptUrl = scriptUrl == null ? null : scriptUrl.trim();
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl == null ? null : checkUrl.trim();
    }

    public String getUserUploadPath() {
        return userUploadPath;
    }

    public void setUserUploadPath(String userUploadPath) {
        this.userUploadPath = userUploadPath == null ? null : userUploadPath.trim();
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath == null ? null : logPath.trim();
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