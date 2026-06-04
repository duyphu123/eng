package com.smartviet.base.salary.security;

import com.smartviet.base.salary.common.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JWTProvider {

    private final Environment env;

    @Autowired
    public JWTProvider(Environment env) {
        this.env = env;
    }

    public String buildToken(Map<String, Object> extraClaims, Long expiration) {
        return Jwts
                .builder()
                .id(UUID.randomUUID().toString())
                .claims(extraClaims)
                .subject(extraClaims.get(Constants.Security.Claims.USERNAME).toString())
                .notBefore(new Date(System.currentTimeMillis()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String subject = extractSubject(token);
        return Objects.equals(subject, userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractSubject(String token) {
        return extractClaim(token, io.jsonwebtoken.Claims::getSubject);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, io.jsonwebtoken.Claims::getExpiration);
        return Objects.nonNull(expiration) && expiration.before(new Date());
    }

    private SecretKey getSignInKey() {
        String secretKey = env.getProperty("security.jwt.secret-key");
        assert secretKey != null;
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
