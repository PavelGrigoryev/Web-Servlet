package ru.clevertec.webservlet.exception.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;

@WebServlet(urlPatterns = "/exception_handler")
public class ExceptionHandlerServlet extends HttpServlet {

    private transient Gson gson;

    @Override
    public void init(ServletConfig config) {
        gson = new Gson();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        Exception exception = (Exception) req.getAttribute(ERROR_EXCEPTION);
        PrintWriter printWriter = resp.getWriter();
        switch (exception.getClass().getSimpleName()) {
            case "NotFoundException" -> {
                resp.setStatus(404);
                printExceptionResponse(exception.getMessage(), printWriter);
            }
            case "JwtException" -> {
                resp.setStatus(401);
                printExceptionResponse(exception.getMessage(), printWriter);
            }
            default -> {
                resp.setStatus(500);
                printExceptionResponse(exception.getMessage(), printWriter);
            }
        }
    }

    private void printExceptionResponse(String message, PrintWriter printWriter) {
        ExceptionResponse response = new ExceptionResponse(message);
        String json = gson.toJson(response);
        printWriter.print(json);
    }

}
