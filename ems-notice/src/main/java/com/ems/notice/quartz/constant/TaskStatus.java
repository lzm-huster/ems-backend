package com.ems.notice.quartz.constant;


public enum TaskStatus {

    START(1, "开启"),
    STOP(0, "关闭");

    private Integer code;

    private String msg;

    TaskStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }
}
