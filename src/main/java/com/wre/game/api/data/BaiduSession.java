package com.wre.game.api.data;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class BaiduSession implements Serializable {
	private static final long serialVersionUID = 1L;
	@JSONField(name = "openid")
	private String openid;
	@JSONField(name = "session_key")
	private String session_key;
	@JSONField(name = "error")
	private String error;
	@JSONField(name = "error_description")
	private String error_description;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	@Override
	public String toString() {
		return "BaiduSession [openid=" + openid + ", session_key=" + session_key + ", error=" + error + ", error_description=" + error_description + "]";
	}

}
