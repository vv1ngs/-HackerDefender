package org.hackDefender.vo;

/**
 * @author vvings
 * @version 2020/4/20 23:29
 */
public class ContainerVo {
    private long remainTime;
    private String ip;
    private String port;

    public long getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
