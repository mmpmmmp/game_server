package com.wre.game.api.constants;

import cn.hutool.crypto.SecureUtil;

public class SdkResponseDataMessage {
    /**应用 ID   */
    private Integer AppID;
    /**结果编号成功：1  失败：非 1、91 订单数据不一致： 91   */
    private Integer ResultCode;
    /**结果描述   */
    private String ResultMsg;
    /**签名   */
    private String Sign;

    public SdkResponseDataMessage(Integer appID, Integer resultCode, String resultMsg, String content,String appSecret) {
        AppID = appID;
        ResultCode = resultCode;
        ResultMsg = resultMsg;
        Content = content;
        Sign= SecureUtil.md5(appID+resultCode+appSecret);
    }

    @Override
    public String toString() {
        return "SdkResponseDataMessage{" +
                "AppID=" + AppID +
                ", ResultCode=" + ResultCode +
                ", ResultMsg='" + ResultMsg + '\'' +
                ", sign='" + Sign + '\'' +
                ", Content='" + Content + '\'' +
                '}';
    }

    /**返回内容，此处 默认为空字符串   */
    private String Content;

    public Integer getAppID() {
        return AppID;
    }

    public void setAppID(Integer appID) {
        AppID = appID;
    }

    public Integer getResultCode() {
        return ResultCode;
    }

    public void setResultCode(Integer resultCode) {
        ResultCode = resultCode;
    }

    public String getResultMsg() {
        return ResultMsg;
    }

    public void setResultMsg(String resultMsg) {
        ResultMsg = resultMsg;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
