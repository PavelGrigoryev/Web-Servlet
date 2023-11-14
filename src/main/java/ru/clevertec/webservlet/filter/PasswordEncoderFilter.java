package ru.clevertec.webservlet.filter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.stream.Collectors;

import static ru.clevertec.webservlet.filter.model.RequestMethod.POST;
import static ru.clevertec.webservlet.filter.model.RequestMethod.PUT;

public class PasswordEncoderFilter implements Filter {

    private Gson gson;

    @Override
    public void init(FilterConfig filterConfig) {
        gson = new Gson();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (POST.name().equals(req.getMethod()) || PUT.name().equals(req.getMethod())) {
            String requestUri = req.getRequestURI();
            String body = req.getReader()
                    .lines()
                    .collect(Collectors.joining());
            if ("/users".equals(requestUri) || "/auth".equals(requestUri)) {
                JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
                String password = jsonObject.get("password").getAsString();
                jsonObject.addProperty("password", encodePassword(password));
                req.setAttribute(requestUri, gson.toJson(jsonObject));
            } else {
                req.setAttribute(requestUri, body);
            }
        }
        chain.doFilter(request, response);
    }

    @SneakyThrows
    private String encodePassword(String password) {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] hash = messageDigest.digest(bytes);
        return Base64.getEncoder().encodeToString(hash);
    }

}
