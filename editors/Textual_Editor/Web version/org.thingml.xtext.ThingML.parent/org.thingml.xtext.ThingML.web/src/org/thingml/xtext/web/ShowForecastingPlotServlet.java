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

@WebServlet(name = "ShowForecastingPlotServlet", urlPatterns = "/show-forecasting-plot")
public class ShowForecastingPlotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get userID and plot file name from request
        String userID = request.getParameter("userID");
        String plotFile = request.getParameter("plots");

        // Validate inputs
        if (userID == null || userID.isEmpty() || plotFile == null || plotFile.isEmpty() || !isValidUserID(userID)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid or missing userID or plot file name.");
            return;
        }

        if (!plotFile.endsWith(".png")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid file type. Only PNG images are supported.");
            return;
        }

        // ✅ Update to match actual deployment directory
        Path plotPath = Paths.get(
            "/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/GeneratedCode",
            userID,
            "python_java",
            "src",
            "python-scripts",
            "plots",
            plotFile
        );

        System.out.println("📍 Serving plot from: " + plotPath.toAbsolutePath());

        File file = plotPath.toFile();
        if (!file.exists() || !file.isFile()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Plot file not found.");
            return;
        }

        response.setContentType("image/png");
        response.setContentLength((int) file.length());

        try (FileInputStream inStream = new FileInputStream(file);
             OutputStream outStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error while serving the plot file.");
        }
    }

    private boolean isValidUserID(String userID) {
        return userID.matches("^[a-zA-Z0-9]+$");
    }
}