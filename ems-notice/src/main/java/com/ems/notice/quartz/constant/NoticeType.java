package com.ems.notice.quartz.constant;
public enum NoticeType {
    ALL(-1);

    private Integer code;


    NoticeType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
