package com.wre.game.api.data.dto;

import com.wre.game.api.data.IpBlock;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * After login or subscription, API will response following data. In case of WEB, create cookies In
 * case of WEB, response this object as JSON
 *
 */
public class UserLoginDTO {

	private String token;
	private Long userId;
	private String openId;
	private String uuid;
	private String sessionKey;
	private Integer block;
	private Boolean legal;
	private String nickName;
	private String avatarUrl;
	private IpBlock ipBlock;
	private String loginTime;
	private String groupType;

	@Override
	public String toString() {
		return "UserLoginDTO{" +
				"token='" + token + '\'' +
				", userId=" + userId +
				", openId='" + openId + '\'' +
				", uuid='" + uuid + '\'' +
				", sessionKey='" + sessionKey + '\'' +
				", block=" + block +
				", legal=" + legal +
				", nickName='" + nickName + '\'' +
				", avatarUrl='" + avatarUrl + '\'' +
				", ipBlock=" + ipBlock +
				", loginTime='" + loginTime + '\'' +
				", groupType='" + groupType + '\'' +
				'}';
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public Integer getBlock() {
		return block;
	}

	public void setBlock(Integer block) {
		this.block = block;
	}

	public Boolean getLegal() {
		return legal;
	}

	public void setLegal(Boolean legal) {
		this.legal = legal;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public IpBlock getIpBlock() {
		return ipBlock;
	}

	public void setIpBlock(IpBlock ipBlock) {
		this.ipBlock = ipBlock;
	}

}
