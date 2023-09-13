package com.wre.game.api.util;

import com.wre.game.api.constants.ApiConstants;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.constants.UserStatus;
import com.wre.game.api.data.SessionDomain;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.exception.AuthException;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class TokenUtil {

    private static final String KEY_USER_ID = "userId";
    // X.Y.Z dot(.) delimiter
    // X means UserStatus.tokenCode();
    // Y means Channel.tokenCode();
    // Z means user.uuid
    private static final String KEY_CODE = "code";

    private static final String CODE_NAME_USERSTATUS = "user_status";
    private static final String CODE_NAME_CHANNEL = "channel";
    private static final String CODE_NAME_UUID = "uuid";

    public static String generateToken(String tokenSecret, User user, Date expireTime, String channel) {

        HashMap<String, Object> sessionMap = new HashMap<String, Object>();
        sessionMap.put(KEY_USER_ID, String.valueOf(user.getUserId()));
        sessionMap.put(KEY_CODE, encodeCode(user, channel));

        return Jwts.builder().setClaims(sessionMap).setExpiration(expireTime).signWith(SignatureAlgorithm.HS256, tokenSecret).compact();
    }

    /**
     * @param secret
     * @param token
     * @return
     * @throws ExpiredJwtException
     * @throws UnsupportedJwtException
     * @throws MalformedJwtException
     * @throws SignatureException
     * @throws IllegalArgumentException
     */
    public static SessionDomain parseToken(String secret, String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
            SignatureException, IllegalArgumentException {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        SessionDomain session = new SessionDomain();
        try {
            session.setUserId(String.valueOf(claims.get(KEY_USER_ID)));

            Map<String, Object> codeMap = decodeCode(String.valueOf(claims.get(KEY_CODE)));
            session.setBlock(String.valueOf(codeMap.get(CODE_NAME_USERSTATUS)));
            session.setChannel(String.valueOf(codeMap.get(CODE_NAME_CHANNEL)));
            session.setUuid(String.valueOf(codeMap.get(CODE_NAME_UUID)));
        } catch (Exception e) {
//            e.printStackTrace();
            throw new UnsupportedJwtException("Wrong format claim message", e);
        }

        return session;
    }

    private static String encodeCode(User user, String channel) {

        StringBuilder sb = new StringBuilder();

        if (UserStatus.ACTIVE.value().equals(user.getBlock()))
            sb.append(UserStatus.ACTIVE.value());
        else if (UserStatus.BLOCK.value().equals(user.getBlock()))
            sb.append(UserStatus.BLOCK.value());
        else
            throw new RuntimeException("Undefined UserStatus. Requested Status=" + user.getBlock());

        sb.append(ApiConstants.DELIMITER_DOT);
        if (channel == null)
            throw new RuntimeException("Undefined Channel");
        sb.append(channel);

        sb.append(ApiConstants.DELIMITER_DOT);
        sb.append(user.getUuid());
        return sb.toString();
    }

    private static Map<String, Object> decodeCode(String code) {

        String[] elements = StringUtils.split(code, ApiConstants.DELIMITER_DOT);

        if (elements.length < 2)
            throw new MalformedJwtException("Malformed code size. size=" + elements.length);

        Integer userStatus = null;
        if (String.valueOf(UserStatus.ACTIVE.value()).equals(elements[0]))
            userStatus = UserStatus.ACTIVE.value();
        else if (String.valueOf(UserStatus.BLOCK.value()).equals(elements[0]))
            userStatus = UserStatus.BLOCK.value();
        else
            throw new AuthException(RtCode.RT_TOKEN_INVALID);

        Map<String, Object> rt = new HashMap<String, Object>();
        rt.put(CODE_NAME_USERSTATUS, userStatus);
        rt.put(CODE_NAME_CHANNEL, elements[1]);
        if (elements.length > 2) {
            rt.put(CODE_NAME_UUID, elements[2]);
        }

        return rt;
    }

    public static String generateUuid(){
       return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}