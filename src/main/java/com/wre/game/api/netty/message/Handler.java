package com.wre.game.api.netty.message;


public abstract class Handler implements ICommand {
	protected Message message;
	/** 进入队列时间 **/
	private long time;

	public Handler() {
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}



	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}