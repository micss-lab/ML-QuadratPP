package org.thingml.xtext.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

@WebServlet(name = "GetForecastingPlotFilesServlet", urlPatterns = "/get-forecasting-plot-files")
public class GetForecastingPlotFilesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("🔍 [Servlet] Received request for forecasting plot files.");

        // Retrieve the userID from the request to locate the specific directory
        String userID = request.getParameter("userID");
        System.out.println("🆔 [Servlet] userID parameter: " + userID);
        if (userID == null || userID.isEmpty()) {
            System.out.println("❌ [Servlet] Missing userID. Returning empty list.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("[]");
            return;
        }

        // Define the base directory dynamically based on the deployment structure
        String baseDir = getServletContext().getRealPath("/");
        System.out.println("📁 [Servlet] Servlet base directory: " + baseDir);

        String forecastingPlotDirectoryPath = "/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/GeneratedCode/"
                + userID + "/python_java/src/python-scripts/plots/";
        System.out.println("📂 [Servlet] Looking in directory: " + forecastingPlotDirectoryPath);

        // Check if the directory exists
        File plotDirectory = new File(forecastingPlotDirectoryPath);
        if (!plotDirectory.exists() || !plotDirectory.isDirectory()) {
            System.out.println("⚠️ [Servlet] Plot directory not found or not a directory.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("[]");
            return;
        }

        // Retrieve .png files from the directory
        File[] files = plotDirectory.listFiles((dir, name) -> name.endsWith(".png"));
        if (files == null || files.length == 0) {
            System.out.println("ℹ️ [Servlet] No .png files found in the directory.");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("[]");
            return;
        }

        // Collect file names
        List<String> fileNames = new ArrayList<>();
        for (File file : files) {
            System.out.println("✅ [Servlet] Found plot file: " + file.getName());
            fileNames.add(file.getName());
        }

        // Convert the list of file names to JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(fileNames);

        // Set response type and write JSON response
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        System.out.println("📤 [Servlet] JSON response sent: " + jsonResponse);
    }
}
