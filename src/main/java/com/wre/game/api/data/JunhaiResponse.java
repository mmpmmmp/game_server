package com.wre.game.api.data;

public class JunhaiResponse {
    private int ret = 0; // 0: 失败；1：成功
    private String msg = "";
    private String content = "";

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JunhaiResponse{");
        sb.append("ret=").append(ret);
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
