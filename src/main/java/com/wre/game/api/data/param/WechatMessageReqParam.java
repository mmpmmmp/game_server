package com.wre.game.api.data.param;


import com.fasterxml.jackson.annotation.JsonProperty;

public class WechatMessageReqParam {
    @JsonProperty("ToUserName")
    private String ToUserName;//小程序的原始ID

    @JsonProperty("FromUserName")
    private String FromUserName;//发送者的openid

    @JsonProperty("CreateTime")
    private Long CreateTime;//消息创建时间(整型）

    @JsonProperty("MsgType")
    private String MsgType;//miniprogrampage, image, text, event
//    private String MsgId;

    /**
     * for text type
     */
    @JsonProperty("Content")
    private String Content;

    /**
     * for image type
     */
    @JsonProperty("PicUrl")
    private String PicUrl;

    @JsonProperty("MediaId")
    private String MediaId;

    /**
     * for miniprogrampage type
     */
    @JsonProperty("Title")
    private String Title;

    @JsonProperty("AppId")
    private String AppId;

    @JsonProperty("PagePath")
    private String PagePath;

    @JsonProperty("ThumbUrl")
    private String ThumbUrl;

    @JsonProperty("ThumbMediaId")
    private String ThumbMediaId;

    /**
     * for event type
     */
    @JsonProperty("Event")
    private String Event;

    @JsonProperty("SessionFrom")
    private String SessionFrom;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

    public String getPagePath() {
        return PagePath;
    }

    public void setPagePath(String pagePath) {
        PagePath = pagePath;
    }

    public String getThumbUrl() {
        return ThumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        ThumbUrl = thumbUrl;
    }

    public String getThumbMediaId() {
        return ThumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        ThumbMediaId = thumbMediaId;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getSessionFrom() {
        return SessionFrom;
    }

    public void setSessionFrom(String sessionFrom) {
        SessionFrom = sessionFrom;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WechatMessageReqParam{");
        sb.append("ToUserName='").append(ToUserName).append('\'');
        sb.append(", FromUserName='").append(FromUserName).append('\'');
        sb.append(", MsgType='").append(MsgType).append('\'');
        sb.append(", Content='").append(Content).append('\'');
        sb.append(", PicUrl='").append(PicUrl).append('\'');
        sb.append(", MediaId='").append(MediaId).append('\'');
        sb.append(", Title='").append(Title).append('\'');
        sb.append(", AppId='").append(AppId).append('\'');
        sb.append(", PagePath='").append(PagePath).append('\'');
        sb.append(", ThumbUrl='").append(ThumbUrl).append('\'');
        sb.append(", ThumbMediaId='").append(ThumbMediaId).append('\'');
        sb.append(", Event='").append(Event).append('\'');
        sb.append(", SessionFrom='").append(SessionFrom).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
