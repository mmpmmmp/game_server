package com.wre.game.api.data;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class QqSession implements Serializable {
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
    @JSONField(name = "unionid")
    private String unionId;

    /**
     * 错误码
     * -1	系统繁忙，此时请开发者稍候再试
     * 0	请求成功
     * 40029	code 无效
     * 45011	频率限制，每个用户每分钟100次
     */
    @JSONField(name = "errcode")
    private Integer errCode;

    @JSONField(name = "errMsg")
    private String errMsg; //错误信息

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

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QqSession{");
        sb.append("openId='").append(openId).append('\'');
        sb.append(", sessionKey='").append(sessionKey).append('\'');
        sb.append(", unionId='").append(unionId).append('\'');
        sb.append(", errCode=").append(errCode);
        sb.append(", errMsg='").append(errMsg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
