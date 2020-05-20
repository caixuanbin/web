package cn.sessiontech.xcx.utils;

import cn.sessiontech.xcx.constant.GlobalConstants;
import cn.sessiontech.xcx.dto.weixin.TokenDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.lang.Strings;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author xbai
 * @classname JwtTokenUtils
 * @description jwt工具类
 * @date 2019/6/17 21:30
 */
public class JwtTokenUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static final char SEPARATOR_CHAR = '.';
    /**
     * 生成token
     * @param map 存放的信息体
     * @param username 登录用户名
     * @return
     */
    public static String createToken(Map<String,Object> map,String username){
        String token = Jwts.builder()
                .setSubject(username)
                .setClaims(map)
                .setId(UUID.randomUUID().toString())
                // 设置过期时间
                .setExpiration(new Date(System.currentTimeMillis()+ GlobalConstants.EXPIRE_IN))
                //采用什么算法是可以自己选择的，不一定非要采用HS512
                .signWith(SignatureAlgorithm.HS512, GlobalConstants.SIGNING_KEY)
                .compact();
        return token;
    }



    /**
     * 构建token实体类返回
     * @param map 存储在token里面的信息
     * @param username 登录名
     * @return
     */
    public static TokenDTO createTokenDTO(Map<String,Object> map, String username){
        String token = createToken(map,username);
        return new TokenDTO(token,System.currentTimeMillis() + GlobalConstants.EXPIRE_IN);
    }

    /**
     * 解析token
     * @param jwt token
     * @return 返回Claims
     */
    public static Map<String,Object> analysisToken(String jwt){
        String payload = "";
        StringBuilder sb = new StringBuilder(128);
        int delimiterCount = 0;
        for (char c : jwt.toCharArray()) {
            if (c == SEPARATOR_CHAR) {
                CharSequence tokenSeq = Strings.clean(sb);
                String token = tokenSeq!=null?tokenSeq.toString():null;
                    //header
                if (delimiterCount == 0) {
                    //payload
                } else if (delimiterCount == 1) {
                    payload = token;
                    //signature
                }else{

                }
                delimiterCount++;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        payload = TextCodec.BASE64URL.decodeToString(payload);
        Map<String,Object> claimsMap = readValue(payload);
        //Claims  claims = new DefaultClaims(claimsMap);
        return claimsMap;
    }

    /**
     * 将json 字符串放入map
     * @param json 字符串
     * @return
     */
    protected static Map<String, Object> readValue(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + json, e);
        }
    }

    public static void main(String[] args) {
//        Map<String,Object> claims = analysisToken("eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NjA4MTczNzcsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn0seyJhdXRob3JpdHkiOiJBVVRIX1dSSVRFIn1dLCJ1c2VybmFtZSI6InhiY2FpIn0.-pgi-H68VyS6H4yIcGWVdyWKFCZGls5wHnkya-am69gMOndpQNKyFZPBXfkDTqNHxmR0mmNK88MQgvqINw_j6A");
//        Object st = claims.get("username");
//        System.out.println(st);
        Map<String,Object> claims = new HashMap<>();
        claims.put("username","xbcai");
        System.out.println(createToken(claims,"zs"));
    }


}
