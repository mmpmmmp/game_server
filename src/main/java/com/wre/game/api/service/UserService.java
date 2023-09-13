package com.wre.game.api.service;

import com.wre.game.api.constants.ApiResponseMessage;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.data.IpBlock;
import com.wre.game.api.data.dto.UserLoginDTO;
import com.wre.game.api.data.dto.UserShareFromDTO;
import com.wre.game.api.data.entity.LogingRepUser;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.data.entity.UserGem;
import com.wre.game.api.data.param.InvitedReqParam;
import com.wre.game.api.data.param.UserLoginReqParam;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    public UserLoginDTO executeLogin(User user);
    /**
     * 有渠道的单独独立出一个方法
     * 目前只用于faceBook
     * 2020年8月21日14:57:56 申欣武
     * @param user 用户信息
     * @return
     */
    public UserLoginDTO executeLoginByChannel(User user);

    public RtCode executeEdit(User user);

    public UserLoginDTO executeExtendToken(String channel, String token, String appId, String version, User user);

    public List<UserShareFromDTO> getShareStatistics(String uuid, String shareType, String appId);

    public List<UserShareFromDTO> getShareStatisticsFunctionToday(String uuid, String shareType, String appId);


    /**
     * 根据渠道返回登录信息
     * channel wechat
     * 2020年8月20日15:04:39 申欣武
     * @param logingRepUser 登录传入参数
     * @param  body 登录传入参数
     * @param ipBlock ip参数
     * @return
     */
    public UserLoginDTO getUserLoginByWechat(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock);

    /**
     * 根据渠道返回登录信息
     * channel douyin
     * 2020年8月20日15:04:39 申欣武
     * @param logingRepUser 登录传入参数
     * @param  body 登录传入参数
     * @param ipBlock ip参数
     * @return
     */
    public UserLoginDTO getUserLoginByDouyin(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock);

    /**
     * 根据渠道返回登录信息
     * channel toutiao
     * 2020年8月20日15:04:39 申欣武
     * @param logingRepUser 登录传入参数
     * @param  body 登录传入参数
     * @param ipBlock ip参数
     * @return
     */
    public UserLoginDTO getUserLoginByToutiao(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock);


    /**
     * 根据渠道返回登录信息
     * channel toutiao
     * 2020年8月20日15:04:39 申欣武
     * @param logingRepUser 登录传入参数
     * @param  body 登录传入参数
     * @param ipBlock ip参数
     * @return
     */
    public UserLoginDTO getUserLoginByQqmini(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock);


    /**
     * 根据渠道返回登录信息
     * channel toutiao
     * 2020年8月20日15:04:39 申欣武
     * @param logingRepUser 登录传入参数
     * @param  body 登录传入参数
     * @param ipBlock ip参数
     * @return
     */
    public UserLoginDTO getUserLoginBySwan(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock);

    /**
     * 根据特殊渠道返回登录信息
     * channel 其它渠道
     * 2020年8月20日15:04:39 申欣武
     * @param logingRepUser 登录传入参数
     * @param  body 登录传入参数
     * @param ipBlock ip参数
     * @return
     */
    public UserLoginDTO getUserLoginByOtherChannel(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock);


    /**
     * 根据渠道返回登录信息
     * channel 默认渠道default
     * 2020年8月20日15:04:39 申欣武
     * @param logingRepUser 登录传入参数
     * @param  body 登录传入参数
     * @param ipBlock ip参数
     * @param isOpen4Code 是否给openId设定code值
     * @return
     */
    public UserLoginDTO getUserLoginByDefault(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock,boolean isOpen4Code);

    /**
     * ab分组
     * @param appId 项目id
     * @return
     */
    public String AbTest(User user);

    int getInvitedFriends(String fromUuid, String shareType, String appId);

    ResponseEntity<ApiResponseMessage> invitedFriends(InvitedReqParam invitedReqParam,String appId);

    boolean addUserGem(Long userId, String appId, Integer gem);

    boolean plusUserGem(Long userId, String appId, Integer gem);

    UserGem getUserGem(Long userId, String appId);

    void removeUser(Long userId, String appId);
}
