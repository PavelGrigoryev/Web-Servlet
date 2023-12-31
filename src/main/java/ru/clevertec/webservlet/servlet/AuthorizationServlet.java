package ru.clevertec.webservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.webservlet.dto.user.AuthorizationResponse;
import ru.clevertec.webservlet.dto.user.LoginRequest;
import ru.clevertec.webservlet.dto.user.UserSaveRequest;
import ru.clevertec.webservlet.service.UserService;
import ru.clevertec.webservlet.service.impl.UserServiceImpl;

import java.io.IOException;

@WebServlet(urlPatterns = "/auth")
public class AuthorizationServlet extends HttpServlet {

    private transient UserService userService;
    private transient Gson gson;

    @Override
    public void init(ServletConfig config) {
        userService = new UserServiceImpl();
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String loggedUser = req.getAttribute("/auth").toString();
        JsonObject jsonObject = gson.fromJson(loggedUser, JsonObject.class);
        if (jsonObject.has("role_ids")) {
            UserSaveRequest request = gson.fromJson(jsonObject, UserSaveRequest.class);
            AuthorizationResponse response = userService.register(request);
            sendResponse(resp, response, 201);
        } else {
            LoginRequest request = gson.fromJson(jsonObject, LoginRequest.class);
            AuthorizationResponse response = userService.findByNicknameAndPassword(request);
            sendResponse(resp, response, 200);
        }
    }

    private void sendResponse(HttpServletResponse resp, Object response, int code) throws IOException {
        String json = gson.toJson(response);
        resp.setStatus(code);
        resp.getWriter().print(json);
    }

}
