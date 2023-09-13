package com.wre.game.api.netty.entity;

/**
 * sdk告诉我的充值订单
 * 
 * @author zs
 * @date 2020-03-27 14:20:21
 */
public class RechargeCallbackInfo {
	// 通知数据解码后为xml格式;
	private String nt_data;
	// 签名串
	private String sign;
	// 计算方法为MD5(nt_data+sign+md5key),三个值直接拼接即可
	private String md5Sign;
	/**渠道信息*/
	private String platform;


	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getNt_data() {
		return nt_data;
	}
	public void setNt_data(String nt_data) {
		this.nt_data = nt_data;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getMd5Sign() {
		return md5Sign;
	}
	public void setMd5Sign(String md5Sign) {
		this.md5Sign = md5Sign;
	}

	@Override
	public String toString() {
		return "RechargeCallbackInfo{" +
				"nt_data='" + nt_data + '\'' +
				", sign='" + sign + '\'' +
				", md5Sign='" + md5Sign + '\'' +
				", platform='" + platform + '\'' +
				'}';
	}
}
