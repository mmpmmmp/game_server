package com.wre.game.api.netty.message;

import io.netty.channel.Channel;

public abstract class Message extends Bean {
	protected String token;
	protected Channel channel;
	/**客户端用协议id*/
	protected int uniqueId;

	public Message() {
	}

	public abstract int getMsgId();

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

}