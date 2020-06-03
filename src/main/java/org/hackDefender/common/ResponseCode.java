package org.hackDefender.common;

/**
 * @author vvings
 * @version 2020/3/20 18:15
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    PROGRAMMER_ERROR(-1, "PROGRAMMER_ERROR"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),
    NEED_ADD_CONTAINER(3, "NEED_ADD_CONTAINER"),
    PERMISSION_DENIED(4, "PERMISSION_DENIED");
    
    private final int code;

    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
