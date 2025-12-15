package org.thingml.xtext.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(name = "ShowPlotServlet", urlPatterns = "/show-plot")
public class ShowPlotServlet extends HttpServlet {

    private static final String BASE_PLOT_DIR = "/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/GeneratedCode";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userID = request.getParameter("userID");
        String plotFile = request.getParameter("plot");

        // Basic input validation
        if (userID == null || userID.isEmpty() || plotFile == null || plotFile.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing userID or plot file name.");
            return;
        }

        // Prevent directory traversal
        if (!userID.matches("^[a-zA-Z0-9]+$") || plotFile.contains("..") || plotFile.contains("/") || plotFile.contains("\\")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid input.");
            return;
        }

        // Construct the full path to the plot
        Path plotPath = Paths.get(BASE_PLOT_DIR, userID, "python_java", "src", "python-scripts", "plots", plotFile);
        File file = plotPath.toFile();

        if (!file.exists() || !file.isFile()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Plot file not found.");
            return;
        }

        // Set MIME type
        String contentType = getServletContext().getMimeType(file.getName());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        response.setContentType(contentType);
        response.setContentLength((int) file.length());

        // Stream the file
        try (FileInputStream inStream = new FileInputStream(file);
             OutputStream outStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }
    }
}