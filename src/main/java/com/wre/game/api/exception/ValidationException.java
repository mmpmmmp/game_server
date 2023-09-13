package com.wre.game.api.exception;


import com.wre.game.api.constants.RtCode;

public class ValidationException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 723898319294925044L;

	public ValidationException() {
		super();
	}

	public ValidationException(RtCode rtCode) {
		super(rtCode);
	}

}
