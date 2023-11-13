package ru.clevertec.webservlet.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ru.clevertec.webservlet.model.UserWithRoles;
import ru.clevertec.webservlet.service.JwtService;
import ru.clevertec.webservlet.tables.pojos.Role;
import ru.clevertec.webservlet.util.YamlUtil;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class JwtServiceImpl implements JwtService {

    private final String secretKey;
    private final Long expiration;

    public JwtServiceImpl() {
        Map<String, String> jwtMap = new YamlUtil().getYamlMap().get("jwt");
        secretKey = jwtMap.get("secretKey");
        expiration = Long.valueOf(jwtMap.get("expiration"));
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public List<String> extractRoles(String token) {
        List<?> roles = extractClaim(token, claims -> claims.get("roles", List.class));
        return roles.stream()
                .map(Object::toString)
                .toList();
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserWithRoles user) {
        Map<String, List<String>> extraClaims = new HashMap<>();
        extraClaims.put("roles", user.getRoles().stream()
                .map(Role::getName)
                .toList());
        return generateToken(extraClaims, user);
    }

    @Override
    public String generateToken(Map<String, List<String>> extraClaims, UserWithRoles user) {
        return Jwts.builder()
                .claims()
                .add(extraClaims)
                .subject(user.getNickname())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public Boolean isTokenValid(String token, UserWithRoles user) {
        String username = extractUsername(token);
        return (username.equals(user.getNickname())) && !isTokenExpired(token);
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDateTime.now());
    }

    @Override
    public LocalDateTime extractExpiration(String token) {
        return extractClaim(token, claims -> claims.getExpiration()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
