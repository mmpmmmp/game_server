package com.wre.game.api.entity;

import com.wre.game.api.util.Fn;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class KSRechargeParam {

    /** 快手分配给游戏的appid，必传非空，应从服务器读取，非客户端传入*/
    private String app_id;
    /**sdk客户端初始化返回的,可通过客户端SDK的DataUtil.getChannel()接口获取，必传非空*/
    private String channel_id;
    /**登录获取的game_id,即快手分配给用户的唯一标识，必传非空*/
    private String game_id;
    /**游戏角色 id，必传非空*/
    private String role_id;
     /**游戏角色名称，必传非空*/
    private String role_name;
     /**游戏角色等级，必传非空*/
    private String role_level;
     /**游戏大区 id，必传非空*/
    private String server_id;
     /**游戏大区名称，必传非空*/
    private String server_name;
     /**购买产品 id，必传非空*/
    private String product_id;
    /**购买产品名称，必传非空*/
    private String product_name;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    /**购买产品名称，必传非空*/
    private String product_desc;
     /**要用户付多少钱(单位分)，必传非空*/
    private String money;
     /**货币类型(目前只支持CNY)，必传非空*/
    private String currency_type;
     /**游戏服务器接收支付成功通知地址(https) ，必传非空*/
    private String notify_url;
     /**附加内容，通知时原封不动带回(字符串长度最长支持500,超过自己处理)，必传非空  目前只发送订单号*/
    private String extension;
     /**>用户当前外网ip(通过游戏服务器获取)，必传非空（请参考微信建议的获取方式：https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=15_5）*/
    private String user_ip;
     /**如客户端传入了orderId（订单号）则服务器计算签名时需要添加third_party_trade_no字段*/
    private String third_party_trade_no;

    private String subscribe;

    private String days;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    @Override
    public String toString() {
        return "KSRechargeParam{" +
                "app_id='" + app_id + '\'' +
                ", channel_id='" + channel_id + '\'' +
                ", game_id='" + game_id + '\'' +
                ", role_id='" + role_id + '\'' +
                ", role_name='" + role_name + '\'' +
                ", role_level='" + role_level + '\'' +
                ", server_id='" + server_id + '\'' +
                ", server_name='" + server_name + '\'' +
                ", product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_desc='" + product_desc + '\'' +
                ", money='" + money + '\'' +
                ", currency_type='" + currency_type + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", extension='" + extension + '\'' +
                ", user_ip='" + user_ip + '\'' +
                ", third_party_trade_no='" + third_party_trade_no + '\'' +
                ", subscribe='" + subscribe + '\'' +
                '}';
    }

    public  String toSign() {
        Map<String,String> map=new HashMap<>();
        map.put("app_id",app_id);
        map.put("channel_id",channel_id);
        map.put("game_id",game_id);
        map.put("role_id",role_id);
        map.put("role_name",role_name);
        map.put("role_level",role_level);
        map.put("server_id",server_id);
        map.put("server_name",server_name);
        map.put("product_id",product_id);
        map.put("product_name",product_name);
        map.put("product_desc",product_desc);
        map.put("money",money);
        map.put("currency_type",currency_type);
        map.put("notify_url",notify_url);
        map.put("extension",extension);
        map.put("user_ip",user_ip);
        if (!StringUtils.isBlank(third_party_trade_no)) {
            map.put("third_party_trade_no", third_party_trade_no);
        }
        System.out.println(map);
        return Fn.formatUrlParam(map,"utf-8", true);

    }

    public static void main(String[] args) {
        KSRechargeParam ksRechargeParam=new KSRechargeParam();
        ksRechargeParam.setApp_id("11");
        ksRechargeParam.setChannel_id("11");
        System.out.println(ksRechargeParam.toSign());
    }
    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_level() {
        return role_level;
    }

    public void setRole_level(String role_level) {
        this.role_level = role_level;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public String getThird_party_trade_no() {
        return third_party_trade_no;
    }

    public void setThird_party_trade_no(String third_party_trade_no) {
        this.third_party_trade_no = third_party_trade_no;
    }
}
