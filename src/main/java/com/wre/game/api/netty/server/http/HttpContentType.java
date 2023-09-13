package com.wre.game.api.netty.server.http;

public enum HttpContentType {
	Text("text/plain"),
	Get("application/x-www-form-urlencoded"),
	Post("multipart/form-data"),
	Json("application/json");
	String value;
	
	HttpContentType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	/**拼接utf_8*/
	@Override
	public String toString() {
		return value+"; charset=UTF-8";
	}
}