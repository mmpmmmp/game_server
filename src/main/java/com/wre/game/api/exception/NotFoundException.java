package com.wre.game.api.exception;


import com.wre.game.api.constants.RtCode;

public class NotFoundException extends ApiException{

    private static final long serialVersionUID = -2170788610719960365L;

    public NotFoundException() {
        super();
    }

    public NotFoundException(RtCode rtCode) {
        super(rtCode);
    }
}
