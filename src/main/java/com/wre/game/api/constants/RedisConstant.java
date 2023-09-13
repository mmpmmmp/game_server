package com.wre.game.api.constants;

/**
 * redis常量key
 * @author zs
 * @date 2020-04-09 15:34:51
 */
public class RedisConstant {
	public static final String PREFIX_KEY_DATA = "data:user:";
	public static final String KEY_DATA_CONFIG = "data:game:config";
	public static final String KEY_DATA_PRODUCT_LIST = "data:game:productlist";
	public static final String KEY_DATA_CONFIG_IP = "data:ip:config";
	public static final String KEY_DATA_CONFIG_REQUEST = "data:request:config";
	public static final String PREFIX_KEY_DATA_RANK = "data:game:rank:";
	public static final String PREFIX_KEY_DATA_ACCESSTOKEN = "data:game:accesstoken:";
	// PREFIX_KEY_TOKEN + {TOKEN} = String.valueOf(userId)
	// PREFIX_KEY_USER + {userId} = HSET(fieldName = Channel, fieldValue = TOKEN)
	public static final String PREFIX_KEY_TOKEN = "account:token:";
	public static final String PREFIX_KEY_USER = "account:user:";

}
