package com.wre.game.api.adapter.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.adapter.BaiduAdapter;
import com.wre.game.api.adapter.WechatAdapter;
import com.wre.game.api.config.GameConfig;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.data.BaiduSession;
import com.wre.game.api.data.GameInfo;
import com.wre.game.api.data.WechatSession;
import com.wre.game.api.exception.AuthException;
import com.wre.game.api.util.FileMessageResource;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

@Component
public class BaiduAdapterImpl implements BaiduAdapter {

	private static final Logger logger = LoggerFactory.getLogger(BaiduAdapterImpl.class);

	private static final String ADMIN_TOKEN = "token";

	@Autowired
	RestTemplate restTemplate;

	/**
	 * https://smartprogram.baidu.com/docs/develop/api/open/log_Session-Key/
	 */
	@Override
	public BaiduSession codeToSession(String code, String appId) {
		logger.info("code:{}, appId:{}", code, appId);

		GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(appId);

		if (gameInfo == null) {
			throw new AuthException(RtCode.RT_NOT_FOUND_APP_ID);
		}

		//params
		//		grant_type：必须参数，固定为“client_credentials”
		//		client_id： 必须参数，智能小程序的AppKey 从开发者平台中获取
		//		client_secret：必须参数，智能小程序的AppSecret 从开发者平台中获取
		//		scope：必须参数，固定为"smartapp_snsapi_base"
		Map<String, String> map = new HashMap<>();
		map.put("code", code);
		map.put("sk", "sk");
		map.put("client_id", "client_id");
		String url = "https://teste/oauth/jscode2sessionkey?code={code}&client_id={client_id}&sk={sk}";

		String json = restTemplate.getForObject(url, String.class, map);

		BaiduSession obj = JSONObject.parseObject(json, BaiduSession.class);
		return obj;
	}

	@Override
	public String getGroupQrCodeLink(String openId) {
		String qrcodeUrl = "";
		try {
			HttpClient client = new DefaultHttpClient();
			BaiduAdapterImpl.MyHttpGetWithEntity e = new BaiduAdapterImpl.MyHttpGetWithEntity("http://test/api/chatrooms/fetch");
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("token", openId));
			urlParameters.add(new BasicNameValuePair("admin_token", ADMIN_TOKEN));
			e.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response = client.execute(e);

			String str = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
			logger.info("get group qrcode link -> {}", str);
			//            System.out.println("get qrcode link -> openId:" + openId + " response:" + str);
//			JSONObject json = JSONObject.parseObject(str);
			cn.hutool.json.JSONObject json = JSONUtil.parseObj(str);
			if (json != null) {
				cn.hutool.json.JSONObject chatroom = json.getJSONObject("chatroom");
				if (chatroom != null) {
					qrcodeUrl = String.valueOf(chatroom.get("qrcode_url"));
				}
			}
			return qrcodeUrl;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qrcodeUrl;
	}

	@Override
	public String getAccessToken(String appId) {
		logger.info("get access token");
		GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(appId);

		if (gameInfo == null) {
			throw new AuthException(RtCode.RT_NOT_FOUND_APP_ID);
		}

		//params
		Map<String, String> map = new HashMap<>();
		map.put("appid", appId);
		map.put("secret", gameInfo.getAppSecret());
		String url = "https://test/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}";

		JSONObject json = restTemplate.getForObject(url, JSONObject.class, map);

		String accessToken = "";
		if (json != null) {
			/**
			 * -1 系统繁忙，此时请开发者稍候再试 0 请求成功 40001 AppSecret 错误或者 AppSecret 不属于这个小程序，请开发者确认
			 * AppSecret 的正确性 40002 请确保 grant_type 字段值为 client_credential 40013 不合法的
			 * AppID，请开发者检查 AppID 的正确性，避免异常字符，注意大小写
			 *
			 * 返回数据示例 正常返回
			 *
			 * {"access_token": "ACCESS_TOKEN", "expires_in": 7200} 错误时返回
			 *
			 * {"errcode": 40013, "errmsg": "invalid appid"}
			 */
			String token = json.getString("access_token");
			if (token != null) {
				accessToken = token;
			}
			logger.info("get access token response -> {}", json.toJSONString());
		}
		return accessToken;
	}

