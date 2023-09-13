package com.wre.game.api.component;

import com.wre.game.api.constants.Channel;
import com.wre.game.api.data.RequestExpireInfo;
import com.wre.game.api.data.SessionDomain;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.exception.AuthException;

import java.util.Map;

public interface SessionComponent {

	/**
	 * Generate Token and Create Session in Redis by Channel. Token is generated based on User.uid,
	 * User.level, User.status, User.age15Plus
	 * 
	 * @param user
	 * @param channel
	 * @return JWT Token
	 */
	public String createSessionAndToken(User user, String channel);

	/**
	 * Create SessionDomain object as a result of parsing a Token
	 * 
	 * @param token
	 * @return
	 * @throws AuthException - token is not invalid or token is expired
	 */
	public SessionDomain getSessionByChannel(String token, String channel) throws AuthException;


	public Long getUserIdByChannel(String token, String channel) throws AuthException;

	/**
	 * Create SessionDomain object as a result of parsing a Token if checkExpired is false, expire
	 * checking is inactive
	 * 
	 * @param token
	 * @return
	 * @throws AuthException - token is not invalid or token is expired
	 */
	public SessionDomain getSessionByChannel(String token, String channel, boolean checkExpired);

	public Map<String, String> getUserIdByToken(String token);

	public void kickoutSessionAll(Long uid);

	public void kickoutSessionByChannel(Long uid, String channel);

	public void kickoutSessionByChannel(String token, String channel);

	public void kickoutSessionOtherChannel(Long uid, String channel);

    boolean getRequestExprie(RequestExpireInfo requestExpireInfo, String token);
}
