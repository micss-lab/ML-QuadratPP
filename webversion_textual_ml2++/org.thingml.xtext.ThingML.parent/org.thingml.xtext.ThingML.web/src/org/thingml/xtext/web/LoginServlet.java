package org.thingml.xtext.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final Map<String, String> USERS = new HashMap<>();

    static {
        for (int i = 1; i <= 30; i++) {
            USERS.put("user" + i, "password" + i);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> requestBody = mapper.readValue(request.getInputStream(), Map.class);

        String username = requestBody.get("username");
        String password = requestBody.get("password");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (username != null && password != null && password.equals(USERS.get(username))) {
            request.getSession().setAttribute("userID", username);
            response.getWriter().write(mapper.writeValueAsString(Map.of(
                "success", true,
                "message", "Login successful."
            )));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(mapper.writeValueAsString(Map.of(
                "success", false,
                "message", "Invalid username or password."
            )));
        }
    }
}
