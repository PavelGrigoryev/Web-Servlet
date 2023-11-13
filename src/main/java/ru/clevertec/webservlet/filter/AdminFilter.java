package ru.clevertec.webservlet.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.webservlet.exception.NotEnoughRightsException;
import ru.clevertec.webservlet.exception.NotFoundException;
import ru.clevertec.webservlet.exception.SessionInvalidException;
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
public class AdminFilter implements Filter {

    private UserService userService;
    private JwtService jwtService;

    @Override
    public void init(FilterConfig filterConfig) {
        userService = new UserServiceImpl();
        jwtService = new JwtServiceImpl();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        switch (RequestMethod.valueOf(req.getMethod())) {
            case POST, PUT, DELETE -> checkForAdminRole(req);
            case GET -> {
                if ("/users".equals(req.getRequestURI())) {
                    checkForAdminRole(req);
                }
            }
            default -> chain.doFilter(request, response);
        }
        chain.doFilter(request, response);
    }

    private void checkForAdminRole(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Object object = session.getAttribute("jwt");
        if (Objects.nonNull(object)) {
            String jwt = object.toString();
            jwt = checkIfUserWasDeletedAndRolesWasUpdated(session, jwt);
            String admin = jwtService.extractRoles(jwt).stream()
                    .filter("ADMIN"::equals)
                    .findAny()
                    .orElseThrow(() -> new NotEnoughRightsException("Only user with role ADMIN has rights to change objects"));
            log.warn("{} {} is here", admin, jwtService.extractUsername(jwt));
        } else {
            throw new NotEnoughRightsException("Non-logged user have rights to view roles only! " +
                                               "Login as ADMIN to access the ability to make changes");
        }
    }

    private String checkIfUserWasDeletedAndRolesWasUpdated(HttpSession session, String jwt) {
        try {
            String nickname = jwtService.extractUsername(jwt);
            List<String> roles = jwtService.extractRoles(jwt);
            UserWithRoles user = userService.findByNickname(nickname);
            if (!new HashSet<>(roles).containsAll(user.getRoles()
                    .stream()
                    .map(Role::getName)
                    .toList())) {
                jwt = jwtService.generateToken(user);
                session.setAttribute("jwt", jwt);
            }
        } catch (NotFoundException e) {
            String sessionId = session.getId();
            session.invalidate();
            throw new SessionInvalidException("Session with ID " + sessionId + " is invalid, " +
                                              "because session user was deleted by user with role ADMIN");
        }
        return jwt;
    }

}
