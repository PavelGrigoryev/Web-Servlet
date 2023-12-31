package ru.clevertec.webservlet.filter.builder;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.clevertec.webservlet.filter.model.RequestMethod.POST;
import static ru.clevertec.webservlet.filter.model.RequestMethod.PUT;

public class RequestLogBuilder {

    private final StringBuilder log;

    private RequestLogBuilder() {
        log = new StringBuilder();
    }

    public static RequestLogBuilder builder() {
        return new RequestLogBuilder();
    }

    public RequestLogBuilder method(String method) {
        return append("\n Request method: " + method);
    }

    public RequestLogBuilder url(String url) {
        return append("\n Request URL: " + url);
    }

    public RequestLogBuilder headers(HttpServletRequest req) {
        return append("\n Request headers: " + Collections.list(req.getHeaderNames())
                .stream()
                .map(headerName -> "\n  " + headerName + ": " + req.getHeader(headerName))
                .collect(Collectors.joining()));
    }

    public RequestLogBuilder params(Map<String, String[]> parameterMap) {
        return parameterMap.isEmpty()
                ? append("")
                : append("\n Request parameters:" + parameterMap.entrySet()
                .stream()
                .map(entry -> "\n  " + entry.getKey() + ": " + String.join("", entry.getValue()))
                .collect(Collectors.joining()));
    }

    public RequestLogBuilder body(HttpServletRequest req, String attributeName) {
        if (POST.name().equals(req.getMethod()) || PUT.name().equals(req.getMethod())) {
            return append("\n Request body:\n  " + req.getAttribute(attributeName));
        } else {
            return append("");
        }
    }

    private RequestLogBuilder append(String command) {
        log.append(command);
        return this;
    }

    public String build() {
        return log.toString();
    }

}
