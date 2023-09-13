package com.wre.game.api.data;

import java.io.Serializable;

public class RequestExpireInfo implements Serializable {
    private static final long serialVersionUID = 289558319903469173L;
    private String path;
    private int    expire;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "RequestExpireInfo{" +
                "path='" + path + '\'' +
                ", expire=" + expire +
                '}';
    }
}
