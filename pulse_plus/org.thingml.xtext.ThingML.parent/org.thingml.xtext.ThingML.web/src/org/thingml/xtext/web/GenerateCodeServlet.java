package org.thingml.xtext.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@WebServlet(name = "GenerateCodeServlet", urlPatterns = "/generate-code")
public class GenerateCodeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method not supported for SSE.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            writer.write("data: Starting code generation...\n\n");
            writer.flush();

            String userID = (String) request.getSession().getAttribute("userID");
            if (userID == null || userID.isEmpty() || !isValidUserID(userID)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("data: Invalid or missing user ID.\n\n");
                writer.write("data: DONE\n\n");
                writer.flush();
                return;
            }

            String modelsDir = "/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/models";
            String userModelFile = Paths.get(modelsDir, userID, userID + "_model.thingml").toString();
            String userOutputDir = Paths.get("/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/GeneratedCode", userID).toString();

            File modelFileObj = new File(userModelFile);
            if (!modelFileObj.exists()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                writer.write("data: Model file for user " + userID + " not found.\n\n");
                writer.write("data: DONE\n\n");
                writer.flush();
                return;
            }

            new File(userOutputDir).mkdirs();

            // حذف zip و فولدر قدیمی
            String zipFilePath = userOutputDir + File.separator + "Generated_" + userID + ".zip";
            File previousZip = new File(zipFilePath);
            if (previousZip.exists()) {
                previousZip.delete();
            }

            File oldGeneratedFolder = new File(userOutputDir + File.separator + "python_java");
            if (oldGeneratedFolder.exists()) {
                deleteRecursive(oldGeneratedFolder);
            }

            String jarPath = "/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/jarfile/mlquadrat.compilers.registry-2.0.0-SNAPSHOT-jar-with-dependencies.jar";
            String command = String.join(" ",
                "java",
                "--add-opens", "java.base/java.lang=ALL-UNNAMED",
                "-jar", jarPath,
                "-c", "auto",
                "-s", userModelFile,
                "-o", userOutputDir
            );

            ProcessBuilder builder = new ProcessBuilder("sh", "-c", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            captureOutput(process, writer, "Code generation timed out.");

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                writer.write("data: Zipping generated code...\n\n");
                writer.flush();

                zipDirectory(userOutputDir, zipFilePath);

                writer.write("data: Code generation and zipping successful for user " + userID + ".\n\n");
                writer.write("data: DONE\n\n");
                writer.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                writer.write("data: Code generation failed with exit code " + exitCode + " for user " + userID + ".\n\n");
                writer.write("data: DONE\n\n");
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter writer = response.getWriter()) {
                writer.write("data: Failed to generate code: " + e.getMessage() + "\n\n");
                writer.write("data: DONE\n\n");
                writer.flush();
            }
        }
    }

    private void captureOutput(Process process, PrintWriter writer, String timeoutMessage)
            throws IOException, InterruptedException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write("data: " + line + "\n\n");
                writer.flush();
            }
        }
        if (!process.waitFor(5, TimeUnit.MINUTES)) {
            process.destroy();
            writer.write("data: " + timeoutMessage + "\n\n");
            writer.write("data: DONE\n\n");
            writer.flush();
        }
    }

    private void zipDirectory(String sourceDirPath, String zipFilePath) throws IOException {
        Path zipFile = Paths.get(zipFilePath);

        // حذف فایل ZIP قبلی در صورت وجود
        if (Files.exists(zipFile)) {
            Files.delete(zipFile);
        }

        Files.createFile(zipFile);

        Path sourcePath = Paths.get(sourceDirPath);
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            Files.walk(sourcePath)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                    try {
                        zs.putNextEntry(zipEntry);
                        Files.copy(path, zs);
                        zs.closeEntry();
                    } catch (IOException e) {
                        System.err.println("❌ Error zipping file: " + path + " => " + e.getMessage());
                    }
                });
        }
    }

    private boolean isValidUserID(String userID) {
        return userID.matches("^[a-zA-Z0-9]+$");
    }

    private void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteRecursive(child);
            }
        }
        file.delete();
    }
}
