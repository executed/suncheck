package com.devserbyn.suncheck.model.enums;


public enum TextResourceKeys {

    FIRST_TXT_RES(1),
    SECOND_TXT_RES(2),
    MSG_TYPE_NOT_SUP(3),
    CMD_NOT_AVAILABLE(4),
    CMD_ONLY_SUPPORTED(5),
    SMTH_WENT_WRONG(6),
    LOC_SETUP_2ND_ATTEMPT(7),
    INITIAL_SETUP_FINISHED(8),
    START_2ND_TIME(9),
    LOCATION_SETUP(10),
    SUNRISE_NOTIF_FORMAT(11),
    SUNSET_NOTIF_FORMAT(12),
    BOT_NOT_CONF_YET(13),
    REMOVEME(14),
    HELP_CMD(15),
    SUNRISE_EMOJI_URL_ENCODED(16),
    SUNSET_EMOJI_URL_ENCODED(17),
    REMOVEME_CONFIRM(18);

    private final Integer textResourceCode;

    TextResourceKeys(Integer code) {
        this.textResourceCode = code;
    }

    public Integer getTextResourceCode() {
        return textResourceCode;
    }
}
