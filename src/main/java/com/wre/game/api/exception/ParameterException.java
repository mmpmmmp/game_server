package com.wre.game.api.exception;


import com.wre.game.api.constants.RtCode;

public class ParameterException extends ApiException {

	private static final long serialVersionUID = 3486973697387510949L;

	public ParameterException() {
		super();
	}

	public ParameterException(RtCode rtCode) {
		super(rtCode);
	}
}
