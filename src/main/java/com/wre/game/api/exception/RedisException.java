package com.wre.game.api.exception;


import com.wre.game.api.constants.RtCode;

public class RedisException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8580278457346355611L;


	public RedisException() {
		super();
	}

	public RedisException(RtCode rtCode) {
		super(rtCode);
	}
	
	public RedisException(RtCode rtCode, Throwable throwable) {
		super(rtCode, throwable);
	}
}
