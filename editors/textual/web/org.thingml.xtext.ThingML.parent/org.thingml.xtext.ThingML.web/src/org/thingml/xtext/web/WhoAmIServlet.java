package org.thingml.xtext.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name="WhoAmIServlet", urlPatterns="/whoami")
public class WhoAmIServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String userID = (String) req.getSession().getAttribute("userID");
        resp.setContentType("application/json");
        resp.getWriter().write(
            "{\"userID\": \"" + (userID != null ? userID : "") + "\"}"
        );
    }
}
