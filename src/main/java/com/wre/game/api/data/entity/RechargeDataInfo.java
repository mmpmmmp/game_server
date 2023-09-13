package com.wre.game.api.data.entity;

import java.util.Date;

public class RechargeDataInfo {
    private Long id;

    private String roleid;

    private String productid;

    private String amount;

    private Integer statue;

    private String orderid;

    private Integer serverid;

    private String channelid;

    private String uugameid;

    private Integer type;

    private Integer shoptype;

    private Date createat;

    private String userid;

    private Date updateat;


    public Date getUpdateat() {
        return updateat;
    }

    public void setUpdateat(Date updateat) {
        this.updateat = updateat;
    }

    @Override
    public String toString() {
        return "RechargeDataInfo{" +
                "id=" + id +
                ", roleid='" + roleid + '\'' +
                ", productid='" + productid + '\'' +
                ", amount='" + amount + '\'' +
                ", statue=" + statue +
                ", orderid='" + orderid + '\'' +
                ", serverid=" + serverid +
                ", channelid='" + channelid + '\'' +
                ", uugameid='" + uugameid + '\'' +
                ", type=" + type +
                ", shoptype=" + shoptype +
                ", createat=" + createat +
                ", userid='" + userid + '\'' +
                ", updateat=" + updateat +
                '}';
    }

    public RechargeDataInfo(Long id, String roleid, String productid, String amount, Integer statue, String orderid, Integer serverid, String channelid, String uugameid, Integer type, Integer shoptype, Date createat, String userid,Date updateat) {
        this.id = id;
        this.roleid = roleid;
        this.productid = productid;
        this.amount = amount;
        this.statue = statue;
        this.orderid = orderid;
        this.serverid = serverid;
        this.channelid = channelid;
        this.uugameid = uugameid;
        this.type = type;
        this.shoptype = shoptype;
        this.createat = createat;
        this.userid = userid;
        this.updateat=updateat;
    }

    public RechargeDataInfo() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public Integer getServerid() {
        return serverid;
    }

    public void setServerid(Integer serverid) {
        this.serverid = serverid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid == null ? null : channelid.trim();
    }

    public String getUugameid() {
        return uugameid;
    }

    public void setUugameid(String uugameid) {
        this.uugameid = uugameid == null ? null : uugameid.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getShoptype() {
        return shoptype;
    }

    public void setShoptype(Integer shoptype) {
        this.shoptype = shoptype;
    }

    public Date getCreateat() {
        return createat;
    }

    public void setCreateat(Date createat) {
        this.createat = createat;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

}