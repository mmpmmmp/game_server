package com.wre.game.api.netty.server.manager;

import com.alibaba.fastjson.JSON;
import com.wre.game.api.dao.RechargeDataInfoMapper;
import com.wre.game.api.data.entity.RechargeDataInfo;
import com.wre.game.api.netty.entity.*;
import com.wre.game.api.util.Fn;
import com.wre.game.api.util.RSA;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


/**
 * 
 * 充值
 * 
 */
@Component
public class RechargeManager {

	@Resource
	private RechargeDataInfoMapper rechargeDataInfoMapper;

	private static final Logger log = LoggerFactory.getLogger(RechargeManager.class);
	/** uu验证key */
	public static String privateKey = "key";
	/** uu验证key */
	public static String publicKey = "key";
	/** uu验证签名类型 */
	public static String signtype = "RSA";
	/** 周礼包 */
	private static int weekGift = 0;
	/** 角色 */
	private static int role = 3;
	/** 通行证 */
	private static int permit = 4;
	/** 每日礼包 */
	private static int dayGift = 5;
	/** 订阅特权 */
	private static int privilege = 6;
	/** 一元首冲礼包 */
	private static int onePackage = 7;
	//1:每日钻石礼包
	private static int day_gift_diamond = 1;
	//2;每日圣器礼包
	private static int day_gift_weapon = 2;
	//3:每日材料礼包
	private static int day_gift_materil = 3;
	//订阅特权表Id
	public static int privilegeId = 101;


	/** quickSdk回调地址 */
	//1 客户端发起支付请求到服务器和sdk
	//2 服务器收到请求处理订单，存入数据库将状态置为0(下单成功未支付状态)
	//3 客服端充值成功sdk发送回调信息到服务器
	//4 服务器收到回调信息对比订单号一致则修改订单状态为1同时发货
	public RechargeResult reqRechargeCallback(RechargeInfo rechargeInfo) {
		log.info("收到充值订单:" + JSON.toJSONString(rechargeInfo));
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(rechargeInfo.getAmount()).append("&")
					//
					.append(rechargeInfo.getChannelId()).append("&")
					//
					.append(rechargeInfo.getGameCustomInfo()).append("&")
					//
					.append(rechargeInfo.getOrderId()).append("&")
					//
					.append(rechargeInfo.getProductId()).append("&")
					//
					.append(rechargeInfo.getServerId()).append("&")
					//
					.append(rechargeInfo.getState()).append("&")
					//
					.append(rechargeInfo.getUserId()).append("&")
					//
					.append(rechargeInfo.getUuGameId());
			log.info("sb:"+sb);
			String content = sb.toString();
			String sign = RSA.sign(content, privateKey, signtype);
			log.info("sign:"+sign);
			if (RSA.doCheck(content, sign, publicKey, signtype)) {
				log.info("Result : 验签成功！");
			} else {
				log.info("Result : 验签失败！");
				log.error("充值RSA验证失败:" + JSON.toJSONString(rechargeInfo));
				return new RechargeResult(-100, "RSA验证失败");
			}
			/**查看是否有该订单*/

			RechargeDataInfo bean=rechargeDataInfoMapper.selectByPrimaryKey(Long.parseLong(rechargeInfo.getGameCustomInfo()));
			if(bean ==null){
				/**存历史哭*/
				RechargeDataInfo rechargeDataInfo=createRechargeBean(rechargeInfo);
				rechargeDataInfoMapper.insertSelectiveLog(rechargeDataInfo);
				return new RechargeResult(1);
			}
			if(!bean.getStatue().equals(0)){
				log.error("该订单已验证:" + JSON.toJSONString(rechargeInfo));
				return new RechargeResult(101, "该订单已验证");
			}

			if(!bean.getAmount().equals(rechargeInfo.getAmount())){
				log.error("金额不正确:" + JSON.toJSONString(rechargeInfo));
				return new RechargeResult(101, "金额不正确");
			}
			//sdk的订单id肯定是不能重复的
//			if (rechargeInfo.getOrderId().equals(bean.getOrderid())) {
//				log.error("充值订单Id重复:" + JSON.toJSONString(rechargeInfo));
//				return new RechargeResult(101, "充值订单Id重复");
//			}
			/**保存订单在本地*/
			RechargeDataInfo rechargeDataInfo=createRechargeBean(rechargeInfo);

			rechargeDataInfoMapper.updateByPrimaryKeySelective(rechargeDataInfo);
			return new RechargeResult(1);
		} catch (Exception e) {
			log.error("充值订单解析失败：" + JSON.toJSONString(rechargeInfo) + e.getMessage(),e);
		}
		return new RechargeResult(-100, "充值订单解析失败");
	}


	/**
	 * 创建充值bean
	 * @return
	 */
	public RechargeDataInfo createRechargeBean(RechargeInfo rechargeInfo) {
		RechargeDataInfo bean = new RechargeDataInfo();
		bean.setAmount(Fn.toString(rechargeInfo.getAmount()));
		bean.setProductid(Fn.toString(rechargeInfo.getProductId()));
		bean.setId(Long.parseLong(rechargeInfo.getGameCustomInfo()));
		bean.setUserid(rechargeInfo.getUserId());
		bean.setServerid(rechargeInfo.getServerId());
		bean.setUugameid(rechargeInfo.getUuGameId());
		bean.setChannelid(rechargeInfo.getChannelId());
		bean.setShoptype(0);
		bean.setStatue(1);
		bean.setOrderid(rechargeInfo.getOrderId());
		bean.setCreateat(new Date());
		bean.setUpdateat(new Date());
		return bean;
	}


}