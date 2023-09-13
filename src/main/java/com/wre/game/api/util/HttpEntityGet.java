package com.wre.game.api.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.FileOutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpEntityGet {

    public static void main(String[] args) {
        HttpEntityGet get = new HttpEntityGet();
        get.test();

    }

    private void test() {
        try {
            HttpClient client = new DefaultHttpClient();
            MyHttpGetWithEntity e = new MyHttpGetWithEntity("http://test/api/chatrooms/fetch");
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("token", "www.token.com"));
            urlParameters.add(new BasicNameValuePair("admin_token", "admin_token"));
            e.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(e);

            String str = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
            String qrcodeUrl = "";
            System.out.println(str);
            JSONObject json = JSONObject.parseObject(str);
            if (json != null) {
                JSONObject chatroom = json.getJSONObject("chatroom");
                if (chatroom != null) {
                    qrcodeUrl = String.valueOf(chatroom.get("qrcode_url"));
                }
            }
            System.out.println(qrcodeUrl);
            byte[] bytes = IOUtils.toByteArray(new URI(qrcodeUrl));
            IOUtils.write(bytes, new FileOutputStream("/Users/admin/Desktop/test.png"));
        } catch (Exception e) {
            System.err.println(e);
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