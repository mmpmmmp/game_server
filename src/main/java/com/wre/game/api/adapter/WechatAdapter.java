package com.wre.game.api.adapter;

import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.data.WechatSession;

public interface WechatAdapter {
	/**
	 * 微信验证token
	 * https://api.weixin.qq.com/sns/jscode2session\?appid\=APPID\&secret\=SECRET\&js_code\=JSCODE\&grant_type\=authorization_code
	 *
	 * @link https://developers.weixin.qq.com/minigame/dev/api-backend/open-api/login/auth.code2Session.html
	 */
	WechatSession codeToSession(String code, String appId);

	String getAccessToken(String appId);

	String getGroupQrCodeLink(String openId);

	String uploadQrCodeAndGetMediaId(String openId, String qrcodeLink, String accessToken);

	JSONObject sendMessageByMediaId(String openId, String mediaId, String accessToken);

	JSONObject sendMessageByText(String openId, String content, String accessToken);

}
