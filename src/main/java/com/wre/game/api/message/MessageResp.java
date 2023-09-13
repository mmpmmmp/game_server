/**
 * 返回信息
 */
package com.wre.game.api.message;

import java.io.Serializable;

/**
 * 通用的返回类
 * @author
 */
public class MessageResp implements Serializable
{
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer     code;
    private String  message;
    private Object  data;
    
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "MessageResp [code=" + code + ", message=" + message + ", data=" + data + "]";
	}
	
	
   

}
