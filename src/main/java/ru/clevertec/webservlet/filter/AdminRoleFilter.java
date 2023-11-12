package ru.clevertec.webservlet.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.webservlet.dto.role.RoleResponse;
import ru.clevertec.webservlet.dto.user.UserResponse;
import ru.clevertec.webservlet.exception.NotEnoughRightsException;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class AdminRoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        if ("POST".equals(req.getMethod()) || "PUT".equals(req.getMethod()) || "DELETE".equals(req.getMethod())) {
            Object object = req.getSession().getAttribute("loggedUser");
            if (Objects.nonNull(object)) {
                UserResponse loggedUser = (UserResponse) object;
                String admin = loggedUser.roles().stream()
                        .map(RoleResponse::name)
                        .filter("ADMIN"::equals)
                        .findAny()
                        .orElseThrow(() -> new NotEnoughRightsException("Only user with role ADMIN has rights to change roles"));
                log.warn("{} {} is here", admin, loggedUser.nickname());
            } else {
                throw new NotEnoughRightsException("Non-logged user have rights to view roles only! Login as ADMIN to change them");
            }
        }
        chain.doFilter(request, response);
    }

}
