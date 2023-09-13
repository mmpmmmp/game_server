package com.wre.game.api.netty.entity;

/**
 * sdk告诉我的充值订单
 * 
 * @author zs
 * @date 2020-03-27 14:20:21
 */
public class RechargeInfo {
	// 为游戏唯一订单号我们原样返回，在Android中对应充值传递的gameCustomInfo，IOS中对应充值传递的code;
	private String gameCustomInfo;
	// skd返回状态1成功
	private int state;
	// 为uugames的订单号
	private String orderId;
	// userId
	private String userId;
	// 为游戏编号，由uugames分配。
	private String uuGameId;
	// 为区服编号
	private int serverId;
	// UU为渠道分配的渠道编号
	private String channelId;
	// 为玩家充值的商品Id
	private int productId;
	// 为玩家充值的金额
	private float amount;
	// 对上边参数的签名数据，注：sign不参与签名
	private String sign;
	// 签名方式，固定值“RSA”，对于java中的“MD5WithRSA”注：signtype不参与签名
	private String signtype;
	// 充值获得钻石
	private int diamondAmount;
	//充值赠送钻石
	private int diamondExtraAmount;

	public int getDiamondAmount() {
		return diamondAmount;
	}

	public void setDiamondAmount(int diamondAmount) {
		this.diamondAmount = diamondAmount;
	}

	public int getDiamondExtraAmount() {
		return diamondExtraAmount;
	}

	public void setDiamondExtraAmount(int diamondExtraAmount) {
		this.diamondExtraAmount = diamondExtraAmount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUuGameId() {
		return uuGameId;
	}

	public void setUuGameId(String uuGameId) {
		this.uuGameId = uuGameId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSigntype() {
		return signtype;
	}

	public void setSigntype(String signtype) {
		this.signtype = signtype;
	}

	public String getGameCustomInfo() {
		return gameCustomInfo;
	}

	public void setGameCustomInfo(String gameCustomInfo) {
		this.gameCustomInfo = gameCustomInfo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
