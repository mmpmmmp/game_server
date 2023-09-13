package com.wre.game.api.controller;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import org.apache.commons.codec.Charsets;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class WebCommonTest {

    protected String uri = "";
    private final Logger logger = LoggerFactory.getLogger(WebCommonTest.class);
    private final Map<String, Object> params = new HashMap<>();

    protected HttpHeaders httpHeaders;

    public MockMvc mockMvc;

    public void setUp() throws Exception {
        httpHeaders = new HttpHeaders();
        httpHeaders.add("X-WRE-APP-ID", "liushuixian_huawei_androidhw");
        httpHeaders.add("X-WRE-APP-NAME", "liushuixian_huawei_androidhw");
        httpHeaders.add("X-WRE-VERSION", "1.0.0");
        httpHeaders.add("X-WRE-CHANNEL", "default");
        httpHeaders.add("X-WRE-TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJjb2RlIjoiMC5kZWZhdWx0LmNjZDExZDlkNmI1MzRlZGY5YTc4OTBjYjc0MTkyODg5IiwiZXhwIjoxNjc3NjY3MDUzLCJ1c2VySWQiOiI1MjMyOTcwOCJ9.qFJaI6A_9hRzYXeawkbUKhBBo2707c7y770ScXhfp_A");
        httpHeaders.add("Content-Type", "application/json");
    }

    @After
    public void tearDown() throws Exception {
        params.clear();
    }

    public String postUrl(String content, String url) {
        try {
            return request(post(uri + url), content);
        } catch (Exception e) {
            logger.error("Run error", e);
        }
        return null;
    }

    public String putUrl(String content, String url) {
        try {
            return request(put(uri + url), content);
        } catch (Exception e) {
            logger.error("Run error", e);
        }
        return null;
    }

    public String getUrl(Map<String, Object> params, String url) {
        try {
            String toParams = HttpUtil.toParams(params);
            return request(get(uri + url + "?" + toParams), "");
        } catch (Exception e) {
            logger.error("Run error", e);
        }
        return null;
    }

    public String request(MockHttpServletRequestBuilder builder, String content) throws Exception {
        MvcResult mvcResult = mockMvc.perform(builder
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .headers(httpHeaders)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        return mvcResult.getResponse().getContentAsString();
    }

    public Map<String, Object> setParams(String key, Object value) {
        params.put(key, value);
        return params;
    }

    public void setAppId() {
        params.put("appId", "liushuixian_huawei_androidhw");
    }

    public String getAppId() {
        return "liushuixian_huawei_androidhw";
    }

    public void setUuid() {
        params.put("uuid", "ccd11d9d6b534edf9a7890cb74192889");
    }

    public String getUuid() {
        return "ccd11d9d6b534edf9a7890cb74192889";
    }

    public void setUserId() {
        params.put("userId", 52329708);
    }

    public Integer getUserId() {
        return 52329708;
    }

    protected Map<String, Object> getParams() {
        return params;
    }
}
