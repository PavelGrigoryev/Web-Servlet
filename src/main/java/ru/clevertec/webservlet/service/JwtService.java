package ru.clevertec.webservlet.service;

import io.jsonwebtoken.Claims;
import ru.clevertec.webservlet.model.UserWithRoles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);

    List<String> extractRoles(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserWithRoles user);

    String generateToken(Map<String, List<String>> extraClaims, UserWithRoles user);

    Boolean isTokenValid(String token, UserWithRoles user);

    Boolean isTokenExpired(String token);

    LocalDateTime extractExpiration(String token);

}
