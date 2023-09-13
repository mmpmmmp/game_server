package com.wre.game.api.entity;

public class KSRechargeCallBackParam {
    /**allin 分配给游戏的 app id*/
    private String app_id;
    /**游戏角色 id*/
    private String role_id;
    /**游戏大区 id*/
    private String server_id;
    /**购买产品 id*/
    private String product_id;
    /** 要用户付多少钱(单位分)*/
    private String money;
    /**附加内容，通知时原封不动带回*/
    private String extension;
    /**融合 sdk 服务端订单号 */
    private String allin_trade_no;
    /**融合sdk服务端通知扩展字段*/
    private String data;
    /**
     融合 sdk 签名(一定要验签，验签方法参见下单签名中的签名规则）
     */
    private String sign;
    /**
     订阅商品详情
     */
    private String notifyDetail;


    @Override
    public String toString() {
        return "KSRechargeCallBackParam{" +
                "app_id='" + app_id + '\'' +
                ", role_id='" + role_id + '\'' +
                ", server_id='" + server_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", money='" + money + '\'' +
                ", extension='" + extension + '\'' +
                ", allin_trade_no='" + allin_trade_no + '\'' +
                ", data='" + data + '\'' +
                ", sign='" + sign + '\'' +
                ", notifyDetail='" + notifyDetail + '\'' +
                '}';
    }

    public String toSign() {
        return
                "app_id=" + app_id +'&' +
                        "role_id=" + role_id +'&' +
                        "server_id=" + server_id +'&' +
                        "product_id=" + product_id +'&' +
                        "money=" + money +'&' +
                        "extension=" + extension +'&' +
                        "allin_trade_no=" + allin_trade_no +'&' +
                        "data=" + data ;
    }

    public static void main(String[] args) {
        KSRechargeCallBackParam ksRechargeCallBackParam=new KSRechargeCallBackParam();
        System.out.println(ksRechargeCallBackParam.toSign());
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getAllin_trade_no() {
        return allin_trade_no;
    }

    public void setAllin_trade_no(String allin_trade_no) {
        this.allin_trade_no = allin_trade_no;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNotifyDetail() {
        return notifyDetail;
    }

    public void setNotifyDetail(String notifyDetail) {
        this.notifyDetail = notifyDetail;
    }
}
