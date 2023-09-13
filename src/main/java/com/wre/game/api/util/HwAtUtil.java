package com.wre.game.api.util;

import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.component.SessionComponentImpl;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.exception.RedisException;
import com.wre.game.api.message.ProductSDKMessage;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class HwAtUtil {

    private static final Logger logger = LoggerFactory.getLogger(HwAtUtil.class);
    private static String accessToken;
    private static final String TOKEN_URL = "https://test/oauth2/v3/token";
    @Resource
    @Qualifier("jedisPool")
    private static JedisPool jedisPool;

    public static String getAppAT() throws Exception {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String accessToken = jedis.get("HWAT");
            if (!StringUtils.isEmpty(accessToken)){
                return accessToken;
            }
            String grantType = "client_credentials";
            String msgBody = MessageFormat.format("grant_type={0}&client_secret={1}&client_id={2}", grantType,
                    URLEncoder.encode(ProductSDKMessage.AssemblyLine.HW_CLIENT_SECRET, "UTF-8"), ProductSDKMessage.AssemblyLine.HW_CLIENT_ID);
            String response =
                    httpPost(TOKEN_URL, "application/x-www-form-urlencoded; charset=UTF-8", msgBody, 5000, 5000, null);
            JSONObject obj = JSONObject.parseObject(response);
            accessToken = obj.getString("access_token");
            jedis.set("HWAT",accessToken);
            jedis.expire("HWAT",60);
            return accessToken;
        }catch (Exception e){
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static Map<String, String> buildAuthorization(String appAt) {
        String oriString = MessageFormat.format("APPAT:{0}", appAt);
        String authorization =
                MessageFormat.format("Basic {0}", Base64.encodeBase64String(oriString.getBytes(StandardCharsets.UTF_8)));
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authorization);
        headers.put("Content-Type", "application/json; charset=UTF-8");
        return headers;
    }

    public static String httpPost(String httpUrl, String contentType, String data, int connectTimeout, int readTimeout,
                                  Map<String, String> headers) throws IOException {
        OutputStream output = null;
        InputStream in = null;
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", contentType);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    urlConnection.setRequestProperty(key, headers.get(key));
                }
            }
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.connect();

            // POST data
            output = urlConnection.getOutputStream();
            output.write(data.getBytes("UTF-8"));
            output.flush();

            // read response
            if (urlConnection.getResponseCode() < 400) {
                in = urlConnection.getInputStream();
            } else {
                in = urlConnection.getErrorStream();
            }

            inputStreamReader = new InputStreamReader(in, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder strBuf = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                strBuf.append(str);
            }
            return strBuf.toString();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (in != null) {
                in.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
