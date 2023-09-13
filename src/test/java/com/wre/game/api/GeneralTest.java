package com.wre.game.api;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.EncodeHintType;
import com.wre.game.api.adapter.ToutiaoAdapter;
import com.wre.game.api.adapter.WechatAdapter;
import com.wre.game.api.component.DataComponent;
import com.wre.game.api.config.GameConfig;
import com.wre.game.api.config.ProductListConfig;
import com.wre.game.api.constants.ApiConstants;
import com.wre.game.api.data.*;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.util.GameUtils;
import com.wre.game.api.util.IpUtil;
import com.wre.game.api.util.TokenUtil;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WechatGameApiApplication.class)
public class GeneralTest {
    @Autowired
    WechatAdapter wechatAdapter;
    @Autowired
    ToutiaoAdapter toutiaoAdapter;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    GameConfig gameConfig;
    @Autowired
    ProductListConfig productListConfig;
    @Autowired
    DataComponent dataComponent;

    {
        System.setProperty("spring.profiles.active", "local");
    }

    @Test
    public void testCodeToSession() throws Exception {
        WechatSession s = wechatAdapter.codeToSession("1234", "wx4e0f107160341efc");
        System.out.println(s);
    }


    @Test
    public void testCodeToSessionToutiao() throws Exception {
        gameConfig.reloadGameInfosFromPath();
        ToutiaoSession s = toutiaoAdapter.codeToSession("57c6af324316070b", "1aaf66fd322b1c06", "tt134565f88c6fa4e4");
        System.out.println(s);
    }


    @Test
    public void testToken() {
        long tokenExpireMillis = 3333000;
        String tokenSecret = "cRlreScAI25hZ2ILbMa=";
        Date expireTime = new Date();
        String channel = "WECHAT";

        User user = new User();
        user.setUserId(20L);
        user.setBlock(0);
        user.setUuid(TokenUtil.generateUuid());
        user.setChannel("WECHAT");

        expireTime.setTime(expireTime.getTime() + tokenExpireMillis);
        String token = TokenUtil.generateToken(tokenSecret, user, expireTime, channel);
        System.out.println(token);

        SessionDomain u = TokenUtil.parseToken(tokenSecret, token);
        System.out.println(u.toString());


    }

    @Test
    public void testParse() {
        String tokenSecret = "cRlreScAI25hZ2ILbMa=";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJjb2RlIjoiMC5KdW5IYWkuMjY5MWY0ZmJmMjRhNDE0MmI4ZDk1MjQ2NTY3ZTEyOTYiLCJleHAiOjE1NzgyODk5MDIsInVzZXJJZCI6IjE4In0.QOYgYr256e9jm51SWfp1juRpvUs3XYTBYsmfOtWxEm0";
        SessionDomain u = TokenUtil.parseToken(tokenSecret, token);
        System.out.println(u.toString());
    }

    @Test
    public void testReqeustCode() {
        String json = "{\"session_key\":\"z4wEV7RG9lZOsjbK2LJGLg==\",\"openid\":\"okgnc4vw0jzl9sHwu8_S6pIW_JBY\"}";
        System.out.println(json);

        WechatSession obj = JSONObject.parseObject(json, WechatSession.class);

        System.out.println(obj);
    }

    @Test
    public void testUuid() {
        System.out.println(TokenUtil.generateUuid());
    }

    @Test
    public void testVersion() {
        System.out.println(GameUtils.compareVersion("1.0.358_20180820090554", "1.0.358_20180820090553"));
        System.out.println(GameUtils.compareVersion("1.0.358", "1.0.358"));
        System.out.println(GameUtils.compareVersion("1.0.3", "1.0.4"));
    }


    @Test
    public void testGameInfos() {

        GameInfos gameInfos = GameConfig.getGameInfos();
        System.out.println(gameInfos);
        System.out.println(gameInfos.getByAppId("test"));
        System.out.println(gameInfos.getByAppId("wx28756efa9d3c411f"));

        User user = new User();
        user.setAppVersion("1.0.8 ");
        user.setAppId("wx28756efa9d3c411f");

        GameInfo gameInfo = gameInfos.getByAppId(user.getAppId());
        if (StringUtils.equals(gameInfo.getServerVersion(), user.getAppVersion())) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }

    @Test
    public void testGameInfos2() {
        System.out.println("gameInfos2" + GameConfig.getGameInfos());

    }

    @Test
    public void testProductList(){
        ProductList productList = ProductListConfig.getProductList();
        System.out.println(productList);
        System.out.println(productList.getProductListMap().get("wx123123123").get("101"));
    }

    @Test
    public void testIpSection() {
        boolean exist = IpUtil.ipExistsInRange("10.0.0.3", "10.0.0.0-10.1.0.0");
        System.out.println(exist);
    }

    @Test
    public void testGenQrCode() throws Exception {
        QRCode.from("https://www.baidu.com").withCharset("UTF-8").to(ImageType.PNG).withSize(320, 320).withHint(EncodeHintType.MARGIN, 1)
                .writeTo(new FileOutputStream("/Users/admin/Desktop/baidu.png"));
    }

    @Test
    public void testGetAccessToken() {
        String appId = "wxb5e0e56c36278fa4";
        System.out.println(wechatAdapter.getAccessToken(appId));
    }

    @Test
    public void testUploadTempFile() {
        String appId = "wxca4cc13d212d9440";
        String openId = "okgnc4vw0jzl9sHwu8_S6pIW_JBY";

        String accessToken = wechatAdapter.getAccessToken(appId);
        String mediaId = wechatAdapter.uploadQrCodeAndGetMediaId(openId, "https://www.baidu.com", accessToken);
        wechatAdapter.sendMessageByMediaId(openId, mediaId, accessToken);
    }

    @Test
    public void testSendMessage() {
        String appId = "wx4e0f107160341efc";
        String openId = "okgnc4vw0jzl9sHwu8_S6pIW_JBY";
        String mediaId = "WUsaeeF49lmf_2gdHoUbPfcp7myxCCD3CgAFQwxQmRxscmiFmoWipQ3iVbB8ZDhp";

        String accessToken = dataComponent.getAppAccessToken(appId);
        if (StringUtils.isBlank(accessToken)) {
            System.out.println("get new accessToken");
            accessToken = wechatAdapter.getAccessToken(appId);
            dataComponent.setAppAccessToken(appId, accessToken, 3600);// access token expire 1 hour
        }

        wechatAdapter.sendMessageByText(openId, ApiConstants.WECHAT_CONTENT, accessToken);

        wechatAdapter.sendMessageByMediaId(openId, mediaId, accessToken);


    }

    @Test
    public void generateFile() throws Exception {
        FileInputStream fis = new FileInputStream("/Users/admin/Downloads/gameapi-access2-20190628.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String message = "";
        String line = null;
        int i = 1;

        int fileIndex = 0;

        String fileName = "gameapi-access2-20190628" + fileIndex + ".txt";

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("/Users/admin/Desktop/" + fileName))));


        while ((line = br.readLine()) != null) {
//            bw.write(line);
//            bw.newLine();
            i++;
//            if (i / 190000 > fileIndex) {
//                bw.close();
//                fileIndex = i / 190000;
//                fileName = "gameapi-access2-20190628" + fileIndex + ".txt";
//                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("/Users/admin/Desktop/" + fileName))));
//            }
        }
        System.out.println(i);
        br.close();
        bw.close();


    }

}