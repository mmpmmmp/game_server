package com.wre.game.api.data;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1762796227337192313L;
    private String sid;
    private String name;
    private String desc;
    private String image;
    private String price;
    private String gold;
    private String showGold;
    private String backGold;
    private String monthCardType;
    private String productID;
    private String platform;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getShowGold() {
        return showGold;
    }

    public void setShowGold(String showGold) {
        this.showGold = showGold;
    }

    public String getBackGold() {
        return backGold;
    }

    public void setBackGold(String backGold) {
        this.backGold = backGold;
    }

    public String getMonthCardType() {
        return monthCardType;
    }

    public void setMonthCardType(String monthCardType) {
        this.monthCardType = monthCardType;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("sid='").append(sid).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append(", price='").append(price).append('\'');
        sb.append(", gold='").append(gold).append('\'');
        sb.append(", showGold='").append(showGold).append('\'');
        sb.append(", backGold='").append(backGold).append('\'');
        sb.append(", monthCardType='").append(monthCardType).append('\'');
        sb.append(", productID='").append(productID).append('\'');
        sb.append(", platform='").append(platform).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