	@Override
	public String uploadQrCodeAndGetMediaId(String openId, String qrcodeUrl, String accessToken) {

		byte[] bytes = null;
		try {
			bytes = IOUtils.toByteArray(new URI(qrcodeUrl));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		FileMessageResource fileMessageResource = new FileMessageResource(bytes, openId + ".png");

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("media", fileMessageResource);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

		String url = "https://test/cgi-bin/media/upload?access_token=" + accessToken + "&type=image";

		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
		JSONObject json = restTemplate.postForObject(url, requestEntity, JSONObject.class);
		String mediaId = "";
		if (json != null) {
			/**
			 * errcode number 错误码 errmsg string 错误信息 type string 文件类型 media_id string
			 * 媒体文件上传后，获取标识，3天内有效。 created_at number 媒体文件上传时间戳
			 */
			String mId = json.getString("media_id");
			if (mId != null) {
				mediaId = mId;
			}
			logger.info("get mediaId response -> {}", json.toJSONString());
		}
		return mediaId;
	}

	@Override
	public JSONObject sendMessageByMediaId(String openId, String mediaId, String accessToken) {
		logger.info("send message to user-> openId:{}, mediaId:{}", openId, mediaId);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("touser", openId);
		jsonObject.put("msgtype", "image");
		JSONObject imageJsonObject = new JSONObject();
		imageJsonObject.put("media_id", mediaId);
		jsonObject.put("image", imageJsonObject);
		HttpEntity<JSONObject> requestEntity = new HttpEntity<>(jsonObject, headers);

		String url = "https://test/cgi-bin/message/custom/send?access_token=" + accessToken;

		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
		JSONObject json = restTemplate.postForObject(url, requestEntity, JSONObject.class);
		if (json != null) {
			/**
			 * 0 请求成功 -1 系统繁忙，此时请开发者稍候再试 40001 获取 access_token 时 AppSecret 错误，或者
			 * access_token 无效。请开发者认真比对 AppSecret 的正确性，或查看是否正在为恰当的小程序调用接口 40002 不合法的凭证类型
			 * 40003 不合法的 OpenID，请开发者确认 OpenID 是否是其他小程序的 OpenID 45015 回复时间超过限制 45047
			 * 客服接口下行条数超过上限 48001 API 功能未授权，请确认小程序已获得该接口
			 */
			logger.info("send result -> {}", json.toJSONString());
		}

		return json;
	}

	@Override
	public JSONObject sendMessageByText(String openId, String content, String accessToken) {

		logger.info("send message to user-> openId:{}, content:{}", openId, content);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("touser", openId);
		jsonObject.put("msgtype", "text");
		JSONObject imageJsonObject = new JSONObject();
		imageJsonObject.put("content", content);
		jsonObject.put("text", imageJsonObject);
		HttpEntity<JSONObject> requestEntity = new HttpEntity<>(jsonObject, headers);

		String url = "https://test/cgi-bin/message/custom/send?access_token=" + accessToken;

		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
		JSONObject json = restTemplate.postForObject(url, requestEntity, JSONObject.class);
		if (json != null) {
			/**
			 * 0 请求成功 -1 系统繁忙，此时请开发者稍候再试 40001 获取 access_token 时 AppSecret 错误，或者
			 * access_token 无效。请开发者认真比对 AppSecret 的正确性，或查看是否正在为恰当的小程序调用接口 40002 不合法的凭证类型
			 * 40003 不合法的 OpenID，请开发者确认 OpenID 是否是其他小程序的 OpenID 45015 回复时间超过限制 45047
			 * 客服接口下行条数超过上限 48001 API 功能未授权，请确认小程序已获得该接口
			 */
			logger.info("send result -> {}", json.toJSONString());
		}

		return json;
	}

	public class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
		public WxMappingJackson2HttpMessageConverter() {
			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.TEXT_PLAIN);
			mediaTypes.add(MediaType.TEXT_HTML); //加入text/html类型的支持
			setSupportedMediaTypes(mediaTypes);// tag6
		}
	}

	public class MyHttpGetWithEntity extends HttpEntityEnclosingRequestBase {
		public final static String GET_METHOD = "GET";

		public MyHttpGetWithEntity(final URI uri) {
			super();
			setURI(uri);
		}

		public MyHttpGetWithEntity(final String uri) {
			super();
			setURI(URI.create(uri));
		}

		@Override
		public String getMethod() {
			return GET_METHOD;
		}
	}
}