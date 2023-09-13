package com.wre.game.api.netty.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wre.game.api.message.MessageResp;

public class JsonUtil {

	public static MessageResp getResponse(Integer code, String message, Object resObject) {
		MessageResp me = new MessageResp();
		// 返回JSON格式提示消息给请求者

		me.setCode(code);
		me.setData(resObject);
		me.setMessage(message);

		return me;
	}
	
	public static String getResponseJsonStr(Integer code, String message, Object resObject) {
		MessageResp me = new MessageResp();
		// 返回JSON格式提示消息给请求者

		me.setCode(code);
		me.setData(resObject);
		me.setMessage(message);

		return JSON.toJSONString(me, SerializerFeature.PrettyFormat);
	}


	public static MessageResp getResponseMessage(Integer code, String message, Object resObject) {
		MessageResp me = new MessageResp();
		// 返回JSON格式提示消息给请求者

		me.setCode(code);
		me.setData(resObject);
		me.setMessage(message);

		return me;
	}

	
	
	/**
	 * 根据参数，得到SQL查询的起始记录
	 * 
	 * @param number
	 *            (每页记录个数)
	 * @param page
	 *            (从第page页开始查询)
	 * @return
	 */
	public static int getRecordBegin(int number, int page, int maxsize) {
		int recordBegin = 0;
		if (number < 1 || page < 1) {
			recordBegin = -1;
		} else {
			recordBegin = ((page - 1) * number);
		}

		if (recordBegin > maxsize) {
			recordBegin = maxsize;
		}

		return recordBegin;
	}

	public static int getRecordEnd(int number, int begin, int maxsize) {
		int recordEnd = begin + number;

		if (recordEnd > maxsize) {
			recordEnd = maxsize;
		}

		return recordEnd;
	}

	public static String getSubStr(String str, String pattern) {
		String oldStr;
		// System.out.println(str);
		// System.out.println(pattern);

		oldStr = str.substring(str.indexOf(pattern), str.length() - 1);

		int length = -1;
		if ((length = oldStr.indexOf(",")) < 0) {
			return (oldStr);
		} else {
			String tmpstr;
			tmpstr = oldStr.substring(0, length);
			return (tmpstr);
		}
	}
	

	
	public static void main(String[] args) {
		 String json = "{\"AutoCode\":\"1\"}";
		 json= new String(json.toLowerCase());

	}
	
	public static class T{
		private String AutoCode;

		public String getAutoCode() {
			return AutoCode;
		}

		public void setAutoCode(String autocode) {
			AutoCode = autocode;
		}

		@Override
		public String toString() {
			return "T [AutoCode=" + AutoCode + "]";
		}
		
		
	}
}
