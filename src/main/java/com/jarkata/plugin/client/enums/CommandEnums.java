package com.jarkata.plugin.client.enums;

public enum CommandEnums {
    COMMAND_SAVE("save"),
    COMMAND_SEND("send"),
    COMMAND_UPLOAD("UPLOAD"),
    ;
    private String command;

    CommandEnums(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
