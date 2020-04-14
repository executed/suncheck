package com.devserbyn.suncheck.model.enums;

public enum BotCommands {

    NOT_EXISTING(""),
    START("/start"),
    SUNRISE("/sunrise"),
    SUNSET("/sunset"),
    REMOVEME("/removeme"),
    HELP("/help");

    private final String command;

    BotCommands(String command) {
        this.command = command;
    }

    public final String getCommand() {
        return command;
    }
}
