package com.fpush.plat.network.demo;

import java.io.Serializable;

public class SendMsgDto<T extends Serializable> {
    private Serializable message;
    private int code;

    public SendMsgDto(Serializable message, int code) {
        this.message = message;
        this.code = code;
    }

    public SendMsgDto() {
    }

    public Serializable getMessage() {
        return message;
    }

    public void setMessage(Serializable message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}