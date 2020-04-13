package com.devserbyn.suncheck.model.enums;


public enum TextResourceKeys {

    FIRST_TXT_RES(1),
    SECOND_TXT_RES(2);

    private final Integer textResourceCode;

    TextResourceKeys(Integer code) {
        this.textResourceCode = code;
    }

    public Integer getTextResourceCode() {
        return textResourceCode;
    }
}
