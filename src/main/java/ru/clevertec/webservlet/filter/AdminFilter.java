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
import ru.clevertec.webservlet.dto.role.RoleResponse;
import ru.clevertec.webservlet.dto.user.UserResponse;
import ru.clevertec.webservlet.exception.NotEnoughRightsException;
import ru.clevertec.webservlet.exception.NotFoundException;
import ru.clevertec.webservlet.exception.SessionInvalidException;
import ru.clevertec.webservlet.filter.model.RequestMethod;
import ru.clevertec.webservlet.service.UserService;
import ru.clevertec.webservlet.service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

@Slf4j
public class AdminFilter implements Filter {

    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) {
        userService = new UserServiceImpl();
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
        Object object = session.getAttribute("loggedUser");
        if (Objects.nonNull(object)) {
            UserResponse loggedUser = (UserResponse) object;
            loggedUser = checkIfLoggedUserWasDeletedAndRolesWasUpdated(session, loggedUser);
            String admin = loggedUser.roles().stream()
                    .map(RoleResponse::name)
                    .filter("ADMIN"::equals)
                    .findAny()
                    .orElseThrow(() -> new NotEnoughRightsException("Only user with role ADMIN has rights to change objects"));
            log.warn("{} {} is here", admin, loggedUser.nickname());
        } else {
            throw new NotEnoughRightsException("Non-logged user have rights to view roles only! " +
                                               "Login as ADMIN to access the ability to make changes");
        }
    }

    private UserResponse checkIfLoggedUserWasDeletedAndRolesWasUpdated(HttpSession session, UserResponse loggedUser) {
        try {
            UserResponse response = userService.findById(loggedUser.id());
            if (!new HashSet<>(loggedUser.roles()).containsAll(response.roles())) {
                session.setAttribute("loggedUser", response);
                loggedUser = (UserResponse) session.getAttribute("loggedUser");
            }
        } catch (NotFoundException e) {
            String sessionId = session.getId();
            session.invalidate();
            throw new SessionInvalidException("Session with ID " + sessionId + " is invalid, " +
                                              "because session user was deleted by user with role ADMIN");
        }
        return loggedUser;
    }

}
