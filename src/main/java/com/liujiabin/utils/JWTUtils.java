package com.liujiabin.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    /**
     * 1.申明密钥
     * 2.签发算法，和密钥
     * 3.将传入的id存入map
     * 4.设置有效时间
     */
    private static final String jwtToken = "688bolg!@#$$";//申明密钥（可修改）

    public static String createToken(Long userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);   //将userid存入map中
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken)  // 签发算法，和密钥（可修改）
                .setClaims(claims)     // 存入账户id的map
                .setIssuedAt(new Date())    // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000)); // 一天的有效时间（可修改）
        String token = jwtBuilder.compact();
        return token;
    }


    public static Map<String, Object> checkToken(String token){
        /**
         * 传入token返回是否有保存用户id的map
         */
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}