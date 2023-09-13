package com.wre.game.api.service;

import com.wre.game.api.constants.ApiResponseDataMessage;
import com.wre.game.api.constants.ApiResponseMessage;
import com.wre.game.api.data.param.MailChangeParam;
import org.springframework.http.ResponseEntity;

public interface MailService {
    ResponseEntity<ApiResponseMessage> getUserMail(String userId,String appId,String countryCode);

    ApiResponseDataMessage changeMailState(MailChangeParam mailChangeParam);

}
