package ru.clevertec.webservlet.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.dto.UserWithRoleIds;
import ru.clevertec.webservlet.dto.UserWithRoles;
import ru.clevertec.webservlet.service.UserService;
import ru.clevertec.webservlet.service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.Objects;

@WebServlet(urlPatterns = "/users")
public class UserServlet extends HttpServlet {

    private transient UserService userService;
    private transient Gson gson;

    @Override
    public void init(ServletConfig config) {
        userService = new UserServiceImpl();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (Objects.nonNull(id) && id.matches("\\d+")) {
            UserWithRoles response = userService.findById(Long.valueOf(id));
            sendResponse(resp, response, 200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserWithRoleIds request = getUserFromRequest(req);
        UserWithRoles response = userService.save(request);
        sendResponse(resp, response, 201);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (Objects.nonNull(id) && id.matches("\\d+")) {
            UserWithRoleIds request = getUserFromRequest(req);
            UserWithRoles response = userService.updateById(Long.valueOf(id), request);
            sendResponse(resp, response, 200);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (Objects.nonNull(id) && id.matches("\\d+")) {
            DeleteResponse response = userService.deleteById(Long.valueOf(id));
            sendResponse(resp, response, 200);
        }
    }

    private void sendResponse(HttpServletResponse resp, Object response, int code) throws IOException {
        String json = gson.toJson(response);
        resp.setStatus(code);
        resp.getWriter().print(json);
    }

    private UserWithRoleIds getUserFromRequest(HttpServletRequest req) {
        String user = req.getAttribute("users").toString();
        return gson.fromJson(user, UserWithRoleIds.class);
    }

}
