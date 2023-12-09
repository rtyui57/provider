package com.ramon.provider.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    protected final static String SECRET = "secret";

    public static String generateToken(UserAuth userDetails) {
        return Jwts.builder().
                setClaims(new HashMap<>(Map.of("auths", userDetails.getAuthorities(), "role", userDetails.getRole()
                        , "email", userDetails.getEmail()))).
                setSubject(userDetails.getUsername()).
                setIssuedAt(new Date()).
                setExpiration(new Date(System.currentTimeMillis() + 10000000000L)).
                signWith(SignatureAlgorithm.HS512, SECRET).
                compact();
    }

    public static String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public static Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }
}
