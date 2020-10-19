package com.wine.to.up.user.service.security;

import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired.access}")
    private long accessValidityInMilliseconds;

    @Value("${jwt.token.expired.refresh}")
    private long refreshValidityInMilliseconds;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(UserDto userDto, boolean isAccessToken) {

        Claims claims = Jwts.claims();
        claims.put("phone_number", userDto.getPhoneNumber());
        claims.put("role", userDto.getRole().getName());
        claims.put("id", userDto.getId().toString());

        Date now = new Date();
        Date validity;

        if(isAccessToken){
            validity = new Date(now.getTime() + accessValidityInMilliseconds);
            claims.put("type", "ACCESS_TOKEN");
        } else{
            validity = new Date(now.getTime() + refreshValidityInMilliseconds);
            claims.put("type", "REFRESH_TOKEN");
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getPhoneNumber(String token) {
        return (String) Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("phone_number");
    }

    public String getTokenType(String token) {
        return (String) Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("type");
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
        } catch (JwtAuthenticationException | IllegalArgumentException | MalformedJwtException | SignatureException e) {
            return false;
        }

        return true;
    }
}
