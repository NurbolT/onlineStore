package com.example.onlinestore.util;

import com.example.onlinestore.auth.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Component
@Log
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access_token-expiration}")
    private int accessTokenExpiration;

    private String createToken(User user, int duration){
        Date currentDate = new Date();
        user.setPassword(null);

        Map claims = new HashMap<String,String[]>();

        claims.put("user",user);
        claims.put(Claims.SUBJECT, user.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + duration))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

    }

    public String createAccessToken(User user){
        return createToken(user, accessTokenExpiration);
    }

    public boolean validate(String jwt){
        try {
            Jwts
                    .parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException e) {
            log.log(Level.SEVERE, "Invalid JWT token: ", jwt);
        } catch (SignatureException e) {
            log.log(Level.SEVERE, "JWT token signature is not valid: ", jwt);
        } catch (UnsupportedJwtException e) {
            log.log(Level.SEVERE, "JWT token is unsupported: ", jwt);
        } catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, "JWT claims string is empty: ", jwt);
        }

        return false;
    }

    public User getUser(String jwt){
        Map map = (Map) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().get("user");
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(map, User.class);
        return user;
    }
}
