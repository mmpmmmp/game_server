package com.wre.game.api.exception;


import com.wre.game.api.constants.RtCode;

public class AuthException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4569171488176433144L;

	public AuthException() {
		super();
	}

	public AuthException(RtCode rtCode) {
		super(rtCode);
	}
}
