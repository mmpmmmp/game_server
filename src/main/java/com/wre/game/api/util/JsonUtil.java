package com.wre.game.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.Impl;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.wre.game.api.message.MessageResp;

public class JsonUtil {
    public JsonUtil() {
    }



    public static String getResponseJsonStr(Integer code, String message, Object resObject) {
        MessageResp me = new MessageResp();
        // 返回JSON格式提示消息给请求者

        me.setCode(code);
        me.setData(resObject);
        me.setMessage(message);

        return JSON.toJSONString(me, SerializerFeature.PrettyFormat);
    }


    public static String marshallingJson(Object object) throws Exception {
        return parseJson(false, object);
    }

    public static String marshallingJsonWithPretty(Object object) throws Exception {
        return "\n" + parseJson(true, object);
    }

    private static String parseJson(boolean pretty, Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.ALWAYS);
        DefaultSerializerProvider provider = new Impl();
        provider.setNullValueSerializer(NullSerializer.instance);
        objectMapper.setSerializerProvider(provider);
        if (pretty) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        return objectMapper.writeValueAsString(object);
    }

    public static <T> T unmarshallingJson(String jsonText, Class<T> valueType) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        T t = null;
        String data = jsonText.replaceAll("null", "\"\"");
        t = objectMapper.readValue(data, valueType);
        return t;
    }

    public static void main(String[] args) {

    }
}
