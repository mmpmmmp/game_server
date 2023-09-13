package com.wre.game.api.data;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class ToutiaoSession implements Serializable {
    /**
     * 用户唯一标识
     */
    @JSONField(name = "openid")
    private String openId;

    /**
     * 会话密钥
     */
    @JSONField(name = "session_key")
    private String sessionKey;

    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
     */
    @JSONField(name = "anonymous_openid")
    private String anonymousOpenId;

    /**
     * 错误码
     * -1	系统繁忙，此时请开发者稍候再试
     * 0	请求成功
     * 40029	code 无效
     * 45011	频率限制，每个用户每分钟100次
     */
    @JSONField(name = "error")
    private Integer error;

    @JSONField(name = "message")
    private String message; //错误信息

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getAnonymousOpenId() {
        return anonymousOpenId;
    }

    public void setAnonymousOpenId(String anonymousOpenId) {
        this.anonymousOpenId = anonymousOpenId;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("openId", openId)
                .append("sessionKey", sessionKey)
                .append("anonymousOpenId", anonymousOpenId)
                .append("error", error)
                .append("message", message)
                .toString();
    }
}
