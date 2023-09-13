package com.wre.game.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author Mr Wu
 * @version 2017年8月30日
 * 类说明:
 */

public class PropertiesUtils {
	private static final Logger log = LoggerFactory.getLogger(PropertiesUtils.class);


	public static final String  getValue(String properties,String key){
		
		Properties prop = new Properties();     
		try{
		            //读取属性文件a.properties
		            prop.load(new InputStreamReader(PropertiesUtils.class.getClassLoader().getResourceAsStream(properties), "GBK"));     ///加载属性列表
		            String value =prop.getProperty(key); 
		            return value;
		         }
		         catch(Exception e){
		        	 log.error("data.properties",e);
		        	 return null;
		         }
		     } 
	
	public static final String  getValueByMemcached(String key){
		
		Properties prop = new Properties();     
		try{
		     //读取属性文件a.properties
				InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream("memcached.properties");  
		            prop.load(in);     ///加载属性列表
		            String value =prop.getProperty(key); 
		            in.close();
		            return value;
		         }
		         catch(Exception e){
		        	 log.error("memcached.properties",e);
		        	 return null;
		         }
		     } 
	
	
	public static final String  getValueByMessage(String key){
		
		Properties prop = new Properties();     
		try{
		     //读取属性文件a.properties
				InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream("messages.properties");  
		            prop.load(new InputStreamReader(in, "utf-8"));     ///加载属性列表
		            String value =prop.getProperty(key); 
		            in.close();
		            return value;
		         }
		         catch(Exception e){
		        	 log.error("messages.properties",e);
		        	 return null;
		         }
		     } 
	
	
					
	
	public static void main(String[] args) {
		System.out.println(getValue("generator.properties","file.url"));
	}
	}

