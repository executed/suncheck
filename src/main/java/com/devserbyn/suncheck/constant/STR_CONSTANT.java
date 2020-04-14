package com.devserbyn.suncheck.constant;

public final class STR_CONSTANT {

    // Config vars names
    public static final String BOT_TOKEN_ENV_VAR = "BOT_TOKEN";
    public static final String BOT_USERNAME_ENV_VAR = "BOT_USERNAME";
    public static final String SERVER_URL_ENV_VAR = "SERVER_URL";

    public static final String SUNRISE_UNICODE = "\uD83C\uDF05";
    public static final String SUNSET_UNICODE = "\uD83C\uDF06";

    public static final String MESSAGE_TIME_FORMAT = "H:mm";

    public static final String SUNRISE_EVENT_NAME = "sunrise";
    public static final String SUNSET_EVENT_NAME = "sunset";

    public static final String BOT_ANSWER_NEW_LINE_CHAR = "'/n'";

    public static final String SUN_EVENT_API_URL_FORMAT = "https://api.sunrise-sunset.org/json?lat=%s&lng=%s";
}
