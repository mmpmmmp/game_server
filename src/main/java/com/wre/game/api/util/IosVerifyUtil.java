package com.wre.game.api.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Locale;

/**
 * 苹果IAP内购验证工具类
 * Created by wangqichang on 2019/2/26.
 */
public class IosVerifyUtil {

    private static final List<String> VERIFY_HOST_NAME_ARRAY = ListUtil.toList("host");

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAnyHostnameVerifierVerify implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            if (StrUtil.isEmpty(hostname)) {
                return false;
            }
            return !VERIFY_HOST_NAME_ARRAY.contains(hostname);
        }
    };

    private static final String url_sandbox = "https://test/verifyReceipt";
    private static final String url_verify = "https://test/verifyReceipt";

    /**
     * 苹果服务器验证
     *
     * @param receipt
     *            账单
     * @url 要验证的地址
     * @return null 或返回结果 沙盒 https://sandbox.itunes.apple.com/verifyReceipt
     *
     */
    public static String buyAppVerify(String receipt,int type) {
        //环境判断 线上/开发环境用不同的请求链接
        String url = "";
        if(type==0){
            url = url_sandbox; //沙盒测试
        }else{
            url = url_verify; //线上测试
        }
        //String url = EnvUtils.isOnline() ?url_verify : url_sandbox;

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            File certificateFile = new File("data/to/ios/certificate");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            X509Certificate generatedCertificate;
            try (InputStream cert = new FileInputStream(certificateFile)) {
                generatedCertificate = (X509Certificate) CertificateFactory.getInstance("X509")
                        .generateCertificate(cert);
            }
            keyStore.setCertificateEntry(certificateFile.getName(), generatedCertificate);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            TrustManager[] trustManagers = tmf.getTrustManagers();
            sc.init(null, trustManagers, new java.security.SecureRandom());
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifierVerify());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "text/json");
            conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            BufferedOutputStream hurlBufOus = new BufferedOutputStream(conn.getOutputStream());

            String str = String.format(Locale.CHINA, "{\"receipt-data\":\"%s\"}", receipt);//拼成固定的格式传给平台
            hurlBufOus.write(str.getBytes());
            hurlBufOus.flush();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (Exception ex) {
            System.out.println("苹果服务器异常");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 用BASE64加密
     *
     * @param str
     * @return
     */
    public static String getBASE64(String str) {
        byte[] b = str.getBytes();
        String s = null;
        if (b != null) {
            s = new sun.misc.BASE64Encoder().encode(b);
        }
        return s;
    }

}