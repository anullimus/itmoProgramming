package com.company.common.util;

public class Response {
    private String serverMessage;

    public Response(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public String getServerMessage() {
        return serverMessage;
    }
}
