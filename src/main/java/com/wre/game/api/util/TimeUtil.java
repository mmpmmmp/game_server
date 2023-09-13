package com.wre.game.api.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtil {

	private static final Logger log = LoggerFactory.getLogger(TimeUtil.class);

	private static final long MILLISECONDOFDAY = 24 * 60 * 60 * 1000;

	/** 线程安全的 SimpleDateFormat（yyyyMMddHHmmss）格式 对象获取 */
	private static final ThreadLocal<SimpleDateFormat> dataformat = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMddHHmmss");
		};
	};

	/** 线程安全的 SimpleDateFormat（yyyy-MM-dd HH:mm:ss）格式 对象获取 */
	private static final ThreadLocal<SimpleDateFormat> dataformat2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		};
	};

	/** 线程安全的 SimpleDateFormat（yyyyMMdd-HHmmss）格式 对象获取 */
	private static final ThreadLocal<SimpleDateFormat> dataformat3 = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd-HHmms");
		};
	};
	/** 线程安全的 SimpleDateFormat（yyyyMM）格式 对象获取 */
	private static final ThreadLocal<SimpleDateFormat> dataformat4 = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMM");
		};
	};

	private static final ThreadLocal<SimpleDateFormat> dataformat5 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		};
	};

	public static Date getAfterDayTime(Date d, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	public static Date getBeforeDayTime(Date d, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, -day);
		return calendar.getTime();
	}

	/** 获取int时间戳 */
	public static int getNowTime() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	/** 获取int时间戳 */
	public static int getTime(Date date) {

		return (int) (date.getTime() / 1000);
	}

	/**
	 * 获取指定时间到现在的时间数（毫秒）
	 * 
	 * @param time
	 * @return
	 */
	public static long getDurationToNow(long time) {
		return System.currentTimeMillis() - time;
	}

	/**
	 * 获取指定时间到现在的时间数（秒）
	 * 
	 * @param time
	 * @return 秒
	 */
	public static int getDurationToNowSec(long time) {
		return (int) (getDurationToNow(time) / 1000);
	}

	/**
	 * 判断两个时间中间所差天数
	 * 
	 * @return
	 */
	public static int getBetweenDays(long time1, long time2) {
		Calendar instance1 = Calendar.getInstance();
		instance1.setTimeInMillis(time1);
		instance1.set(Calendar.HOUR_OF_DAY, 0);
		instance1.set(Calendar.MINUTE, 0);
		instance1.set(Calendar.SECOND, 0);
		instance1.set(Calendar.MILLISECOND, 0);
		Calendar instance2 = Calendar.getInstance();
		instance2.setTimeInMillis(time2);
		instance2.set(Calendar.HOUR_OF_DAY, 0);
		instance2.set(Calendar.MINUTE, 0);
		instance2.set(Calendar.SECOND, 0);
		instance2.set(Calendar.MILLISECOND, 0);
		return (int) ((instance1.getTimeInMillis() - instance2.getTimeInMillis()) / (24 * 60 * 60 * 1000));
	}


	/**
	 * 时间戳转换为时间
	 */
	public static Date formatDate(String time){
		return new Date(Long.parseLong(time)*1000L);
	}

	/**
	 * 判断两个时间是否在同一天
	 * 
	 * @param time
	 * @param time2
	 * @return
	 */
	public static boolean isSameDay(int time, int time2) {
		return isSameDay(time * 1000L, time2 * 1000L);
	}

	/**
	 * 判断两个时间是否在同一天
	 * 
	 * @param time
	 * @param time2
	 * @return
	 */
	public static boolean isSameDay(long time, long time2) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		int d1 = instance.get(Calendar.DAY_OF_YEAR);
		int y1 = instance.get(Calendar.YEAR);
		instance.setTimeInMillis(time2);
		int d2 = instance.get(Calendar.DAY_OF_YEAR);
		int y2 = instance.get(Calendar.YEAR);
		return d1 == d2 && y1 == y2;
	}

	/**
	 * 判断两个时间是否是同一年同一月
	 * 
	 * @param time
	 * @param time2
	 * @return
	 */
	public static boolean isSameYearMonth(long time, long time2) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		int d1 = instance.get(Calendar.MONTH);
		int y1 = instance.get(Calendar.YEAR);
		instance.setTimeInMillis(time2);
		int d2 = instance.get(Calendar.MONTH);
		int y2 = instance.get(Calendar.YEAR);
		return d1 == d2 && y1 == y2;
	}

	/**
	 * 判断两个时间是同一周
	 * 
	 * @param time
	 * @param time2
	 * @return
	 */
	public static boolean isSameWeek(long time, long time2) {
		return GetCurTimeInMin(5, time) == GetCurTimeInMin(5, time2);
	}

	/**
	 * 判断是否在今天几点之后 true为大于指定时间
	 * 
	 * @param hour
	 * @return
	 */
	public static boolean isAfterHourOfCurrentDay(int hour, long time) {
		long currentTimeMillis = System.currentTimeMillis();
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(currentTimeMillis);
		instance.set(Calendar.HOUR_OF_DAY, hour);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		long timeInMillis = instance.getTimeInMillis();
		return time - timeInMillis > 0;
	}

	/**
	 * 获取几天前的时间
	 * 格式yyyy-MM-dd
	 * @param x
	 * @return
	 */
	public static String getDay(int x){
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.DATE,-x);
		Date date=calendar.getTime();
		return dataformat5.get().format(date);
	}

	/**
	 * 指定时间的年份
	 * 
	 * @param time
	 * @return
	 */
	public static int getYear(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.YEAR);
	}

	/**
	 * 指定时间的月份,0-11
	 * 
	 * @param time
	 * @return
	 */
	public static int getMonth(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.MONTH);
	}

	/**
	 * 获取日期(一个月内的第几天)
	 * 
	 * @param time
	 * @return
	 */
	public static int getDayOfMonth(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取小时
	 * 
	 * @param time
	 * @return
	 */
	public static int getDayOfHour(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取分钟
	 * 
	 * @param time
	 * @return
	 */
	public static int getDayOfMin(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.MINUTE);
	}

	/**
	 * 获取秒
	 * 
	 * @param time
	 * @return
	 */
	public static int getDayOfSecond(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.SECOND);
	}

	/**
	 * 获取指定时间 是一月内的第几周
	 * 
	 * @param time
	 * @return
	 */
	public static int getDayOfWeekInMonth(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * 获取星期几 1到7
	 * 
	 * @param time
	 * @return
	 */
	public static int getDayOfWeek(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		int i = instance.get(Calendar.DAY_OF_WEEK);
		if (i == 1) {
			return 7;
		} else {
			i -= 1;
		}
		return i;
	}

	/**
	 * 获取一年内的第几天
	 * 
	 * @param time
	 * @return
	 */
	public static int getDayOfYear(long time) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(time);
		return instance.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取1970至今的天数 （计数会在在每天指定的小时+1，用来判断每天X点清数据之类的,,和 Global.GENERAL_RESET_HOUR 配合使用）
	 * 
	 * @param hour 每天第X个小时+1
	 * @return
	 */
	public static int GetCurDay(int hour) {
		TimeZone zone = TimeZone.getDefault(); // 默认时区
		long s = System.currentTimeMillis() / 1000 - (hour) * 3600;
		if (zone.getRawOffset() != 0) {
			s = s + zone.getRawOffset() / 1000;
		}
		s = s / 86400;
		return (int) s;
	}

	/**
	 * 根据1970至今的天数获取Date
	 * 
	 * @return
	 */
	public static Date GetCurDate(long day) {
		TimeZone zone = TimeZone.getDefault(); // 默认时区
		long s = day * 86400;
		if (zone.getRawOffset() != 0) {
			s = s - zone.getRawOffset() / 1000;
		}
		return new Date(s * 1000);
	}

	public static void main(String[] args) {
		System.out.println(getDay(1));
	}

	/**
	 * 获取1970至今的时间,
	 * 
	 * @param x    1获取秒，2 分钟，3小时，4天数,5周数
	 * @param time 毫秒
	 * @return
	 */
	public static long GetCurTimeInMin(int x, long time) {
		TimeZone zone = TimeZone.getDefault(); // 默认时区
		long s = time / 1000;
		if (zone.getRawOffset() != 0) {
			s = s + zone.getRawOffset() / 1000;
		}
		switch (x) {
		case 1:
			break;
		case 2:
			s = s / 60;
			break;
		case 3:
			s = s / 3600;
			break;
		case 4:
			s = s / 86400;
			break;
		case 5:
			s = s / 86400 + 3;// 补足天数，星期1到7算一周
			s = s / 7;
			break;
		default:
			break;
		}
		return s;
	}

	/**
	 * 获取1970至今的时间, 1获取秒，2 分钟，3小时，4天数,5周数
	 * 
	 * @param x
	 * @return
	 */
	public static long GetCurTimeInMin(int x) {
		return GetCurTimeInMin(x, System.currentTimeMillis());
	}

	/**
	 * 获取1970至今的时间, 1获取秒，2 分钟，3小时，4天数,5周数
	 * 
	 * @param x
	 * @param hour 再初始时间上加X小时 用于判断每日X点清0 和 Global.GENERAL_RESET_HOUR 配合使用）
	 * @return
	 */
	public static long GetCurTimeInMin(int x, int hour) {
		return GetCurTimeInMin(x, System.currentTimeMillis() - (hour) * 3600l * 1000);
	}

	/**
	 * 获取今天指定时间的UNIX时间
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @param millisecond
	 * @return
	 */
	public static long getTheDayUnixTime(int hour, int minute, int second, int millisecond) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		return cal.getTimeInMillis();
	}

	/**
	 * 获取指定时间的UNIX时间
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @param millisecond
	 * @return
	 */
	public static long getTheUnixTime(int day, int hour, int minute, int second, int millisecond) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		return cal.getTimeInMillis();
	}

	/**
	 * 获取格式化的时间字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringDate(Date date) {
		return dataformat2.get().format(date);
	}

	/**
	 * 获取当前格式化的时间字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getNowStringDate() {
		return dataformat2.get().format(new Date());
	}

	/**
	 * 字符串转日期("yyyy-MM-dd HH:mm:ss");
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateByString(String date) {
		try {
			if(StringUtils.isBlank(date)){
				return  null;
			}
			return dataformat2.get().parse(date);
		} catch (ParseException e) {

			log.error("{}日期格式有误{}" + date + "yyyy-MM-dd HH:mm:ss"+e.getMessage(),e);
			return null;
		}
	}




	/**
	 * 字符串转日期("yyyy/MM/dd");
	 *
	 * @param date
	 * @return
	 */
	public static Date getDateByString5(String date) {
		try {
			return dataformat5.get().parse(date);
		} catch (ParseException e) {
			log.error(e.getMessage()+"{}日期格式有误{}" + date + "yyyy-MM-dd",e);
			return null;
		}
	}



	/**
	 * 字符串转日期("yyyyMMdd-HHmmss");
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateByString2(String date) {
		try {
			return dataformat3.get().parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			log.error("{}日期格式有误{}" + date + "yyyyMMdd-HHmmss");
			return null;
		}
	}

	/**
	 * 字符串转日期("yyyyMMddHHmmss");
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateByString3(String date) {
		try {
			return dataformat.get().parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			log.error("{}日期格式有误{}" + date + "yyyyMMddHHmmss");
			return null;
		}
	}

	/**
	 * 判定是不是今天
	 * 
	 * @author zhouyu @dateTime 2011-11-14 下午09:38:55
	 * @param time 时间毫秒串(<strong>注意:精确到毫秒</strong>)
	 * @return
	 */
	public static boolean isToday(final Long time) {
		if (null == time) {
			return false;
		}
		Calendar target = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		target.setTimeInMillis(time);
		if (target.get(Calendar.YEAR) == today.get(Calendar.YEAR) && target.get(Calendar.MONTH) == today.get(Calendar.MONTH) && target.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是不是本周
	 * 
	 * @author zhouyu @dateTime 2011-11-16 下午06:05:27
	 * @param time 时间毫秒串 <string>注意：精确到毫秒</strong>
	 * @return
	 */
	public static boolean isCurrentWeek(final Long time) {
		if (null == time) {
			return false;
		}
		Calendar target = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		target.setTimeInMillis(time);
		if (target.get(Calendar.YEAR) == today.get(Calendar.YEAR) && target.get(Calendar.WEEK_OF_YEAR) == today.get(Calendar.WEEK_OF_YEAR)) {
			return true;
		} else {
			return false;
		}
	}

	// /**
	// * 传入秒 ,获得String时间格式 X天X小时X分钟X秒
	// *
	// * @param mss
	// * @return
	// */
	// public static String GetTransformTime(long mss) {
	// String t = "";
	// long days = mss / (60 * 60 * 24);
	// long hours = (mss % (60 * 60 * 24)) / (60 * 60);
	// long minutes = (mss % (60 * 60)) / 60;
	// long seconds = mss % 60;
	// if (days > 0) {
	// t = days + ResManager.me().get("天") + hours + ResManager.me().get("小时") +
	// minutes + ("分") + seconds + ("秒");
	// } else {
	// t = hours + ResManager.me().get("小时") + minutes +
	// ResManager.me().get("分") + seconds + ResManager.me().get("秒");
	// }
	// return t;
	// }

	/**
	 * 根据毫秒数获取 天 时 分秒 格式
	 * 
	 * @param millisecond
	 * @return
	 */
	public static String millisecondToStr(long millisecond) {
		if (millisecond < 0) {
			return millisecond + "毫秒";
		}
		if (millisecond < 1000) {
			return "1秒";
		}
		long second = millisecond / 1000;
		String result = "";
		long day = second / (3600 * 24);
		if (day > 0) {
			result += day + "天";
		}
		second %= 3600 * 24;
		long hour = second / 3600;
		if (hour > 0) {
			result += hour + "小时";
		}
		second %= 3600;
		long minute = second / 60;
		if (minute > 0) {
			result += minute + "分";
		}
		second %= 60;
		if (second > 0) {
			result += second + "秒";
		}
		return result;
	}

	/**
	 * 获得最近指定星期X 返回毫秒
	 * 
	 * @param week 0=周日 ，1=周一 。。。 6=周六
	 * @return
	 */
	public static long getSoonWeek(int week) {
		return getSoonWeek(new Date(), week);
	}

	/**
	 * 获得最近指定星期X 返回毫秒
	 * 
	 * @param week 0=周日 ，1=周一 。。。 6=周六
	 * @return
	 */
	public static long getSoonWeek(long ms, int week) {
		Date date = new Date(ms);
		return getSoonWeek(date, week);
	}

	/**
	 * 获得最近指定星期X 返回毫秒
	 * 
	 * @param week 0=周日 ，1=周一 。。。 6=周六
	 * @return
	 */
	public static long getSoonWeek(Date date, int week) {
		if (week > 6) {
			week = week % 7;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// 如果当前日期不是周6则自动往后递增日期
		while (cal.get(Calendar.DAY_OF_WEEK) != week + 1) {
			cal.add(Calendar.DAY_OF_WEEK, 1);
		}
		return cal.getTimeInMillis();
	}

	/**
	 * 计算当前时间距截止的时间截止时间还有多少天,不足一天按一天处理
	 * 
	 * @param startTime
	 * @param deadLineDays
	 * @return
	 */

	public static int getLeftDays(long startTime, int deadLineDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTime);
		calendar.add(Calendar.DAY_OF_MONTH, deadLineDays);
		int lefDays = (int) ((calendar.getTime().getTime() - System.currentTimeMillis()) / MILLISECONDOFDAY);
		return lefDays <= 0 ? 1 : lefDays;
	}

	/**
	 * 格式化时间,获取时间的年月日格式：2016年9月2日
	 * 
	 * @param time
	 * @return
	 */
	public static String formateDate(long time) {
		String pattern = "yyyy'年'MM'月'dd'日'HH'时'mm'分'ss秒";
		return formateDate(time, pattern);
	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String formateDate(long time, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date date = new Date(time);
		return simpleDateFormat.format(date);
	}

	/**
	 * 得到年月日组合格式int ， 例子20110101 用来精确控制到 某一天 的时间
	 * 
	 * @return
	 */
	public static int getSeriesDay() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int sday = year * 10000 + month * 100 + day;
		return sday;
	}

	/**
	 * 得到年月 例子201101
	 * 
	 * @return
	 */
	public static String getSeriesMonth() {
		return dataformat4.get().format(new Date());
	}

	/**
	 * 传入要加的月数 获得下个月最后一天 的时间 i是正数就是后i月，i是负数就是前i月，i是几就加几月
	 * 
	 * @return
	 */
	public static Date getYearMonth(int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, i); // 得到某一天
		calendar.add(Calendar.MONTH, i); // 得到某一个月
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;// 这里月要加1

		String dateTime = year + "-" + month;
		dateTime += "-" + getDayCount(dateTime);
		dateTime = dateTime + " 23:59:59";
		Date date = getDateByString(dateTime);
		return date;
	}

	/**
	 * 给一个yyyy-MM(2013-11)日期格式，判断出所传月一共多少天
	 */
	public static int getDayCount(String dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		/*
		 * GregorianCalendar 是 Calendar 的一个具体子类， 提供了世界上大多数国家/地区使用的标准日历系统。
		 */
		Calendar calendar = new GregorianCalendar();
		try {
			/* 使用给定的 Date 设置此 Calendar 的时间 */
			calendar.setTime(sdf.parse(dateTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/* 返回此日历字段可能具有的最大值。DAY_OF_MONTH 用于指示一个月的某天 */
		int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return day;
	}
//
//	/**
//	 * 获得下一次触发时间，单位毫秒
//	 *
//	 * @return
//	 */
//	public static long getNextTriggerTime(CronExpression time) {
//		Date nextDate = time.getNextValidTimeAfter(new Date());
//		return nextDate.getTime();
//	}

	/**
	 * 获取月份开始的时间
	 * 
	 * @return
	 */
	public static long getMonthStart(long time) {
		// 得到接受任务时候的时期
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);// 0时
		cal.set(Calendar.MINUTE, 0);// 0分
		cal.set(Calendar.SECOND, 0);// 0秒
		cal.set(Calendar.MILLISECOND, 0);// 0毫秒
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTimeInMillis();
	}

	/**
	 * 获取月份结束的时间 下一天减去1s
	 * 
	 * @return
	 */
	public static long getMonthEnd(long time) {
		// 得到接受任务时候的时期
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);// 0时
		cal.set(Calendar.MINUTE, 0);// 0分
		cal.set(Calendar.SECOND, 0);// 0秒
		cal.set(Calendar.MILLISECOND, 0);// 0毫秒
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTimeInMillis() - 1000;
	}

	/**
	 * 当前月最大天数
	 * 
	 * @param time
	 * @return
	 */
	public static int getMonthMaxDay(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 设置为当前周周末
	 * 
	 * @param calendar
	 * @return
	 */
	public static Calendar setNowWeekSunday(Calendar calendar) {
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		int addday = 0;
		if (i != Calendar.SUNDAY) {
			addday = 7 - (i - 1);
		}
		calendar.add(Calendar.DATE, addday);
		return calendar;
	}

	/**
	 * 设置为当前周周1
	 * 
	 * @param calendar
	 * @return
	 */
	public static Calendar setNowWeekMonday(Calendar calendar) {
		int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (i == 0) {
			i = 7;
		}
		int addday = 1 - i;
		calendar.add(Calendar.DATE, addday);
		return calendar;
	}

	/**
	 * 获取当天0点时间时间戳
	 * 
	 * @return
	 */
	public static long getNow0ClockTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 获取从现在开始往后x天0点时间时间戳
	 * 
	 * @return
	 */
	public static long getAfterClockTime(int day) {
		return getNow0ClockTime() + day * 24 * 3600 * 1000;
	}

	/**
	 * 获取当天23:59:59的时间戳
	 * 
	 * @return
	 */
	public static long getToDayEndClockTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTimeInMillis();
	}

	/**
	 * 获得下周星期X 返回毫秒
	 * 
	 * @param week 0=周日 ，1=周一 。。。 6=周六
	 * @return
	 */
	public static long getNextWeek(int week) {
		week += 1;
		if (week > 6) {
			week = week % 7;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_WEEK, week);
		cal.add(Calendar.DAY_OF_WEEK, 7);
		return cal.getTimeInMillis();
	}

}
