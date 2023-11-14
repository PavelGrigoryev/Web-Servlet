package ru.clevertec.webservlet.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.webservlet.filter.model.RequestMethod;
import ru.clevertec.webservlet.model.UserWithRoles;
import ru.clevertec.webservlet.service.JwtService;
import ru.clevertec.webservlet.service.UserService;
import ru.clevertec.webservlet.service.impl.JwtServiceImpl;
import ru.clevertec.webservlet.service.impl.UserServiceImpl;
import ru.clevertec.webservlet.tables.pojos.Role;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Slf4j
public class JwtFilter implements Filter {

    private UserService userService;
    private JwtService jwtService;

    @Override
    public void init(FilterConfig filterConfig) {
        userService = new UserServiceImpl();
        jwtService = new JwtServiceImpl();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        switch (RequestMethod.valueOf(req.getMethod())) {
            case POST, PUT, DELETE -> checkTokenValidation(req);
            case GET -> {
                if ("/users".equals(req.getRequestURI())) {
                    checkTokenValidation(req);
                }
            }
            default -> chain.doFilter(request, response);
        }
        chain.doFilter(request, response);
    }

    private void checkTokenValidation(HttpServletRequest req) {
        String authorization = req.getHeader("Authorization");
        if (Objects.nonNull(authorization)) {
            String jwt = authorization.substring(7);
            String nickname = jwtService.extractUsername(jwt);
            UserWithRoles user = userService.findByNickname(nickname);
            if (Boolean.TRUE.equals(jwtService.isTokenValid(jwt, user))) {
                List<String> roles = jwtService.extractRoles(jwt);
                checkIfRolesWasUpdated(roles, user);
                checkForAdminRole(roles, jwt);
            } else {
                throw new JwtException("Jwt " + jwt + " is invalid, please login again");
            }
        } else {
            throw new JwtException("Jwt in header 'Authorization' is required");
        }
    }

    private void checkIfRolesWasUpdated(List<String> roles, UserWithRoles user) {
        if (roles.size() != user.getRoles().size()) {
            generateNewJwt(user);
        }
        if (!new HashSet<>(roles).containsAll(user.getRoles()
                .stream()
                .map(Role::getName)
                .toList())) {
            generateNewJwt(user);
        }
    }

    private void checkForAdminRole(List<String> roles, String jwt) {
        String admin = roles.stream()
                .filter("ADMIN"::equals)
                .findAny()
                .orElseThrow(() -> new JwtException("Only user with role ADMIN has rights to change objects"));
        log.warn("{} {} is here", admin, jwtService.extractUsername(jwt));
    }

    private void generateNewJwt(UserWithRoles user) {
        String newJwt = jwtService.generateToken(user);
        throw new JwtException("ADMIN change your roles, your new jwt:  " + newJwt
                               + "  put it in header 'Authorization'");
    }

}
