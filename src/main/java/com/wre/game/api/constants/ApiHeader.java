package com.wre.game.api.constants;

public interface ApiHeader {
	public static final String TOKEN = "X-WRE-TOKEN";
	public static final String CHANNEL = "X-WRE-CHANNEL";
	public static final String VERSION = "X-WRE-VERSION";
	public static final String APP_ID = "X-WRE-APP-ID";
	public static final String APP_NAME = "X-WRE-APP-NAME";
	public static final String TRACEUUID = "X-WRE-TRACEUUID";
	public static final String CLIENT_IP = "X-FORWARDED-FOR";
}
