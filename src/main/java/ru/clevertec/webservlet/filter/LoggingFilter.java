package ru.clevertec.webservlet.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.webservlet.filter.builder.RequestLogBuilder;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestUri = req.getRequestURI().substring(1);
        String logMessage = RequestLogBuilder.builder()
                .method(req.getMethod())
                .url(req.getRequestURL().toString())
                .headers(req)
                .params(req.getParameterMap())
                .body(req, requestUri)
                .build();
        log.info(logMessage);
        chain.doFilter(request, response);
    }

}
