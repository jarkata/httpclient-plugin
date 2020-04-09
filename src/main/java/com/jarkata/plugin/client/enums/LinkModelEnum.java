package com.jarkata.plugin.client.enums;

public enum LinkModelEnum {

    P2P("p2p"),
    REGISTRY("registry");

    private String code;

    LinkModelEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
