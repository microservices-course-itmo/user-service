package com.wine.to.up.user.service.security;

import com.wine.to.up.commonlib.annotations.InjectEventLogger;
import com.wine.to.up.commonlib.logging.EventLogger;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.logging.UserServiceNotableEvents;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    @Value("${jwt.token.secret}")
    private String secret;

    private String secretEncoded;

    @Value("${jwt.token.expired.access}")
    private long accessValidityInMilliseconds;

    @Value("${jwt.token.expired.refresh}")
    private long refreshValidityInMilliseconds;

    @InjectEventLogger
    private EventLogger eventLogger;

    @PostConstruct
    protected void init() {
        secretEncoded = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(UserDto userDto, boolean isAccessToken) {

        Claims claims = Jwts.claims();
        claims.put("phone_number", userDto.getPhoneNumber());
        claims.put("role", userDto.getRole().getName());
        claims.put("id", userDto.getId().toString());

        Date now = new Date();

        long validityTimeMillis;
        long currentTimeMillis = Instant.now().toEpochMilli();

        if (isAccessToken) {
            validityTimeMillis = currentTimeMillis + accessValidityInMilliseconds;
            claims.put("type", "ACCESS_TOKEN");
        } else {
            validityTimeMillis = currentTimeMillis + refreshValidityInMilliseconds;
            claims.put("type", "REFRESH_TOKEN");
        }

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(validityTimeMillis))
            .signWith(SignatureAlgorithm.HS256, secretEncoded)
            .compact();
    }

    public String getPhoneNumber(String token) {
        return (String) Jwts.parser()
            .setSigningKey(secretEncoded)
            .parseClaimsJws(token)
            .getBody()
            .get("phone_number");
    }

    public String getTokenType(String token) {
        return (String) Jwts.parser()
            .setSigningKey(secretEncoded)
            .parseClaimsJws(token)
            .getBody()
            .get("type");
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
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretEncoded).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
        } catch (Exception ex) {
            eventLogger.debug(UserServiceNotableEvents.W_AUTH_FAILURE, ex.getMessage());
            return false;
        }

        return true;
    }
}
