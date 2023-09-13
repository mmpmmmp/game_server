package com.wre.game.api.netty.entity;

/** 充值返回sdk参数 */
public class RechargeResult {
	private int errcode;
	private String errMsg;

	public RechargeResult() {
		super();
	}

	/**
	 * 充值结果
	 * @param code 1成功101失败-100其它错误
	 */
	public RechargeResult(int code) {
		super();
		errcode = code;
		if (code == 1) {
			errMsg = "Success";
		} else if (code == 101) {
			errMsg = "orderId is exits";
		}
	}

	/**
	 * 充值结果
	 * @param code 1成功101失败-100其它错误
	 * @param msg
	 */
	public RechargeResult(int code, String msg) {
		super();
		errcode = code;
		if (code == 1) {
			errMsg = "Success";
		} else if (code == 101) {
			errMsg = "gameCustomInfo is exits";
		} else {
			errMsg = msg;
		}
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	@Override
	public String toString() {
		return "RechargeResult [errcode=" + errcode + ", errMsg=" + errMsg + ", getErrcode()=" + getErrcode() + ", getErrMsg()=" + getErrMsg() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
