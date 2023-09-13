package com.wre.game.api.adapter;

import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.data.BaiduSession;

public interface BaiduAdapter {
	BaiduSession codeToSession(String code, String appId);

	String getAccessToken(String appId);

	String getGroupQrCodeLink(String openId);

	String uploadQrCodeAndGetMediaId(String openId, String qrcodeLink, String accessToken);

	JSONObject sendMessageByMediaId(String openId, String mediaId, String accessToken);

	JSONObject sendMessageByText(String openId, String content, String accessToken);

}
