package com.wre.game.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.entity.IosRechargeParam;
import io.netty.util.internal.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.crypto.Data;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公用方法
 *
 * @Author 申欣武
 * @Date 2018年6月11日10:59:22
 */
public class Fn {
	private final static Pattern intPaddern = Pattern.compile("^[+-]?[0-9]+$");
	private final static Pattern numFromFour=Pattern.compile("^\\d{4}$");
	private final static String regx = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$";//科学计数法正则表达式
	/**
	 * List<T> 转 json 保存到数据库
	 */
	public static <T> String listToJson(List<T> ts) {
		String jsons = JSON.toJSONString(ts);
		return jsons;
	}

	/**
	 * json 转 List<T>
	 */
	public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
		List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
		if (ts == null) {
			ts = new ArrayList<T>();
		}
		return ts;
	}

	/**
	 * 判断字符串是否可以转int
	 *
	 * @param str
	 *            字符串
	 * @return
	 */
	public static Boolean isInt(String str) {
		return str != null && intPaddern.matcher(str).find();
	}
	
	/**
	 * 判断是否是四位数字
	 */
	public static Boolean numFromFour(String str) {
		return str != null && numFromFour.matcher(str).find();
	}
	

	/**
	 * 判断对象是否可以转int
	 *
	 * @param o
	 * @return
	 */
	public static Boolean isInt(Object o) {
		if (o == null) {
			return false;
		}
		String str = o.toString();

		Matcher mer = intPaddern.matcher(str);
		return mer.find();
	}

	/**
	 * 对象转int(默认是对象的toString())，失败返回 failureVal
	 *
	 * @param o
	 * @param failureVal
	 * @return
	 */
	public static Integer toInt(Object o, int failureVal) {
		if (o == null) {
			return failureVal;
		}
		String str = o.toString();

		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			return failureVal;
		}
	}

	/**
	 * 对象转int(默认是对象的toString())，失败返回 0
	 *
	 * @param o
	 * @return
	 */
	public static Integer toInt(Object o) {
		if (o == null) {
			return 0;
		}
		String str = o.toString();
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	/**
	 * 对象转int(默认是对象的toString())，失败返回 默认值
	 * 
	 * @param o
	 * @param i
	 *            默认值
	 * @return
	 */
	public static Integer toInt(Object o, Integer i) {
		if (o == null) {
			return i;
		}
		String str = o.toString();
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			return i;
		}
	}

	/**
	 * 字符串重新组合 “1,2,a,3,4” -> 1,2,3,4
	 *
	 * @param t
	 * @return
	 */
	public static String clearNoInt(String t) {
		String[] arr = t.split(",");
		List<String> list = new ArrayList<String>();
		for (String val : arr) {
			if (isInt(val)) {
				list.add(val);
			}
		}

		return String.join(",", list);
	}
	

	/**
	 * 过滤字符串前后空格，支持全角
	 *
	 * @param str
	 * @return 若为null，返回空字符串
	 */
	public static String trim(String str) {
		if (str == null) {
			return "";
		}
		// 第1个：全角空格 encodeURI(' ')=%E3%80%80
		// 第2个：半角空格 encodeURI(' ')=%20
		// 第3个：特殊空格 encodeURI(' ')=%C2%A0
		while (str.startsWith("　") || str.startsWith(" ") || str.startsWith(" ")) {// 全角空格
			str = str.substring(1, str.length()).trim();
		}
		while (str.endsWith("　") || str.endsWith(" ") || str.endsWith(" ")) {// 全角空格
			str = str.substring(0, str.length() - 1).trim();
		}
		return str;
	}

	/**
	 * 过滤字符串前后空格，支持全角
	 *
	 * @param o
	 * @return 若为null，返回空字符串
	 */
	public static String trim(Object o) {
		if (o == null) {
			return "";
		}
		return trim(o.toString());
	}

	public static String md5(String s) {
		return DigestUtils.md5Hex(s);
	}

	public static String md5_16(String s) {
		return DigestUtils.md5Hex(s).substring(8, 24);
	}

	/**
	 * 对象转字符串，当null时，返回空字符串，并去除前后空格
	 *
	 * @param o
	 * @return
	 */
	public static String toString(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString().trim();
	}


	/**
	 * @param param 参数
	 * @param encode 编码
	 * @param isLower 是否小写
	 * @return
	 */
	public static String formatUrlParam(Map<String, String> param, String encode, boolean isLower) {
		String params = "";
		Map<String, String> map = param;

		try {
			List<Map.Entry<String, String>> itmes = new ArrayList<Map.Entry<String, String>>(map.entrySet());

			//对所有传入的参数按照字段名从小到大排序
			//Collections.sort(items); 默认正序
			//可通过实现Comparator接口的compare方法来完成自定义排序
			Collections.sort(itmes, new Comparator<Map.Entry<String, String>>() {
				@Override
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					// TODO Auto-generated method stub
					return (o1.getKey().toString().compareTo(o2.getKey()));
				}
			});

			//构造URL 键值对的形式
			StringBuffer sb = new StringBuffer();
			for (Map.Entry<String, String> item : itmes) {
				if (StringUtils.isNotBlank(item.getKey())) {
					if(StringUtil.isNullOrEmpty(item.getValue())){
						continue;
					}
					String key = item.getKey();
					String val = item.getValue();
//					val = URLEncoder.encode(val, encode);
					if (isLower) {
						sb.append(key.toLowerCase() + "=" + val);
					} else {
						sb.append(key + "=" + val);
					}
					sb.append("&");
				}
			}

			params = sb.toString();
			if (!params.isEmpty()) {
				params = params.substring(0, params.length() - 1);
			}
		} catch (Exception e) {
			return "";
		}
		return params;
	}

	/**
	 * 对象转double，失败返回0
	 *
	 * @param o
	 * @return
	 */
	public static Double toDouble(Object o) {
		if (o == null) {
			return 0d;
		}
		String str = o.toString();

		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException ex) {
			return 0d;
		}
	}

	/**
	 * 判断字符串是否可以转 指定的时间类型
	 *
	 * @param strDate
	 * @param dateFormat
	 *            时间格式
	 * @return
	 */
	public static Boolean isDateFormat(String strDate, String dateFormat) {
		if (strDate == null) {
			return false;
		}

		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		format.setLenient(false);
		try {
			format.parse(strDate);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static Boolean isBlank(String str){
		return StringUtils.isBlank(str)||"null".equals(str);
	}

	/**
	 * 字符串传时间类型带
	 *
	 * @param datastr
	 *            传入时间字符串 如:2016-01-01,2016-01-01 00:00:00
	 * @return 字符串转时间类型, 如果转换成功返回类型为时间类型：2016-01-01 00：00：00，转换失败返回null
	 */
	public static Timestamp toTimestamp(String datastr) {
		datastr = datastr.length() == 10 ? datastr + " 00:00:00" : datastr;
		if (!Fn.isDateFormat(datastr, "yyyy-MM-dd HH:mm:ss")) {
			return null;
		}
		return Timestamp.valueOf(datastr);
	}

	/**
	 * 获取当前时间字符串格式 yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String getNowStr() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	// /**
	// * 集合转化分页 2016年9月10日 下午5:57:04
	// *
	// * @param list
	// * 需分页的集合
	// * @param pageIndex
	// * 页码
	// * @param pageSize
	// * 每页记录数
	// * @return 返回Page对象，Page对象的getList()返回结果是经过筛选后的当前pageIndex页的记录
	// */
	// public static <T> PageList<T> listToPage(Collection<T> list, int
	// pageIndex, int pageSize) {
	// if (pageIndex <= 0) {
	// pageIndex = 1;
	// }
	// if (pageSize <= 0) {
	// pageSize = 1;
	// }
	//
	// int rowTotal = list.size();// 总条数
	// int pageTotal = rowTotal / pageSize;// 总页数
	// int startIndex = (pageIndex - 1) * pageSize;// 所在页码的开始索引
	// int endIndex = startIndex + pageSize;// 所在页码的结束索引
	//
	// // 若最后一页不满pageSize条
	// if (rowTotal % pageSize > 0) {
	// pageTotal++;
	// }
	//
	// List<T> tmpResult = new ArrayList<>();// 分页后的结果集
	//
	// int itIndex = 0;// 迭代器索引
	// for (T aList : list) {
	// if (itIndex >= endIndex) {
	// break;// 找完后退出
	// }
	//
	// // 若页码在指的开始索引和结束索引，则加入结果集
	// if (startIndex <= itIndex && itIndex < endIndex) {
	// tmpResult.add(aList);
	// }
	// itIndex++;
	// }
	// return new PageList<>(tmpResult);
	// }

	/**
	 * 转换前端传回的 1,a,3 字符串类型为List集合
	 * 
	 * @param t
	 * @return
	 */
	public static List<Integer> intStrToList(String t) {
		if (t == null || "".equals(t)) {
			return new ArrayList<>();
		}
		t = t.replace("，", ",");
		String[] arr = t.split(",");
		List<Integer> list = new ArrayList<>();
		for (String val : arr) {
			if (isInt(val)) {
				list.add(Fn.toInt(val));
			}
		}
		return list;
	}

	/**
	 * 判断字符串是否为空，去除前后空格
	 *
	 * @param str
	 * @return true:空的 false:非空
	 */
	public static Boolean isStrEmpty(String str) {
		return str == null || trim(str).isEmpty();
	}

	/**
	 * 字符串转Date，若字符串非 dateFormat 格式，则返回defaultValue
	 *
	 * @param strDate
	 * @param dateFormat
	 *            需要转换的日期格式，如yyyy-MM
	 * @param defaultValue
	 *            转换失败返回的默认值
	 * @return
	 */
	public static Date toDate(String strDate, String dateFormat, Date defaultValue) {
		if (isStrEmpty(strDate)) {
			return defaultValue;
		}
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		format.setLenient(false);// false：比较宽松的日期验证方式
		try {
			return format.parse(strDate);
		} catch (ParseException e) {
			return defaultValue;
		}
	}

	/**
	 * 字符串转Date，若字符串非 yyyy-MM 格式，则返回null
	 *
	 * @param strDate
	 * @return
	 */
	public static Date toDateYM(String strDate) {
		return toDate(strDate, "yyyy-MM", null);
	}

	/**
	 * 时间相减
	 * 
	 * @param s
	 *            开始时间
	 * @param e
	 *            结束时间
	 * @param d
	 *            比较类型 y：年，m：yue，d：日
	 * @return
	 */
	public static int TimeSubtract(Date s, Date e, String d) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(s);
		c2.setTime(e);
		if ("y".equals(d)) {
			return c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		} else if ("m".equals(d)) {
			return c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		} else if ("d".equals(d)) {
			return c2.get(Calendar.DAY_OF_MONTH) - c1.get(Calendar.DAY_OF_MONTH);
		}

		return 0;
	}

	/**
	 * 保留六位位小数
	 *
	 * @param val
	 * @return
	 */
	public static String toFixed(Double val) {
		DecimalFormat df = new DecimalFormat("0.000000");
		return df.format(val);
	}

	/**
	 * String转换为double保留6位小数
	 */
	public static Double StringToDouble(String num) {
		Double db = Double.parseDouble(num);
		DecimalFormat df = new DecimalFormat("0.000000");
		return Double.parseDouble(df.format(db));
	}

	/**
	 * 返回消耗时间
	 * @param time
	 * @return
	 */
	public static int endTime(long time){
		Date date=new Date();
		return toInt(date.getTime()-time);
	}

	/**
	 * String转换为float保留2位小数
	 */
	public static Double StringToDoubleByTwo(String num) {
		if(StringUtils.isBlank(num)){
			return 0.00;
		}
		Double db = Double.parseDouble(num);
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.parseDouble(df.format(db));
	}
	/**
	 * 判断是否为科学计数
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
	     Pattern pattern = Pattern.compile(regx);
	     return pattern.matcher(str).matches();
	 }

	/**
	 * 给时间添加（日，月，年）
	 *
	 * @param d
	 * @param num
	 * @param dateType
	 *            y:年 m：月 d：日
	 * @return
	 */
	public static Date addDate(Date d, int num, String dateType) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		if (dateType.equals("y")) {
			c.set(Calendar.YEAR, c.get(Calendar.YEAR) + num);
		} else if (dateType.equals("m")) {
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) + num);
		} else if (dateType.equals("d")) {
			c.set(Calendar.DATE, c.get(Calendar.DATE) + num);
		}
		return c.getTime();
	}

	/**
	 * 转时间字符串格式 （如 年-月-日 时:分:秒 毫秒 格式为 yyyy-MM-dd HH:mm:ss SSS）
	 *
	 * @param date
	 * @param dateFormat
	 *            指定格式
	 * @return 转换失败反回空字符串
	 */
	public static String toDateTimeStr(Date date, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		format.setLenient(false);
		try {
			return format.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 转时间字符串格式 （如 年-月-日 时:分:秒 毫秒 格式为 yyyy-MM-dd HH:mm:ss SSS）
	 *
	 * @param date
	 * @return 转换失败反回空字符串
	 */
	public static String toDateTimeStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setLenient(false);
		try {
			return format.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 转时间字符串格式 （如 年/月/日 格式为 yyyy/MM/dd）
	 *
	 * @param date
	 * @return 转换失败反回空字符串
	 */
	public static String toDateTimeToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		format.setLenient(false);
		try {
			return format.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取几位随机数任意
	 * 
	 * @param i
	 *            传入几位数
	 * @return 多少位随机数 2018年6月11日11:12:30 申欣武
	 */
	public static  int getMath(int i) {
		return (int) ((Math.random() * 9 + 1) * Math.pow(10, (i - 1)));
	}

	/**
	 * 判断一个url是否合法
	 */
	public static boolean isUrl(String pInput) {
		if (pInput == null) {
			return false;
		}
		String regEx = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(pInput);
		return matcher.matches();
	}

	/**
	 * 获取随机数
	 * @return
	 */
	public static Integer getUnreadCount(){
		return 50+(int)(Math.random()*(50));
	}
	
	
	
	  /**
     * 判断是否为最新版本方法 将版本号根据.切分为int数组 比较
     *
     * @param localVersion  本地版本号
     * @param onlineVersion 线上版本号
     * @return 是否为新版本
     */
	public static boolean versionCode(String localVersion, String onlineVersion) {
		 if (localVersion == null || onlineVersion == null) {
	            throw new IllegalArgumentException("argument may not be null !");
	        }
	        if (localVersion.equals(onlineVersion)) {
	            return false;
	        }
	        String[] localArray = localVersion.replaceAll("[^0-9.]", "").split("[.]");
	        String[] onlineArray = onlineVersion.replaceAll("[^0-9.]", "").split("[.]");
	        int length = localArray.length < onlineArray.length ? localArray.length : onlineArray.length;
	        for (int i = 0; i < length; i++) {
	            if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(localArray[i])) {
	                return true;
	            } else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(localArray[i])) {
	                return false;
	            }
	            // 相等 比较下一组值
	        }
	        //比较最后差异组数值
	        if (localArray.length < onlineArray.length) {
	            return Integer.parseInt(onlineArray[onlineArray.length - 1]) > 0;
	        } else if (localArray.length > onlineArray.length) {
	            return 0 > Integer.parseInt(localArray[localArray.length - 1]);
	        }
	        return true;
	}
	
	/**
	 * 将整型数字转换为二进制字符串，一共32位，不舍弃前面的0
	 * @param number 整型数字
	 * @return 二进制字符串
	 */
	public static  String get32BitBinString(int number) {
	    StringBuilder sBuilder = new StringBuilder();
	    for (int i = 0; i < 32; i++){
	        sBuilder.append(number & 1);
	        number = number >>> 1;
	    }
	    return  sBuilder.reverse().toString();
	}
	
	/**
	 * 科学技术法保留0位小数
	 * @param num
	 * @return
	 */
	public static String NumeirToString(Double num){
		if(num==null){
			return "";
		}
        BigDecimal bd2 = new BigDecimal(num);
        return bd2.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
	}
	
	 public   static final String  getUUID(){
	    	return Fn.toString(UUID.randomUUID());
	    }
	
	/**
	 * 科学技术法保留2位小数
	 * @param num
	 * @return
	 */
	public static String NumeirToStringBy4(Double num){
		if(num==null){
			return "";
		}
        BigDecimal bd2 = new BigDecimal(num);
        return bd2.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
	}
	
	

/**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     *
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static String roundByScale(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if(scale == 0){
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for(int i=0;i<scale;i++){
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }

    /**
     * 获取当前文件的文件后缀
     * @param fileName 文件名
     * @return
     */
   public static String getFilesuf(String fileName){
	   if(StringUtils.isBlank(fileName)){
		   return null;
		   
	   }
		return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
   }
    
   public static String subPhone(String phone){
	   if(StringUtils.isBlank(phone)){
		   return "";
	   }
	   return  phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
   }
   
   
   /**
    * 转换中文编码
    * @param str
    * @return
 * @throws UnsupportedEncodingException 
    */
   public static String URLEncoderUtf8(String str) throws UnsupportedEncodingException{
	   if(StringUtils.isBlank(str)){
		   return "";
	   }
	   return URLEncoder.encode(str,"utf-8");
   }
   
   
   /**
    * 转换中文编码
    * @param str
    * @return
 * @throws UnsupportedEncodingException 
    */
   public static String URLDecoderUtf8(String str) throws UnsupportedEncodingException{
	   if(StringUtils.isBlank(str)){
		   return "";
	   }
	   return URLDecoder.decode(str, "utf-8");
   }
   
   /**
    * 替换符号
    * @param str
    * @return
    */
   public static String email_star(String str){
	   if(StringUtils.isBlank(str)){
		   return "";
	   }
	   StringBuffer address = new StringBuffer(str);
       
      
       return  Fn.toString(address.replace(1, address.indexOf("@")-1, "****"));
   }
   
   /**
    * 替换符号
    * @param str
    * @return
    */
   public static String string_star(String str){
	   if(StringUtils.isBlank(str)){
		   return "";
	   }
	   StringBuffer address = new StringBuffer(str);
	 if(str.length()>4){
		return   Fn.toString(address.replace(2, str.length()-2, "****"));
	 }else if(str.length()<=4&str.length()>2){
			return  Fn.toString(address.replace(1, str.length()-1, "****"));
	 }
	 else{
		  return str;
	 }
   }


   
   
	public static void main(String[] args){
		String json="{\n" +
				"\t\"receipt\": {\n" +
				"\t\t\"receipt_type\": \"ProductionSandbox\",\n" +
				"\t\t\"adam_id\": 0,\n" +
				"\t\t\"app_item_id\": 0,\n" +
				"\t\t\"bundle_id\": \"com.warrior.ygiosgj\",\n" +
				"\t\t\"application_version\": \"1\",\n" +
				"\t\t\"download_id\": 0,\n" +
				"\t\t\"version_external_identifier\": 0,\n" +
				"\t\t\"receipt_creation_date\": \"2021-04-29 10:10:46 Etc/GMT\",\n" +
				"\t\t\"receipt_creation_date_ms\": \"1619691046000\",\n" +
				"\t\t\"receipt_creation_date_pst\": \"2021-04-29 03:10:46 America/Los_Angeles\",\n" +
				"\t\t\"request_date\": \"2021-04-30 03:04:52 Etc/GMT\",\n" +
				"\t\t\"request_date_ms\": \"1619751892272\",\n" +
				"\t\t\"request_date_pst\": \"2021-04-29 20:04:52 America/Los_Angeles\",\n" +
				"\t\t\"original_purchase_date\": \"2013-08-01 07:00:00 Etc/GMT\",\n" +
				"\t\t\"original_purchase_date_ms\": \"1375340400000\",\n" +
				"\t\t\"original_purchase_date_pst\": \"2013-08-01 00:00:00 America/Los_Angeles\",\n" +
				"\t\t\"original_application_version\": \"1.0\",\n" +
				"\t\t\"in_app\": [{\n" +
				"\t\t\t\"quantity\": \"1\",\n" +
				"\t\t\t\"product_id\": \"yubi5\",\n" +
				"\t\t\t\"transaction_id\": \"1000000806945293\",\n" +
				"\t\t\t\"original_transaction_id\": \"1000000806945293\",\n" +
				"\t\t\t\"purchase_date\": \"2021-04-29 09:08:53 Etc/GMT\",\n" +
				"\t\t\t\"purchase_date_ms\": \"1619687333000\",\n" +
				"\t\t\t\"purchase_date_pst\": \"2021-04-29 02:08:53 America/Los_Angeles\",\n" +
				"\t\t\t\"original_purchase_date\": \"2021-04-29 09:08:53 Etc/GMT\",\n" +
				"\t\t\t\"original_purchase_date_ms\": \"1619687333000\",\n" +
				"\t\t\t\"original_purchase_date_pst\": \"2021-04-29 02:08:53 America/Los_Angeles\",\n" +
				"\t\t\t\"is_trial_period\": \"false\"\n" +
				"\t\t}, {\n" +
				"\t\t\t\"quantity\": \"1\",\n" +
				"\t\t\t\"product_id\": \"yubi5\",\n" +
				"\t\t\t\"transaction_id\": \"1000000806984563\",\n" +
				"\t\t\t\"original_transaction_id\": \"1000000806984563\",\n" +
				"\t\t\t\"purchase_date\": \"2021-04-29 10:08:48 Etc/GMT\",\n" +
				"\t\t\t\"purchase_date_ms\": \"1619690928000\",\n" +
				"\t\t\t\"purchase_date_pst\": \"2021-04-29 03:08:48 America/Los_Angeles\",\n" +
				"\t\t\t\"original_purchase_date\": \"2021-04-29 10:08:48 Etc/GMT\",\n" +
				"\t\t\t\"original_purchase_date_ms\": \"1619690928000\",\n" +
				"\t\t\t\"original_purchase_date_pst\": \"2021-04-29 03:08:48 America/Los_Angeles\",\n" +
				"\t\t\t\"is_trial_period\": \"false\"\n" +
				"\t\t}, {\n" +
				"\t\t\t\"quantity\": \"1\",\n" +
				"\t\t\t\"product_id\": \"jinhulu\",\n" +
				"\t\t\t\"transaction_id\": \"1000000805091252\",\n" +
				"\t\t\t\"original_transaction_id\": \"1000000805091252\",\n" +
				"\t\t\t\"purchase_date\": \"2021-04-25 08:49:42 Etc/GMT\",\n" +
				"\t\t\t\"purchase_date_ms\": \"1619340582000\",\n" +
				"\t\t\t\"purchase_date_pst\": \"2021-04-25 01:49:42 America/Los_Angeles\",\n" +
				"\t\t\t\"original_purchase_date\": \"2021-04-25 08:49:42 Etc/GMT\",\n" +
				"\t\t\t\"original_purchase_date_ms\": \"1619340582000\",\n" +
				"\t\t\t\"original_purchase_date_pst\": \"2021-04-25 01:49:42 America/Los_Angeles\",\n" +
				"\t\t\t\"is_trial_period\": \"false\"\n" +
				"\t\t}, {\n" +
				"\t\t\t\"quantity\": \"1\",\n" +
				"\t\t\t\"product_id\": \"qianxiang\",\n" +
				"\t\t\t\"transaction_id\": \"1000000805087796\",\n" +
				"\t\t\t\"original_transaction_id\": \"1000000805087796\",\n" +
				"\t\t\t\"purchase_date\": \"2021-04-25 08:35:47 Etc/GMT\",\n" +
				"\t\t\t\"purchase_date_ms\": \"1619339747000\",\n" +
				"\t\t\t\"purchase_date_pst\": \"2021-04-25 01:35:47 America/Los_Angeles\",\n" +
				"\t\t\t\"original_purchase_date\": \"2021-04-25 08:35:47 Etc/GMT\",\n" +
				"\t\t\t\"original_purchase_date_ms\": \"1619339747000\",\n" +
				"\t\t\t\"original_purchase_date_pst\": \"2021-04-25 01:35:47 America/Los_Angeles\",\n" +
				"\t\t\t\"is_trial_period\": \"false\"\n" +
				"\t\t}, {\n" +
				"\t\t\t\"quantity\": \"1\",\n" +
				"\t\t\t\"product_id\": \"xingyizhizhao2\",\n" +
				"\t\t\t\"transaction_id\": \"1000000805036305\",\n" +
				"\t\t\t\"original_transaction_id\": \"1000000805036305\",\n" +
				"\t\t\t\"purchase_date\": \"2021-04-25 04:13:14 Etc/GMT\",\n" +
				"\t\t\t\"purchase_date_ms\": \"1619323994000\",\n" +
				"\t\t\t\"purchase_date_pst\": \"2021-04-24 21:13:14 America/Los_Angeles\",\n" +
				"\t\t\t\"original_purchase_date\": \"2021-04-25 04:13:14 Etc/GMT\",\n" +
				"\t\t\t\"original_purchase_date_ms\": \"1619323994000\",\n" +
				"\t\t\t\"original_purchase_date_pst\": \"2021-04-24 21:13:14 America/Los_Angeles\",\n" +
				"\t\t\t\"is_trial_period\": \"false\"\n" +
				"\t\t}]\n" +
				"\t},\n" +
				"\t\"environment\": \"Sandbox\",\n" +
				"\t\"status\": 0\n" +
				"}";
		JSONObject appleReturn = JSONObject.parseObject(json);
		IosRechargeParam iosParam=JSONObject.parseObject(appleReturn.getString("receipt"),IosRechargeParam.class);
		iosParam.ListToMap();
		System.out.println(iosParam);

	}

}