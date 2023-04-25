package com.ems.utils;

import com.ems.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    //准备两常量
    private static final String CLAIM_KEY_ID = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    //@Value可以从配置目录里面拿静态值
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expire}")
    private Long expiration;


    /**
     * 第一个工具类功能
     * 根据用户信息生成token
     * 用户信息根据Spring security框架中的UserDetail中拿
     */
    public String generateToken(String userId, Map<String, Object> claims) {
        claims.put(CLAIM_KEY_ID, userId);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 根据荷载生成token
     * 主要是通过Jwts把荷载、失效时间、以及密钥加密生成token
     *
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)//把荷载存储到里面
                .setExpiration(generateExpirationDate())//设置失效时间
                .signWith(SignatureAlgorithm.ES512, secret) //签名
                .compact();
    }


    /**
     * 生成token失效时间
     *
     * @return
     */
    private Date generateExpirationDate() {
        //失效时间是当前系统的时间+我们在配置文件里定义的时间
        return new Date(System.currentTimeMillis() + expiration);
    }

    /**
     * 根据token获取用户ID
     *
     * @param token
     * @return
     */
    public String getUserIdFormToken(String token) {
        String userId;
        try {
            Claims claims = getClaimsFormToken(token);
            userId = claims.get(CLAIM_KEY_ID,String.class);
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    /**
     * 从token中获取荷载
     * 获取荷载是通过jwts，然后传入参数，分别是secret、和token
     *
     * @param token
     * @return
     */
    private Claims getClaimsFormToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

//    /**
//     * 验证token是否有效
//     *
//     * @param token
//     * @param
//     * @return
//     */
//    public boolean validateToken(String token) {
//        String userId = getUserIdFormToken(token);
//        String redisToken = (String) redisUtil.get(userId);
//        if (!redisToken.equals(token)){
//            return false;
//        }
//        return true;
//    }

    /**
     * 判断token是否已经失效
     *
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        //先获取之前设置的token的失效时间
        Date expireDate = getExpiredDateFormToken(token);
        return expireDate.before(new Date()); //判断下，当前时间是都已经在expireDate之后
    }

    /**
     * 根据token获取失效时间
     * 也是先从token中获取荷载
     * 然后从荷载中拿到到设置的失效时间
     *
     * @param token
     * @return
     */
    private Date getExpiredDateFormToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断toke是否可以被刷新
     * 如果过期则可以刷新
     *
     * @param token
     * @return
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFormToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}