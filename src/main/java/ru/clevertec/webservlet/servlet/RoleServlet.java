package ru.clevertec.webservlet.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.dto.role.RoleResponse;
import ru.clevertec.webservlet.dto.role.RoleSaveRequest;
import ru.clevertec.webservlet.dto.role.RoleUpdateRequest;
import ru.clevertec.webservlet.service.RoleService;
import ru.clevertec.webservlet.service.impl.RoleServiceImpl;

import java.io.IOException;
import java.util.Objects;

@WebServlet(urlPatterns = "/roles")
public class RoleServlet extends HttpServlet {

    private transient RoleService roleService;
    private transient Gson gson;

    @Override
    public void init(ServletConfig config) {
        roleService = new RoleServiceImpl();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (Objects.nonNull(id) && id.matches("\\d+")) {
            RoleResponse response = roleService.findById(Long.valueOf(id));
            sendResponse(resp, response, 200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String role = req.getAttribute("/roles").toString();
        RoleSaveRequest request = gson.fromJson(role, RoleSaveRequest.class);
        RoleResponse response = roleService.save(request);
        sendResponse(resp, response, 201);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (Objects.nonNull(id) && id.matches("\\d+")) {
            String role = req.getAttribute("/roles").toString();
            RoleUpdateRequest request = gson.fromJson(role, RoleUpdateRequest.class);
            RoleResponse response = roleService.updateById(Long.valueOf(id), request);
            sendResponse(resp, response, 200);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (Objects.nonNull(id) && id.matches("\\d+")) {
            DeleteResponse response = roleService.deleteById(Long.valueOf(id));
            sendResponse(resp, response, 200);
        }
    }

    private void sendResponse(HttpServletResponse resp, Object response, int code) throws IOException {
        String json = gson.toJson(response);
        resp.setStatus(code);
        resp.getWriter().print(json);
    }

}
