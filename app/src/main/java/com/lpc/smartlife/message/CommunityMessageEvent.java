package com.lpc.smartlife.message;

public class CommunityMessageEvent {
    public final static int ClearText = 0;
    public final static int UDPDataMessage = 1;
    public final static int TCPDataMessage = 2;

    private String message;
    private int code;
    private String cmd;
    private int id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
