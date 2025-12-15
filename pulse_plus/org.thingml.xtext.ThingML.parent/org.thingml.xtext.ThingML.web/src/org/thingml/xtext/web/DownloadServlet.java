package org.thingml.xtext.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet(name = "DownloadServlet", urlPatterns = "/download-code")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve userID from session
        String userID = (String) request.getSession().getAttribute("userID");
        if (userID == null || userID.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: User not logged in.");
            return;
        }

        // Use a default filename based on the userID
        String requestedFile = "Generated_" + userID + ".zip";

        // Construct the absolute file path using the known directory structure
     // Construct the absolute file path using the new directory structure
        String filePath = Paths.get("/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/GeneratedCode", userID, requestedFile).toString();

        // Debug: Print the file path to verify it's correct
        System.out.println("Attempting to download file: " + filePath);

        // Check if the file exists
        File file = new File(filePath);
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Requested file not found.");
            return;
        }

        // Set response headers for file download
        response.setContentType("application/zip");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + requestedFile + "\"");

        // Stream the file to the client
        try (OutputStream out = response.getOutputStream()) {
            Files.copy(file.toPath(), out);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error while streaming the file.");
        }
    }
}