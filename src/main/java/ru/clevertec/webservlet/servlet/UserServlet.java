package ru.clevertec.webservlet.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.dto.user.UserResponse;
import ru.clevertec.webservlet.dto.user.UserSaveRequest;
import ru.clevertec.webservlet.dto.user.UserUpdateRequest;
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
            UserResponse response = userService.findById(Long.valueOf(id));
            sendResponse(resp, response, 200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String user = req.getAttribute("/users").toString();
        UserSaveRequest request = gson.fromJson(user, UserSaveRequest.class);
        UserResponse response = userService.save(request);
        sendResponse(resp, response, 201);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (Objects.nonNull(id) && id.matches("\\d+")) {
            String user = req.getAttribute("/users").toString();
            UserUpdateRequest request = gson.fromJson(user, UserUpdateRequest.class);
            UserResponse response = userService.updateById(Long.valueOf(id), request);
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

}
